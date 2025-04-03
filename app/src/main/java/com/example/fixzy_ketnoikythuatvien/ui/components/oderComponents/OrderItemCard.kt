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
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography

@Composable
fun OrderItemCard(order: OrderData) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
//        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            //Dòng đầu tiên: Icon + Tiêu đề
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Build,//hình ảnh dịch vụ,
                    contentDescription = "Service Icon",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(45.dp)
                        .background(Color(0xFFFFE0B2), shape = CircleShape)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width((8.dp)))
                Column {
                    Text(text = order.serviceName, style = AppTheme.typography.titleMedium)
                    Text(
                        text = "Reference Code: ${order.referenceCode}",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
            }
        }
        Divider(
            color = Color(0xFFDADADA),
            thickness = 1.dp,
            modifier = Modifier
                .width(340.dp) // Chỉ rộng 200dp thay vì full
                .align(Alignment.CenterHorizontally) // Căn giữa nếu cần
        )

        // Trạng thái đơn hàng
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
        ) {
            Text(
                text = "Status",
                style = LocalAppTypography.current.body,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when (order.status) {
                    OrderStatus.CONFIRMED -> "Confirmed"
                    OrderStatus.DONE -> "Done"
                    OrderStatus.NOT_BOOKED -> "Not Booked"
                },
                style = LocalAppTypography.current.bodySmall,
                color = when (order.status) {
                    OrderStatus.CONFIRMED -> AppTheme.colors.onBackgroundVariant
                    OrderStatus.DONE -> AppTheme.colors.mainColor
                    OrderStatus.NOT_BOOKED -> AppTheme.colors.onRegularSurface
                },
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(12.dp)
                    .background(
                        when (order.status) { // Màu nền theo trạng thái
                            OrderStatus.CONFIRMED -> Color(0xFFF2FCF0) // Màu xanh nhạt
                            OrderStatus.DONE -> Color(0xFFE3F0FF) // Màu xanh dương nhạt
                            OrderStatus.NOT_BOOKED -> Color(0xFFFFF0F0) // Màu đỏ nhạt
                        },
                        shape = RoundedCornerShape(12.dp) // Bo góc cho nền
                    )
                    .padding(6.dp)
            )

        }
        Divider(
            color = Color(0xFFDADADA),
            thickness = 1.dp,
            modifier = Modifier
                .width(340.dp) // Chỉ rộng 200dp thay vì full
                .align(Alignment.CenterHorizontally) // Căn giữa nếu cần
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Lịch hẹn
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp) // Kích thước tổng thể
                    .clip(CircleShape) // Bo tròn nền
                    .background(Color(0xFFD5D5D5)), // Màu nền
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Schedule")
            }

            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = order.schedule, style = LocalAppTypography.current.body)
                Text(
                    text = "Schedule",
                    style = LocalAppTypography.current.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nút hành động thay đổi theo trạng thái
        when (order.status) {
            OrderStatus.CONFIRMED -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                ) {
                    Button(
                        onClick = { /* Gọi */ },
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Call", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = { /* Chat */ },
                        border = BorderStroke(1.dp, AppTheme.colors.mainColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Chat",
                            tint = AppTheme.colors.mainColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Chat", color = AppTheme.colors.mainColor)
                    }
                }
            }

            OrderStatus.DONE -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.End // Căn phải
                ) {
                    OutlinedButton(
                        onClick = { /* Thông tin đơn hàng */ },
                        border = BorderStroke(1.dp, AppTheme.colors.mainColor),
                        modifier = Modifier
                            .width(180.dp) // Giảm kích thước
                            .height(40.dp) // Điều chỉnh chiều cao
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = AppTheme.colors.mainColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Info", color = AppTheme.colors.mainColor)
                    }
                }


            }

            OrderStatus.NOT_BOOKED -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                ) {
                    Button(
                        onClick = { /* Đặt đơn */ },
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Book",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Book", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = { /* Xem thông tin */ },
                        border = BorderStroke(1.dp, AppTheme.colors.mainColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = AppTheme.colors.mainColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Info", color = AppTheme.colors.mainColor)
                    }
                }
            }
        }
    }
}