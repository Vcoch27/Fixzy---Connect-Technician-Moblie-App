package com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun TopBar(navController: NavController, title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier, // ✅ Dùng modifier từ bên ngoài
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            Modifier
                .height(25.dp)
                .width(5.dp)
                .clip(RoundedCornerShape(50))
                .background(AppTheme.colors.mainColor)
        )

        Text(
            text = title,
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.mainColor,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
