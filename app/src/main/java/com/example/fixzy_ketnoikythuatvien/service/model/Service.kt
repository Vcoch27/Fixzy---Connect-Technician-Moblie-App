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

data class GetModeServiceResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("service") val service: ModeService,
    @SerializedName("schedules") val schedules: List<ModeSchedule>,
    @SerializedName("bookings") val bookings: List<ModeBooking>
)

data class ModeService(
    @SerializedName("service_id") val serviceId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("orders_completed") val ordersCompleted: Int,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("category_name") val categoryName: String
)

data class ModeSchedule(
    @SerializedName("schedule_id") val scheduleId: Int,
    @SerializedName("day_of_week") val dayOfWeek: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String
)

data class ModeBooking(
    @SerializedName("booking_id") val bookingId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("service_id") val serviceId: Int,
    @SerializedName("booking_date") val bookingDate: String,
    @SerializedName("booking_time") val bookingTime: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("status") val status: String,
    @SerializedName("reference_code") val referenceCode: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("availability_id") val availabilityId: Int,
    @SerializedName("day_of_week") val dayOfWeek: String,
    @SerializedName("date") val date: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String
)

data class GetServiceInformationResponse(
    val success: Boolean,
    val data: ModeService,
    val reviews: List<Review>
)

/*
* {
      {
      "review_id": 5,
      "booking_id": 47,
      "reference_code": "BOOK-2BC737DC",
      "full_name": "Bích Hồng Trần thị",
      "avatar_url": "https://lh3.googleusercontent.com/a/ACg8ocI4SoUExtyvZBuDfj3FNp0SGItanYRLrFmniGe7XblcuM3CQw=s96-c",
      "rating": "5.0",
      "feedback": "oke",
      "feedback_url": "https://res.cloudinary.com/dlkrskgwq/image/upload/v1747640953/fixzy/feedback/npff7grxgtdkmeojlz55.png",
      "booking_date": "2025-05-23T00:00:00.000Z",
      "booking_time": "10:23:00",
      "created_at": "2025-05-19T14:49:15.000Z"
    },
    * */
data class Review(
    @SerializedName("review_id") val reviewId: Int,
    @SerializedName("booking_id") val bookingId: Int,
    @SerializedName("reference_code") val referenceCode: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("rating") val rating: Double,
    @SerializedName("feedback") val feedback: String?,
    @SerializedName("feedback_url") val feedbackUrl: String?,
    @SerializedName("booking_date") val bookingDate: String,
    @SerializedName("booking_time") val bookingTime: String,
    @SerializedName("created_at") val createdAt: String
)

