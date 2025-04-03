package com.example.fixzy_ketnoikythuatvien.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET

data class UpdateCountRequest(val id: Int)
data class ApiResponse(val success: Boolean)
interface TestApiService {
    @POST("test/update")
    suspend fun updateCount(@Body request: UpdateCountRequest): ApiResponse
}
