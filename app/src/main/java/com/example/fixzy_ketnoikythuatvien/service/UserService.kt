package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.CategoryResponse
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnician
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnicianResponse
import retrofit2.Call
private const val TAG = "UserService"

class UserService {
    private val apiService = ApiClient.apiService
    private val store = Store.store

    suspend fun fetchTopTechnicians(categoryId: String? = null): TopTechnicianResponse {
        Log.d(TAG, "Bắt đầu lấy kỹ thuật viên với categoryId: $categoryId")
        val response = apiService.getTopTechnicians(categoryId)
        Log.d(TAG, "Nhận được phản hồi: số lượng=${response.data.size}, chi tiết=${response.data}")
        return response
    }

    suspend fun dispatch(action: Action, dispatch: (Action) -> Unit) {
        Log.d(TAG, "Xử lý action: $action")
        when (action) {
            is Action.FetchTopTechnicians -> {
                Log.d(TAG, "Xử lý FetchTopTechnicians với categoryId: ${action.categoryId}")
                try {
                    val response = fetchTopTechnicians(action.categoryId)
                    val technicians = response.data.map { dto ->
                        Log.d(TAG, "Ánh xạ kỹ thuật viên: id=${dto.technician_id}, name=${dto.full_name}")
                        TopTechnician(
                            id = dto.technician_id,
                            name = dto.full_name ?: "Kỹ thuật viên không xác định", 
                            avatarUrl = dto.avatar_url ?: "", // Giá trị mặc định
                            serviceName = dto.service_name,
                            price = dto.service_price,
                            rating = dto.service_rating,
                            completedOrders = dto.service_orders_completed,
                            categoryName = dto.category_name,
                            categoryId = dto.category_id
                        )
                    }
                    Log.i(TAG, "Đã ánh xạ ${technicians.size} kỹ thuật viên: ${technicians.map { it.name }}")
                    dispatch(Action.TopTechniciansLoaded(technicians))
                    Log.i(TAG, "Đã gửi Action.TopTechniciansLoaded")
                } catch (e: Exception) {
                    Log.e(TAG, "Lỗi khi lấy kỹ thuật viên: ${e.message}", e)
                    dispatch(Action.TopTechniciansLoadFailed(e.message ?: "Lỗi không xác định"))
                    Log.i(TAG, "Đã gửi Action.TopTechniciansLoadFailed")
                }
            }
            else -> {
                Log.w(TAG, "Action không được xử lý: $action")
            }
        }
    }
}