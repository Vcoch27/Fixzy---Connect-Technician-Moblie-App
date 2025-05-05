package com.example.fixzy_ketnoikythuatvien.service

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderData
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceDetail
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
                        val isWithinRange = date.isBefore(twoWeeksLater) && date.isAfter(currentDate.minusDays(1))
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
}

