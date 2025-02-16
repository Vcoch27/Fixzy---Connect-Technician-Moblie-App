package com.example.fixzy_ketnoikythuatvien.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.data.model.ProductFlavorData
import com.example.fixzy_ketnoikythuatvien.data.model.ProductFlavorState
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun FlavorSection(
    modifier: Modifier = Modifier,
    data: List<ProductFlavorState> = ProductFlavorData
) {
    Column(
        modifier = modifier,

        ) {
        SectionHeader(
            title = "Flavor",
            emotion = "\uD83E\uDD29"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            data.forEach { item ->
                ProductFlavorItem(
                    state = item,
                    modifier = Modifier.weight(1f)
                )

            }
        }
    }
}

@Composable
private fun SectionHeader(modifier: Modifier = Modifier, title: String, emotion: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp) //cách đều 4dp giữa các phần tuwr trong row
    ) {
        Text(
            text = title,
            style = AppTheme.typography.titleLarge,
            color = AppTheme.colors.onBackground
        )
        Text(
            text = emotion,
            style = AppTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ProductFlavorItem(
    modifier: Modifier = Modifier,
    state: ProductFlavorState
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                spotColor = Color.LightGray,
                shape = RoundedCornerShape(28.dp)
            )
            .background(
                shape = RoundedCornerShape(28.dp),
                color = AppTheme.colors.regularSurface
            )
            .height(160.dp) // Đảm bảo chiều cao cố định
            .padding(8.dp) // Padding tổng thể để tránh nội dung bị cắt
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp), // Khoảng cách giữa các phần tử nhỏ hơn
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hình ảnh có kích thước cố định nhưng thấp hơn trước
            Image(
                painter = painterResource(id = state.imgRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(90.dp) // Giảm chiều cao ảnh để rút ngắn khoảng cách
                    .fillMaxWidth()
            )

            // Phần Text chứa tên và giá
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp), // Khoảng cách nhỏ giữa ảnh và chữ
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.name,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.onRegularSurface
                )
                Text(
                    text = "+${state.price}",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.onRegularSurface
                )
            }
        }
    }
}

