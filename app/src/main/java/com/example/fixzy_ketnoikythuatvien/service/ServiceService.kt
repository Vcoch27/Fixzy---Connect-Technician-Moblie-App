package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store

private const val TAG = "ServiceService"

class ServiceService {
    private val apiService = ApiClient.apiService
    private val store = Store.store

    suspend fun fetchServices(categoryId: Int) {
        Log.i(TAG, "=== fetchServices() STARTED for categoryId=$categoryId ===")
        store.dispatch(Action.FetchServicesRequest)
        Log.d(TAG, "Dispatch: FetchServicesRequest")

        try {
            val response = apiService.getServicesByCategory(categoryId)
            Log.d(TAG, "API response: success=${response.success}, data size=${response.data?.size ?: 0}")

            if (response.success) {
                Log.i(TAG, "Services fetched successfully: ${response.data.size} items")
                store.dispatch(Action.FetchServicesSuccess(response.data))
                Log.d(TAG, "Dispatch: FetchServicesSuccess")
            } else {
                store.dispatch(Action.FetchServicesSuccess(emptyList()))
                Log.d(TAG, "Dispatch: FetchServicesSuccess with empty list")
            }
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) {
                Log.w(TAG, "No services found for categoryId=$categoryId, treating as empty result")
                store.dispatch(Action.FetchServicesSuccess(emptyList()))
            } else {
                Log.e(TAG, "Network call failed: ${e.message}", e)
                store.dispatch(Action.FetchServicesFailure("Network error: ${e.message ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network call failed: ${e.message}", e)
            store.dispatch(Action.FetchServicesFailure("Network error: ${e.message ?: "Unknown error"}"))
        }
    }
}