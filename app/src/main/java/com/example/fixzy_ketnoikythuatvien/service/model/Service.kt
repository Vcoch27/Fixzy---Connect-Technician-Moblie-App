package com.example.fixzy_ketnoikythuatvien.service.model

import com.google.gson.annotations.SerializedName

data class CreateServiceRequest(
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("name") val serviceName: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("price") val price: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("schedules") val schedules: List<Schedule>? = null
)

data class Schedule(
    @SerializedName("day_of_week") val dayOfWeek: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String
)
data class CreateServiceResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("service_id") val serviceId: Int? = null
)
