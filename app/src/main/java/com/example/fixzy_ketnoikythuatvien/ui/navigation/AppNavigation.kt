package com.example.fixzy_ketnoikythuatvien.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fixzy_ketnoikythuatvien.data.model.OrderState
import com.example.fixzy_ketnoikythuatvien.ui.screen.ChatScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.NotificationsScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.OrdersScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProductDetailsScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProductHomePageScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProfileScreen

const val PRODUCT_PRICE_PER_UNIT = 5.99
const val PRODUCT_CURRENCY = "$"

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    //Đối tượng NavController được sử dụng để điều hướng giữa các màn hình
    val navController = rememberNavController()

    //trạng thái lưu trữ số lượng sản phẩm và tổng giá tiền
    var amount by remember { mutableStateOf(5) }
    val totalPrice by remember { derivedStateOf { amount * PRODUCT_PRICE_PER_UNIT } }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        //quản lí các màn hình của ứng dụng với điểm bắt đầu
        NavHost(
            navController = navController,
            startDestination = "home_page",
            modifier = Modifier.padding(paddingValues)

        ) {
            composable("home_page") {
                ProductHomePageScreen(
                    modifier = Modifier,
                    navController = navController
                )
            }
            composable("orders_page") {
//                OrdersScreen(
//
//                )
                ProductDetailsScreen(
                    modifier = Modifier,
                    navController = navController,
                    onAddItemClicked = { amount = amount.inc() }, // Tăng số lượng
                    onRemoveItemClicked = {
                        if (amount > 0) amount = amount.dec()
                    }, // Giảm số lượng
                    onCheckOutClicked = {},
                    orderState = OrderState(
                        amount = amount,
                        totalPrice = "$PRODUCT_CURRENCY${String.format("%.2f", totalPrice)}"
                    )
                )
            }
            composable("notifications_page") {
                NotificationsScreen()
            }
            composable("chat_page") {
                ChatScreen()
            }
            composable("profile_page") {
                ProfileScreen()
            }
            composable("product_detail_screen") {
                ProductDetailsScreen(
                    modifier = Modifier,
                    navController = navController,
                    onAddItemClicked = { amount = amount.inc() }, // Tăng số lượng
                    onRemoveItemClicked = {
                        if (amount > 0) amount = amount.dec()
                    }, // Giảm số lượng
                    onCheckOutClicked = {},
                    orderState = OrderState(
                        amount = amount,
                        totalPrice = "$PRODUCT_CURRENCY${String.format("%.2f", totalPrice)}"
                    )
                )
            }
        }
    }
}
