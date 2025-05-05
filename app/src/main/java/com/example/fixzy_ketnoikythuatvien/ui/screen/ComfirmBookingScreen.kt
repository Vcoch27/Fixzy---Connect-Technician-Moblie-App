package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.service.ProviderService
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Composable
fun ConfirmBookingScreen(
    navController: NavController,
    serviceName: String?,
    date: String?
) {
    val state by Store.stateFlow.collectAsState()
    val store = Store.store
    val bookingService = remember { BookingService() }
    val scope = rememberCoroutineScope()

    var address by remember { mutableStateOf(state.user?.address ?: "") }
    var phone by remember { mutableStateOf(state.user?.phone ?: "") }
    var notes by remember { mutableStateOf("") }

    val isFormValid = remember(address, phone) {
        address.isNotBlank() && phone.isNotBlank() && phone.length >= 10
    }

    val addressError = if (address.isBlank()) "Address is required" else null
    val phoneError = when {
        phone.isBlank() -> "Phone is required"
        phone.length < 10 -> "Phone must be at least 10 digits"
        else -> null
    }

    LaunchedEffect(state.referenceCode) {
        state.referenceCode?.let { code ->
            navController.navigate("booking_success_screen/$code") {
                popUpTo("confirm_booking") { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (state.booking?.serviceId == null || state.booking?.availabilityId == null) {
            store.dispatch(Action.CreateBookingFailure("Incomplete booking data"))
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column {
                state.createBookingError?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
                Button(
                    onClick = {
                        if (!isFormValid) return@Button
                        scope.launch {
                            try {
                                bookingService.createBooking(
                                    serviceId = state.booking?.serviceId,
                                    availabilityId = state.booking?.availabilityId,
                                    address = address,
                                    phone = phone,
                                    notes = notes.takeIf { it.isNotBlank() }
                                )
                            } catch (e: Exception) {
                                store.dispatch(Action.CreateBookingFailure("Failed to create booking: ${e.message}"))
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
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
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Confirm Booking", style = AppTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Booking Details", style = AppTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    BookingInfoItem("Service", serviceName ?: "N/A")
                    BookingInfoItem("Date", date ?: "N/A")
                    BookingInfoItem("Time", state.booking?.startTime ?: "N/A")
                    BookingInfoItem("Total Price", state.booking?.totalPrice?.let { "$%.2f".format(it) } ?: "N/A")
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
    Log.v("BookingInfoItem", "Rendering item: $label = $value")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.onBackground.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.onBackground,
            fontWeight = FontWeight.Medium
        )
    }
}