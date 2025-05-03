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

private const val TAG = "response"

class ProviderService {
    private val apiService = ApiClient.apiService
    private val store = Store.store

    suspend fun fetchProviderDetails(providerId: Int) {
        Log.i(TAG, "Fetching provider details for providerId=$providerId")
        store.dispatch(Action.FetchProviderRequest)
        try {
            val response = apiService.getProviderDetails(providerId)
            if (response.isSuccessful && response.body()?.success == true) {
                val apiData = response.body()!!
// Thêm validation
                if (apiData.technician == null) {
                    Log.e(TAG, "Technician data is null")
                    store.dispatch(Action.FetchProviderFailure("Technician data not found"))
                    return
                }
                // Kiểm tra null và cung cấp giá trị mặc định
                val technician = apiData.technician
                val providerData = ProviderData(
                    name = technician.full_name ?: "Unknown Technician", // Giá trị mặc định
                    job = technician.role ?: "Technician",
                    rating = technician.rating,
                    avatar_url = technician.avatar_url ?: "",
                    ordersCompleted = technician.orders_completed ?: 0,
                    experience = "${technician.years_of_experience ?: 0} Years",
                    bio = technician.bio ?: "",
                    skills = listOf("Sink", "Shower", "Boiler", "Toilet"),
                    services = apiData.services.map {
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

                Log.i(TAG, "Provider data fetched successfully: ${providerData.name}")
                store.dispatch(Action.FetchProviderSuccess(providerData))
            } else {
                Log.e(TAG, "Failed to load provider data: ${response.message()}")
                store.dispatch(Action.FetchProviderFailure("Failed to load provider data"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching provider data: ${e.message}", e)
            store.dispatch(Action.FetchProviderFailure("Error: ${e.message ?: "Unknown error"}"))
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchAvailability(serviceId: Int) {
        Log.i(TAG, "Fetching availability for serviceId=$serviceId")
        store.dispatch(Action.FetchAvailabilityRequest)

        try {
            val response = apiService.getAvailability(serviceId)
            Log.i(TAG, "API Response: $response")
            if (response.isSuccessful && response.body()?.success == true) {
                val apiData = response.body()!!
                val availabilityList = apiData.data ?: emptyList()
                Log.i(TAG, "Raw availability data: $availabilityList")

                val currentDate = LocalDate.now()
                val twoWeeksLater = currentDate.plusWeeks(2)
                val filteredAvailability = availabilityList.filter {
                    val date = LocalDate.parse(it.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    val isWithinRange = date.isBefore(twoWeeksLater) && date.isAfter(currentDate.minusDays(1))
                    Log.i(TAG, "Checking date $date: isWithinRange=$isWithinRange")
                    isWithinRange
                }
                Log.i(TAG, "Fetched ${filteredAvailability.size} availability items after filtering")

                store.dispatch(Action.FetchAvailabilitySuccess(filteredAvailability))
                Log.i(TAG, "Availability data dispatched successfully")
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Log.e(TAG, "Failed to load availability: $errorMsg")
                store.dispatch(Action.FetchAvailabilityFailure("Failed to load availability: $errorMsg"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching availability", e)
            store.dispatch(Action.FetchAvailabilityFailure("Error: ${e.message ?: "Unknown error"}"))
        }
    }
}