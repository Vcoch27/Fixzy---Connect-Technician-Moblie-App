package com.example.fixzy_ketnoikythuatvien.service

import retrofit2.Call
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.data_class.AppState
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.Booking
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingResponse
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.service.model.GetBookingsResponse
import retrofit2.Response
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "BOOKING_SERVICE"

class BookingService {
    private val apiService = ApiClient.apiService
    private val store = Store.store
    private val firebaseAuth = Firebase.auth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun createBooking(
        serviceId: Int?,
        availabilityId: Int?,
        address: String,
        phone: String,
        notes: String?,
    ) {
        if (serviceId == null || availabilityId == null) {
            Log.e(TAG, "Missing serviceId or availabilityId")
            store.dispatch(Action.CreateBookingFailure("Missing service or availability information"))
            return
        }
        if (address.isBlank() || phone.isBlank() || phone.length < 10) {
            Log.e(TAG, "Invalid form data - address: $address, phone: $phone")
            store.dispatch(Action.CreateBookingFailure("Address and valid phone number are required"))
            return
        }

        store.dispatch(Action.ResetBookingState)
        store.dispatch(Action.StartCreatingBooking)

        val requestBody = CreateBookingRequest(
            service_id = serviceId,
            availability_id = availabilityId,
            address = address,
            phone = phone,
            notes = notes
        )
        Log.d(TAG, "Request body prepared: $requestBody")

        firebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (!task.isSuccessful || task.result?.token == null) {
                Log.e(TAG, "Firebase authentication failed: ${task.exception?.message}")
                store.dispatch(Action.CreateBookingFailure("Authentication failed"))
                return@addOnCompleteListener
            }

            val token = "Bearer ${task.result.token}"
            Log.d(TAG, "Firebase token acquired successfully: ${token.take(10)}...")

            try {
                apiService.createBooking(token, requestBody)
                    .enqueue(object : Callback<CreateBookingResponse> {
                        override fun onResponse(
                            call: Call<CreateBookingResponse>,
                            response: Response<CreateBookingResponse>,
                        ) {
                            Log.d(
                                TAG,
                                "API response received: code=${response.code()}, success=${response.isSuccessful}"
                            )
                            if (response.isSuccessful) {
                                response.body()?.let { body ->
                                    Log.d(TAG, "API response body: $body")
                                    if (body.success && body.reference_code != null) {
                                        Log.d(
                                            TAG,
                                            "Booking successful with reference code: ${body.reference_code}"
                                        )
                                        store.dispatch(Action.CreateBookingSuccess(body.reference_code))
                                    } else {
                                        Log.w(
                                            TAG,
                                            "Booking failed from server logic: ${body.message}"
                                        )
                                        store.dispatch(
                                            Action.CreateBookingFailure(
                                                body.message ?: "Booking failed"
                                            )
                                        )
                                    }
                                } ?: run {
                                    Log.e(TAG, "Response body is null")
                                    store.dispatch(Action.CreateBookingFailure("Response body is null"))
                                }
                            } else {
                                val errorMessage = response.errorBody()?.string() ?: "Server error"
                                Log.e(TAG, "API error response: $errorMessage")
                                store.dispatch(Action.CreateBookingFailure(errorMessage))
                            }
                        }

                        override fun onFailure(call: Call<CreateBookingResponse>, t: Throwable) {
                            Log.e(TAG, "API request failed: ${t.message}", t)
                            store.dispatch(Action.CreateBookingFailure("Connection error: ${t.message}"))
                        }
                    })
            } catch (e: Exception) {
                Log.e(TAG, "Error initiating API call: ${e.message}", e)
                store.dispatch(Action.CreateBookingFailure("Failed to initiate booking: ${e.message}"))
            }
        }?.addOnFailureListener {
            Log.e(TAG, "Firebase token request failed: ${it.message}", it)
            store.dispatch(Action.CreateBookingFailure("Firebase error: ${it.message}"))
        }
    }

    fun getBookingsForUser() {
        // Dispatch action FetchBookings để báo hiệu bắt đầu lấy dữ liệu
        store.dispatch(Action.FetchBookings)

        // Lấy token từ Firebase
        firebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (!task.isSuccessful || task.result?.token == null) {
                val errorMessage = "Firebase authentication failed: ${task.exception?.message}"
                Log.e(TAG, errorMessage)
                store.dispatch(Action.FetchBookingsFailure(errorMessage))
                return@addOnCompleteListener
            }

            val token = "Bearer ${task.result.token}"
            Log.d(TAG, "Firebase token acquired successfully: ${token.take(10)}...")

            // Gọi API để lấy danh sách bookings
            try {
                apiService.getBookingsForUser(token)
                    .enqueue(object : Callback<GetBookingsResponse> {
                        override fun onResponse(
                            call: Call<GetBookingsResponse>,
                            response: Response<GetBookingsResponse>,
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                val bookings = responseBody?.data ?: emptyList()
                                Log.d(TAG, "Fetched ${bookings.size} bookings successfully")
                                store.dispatch(Action.FetchBookingsSuccess(bookings))
                            } else {
                                val errorMessage =
                                    "API error: ${response.code()} - ${response.message()}"
                                Log.e(TAG, errorMessage)
                                store.dispatch(Action.FetchBookingsFailure(errorMessage))
                            }
                        }

                        override fun onFailure(call: Call<GetBookingsResponse>, t: Throwable) {
                            val errorMessage = "Network error: ${t.message}"
                            Log.e(TAG, errorMessage, t)
                            store.dispatch(Action.FetchBookingsFailure(errorMessage))
                        }
                    })
            } catch (e: Exception) {
                val errorMessage = "Error initiating API call: ${e.message}"
                Log.e(TAG, errorMessage, e)
                store.dispatch(Action.FetchBookingsFailure(errorMessage))
            }
        }?.addOnFailureListener { exception ->
            val errorMessage = "Firebase token request failed: ${exception.message}"
            Log.e(TAG, errorMessage, exception)
            store.dispatch(Action.FetchBookingsFailure(errorMessage))
        }
    }

    suspend fun updateBookingStatus(
        bookingId: Int,
        status: String,
        rating: Int?=0,
        feedback: String?="no feedback",
    ): Boolean {
        store.dispatch(Action.UpdateBookingStatus)
        Log.d(TAG, "Updating booking status for bookingId=$bookingId, status=$status")

        try {
            // Kiểm tra giá trị đầu vào
            if (bookingId <= 0) {
                Log.e(TAG, "Invalid bookingId: $bookingId")
                store.dispatch(Action.UpdateBookingStatusFailure("ID đặt lịch không hợp lệ."))
                return false
            }

            val validStatuses = listOf(
                "Pending",
                "Confirmed",
                "Completed",
                "Cancelled",
                "WaitingForCustomerConfirmation"
            )
            if (status !in validStatuses) {
                Log.e(TAG, "Invalid status: $status")
                store.dispatch(
                    Action.UpdateBookingStatusFailure(
                        "Trạng thái không hợp lệ. Các trạng thái hợp lệ: ${
                            validStatuses.joinToString(
                                ", "
                            )
                        }."
                    )
                )
                return false
            }

            val state = store.getState()
            val user = state.user
            if (user == null) {
                Log.e(TAG, "User not logged in")
                store.dispatch(Action.UpdateBookingStatusFailure("Vui lòng đăng nhập để cập nhật trạng thái."))
                return false
            }

            val userId = user.id
            val role = user.role
            if (userId == null || role == null) {
                Log.e(TAG, "Invalid user data: userId=$userId, role=$role")
                store.dispatch(Action.UpdateBookingStatusFailure("Thông tin người dùng không hợp lệ."))
                return false
            }

            Log.d(
                TAG,
                "Sending request with userId=$userId, role=$role, bookingId=$bookingId, status=$status"
            )
            val response: Response<ApiService.StatusUpdateResponse> = if (status == "Completed" && role == "user") {
                apiService.updateBookingStatus(
                    bookingId,
                    ApiService.StatusUpdateRequest(status, userId, role),
                    rating,
                    feedback
                )
            } else {
                apiService.updateBookingStatus(
                    bookingId,
                    ApiService.StatusUpdateRequest(status, userId, role)
                )
            }
            if (response.isSuccessful && response.body()?.success == true) {
                val message = response.body()?.message ?: "Cập nhật trạng thái thành công."
                Log.d(TAG, "Booking status updated successfully: $message")
                store.dispatch(Action.UpdateBookingStatusSuccess(message))
                return true
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val errorResponse =
                        Gson().fromJson(errorBody, ApiService.StatusUpdateResponse::class.java)
                    when (response.code()) {
                        400 -> errorResponse.message ?: "Yêu cầu không hợp lệ."
                        401 -> errorResponse.message ?: "Không xác thực được người dùng."
                        403 -> errorResponse.message
                            ?: "Bạn không có quyền thực hiện hành động này."

                        else -> errorResponse.message ?: "Lỗi server khi cập nhật trạng thái."
                    }
                } catch (e: Exception) {
                    errorBody ?: "Lỗi server khi cập nhật trạng thái."
                }
                Log.e(
                    TAG,
                    "Failed to update booking status: HTTP ${response.code()}, message=$errorMessage"
                )
                store.dispatch(Action.UpdateBookingStatusFailure(errorMessage))
                return false
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                val errorResponse =
                    Gson().fromJson(errorBody, ApiService.StatusUpdateResponse::class.java)
                when (e.code()) {
                    400 -> errorResponse.message ?: "Yêu cầu không hợp lệ."
                    401 -> errorResponse.message ?: "Không xác thực được người dùng."
                    403 -> errorResponse.message ?: "Bạn không có quyền thực hiện hành động này."
                    else -> errorResponse.message ?: "Lỗi server khi cập nhật trạng thái."
                }
            } catch (ex: Exception) {
                errorBody ?: "Lỗi server khi cập nhật trạng thái."
            }
            Log.e(TAG, "HTTP error updating booking status: $errorMessage", e)
            store.dispatch(Action.UpdateBookingStatusFailure(errorMessage))
            return false
        } catch (e: IOException) {
            Log.e(TAG, "Network error updating booking status", e)
            store.dispatch(Action.UpdateBookingStatusFailure("Lỗi kết nối mạng. Vui lòng kiểm tra lại."))
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error updating booking status", e)
            store.dispatch(Action.UpdateBookingStatusFailure("Đã xảy ra lỗi không xác định: ${e.message}"))
            return false
        }
    }

    suspend fun getSummaryStatus() {
        withContext(Dispatchers.IO) {
            try {
                val idToken = getFirebaseToken()
                    ?: throw Exception("Không thể lấy token: Người dùng chưa đăng nhập")

                try {
                    val response = apiService.getSummaryStatus("Bearer $idToken").execute()

                    if (response.isSuccessful) {
                        response.body()?.let { summaryResponse ->
                            Log.d(TAG, "Summary response received: $summaryResponse")
                            store.dispatch(
                                Action.GetSummaryStatusSuccess(
                                    todayBookings = summaryResponse.todayBookings ?: emptyList(),
                                    needAction = summaryResponse.needAction ?: emptyList()
                                )
                            )
                        } ?: run {
                            store.dispatch(Action.GetSummaryStatusFailure("Không có dữ liệu"))
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        store.dispatch(
                            Action.GetSummaryStatusFailure("Lỗi: ${errorBody ?: response.message()}")
                        )
                    }
                } catch (e: Exception) {
                    store.dispatch(Action.GetSummaryStatusFailure("Lỗi kết nối: ${e.message}"))
                }
            } catch (e: Exception) {
                store.dispatch(Action.GetSummaryStatusFailure("Lỗi: ${e.message}"))
            }
        }
    }

    private suspend fun getFirebaseToken(): String? = suspendCancellableCoroutine { continuation ->
        val user = auth.currentUser
        if (user == null) {
            Log.e("CreateService", "Firebase user is null")
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        user.getIdToken(false).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result?.token
                Log.d(
                    "CreateService",
                    "Firebase token retrieved: ${token?.take(10)}..."
                ) // Chỉ log vài ký tự
                continuation.resume(token)
            } else {
                Log.e("CreateService", "Failed to get token", task.exception)
                continuation.resumeWithException(task.exception ?: Exception("Không thể lấy token"))
            }
        }
    }

}

