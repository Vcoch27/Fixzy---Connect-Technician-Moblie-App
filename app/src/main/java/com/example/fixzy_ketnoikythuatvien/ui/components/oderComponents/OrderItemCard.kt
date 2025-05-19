package com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fixzy_ketnoikythuatvien.data.model.OrderData
import com.example.fixzy_ketnoikythuatvien.data.model.OrderStatus
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import com.example.fixzy_ketnoikythuatvien.ui.theme.LocalAppTypography
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

val TAG = "OrderItemCard"

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun OrderItemCard(
    booking: DetailBooking,
    onBookingUpdated: (DetailBooking) -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val bookingService = remember { BookingService() }
    var isCancelLoading by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showReviewBox by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf<Int?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }
    Log.d("OrderItemCard", "Rendering OrderItemCard for booking: ${booking.reference_code}")
    val currentTime = LocalDateTime.now()
    Log.d("OrderItemCard", "Current time: $currentTime")
    var isLoading by remember { mutableStateOf(false) }
    val bookingDateTime = try {
        LocalDateTime.parse("${booking.booking_date}T${booking.booking_time}")
    } catch (e: Exception) {
        Log.e(
            "OrderItemCard",
            "Failed to parse booking date/time: ${booking.booking_date} ${booking.booking_time}",
        )
        LocalDateTime.now()
    }
    val hoursUntilCancellation =
        Duration.between(currentTime, bookingDateTime.plusHours(6)).toHours()
    Log.d("OrderItemCard", "Hours until cancellation: $hoursUntilCancellation")

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8FF))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Icon + Service name + Reference code
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFFFE0B2), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = AppTheme.colors.mainColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = booking.service_name ?: "Unknown Service",
                        style = AppTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.mainColor
                    )
                    Text(
                        text = "Reference Code: #${booking.reference_code ?: "N/A"}",
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                }
            }
            // Trạng thái
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            when (booking.status) {
                                "Pending" -> Color(0xFFFFF3E0)
                                "Confirmed" -> Color(0xFFE8F5E9)
                                "Completed" -> Color(0xFFE3F0FF)
                                "Cancelled" -> Color(0xFFFFEBEE)
                                "WaitingForCustomerConfirmation" -> Color(0xFFE8EAF6)
                                else -> Color.LightGray
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = booking.status ?: "Unknown",
                        color = when (booking.status) {
                            "Pending" -> Color(0xFFFF9800)
                            "Confirmed" -> Color(0xFF388E3C)
                            "Completed" -> AppTheme.colors.mainColor
                            "Cancelled" -> Color.Red
                            "WaitingForCustomerConfirmation" -> Color(0xFF1976D2)
                            else -> Color.Gray
                        },
                        fontWeight = FontWeight.Bold,
                        style = AppTheme.typography.bodySmall
                    )
                }
            }
            // Lịch hẹn
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = AppTheme.colors.mainColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${booking.booking_time.take(5)} ${booking.booking_date.substring(8, 10)} ${monthFromNumber(booking.booking_date.substring(5, 7))}",
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Schedule",
                    style = AppTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            // Ghi chú trạng thái
            if (booking.status == "Pending") {
                Text(
                    text = "Booking will auto-cancel in 2 hours if not confirmed by provider.",
                    style = AppTheme.typography.bodySmall,
                    color = Color(0xFFFF9800),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            // Nút hành động
            if (booking.status == "Pending") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { /* Gọi điện */ },
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { showConfirmDialog = true },
                        border = BorderStroke(1.dp, Color.Red),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.Red)
                    }
                }
                if (showConfirmDialog) {
                    ConfirmDialog(
                        onConfirm = {
                            scope.launch {
                                isCancelLoading = true
                                try {
                                    val success = bookingService.updateBookingStatus(
                                        booking.booking_id,
                                        "Cancelled"
                                    )
                                    if (success) {
                                        val updatedBooking = booking.copy(status = "Cancelled")
                                        onBookingUpdated(updatedBooking)
                                    }
                                } finally {
                                    isCancelLoading = false
                                    showConfirmDialog = false
                                }
                            }
                        },
                        onDismiss = { showConfirmDialog = false }
                    )
                }
            }
            if (booking.status == "WaitingForCustomerConfirmation") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { showReviewBox = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.mainColor),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Complete & Feedback", color = Color.White)
                    }
                }
                // In OrderItemCard.kt where you handle the review submission
                if (showReviewBox) {
                    ReviewDialog(
                        onDismiss = { showReviewBox = false },
                        onSubmit = { rating, feedback, imageUrl ->
                            scope.launch {
                                isLoading = true
                                try {
                                    val success = bookingService.updateBookingStatus(
                                        booking.booking_id,
                                        "Completed",
                                        rating = rating,
                                        feedback = feedback,
                                        feedback_url = imageUrl
                                    )
                                    if (success) {
                                        val updatedBooking = booking.copy(status = "Completed")
                                        onBookingUpdated(updatedBooking)
                                    }
                                } finally {
                                    isLoading = false
                                    showReviewBox = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }

}

// Hàm helper để chuyển đổi số tháng thành tên tháng viết tắt
private fun monthFromNumber(month: String): String {
    return when (month) {
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "May"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Aug"
        "09" -> "Sep"
        "10" -> "Oct"
        "11" -> "Nov"
        "12" -> "Dec"
        else -> ""
    }
}

@Composable
fun ConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Xác nhận hủy") },
        text = { Text("Bạn có chắc chắn muốn hủy booking này?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Hủy", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Thoát")
            }
        }
    )
}

@Composable
fun ReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, feedback: String?, imageUrl: String?) -> Unit
) {
    var selectedStar by remember { mutableStateOf<Int?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val bookingService = remember { BookingService() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(16.dp),
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Service Review",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Rating Stars
                Text("Rate your experience:", fontSize = 18.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star $star",
                            tint = if ((selectedStar ?: 0) >= star) Color(0xFFFFC107)
                            else Color.LightGray,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { selectedStar = star }
                                .padding(horizontal = 4.dp)
                        )
                    }
                }

                TextField(
                    value = feedback ?: "",
                    onValueChange = { feedback = it },
                    label = { Text("Your feedback (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                // Image Upload Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { launcher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.mainColor.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = AppTheme.colors.mainColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Add Photo Evidence",
                            color = AppTheme.colors.mainColor
                        )
                    }

                    imageUri?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(data = it).apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                    }).build()
                                ),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            val imageUrl = imageUri?.let {
                                bookingService.uploadFeedbackImage(context, it)
                            }
                            selectedStar?.let { rating ->
                                onSubmit(rating, feedback, imageUrl)
                            }
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading && selectedStar != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.mainColor
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Submit Review", color = Color.White)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}