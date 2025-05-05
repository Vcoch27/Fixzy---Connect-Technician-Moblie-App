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
) {
    fun isComplete(): Boolean {

        return userId != null && serviceId != null && availabilityId != null && address != null && phone != null
    }
    fun toRequestBody(): Map<String, Any?> {
        return mapOf(
            "service_id" to serviceId,
            "availability_id" to availabilityId,
            "address" to address,
            "phone" to phone,
            "notes" to notes
        )
    }
}

data class CreateBookingResponse(
    val success: Boolean,
    val message: String?,
    val reference_code: String?
)