package com.example.fixzy_ketnoikythuatvien.ui.screen.controller

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.UserService
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnicianResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "TopTechniciansController"

object TopTechniciansController {
    private val userService = UserService()
    private var isFetching = false

    suspend fun fetchTechnicians(categoryId: Int? = null): TopTechnicianResponse {
        if (isFetching) {
            Log.w(TAG, "Đang thực hiện fetch cho categoryId=$categoryId, bỏ qua")
            return TopTechnicianResponse(success = false, data = emptyList())
        }
        isFetching = true
        return try {
            Log.d(TAG, "Bắt đầu lấy kỹ thuật viên: categoryId=$categoryId")
            val response = withContext(Dispatchers.IO) {
                userService.fetchTopTechnicians(categoryId?.toString())
            }
            Log.d(TAG, "Nhận được kỹ thuật viên: số lượng=${response.data.size}, chi tiết=${response.data}")
            isFetching = false
            userService.dispatch(Action.FetchTopTechnicians(categoryId?.toString())) { action ->
                Store.store.dispatch(action)
                Log.d(TAG, "Đã gửi action tới Store: $action")
            }
            response
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi lấy kỹ thuật viên: ${e.message}", e)
            isFetching = false
            userService.dispatch(Action.TopTechniciansLoadFailed(e.message ?: "Lỗi không xác định")) { action ->
                Store.store.dispatch(action)
                Log.d(TAG, "Đã gửi action lỗi tới Store: $action")
            }
            TopTechnicianResponse(success = false, data = emptyList())
        }
    }
}