package com.example.fixzy_ketnoikythuatvien.ui.components.profileComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.redux.data_class.AppState
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun ProfileHeader(modifier: Modifier = Modifier,state: AppState) {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = rememberAsyncImagePainter(
                model = state.user?.avatarUrl ?: R.drawable.coc
            ),
            contentDescription = "Profile Picture",
            modifier = Modifier.size(150.dp).clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = state.user?.name ?: "No Name",
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.mainColor,
        )
    }
}