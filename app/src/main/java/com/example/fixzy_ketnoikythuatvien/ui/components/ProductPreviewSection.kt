package com.example.fixzy_ketnoikythuatvien.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun ProductPreviewSection(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.height(IntrinsicSize.Max)
    ) {
        ProductBackground(
            modifier = Modifier.padding(bottom = 70.dp)
        )
        Content(
            modifier = Modifier.statusBarsPadding() // Thêm khoảng cách bằng kích thước của status để tránh bị che khuất bởi thanh trạng thái
        )
    }
}

@Composable
private fun ProductBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()//lấp đầy toàn bộ không gian của parent container
            .background(
                color = AppTheme.colors.secondarySurface, //
                shape = RoundedCornerShape( //bo góc cho box
                            bottomEnd = 32.dp,
                            bottomStart =32.dp
                )

            )
    )
}

@Composable
private fun Content(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth() // Đảm bảo các layout có độ rộng tối đa
    ) {
        // Hiển thị ActionBar
        ActionBar(
            headline = "Hikigaeru",
            modifier = Modifier
                .padding(horizontal = 25.dp,vertical =15.dp)
                .align(Alignment.TopCenter) // Căn chỉnh ActionBar ở trên, giữa

        )
        Image(
            painter = painterResource(id = R.drawable.demo_logo),
            contentDescription = null,
            contentScale = ContentScale.FillHeight, // Căn chỉnh hình ảnh theo chiều cao mà không bị méo
            modifier = Modifier
                .height(250.dp)
                .padding(start = 140.dp, top = 20.dp,)
                .padding(vertical =20.dp)
        )
    }
}


@Composable
private fun ActionBar(
    modifier: Modifier = Modifier, // Giúp tùy chỉnh giao diện
    headline: String // Tiêu đề của thanh ActionBar
) {
    Row(
        modifier = modifier.fillMaxWidth(), // Row sẽ kéo dài toàn bộ chiều rộng của màn hình
        verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
        horizontalArrangement = Arrangement.SpaceBetween // Các phần tử bên trong được đặt cách đều nhau
    ) {
        Text(
            text = headline, // Hiển thị nội dung tiêu đề
            style = AppTheme.typography.headline, // Áp dụng kiểu chữ từ theme ứng dụng
            color = AppTheme.colors.onSecondarySurface // Lấy màu chữ từ theme của ứng dụng
        )
        // Nút đóng (CloseButton)
        CloseButton()
    }
}

@Composable
private fun CloseButton(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(44.dp), // Áp dụng modifier truyền vào để tùy chỉnh giao diện
        shape = RoundedCornerShape(16.dp), // Bo góc cho nút với bán kính 16.dp
        color = AppTheme.colors.actionSurface, // Nền của nút
        contentColor = AppTheme.colors.secondarySurface // Màu nội dung
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search), // Biểu tượng đóng nút
                contentDescription = null, // Không có mô tả nội dung
                modifier = Modifier.size(24.dp) // Đặt kích cỡ của icon
            )
        }
    }
}
