package com.example.fixzy_ketnoikythuatvien.ui.screen.controller

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.service.CategoryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "CategoryController"

object CategoryController {
    private val categoryService = CategoryService()

    suspend fun fetchCategories() {
        Log.d(TAG, "Gọi CategoryService.fetchCategories()")
        withContext(Dispatchers.IO) {
            categoryService.fetchCategories()
            Log.d(TAG, "Đã gọi fetchCategories, CategoryService sẽ gửi action tới Store")
        }
    }
}