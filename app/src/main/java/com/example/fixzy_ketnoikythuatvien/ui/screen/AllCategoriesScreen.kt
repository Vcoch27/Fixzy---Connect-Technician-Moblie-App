package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.ServiceService
import com.example.fixzy_ketnoikythuatvien.service.model.Service
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.CategoryItem
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "AllCategoriesScreen"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AllCategoriesScreen(navController: NavController, modifier: Modifier = Modifier) {
    val state by Store.stateFlow.collectAsState()
    val pagerState = rememberPagerState(pageCount = {
        (state.categories.size + 5) / 6
    })
    val serviceService = remember { ServiceService() }
    val coroutineScope = rememberCoroutineScope()
    var lastFetchJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    var lastSelectedCategory by remember { mutableStateOf<CategoryData?>(null) }

    Log.d(TAG, "Rendering AllCategoriesScreen: categories=${state.categories.size}, selectedCategory=${state.selectedCategory?.name}, services=${state.services.size}")

    // Debounce và hủy yêu cầu cũ khi category thay đổi
    LaunchedEffect(state.selectedCategory) {
        // Nếu category không thay đổi, không làm gì
        if (state.selectedCategory == lastSelectedCategory) return@LaunchedEffect

        // Hủy yêu cầu API cũ nếu có
        lastFetchJob?.cancel()
        lastSelectedCategory = state.selectedCategory

        state.selectedCategory?.let { category ->
            Log.i(TAG, "Category selected: ${category.name}, fetching services for categoryId=${category.categoryId}")

            // Debounce: chờ 300ms trước khi gọi API
            lastFetchJob = coroutineScope.launch {
                delay(300) // Debounce 300ms
                try {
                    serviceService.fetchServices(category.categoryId)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to fetch services for categoryId=${category.categoryId}: ${e.message}", e)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 10.dp, end = 10.dp)
    ) {
        TopBar(navController, "See All Categories")

        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    state.isLoadingCategories -> {
                        Log.d(TAG, "Categories loading state: isLoadingCategories=true")
                        Text(
                            text = "Đang tải...",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            textAlign = TextAlign.Center,
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colors.onBackground
                        )
                    }

                    state.categoriesError != null -> {
                        Log.e(TAG, "Categories error: ${state.categoriesError}")
                        Text(
                            text = "Lỗi: ${state.categoriesError}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            textAlign = TextAlign.Center,
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colors.onBackground
                        )
                    }

                    else -> {
                        Log.d(TAG, "Rendering categories: total=${state.categories.size}, pages=${pagerState.pageCount}")
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .padding(horizontal = 15.dp, vertical = 10.dp)
                        ) { page ->
                            val startIndex = page * 6
                            val endIndex = minOf(startIndex + 6, state.categories.size)
                            val pageCategories = state.categories.subList(startIndex, endIndex)
                            Log.d(TAG, "Rendering page $page: categories=${pageCategories.map { it.name }}")

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(pageCategories) { category ->
                                    CategoryItem(
                                        category = category,
                                        typography = LocalAppTypography.current,
                                        onClick = {
                                            Log.i(TAG, "Category clicked: ${category.name}")
                                            Store.store.dispatch(Action.SelectCategory(category))
                                        }
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(pagerState.pageCount) { index ->
                                val color = if (pagerState.currentPage == index) AppTheme.colors.onBackgroundVariant else Color.Gray
                                Canvas(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .padding(horizontal = 2.dp)
                                ) {
                                    drawCircle(color = color)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        state.selectedCategory?.let { category ->
                            Log.d(TAG, "Rendering services section for category: ${category.name}")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = category.name,
                                    style = AppTheme.typography.titleMedium,
                                    color = AppTheme.colors.mainColor
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = AppTheme.colors.onBackground,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Filter",
                                        tint = AppTheme.colors.onBackground,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Sort,
                                        contentDescription = "Sort",
                                        tint = AppTheme.colors.onBackground,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            when {
                                state.isLoadingServices -> {
                                    Log.d(TAG, "Services loading state: isLoadingServices=true")
                                    Text(
                                        text = "Đang tải dịch vụ...",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        textAlign = TextAlign.Center,
                                        style = AppTheme.typography.bodyMedium,
                                        color = AppTheme.colors.onBackground
                                    )
                                }

                                state.servicesError != null -> {
                                    Log.e(TAG, "Services error: ${state.servicesError}")
                                    Text(
                                        text = state.servicesError!!,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        textAlign = TextAlign.Center,
                                        style = AppTheme.typography.bodyMedium,
                                        color = AppTheme.colors.onBackground
                                    )
                                }

                                state.services.isEmpty() -> {
                                    Log.w(TAG, "No services found for category: ${category.name}")
                                    Text(
                                        text = "Không có dịch vụ nào",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        textAlign = TextAlign.Center,
                                        style = AppTheme.typography.bodyMedium,
                                        color = AppTheme.colors.onBackground
                                    )
                                }

                                else -> {
                                    Log.d(TAG, "Rendering services: total=${state.services.size}")
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(state.services) { service ->
                                            ServiceCard(service = service)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val TAG_SERVICE_CARD = "ServiceCard"
@Composable
fun ServiceCard(service: Service) {
    var isFavorite by remember { mutableStateOf(false) }
    Log.d(
        TAG_SERVICE_CARD,
        "Rendering ServiceCard: provider=${service.providerName}, service=${service.name}, isFavorite=$isFavorite"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface)
    ) {
        Log.d(TAG_SERVICE_CARD, "Rendering ServiceCard content for ${service.providerName}")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = "https://example.com/placeholder.jpg",
                contentDescription = "${service.providerName}'s avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.coc),
                error = painterResource(id = R.drawable.coc),
                onLoading = {
                    Log.d(TAG_SERVICE_CARD, "Loading avatar for ${service.providerName}")
                },
                onSuccess = {
                    Log.d(TAG_SERVICE_CARD, "Avatar loaded successfully for ${service.providerName}")
                },
                onError = {
                    Log.e(TAG_SERVICE_CARD, "Failed to load avatar for ${service.providerName}")
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = service.providerName?:"Unknown",
                    style = AppTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.onSurface
                )

                Text(
                    text = service.name,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onBackgroundVariant
                )

                Text(
                    text = "${service.price} VND",
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.mainColor
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val rating = service.rating.coerceIn(0.0, 5.0)
                    val fullStars = rating.toInt()
                    val hasHalfStar = rating % 1 >= 0.5
                    repeat(5) { index ->
                        Icon(
                            imageVector = when {
                                index < fullStars -> Icons.Default.Star
                                index == fullStars && hasHalfStar -> Icons.AutoMirrored.Filled.StarHalf
                                else -> Icons.Default.StarBorder
                            },
                            contentDescription = null,
                            tint = Color(0xFFF3B700),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = String.format("%.1f", service.rating),
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )

                    Text(
                        text = " | ${service.ordersCompleted} Đơn",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
            }

            IconButton(onClick = {
                isFavorite = !isFavorite
                Log.i(TAG_SERVICE_CARD, "Favorite button clicked for ${service.providerName}: isFavorite=$isFavorite")
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Xóa khỏi yêu thích" else "Thêm vào yêu thích",
                    tint = if (isFavorite) AppTheme.colors.secondarySurface else AppTheme.colors.onBackgroundVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}