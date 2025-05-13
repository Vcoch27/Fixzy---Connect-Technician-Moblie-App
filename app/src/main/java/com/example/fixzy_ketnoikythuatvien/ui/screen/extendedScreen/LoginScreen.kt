@file:Suppress("DEPRECATION", "UNUSED_EXPRESSION")

package com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.service.AuthService
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    context: Context = LocalContext.current,
    onNavigateToHome: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val activity = context as? ComponentActivity ?: throw IllegalStateException("Context must be a ComponentActivity")
    val authService = remember {
        AuthService(
            context = context,
            activity = activity,
            onSuccess = { userData ->
                Log.d("SocialLogin", "Đăng nhập thành công: $userData")
                Toast.makeText(context, "Chào ${userData.name}", Toast.LENGTH_SHORT).show()
                navController.navigate("home_page")
            },
            onError = { error ->
                Log.e("SocialLogin", "Lỗi đăng nhập: $error")
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    val TAG = "LOGIN_SCREEN"
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "Google Sign-In result received: resultCode=${result.resultCode}, data=${result.data}")
        
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                authService.handleGoogleSignInResult(
                    result = result,
                    onSuccess = { userData ->
                        Log.d(TAG, "Google Sign-In success: $userData")
                        errorMessage = null
                        onNavigateToHome()
                    },
                    onError = { error ->
                        errorMessage = when (error) {
                            "Đăng nhập Google đã bị hủy" -> "Bạn đã hủy đăng nhập Google"
                            "ID Token null" -> "Không thể xác thực với Google. Vui lòng thử lại"
                            else -> "Lỗi đăng nhập Google: $error"
                        }
                        Log.e(TAG, "Google Sign-In error: $error")
                        scope.launch {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            Activity.RESULT_CANCELED -> {
                Log.d(TAG, "Google Sign-In was canceled by user")
                errorMessage = "Bạn đã hủy đăng nhập Google"
                scope.launch {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Log.e(TAG, "Google Sign-In failed with result code: ${result.resultCode}")
                errorMessage = "Đăng nhập Google thất bại. Vui lòng thử lại"
                scope.launch {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.mainColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(80.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorMessage != null
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        isError = errorMessage != null
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Forgot Password?",
                        color = AppTheme.colors.mainColor,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { onForgotPasswordClick() }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    if (isLoading) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                isLoading = true
                                errorMessage = null
                                authService.login(
                                    email = email,
                                    password = password,
                                    onSuccess = {
                                        isLoading = false
                                        Log.d(TAG, "Email/Password login success")
                                        onNavigateToHome()
                                    },
                                    onError = { error ->
                                        isLoading = false
                                        errorMessage = error
                                        Log.e(TAG, "Email/Password login error: $error")
                                        scope.launch {
                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                            } else {
                                errorMessage = "Vui lòng nhập email và mật khẩu"
                                scope.launch {
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text("Login")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Don't have an account? Sign Up",
                        color = AppTheme.colors.mainColor,
                        modifier = Modifier
                            .clickable { onSignUpClick() }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    SocialLoginButtons(
                        authService = authService,
                        googleSignInLauncher = googleSignInLauncher,
                        onSuccess = { userData ->
                            isLoading = false
                            Log.d(TAG, "Social login success: $userData")
                            onNavigateToHome()
                        },
                        onError = { error ->
                            isLoading = false
                            errorMessage = error
                            Log.e(TAG, "Social login error: $error")
                            scope.launch {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SocialLoginButtons(
    authService: AuthService,
    googleSignInLauncher: ActivityResultLauncher<Intent>,
    onSuccess: (UserData) -> Unit,
    onError: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            SocialButton(
                text = "Facebook",
                drawableId = R.drawable.fb_logo,
                borderColor = Color.LightGray,
                onClick = { /* Handle Facebook login if implemented */ }
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            SocialButton(
                text = "Google",
                drawableId = R.drawable.google_logo,
                borderColor = Color.LightGray,
                onClick = {
                    isLoading = true
                    try {
                        authService.launchGoogleSignIn(googleSignInLauncher)
                    } catch (e: Exception) {
                        isLoading = false
                        onError("Lỗi khởi tạo đăng nhập Google: ${e.message}")
                    }
                },
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun SocialButton(
    text: String,
    drawableId: Int,
    borderColor: Color,
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFFF5F5F5),
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.Black
            )
        } else {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, color = Color.Black)
        }
    }
}

@Composable
fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )
        Text(
            text = "Or",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )
    }
}
