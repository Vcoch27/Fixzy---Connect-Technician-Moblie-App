package com.example.fixzy_ketnoikythuatvien.service.model

import com.google.gson.annotations.SerializedName

data class Booking(
    var userId: Int,
    var serviceId: Int,
    var serviceName: String,
    var availabilityId: Int,
    var date: String,
    var startTime: String,
    var duration: Int,
    var address: String,
    var phone: String,
    var notes: String? = null,
    var totalPrice: Double,
    var status: String = "Pending",
)
// Booking.kt
//data class Booking(
//    val booking_id: Int,
//    val user_id: Int,
//    val service_id: Int,
//    val booking_date: String, // "2024-06-10"
//    val booking_time: String, // "09:00"
//    val address: String,
//    val status: String,       // "Pending", "Confirmed", "Completed", "Cancelled"
//    val total_price: Double,
//    val reference_code: String,
//    val created_at: String,
//    val updated_at: String,
//    val availability_id: Int,
//    val notes: String?,
//    val customerName: String // Thêm trường này để hiển thị tên khách (không có trong bảng gốc, dùng cho UI mock)
//)

data class CreateBookingRequest(
    val service_id: Int,
    val availability_id: Int,
    val address: String,
    val phone: String,
    val notes: String? = null,
)

data class CreateBookingResponse(
    val success: Boolean,
    val message: String?,
    val reference_code: String?,
)

data class DetailBooking(
    val booking_id: Int,
    val user_id: Int,
    val service_id: Int,
    val booking_date: String,
    val booking_time: String,
    val address: String,
    val phone: String,
    val status: String,
    val total_price: String,
    val reference_code: String,
    val created_at: String,
    val updated_at: String,
    val availability_id: Int,
    val notes: String,
    val service_name: String,
    val provider_name: String?,
    val icon_url: String,
)

data class GetBookingsResponse(
    val success: Boolean,
    val data: List<DetailBooking>,
)


data class GetSummaryStatusResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("todayBookings") val todayBookings: List<SummaryBooking>?,
    @SerializedName("needAction") val needAction: List<SummaryBooking>?,
)

data class SummaryBooking(
    @SerializedName("booking_id") val bookingId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("service_id") val serviceId: Int,
    @SerializedName("booking_date") val bookingDate: String,
    @SerializedName("booking_time") val bookingTime: String,
    @SerializedName("address") val address: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("status") val status: String,
    @SerializedName("total_price") val totalPrice: String,
    @SerializedName("reference_code") val referenceCode: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("availability_id") val availabilityId: Int,
    @SerializedName("notes") val notes: String?,
)

/*
* {
      "booking_id": 40,
      "user_id": 7,
      "service_id": 17,
      "availability_id": 51,
      "booking_date": "2025-05-24T17:00:00.000Z",
      "booking_time": "09:30:00",
      "address": "352 Mai dang chon",
      "phone": "07023451263",
      "status": "Pending",
      "total_price": "600000.00",
      "reference_code": "BOOK-DA795DD2",
      "notes": "toi muon ahaha",
      "created_at": "2025-05-15T03:25:00.000Z",
      "updated_at": "2025-05-15T03:25:00.000Z",
      "full_name": "abc@gmail.com",
      "email": "abc@gmail.com"
    }
* */
data class GetProviderBookingsResponse(
    val success: Boolean,
    val data: List<ProviderBooking>
)
data class ProviderBooking(
    @SerializedName("booking_id") val bookingId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("service_id") val serviceId: Int,
    @SerializedName("availability_id") val availabilityId: Int,
    @SerializedName("booking_date") val bookingDate: String,
    @SerializedName("booking_time") val bookingTime: String,
    @SerializedName("address") val address: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("status") val status: String,
    @SerializedName("total_price") val totalPrice: String,
    @SerializedName("reference_code") val referenceCode: String,
    @SerializedName("notes") val notes: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String,
)


