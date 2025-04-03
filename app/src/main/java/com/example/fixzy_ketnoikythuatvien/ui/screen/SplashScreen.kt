package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val user = FirebaseAuth.getInstance().currentUser
    Log.d("SPLASH", "User logged in: ${user != null}")
    Log.d("SPLASH", "User email: ${user?.email ?: "No user"}")

    LaunchedEffect(Unit) {
        delay(1500)
        if (isUserLoggedIn) {
            navController.navigate("home_page") {
                popUpTo("splash_screen") { inclusive = true }
            }
        } else {
            navController.navigate("login_screen") {
                popUpTo("splash_screen") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,

    ) {
        Text(text = "Fixzy Loading...", fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}
