@file:Suppress("UNREACHABLE_CODE")

package com.example.fixzy_ketnoikythuatvien.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fixzy_ketnoikythuatvien.data.model.dummyChats
import com.example.fixzy_ketnoikythuatvien.data.remote.RetrofitInstance
import com.example.fixzy_ketnoikythuatvien.ui.screen.ChatScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.NotificationScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.OrdersScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProductHomePageScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProfileScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.SplashScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.TestScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.ExtendedChat
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.LoginScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.SignUpScreen


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val api = remember { RetrofitInstance.testApi } // ✅ Dùng testApi thay vì api
    Scaffold(
        bottomBar = {
            val hideBottomBarRoutes =
                listOf("chat_screen/{userName}", "login_screen", "signup_screen","splash_screen")
            if (currentRoute !in hideBottomBarRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "test_page", // Gộp chat_list_screen vào chat_page
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("splash_screen") {
                SplashScreen(navController)
            }

            composable("signup_screen") {
                SignUpScreen(
                    onNavigateToHome = {
                        navController.navigate("home_page") {
                            popUpTo("signup_screen") { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }
            composable("login_screen") {
                LoginScreen(

                    onNavigateToHome = {
                        navController.navigate("home_page") {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    },
                    onSignUpClick = { navController.navigate("signup_screen") },
                    onForgotPasswordClick = { /* xử lý forgot */ }
                )
            }
            composable("chat_page") {
                ChatScreen(navController)
            }
            composable("chat_screen/{userName}") { backStackEntry ->
                val userName = backStackEntry.arguments?.getString("userName") ?: ""
                val chat = dummyChats.find { it.userName == userName }

                if (chat != null) {
                    ExtendedChat(chat, navController)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Chat not found", textAlign = TextAlign.Center, color = Color.Red)
                    }
                }
            }
            // Các màn hình khác
            composable("home_page") {
                ProductHomePageScreen(modifier = Modifier, navController = navController)
            }
            composable("orders_page") {
                OrdersScreen(navController)
            }
            composable("notifications_page") {
                NotificationScreen(navController)
            }
            composable("profile_page") {
                ProfileScreen(navController)
            }

            composable("test_page") {
                TestScreen(api = RetrofitInstance.testApi)  // Thay `api` bằng `testApi`
            }


        }
    }
}
