package com.example.fixzy_ketnoikythuatvien.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.fixzy_ketnoikythuatvien.BuildConfig
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

val url = BuildConfig.BASE_URL
object NotificationHelper {
    private const val CHANNEL_ID = "booking_channel"
    private const val CHANNEL_NAME = "Booking Notifications"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for booking updates"
                // setSound(null, null) // Uncomment if you don't want sound
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showBookingSuccessNotification(context: Context, referenceCode: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Đặt lịch thành công")
            .setContentText("Mã đặt lịch: $referenceCode")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            // Use a unique ID for booking success notifications, e.g., a constant base ID
            notificationManager.notify(1001, notification)
        }
    }

    fun showNotification(title: String, message: String, context: Context, notificationId: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            // Use the provided notificationId to ensure uniqueness
            notificationManager.notify(notificationId, notification)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun startNotificationPolling(context: Context) {
    Log.d("NotificationPolling", "Bắt đầu kiểm tra thông báo")
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            val user = Store.stateFlow.value.user
            Log.d("NotificationPolling", "userId: ${user?.id}")
            if (user?.id != null) {
                Log.d("NotificationPolling", "Bắt đầu kiểm tra thông báo cho userId = ${user.id}")
                try {
                    val apiUrl = "${url}notifications/${user.id}/unread"

                    val response = URL(apiUrl).readText()
                    Log.d("NotificationPolling", "Phản hồi API: $response")

                    val notifications = JSONArray(response)
                    Log.d("NotificationPolling", "Số lượng thông báo chưa đọc: ${notifications.length()}")

                    for (i in 0 until notifications.length()) {
                        val noti = notifications.getJSONObject(i)
                        val title = noti.getString("title")
                        val content = noti.getString("content")
                        val id = noti.getInt("notification_id")

                        Log.d("NotificationPolling", "Hiển thị thông báo: $title - $content")

                        withContext(Dispatchers.Main) {
                            NotificationHelper.showNotification(title, content, context, id)
                        }

                        delay(1500)

                        try {
                            val markReadUrl = "${url}notifications/$id/read"
                            Log.d("NotificationPolling", "Gọi API đánh dấu đã đọc: $markReadUrl")

                            val conn = URL(markReadUrl).openConnection() as HttpURLConnection
                            conn.requestMethod = "PATCH"
                            conn.doOutput = true
                            conn.connect()
                            val readResponse = conn.inputStream.bufferedReader().readText()
                            Log.d("NotificationPolling", "Phản hồi đánh dấu đã đọc: $readResponse")
                        } catch (e: Exception) {
                            Log.e("NotificationPolling", "Lỗi khi đánh dấu đã đọc", e)
                        }
                    }

                } catch (e: Exception) {
                    Log.e("NotificationPolling", "Lỗi khi kiểm tra thông báo", e)
                }
            } else {
                Log.d("NotificationPolling", "Chưa có user, bỏ qua lần kiểm tra")
            }

            delay(15000)
        }
    }
}