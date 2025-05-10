package com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.data.model.OrderData
import com.example.fixzy_ketnoikythuatvien.data.model.OrderStatus
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

val TAG = "OrderItemCard"
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun OrderItemCard(booking: DetailBooking,
                  onBookingUpdated: (DetailBooking) -> Unit) {
    val scope = rememberCoroutineScope()
    val bookingService = remember { BookingService() }

    var isCancelLoading by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    Log.d("OrderItemCard", "Rendering OrderItemCard for booking: ${booking.reference_code}")
    val currentTime = LocalDateTime.now()
    Log.d("OrderItemCard", "Current time: $currentTime")
    val bookingDateTime = try {
        LocalDateTime.parse("${booking.booking_date}T${booking.booking_time}")
    } catch (e: Exception) {
        Log.e(
            "OrderItemCard",
            "Failed to parse booking date/time: ${booking.booking_date} ${booking.booking_time}",
            e
        )
        LocalDateTime.now()
    }
    val hoursUntilCancellation =
        Duration.between(currentTime, bookingDateTime.plusHours(6)).toHours()
    Log.d("OrderItemCard", "Hours until cancellation: $hoursUntilCancellation")

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Dòng đầu tiên: Icon + Tiêu đề
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Service Icon",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(45.dp)
                        .background(Color(0xFFFFE0B2), shape = CircleShape)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Log.d("OrderItemCard", "Service name: ${booking.service_name}")
                    Text(
                        text = booking.service_name ?: "Unknown Service",
                        style = AppTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Reference Code: #${booking.reference_code ?: "N/A"}",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
            }
            Divider(
                color = Color(0xFFDADADA),
                thickness = 1.dp,
                modifier = Modifier
                    .width(340.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "Status",
                    style = LocalAppTypography.current.body,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = booking.status ?: "Unknown",
                    style = LocalAppTypography.current.bodySmall,
                    color = when (booking.status) {
                        "Pending" -> AppTheme.colors.onActionSurface
                        "Confirmed" -> AppTheme.colors.onBackgroundVariant
                        "Completed" -> AppTheme.colors.mainColor
                        "Cancelled" -> Color.Red
                        else -> AppTheme.colors.onRegularSurface
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(12.dp)
                        .background(
                            when (booking.status) {
                                "Pending" -> Color(0xFFF3E5F5)
                                "Confirmed" -> Color(0xFFF2FCF0)
                                "Completed" -> Color(0xFFE3F0FF)
                                "Cancelled" -> Color(0xFFFFF0F0)
                                else -> Color(0xFFFFF0F0)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(6.dp)
                )
            }
            Divider(
                color = Color(0xFFDADADA),
                thickness = 1.dp,
                modifier = Modifier
                    .width(340.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Lịch hẹn
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            if (booking.status == "Confirmed") Color(0xFFFFE0B2) else Color(
                                0xFFD5D5D5
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Schedule",
                        tint = if (booking.status == "Confirmed") AppTheme.colors.mainColor else Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    val schedule = try {
                        "${
                            booking.booking_time.substring(
                                0,
                                5
                            )
                        } ${
                            booking.booking_date.substring(
                                8,
                                10
                            )
                        } ${monthFromNumber(booking.booking_date.substring(5, 7))}"
                    } catch (e: Exception) {
                        Log.e(
                            "OrderItemCard",
                            "Failed to format schedule: ${booking.booking_date} ${booking.booking_time}",
                            e
                        )
                        "Invalid Schedule"
                    }
                    Log.d("OrderItemCard", "Schedule: $schedule")
                    Text(
                        text = schedule,
                        style = LocalAppTypography.current.body,
                        fontWeight = if (booking.status == "Confirmed") FontWeight.Bold else FontWeight.Normal,
                        color = if (booking.status == "Confirmed") AppTheme.colors.mainColor else Color.Black
                    )
                    Text(
                        text = "Schedule",
                        style = LocalAppTypography.current.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Ghi chú theo trạng thái
            when (booking.status) {
                "Pending" -> {
                    Log.d(
                        "OrderItemCard",
                        "Pending note: Auto-cancel in $hoursUntilCancellation hours"
                    )
                    Text(
                        text = "Booking will auto-cancel in $hoursUntilCancellation hours if not confirmed by provider.",
                        style = LocalAppTypography.current.bodySmall,
                        color = AppTheme.colors.onActionSurface,
                        modifier = Modifier.padding(start = 10.dp, top = 8.dp)
                    )
                }

                "Cancelled" -> {
                    Log.d("OrderItemCard", "Cancellation reason:")
                    Text(
                        text = "Reason for cancellation: ${true ?: "Not specified"}",
                        style = LocalAppTypography.current.bodySmall,
                        color = Color.Red,
                        modifier = Modifier.padding(start = 10.dp, top = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Nút hành động theo trạng thái
            Log.d("OrderItemCard", "Rendering action buttons for status: ${booking.status}")
            when (booking.status) {
                "Pending" -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { Log.d("OrderItemCard", "Call button clicked") },
                            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Call",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Call",
                                    color = Color.White,
                                    style = LocalAppTypography.current.bodySmall
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                Log.d(TAG, "Cancel button clicked for bookingId=${booking.booking_id}")
                                scope.launch {
                                    showConfirmDialog = true
                                }
                            },
                            border = BorderStroke(1.dp, Color.Red),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = Color.Red,
                                    style = LocalAppTypography.current.bodySmall
                                )
                            }
                        }
                        if (showConfirmDialog) {
                            ConfirmDialog(
                                onConfirm = {
                                    scope.launch {
                                        isCancelLoading = true
                                        try {
                                            // Gọi API và xử lý kết quả
                                            val success = bookingService.updateBookingStatus(booking.booking_id, "Cancelled")
                                            if (success) {
                                                val updatedBooking = booking.copy(status = "Cancelled")
                                                onBookingUpdated(updatedBooking)
                                            }
                                        } catch (e: Exception) {
                                            Log.e(TAG, "Failed to cancel booking", e)
                                            // Có thể hiển thị thông báo lỗi ở đây
                                        } finally {
                                            isCancelLoading = false
                                            showConfirmDialog = false
                                        }
                                    }
                                },
                                onDismiss = { showConfirmDialog = false }
                            )
                        }
                    }
                }

                "Confirmed" -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { Log.d("OrderItemCard", "Call button clicked") },
                            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Call",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Call",
                                    color = Color.White,
                                    style = LocalAppTypography.current.bodySmall
                                )
                            }
                        }
                        OutlinedButton(
                            onClick = { Log.d("OrderItemCard", "Chat button clicked") },
                            border = BorderStroke(1.dp, AppTheme.colors.mainColor),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Chat,
                                    contentDescription = "Chat",
                                    tint = AppTheme.colors.mainColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Chat",
                                    color = AppTheme.colors.mainColor,
                                    style = LocalAppTypography.current.bodySmall
                                )
                            }
                        }
                    }
                }

                "Completed", "Cancelled" -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { Log.d("OrderItemCard", "Info button clicked") },
                            border = BorderStroke(1.dp, AppTheme.colors.mainColor),
                            modifier = Modifier
                                .width(180.dp)
                                .height(40.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info",
                                    tint = AppTheme.colors.mainColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Info",
                                    color = AppTheme.colors.mainColor,
                                    style = LocalAppTypography.current.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

// Hàm helper để chuyển đổi số tháng thành tên tháng viết tắt
private fun monthFromNumber(month: String): String {
    return when (month) {
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "May"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Aug"
        "09" -> "Sep"
        "10" -> "Oct"
        "11" -> "Nov"
        "12" -> "Dec"
        else -> ""
    }
}
@Composable
fun ConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Xác nhận hủy") },
        text = { Text("Bạn có chắc chắn muốn hủy booking này?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Hủy", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Thoát")
            }
        }
    )
}