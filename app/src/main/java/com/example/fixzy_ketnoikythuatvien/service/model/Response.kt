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

data class TopTechnicianResponse(
    val success: Boolean,
    val data: List<TopTechnicianDto>
)

data class TopTechnicianDto(
    val technician_id: Int,
    val full_name: String,
    val avatar_url: String,
    val service_id: Int,
    val service_name: String,
    val service_price: String,
    val service_rating: Double,
    val service_orders_completed: Int,
    val category_id: Int,
    val category_name: String
)
data class TopTechnician(
    val id: Int,
    val name: String,
    val avatarUrl: String,
    val serviceName: String,
    val price: String,
    val rating: Double,
    val completedOrders: Int,
    val categoryName: String,
    val categoryId: Int // Add this field
)