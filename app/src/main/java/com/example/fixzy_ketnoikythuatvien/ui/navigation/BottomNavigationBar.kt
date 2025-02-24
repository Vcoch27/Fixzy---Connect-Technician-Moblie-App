package com.example.fixzy_ketnoikythuatvien.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home_page", "Home", Icons.Outlined.Home),
        BottomNavItem("orders_page", "Orders", Icons.Default.ShoppingCart),
        BottomNavItem("notifications_page", "Notifications", Icons.Outlined.Notifications, hasBadge = true),
        BottomNavItem("chat_page", "Chat", Icons.AutoMirrored.Outlined.Message),
        BottomNavItem("profile_page", "Profile", Icons.Outlined.Person)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomNavigation(navController, items, currentRoute)
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val hasBadge: Boolean = false // Mặc định không có thông báo
)
@Composable
fun BottomNavigation(
    navController: NavController,
    items: List<BottomNavItem>,
    currentRoute: String?
) {
    BottomNavigation(
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier.height(70.dp) //  Đặt chiều cao cho BottomNavigatio

    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val iconColor = if (isSelected) AppTheme.colors.mainColor else Color.Gray
            val textColor = if (isSelected) AppTheme.colors.mainColor else Color.Gray
            val isNotification = item.label == "Notifications" // Kiểm tra nếu là Notifications

            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(32.dp) // ✅ Đảm bảo đủ không gian cho icon + badge
                            .align(Alignment.CenterVertically),
                        contentAlignment = Alignment.TopStart // ✅ Đặt badge ở góc trên phải
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(26.dp) // ✅ Điều chỉnh kích thước icon
                        )
                        if (item.hasBadge) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color.Red, shape = CircleShape)
                                    .offset(x = 8.dp, y = (-8).dp) // ✅ Điều chỉnh vị trí badge
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = AppTheme.typography.bodySmall,
                        fontSize = 10.sp, //  Tăng nhẹ font size
                        color = textColor,
                        modifier = Modifier.padding(top = 2.dp) //  Khoảng cách hợp lý giữa icon và text
                    )
                },
                modifier = Modifier
                    .offset(y = (5).dp)
                    .then(if (isNotification) Modifier.weight(1.5f) else Modifier.weight(1f)) //  Notifications rộng hơn

            )
        }
    }

}


