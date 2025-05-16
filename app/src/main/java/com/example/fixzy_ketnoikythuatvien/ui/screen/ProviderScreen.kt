package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.annotation.SuppressLint
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
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.service.ProviderService
import com.example.fixzy_ketnoikythuatvien.service.model.Booking
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderBooking
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceDetail
import com.example.fixzy_ketnoikythuatvien.ui.screen.controller.TopTechniciansController
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.google.common.base.Objects
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.text.NumberFormat

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProviderScreen(navController: NavController, modifier: Modifier = Modifier, providerId: Int) {
    var selectedService by remember { mutableStateOf<ServiceDetail?>(null) }
    val state by Store.stateFlow.collectAsState()
    val providerService = remember { ProviderService() }
    val bookingService = remember { BookingService() }
    val store = Store.store
    val isOwner = state.user?.id == providerId
    bookingService.getProviderBookings()
    Log.d("ProviderScreen", "isOwner: $isOwner")
    LaunchedEffect(providerId) {
        if (state.provider == null || state.provider?.name != "Test User") {
            providerService.fetchProviderDetails(providerId)
        }

    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (isOwner) {
                Button(
                    onClick = {
                        if (!isOwner) {
//                        selectedService?.let { service ->
//                            Log.d("ProviderScreen", "Selected service: $service")
//                            store.dispatch(
//                                Action.UpdateBooking(
//                                    serviceId = service.service_id,
//                                    userId = state.user?.id,
//                                    address = state.user?.address,
//                                    phone = state.user?.phone,
//                                    totalPrice = service.service_price.toDoubleOrNull()
//                                )
//                            )
//                            Log.d("ProviderScreen", "Dispatched UpdateBooking for serviceId: ${service.service_id}")
//
//                            val encodedName = URLEncoder.encode(service.service_name, "UTF-8").replace("+", "%20")
//                            navController.navigate("availability_screen/${service.service_id}?service_name=$encodedName")
//                            Log.d("ProviderScreen", "Navigated to availability_screen with serviceId: ${service.service_id}")
//                        }
                        } else {
                            navController.navigate("add_service_screen")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.mainColor
                    ),
                    enabled = isOwner || (selectedService != null)
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Book Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOwner) "Add service" else "",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
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
                        model = providerData.avatar_url
                            ?: "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcS5oOiRbP2oJSvvZSEkMfRYF5sprtL8ZlXw8J36BzvgfobTOoknbsgUSG1TBtE-jLtBPOP8b6TtvHq2GOOSxU5YLw",
                        contentDescription = "Provider Avatar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.coc),
                        error = painterResource(id = R.drawable.coc)
                    )

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
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
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

                    if (isOwner) {
                        item {
                            var selectedTab by remember { mutableStateOf(0) }
                            val tabs = listOf("Services", "Bookings")

                            TabRow(
                                selectedTabIndex = selectedTab,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                tabs.forEachIndexed { index, title ->
                                    Tab(
                                        selected = selectedTab == index,
                                        onClick = { selectedTab = index },
                                        text = { Text(title) }
                                    )
                                }
                            }

                            when (selectedTab) {
                                0 -> {
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(top = 16.dp)
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
                                        providerData.services.forEach { service ->
                                            ServiceCardd(
                                                service = service,
                                                isSelected = false,
                                                onSelect = {
                                                    Log.d(
                                                        "ProviderScreen",
                                                        "price serrvie: ${service.service_price.toDouble()}"
                                                    )
                                                    store.dispatch(
                                                        Action.UpdateTempPrice(price = service.service_price)
                                                    )
                                                    val encodedName = URLEncoder.encode(
                                                        service.service_name,
                                                        "UTF-8"
                                                    ).replace("+", "%20")
                                                    navController.navigate("availability_screen/${service.service_id}?service_name=$encodedName")
                                                },
                                                modifier = Modifier.padding(bottom = 8.dp),
                                                isOwner = isOwner,
                                                navController = navController
                                            )
                                        }
                                    }
                                }

                                1 -> {
                                    // Đổi Box thành Column ở đây
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Log.d(
                                            "ProviderScreen",
                                            "provider bookings: ${Store.stateFlow.value.providerBookings}}"
                                        )
                                        if (Store.stateFlow.value.providerBookings.isNotEmpty()) {
                                            Log.d(
                                                "ProviderScreen",
                                                "provider bookings count: ${Store.stateFlow.value.providerBookings.count()}"
                                            )
                                            Store.stateFlow.value.providerBookings.forEach { booking ->
                                                ProviderBookingCard(
                                                    navController  = navController,
                                                    booking = booking,
                                                    onClick = {},
                                                    onConfirm = {},
                                                    onCancel = {}
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = "Booking List Null",
                                                style = AppTheme.typography.bodyMedium,
                                                color = AppTheme.colors.onBackground
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        item {
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
                        items(providerData.services) { service ->
                            ServiceCardd(
                                service = service,
                                onSelect = { selectedService = service },
                                isSelected = selectedService == service,
                                modifier = Modifier.padding(bottom = 8.dp),
                                isOwner = isOwner,
                                navController = navController,
                            )
                        }
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
    modifier: Modifier = Modifier,
    isOwner: Boolean = false,
    navController: NavController,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (isOwner) (navController.navigate("service_provider_mode/${service.service_id}/${service.service_name}")) else (navController.navigate(
                    "availability_screen/${service.service_id}?service_name=${service.service_name}"
                ))
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected && !isOwner) AppTheme.colors.mainColor.copy(alpha = 0.1f)
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
                        text = "${formatCurrency(service.service_price.toDouble())} VND",
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
//            if (!isOwner) {
//                RadioButton(
//                    selected = isSelected,
//                    onClick = { onSelect() },
//                    colors = RadioButtonDefaults.colors(
//                        selectedColor = AppTheme.colors.mainColor,
//                        unselectedColor = AppTheme.colors.onBackgroundVariant
//                    )
//                )
//            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun ProviderBookingCard(
    navController: NavController,
    booking: ProviderBooking,
    onClick: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isToday = try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val bookingDate = inputFormat.parse(booking.bookingDate)?.toInstant()
        val today = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate()
        bookingDate?.atZone(ZoneId.of("Asia/Ho_Chi_Minh"))?.toLocalDate() == today
    } catch (e: Exception) {
        false
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { navController.navigate("service_provider_mode/${booking.serviceId}/Service") },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isToday) AppTheme.colors.mainColor.copy(alpha = 0.1f) else AppTheme.colors.surface
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
//                    text = "Mã đặt lịch: ",
                    text = "Mã đặt lịch: ${booking.referenceCode}",
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.onSurface
                )
                Text(
//                    text = "Khách hàng:",
                    text = "Khách hàng: ${booking.fullName}",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onBackgroundVariant
                )
                Text(
//                    text = "Ngày:",
                    text = "Ngày: ${formatDateTime(booking.bookingDate, booking.bookingTime)}",
                    style = AppTheme.typography.bodySmall,
                    color = if (isToday) AppTheme.colors.mainColor else AppTheme.colors.onBackgroundVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
//                        text = "Trạng thái:",
                        text = "Trạng thái: ${booking.status}",
                        style = AppTheme.typography.bodySmall,
                        color = when (booking.status.lowercase()) {
                            "pending" -> Color.Red
                            "confirmed" -> Color.Green
                            else -> AppTheme.colors.onBackgroundVariant
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Giá: ${formatCurrency(booking.totalPrice.toDouble())} VND",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.mainColor
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(80.dp, 30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green.copy(alpha = 0.8f),
                            contentColor = Color.White
                        ),
                        enabled = booking.status.equals("Pending", ignoreCase = true)
                    ) {
                        Text("Xác nhận", fontSize = 12.sp)
                    }
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.size(80.dp, 30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.8f),
                            contentColor = Color.White
                        ),
                        enabled = booking.status.equals(
                            "Pending",
                            ignoreCase = true
                        ) || booking.status.equals("Confirmed", ignoreCase = true)
                    ) {
                        Text("Hủy", fontSize = 12.sp)
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View Details",
                    tint = AppTheme.colors.onBackgroundVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun formatDateTime(date: String, time: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val parsedDate = inputFormat.parse(date) ?: Date()
        val formattedDate = outputFormat.format(parsedDate)
        if (formattedDate.startsWith("16 May 2025")) {
            "Hôm nay, $time"
        } else {
            "$formattedDate ($time)"
        }
    } catch (e: Exception) {
        "$date $time"
    }
}

private fun formatCurrency(amount: Double): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(amount)
}