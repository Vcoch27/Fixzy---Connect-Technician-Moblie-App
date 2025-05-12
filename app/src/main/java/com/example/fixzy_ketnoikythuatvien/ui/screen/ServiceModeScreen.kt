package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.service.ServiceService
import com.example.fixzy_ketnoikythuatvien.service.model.AddScheduleRequest
import com.example.fixzy_ketnoikythuatvien.service.model.ModeBooking
import com.example.fixzy_ketnoikythuatvien.service.model.ModeSchedule
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.ServiceSchedule
import com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen.TimeInputField
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import androidx.compose.runtime.toMutableStateList

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ServiceModeScreen(
    navController: NavController,
    serviceId: Int,
    serviceName: String?,
    selectedTabFromNav: Int
) {
    val serviceService = remember { ServiceService() }
    val bookingService = remember { BookingService() }
    val state by Store.stateFlow.collectAsState()
    val scope = rememberCoroutineScope()
    var newDay by remember { mutableStateOf("Monday") }
    var newStartTime by remember { mutableStateOf("") }
    var newEndTime by remember { mutableStateOf("") }
    var dayExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(selectedTabFromNav) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showCompletionMessage by remember { mutableStateOf<String?>(null) }
    Log.d("ServiceModeScreen", "${state.modeService?.schedules ?: emptyList()}")
    val schedules = remember { mutableStateListOf<ModeSchedule>() }
//    LaunchedEffect(state.modeService?.schedules) {
//        schedules.clear()
//        schedules.addAll(state.modeService?.schedules ?: emptyList())
//        Log.d("ServiceModeScreen", "Schedules: $schedules")
//    }


    LaunchedEffect(serviceId) {
        serviceService.getModeService(serviceId)
        schedules.clear()
        schedules.addAll(state.modeService?.schedules ?: emptyList())
    }
    val snackbarHostState = remember { SnackbarHostState() }

    // Xử lý thông báo trạng thái
    LaunchedEffect(state) {
        when {
            state.isLoading -> {
                snackbarHostState.showSnackbar("Adding schedule...")
            }
            state.success && state.newScheduleId != null -> {
                snackbarHostState.showSnackbar("Schedule added successfully!")
            }
            state.error != null -> {
                snackbarHostState.showSnackbar("Error: ${state.error}")
            }
        }
    }

    LaunchedEffect(showCompletionMessage) {
        showCompletionMessage?.let {
            // Simulate a delay for the message (e.g., 2 seconds)
            kotlinx.coroutines.delay(2000)
            showCompletionMessage = null
        }
    }

    val decodedName = remember(serviceName) {
        try {
            URLDecoder.decode(serviceName ?: "", "UTF-8")
        } catch (e: Exception) {
            serviceName ?: "N/A"
        }
    }



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 35.dp, start = 20.dp, end = 20.dp),
        bottomBar = {
            if (selectedTab == 1) {
                Button(
                    onClick = {
                        navController.navigate("add_service_schedule_screen/$serviceId")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                    enabled = (state.modeService?.schedules?.size ?: 0) < 5
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Schedule",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Schedule", color = Color.White, fontSize = 16.sp)
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

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.error!!, color = AppTheme.colors.onBackground)
                }
            } else {
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Information", "Schedules", "Bookings").forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }

                when (selectedTab) {
                    0 -> { // Information
                        val service = state.modeService?.service
                        if (service != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Service Name: ${service.name}",
                                        fontWeight = FontWeight.Bold,
                                        style = AppTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Category: ${service.categoryName}",
                                        style = AppTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Price: ${service.price} VND",
                                        style = AppTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Description: ${service.description ?: "N/A"}",
                                        style = AppTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Created At: ${service.createdAt}",
                                        style = AppTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Duration: ${service.duration} minutes",
                                        style = AppTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Rating: ${String.format("%.1f", service.rating)}",
                                        style = AppTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Orders Completed: ${service.ordersCompleted}",
                                        style = AppTheme.typography.bodyMedium
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    // serviceService.updateServiceStatus(serviceId, "INACTIVE")
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Text(
                                                "Deactivate",
                                                fontSize = 14.sp,
                                                color = Color.White
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    // serviceService.deleteService(serviceId)
                                                    navController.popBackStack()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Text("Delete", fontSize = 14.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> { // Schedules
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Tiêu đề
                            Text(
                                text = "Fixed Schedules",
                                style = AppTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(schedules) { schedule ->
                                    ScheduleCard(
                                        schedule = ServiceSchedule(
                                            schedule_id = schedule.scheduleId.toLong(),
                                            service_id = serviceId,
                                            day_of_week = schedule.dayOfWeek,
                                            start_time = schedule.startTime,
                                            end_time = schedule.endTime,
                                            created_at = "",
                                            status = ""
                                        ),
                                        onDelete = {
                                            scope.launch {
                                                // Xóa lịch khỏi danh sách
                                                schedules.remove(schedule)
                                            }
                                        }
                                    )
                                }
                            }


                            // Phần thêm lịch
                            if (schedules.size < 5) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Day Selection
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentSize(Alignment.TopStart)
                                    ) {
                                        OutlinedTextField(
                                            value = newDay,
                                            onValueChange = { },
                                            label = { Text("Day") },
                                            modifier = Modifier.fillMaxWidth(),
                                            readOnly = true,
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowDropDown,
                                                    contentDescription = "Dropdown",
                                                    modifier = Modifier.clickable { dayExpanded = !dayExpanded }
                                                )
                                            }
                                        )
                                        DropdownMenu(
                                            expanded = dayExpanded,
                                            onDismissRequest = { dayExpanded = false },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            listOf(
                                                "Monday", "Tuesday", "Wednesday", "Thursday",
                                                "Friday", "Saturday", "Sunday"
                                            ).forEach { day ->
                                                DropdownMenuItem(
                                                    text = { Text(day) },
                                                    onClick = {
                                                        newDay = day
                                                        dayExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    // Time Selection
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        TimeInputField(
                                            label = "Start Time",
                                            time = newStartTime,
                                            onTimeSelected = { newStartTime = it },
                                            modifier = Modifier.weight(1f)
                                        )
                                        TimeInputField(
                                            label = "End Time",
                                            time = newEndTime,
                                            onTimeSelected = { newEndTime = it },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    // Add Schedule Button
                                    Button(
                                        onClick = {
                                            if (schedules.size < 5 && newStartTime.isNotEmpty() && newEndTime.isNotEmpty()) {
                                                scope.launch {
                                                    serviceService.addSchedule(
                                                        serviceId,
                                                        AddScheduleRequest(
                                                            dayOfWeek = newDay,
                                                            startTime = newStartTime,
                                                            endTime = newEndTime
                                                        )
                                                    )
                                                    navController.navigate("service_provider_mode/$serviceId/$serviceName?selectedTab=1")

                                                    newDay = ""
                                                    newStartTime = ""
                                                    newEndTime = ""
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(56.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                                        enabled = schedules.size < 5 && newStartTime.isNotEmpty() && newEndTime.isNotEmpty()
                                    ) {
                                        Text("Add Schedule", color = Color.White, fontSize = 14.sp)
                                    }
                                }
                            } else {
                                Text(
                                    text = "Maximum 5 schedules reached",
                                    color = Color.Red,
                                    style = AppTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            // Snackbar để hiển thị thông báo
                            SnackbarHost(
                                hostState = snackbarHostState,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }}

                    2 -> {
                        val bookings = state.modeService?.bookings ?: emptyList()
                        Log.d(
                            "BookingScreen",
                            "Total bookings: ${bookings.size}, Data: ${bookings.joinToString { it.referenceCode }}"
                        )

                        val confirmedCount =
                            bookings.count { it.status.equals("Confirmed", ignoreCase = true) }
                        val pendingCount =
                            bookings.count { it.status.equals("Pending", ignoreCase = true) }
                        val dialogState =
                            remember { mutableStateOf(false) } // Trạng thái hiển thị dialog thông tin

                        Column {
                            // Hiển thị số lượng booking
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Confirmed: $confirmedCount",
                                    style = AppTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Green
                                )
                                Text(
                                    text = "Pending: $pendingCount",
                                    style = AppTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue
                                )
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Delete",
                                    modifier = Modifier.clickable { dialogState.value = true },
                                    tint = Color.Gray
                                )
                            }

                            if (dialogState.value) {
                                AlertDialog(
                                    onDismissRequest = { dialogState.value = false },
                                    title = { Text("Booking Information") },
                                    text = {
                                        Text(
                                            text = """
                                            - Bookings will be automatically canceled 3 hours after the customer places the booking if not confirmed.
                                            - Confirmed bookings will be canceled at the end of the booking date if not completed.
                                        """.trimIndent(),
                                            style = AppTheme.typography.bodyMedium
                                        )
                                    },
                                    confirmButton = {
                                        Button(onClick = { dialogState.value = false }) {
                                            Text("OK")
                                        }
                                    }
                                )
                            }

                            CalendarView2(
                                onDateSelected = { date ->
                                    Log.d(
                                        "BookingScreen",
                                        "Date selected from CalendarView2: $date"
                                    )
                                    selectedDate = date
                                },
                                bookings = bookings
                            )

                            selectedDate?.let { date ->
                                Log.d("BookingScreen", "Processing selected date: $date")
                                val bookingsOnDate = bookings.filter { booking ->
                                    try {
                                        val parsedDate = LocalDate.parse(
                                            booking.bookingDate,
                                            DateTimeFormatter.ISO_OFFSET_DATE_TIME
                                        )
                                        Log.d(
                                            "BookingScreen",
                                            "Parsed bookingDate for booking ${booking.referenceCode}: $parsedDate"
                                        )
                                        parsedDate == date
                                    } catch (e: DateTimeParseException) {
                                        Log.e(
                                            "BookingScreen",
                                            "Failed to parse bookingDate for booking ${booking.referenceCode}: ${booking.bookingDate}, error: ${e.message}"
                                        )
                                        false
                                    }
                                }

                                Log.d(
                                    "BookingScreen",
                                    "Bookings found for $date: ${bookingsOnDate.size}, References: ${bookingsOnDate.joinToString { it.referenceCode }}"
                                )

                                if (bookingsOnDate.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Bookings on ${date.toString()}",
                                        style = AppTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp)
                                    ) {
                                        items(bookingsOnDate) { booking ->
                                            Log.d(
                                                "BookingScreen",
                                                "Displaying booking: ${booking.referenceCode}"
                                            )
                                            BookingCard(booking, bookingService, scope) { message ->
                                                showCompletionMessage = message
                                                scope.launch {
                                                    serviceService.getModeService(serviceId) // Refresh bookings
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No bookings on this date",
                                            style = AppTheme.typography.bodyMedium,
                                            color = AppTheme.colors.onBackground
                                        )
                                    }
                                }
                            }
                            showCompletionMessage?.let { message ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .background(Color.LightGray, RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = message,
                                        color = Color.Black,
                                        style = AppTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(8.dp)
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

// Composable cho Card booking
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingCard(
    booking: ModeBooking,
    bookingService: BookingService,
    scope: CoroutineScope,
    onCompletion: (String) -> Unit
) {
    val today = LocalDate.now()
    val bookingDate = try {
        LocalDate.parse(booking.bookingDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    } catch (e: DateTimeParseException) {
        Log.e("BookingScreen", "Failed to parse booking date: ${booking.date}")
        LocalDate.MIN
    }
    Log.d("BookingScreen", "Parsed booking date: $bookingDate ;today: $today")
    val isToday = bookingDate == today

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Booking: ${booking.referenceCode}",
                    style = AppTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Customer: ${booking.fullName}",
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = "Phone: ${booking.phone ?: "N/A"}",
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = "Address: ${booking.address ?: "N/A"}",
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = "Date: ${DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(LocalDateTime.parse(booking.bookingDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME))}",
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = "Time: ${booking.bookingTime}",
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = "Created At: ${DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(LocalDateTime.parse(booking.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME))}",
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = "Status: ${booking.status}",
                    style = AppTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (booking.status.equals("Pending", ignoreCase = true)) {
                    Button(
                        onClick = {
                            scope.launch {
                                bookingService.updateBookingStatus(booking.bookingId, "Confirmed")
                                onCompletion("Booking ${booking.referenceCode} confirmed successfully!")
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Confirm")
                    }
                } else if (booking.status.equals("Confirmed", ignoreCase = true) && isToday) {
                    Button(
                        onClick = {
                            scope.launch {
                                bookingService.updateBookingStatus(booking.bookingId, "WaitingForCustomerConfirmation")
                                onCompletion("Booking ${booking.referenceCode} marked as completed!")
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Complete")
                    }
                }else if(booking.status.equals("WaitingForCustomerConfirmation", ignoreCase = true) && isToday){
//                    Text(
//                        text = "Waiting for customer confirmation",
//                        style = AppTheme.typography.bodyMedium
//                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleCard(
    schedule: ServiceSchedule,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    text = schedule.day_of_week,
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${schedule.start_time} - ${schedule.end_time}",
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Created: ${schedule.created_at}",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.onBackgroundVariant
                )
            }
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier.clickable { onDelete() },
                tint = Color.Red
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView2(
    onDateSelected: (LocalDate) -> Unit,
    bookings: List<ModeBooking>,
) {
    val today = LocalDate.now()
    val endDate = today.plusWeeks(2)
    val days = mutableListOf<LocalDate>()
    var currentDate = today
    while (!currentDate.isAfter(endDate)) {
        days.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }

    Log.d(
        "CalendarView2",
        "Total bookings: ${bookings.size}, References: ${bookings.joinToString { it.referenceCode }}"
    )

    // Tạo map cho bookings, nhóm theo bookingDate
    val bookingMap = bookings.groupBy { booking ->
        try {
            val parsedDate =
                LocalDate.parse(booking.bookingDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            Log.d(
                "CalendarView2",
                "Parsed bookingDate for booking ${booking.referenceCode}: $parsedDate"
            )
            parsedDate
        } catch (e: DateTimeParseException) {
            Log.e(
                "CalendarView2",
                "Failed to parse bookingDate for booking ${booking.referenceCode}: ${booking.bookingDate}, error: ${e.message}"
            )
            LocalDate.MIN
        }
    }

    Log.d("CalendarView2", "Booking map created with ${bookingMap.size} unique dates")

    val months = days.groupBy { it.month }.keys.sortedBy { it.value }

    months.forEach { month ->
        Text(
            text = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
                .format(days.first { it.month == month }),
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

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

        LazyColumn {
            items((days.size + 6) / 7) { weekIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (dayIndex in 0 until 7) {
                        val absoluteIndex = weekIndex * 7 + dayIndex
                        if (absoluteIndex in days.indices) {
                            val date = days[absoluteIndex]

                            // Lấy danh sách bookings cho ngày hiện tại
                            val bookingsForDate = bookingMap[date] ?: emptyList()
                            val hasConfirmedBooking = bookingsForDate.any {
                                it.status.equals("Confirmed", ignoreCase = true)
                            }
                            val hasPendingBooking = bookingsForDate.any {
                                it.status.equals("Pending", ignoreCase = true)
                            }
                            val hasWaitingForCustomerConfirmationBooking = bookingsForDate.any {
                                it.status.equals("WaitingForCustomerConfirmation", ignoreCase = true)
                            }
                            Log.d(
                                "CalendarView2",
                                "Date: $date, Confirmed: $hasConfirmedBooking, Pending: $hasPendingBooking"
                            )

                            // Xác định màu nền
                            val backgroundColor = if (hasConfirmedBooking) {
                                Color.Green.copy(alpha = 0.5f)
                            } else {
                                Color.Transparent
                            }
                            val color  = if (hasWaitingForCustomerConfirmationBooking) Color(
                                0xFF437500
                            ) else Color(0xFF000000)
                            // Xác định viền cho Pending
                            val borderModifier = if (hasPendingBooking) {
                                Modifier.border(
                                    width = 2.dp,
                                    color = Color.Blue,
                                    shape = CircleShape
                                )
                            } else {
                                Modifier
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .then(borderModifier)
                                    .background(backgroundColor, CircleShape)
                                    .clickable {
                                        Log.d("CalendarView2", "Clicked on date: $date")
                                        onDateSelected(date)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    style = AppTheme.typography.bodyMedium,
                                    color = color,
                                    textAlign = TextAlign.Center,
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