package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.AuthService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

private const val TAG = "SplashScreen"

@Composable
fun SplashScreen(navController: NavController) {
    val authService = remember { AuthService() }

    LaunchedEffect(Unit) {
        Log.d(TAG, "SplashScreen launched")
        delay(1000)

        val user = FirebaseAuth.getInstance().currentUser
        Log.d(TAG, "Checking user authentication status: ${user != null}")

        if (user != null) {
            Log.d(TAG, "User found, attempting to get ID token")
            user.getIdToken(false).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token
                    Log.d(TAG, "ID token retrieval success: ${!idToken.isNullOrEmpty()}")

                    if (!idToken.isNullOrEmpty()) {
                        Log.d(TAG, "Authenticating with backend using ID token")
                        authService.authenticateWithBackend(
                            idToken,
                            onSuccess = { userData ->
                                Log.d(TAG, "Backend authentication successful")
                                val user = userData as? UserData
                                if (user != null) {
                                    Log.d(TAG, "User data received, dispatching to store and navigating to home")
                                    Store.store.dispatch(Action.setUser(user))
                                    navController.navigate("home_page") {
                                        popUpTo("splash_screen") { inclusive = true }
                                    }
                                } else {
                                    Log.w(TAG, "User data is null, navigating to login")
                                    navController.navigate("login_screen") {
                                        popUpTo("splash_screen") { inclusive = true }
                                    }
                                }
                            },
                            onError = {
                                Log.e(TAG, "Backend authentication failed")
                                navController.navigate("login_screen") {
                                    popUpTo("splash_screen") { inclusive = true }
                                }
                            }
                        )
                    } else {
                        Log.w(TAG, "ID token is empty, navigating to login")
                        navController.navigate("login_screen") {
                            popUpTo("splash_screen") { inclusive = true }
                        }
                    }
                } else {
                    Log.e(TAG, "Failed to get ID token", task.exception)
                    navController.navigate("login_screen") {
                        popUpTo("splash_screen") { inclusive = true }
                    }
                }
            }
        } else {
            Log.d(TAG, "No user found, navigating to login")
            navController.navigate("login_screen") {
                popUpTo("splash_screen") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Fixzy Loading...",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}