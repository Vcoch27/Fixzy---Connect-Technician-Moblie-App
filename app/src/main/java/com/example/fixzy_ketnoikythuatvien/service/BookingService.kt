package com.example.fixzy_ketnoikythuatvien.service

import retrofit2.Call
import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.Booking
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingResponse
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

        val requestBody = mutableMapOf<String, Any>(
            "service_id" to serviceId,
            "availability_id" to availabilityId,
            "address" to address,
            "phone" to phone
        )
        notes?.let { requestBody["notes"] = it }

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
}


//private fun sendBookingRequest(token: String, requestBody: Map<String, Any?>) {
//    Log.d(TAG, "Sending booking request with token: ${token.take(10)}...")
//
//    apiService.createBooking(token, requestBody).enqueue(
//        object : Callback<CreateBookingResponse> {
//            override fun onResponse(
//                call: Call<CreateBookingResponse>,
//                response: Response<CreateBookingResponse>
//            ) {
//                try {
//                    when {
//                        response.isSuccessful -> {
//                            response.body()?.let { body ->
//                                if (body.success && body.reference_code != null) {
//                                    Log.d(TAG, "Booking created: ${body.reference_code}")
//                                    store.dispatch(Action.CreateBookingSuccess(body.reference_code))
//                                } else {
//                                    handleError(body.message)
//                                }
//                            } ?: handleError("Response body is null")
//                        }
//                        else -> {
//                            val errorBody = response.errorBody()?.string()
//                            Log.e(TAG, "Server error: $errorBody")
//                            store.dispatch(
//                                Action.CreateBookingFailure(
//                                    errorBody ?: "Server error: ${response.code()}"
//                                )
//                            )
//                        }
//                    }
//                } catch (e: Exception) {
//                    Log.e(TAG, "Response parsing error", e)
//                    store.dispatch(Action.CreateBookingFailure("Data processing error"))
//                }
//            }
//
//            override fun onFailure(call: Call<CreateBookingResponse>, t: Throwable) {
//                Log.e(TAG, "API call failed", t)
//                store.dispatch(
//                    Action.CreateBookingFailure(
//                        "Connection error: ${t.message ?: "Unknown error"}"
//                    )
//                )
//            }
//
//            private fun handleError(message: String?) {
//                Log.e(TAG, "Booking creation failed: $message")
//                store.dispatch(
//                    Action.CreateBookingFailure(
//                        message ?: "Unknown error occurred"
//                    )
//                )
//            }
//        }
//    )
//}