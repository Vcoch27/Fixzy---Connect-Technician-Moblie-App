package com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTypography
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography

@Composable
fun CategoriesSection(modifier: Modifier = Modifier) {
    val typography = LocalAppTypography.current
    Column(
        modifier = Modifier.fillMaxWidth()
    )
    {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .height(16.dp)
                        .width(4.dp)
                        .background(AppTheme.colors.mainColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Categories",
                    style = typography.titleSmall,
                    color = AppTheme.colors.onBackground
                )
            }

            Button(
                onClick = {},
                shape = RoundedCornerShape(40),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = AppTheme.colors.mainColor
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp), // Loại bỏ bóng
                border = ButtonDefaults.outlinedButtonBorder // Viền mỏng quanh nút
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "See All",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                        contentDescription = "Arrow",
                        tint = AppTheme.colors.onBackgroundVariant,
                        modifier = Modifier.size(14.dp) // Giảm kích thước biểu tượng
                    )
                }
            }


        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(categoryList.take(3)){
                category -> CategoryItem(category,typography)
            }
            item{
                SeeAllCategory({},typography)
            }
        }
//        LazyRow  {
//            items(categoryList) { category ->
//                CategoryItem(category)
//            }
//        }
    }
}


val categoryList = listOf(
    CategoryData("AC Repair", Icons.Default.Build, Color(0xFFFFE8AD)), // Màu vàng
    CategoryData("Beauty", Icons.Default.Face, Color(0xFFFC8EA7)), // Màu hồng
    CategoryData("Appliance", Icons.Default.Settings, Color(0xFF969CEC)) // Màu xanh dương
)

@Composable
fun CategoryItem(category: CategoryData,typography: AppTypography) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { /* chuyển đến danh mục cụ thể*/ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(56.dp).background(category.backgroundColor, shape = CircleShape),
            contentAlignment = Alignment.Center){
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(32.dp), // Kích thước icon nhỏ hơn
                tint = Color.DarkGray // Đảm bảo icon có màu trắng nổi bật
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = category.name, fontSize = 12.sp, style = typography.bodySmall)
//        Icon(category.icon, contentDescription = category.name, modifier = Modifier.size(40.dp))
//        Spacer(modifier = Modifier.height(4.dp))
//        Text(text = category.name, fontSize = 14.sp)
    }
}

@Composable
fun SeeAllCategory(onClick: () -> Unit,typography: AppTypography) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp) // Kích thước nền giống với CategoryItem
                .background(Color.LightGray, shape = CircleShape), // Nền bo tròn
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "See All",
                modifier = Modifier.size(24.dp), // Nhỏ hơn icon danh mục
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "See All", fontSize = 12.sp, style = typography.bodySmall)
    }
}

data class CategoryData(
    val name: String,
    val icon: ImageVector,
    val backgroundColor: Color // Thêm màu nền cho icon
)