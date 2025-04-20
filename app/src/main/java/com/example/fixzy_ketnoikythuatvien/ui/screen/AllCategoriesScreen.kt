package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.CategoryService
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.CategoryItem
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTypography
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllCategoriesScreen(navController: NavController, modifier: Modifier = Modifier) {
    // Lấy trạng thái từ Store
    val state by Store.Companion.stateFlow.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tất cả danh mục") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoadingCategories -> {
                Text(
                    text = "Đang tải...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    textAlign = TextAlign.Center
                )
            }
            state.categoriesError != null -> {
                Text(
                    text = "Lỗi: ${state.categoriesError}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 cột
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.categories) { category ->
                        CategoryItem(
                            category= category,
                            typography = LocalAppTypography.current
                        )
                    }
                }
            }
        }
    }
}
