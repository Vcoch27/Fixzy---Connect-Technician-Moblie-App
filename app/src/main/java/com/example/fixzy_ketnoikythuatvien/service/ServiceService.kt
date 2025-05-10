package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.CreateServiceRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreateServiceResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Response
import javax.security.auth.callback.Callback
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "ServiceService"
class ServiceService {
    private val apiService = ApiClient.apiService
    private val store = Store.store
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun createService(body: CreateServiceRequest) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("CreateService", "Dispatching loading action")
                store.dispatch(Action.CreateServiceLoading)

                Log.d("CreateService", "Getting Firebase token")
                val idToken = getFirebaseToken()
                    ?: throw Exception("Không thể lấy token: Người dùng chưa đăng nhập")

                Log.d("CreateService", "Calling API with token")
                try {
                    val response = apiService.createService("Bearer $idToken", body).execute()
                    
                    if (response.isSuccessful) {
                        Log.d("CreateService", "API call successful")
                        response.body()?.let {
                            Log.d("CreateService", "Dispatching success action")
                            store.dispatch(Action.CreateServiceSuccess("Tạo dịch vụ thành công"))
                        } ?: run {
                            Log.e("CreateService", "Empty response body")
                            store.dispatch(Action.CreateServiceFailure("Không thể tạo dịch vụ: Phản hồi rỗng"))
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("CreateService", "API call failed: ${response.code()} - $errorBody")
                        store.dispatch(Action.CreateServiceFailure("Lỗi: ${errorBody ?: response.message()}"))
                    }
                } catch (e: Exception) {
                    Log.e("CreateService", "API call exception: ${e.message}", e)
                    store.dispatch(Action.CreateServiceFailure("Lỗi kết nối: ${e.message}"))
                }
            } catch (e: Exception) {
                Log.e("CreateService", "Exception caught: ${e.message}", e)
                store.dispatch(Action.CreateServiceFailure("Lỗi: ${e.message}"))
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
                Log.d("CreateService", "Firebase token retrieved: ${token?.take(10)}...") // Chỉ log vài ký tự
                continuation.resume(token)
            } else {
                Log.e("CreateService", "Failed to get token", task.exception)
                continuation.resumeWithException(task.exception ?: Exception("Không thể lấy token"))
            }
        }
    }


    suspend fun fetchServices(categoryId: Int) {
        Log.i(TAG, "=== fetchServices() STARTED for categoryId=$categoryId ===")
        store.dispatch(Action.FetchServicesRequest)
        Log.d(TAG, "Dispatch: FetchServicesRequest")

        try {
            val response = apiService.getServicesByCategory(categoryId)
            Log.d(
                TAG,
                "API response: success=${response.success}, data size=${response.data?.size ?: 0}"
            )
            response.data.forEach { service ->
                Log.d(TAG, "Service: name=${service.name}, providerName=${service.providerName}")
            }

            if (response.success) {
                Log.i(TAG, "Services fetched successfully: ${response.data.size} items")
                store.dispatch(Action.FetchServicesSuccess(response.data))
                Log.d(TAG, "Dispatch: FetchServicesSuccess")
            } else {
                store.dispatch(Action.FetchServicesSuccess(emptyList()))
                Log.d(TAG, "Dispatch: FetchServicesSuccess with empty list")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching services", e)
            store.dispatch(Action.FetchServicesFailure("Error: ${e.message}"))
        }
    }


}