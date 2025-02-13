package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fixzy_ketnoikythuatvien.ui.components.ProductPreviewSection

@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier //tùy chỉnh giao diện của composable
){
    //tạo trạng thái cuộn màn hình  -> ghi nhớ vị trí cuộn
    val scrollState = rememberScrollState()
    Column (
        //áp dụng trạng thái cuộn vào column để có thể cuộn theo chiều dọc
        modifier = modifier.verticalScroll(scrollState)
    ){
        //gọi composable
        ProductPreviewSection()
    }
}