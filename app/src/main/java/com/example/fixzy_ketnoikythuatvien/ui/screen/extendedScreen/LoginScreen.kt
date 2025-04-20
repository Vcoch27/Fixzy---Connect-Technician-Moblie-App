@file:Suppress("DEPRECATION", "UNUSED_EXPRESSION")

package com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.service.AuthService
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.AuthUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    context: Context = LocalContext.current,
    onNavigateToHome: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val authService = remember { AuthService() }
    val TAG = "LOGIN_SCREEN"
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    Log.d(TAG, "LoginScreen composed")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.mainColor) // Màu nền xanh phía trên
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // Khoảng trống phía trên tiêu đề
            Text(
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(80.dp))
            // Lớp chứa nội dung màu trắng, bo góc
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Nút đăng nhập Facebook & Google
                    SocialLoginButtons(
                        onFacebookLogin = { /* Xử lý đăng nhập Facebook */ },
                        onGoogleLogin = { /* Xử lý đăng nhập Google */ }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OrDivider()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ô nhập Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AppTheme.colors.mainColor, // Viền khi focus
                            unfocusedBorderColor = Color.Gray, // Viền khi không focus
                            focusedLabelColor = AppTheme.colors.mainColor, // Màu label khi focus
                            cursorColor = AppTheme.colors.mainColor,//con trỏ
                            unfocusedLabelColor = Color.Gray, // Màu label khi không focus
                            disabledBorderColor = Color.LightGray // Màu viền khi bị vô hiệu hóa
                        )
                    )


                    Spacer(modifier = Modifier.height(12.dp))

                    // Ô nhập mật khẩu
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AppTheme.colors.mainColor, // Viền khi focus
                            unfocusedBorderColor = Color.Gray, // Viền khi không focus
                            focusedLabelColor = AppTheme.colors.mainColor, // Màu label khi focus
                            unfocusedLabelColor = Color.Gray, // Màu label khi không focus
                            cursorColor = AppTheme.colors.mainColor,//con trỏ
                            disabledBorderColor = Color.LightGray // Màu viền khi bị vô hiệu hóa
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nút "Quên mật khẩu" căn phải và màu xám
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End // Căn phải
                    ) {
                        TextButton(
                            onClick = onForgotPasswordClick
                        ) {
                            TextButton(onClick = onForgotPasswordClick) {
                                Text("Forgot Password?", color = Color.Gray)
                            }
                        }
                    }



                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            Log.d(TAG, "Login button clicked")
                            Log.d(TAG, "Input data - Email: $email, Password: $password")
                            when {
                                email.isEmpty() -> {
                                    Log.w(TAG, "Validation failed: Email is empty")
                                    showToast(context, "Email cannot be empty")
                                }
                                password.length < 6 -> {
                                    Log.w(TAG, "Validation failed: Password is less than 6 characters")
                                    showToast(context, "Password must be at least 6 characters")
                                }
                                else -> {
                                    Log.i(TAG, "Validation passed, calling authService.login")
                                    authService.login(
                                        email = email,
                                        password = password,
                                        onSuccess = {
                                            Log.i(TAG, "Login successful, navigating to home")
                                            showToast(context, "Login successful")
                                            onNavigateToHome()
                                        },
                                        onError = { error ->
                                            Log.e(TAG, "Login failed: $error")
                                            showToast(context, error)
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.mainColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Login", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Don't have an account?")
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(
                            onClick = onSignUpClick,
                            contentPadding = PaddingValues(0.dp) // Xóa padding mặc định
                        ) {
                            Text("Sign Up", color = AppTheme.colors.mainColor)
                        }
                    }

                }
            }
        }
    }

}

@Composable
fun SocialLoginButtons(
    onFacebookLogin: () -> Unit,
    onGoogleLogin: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            SocialButton(
                text = "Facebook",
                drawableId = R.drawable.fb_logo, // Ảnh từ drawable
                borderColor = Color.LightGray, onClick = onFacebookLogin // Gọi sự kiện khi bấm

            )
        }
        Box(modifier = Modifier.weight(1f)) {
            SocialButton(
                text = "Google",
                drawableId = R.drawable.google_logo, // Ảnh từ drawable
                borderColor = Color.LightGray,  onClick = onGoogleLogin // Gọi sự kiện khi bấm
            )
        }
    }
}


@Composable
fun SocialButton(text: String, drawableId: Int, borderColor: Color, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick, // Gọi sự kiện khi bấm
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFFF5F5F5), // Màu nền nhạt
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = text,
            modifier = Modifier.size(24.dp) // Định kích thước icon
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.Black)
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

