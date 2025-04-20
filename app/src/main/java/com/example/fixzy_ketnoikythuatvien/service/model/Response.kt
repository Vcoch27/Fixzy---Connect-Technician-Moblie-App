package com.example.fixzy_ketnoikythuatvien.service.model

data class ApiCategory(
    val category_id: Int,
    val name: String,
    val icon_url: String,
    val created_at: String
)
data class CategoryResponse(
    val success: Boolean,
    val data: List<ApiCategory>
)