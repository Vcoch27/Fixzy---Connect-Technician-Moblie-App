package com.example.fixzy_ketnoikythuatvien.ui.components.authComponents

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthSocialButtons() {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SocialButton("Facebook")
        SocialButton("Google")
    }
}

@Composable
fun SocialButton(platform: String) {
    Row(modifier = Modifier.fillMaxWidth()) { // Row để sử dụng weight
        Button(
            onClick = { /* Handle Social Login */ },
            modifier = Modifier.weight(1f) // Sử dụng weight đúng cách
        ) {
            Text(text = platform)
        }
    }
}
