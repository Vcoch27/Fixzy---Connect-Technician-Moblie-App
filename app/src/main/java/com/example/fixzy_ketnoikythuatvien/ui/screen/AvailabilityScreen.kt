package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.net.Uri
import android.os.Build
import android.util.Log
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
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.model.Availability
import com.example.fixzy_ketnoikythuatvien.service.ProviderService
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
    val store = Store.store

    LaunchedEffect(serviceId) {
        Log.d(
            "AvailabilityScreen",
            "Launching effect to fetch availability for serviceId: $serviceId"
        )
//        scope.launch {
            try {
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
            Button(
                onClick = {

                    Log.d("AvailabilityScreen", "button clicked")
                    availabilitySelected?.let { availability ->
                        Log.d(
                            "AvailabilityScreen",
                            " date data: ${availability.date::class} with value ${availability.date}"
                        )

                        Log.d("AvailabilityScreen", convertDateFormat(availability.date))
                        store.dispatch(
                            Action.UpdateBooking(
                                availabilityId = availability.availability_id,
//                                date = convertDateFormat(availability.date),
                                startTime = availability.start_time
                            )
                        )
//                        store.dispatch(
//                            Action.DateForBooking(availability.date)
//                        )
                        Log.e("AvailabilityScreen", "Continue clicked")
                        navController.navigate("confirm_booking_screen?service_name=${serviceName}?date=${convertDateFormat(availability.date)}")
                    } ?: run {
                        Log.w("AvailabilityScreen", "Continue clicked but no availability selected")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                enabled = availabilitySelected != null && availabilitySelected?.status == "AVAILABLE"
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
fun convertDateFormat(isoDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(isoDate)
    return outputFormat.format(date)
}

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

    // Determine months to display
    val months = days.groupBy { it.month }.keys.sortedBy { it.value }
    Log.d("CalendarView", "Months to display: ${months.joinToString { it.name }}")

    months.forEach { month ->
        Log.d("CalendarView", "Rendering month: ${month.name}")
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

        val daysInMonth = days.filter { it.month == month }

        val firstDayOfMonth = daysInMonth.first()
        val currentDayOfWeek = firstDayOfMonth.dayOfWeek.value
        val adjustedDayOfWeek = if (currentDayOfWeek == 7) 0 else currentDayOfWeek
        val emptyDays = (adjustedDayOfWeek + 6) % 7
        Log.d(
            "CalendarView",
            "First day: $firstDayOfMonth, dayOfWeek: $currentDayOfWeek, emptyDays: $emptyDays"
        )

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
                            Log.v(
                                "CalendarView",
                                "Date: $date, hasAvailability: $hasAvailability, isAvailable: $isAvailable"
                            )

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
                                    .clickable {
                                        Log.d("CalendarView", "Date clicked: $date")
                                        onDateSelected(date)
                                    },
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
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        }
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
    Log.d("TimeSlotCard", "Rendering slot: ${slot.availability_id}, isSelected: $isSelected")

    val isAvailable = slot.status == "AVAILABLE"
    val cardColor = if (!isAvailable) {
        Color.Red.copy(alpha = 0.1f)
    } else if (isSelected) {
        AppTheme.colors.mainColor.copy(alpha = 0.1f)
    } else {
        AppTheme.colors.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isAvailable) {
                if (isAvailable) {
                    Log.d("TimeSlotCard", "Slot ${slot.availability_id} clicked")
                    onSelected(slot)
                } else {
                    Log.d("TimeSlotCard", "Slot ${slot.availability_id} is not available")
                }
            },
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
                    onClick = {
                        if (isAvailable) {
                            Log.d(
                                "TimeSlotCard",
                                "Radio button clicked for slot ${slot.availability_id}"
                            )
                            onSelected(slot)
                        }
                    },
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