package com.example.fixzy_ketnoikythuatvien.data.remote

import com.example.fixzy_ketnoikythuatvien.data.model.SyncUserRequest
import com.example.fixzy_ketnoikythuatvien.data.model.TestApiResponse
import com.example.fixzy_ketnoikythuatvien.data.model.TestData
import com.example.fixzy_ketnoikythuatvien.data.model.UpdateCountRequest
import com.example.fixzy_ketnoikythuatvien.data.model.UpdateCountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class TestItem(val id: Int, val count: Int) // Match your DB columns


interface ApiService {
    @POST("/sync-user")
    suspend fun syncUser(@Body request: SyncUserRequest): Response<Map<String, String>>

    @GET("test")  // ✅ Matches Express route `/api/test`
    suspend fun getTestData(): List<TestItem>
}
