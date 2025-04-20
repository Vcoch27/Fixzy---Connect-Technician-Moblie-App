    @file:Suppress("UNREACHABLE_CODE")

    package com.example.fixzy_ketnoikythuatvien.ui.navigation

    import android.util.Log
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.style.TextAlign
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.currentBackStackEntryAsState
    import androidx.navigation.compose.rememberNavController
    import com.example.fixzy_ketnoikythuatvien.data.model.dummyChats
    import com.example.fixzy_ketnoikythuatvien.ui.screen.AllCategoriesScreen
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
    import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.TestViewModel

    @Composable
    fun AppNavigation(modifier: Modifier = Modifier) {

        val navController = rememberNavController()
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        Scaffold(
            bottomBar = {
                val hideBottomBarRoutes =
                    listOf("chat_screen/{userName}", "login_screen", "signup_screen", "splash_screen")
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
                composable("splash_screen") {
                    SplashScreen(navController)
                }
                composable("signup_screen") {
                    SignUpScreen(

                        onBackToLogin = {
                            navController.popBackStack()
                        },
                        onNavigateToHome = {navController.navigate("home_page")},

                    )
                }
                composable("login_screen") {
                    LoginScreen(
                        onNavigateToHome = {navController.navigate("home_page")},
                        onSignUpClick = { navController.navigate("signup_screen") },
                        onForgotPasswordClick = { /* handle forgot */ }
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
                composable("home_page") {
                    ProductHomePageScreen(modifier = Modifier, navController = navController)
                }
                composable("home_page") {
                    ProductHomePageScreen(modifier = Modifier, navController = navController)
                }
                composable("all_categories") {
                    AllCategoriesScreen(navController = navController)
                }
                composable("orders_page") {
                    OrdersScreen(navController)
                }
                composable("notifications_page") {
                    NotificationScreen(navController)
                }
                composable("profile_page") {
                    ProfileScreen(navController,
                        onLogout = {navController.navigate("login_screen")})
                }
                composable("test_page") {

                    val viewModel: TestViewModel = viewModel()
                    Log.d("appnav", "tôi aaay")

                    // Truyền ViewModel vào màn hình
                    TestScreen(viewModel = viewModel)
                }
            }
        }
    }
