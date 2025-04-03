package com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun NewNotificationHeader(onMarkAllRead: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp)) // Bo tròn góc
                .background(AppTheme.colors.regularSurface) // Màu nền
                .padding(4.dp) // Khoảng cách bên trong Box
        ) {
            Text(
                text = "Nguyen Van Coc",
                style = AppTheme.typography.titleSmall,
                color = AppTheme.colors.mainColor,
                modifier = Modifier.padding(8.dp) // Để chữ không sát viền
            )
        }

        Box(
            Modifier
                .height(25.dp)
                .width(4.dp)
                .clip(RoundedCornerShape(50))
                .background(AppTheme.colors.mainColor)
        )

        Text(
            text = "Mark as all read",
            color = AppTheme.colors.mainColor,
            style = AppTheme.typography.bodySmall,
            modifier = Modifier.clickable { onMarkAllRead() }
        )
    }
}
