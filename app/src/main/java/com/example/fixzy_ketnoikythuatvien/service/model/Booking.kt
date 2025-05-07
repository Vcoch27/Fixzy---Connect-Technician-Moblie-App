package com.example.fixzy_ketnoikythuatvien.service.model

data class Booking(
    var userId: Int? = null,
    var serviceId: Int? = null,
    var serviceName: String? = null,
    var availabilityId: Int? = null,
    var date: String? = null,
    var startTime: String? = null,
    var duration: Int? = null, // in minutes
    var address: String? = null,
    var phone: String? = null,
    var notes: String? = null,
    var totalPrice: Double? = null,
    var status: String = "Pending"
)

data class CreateBookingRequest(
    val service_id: Int,
    val availability_id: Int,
    val address: String,
    val phone: String,
    val notes: String? = null
)

data class CreateBookingResponse(
    val success: Boolean,
    val message: String?,
    val reference_code: String?
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
    val icon_url: String
)
data class GetBookingsResponse(
    val success: Boolean,
    val data: List<DetailBooking>
)


