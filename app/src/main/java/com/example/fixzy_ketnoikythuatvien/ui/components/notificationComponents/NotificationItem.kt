package com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.data.model.NotificationData
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun NotificationItem(notification: NotificationData) {
    Card(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ảnh người gửi
            Image(
                painter = painterResource(id = notification.imageRes),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = notification.title, style = AppTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(5.dp)) // Đẩy "Time" xuống dưới

                Text(text = notification.message, style = AppTheme.typography.bodySmall, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                if (notification.isNew) {
                    Text(text = "New", color = AppTheme.colors.mainColor, style = AppTheme.typography.titleSmall)
                }

                Spacer(modifier = Modifier.weight(1f)) // Đẩy "Time" xuống dưới

                Text(text = notification.time, style = AppTheme.typography.bodySmall, color = Color.Gray)
            }

        }
    }
}
