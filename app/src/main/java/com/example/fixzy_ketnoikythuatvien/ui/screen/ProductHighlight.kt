package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
private fun Hightlight(
    modifier: Modifier = Modifier,
    text: String,
    colors: HighlightColors = HighlightColors.Primary
) {
    Surface (
        modifier= modifier,
        shape = RoundedCornerShape(percent = 50),//tạo hình dáng s bo góc 50%
        contentColor = colors.contentColor
    ){  }
}