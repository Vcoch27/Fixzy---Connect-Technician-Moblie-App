package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.data.model.OrderData
import com.example.fixzy_ketnoikythuatvien.data.model.OrderState
import com.example.fixzy_ketnoikythuatvien.data.model.ProductDescriptionData
import com.example.fixzy_ketnoikythuatvien.data.model.ProductFlavorData
import com.example.fixzy_ketnoikythuatvien.data.model.ProductFlavorState
import com.example.fixzy_ketnoikythuatvien.data.model.ProductNutritionData
import com.example.fixzy_ketnoikythuatvien.data.model.ProductNutritionState
import com.example.fixzy_ketnoikythuatvien.data.model.ProductPreviewState
import com.example.fixzy_ketnoikythuatvien.ui.components.FlavorSection
import com.example.fixzy_ketnoikythuatvien.ui.components.OrderActionBar
import com.example.fixzy_ketnoikythuatvien.ui.components.ProductDescriptionSection
import com.example.fixzy_ketnoikythuatvien.ui.components.ProductNutritionSection
import com.example.fixzy_ketnoikythuatvien.ui.components.ProductPreviewSection

@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,//tùy chỉnh giao diện của composable
    productPreviewState: ProductPreviewState = ProductPreviewState(),
    productFlavors: List<ProductFlavorState> = ProductFlavorData,
    productNutritionState: ProductNutritionState = ProductNutritionData,
    productDescription: String = ProductDescriptionData,
    orderState: OrderState = OrderData,
    //() -> Unit: hàm không có tham số và không trả về giá trị (tương đương với hàm void trong Java)
    onAddItemClicked: () -> Unit = {},
    onRemoveItemClicked: () -> Unit = {},
    onCheckOutClicked: () -> Unit = {},
    navController: NavController
) {
    //vì box không cuộn đượ, nhưng trong Content() bên trong nó chưa Column có thể cuộn, mà ActionBar thì cố dịnh ở cuối Box -> hiệuuuwngs ssticky
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Content(
            modifier = Modifier.fillMaxWidth(),

            productFlavors = productFlavors,
            productPreviewState = productPreviewState,
            productNutritionState = productNutritionState,
            productDescription = productDescription
        )
        OrderActionBar(
            state = orderState,
            onAddItemClick = onAddItemClicked,
            onRemoveItemClick = onRemoveItemClicked,
            onCheckOutClick = onCheckOutClicked,
            modifier = Modifier
                .navigationBarsPadding() //padding tránh điều hướng
                .padding(horizontal = 18.dp, vertical = 8.dp),
            navController = navController

        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    productFlavors: List<ProductFlavorState>,
    productPreviewState: ProductPreviewState,
    productNutritionState: ProductNutritionState,
    productDescription: String

) {
    //tạo trạng thái cuộn màn hình  -> ghi nhớ vị trí cuộn
    val scrollState = rememberScrollState()
    Column(
        //áp dụng trạng thái cuộn vào column để có thể cuộn theo chiều dọc
        modifier = modifier.verticalScroll(scrollState)
    ) {
        //gọi composable
        ProductPreviewSection(
            state = productPreviewState
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        FlavorSection(
            modifier = Modifier.padding(18.dp),
            data = productFlavors
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        ProductNutritionSection(
            modifier = Modifier.padding(18.dp),
            state = productNutritionState
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        ProductDescriptionSection(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 18.dp)
                .padding(bottom = 120.dp),
            productDescription = productDescription
        )
    }
}