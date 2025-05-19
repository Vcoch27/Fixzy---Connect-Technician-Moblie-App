package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.Availability
import com.example.fixzy_ketnoikythuatvien.service.ProviderService
import com.example.fixzy_ketnoikythuatvien.service.ServiceService
import com.example.fixzy_ketnoikythuatvien.service.model.Review
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.JoinType
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.DayOfWeek
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AvailabilityScreen(
    navController: NavController,
    serviceId: Int,
    serviceName: String?
) {
    Log.d(
        "AvailabilityScreen",
        "serviceId: $serviceId, serviceName: $serviceName"
    )

    val decodedName = remember(serviceName) {
        try {
            val decoded = URLDecoder.decode(serviceName ?: "", "UTF-8")
            Log.d("AvailabilityScreen", "decoded service name: $decoded")
            decoded
        } catch (e: Exception) {
            Log.e("AvailabilityScreen", "Error decoding service name: ${e.message}", e)
            serviceName ?: "N/A"
        }
    }

    val scope = rememberCoroutineScope()
    val state by Store.stateFlow.collectAsState()
    val providerService = remember { ProviderService() }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var availabilitySelected by remember { mutableStateOf<Availability?>(null) }
    var selectedTab by remember { mutableStateOf(0) }
    val store = Store.store

    LaunchedEffect(serviceId) {
        Log.d(
            "AvailabilityScreen",
            "Launching effect to fetch availability for serviceId: $serviceId"
        )
//        scope.launch {
            try {
                ServiceService().getServiceInformation(serviceId)
                Log.d(
                    "AvailabilityScreen",
                    "fetch service tc: $serviceId, detail: ${state.selectedServiceInformation}"
                )
                Log.d("AvailabilityScreen", "review ${state.reviews}")
                providerService.fetchAvailability(serviceId)
                Log.d(
                    "AvailabilityScreen",
                    "Successfully fetched availability for serviceId: $serviceId"
                )
            } catch (e: Exception) {
                Log.e("AvailabilityScreen", "Error fetching availability: ${e.message}", e)
            }
//        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 35.dp, start = 20.dp, end = 20.dp),
        bottomBar = {
            if (selectedTab == 1) {
                Button(
                    onClick = {
                        availabilitySelected?.let { availability ->
                            store.dispatch(
                                Action.UpdateBooking(
                                    serviceId = availability.service_id,
                                    availabilityId = availability.availability_id,
                                    startTime = availability.start_time,
                                    serviceName = decodedName,
                                    totalPrice = state.tempPrice?.toDouble()
                                )
                            )
                            navController.navigate(
                                "confirm_booking_screen?service_name=${serviceName}&date=${availability.date}}"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                    enabled = availabilitySelected != null && availabilitySelected?.status == "AVAILABLE"
                ) {
                    Text("Continue to Book", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = decodedName,
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("Information", "Booking").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, color = AppTheme.colors.mainColor) }
                    )
                }
            }

            when (selectedTab) {
                0 -> { // Information Tab
                    val service = state.selectedServiceInformation
                    if (service != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8FF))
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Service Title
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = AppTheme.colors.mainColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = service.name,
                                        style = AppTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.mainColor
                                    )
                                }
                                
                                // Price and Rating
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AttachMoney,
                                            contentDescription = null,
                                            tint = Color(0xFF43A047),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "${service.price} VND",
                                            style = AppTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF43A047)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFC107),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "${String.format("%.1f", service.rating)}",
                                            style = AppTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFC107)
                                        )
                                    }
                                }

                                // Category & Duration
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Category,
                                            contentDescription = null,
                                            tint = AppTheme.colors.mainColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = service.categoryName,
                                            style = AppTheme.typography.bodyMedium
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = AppTheme.colors.mainColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${service.duration} phút",
                                            style = AppTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                // Description
                                Text(
                                    text = service.description ?: "No description",
                                    style = AppTheme.typography.bodyMedium,
                                    color = AppTheme.colors.onBackgroundVariant
                                )
                            }
                        }

                        // Reviews Section
                        if (state.reviews.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8FF))
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Reviews Header
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Reviews (${state.reviews.size})",
                                            style = AppTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFFFC107),
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = String.format("%.1f", state.reviews.map { it.rating }.average()),
                                                style = AppTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFC107)
                                            )
                                        }
                                    }


                                }
                            }
                        }
                        if (state.reviews.isNotEmpty()) {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.heightIn(max = 400.dp)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),

                            ) {
                                items(state.reviews) { review ->
                                    ReviewItem(review = review)
                                }
                            }
                        }else{
                            Text(
                                text = "No reviews yet",
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colors.onBackgroundVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                
                1 -> { // Booking Tab - Existing Availability Content
                    if (state.isLoadingAvailability) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (state.availabilityError != null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(state.availabilityError!!, color = AppTheme.colors.onBackground)
                        }
                    } else if (state.availability.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No availability available", color = AppTheme.colors.onBackground)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            CalendarView(
                                availabilities = state.availability,
                                onDateSelected = { date ->
                                    Log.d("AvailabilityScreen", "Date selected: $date")
                                    selectedDate = date
                                }
                            )

                            selectedDate?.let { date ->
                                Log.d("AvailabilityScreen", "Processing selected date: $date")
                                val timeSlots = state.availability.filter {
                                    LocalDate.parse(it.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME) == date
                                }

                                if (timeSlots.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Available Time Slots",
                                        style = AppTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = date.toString(),
                                        style = AppTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(timeSlots) { slot ->
                                            TimeSlotCard(
                                                slot = slot,
                                                isSelected = slot == availabilitySelected,
                                                onSelected = { selectedSlot ->
                                                    availabilitySelected =
                                                        if (selectedSlot == availabilitySelected) {
                                                            null
                                                        } else {
                                                            selectedSlot
                                                        }
                                                }
                                            )
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
//fun convertDateFormat(isoDate: String): String {
//    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    val date = inputFormat.parse(isoDate)
//    return outputFormat.format(date)
//}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    availabilities: List<Availability>,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val endDate = today.plusWeeks(2)
    val days = mutableListOf<LocalDate>()
    var currentDate = today
    while (!currentDate.isAfter(endDate)) {
        days.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }

    // Group availabilities by date
    val availabilityMap = availabilities.groupBy {
        LocalDate.parse(it.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    // Tìm ngày đầu tuần (thứ 2) của tuần chứa ngày đầu tiên
    val firstDayOfWeek = days.first().with(java.time.DayOfWeek.MONDAY)
    // Tìm ngày cuối tuần (chủ nhật) của tuần chứa ngày cuối cùng
    val lastDayOfWeek = days.last().with(java.time.DayOfWeek.SUNDAY)
    // Tạo danh sách ngày đầy đủ để đủ tuần
    val fullDays = mutableListOf<LocalDate>()
    var d = firstDayOfWeek
    while (!d.isAfter(lastDayOfWeek)) {
        fullDays.add(d)
        d = d.plusDays(1)
    }

    // Theo dõi tháng hiện tại để hiển thị tiêu đề tháng
    var lastMonth: java.time.Month? = null

    Column {
        // Tiêu đề các ngày trong tuần
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.onBackground
                )
            }
        }

        // Render từng tuần
        for (weekIndex in 0 until (fullDays.size / 7)) {
            val week = fullDays.subList(weekIndex * 7, weekIndex * 7 + 7)
            
            // Hiển thị tiêu đề tháng nếu là tuần đầu của tháng mới
            val month = week.first().month
            if (month != lastMonth) {
                Text(
                    text = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
                        .format(week.first()),
                    style = AppTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                lastMonth = month
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { date ->
                    val inRange = date in days
                    val availabilitiesForDate = availabilityMap[date] ?: emptyList()
                    val hasAvailability = availabilitiesForDate.isNotEmpty()
                    val isAvailable = availabilitiesForDate.any { it.status == "AVAILABLE" }

                    val backgroundColor = when {
                        hasAvailability && isAvailable -> Color.Green.copy(alpha = 0.3f)
                        hasAvailability -> Color.Red.copy(alpha = 0.3f)
                        else -> Color.Transparent
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .background(
                                if (inRange) backgroundColor else Color.Transparent,
                                CircleShape
                            )
                            .clickable(enabled = inRange) {
                                if (inRange) onDateSelected(date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = AppTheme.typography.bodyMedium,
                            color = if (inRange) AppTheme.colors.onBackground else Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSlotCard(
    slot: Availability,
    isSelected: Boolean,
    onSelected: (Availability) -> Unit
) {
    val isAvailable = slot.status == "AVAILABLE"
    val cardColor = when {
        isAvailable && isSelected -> AppTheme.colors.mainColor.copy(alpha = 0.12f)
        isAvailable -> Color(0xFFE8F5E9)
        else -> Color(0xFFFFEBEE)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = if (isAvailable) AppTheme.colors.mainColor else Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "${slot.start_time.take(5)} - ${slot.end_time.take(5)}",
                        style = AppTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isAvailable) AppTheme.colors.mainColor else Color.Red
                    )
                    Text(
                        text = slot.day_of_week,
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
            }
            if (isAvailable) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onSelected(slot) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppTheme.colors.mainColor,
                        unselectedColor = AppTheme.colors.onBackgroundVariant
                    )
                )
            } else {
                Box(
                    modifier = Modifier
                        .background(Color.Red, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Busy",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = AppTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEFF7FF), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(),) {
                AsyncImage(
                    model = review.avatarUrl ?: "https://th.bing.com/th/id/OIP.uF4xhcEH4QKRB9cX8sMSeAHaFA?rs=1&pid=ImgDetMain",
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = review.fullName,
                        style = AppTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = formatDate(review.createdAt),
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
        }
        Row {
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < review.rating) Color(0xFFFFC107)
                    else Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        review.feedback?.let { feedback ->
            if (feedback.isNotEmpty()) {
                Text(
                    text = feedback,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onBackground
                )
            }
        }

        // Feedback image if exists
        review.feedbackUrl?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Booking details
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = AppTheme.colors.mainColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${formatDate(review.bookingDate)} ${review.bookingTime}",
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.onBackgroundVariant
            )
            Spacer(modifier = Modifier.width(8.dp))


        }
        Text(
            text = "Booking Code: ${review.referenceCode}",
            style = AppTheme.typography.bodySmall,
            color = AppTheme.colors.onBackgroundVariant
        )
    }
}

// Add this utility function
private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}