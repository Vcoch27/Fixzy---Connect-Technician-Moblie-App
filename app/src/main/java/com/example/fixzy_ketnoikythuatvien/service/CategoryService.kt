package com.example.fixzy_ketnoikythuatvien.service

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.CategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryService {
    private val TAG = "CATEGORY_SERVICE"
    private val apiService = ApiClient.apiService
    private val store = Store.Companion.store

    fun fetchCategories() {
        Log.i(TAG, "=== fetchCategories() STARTED ===")
        store.dispatch(Action.FetchCategoriesRequest)
        Log.d(TAG, "Dispatch: FetchCategoriesRequest")

        apiService.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                Log.i(TAG, "API onResponse called")
                Log.d(TAG, "Response code: ${response.code()}")
                Log.d(TAG, "Response success: ${response.isSuccessful}")
                Log.d(TAG, "Raw response: $response")

                if (response.isSuccessful) {
                    val categoryResponse = response.body()
                    Log.d(TAG, "Parsed body: $categoryResponse")

                    if (categoryResponse?.success == true) {
                        Log.i(TAG, "Categories fetched successfully: ${categoryResponse.data.size} items")

                        val colors = listOf(
                            Color(0xFFFFE8AD),
                            Color(0xFFFC8EA7),
                            Color(0xFF969CEC),
                            Color(0xFFA1D6E2)
                        )

                        val categories = categoryResponse.data.mapIndexed { index, apiCategory ->
                            Log.d(TAG, "Mapping category: ${apiCategory.name}")
                            CategoryData(
                                categoryId = apiCategory.category_id,
                                name = apiCategory.name,
                                iconUrl = apiCategory.icon_url,
                                backgroundColor = colors[index % colors.size]
                            )
                        }

                        Log.d(TAG, "Mapped categories: $categories")
                        store.dispatch(Action.FetchCategoriesSuccess(categories))
                        Log.d(TAG, "Dispatch: FetchCategoriesSuccess")
                    } else {
                        Log.e(TAG, "API returned success: false or data is null")
                        store.dispatch(Action.FetchCategoriesFailure("API returned success: false"))
                    }
                } else {
                    Log.e(TAG, "HTTP error: ${response.code()} - ${response.message()}")
                    store.dispatch(Action.FetchCategoriesFailure("HTTP error: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.e(TAG, "Network call failed: ${t.message}", t)
                store.dispatch(Action.FetchCategoriesFailure("Network error: ${t.message ?: "Unknown error"}"))
            }
        })
    }
}
