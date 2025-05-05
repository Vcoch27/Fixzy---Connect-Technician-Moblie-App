package com.example.fixzy_ketnoikythuatvien.ui.screen

import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.ProviderService
import com.example.fixzy_ketnoikythuatvien.service.model.Booking
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderData
import com.example.fixzy_ketnoikythuatvien.service.model.Service
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceDetail
import com.example.fixzy_ketnoikythuatvien.ui.screen.controller.CategoryController
import com.example.fixzy_ketnoikythuatvien.ui.screen.controller.TopTechniciansController
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.google.common.base.Objects
import java.net.URLEncoder


@Composable
fun ProviderScreen(navController: NavController, modifier: Modifier = Modifier, providerId: Int) {
    // State để theo dõi dịch vụ được chọn
    var selectedService by remember { mutableStateOf<ServiceDetail?>(null) }
    val state by Store.stateFlow.collectAsState()
    val providerService = remember { ProviderService() }
     val store = Store.store

    LaunchedEffect(providerId) {
        if (state.provider == null || state.provider?.name != "Test User") { // Điều kiện để tránh gọi lại nếu đã tải
            providerService.fetchProviderDetails(providerId)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Button(
                onClick = {
                    selectedService?.let { service ->
                        Log.d("ProviderScreen", "Selected service: $service")
                        // Dispatch hành động UpdateBooking
//                        store.dispatch(
//                            Action.UpdateBooking(
//                                serviceId = service.service_id,
//                                userId = state.user?.id,
//                                address = state.user?.address,
//                                phone = state.user?.phone,
//                                totalPrice = service.service_price.toDoubleOrNull()
//                            )
//                        )
                        store.dispatch(
                            Action.UpdateBooking(
                                serviceId = service.service_id,
                                userId = state.user?.id,
                                address = state.user?.address,
                                phone = state.user?.phone,
                                totalPrice = service.service_price.toDoubleOrNull()
                            )
                        )
                        Log.d("ProviderScreen", "Dispatched UpdateBooking for serviceId: ${service.service_id}")

                        val encodedName = URLEncoder.encode(service.service_name, "UTF-8").replace("+", "%20")
                        navController.navigate("availability_screen/${service.service_id}?service_name=$encodedName")
                        Log.d("ProviderScreen", "Navigated to availability_screen with serviceId: ${service.service_id}")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.mainColor
                ),
                enabled = selectedService != null
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Book Icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Continue to Book",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoadingProvider) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.providerError != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.providerError!!, color = AppTheme.colors.onBackground)
                }
            } else if (state.provider != null) {
                val providerData = state.provider!!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = (LocalConfiguration.current.screenHeightDp / 4).dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                ) {
                    AsyncImage(
                        model = providerData.avatar_url ?: "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcS5oOiRbP2oJSvvZSEkMfRYF5sprtL8ZlXw8J36BzvgfobTOoknbsgUSG1TBtE-jLtBPOP8b6TtvHq2GOOSxU5YLw" ,
                        contentDescription = "Provider Avatar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.coc),
                        error = painterResource(id = R.drawable.coc)
                    )
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        // Tên và nghề nghiệp
                        Column {
                            Text(
                                text = providerData.name,
                                style = AppTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.onBackground
                            )
                            Text(
                                text = providerData.job,
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colors.onBackgroundVariant
                            )
                        }
                    }

                    item {
                        // Rating, Orders Completed, Contact
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            // Rating
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(AppTheme.colors.mainColor.copy(alpha = 0.1f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = AppTheme.colors.mainColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = String.format("%.2f", providerData.rating),
                                    style = AppTheme.typography.bodyMedium,
                                    color = AppTheme.colors.onBackground
                                )
                            }
                            // Orders Completed
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(AppTheme.colors.mainColor.copy(alpha = 0.1f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Orders Completed",
                                    tint = AppTheme.colors.mainColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = providerData.ordersCompleted.toString(),
                                    style = AppTheme.typography.bodyMedium,
                                    color = AppTheme.colors.onBackground
                                )
                            }
                            // Contact
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(AppTheme.colors.mainColor.copy(alpha = 0.1f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Contact",
                                    tint = AppTheme.colors.mainColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Contact",
                                    style = AppTheme.typography.bodyMedium,
                                    color = AppTheme.colors.onBackground
                                )
                            }
                        }
                    }

                    item {
                        // Bio
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Bio",
                                tint = AppTheme.colors.onBackgroundVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = providerData.bio,
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colors.onBackground
                            )
                        }
                    }

                    item {
                        // Skills
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Skills",
                                tint = AppTheme.colors.onBackgroundVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Skills",
                                style = AppTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.colors.onBackground
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            providerData.skills.forEach { skill ->
                                FilterChip(
                                    selected = true,
                                    onClick = { },
                                    label = { Text(skill) },
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        // Danh sách dịch vụ
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Services",
                                tint = AppTheme.colors.onBackgroundVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Services",
                                style = AppTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.colors.onBackground
                            )
                        }
                    }

                    // Danh sách dịch vụ
                    items(providerData.services) { service ->
                        ServiceCardd(
                            service = service,
                            onSelect = { selectedService = service },
                            isSelected = selectedService == service,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCardd(
    service: ServiceDetail,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AppTheme.colors.mainColor.copy(alpha = 0.1f)
            else AppTheme.colors.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = service.service_name,
                    style = AppTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Money,
                        contentDescription = "Price",
                        tint = AppTheme.colors.onBackgroundVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${service.service_price} VND",
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.mainColor
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFF3B700),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = String.format("%.1f", service.service_rating),
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Orders Completed",
                        tint = AppTheme.colors.onBackgroundVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${service.service_orders_completed} Đơn",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
            }
            RadioButton(
                selected = isSelected,
                onClick = { onSelect() },
                colors = RadioButtonDefaults.colors(
                    selectedColor = AppTheme.colors.mainColor,
                    unselectedColor = AppTheme.colors.onBackgroundVariant
                )
            )
        }
    }
}