package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.CategoryResponse
import com.example.fixzy_ketnoikythuatvien.service.model.GetNotificationsResponse
import com.example.fixzy_ketnoikythuatvien.service.model.RegistrationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationService {
        private val apiService = ApiClient.apiService
        private val store = Store.Companion.store

        fun getNotifications(userId: Int) {
                Log.d("Notifications", "Fetching notifications for userId: $userId")
                apiService.getNotifications(userId = userId).enqueue(object : Callback<GetNotificationsResponse> {
                        override fun onResponse(
                                call: Call<GetNotificationsResponse>,
                                response: Response<GetNotificationsResponse>
                        ) {
                                Log.d("Notifications", "Response received. isSuccessful=${response.isSuccessful}")
                                if (response.isSuccessful) {
                                        val notificationsResponse = response.body()
                                        Log.d("Notifications", "Response body: $notificationsResponse")
                                        if (notificationsResponse?.success == true) {
                                                Log.d("Notifications", "Successfully fetched notifications: ${notificationsResponse.notifications}")
                                                store.dispatch(
                                                        Action.GetNotificationsSuccess(
                                                                notifications = notificationsResponse.notifications ?: emptyList()
                                                        )
                                                )
                                        } else {
                                                Log.w("Notifications", "API returned success=false or null notifications")
                                                store.dispatch(
                                                        Action.GetNotificationsFailure(
                                                                error = "Failed to fetch notifications"
                                                        )
                                                )
                                        }
                                } else {
                                        Log.e("Notifications", "Server error: ${response.code()} ${response.message()}")
                                        store.dispatch(
                                                Action.GetNotificationsFailure(
                                                        error = "Server error: ${response.code()}"
                                                )
                                        )
                                }
                        }

                        override fun onFailure(call: Call<GetNotificationsResponse>, t: Throwable) {
                                Log.e("Notifications", "Network error: ${t.message}", t)
                                store.dispatch(
                                        Action.GetNotificationsFailure(
                                                error = t.message ?: "Network error"
                                        )
                                )
                        }
                })
        }

}