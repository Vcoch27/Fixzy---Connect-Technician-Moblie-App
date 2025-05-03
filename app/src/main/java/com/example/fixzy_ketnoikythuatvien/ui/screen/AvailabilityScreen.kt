package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.Availability
import com.example.fixzy_ketnoikythuatvien.service.ProviderService
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.JoinType
import java.net.URLDecoder
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
    // Giải mã lại tên dịch vụ
    val decodedName = remember(serviceName) {
        try {
            URLDecoder.decode(serviceName ?: "", "UTF-8")
        } catch (e: Exception) {
            serviceName ?: "N/A"
        }
    }
    val scope = rememberCoroutineScope()
    val state by Store.stateFlow.collectAsState()
    val providerService = remember { ProviderService() }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(serviceId) {
        scope.launch {
            providerService.fetchAvailability(serviceId)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .padding( top = 35.dp, start = 20.dp, end = 20.dp),
        bottomBar = {
            Button(
                onClick = { /* Handle booking with selected availability */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                enabled = state.availability.any { it.status == "AVAILABLE" }
            ) {
                Text("Continue to Book", color = Color.White, fontSize = 16.sp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "$decodedName",
                style = AppTheme.typography.titleMedium
            )
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
                    // Calendar
                    CalendarView(
                        availabilities = state.availability,
                        onDateSelected = { date -> selectedDate = date }
                    )

                    // Time slots for selected date
                    selectedDate?.let { date ->
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
                                    TimeSlotCard(slot = slot)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    availabilities: List<Availability>,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now() // 03/05/2025
    val endDate = today.plusWeeks(2) // 17/05/2025
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

    // Determine months to display
    val months = days.groupBy { it.month }.keys.sortedBy { it.value }
    months.forEach { month ->
        Text(
            text = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
                .format(days.first { it.month == month }),
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Header: Days of the week
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
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.onBackground
                )
            }
        }

        // Filter days for this month
        val daysInMonth = days.filter { it.month == month }
        val firstDayOfMonth = daysInMonth.first()
        val currentDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 (Mon) to 7 (Sun)
        val adjustedDayOfWeek =
            if (currentDayOfWeek == 7) 0 else currentDayOfWeek // Adjust for Sunday
        val emptyDays = (adjustedDayOfWeek + 6) % 7 // Empty slots before first day

        LazyColumn {
            items((emptyDays + daysInMonth.size + 6) / 7) { weekIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (dayIndex in 0 until 7) {
                        val absoluteIndex = weekIndex * 7 + dayIndex
                        if (absoluteIndex >= emptyDays && (absoluteIndex - emptyDays) in daysInMonth.indices) {
                            val date = daysInMonth[absoluteIndex - emptyDays]
                            val availabilityForDate = availabilityMap[date] ?: emptyList()
                            val hasAvailability = availabilityForDate.isNotEmpty()
                            val isAvailable = availabilityForDate.any { it.status == "AVAILABLE" }

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
                                    .background(backgroundColor, CircleShape)
                                    .clickable { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    style = AppTheme.typography.bodyMedium,
                                    color = AppTheme.colors.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            // Hiển thị ô trống cho ngày không thuộc tháng hiện tại
                            Box(modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimeSlotCard(slot: Availability) {
    var isSelected by remember { mutableStateOf(false) }
    val isAvailable = slot.status == "AVAILABLE"
    val cardColor =
        if (!isAvailable) Color.Red.copy(alpha = 0.1f) else if (isSelected) AppTheme.colors.mainColor.copy(
            alpha = 0.1f
        ) else AppTheme.colors.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isAvailable) { if (isAvailable) isSelected = !isSelected },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${slot.start_time} - ${slot.end_time}",
                    style = AppTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.onSurface
                )
                Text(
                    text = slot.day_of_week,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.onBackground
                )
            }
            if (isAvailable) {
                RadioButton(
                    selected = isSelected,
                    onClick = { if (isAvailable) isSelected = !isSelected },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppTheme.colors.mainColor,
                        unselectedColor = AppTheme.colors.onBackgroundVariant
                    )
                )
            } else {
                Text(
                    text = "Busy",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}