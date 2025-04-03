package com.example.fixzy_ketnoikythuatvien.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fixzy_ketnoikythuatvien.data.model.SyncUserRequest
import com.example.fixzy_ketnoikythuatvien.data.repository.UserApiRepository
import kotlinx.coroutines.launch

//lopwr mở rộng ViewModel
class UserApiViewModel : ViewModel() {
    //kởi tạo repository để thực hiện các thao tác giao tiếp với api
    private val repository = UserApiRepository()
//    đồng bộ thông tin ngời dùng lên server qua api
    fun syncUserInfo(uid: String, email: String, fullName: String?, phone: String?) {
        viewModelScope.launch {
            try {
                //gửi yêu cầu đôg bộ thông tin ngươ dùng lên server
                val response = repository.syncUser(
                    SyncUserRequest(uid, email, fullName, phone)
                )
//                xử lí phản hồi từ api
                if (response.isSuccessful) {
//                    trường hợp thành công
                    Log.d("SYNC_USER", "✅ ${response.body()?.get("message")}")
                } else {
                    Log.e("SYNC_USER", "❌ Lỗi API: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SYNC_USER", "❌ Exception: ${e.message}")
            }
        }
    }
}
