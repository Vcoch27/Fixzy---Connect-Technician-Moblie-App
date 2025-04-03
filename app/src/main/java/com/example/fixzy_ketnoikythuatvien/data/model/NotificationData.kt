package com.example.fixzy_ketnoikythuatvien.data.model

import com.example.fixzy_ketnoikythuatvien.R

data class NotificationData(
    val imageRes: Int,
    val title: String,
    val message: String,
    val time: String,
    var isNew: Boolean
)

val notificationList = listOf(
    NotificationData(R.drawable.coc, "Add Booking", "New Booking Added by Amit Pandey", "2 min ago", true),
    NotificationData(R.drawable.coc, "Payment Received", "Payment received from John Doe", "10 min ago", false),
    NotificationData(R.drawable.coc, "Service Completed", "Your service request has been completed", "1 hour ago", false)
)
