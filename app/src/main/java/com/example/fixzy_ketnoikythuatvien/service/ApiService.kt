package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.BuildConfig
import com.example.fixzy_ketnoikythuatvien.data.model.SyncUserRequest
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.service.model.CategoryResponse
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceResponse
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnician
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnicianResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class TestItem(val id: Int, val count: Int) // Match your DB columns


object ApiClient {
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}


interface ApiService {
    @POST("user/sync-user")
    fun syncUser(@Body userData: UserData): Call<ResponseBody>

    @GET("api/test")
    fun getTestData(): Call<List<TestItem>>//đối tượng chứa danh sách các TestItem từ API
    //Call là interface đại diện cho một HTTP request vó thể thực thi, nó hoạt đôgn như một promise/futer

    @POST("user/auth")
    fun authenticate(@Header("Authorization") token: String): Call<UserData>

    @GET("/user/top-technicians")
    suspend fun getTopTechnicians(
        @Query("categoryID") categoryID: String? = null,
    ): TopTechnicianResponse

    @GET("/categories")
    fun getCategories(): Call<CategoryResponse>

    @GET("service/category/{categoryId}")
    suspend fun getServicesByCategory(@Path("categoryId") categoryId: Int): ServiceResponse
}
