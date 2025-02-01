package com.example.fixzy_ketnoikythuatvien.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext


//bao bọc các thành phần giao diện trong ứng  Jetpack Compose (framework của google)
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        //cung cấp giá trị cục bộ cho toàn bộ thành phần con trong giao diện
        //gán extendedColors vaào biến LocalAppColors
        //thành phần còn có thể sử dụng màu sắc từ extendedColors mà không cần truyền tham số
        LocalAppColors provides extendedColors,
        LocalAppTypography provides extendedTypography
    ) {
        MaterialTheme(
//        áp dụng Material Design 3 cho giao diện
            //định nghĩa kiểu chữ
            typography = Typography,
            //hiển thị giao diện con được truyển vào
            content = content
        )
    }
}

//singleton object (đối tượng duy nhất trong toaàn bộ ứng dụng)
//cung cấp một cách dễ dàng truy cấp màu sắc của theme
object AppTheme {
    //truy cập màu sắc hiện tại của ứng dụng
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current
}