package com.example.fixzy_ketnoikythuatvien.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Thêm TestApiService
    val testApi: TestApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")  // Có thể là URL khác nếu cần
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TestApiService::class.java)
    }
}
