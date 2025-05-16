package com.example.fixzy_ketnoikythuatvien.service.model

import com.example.fixzy_ketnoikythuatvien.data.model.NotificationData
import com.google.gson.annotations.SerializedName

data class GetNotificationsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("notifications") val notifications: List<Notification>?,
)

data class Notification (
    @SerializedName("notification_id") val notificationId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("deeplink") val deeplink: String?,
    @SerializedName("is_read") val isRead: Int,
    @SerializedName("created_at") val createdAt: String
)
/*
*  {
      "notification_id": 7,
      "receiver_id": 7,
      "title": "Đặt lịch thành công",
      "content": "Bạn đã đặt lịch dịch vụ \"Sơn chống thấm\" thành công. Đơn hàng đang chờ xác nhận từ nhà cung cấp.",
      "deeplink": null,
      "is_read": 1,
      "created_at": "2025-05-15T03:28:22.000Z"
    },
* */