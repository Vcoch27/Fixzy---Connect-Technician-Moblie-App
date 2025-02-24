package com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography

@Composable
fun GreetingSection() {
    val typography = LocalAppTypography.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Hello Cóc 👋",
            style = typography.titleMedium,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "What you are looking for today",
            style = typography.headline,
            color = AppTheme.colors.onBackground
        )
    }
}
