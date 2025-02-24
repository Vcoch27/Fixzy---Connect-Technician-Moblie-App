package com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography

@Composable
fun OffersSection(modifier: Modifier = Modifier) {
    val typography = LocalAppTypography.current
    Column(
        modifier = Modifier.fillMaxWidth()
    )
    {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    Modifier.height(16.dp).width(4.dp).background(AppTheme.colors.mainColor)
                )
                Spacer(modifier=Modifier.width(8.dp))
                Text(
                    text = "Offers",
                    style = typography.titleSmall,
                    color = AppTheme.colors.onBackground
                )
            }

            Button(
                onClick = {},
                shape = RoundedCornerShape(40),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = AppTheme.colors.mainColor
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp), // Loại bỏ bóng
                border = ButtonDefaults.outlinedButtonBorder // Viền mỏng quanh nút
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "See All",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                        contentDescription = "Arrow",
                        tint = AppTheme.colors.onBackgroundVariant,
                        modifier = Modifier.size(14.dp) // Giảm kích thước biểu tượng
                    )
                }
            }


        }
        Spacer(modifier = Modifier.height(8.dp))
        val colors = listOf(
            Color(0xFFFFDBDE), // Màu đỏ nhạt
            Color(0xFFDEF6DE), // Màu xanh lá nhạt
            Color(0xFFCEE7FA), // Màu xanh dương nhạt
            Color(0xFFFCF7D7)  // Màu vàng nhạt
        )

        LazyRow {
            items(4) { index ->
                OfferCard(backgroundColor = colors[index % colors.size]) // Chọn màu dựa trên index
            }
        }

    }
}

@Composable
private fun OfferCard(modifier: Modifier = Modifier,backgroundColor: Color) {
    val typography = LocalAppTypography.current
    Card(
        modifier = Modifier.padding(end = 8.dp).width(260.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Offer AC Service",
                    style = typography.bodySmall,
                    color = AppTheme.colors.onBackground
                )
                IconButton (onClick = { /* Mở rộng thông tin */ }) {
                    Icon(Icons.Default.Info, contentDescription = "Thông tin",modifier=Modifier.size(20.dp))
                }
            }
            Text(text = "Get 25%", style = typography.headline, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Nhận ưu đãi */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = AppTheme.colors.onBackgroundVariant
                ),
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Grab Offer", style = typography.label)
                    Spacer(modifier = Modifier.width(4.dp)) // Giảm khoảng cách giữa Text và Icon
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                        contentDescription = "Arrow",
                        tint = AppTheme.colors.onBackgroundVariant,
                        modifier = Modifier.size(12.dp) // Giảm kích thước biểu tượng
                    )
                }
            }


        }
    }
}