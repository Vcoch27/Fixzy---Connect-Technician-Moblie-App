package com.example.fixzy_ketnoikythuatvien.data.model

enum class OrderStatus { CONFIRMED, DONE, NOT_BOOKED }

data class OrderData(
    val serviceName: String,
    val referenceCode: String,
    val status: OrderStatus,
    val schedule: String
)
val orderList = listOf(
    OrderData("AC Installation", "#D-571224", OrderStatus.CONFIRMED, "8:00-9:00 AM, 09 Dec"),
    OrderData("AC Installation", "#D-571225", OrderStatus.NOT_BOOKED, "10:00-11:00 AM, 10 Dec"),
    OrderData("AC Installation", "#D-571226", OrderStatus.DONE, "2:00-3:00 PM, 11 Dec")
)