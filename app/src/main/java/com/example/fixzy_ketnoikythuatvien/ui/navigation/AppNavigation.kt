@file:Suppress("UNREACHABLE_CODE")

package com.example.fixzy_ketnoikythuatvien.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fixzy_ketnoikythuatvien.data.model.dummyChats
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.ui.screen.AllCategoriesScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.AvailabilityScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ChatScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ConfirmBookingScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.EditProfileScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.NotificationScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.OrdersScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProductHomePageScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProfileScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProviderModeScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ProviderScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.ServiceModeScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.SplashScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.TestScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.AddServiceScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.ExtendedChat
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.LoginScreen
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.SignUpScreen
import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.TestViewModel
import com.example.fixzy_ketnoikythuatvien.utils.startNotificationPolling

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    startNotificationPolling(
        context  = navController.context
    )
    Scaffold(
        bottomBar = {
            val hideBottomBarRoutes = listOf("chat_screen/{userName}", "login_screen", "signup_screen", "splash_screen", "booking_success_screen/{referenceCode}")
            if (currentRoute !in hideBottomBarRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash_screen",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Authentication (Màn hình xác thực)
            composable("splash_screen") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    SplashScreen(navController)
                }
            }
            composable("login_screen") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    LoginScreen(
                        navController = navController,
                        onNavigateToHome = { navController.navigate("home_page") },
                        onSignUpClick = { navController.navigate("signup_screen") },
                        onForgotPasswordClick = { /* handle forgot */ }
                    )
                }
            }
            composable("signup_screen") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    SignUpScreen(
                        onBackToLogin = { navController.popBackStack() },
                        onNavigateToHome = { navController.navigate("home_page") }
                    )
                }
            }

            // Home & Categories (Màn hình chính và danh mục)
            composable("home_page") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    ProductHomePageScreen(modifier = Modifier, navController = navController)
                }
            }
            composable("all_categories") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    AllCategoriesScreen(navController = navController)
                }
            }

            // Service & Provider (Màn hình dịch vụ và nhà cung cấp)
            composable(
                "service_provider_mode/{serviceId}/{service_name}?selectedTab={selectedTab}",
                arguments = listOf(
                    navArgument("serviceId") { type = NavType.IntType },
                    navArgument("service_name") { type = NavType.StringType; nullable = true },
                    navArgument("selectedTab") {
                        type = NavType.IntType
                        defaultValue = 0
                        nullable = false
                    }
                )
            ) { backStackEntry ->
                val serviceId = backStackEntry.arguments?.getInt("serviceId") ?: 0
                val serviceName = backStackEntry.arguments?.getString("service_name")
                val selectedTab = backStackEntry.arguments?.getInt("selectedTab") ?: 0
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    ServiceModeScreen(
                        navController = navController,
                        serviceId = serviceId,
                        serviceName = serviceName,
                        selectedTabFromNav = selectedTab // truyền vào màn hình
                    )
                }
            }
            composable(
                "provider_screen/{providerId}",
                arguments = listOf(navArgument("providerId") { type = NavType.IntType })
            ) {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    ProviderScreen(navController = navController, providerId = it.arguments?.getInt("providerId") ?: 0)
                }
            }
            composable("add_service_screen") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    AddServiceScreen(navController)
                }
            }

            // Booking (Màn hình đặt lịch)
            composable(
                "availability_screen/{serviceId}?service_name={service_name}",
                arguments = listOf(
                    navArgument("serviceId") { type = NavType.IntType },
                    navArgument("service_name") { type = NavType.StringType; nullable = true }
                )
            ) {
                val serviceId = it.arguments?.getInt("serviceId") ?: 0
                val serviceName = it.arguments?.getString("service_name")
                SwipeBackWrapper(navController = navController) {
                    AvailabilityScreen(
                        navController = navController,
                        serviceId = serviceId,
                        serviceName = serviceName
                    )
                }
            }
            composable(
                "confirm_booking_screen?service_name={service_name}&date={date}",
                arguments = listOf(
                    navArgument("service_name") { type = NavType.StringType; nullable = true },
                    navArgument("date") { type = NavType.StringType; nullable = true }
                )
            ) {
                val serviceName = it.arguments?.getString("service_name")
                val date = it.arguments?.getString("date")
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    ConfirmBookingScreen(navController = navController, serviceName = serviceName, date = date)
                }
            }
            composable(
                "booking_success_screen/{referenceCode}",
                arguments = listOf(navArgument("referenceCode") { type = NavType.StringType })
            ) {
                val referenceCode = it.arguments?.getString("referenceCode") ?: ""
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    BookingSuccessScreen(referenceCode = referenceCode, navController = navController)
                }
            }

            // Chat (Màn hình trò chuyện)
            composable("chat_page") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    ChatScreen(navController)
                }
            }
            composable("chat_screen/{userName}") { backStackEntry ->
                val userName = backStackEntry.arguments?.getString("userName") ?: ""
                val chat = dummyChats.find { it.userName == userName }
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    if (chat != null) {
                        ExtendedChat(chat, navController)
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Chat not found", textAlign = TextAlign.Center, color = Color.Red)
                        }
                    }
                }
            }

            //  Profile (Màn hình hồ sơ)
            composable("profile_page") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    ProfileScreen(
                        navController,
                        onLogout = { navController.navigate("login_screen") }
                    )
                }
            }
            composable("edit_profile_screen") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    EditProfileScreen(
                        navController
                    )
                }
            }

            // Notifications (Màn hình thông báo)
            composable("notifications_page") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    NotificationScreen(navController)
                }
            }

            // Orders (Màn hình đơn hàng)
            composable("orders_page") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    OrdersScreen(navController)
                }
            }

            // Provider Mode (Màn hình chế độ nhà cung cấp)
            composable("provider_mode_page") {
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    ProviderModeScreen(navController)
                }
            }

            // Nhóm 10: Miscellaneous (Màn hình khác)
            composable("test_page") {
                val viewModel: TestViewModel = viewModel()
                SwipeBackWrapper(navController = navController, modifier = Modifier.fillMaxSize()) {
                    TestScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSuccessScreen(referenceCode: String, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đặt Lịch Thành Công") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home_page") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chúc mừng! Bạn đã đặt lịch thành công.",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mã đặt lịch: $referenceCode",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate("home_page") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Quay về Trang Chủ")
            }
        }
    }
}