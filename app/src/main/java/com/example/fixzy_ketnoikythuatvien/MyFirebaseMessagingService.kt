package com.example.fixzy_ketnoikythuatvien

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Xử lý thông báo nhận được
    }

    override fun onNewToken(token: String) {
        // Gửi token lên server để lưu trữ
    }
}