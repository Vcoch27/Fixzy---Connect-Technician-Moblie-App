package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.BuildConfig
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Service {
    private var BASE_URL = BuildConfig.BASE_URL
    private var TAG = "DATA_RESPONSE"
    private var store = Store.Companion.store;
    fun getTest() {
        //khời tạo Retrofit
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
        Log.i(TAG, "On Fail?????")
        //gọi API để lấy dữ liệu và xử lý kết quả
        api.getTestData().enqueue(object : Callback<List<TestItem>> {
            override fun onResponse(call: Call<List<TestItem>>, response: Response<List<TestItem>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.i(TAG, "API response body: $body") // 👈 log dữ liệu trước khi dispatch
                    if (body != null) {
                        store.dispatch(Action.getTest(body))
                        Log.i(TAG, "Dispatched to store")
                    }
                } else {
                    Log.e(TAG, "API Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<TestItem>>, t: Throwable) {
                Log.i(TAG, "On Fail: ${t.message}")
            }
        })


    }

}
