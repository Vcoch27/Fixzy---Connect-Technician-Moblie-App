package com.example.fixzy_ketnoikythuatvien.service.model

data class AvailabilityResponse(
    val success: Boolean,
    val data: List<Availability> // Sửa từ 'availability' thành 'data'
)

data class Availability(
    val availability_id: Int,
    val service_id: Int,
    val day_of_week: String,
    val date: String,
    val start_time: String,
    val end_time: String,
    val status: String,
    val created_at: String
)