package com.example.fixzy_ketnoikythuatvien.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fixzy_ketnoikythuatvien.R

//định nghĩa một FontFamily
private val UnBoundedFontFamily = FontFamily(
    Font(R.font.font_unbounded_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.font_unbounded_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.font_unbounded_light, FontWeight.Light, FontStyle.Normal),
)

//Đại diện một tập hợp các kiểu chữ cho ứng dụng
@Immutable
data class AppTypography(
    val headline: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val body: TextStyle,
    val bodySmall: TextStyle,
    val bodyMedium: TextStyle,
    val label: TextStyle
)

//cung cấp các giá trị mặc định cho kiểu chữ trong toàn bộ ứng dụng (không cần truyền lại kiểu chữ qua cá tham số)
//có giá trị mặc định cho tất cả các kiểu chữ ( TextStyle.Default ) sử dụng kiểu chữ mặt định của Compose nêu không được ung cấp kiểu chữ tùy chỉnh
//staticCompositionLocalOf (một công cụ của Jetpack Compose để quản lý giá trị cục bộ cho các thành phần trong giao diện).
val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        headline = TextStyle.Default,
        titleLarge = TextStyle.Default,
        titleMedium = TextStyle.Default,
        titleSmall = TextStyle.Default,
        body = TextStyle.Default,
        bodySmall = TextStyle.Default,
        bodyMedium = TextStyle.Default,
        label = TextStyle.Default
    )
}
//Đối tượng AppTypography với các giá trị tùy chỉnh cho các kiểu chữ, sử dụng font UnboundedFontFamily
val extendedTypography = AppTypography(
    headline = TextStyle(
        fontFamily = UnBoundedFontFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = TextStyle(
        fontFamily = UnBoundedFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    titleMedium = TextStyle(
        fontFamily = UnBoundedFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal
    ),
    titleSmall = TextStyle(
        fontFamily = UnBoundedFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    body = TextStyle(
        fontFamily = UnBoundedFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle(
        fontFamily = UnBoundedFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = UnBoundedFontFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    ),
    label = TextStyle(
        fontFamily = UnBoundedFontFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.Light
    )
)
