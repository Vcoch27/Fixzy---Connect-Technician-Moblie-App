package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.Manifest
import androidx.annotation.RequiresApi
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.service.ProviderService
import com.example.fixzy_ketnoikythuatvien.ui.components.notifications.CustomAlertDialog
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.utils.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
@Composable
fun ConfirmBookingScreen(
    navController: NavController,
    serviceName: String?,
    date: String?,
) {
    val state by Store.stateFlow.collectAsState()
    val store = Store.store
    val bookingService = remember { BookingService() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var address by remember { mutableStateOf(state.user?.address ?: "") }
    var phone by remember { mutableStateOf(state.user?.phone ?: "") }
    var notes by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val isFormValid = remember(address, phone) {
        address.isNotBlank() && phone.isNotBlank() && phone.length >= 10
    }

    val addressError = if (address.isBlank()) "Address is required" else null
    val phoneError = when {
        phone.isBlank() -> "Phone is required"
        phone.length < 10 -> "Phone must be at least 10 digits"
        else -> null
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (state.booking?.serviceId == null || state.booking?.availabilityId == null) {
            store.dispatch(Action.CreateBookingFailure("Incomplete booking data"))
            navController.popBackStack()
        }
    }

    LaunchedEffect(state.referenceCode) {
        if (state.referenceCode != null) {
            Log.d("ConfirmBookingScreen", "Reference Code: ${state.referenceCode}")
            dialogMessage = "Đặt lịch thành công! Mã: ${state.referenceCode}"
            showDialog = true
//            NotificationHelper.showBookingSuccessNotification(context, state.referenceCode!!)
            delay(2000)
            showDialog = false
            navController.navigate("orders_page") {
                popUpTo("confirm_booking") { inclusive = true }
            }
            store.dispatch(Action.ResetBookingState)
        }
    }

    CustomAlertDialog(
        message = dialogMessage,
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        title = "Notification",
        modifier = Modifier
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column {
                state.createBookingError?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
                Button(
                    onClick = {
                        if (!isFormValid) return@Button
                        dialogMessage = "Đang xử lý..."
                        showDialog = true
                        store.dispatch(Action.ResetBookingState)
                        scope.launch {
                            try {
                                bookingService.createBooking(
                                    serviceId = state.booking?.serviceId,
                                    availabilityId = state.booking?.availabilityId,
                                    address = address,
                                    phone = phone,
                                    notes = notes.takeIf { it.isNotBlank() },
                                )
                                Log.d("ConfirmBookingScreen", "Booking created successfully")
                                showDialog = false
                            } catch (e: Exception) {
                                dialogMessage = "Lỗi: ${e.message}"
                                showDialog = true
                                delay(2000)
                                showDialog = false
                                store.dispatch(Action.CreateBookingFailure("Failed to create booking: ${e.message}"))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                    enabled = isFormValid && !state.isCreatingBooking
                ) {
                    if (state.isCreatingBooking) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Confirm Booking", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Confirm Booking", style = AppTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Booking Details", style = AppTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    BookingInfoItem("Service", serviceName ?: "N/A")
                    BookingInfoItem("Date", "${date?.let { formatIsoDateToDDMMYYYY(it) }}" ?: "N/A")
                    BookingInfoItem("Time", formatTimeHHmm(state.booking?.startTime ?: "") ?: "N/A")
//                    BookingInfoItem("Total Price", state.booking?.totalPrice?.let { "$%.2f".format(it) } ?: "N/A")
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "Your Information", style = AppTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address*") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = addressError != null,
                        supportingText = { addressError?.let { Text(it, color = Color.Red) } },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { if (it.all { char -> char.isDigit() || char == '+' }) phone = it },
                        label = { Text("Phone*") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError != null,
                        supportingText = { phoneError?.let { Text(it, color = Color.Red) } },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            }
        }
    }
}

@Composable
fun BookingInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}


fun formatIsoDateToDDMMYYYY(dateString: String): String {
    return try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date = isoFormat.parse(dateString) ?: return "Invalid date"
        val desiredFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        desiredFormat.format(date)
    } catch (e: Exception) {
        "Invalid date"
    }
}

fun formatTimeHHmm(time: String): String {
    return try {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = timeFormat.parse(time) ?: return "Invalid time"
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        "Invalid time"
    }
}