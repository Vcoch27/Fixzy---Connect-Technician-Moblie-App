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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fixzy_ketnoikythuatvien.data.local.UserPreferences
import com.example.fixzy_ketnoikythuatvien.data.repository.AuthRepositoryImpl
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.AuthUiState
import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.AuthViewModel
//import com.example.fixzy_ketnoikythuatvien.ui.viewmodel.UserApiViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(
    context: Context = LocalContext.current,  //nguwx cảnh hiện tại
    onNavigateToHome: () -> Unit,  //callback trở lại màn hình chính
    onBackToLogin: () -> Unit      //callback la màn hình đăng nhập
) {
//    viewmodel xử lí logic API đồng bộ dữ liệu
//    val userApiViewModel = viewModel<UserApiViewModel>()

//view model quản lí trạng thái giao diện và logic xác thức
    val viewModel = remember {
        AuthViewModel(
            repository = AuthRepositoryImpl(context),  // thực hiện các thao tác backend như gửi dữ liệu lên từ server
            userPreferences = UserPreferences(context)  // Lưu thông tin người dùng vào bộ nhớ cục bộ
        )
    }
    val uiState by viewModel.uiState.collectAsState()  // quan sát tranng thái giao diện để xử lí kết quả đăng kí

   //các trạng thái
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) } // Checkbox trạng thái

    //theo dõi giao diện và thực hiện đồng bộsau khi đăng kí thành công
//    LaunchedEffect(uiState) {
//        if (uiState is AuthUiState.Success) {
//            val currentUser = FirebaseAuth.getInstance().currentUser  // Lấy người dùng hiện tại từ Firebase
//            Log.d("SIGNUP_SCREEN", "🔥 Firebase user: $currentUser")
//            currentUser?.let {
//                Log.d("SIGNUP_SCREEN", "✅ Syncing to API: ${it.uid}")
//                userApiViewModel.syncUserInfo(  //đồng bộ dữ lệu với backend thông qua api
//                    it.uid,
//                    it.email ?: "",
//                    fullName = name,
//                    phone = null
//                )
//            }
//            onNavigateToHome() // Chuyển hướng sang màn hình chính
//        }
//    }

    // Phần giao diện hiển thị giao diện đăng ký
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
                    // Các trường nhập liệu cho thông tin đăng ký
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

                    // Mật khẩu với tính năng hiển thị/ẩn
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

                    // Checkbox xác nhận điều khoản
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
                        Text(text = "I agree to the ")
                        ClickableText(text = AnnotatedString("Terms of Service"), onClick = { /* Mở điều khoản dịch vụ */ })
                        Text(text = " and ")
                        ClickableText(text = AnnotatedString("Privacy Policy"), onClick = { /* Mở chính sách bảo mật */ })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Xử lý đăng ký khi nhấn nút "Create Account"
                    Button(
                        onClick = {
                            when {
                                name.isEmpty() -> showToast(context, "Name cannot be empty")
                                email.isEmpty() -> showToast(context, "Email cannot be empty")
                                password.length < 6 -> showToast(context, "Password must be at least 6 characters")
                                password != confirmPassword -> showToast(context, "Passwords do not match")
                                !isChecked -> showToast(context, "Please accept the Terms of Service")
                                else -> viewModel.register(email, password) // Gọi ViewModel để đăng ký
                            }
                        },
                        enabled = isChecked,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor)
                    ) {
                        Text("Create Account", color = Color.White)
                    }

                    // Hiển thị lỗi khi có vấn đề xảy ra
                    if (uiState is AuthUiState.Error) {
                        Text((uiState as AuthUiState.Error).message, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Chuyển hướng đến trang đăng nhập
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Do you have an account?")
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = onBackToLogin, contentPadding = PaddingValues(0.dp)) {
                            Text("Login", color = AppTheme.colors.mainColor)
                        }
                    }
                }
            }
        }
    }
}

// Hàm hiển thị Toast khi có lỗi
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun textFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = AppTheme.colors.mainColor,
    unfocusedBorderColor = Color.Gray,
    focusedLabelColor = AppTheme.colors.mainColor,
    unfocusedLabelColor = Color.Gray,
    cursorColor = AppTheme.colors.mainColor,
    disabledBorderColor = Color.LightGray
)
