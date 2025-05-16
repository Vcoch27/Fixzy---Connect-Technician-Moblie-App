package com.example.fixzy_ketnoikythuatvien.ui.navigation

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Build
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
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BottomNavigationBar(navController: NavController) {
    val userId = Store.stateFlow.value.user?.id
    val items = listOf(
        BottomNavItem("home_page", "Home", Icons.Outlined.Home),
        BottomNavItem("orders_page", "Order", Icons.Default.ShoppingCart),
        BottomNavItem("notifications_page", "Notification", Icons.Outlined.Notifications, hasBadge = true),
        BottomNavItem("provider_screen/${userId}", "Provider", Icons.Default.Build),
        BottomNavItem("profile_page", "Profile", Icons.Outlined.Person)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomNavigation(navController, items, currentRoute)
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val hasBadge: Boolean = false
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
        modifier = Modifier.height(70.dp)

    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val iconColor = if (isSelected) AppTheme.colors.mainColor else Color.Gray
            val textColor = if (isSelected) AppTheme.colors.mainColor else Color.Gray
            val isNotification = item.label == "Notifications"

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
                            .size(32.dp)
                            .align(Alignment.CenterVertically),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(26.dp)
                        )
                        if (item.hasBadge) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color.Red, shape = CircleShape)
                                    .offset(x = 8.dp, y = (-8).dp)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = AppTheme.typography.bodySmall,
                        fontSize = 7.5.sp,
                        color = textColor,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                },
                modifier = Modifier
                    .offset(y = (5).dp)
                    .then(if (isNotification) Modifier.weight(1.5f) else Modifier.weight(1f))

            )
        }
    }

}


