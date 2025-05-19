@file:Suppress("DEPRECATION")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixzy_ketnoikythuatvien.service.AuthService
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
// ui/screen/extendedScreen/AuthScreen.kt
@Composable
fun SignUpScreen(
    context: Context = LocalContext.current,
    onBackToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val authService = remember { AuthService(context =  context,
        activity = null,
        onSuccess = null,
        onError =null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    val TAG = "SIGNUP_SCREEN" // Thêm TAG để dễ nhận diện log

    Log.d(TAG, "SignUpScreen composed") // Log khi màn hình được hiển thị

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
            Text("Sign Up", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

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
                        colors = textFieldColors()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
                        Text(text = "I agree to the Terms of Service and Privacy Policy")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            Log.d(TAG, "Create Account button clicked")
                            Log.d(TAG, "Input data - Name: $name, Email: $email, Phone: $phone, Password: $password, ConfirmPassword: $confirmPassword, IsChecked: $isChecked") // Log dữ liệu đầu vào
                            when {
                                name.isEmpty() -> {
                                    Log.w(TAG, "Validation failed: Name is empty") // Log khi validate thất bại
                                    showToast(context, "Name cannot be empty")
                                }
                                email.isEmpty() -> {
                                    Log.w(TAG, "Validation failed: Email is empty") // Log khi validate thất bại
                                    showToast(context, "Email cannot be empty")
                                }
                                password.length < 6 -> {
                                    Log.w(TAG, "Validation failed: Password is less than 6 characters") // Log khi validate thất bại
                                    showToast(context, "Password must be at least 6 characters")
                                }
                                password != confirmPassword -> {
                                    Log.w(TAG, "Validation failed: Passwords do not match") // Log khi validate thất bại
                                    showToast(context, "Passwords do not match")
                                }
                                !isChecked -> {
                                    Log.w(TAG, "Validation failed: Terms of Service not accepted") // Log khi validate thất bại
                                    showToast(context, "Please accept the Terms of Service")
                                }
                                else -> {
                                    Log.i(TAG, "Validation passed, calling authService.signUp") // Log khi validate thành công
                                    authService.signUp(
                                        email = email,
                                        password = password,
                                        name = name,
                                        phone = phone,
                                        onSuccess = {
                                            Log.i(TAG, "Sign up successful, navigating to home") // Log khi đăng ký thành công
                                            showToast(context, "Sign up successful")
                                            onNavigateToHome()
                                        },
                                        onError = { error ->
                                            Log.e(TAG, "Sign up failed: $error") // Log khi đăng ký thất bại
                                            showToast(context, error)
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor)
                    ) {
                        Text("Create Account", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Do you have an account?")
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = {
                            Log.d(TAG, "Login button clicked, navigating to login") // Log khi nhấn nút Login
                            onBackToLogin()
                        }, contentPadding = PaddingValues(0.dp)) {
                            Text("Login", color = AppTheme.colors.mainColor)
                        }
                    }
                }
            }
        }
    }
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = AppTheme.colors.mainColor,
    unfocusedBorderColor = Color.Gray,
    focusedLabelColor = AppTheme.colors.mainColor,
    unfocusedLabelColor = Color.Gray,
    cursorColor = AppTheme.colors.mainColor,
    disabledBorderColor = Color.LightGray
)
