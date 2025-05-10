package com.example.fixzy_ketnoikythuatvien.service.model

import com.google.gson.annotations.SerializedName


data class ProviderResponse(
    val success: Boolean,
    val technician: Technician,
    val services: List<ServiceDetail>
)

data class Technician(
    val technician_id: Int,
    val full_name: String,
    val avatar_url: String?="",
    val phone: String,
    val address: String,
    val email: String,
    val role: String,
    val rating: Double,
    val orders_completed: Int,
    val years_of_experience: Int,
    val bio: String
)

data class ServiceDetail(
    val service_id: Int,
    val service_name: String,
    val service_price: String,
    val service_rating: Double,
    val service_orders_completed: Int,
    val category_id: Int,
    val category_name: String
)

data class ProviderData(
    val name: String = "Unknown Technician",
    val job: String = "Technician",
    val rating: Double,
    val avatar_url: String? ="",
    val ordersCompleted: Int,
    val experience: String,
    val bio: String,
    val skills: List<String>,
    val services: List<ServiceDetail>
)
//-----------
