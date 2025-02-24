package com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import coil.compose.rememberImagePainter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography

data class Service(
    val imageRes: Int,
    val title: String,
    val provider: String,
    val price: String,
    val rating: Double,
    val reviews: Int
)

val serviceList = listOf(
    Service(R.drawable.coc, "Electrical expert", "Ahmed Hassan", "200 EGP/Hour", 4.8, 839),
    Service(R.drawable.coc, "Electrical expert", "Ahmed Hassan", "200 EGP/Hour", 4.8, 839)
)

@Composable
fun TopTechniciansSection(modifier: Modifier = Modifier) {
    val typography = LocalAppTypography.current
    var selectedCategory by remember { mutableStateOf("All") }
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
                    text = "Top technicians",
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
                        modifier = Modifier.size(14.dp)
                    )
                }
            }


        }
        Spacer(modifier = Modifier.height(8.dp))
        FilterSection(selectedCategory = selectedCategory) {
            selectedCategory = it
        }
        Spacer(modifier = Modifier.height(8.dp))
        ServiceListSection(selectedCategory = selectedCategory)

    }
}

//-----------------------------------
//Bộ lọc danh mục (FilterSection)
@Composable
fun FilterSection(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("All", "Electrical", "Plumber", "Painting", "Carpenter", "Mason")
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(categories) { category ->
            FilterChip(category, isElected = category == selectedCategory) {
                onCategorySelected(category)
            }
        }
    }
}

@Composable
fun FilterChip(category: String, isElected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isElected) AppTheme.colors.mainColor else Color.White,
            contentColor = if (isElected) Color.White else AppTheme.colors.mainColor
        ),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .height(36.dp)
            .padding(horizontal = 4.dp),
        border = BorderStroke(1.dp, AppTheme.colors.mainColor)
    ) {
        Text(text = category, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

//----------------
//Hiển thị danh sách dịch vụ

@Composable
fun ServiceListSection(selectedCategory: String) {
    val filteredList = serviceList.filter {
        it.title.contains(
            selectedCategory,
            ignoreCase = true
        ) || selectedCategory == "All"
    }
    if (filteredList.isEmpty()) {
        Text(
            text = "No services available",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            style = AppTheme.typography.bodySmall
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filteredList.forEach { service ->
                ServiceCard(service)
            }
        }

    }

}

@Composable
fun ServiceCard(service: Service) {
//    Text(text = "abc", style = LocalAppTypography.current.titleMedium, color = Color.Blue)
    var isFavorite by remember { mutableStateOf(false) }//trạng thái yêu thích
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = service.imageRes),
                contentDescription = service.title,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = service.title, style = AppTheme.typography.titleSmall)
                Text(
                    text = service.provider,
                    style = AppTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = service.price,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.mainColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700)
                    )
                    Text(
                        text = "${service.rating} | ${service.reviews} Reviews",
                        style = AppTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            IconButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if(isFavorite) "Add to favorites" else "Remove from favorites",
                    tint = if(isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}
