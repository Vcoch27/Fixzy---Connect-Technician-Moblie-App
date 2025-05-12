package com.example.fixzy_ketnoikythuatvien.service.model

import com.google.gson.annotations.SerializedName

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

data class AddScheduleRequest(
    @SerializedName("day_of_week") val dayOfWeek: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String
)
data class AddScheduleResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("schedule_id") val scheduleId: Int? = null
        )