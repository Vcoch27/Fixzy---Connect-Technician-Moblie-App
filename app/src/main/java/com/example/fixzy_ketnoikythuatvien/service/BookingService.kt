package com.example.fixzy_ketnoikythuatvien.service

import retrofit2.Call
import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.Booking
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingResponse
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.service.model.GetBookingsResponse
import retrofit2.Response
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import retrofit2.Callback

private const val TAG = "BOOKING_SERVICE"

class BookingService {
    private val apiService = ApiClient.apiService
    private val store = Store.store
    private val firebaseAuth = Firebase.auth

    fun createBooking(
        serviceId: Int?,
        availabilityId: Int?,
        address: String,
        phone: String,
        notes: String?
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

        store.dispatch(Action.ResetBookingState) // Reset trước khi bắt đầu
        store.dispatch(Action.StartCreatingBooking) // Đặt isCreatingBooking = true

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
                apiService.createBooking(token, requestBody).enqueue(object : Callback<CreateBookingResponse> {
                    override fun onResponse(call: Call<CreateBookingResponse>, response: Response<CreateBookingResponse>) {
                        Log.d(TAG, "API response received: code=${response.code()}, success=${response.isSuccessful}")
                        if (response.isSuccessful) {
                            response.body()?.let { body ->
                                Log.d(TAG, "API response body: $body")
                                if (body.success && body.reference_code != null) {
                                    Log.d(TAG, "Booking successful with reference code: ${body.reference_code}")
                                    store.dispatch(Action.CreateBookingSuccess(body.reference_code))
                                } else {
                                    Log.w(TAG, "Booking failed from server logic: ${body.message}")
                                    store.dispatch(Action.CreateBookingFailure(body.message ?: "Booking failed"))
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
                apiService.getBookingsForUser(token).enqueue(object : Callback<GetBookingsResponse> {
                    override fun onResponse(call: Call<GetBookingsResponse>, response: Response<GetBookingsResponse>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val bookings = responseBody?.data ?: emptyList()
                            Log.d(TAG, "Fetched ${bookings.size} bookings successfully")
                            store.dispatch(Action.FetchBookingsSuccess(bookings))
                        } else {
                            val errorMessage = "API error: ${response.code()} - ${response.message()}"
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
}

