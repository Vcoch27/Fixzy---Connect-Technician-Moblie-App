package com.example.fixzy_ketnoikythuatvien.ui.screen.extendedScreen

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.service.ServiceService
import com.example.fixzy_ketnoikythuatvien.service.model.CreateServiceRequest
import com.example.fixzy_ketnoikythuatvien.service.model.Schedule
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import java.util.Calendar
import kotlinx.coroutines.launch
@Composable
fun AddServiceScreen(
    navController: NavController
) {
    val state by Store.stateFlow.collectAsState()
    val categories = state.categories
    Log.d("AddServiceScreen", "Categories: $categories")
    var selectedCategory by remember { mutableStateOf("") }
    var serviceName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var schedules by remember { mutableStateOf<List<ServiceSchedule>>(emptyList()) }
    var newDay by remember { mutableStateOf("Monday") }
    var newStartTime by remember { mutableStateOf("") }
    var newEndTime by remember { mutableStateOf("") }
    var dayExpanded by remember { mutableStateOf(false) }
    val serviceService = remember { ServiceService() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Hiển thị thông báo từ state
    LaunchedEffect(state.createServiceError) {
        state.createServiceError?.let {
            coroutineScope.launch { snackbarHostState.showSnackbar(it) }
        }
    }
    LaunchedEffect(state.createServiceMessage) {
        state.createServiceMessage?.let {
            coroutineScope.launch { snackbarHostState.showSnackbar(it) }
            if (it.contains("Tạo dịch vụ thành công")) navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Button(
                onClick = {
                    val categoryId = categories.find { it.name == selectedCategory }?.categoryId
                    if (categoryId == null) {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Vui lòng chọn danh mục") }
                        return@Button
                    }

                    if (serviceName.isEmpty() || price.isEmpty() || duration.isEmpty()) {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Vui lòng điền đầy đủ thông tin") }
                        return@Button
                    }

                    val request = CreateServiceRequest(
                        categoryId = categoryId,
                        serviceName = serviceName,
                        description = description.ifEmpty { null },
                        price = price.toIntOrNull() ?: 0,
                        duration = duration.toIntOrNull() ?: 0,
                        schedules = schedules.map {
                            Schedule(
                                dayOfWeek = it.day_of_week,
                                startTime = it.start_time,
                                endTime = it.end_time
                            )
                        }
                    )

                    coroutineScope.launch {
                        serviceService.createService(request)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                enabled = !state.isCreatingService
            ) {
                if (state.isCreatingService) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Book Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (state.isCreatingService) "Đang tạo..." else "Add service",
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = "Add Service",
                style = AppTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Category Selection
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = { },
                    label = { Text("Select Category") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategory = category.name
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Service Details
            OutlinedTextField(
                value = serviceName,
                onValueChange = { serviceName = it },
                label = { Text("Service Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isCreatingService
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (VND)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isCreatingService
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isCreatingService
            )

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isCreatingService
            )

            // Service Schedules
            Text(
                text = "Service Schedules (Max 5)",
                style = AppTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(schedules) { schedule ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${schedule.day_of_week}: ${schedule.start_time} - ${schedule.end_time}",
                                    style = AppTheme.typography.bodyMedium
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.clickable {
                                    schedules = schedules.filter { it.schedule_id != schedule.schedule_id }
                                },
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
            if (schedules.size < 5) {
                // Add Schedule Form
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
                                "Monday",
                                "Tuesday",
                                "Wednesday",
                                "Thursday",
                                "Friday",
                                "Saturday",
                                "Sunday"
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
                                schedules = schedules + ServiceSchedule(
                                    schedule_id = (schedules.size + 1).toLong(),
                                    service_id = 0,
                                    day_of_week = newDay,
                                    start_time = newStartTime,
                                    end_time = newEndTime,
                                    created_at = ""
                                )
                                newStartTime = ""
                                newEndTime = ""
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
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (state.isCreatingService) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimeInputField(
    label: String,
    time: String,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = time,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Select Time"
            )
        },
        interactionSource = interactionSource,
        modifier = modifier
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                Log.d("TimeInputField", "Time picker clicked for $label")
                TimePickerDialog(
                    context,
                    { _, selectedHour: Int, selectedMinute: Int ->
                        val formatted = String.format("%02d:%02d", selectedHour, selectedMinute)
                        Log.d("TimeInputField", "Time selected: $formatted")
                        onTimeSelected(formatted)
                    },
                    hour,
                    minute,
                    true
                ).show()
            }
        }
    }
}

data class ServiceSchedule(
    val schedule_id: Long,
    val service_id: Int,
    val day_of_week: String,
    val start_time: String,
    val end_time: String,
    val created_at: String
)