package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.data.model.notificationList
import com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents.NewNotificationHeader
import com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents.NotificationFilterDropdown
import com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents.NotificationItem
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar

@Composable
fun NotificationScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Recent") }
    val filters = listOf("Recent", "Older", "Unread")
    val notifications = remember { mutableStateListOf(*notificationList.toTypedArray()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 28.dp, start = 10.dp, end = 10.dp)) {
        // TopBar + Bộ lọc
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(end = 10.dp), // ✅ Giữ khoảng cách hai bên
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TopBar(
                navController, "Notification",
                modifier = Modifier.weight(1f) // ✅ Đảm bảo nó không chiếm toàn bộ chiều rộng
            )

            NotificationFilterDropdown(
                selectedFilter = selectedFilter,
                filters = filters,
                onFilterSelected = { selectedFilter = it }, // ✅ Đặt đúng vị trí
                modifier = Modifier.wrapContentWidth() // ✅ Modifier nằm cuối cùng
            )

        }
        NewNotificationHeader { notifications.forEach { it.isNew = false } }

        // Danh sách thông báo
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notifications) { notification ->
                NotificationItem(notification)
            }
        }
    }
}
