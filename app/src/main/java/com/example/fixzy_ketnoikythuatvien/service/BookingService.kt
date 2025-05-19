package com.example.fixzy_ketnoikythuatvien.service

import android.content.Context
import android.net.Uri
import retrofit2.Call
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.data_class.AppState
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.Booking
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingResponse
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.service.model.GetBookingsResponse
import com.example.fixzy_ketnoikythuatvien.service.model.GetProviderBookingsResponse
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
        rating: Int? = 0,
        feedback: String? = "no feedback",
        feedback_url: String? = null
    ): Boolean {
        Log.d(TAG, "Starting updateBookingStatus for bookingId=$bookingId, status=$status, rating=$rating, feedback=$feedback, feedback_url=$feedback_url")
        store.dispatch(Action.UpdateBookingStatus)
        Log.d(TAG, "Dispatched UpdateBookingStatus action for bookingId=$bookingId")

        try {
            // Kiểm tra giá trị đầu vào
            if (bookingId <= 0) {
                Log.e(TAG, "Validation failed: Invalid bookingId=$bookingId")
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
                Log.e(TAG, "Validation failed: Invalid status=$status, valid statuses: ${validStatuses.joinToString(", ")}")
                store.dispatch(
                    Action.UpdateBookingStatusFailure(
                        "Trạng thái không hợp lệ. Các trạng thái hợp lệ: ${
                            validStatuses.joinToString(", ")
                        }."
                    )
                )
                return false
            }

            val state = store.getState()
            Log.d(TAG, "Retrieved store state for bookingId=$bookingId")
            val user = state.user
            if (user == null) {
                Log.e(TAG, "Authentication failed: No user logged in")
                store.dispatch(Action.UpdateBookingStatusFailure("Vui lòng đăng nhập để cập nhật trạng thái."))
                return false
            }

            val userId = user.id
            val role = user.role
            if (userId == null || role == null) {
                Log.e(TAG, "Validation failed: Invalid user data - userId=$userId, role=$role")
                store.dispatch(Action.UpdateBookingStatusFailure("Thông tin người dùng không hợp lệ."))
                return false
            }

            Log.d(
                TAG,
                "Preparing API request with userId=$userId, role=$role, bookingId=$bookingId, status=$status"
            )
            val response: Response<ApiService.StatusUpdateResponse> =
                if (status == "Completed") {
                    Log.d(TAG, "Sending update with feedback: rating=$rating, feedback=$feedback, feedback_url=$feedback_url")
                    apiService.updateBookingStatus(
                        bookingId,
                        ApiService.StatusUpdateRequest(status, userId, role),
                        rating,
                        feedback,
                        feedback_url
                    )
                } else {
                    Log.d(TAG, "Sending update without feedback for status=$status")
                    apiService.updateBookingStatus(
                        bookingId,
                        ApiService.StatusUpdateRequest(status, userId, role)
                    )
                }
            if (response.isSuccessful && response.body()?.success == true) {
                val message = response.body()?.message ?: "Cập nhật trạng thái thành công."
                Log.d(TAG, "Booking status updated successfully: $message, responseCode=${response.code()}")
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
                    "Update failed: HTTP ${response.code()}, message=$errorMessage, errorBody=$errorBody"
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
            Log.e(TAG, "HTTP exception caught: code=${e.code()}, message=$errorMessage, errorBody=$errorBody", e)
            store.dispatch(Action.UpdateBookingStatusFailure(errorMessage))
            return false
        } catch (e: IOException) {
            Log.e(TAG, "Network error occurred while updating booking status", e)
            store.dispatch(Action.UpdateBookingStatusFailure("Lỗi kết nối mạng. Vui lòng kiểm tra lại."))
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error occurred: message=${e.message}", e)
            store.dispatch(Action.UpdateBookingStatusFailure("Đã xảy ra lỗi không xác định: ${e.message}"))
            return false
        }
    }

    suspend fun uploadFeedbackImage(context: Context, imageUri: Uri): String =
        suspendCancellableCoroutine { continuation ->
            Log.d(TAG, "Initiating feedback image upload for URI=$imageUri")
            MediaManager.get()
                .upload(imageUri)
                .option("folder", "fixzy/feedback")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {
                        Log.d(TAG, "Upload started: requestId=$requestId, URI=$imageUri")
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        Log.d(TAG, "Upload progress: requestId=$requestId, $bytes/$totalBytes bytes uploaded")
                    }

                    override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                        Log.d(TAG, "Upload successful: requestId=$requestId, result=$resultData")
                        val url = resultData["secure_url"] as? String
                        if (url != null) {
                            Log.d(TAG, "Image uploaded successfully, secure_url=$url")
                            continuation.resume(url) { cause, _, _ ->
                                Log.e(TAG, "Continuation resumption error: ${cause?.message}", cause)
                            }
                        } else {
                            Log.e(TAG, "Upload failed: No secure_url in result data=$resultData")
                            continuation.resumeWithException(IOException("No secure_url in result"))
                        }
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Log.e(TAG, "Upload error: requestId=$requestId, error=${error?.description}")
                        continuation.resumeWithException(IOException("Upload error: ${error?.description}"))
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        Log.w(TAG, "Upload rescheduled: requestId=$requestId, error=${error?.description}")
                    }
                })
                .dispatch()
            Log.d(TAG, "Upload dispatched for URI=$imageUri")
        }

    suspend fun getSummaryStatus() {
        Log.d(TAG, "Starting getSummaryStatus")
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Attempting to get Firebase token")
                val idToken = getFirebaseToken()
                    ?: throw Exception("Không thể lấy token: Người dùng chưa đăng nhập")
                Log.d(TAG, "Firebase token obtained successfully")

                try {
                    Log.d(TAG, "Sending API request for summary status with token=Bearer $idToken")
                    val response = apiService.getSummaryStatus("Bearer $idToken").execute()

                    if (response.isSuccessful) {
                        response.body()?.let { summaryResponse ->
                            Log.d(TAG, "Summary status API call successful: response=$summaryResponse")
                            store.dispatch(
                                Action.GetSummaryStatusSuccess(
                                    todayBookings = summaryResponse.todayBookings ?: emptyList(),
                                    needAction = summaryResponse.needAction ?: emptyList()
                                )
                            )
                            Log.d(TAG, "Dispatched GetSummaryStatusSuccess with ${summaryResponse.todayBookings?.size} bookings")
                        } ?: run {
                            Log.e(TAG, "Summary status API call returned null body")
                            store.dispatch(Action.GetSummaryStatusFailure("Không có dữ liệu"))
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "Summary status API call failed: HTTP ${response.code()}, errorBody=$errorBody")
                        store.dispatch(
                            Action.GetSummaryStatusFailure("Lỗi: ${errorBody ?: response.message()}")
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Exception during summary status API call: ${e.message}", e)
                    store.dispatch(Action.GetSummaryStatusFailure("Lỗi kết nối: ${e.message}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get Firebase token: ${e.message}", e)
                store.dispatch(Action.GetSummaryStatusFailure("Lỗi: ${e.message}"))
            }
        }
        Log.d(TAG, "Completed getSummaryStatus execution")
    }

    fun getProviderBookings() {
        val userId = Store.stateFlow.value.user?.id
        if (userId == null) {
            Log.w("GetProviderBookings", "User ID is null, cannot fetch bookings")
            store.dispatch(Action.GetProviderBookingFailure("Người dùng chưa đăng nhập"))
            return
        }

        apiService.getProviderBookings(userId).enqueue(object : Callback<GetProviderBookingsResponse> {
            override fun onResponse(
                call: Call<GetProviderBookingsResponse>,
                response: Response<GetProviderBookingsResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            store.dispatch(Action.GetProviderBookingSuccess(it.data))
                        } else {
                            store.dispatch(Action.GetProviderBookingFailure("Không có dữ liệu"))
                        }
                    } ?: run {
                        store.dispatch(Action.GetProviderBookingFailure("Dữ liệu trả về rỗng"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("GetProviderBookings", "API error: $errorBody")
                    store.dispatch(Action.GetProviderBookingFailure("Lỗi: ${errorBody ?: response.message()}"))
                }
            }

            override fun onFailure(call: Call<GetProviderBookingsResponse>, t: Throwable) {
                Log.e("GetProviderBookings", "Network error: ${t.message}", t)
                store.dispatch(Action.GetProviderBookingFailure("Lỗi: ${t.message ?: "Không thể kết nối"}"))
            }
        })
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

