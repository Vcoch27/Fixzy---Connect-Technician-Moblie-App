package com.example.fixzy_ketnoikythuatvien.service.model

// ServiceSchedule.kt
data class ServiceSchedule(
    val schedule_id: Int,
    val service_id: Int,
    val day_of_week: String, // "Monday", "Tuesday", ...
    val date: String,        // "2024-06-10"
    val start_time: String,  // "08:00"
    val end_time: String,    // "12:00"
    val status: String,      // "AVAILABLE" or "BUSY"
    val created_at: String   // "2024-06-01 10:00:00"
)

