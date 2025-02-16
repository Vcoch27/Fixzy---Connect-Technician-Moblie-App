package com.example.fixzy_ketnoikythuatvien.data.model

import androidx.annotation.DrawableRes
import com.example.fixzy_ketnoikythuatvien.R

//data class  dùng để lưu trữ thông tin của một sản phẩm nổi bật
data class ProductHighlightState(
    val text: String,
    val type: ProductHighlightType,
)

//enumeration : liệt kê
//emun để phân loại các highlight sản phẩm nổi bật
enum class ProductHighlightType{
    PRIMARY,
    SECONDARY
}

//Mô tả sản phầm trong phần xem trước
data class  ProductPreviewState(
    val healine: String = "Mr.Coc",
    @DrawableRes val productImg: Int = R.drawable.coc,
    val highlights: List <ProductHighlightState> = listOf(
        ProductHighlightState(
            text = "Sản phẩm nổi bật",
            type = ProductHighlightType.PRIMARY
        ),
        ProductHighlightState(
            text = "Sản phẩm mới",
            type = ProductHighlightType.SECONDARY
        )
    )
)