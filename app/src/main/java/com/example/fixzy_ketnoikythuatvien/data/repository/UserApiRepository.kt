package com.example.fixzy_ketnoikythuatvien.data.repository

import com.example.fixzy_ketnoikythuatvien.data.model.SyncUserRequest
import com.example.fixzy_ketnoikythuatvien.data.remote.RetrofitInstance

class UserApiRepository {
    suspend fun syncUser(request: SyncUserRequest) = RetrofitInstance.api.syncUser(request)
}