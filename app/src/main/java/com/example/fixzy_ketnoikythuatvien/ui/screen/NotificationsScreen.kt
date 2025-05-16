package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.data.model.notificationList
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.NotificationService
import com.example.fixzy_ketnoikythuatvien.service.model.Notification
import com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents.NewNotificationHeader
import com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents.NotificationFilterDropdown
import com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents.NotificationItem
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun NotificationScreen(
    navController: NavController,
) {
    val notificationService = remember { NotificationService() }
    var selectedFilter by remember { mutableStateOf("Recent") }
    val filters = listOf("Recent", "Older", "Unread")
    val state by Store.stateFlow.collectAsState()
    val userId = state.user?.id

    Log.d("NotificationScreen", "Composable recomposed. State user: ${state.user}")

    // Fetch notifications when userId changes
    LaunchedEffect(userId) {
        if (userId != null) {
            Log.d("NotificationScreen", "Fetching notifications for userId: $userId")
            notificationService.getNotifications(userId)
        } else {
            Log.w("NotificationScreen", "User ID is null, cannot fetch notifications")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 10.dp, end = 10.dp)
    ) {
        // TopBar + Filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TopBar(
                navController = navController,
                title = "Notification",
                modifier = Modifier.weight(1f)
            )

            NotificationFilterDropdown(
                selectedFilter = selectedFilter,
                filters = filters,
                onFilterSelected = {
                    Log.d("NotificationScreen", "Filter selected: $it")
                    selectedFilter = it
                },
                modifier = Modifier.wrapContentWidth()
            )
        }

//        NewNotificationHeader {
//            Log.d("NotificationScreen", "Marking all notifications as read")
//            if (userId != null) {
//                notificationService.markAllNotificationsAsRead(userId)
//            }
//        }

        when {
            state.isLoading -> {
                Log.d("NotificationScreen", "Loading notifications...")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Log.e("NotificationScreen", "Error occurred: ${state.error}")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "An error occurred",
                        color = Color.Red,
                        style = AppTheme.typography.bodySmall
                    )
                }
            }
            else -> {
                // Apply filtering based on selectedFilter
                val sortedNotifications = when (selectedFilter) {
                    "Older" -> state.notifications.sortedBy { it.createdAt }
                    "Unread" -> state.notifications.filter { it.isRead == 0 }
                    else -> state.notifications.sortedByDescending { it.createdAt }
                }

                Log.d("NotificationScreen", "Displaying ${sortedNotifications.size} notifications")

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sortedNotifications) { notification ->
                        Log.d("NotificationScreen", "Rendering notification: $notification")
                        NotificationItem(notification = notification)
                    }
                }
            }
        }
    }
}

