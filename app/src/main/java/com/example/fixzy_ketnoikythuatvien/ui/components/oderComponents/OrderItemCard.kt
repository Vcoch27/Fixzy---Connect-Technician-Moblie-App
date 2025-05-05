package com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.data.model.OrderData
import com.example.fixzy_ketnoikythuatvien.data.model.OrderStatus
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography


@Composable
fun OrderItemCard(booking: DetailBooking) {
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
                    Text(
                        text = booking.service_name,
                        style = AppTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Reference Code: #${booking.reference_code}",
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

            // Trạng thái đơn hàng
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
                    text = booking.status,
                    style = LocalAppTypography.current.bodySmall,
                    color = when (booking.status) {
                        "Pending" -> Color(0xFF6200EE)
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
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD5D5D5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Schedule")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    val schedule = "${booking.booking_time.substring(0, 5)} ${booking.booking_date.substring(8, 10)} ${monthFromNumber(booking.booking_date.substring(5, 7))}"
                    Text(text = schedule, style = LocalAppTypography.current.body)
                    Text(
                        text = "Schedule",
                        style = LocalAppTypography.current.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Nút hành động theo trạng thái
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
                            onClick = { /* Gọi */ },
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
                            onClick = { /* Chat */ },
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
                        OutlinedButton(
                            onClick = { /* Hủy */ },
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
                            onClick = { /* Gọi */ },
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
                            onClick = { /* Chat */ },
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
                            onClick = { /* Thông tin đơn hàng */ },
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