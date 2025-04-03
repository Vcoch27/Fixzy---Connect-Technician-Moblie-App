package com.example.fixzy_ketnoikythuatvien.ui.components.chatComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.data.model.ChatData
import com.example.fixzy_ketnoikythuatvien.data.model.NotificationData
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
@Composable
fun ChatItem(chat: ChatData, onChatClick: (ChatData) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onChatClick(chat) },
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ảnh người gửi
            Image(
                painter = painterResource(id = chat.userAvatar),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Nội dung chat
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.userName,
                    style = AppTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = chat.messages.lastOrNull()?.text ?: "No messages yet",
                    style = AppTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Trạng thái tin nhắn (New, Time)
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End
            ) {
                if (chat.isNew) {
                    Text(
                        text = "New",
                        color = AppTheme.colors.mainColor,
                        style = AppTheme.typography.titleSmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = chat.messages.lastOrNull()?.timestamp ?: "",
                    style = AppTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
