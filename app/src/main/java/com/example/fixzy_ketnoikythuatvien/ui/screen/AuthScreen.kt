package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixzy_ketnoikythuatvien.ui.components.authComponents.AuthButton
import com.example.fixzy_ketnoikythuatvien.ui.components.authComponents.AuthCheckBox
import com.example.fixzy_ketnoikythuatvien.ui.components.authComponents.AuthHeader
import com.example.fixzy_ketnoikythuatvien.ui.components.authComponents.AuthSocialButtons
import com.example.fixzy_ketnoikythuatvien.ui.components.authComponents.AuthTextField

@Composable
fun AuthScreen(isSignUp: Boolean, onSwitchScreen: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthHeader(title = if (isSignUp) "Sign up" else "Login")

        AuthSocialButtons()

        Text("Or", modifier = Modifier.padding(vertical = 8.dp), fontSize = 14.sp, color = Color.Gray)

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            if (isSignUp) {
                AuthTextField(value = name, onValueChange = { name = it }, placeholder = "Name")
            }
            AuthTextField(value = email, onValueChange = { email = it }, placeholder = "Email/Phone Number")
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true,
                isPasswordVisible = isPasswordVisible,
                onPasswordToggle = { isPasswordVisible = !isPasswordVisible }
            )

            if (isSignUp) {
                AuthCheckBox (isChecked, onCheckedChange = { isChecked = it })
            } else {
                Text(
                    "Forgot Password?",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(vertical = 8.dp),
                    fontSize = 14.sp,
                    color = Color.Blue
                )
            }

            AuthButton(text = if (isSignUp) "Create Account" else "Login") {
                // Handle Sign Up / Login Action
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isSignUp) "Do you have an account? Login" else "Don't have an account? Sign Up",
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSwitchScreen() }
            )
        }
    }
}
