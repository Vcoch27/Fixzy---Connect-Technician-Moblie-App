package com.example.fixzy_ketnoikythuatvien.service

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.CreateBookingResponse
import com.example.fixzy_ketnoikythuatvien.service.model.CreatePaymentRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreatePaymentResponse
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderData
import com.example.fixzy_ketnoikythuatvien.service.model.RegisterProviderRequest
import com.example.fixzy_ketnoikythuatvien.service.model.RegisterProviderResponse
import com.example.fixzy_ketnoikythuatvien.service.model.RegistrationResponse
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceDetail
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

private const val TAG = "ProviderService"

class ProviderService {
    private val apiService = ApiClient.apiService
    private val store = Store.store

    suspend fun fetchProviderDetails(providerId: Int) {
        Log.i(TAG, "---- START fetchProviderDetails ----")
        Log.i(TAG, "Fetching provider details for providerId=$providerId")
        store.dispatch(Action.FetchProviderRequest)

        try {
            val response = apiService.getProviderDetails(providerId)
            Log.i(TAG, "API response received: isSuccessful=${response.isSuccessful}")

            if (response.isSuccessful && response.body()?.success == true) {
                val apiData = response.body()!!
                Log.d(TAG, "API data body: $apiData")

                if (apiData.technician == null) {
                    Log.e(TAG, "Technician data is null")
                    store.dispatch(Action.FetchProviderFailure("Technician data not found"))
                    return
                }

                val technician = apiData.technician
                Log.d(TAG, "Technician: $technician")

                val providerData = ProviderData(
                    name = technician.full_name ?: "Unknown Technician",
                    job = technician.role ?: "Technician",
                    rating = technician.rating,
                    avatar_url = technician.avatar_url ?: "",
                    ordersCompleted = technician.orders_completed ?: 0,
                    experience = "${technician.years_of_experience ?: 0} Years",
                    bio = technician.bio ?: "",
                    skills = listOf("Sink", "Shower", "Boiler", "Toilet"),
                    services = apiData.services.map {
                        Log.d(TAG, "Service: $it")
                        ServiceDetail(
                            service_id = it.service_id,
                            service_name = it.service_name ?: "Unknown Service",
                            service_price = it.service_price,
                            service_rating = it.service_rating,
                            service_orders_completed = it.service_orders_completed ?: 0,
                            category_id = it.category_id,
                            category_name = it.category_name ?: "Uncategorized"
                        )
                    }
                )

                Log.i(TAG, "Provider data built successfully: $providerData")
                store.dispatch(Action.FetchProviderSuccess(providerData))
            } else {
                val msg = response.message()
                Log.e(TAG, "Failed to load provider data: $msg")
                store.dispatch(Action.FetchProviderFailure("Failed to load provider data"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in fetchProviderDetails: ${e.message}", e)
            store.dispatch(Action.FetchProviderFailure("Error: ${e.message ?: "Unknown error"}"))
        }

        Log.i(TAG, "---- END fetchProviderDetails ----")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchAvailability(serviceId: Int) {
        Log.i(TAG, "---- START fetchAvailability ----")
        Log.i(TAG, "Fetching availability for serviceId=$serviceId")
        store.dispatch(Action.FetchAvailabilityRequest)

        try {
            val response = apiService.getAvailability(serviceId)
            Log.i(TAG, "API response received: isSuccessful=${response.isSuccessful}")

            if (response.isSuccessful && response.body()?.success == true) {
                val apiData = response.body()!!
                val availabilityList = apiData.data ?: emptyList()
                Log.i(TAG, "Raw availability data count: ${availabilityList.size}")

                val currentDate = LocalDate.now()
                val twoWeeksLater = currentDate.plusWeeks(2)

                val filteredAvailability = availabilityList.filter {
                    try {
                        val date = LocalDate.parse(it.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        val isWithinRange =
                            date.isBefore(twoWeeksLater) && date.isAfter(currentDate.minusDays(1))
                        isWithinRange
                    } catch (e: Exception) {
                        Log.e(TAG, "Date parsing error: ${it.date}", e)
                        false
                    }
                }

                Log.i(TAG, "Filtered availability count: ${filteredAvailability.size}")
                store.dispatch(Action.FetchAvailabilitySuccess(filteredAvailability))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Log.e(TAG, "Failed to load availability: $errorMsg")
                store.dispatch(Action.FetchAvailabilityFailure("Failed to load availability: $errorMsg"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in fetchAvailability: ${e.message}", e)
            store.dispatch(Action.FetchAvailabilityFailure("Error: ${e.message ?: "Unknown error"}"))
        }

        Log.i(TAG, "end fetchAvailability ")
    }

    fun registerProvider(userId: Int, yearsOfExperience: Int, bio: String, certificateUrl: String) {
        store.dispatch(Action.RegisterProviderLoading)

        val request = RegisterProviderRequest(
            userId = userId,
            yearsOfExperience = yearsOfExperience,
            bio = bio,
            certificateUrl = certificateUrl
        )

        apiService.registerProvider(request).enqueue(object : Callback<RegisterProviderResponse> {
            override fun onResponse(
                call: Call<RegisterProviderResponse>,
                response: Response<RegisterProviderResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        store.dispatch(Action.RegisterProviderSuccess(body.message))
                    } else {
                        store.dispatch(Action.RegisterProviderFailure(body.message))
                    }
                } else {
                    store.dispatch(Action.RegisterProviderFailure("Failed to register provider"))
                }
            }

            override fun onFailure(call: Call<RegisterProviderResponse>, t: Throwable) {
                Log.e(TAG, "registerProvider failed: ${t.message}", t)
                store.dispatch(Action.RegisterProviderFailure(t.message ?: "Unknown error"))
            }
        })
    }


    fun getRegistration(userId: Int?) {
        if (userId == null) {
            store.dispatch(Action.GetRegistrationFailure("User ID is null"))
            return
        }

        Log.d(TAG, "Fetching registration for userId: $userId")

        apiService.getRegistration(userId).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success && body.data != null) {
                        store.dispatch(Action.GetRegistrationSuccess(body.data))
                    } else {
                        store.dispatch(Action.GetRegistrationFailure(body.error ?: "Unknown error"))
                    }
                } else {
                    store.dispatch(Action.GetRegistrationFailure("Failed to fetch registration"))
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Log.e(TAG, "getRegistration failed: ${t.message}", t)
                store.dispatch(Action.GetRegistrationFailure(t.message ?: "Unknown error"))
            }
        })
    }

    // Payment creation function
    fun createPayment(id: Int, userId: Int?) {

        Log.d(TAG, "createPayment called with id=$id, userId=$userId")

        if (userId == 0 || userId == null) {
            Log.w(TAG, "User ID is null or zero")
            store.dispatch(Action.GetRegistrationFailure("ID is null"))
            return
        }

        val embedData = Json.encodeToString(
            mapOf(
                "registration_id" to id.toString(),
                "user_id" to userId.toString(),
                "type" to "provider_registration",
                "redirecturl" to "fixzy://payment-callback"
            )
        )

        Log.d(TAG, "Embed data: $embedData")

        val request = CreatePaymentRequest(
            userId = userId,
            embedData = embedData
        )

        Log.d(TAG, "Sending payment request for user $userId")

        apiService.createPayment(id, request).enqueue(object : Callback<CreatePaymentResponse> {
            override fun onResponse(
                call: Call<CreatePaymentResponse>,
                response: Response<CreatePaymentResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    Log.d(TAG, "Response success: $body")

                    if (body.success && body.order_url != null) {
                        Log.i(TAG, "Payment created. Order URL: ${body.order_url}")
                        store.dispatch(
                            Action.CreatePaymentSuccess(
                                body.order_url,
                                body.app_trans_id
                            )
                        )
                    } else {
                        Log.e(TAG, "Payment creation failed: ${body.error}")
                        store.dispatch(Action.CreatePaymentFailure(body.error ?: "Unknown error"))
                    }
                } else {
                    Log.e(TAG, "Response unsuccessful or body is null")
                    store.dispatch(Action.CreatePaymentFailure("Unknown error"))
                }
            }

            override fun onFailure(call: Call<CreatePaymentResponse>, t: Throwable) {
                Log.e(TAG, "API call failed: ${t.message}")
                store.dispatch(Action.CreatePaymentFailure(t.message ?: "Unknown error"))
            }
        })
    }


}

