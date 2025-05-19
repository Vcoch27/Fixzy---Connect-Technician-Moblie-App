package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.AddScheduleRequest
import com.example.fixzy_ketnoikythuatvien.service.model.CreateServiceRequest
import com.example.fixzy_ketnoikythuatvien.service.model.GetServiceInformationResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


private const val TAG = "ServiceService"

class ServiceService {
    private val apiService = ApiClient.apiService
    private val store = Store.store
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun getModeService(idService: Int) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("ServiceService", "Bắt đầu lấy token Firebase...")
                val idToken = getFirebaseToken()
                    ?: throw Exception("Không thể lấy token: Người dùng chưa đăng nhập")
                Log.d("ServiceService", "Token đã lấy thành công: $idToken")

                try {
                    Log.d("ServiceService", "Gọi API getModeService với idService = $idService")
                    val call = apiService.getModeService("Bearer $idToken", idService)
                    val response = call.execute()
                    Log.d("ServiceService", "Đã nhận được phản hồi từ API")

                    if (response.isSuccessful) {
                        Log.d("ServiceService", "API trả về thành công: ${response.code()}")
                        response.body()?.let {
                            Log.d("ServiceService", "Response body: $it")
                            store.dispatch(Action.GetModeServiceSuccess(it))
                        } ?: run {
                            Log.e("ServiceService", "Empty response body")
                            store.dispatch(Action.GetModeServiceFailure("Không thể lấy thông tin dịch vụ: Phản hồi rỗng"))
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ServiceService", "API call failed: ${response.code()} - $errorBody")
                        store.dispatch(Action.GetModeServiceFailure("Lỗi: ${errorBody ?: response.message()}"))
                    }
                } catch (e: Exception) {
                    Log.e("ServiceService", "Ngoại lệ trong khi gọi API: ${e.message}", e)
                    store.dispatch(Action.GetModeServiceFailure("Lỗi kết nối: ${e.message}"))
                }
            } catch (e: Exception) {
                Log.e(
                    "ServiceService",
                    "Ngoại lệ trong quá trình lấy token hoặc gọi API: ${e.message}",
                    e
                )
                store.dispatch(Action.GetModeServiceFailure("Lỗi: ${e.message}"))
            }
        }
    }

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

    suspend fun addSchedule(serviceId: Int, body: AddScheduleRequest) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("CreateService", "Dispatching loading action")
                store.dispatch(Action.AddScheduleLoading())
                val idToken = getFirebaseToken()
                    ?: throw Exception("Không thể lấy token: Người dùng chưa đăng nhập")
                Log.d("CreateService", "Calling API with token")
                try {
                    val response =
                        apiService.addSchedule("Bearer $idToken", serviceId, body).execute()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            store.dispatch(Action.AddScheduleSuccess("tao lich thanh cong"))
                        } ?: run {
                            store.dispatch(Action.AddScheduleFailure("Không thể tạo dịch vụ: Phản hồi rỗng"))
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        store.dispatch(Action.AddScheduleFailure("Lỗi: ${errorBody ?: response.message()}"))
                    }
                } catch (e: Exception) {
                    store.dispatch(Action.AddScheduleFailure("Lỗi kết nối: ${e.message}"))
                }
            } catch (e: Exception) {
                store.dispatch(Action.AddScheduleFailure("Lỗi: ${e.message}"))
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

    suspend fun fetchServices(categoryId: Int) {
        Log.i(TAG, "=== fetchServices() STARTED for categoryId=$categoryId ===")
        store.dispatch(Action.FetchServicesRequest)
        try {
            val response = apiService.getServicesByCategory(categoryId)
            Log.e(TAG, "fetching services: $response")
            if (response.success) {
                store.dispatch(Action.FetchServicesSuccess(response.data))
                Log.e(TAG, "fetching services:have")
            } else {
                store.dispatch(Action.FetchServicesSuccess(emptyList()))
                Log.e(TAG, "fetching services:null")
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchServices ERROR", e)
            store.dispatch(Action.FetchServicesFailure("Error: ${e.message ?: "Unknown error"}"))
        }
    }

    //use
    /*
    @GET("service/information/{serviceId}")
    fun getServiceInformation(
        @Path("serviceId") serviceId: Int
    ):Call<GetServiceInformationResponse>
    and
     is Action.getServiceInformationSuccess -> {
                    state.copy(
                        selectedServiceInformation = action.service,
                        isLoading = false,
                        error = null
                    )
                }
                is Action.getServiceInformationFailure -> {
                    state.copy(
                        isLoading = false,
                        error = action.error
                    )
                }
    * */

     fun getServiceInformation(serviceId: Int) {
        apiService.getServiceInformation(serviceId).enqueue(object: Callback<GetServiceInformationResponse> {
            override fun onResponse(
                call: Call<GetServiceInformationResponse>,
                response: Response<GetServiceInformationResponse>
            ){
                if(response.isSuccessful){
                    response.body()?.let {
                        if(it.success){
                            Log.d("ServiceService", "fetch service tc: $serviceId, detail: ${it.data}")
                            store.dispatch(Action.getServiceInformationSuccess(it.data, it.reviews))
                        }else{
                            store.dispatch(Action.getServiceInformationFailure("Error when fetching service information"))
                        }
                    }
                }else{
                    val errorBody = response.errorBody()?.string()
                    store.dispatch(Action.getServiceInformationFailure("API call failed: ${response.code()} - $errorBody"))
                }
            }
            override  fun onFailure(call: Call<GetServiceInformationResponse>, t: Throwable){
                store.dispatch(Action.getServiceInformationFailure("API call failed: ${t.message}"))
            }
        })
    }
}