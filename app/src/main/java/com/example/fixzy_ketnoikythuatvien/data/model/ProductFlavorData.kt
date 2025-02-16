package com.example.fixzy_ketnoikythuatvien.data.model

import androidx.annotation.DrawableRes
import com.example.fixzy_ketnoikythuatvien.R

//Định nghĩa dữ liệu cho từng hương vị (Flavor)
data class ProductFlavorState(
    val name: String,
    val price: String,
    @DrawableRes val imgRes: Int //bảo rằng giá trị được truyền vào là ID của một tài nguyên drawable
)
val ProductFlavorData = listOf(
    ProductFlavorState(
        imgRes = R.drawable.flavor1,
        name = "Ếch 1",
        price = "100.000đ"
    ),
    ProductFlavorState(
        imgRes = R.drawable.flavor2,
        name = "Ếch 2",
        price = "100.000đ"
    ),
    ProductFlavorState(
        imgRes = R.drawable.flavor3,
        name = "Ếch 3",
        price = "100.000đ"
    ),
)