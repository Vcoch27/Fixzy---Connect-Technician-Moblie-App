package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.data.model.ProductHighlightState
import com.example.fixzy_ketnoikythuatvien.data.model.ProductHighlightType
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun ProductHighlights(
    highlights: List<ProductHighlightState>,//Danh sách các highlight của sản phẩm
    modifier: Modifier = Modifier,//để tùy chỉnh giao diện (măặc định không có tùy chỉnh)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start, //căn các phần tử về phía trái
        verticalArrangement = Arrangement.spacedBy(4.dp) //khoảng cách giữa các phần từ là 4.dp
    ) {
        //
        highlights.forEach { item ->
            Hightlight(
                text = item.text,
                colors = HighlightDefaults.colors(item.type)//lấy màu sắc tương ứng với kiểu highlight
            )

        }
    }
}

@Composable
private fun Hightlight(
    modifier: Modifier = Modifier,
    text: String,
    colors: HighlightColors = HighlightDefaults.defaultColors
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 50),//tạo hình dáng s bo góc 50%
        color = colors.containerColor,
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                style = AppTheme.typography.titleSmall,
                color = colors.contentColor
            )
        }
    }
}

@Immutable //đảm bảo dữ liêệu không thay đổi sau khi khời tạo
private data class HighlightColors(
    val contentColor: Color,
    val containerColor: Color,
)

//dối tượng  các maù mặc định  cho highlight
private object HighlightDefaults {
    //Color.Unspecified đẻ áp dụng màu cụ thể
    val defaultColors = HighlightColors(
        contentColor = Color.Unspecified,
        containerColor = Color.Unspecified
    )

    //hảm trả về màu sắc ph hợp với từng loại  ProductHighlightType
    @Composable
    fun colors(type: ProductHighlightType): HighlightColors {

        return when (type) {
            ProductHighlightType.SECONDARY -> HighlightColors(
                containerColor = AppTheme.colors.highlightSurface,  // Màu nền của PRIMARY
                contentColor = AppTheme.colors.onHighlightSurface   // Màu chữ của PRIMARY
            )

            ProductHighlightType.PRIMARY -> HighlightColors(
                containerColor = AppTheme.colors.actionSurface,     // Màu nền của SECONDARY
                contentColor = AppTheme.colors.onActionSurface      // Màu chữ của SECONDARY
            )
        }
    }

}