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
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.data.model.NotificationData
import com.example.fixzy_ketnoikythuatvien.service.model.Notification
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/512/565/565422.png", // Replace with actual image URL if available
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.coc) // Define in resources
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = AppTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = notification.content,
                    style = AppTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = formatCreatedAt(notification.createdAt),
                    style = AppTheme.typography.bodySmall,
                    color = Color.Gray
                )

            }

            Spacer(modifier = Modifier.width(8.dp))

//            Column(
//                horizontalAlignment = Alignment.End,
//                verticalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier
//                    .padding(start = 8.dp)
//                    .height(70.dp) // Chiều cao tương đương ảnh
//            ) {
//                if (notification.isRead == 0) {
//                    Text(
//                        text = "New",
//                        color = AppTheme.colors.mainColor,
//                        style = AppTheme.typography.titleSmall
//                    )
//                }
//                Spacer(modifier = Modifier.weight(1f))
//                Text(
//                    text = formatCreatedAt(notification.createdAt),
//                    style = AppTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
//            }

        }
    }
}

private fun formatCreatedAt(createdAt: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm, MMM dd", Locale.getDefault())
        val date = inputFormat.parse(createdAt)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        createdAt // Fallback to raw string
    }
}
