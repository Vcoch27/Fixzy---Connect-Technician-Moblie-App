package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.ProviderService
import com.example.fixzy_ketnoikythuatvien.service.UserService
import com.example.fixzy_ketnoikythuatvien.service.model.Registration
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.text.SimpleDateFormat
import java.util.Locale
@Composable
fun ProviderModeScreen(
    navController: NavController
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val state by Store.stateFlow.collectAsStateWithLifecycle()

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var yearsOfExperience by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var formError by remember { mutableStateOf<String?>(null) }
    var hasFetchedRegistration by remember { mutableStateOf(false) }

    val providerService = remember { ProviderService() }
    val userService = remember { UserService() }
    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
        }

    val registration = state.registration

    // Fetch registration once when screen loads and registration is null
    LaunchedEffect(state.user?.id) {
        if (!hasFetchedRegistration && state.user?.id != null) {
            hasFetchedRegistration = true
            providerService.getRegistration(state.user!!.id)
        }
    }

    // Auto-clear error after 5 seconds
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            delay(5000)
            errorMessage = null
        }
    }

    LaunchedEffect(state.orderUrl) {
        state.orderUrl?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }
//    LaunchedEffect(state.appTransId) {
//        coroutineScope.launch {
//            state.appTransId?.let {
//                while (true) {
//                    try {
//                        providerService.getRegistration(state.user?.id ?: return@let)
//                        delay(20000)
//                        Log.i(
//                            "ProviderModeScreen",
//                            "Fetching registration ${state.registration?.paymentStatus}"
//                        )
//                        if (state.registration?.paymentStatus == "paid") {
//                            navController.navigate("home_page") {
//                                popUpTo(navController.graph.startDestinationId)
//                                launchSingleTop = true
//                            }
//                            break
//                        }
//                    } catch (e: Exception) {
//                        Log.e("ProviderModeScreen", "Error fetching registration: ${e.message}")
//                        delay(10000) // Wait before retrying
//                    }
//                }
//            }
//        }
//
//    }
    var isTechnician = state.user?.role == "technician"
    Log.d("ProviderModeScreen", "isTechnician:${state.user?.role}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()

            errorMessage != null -> Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )

            isTechnician -> {
                navController.navigate("provider_screen/${state.user?.id}")
            }

            registration == null -> {
                // Registration form
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Provider Registration",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = AppTheme.colors.mainColor
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = yearsOfExperience,
                                onValueChange = { if (it.length <= 2) yearsOfExperience = it },
                                label = { Text("Years of Experience") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.colors.mainColor,
                                    focusedLabelColor = AppTheme.colors.mainColor
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = bio,
                                onValueChange = { if (it.length <= 500) bio = it },
                                label = { Text("Professional Bio") },
                                placeholder = { Text("Tell us about your expertise and experience...") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 4,
                                maxLines = 6,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.colors.mainColor,
                                    focusedLabelColor = AppTheme.colors.mainColor
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Certificate Image Selection
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clickable { imagePicker.launch("image/*") },
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    if (imageUri != null) {
                                        Image(
                                            painter = rememberAsyncImagePainter(imageUri),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AddAPhoto,
                                                contentDescription = null,
                                                tint = AppTheme.colors.mainColor,
                                                modifier = Modifier.size(48.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                "Upload Certificate",
                                                color = AppTheme.colors.mainColor
                                            )
                                        }
                                    }
                                }
                            }

                            // Error Message
                            formError?.let {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = {
                                    when {
                                        yearsOfExperience.isBlank() || yearsOfExperience.toIntOrNull() == null ->
                                            formError = "Số năm kinh nghiệm phải là một số hợp lệ"

                                        yearsOfExperience.toInt() <= 0 ->
                                            formError = "Số năm kinh nghiệm phải lớn hơn 0"

                                        bio.isBlank() || bio.length < 10 ->
                                            formError = "Giới thiệu phải có ít nhất 10 ký tự"

                                        imageUri == null ->
                                            formError = "Vui lòng chọn ảnh chứng chỉ"

                                        else -> {
                                            formError = null
                                            showConfirmDialog = true
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppTheme.colors.mainColor
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    "Submit Registration",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                if (showConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showConfirmDialog = false },
                        title = { Text("Confirm registration") },
                        text = { Text("Are you sure you want to submit your application?") },
                        confirmButton = {
                            Button(onClick = {
                                showConfirmDialog = false
                                coroutineScope.launch {
                                    isLoading = true
                                    try {
                                        val certificateUrl =
                                            userService.uploadAvatar(context, imageUri!!)
                                        state.user?.id?.let { userId ->
                                            providerService.registerProvider(
                                                userId = userId,
                                                yearsOfExperience = yearsOfExperience.toInt(),
                                                bio = bio,
                                                certificateUrl = certificateUrl
                                            )
                                            // Giả định rằng registration sẽ được cập nhật lại ở state sau khi gọi API
                                            delay(1000)
                                            providerService.getRegistration(userId)
                                        } ?: run {
                                            errorMessage = "Không tìm thấy ID người dùng"
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Lỗi không xác định"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }) { Text("Xác nhận") }
                        },
                        dismissButton = {
                            Button(onClick = { showConfirmDialog = false }) { Text("Hủy") }
                        }
                    )
                }
            }

            else -> {
                when (registration.status) {
                    "pending" -> {

                        Spacer(modifier = Modifier.height(16.dp))
                        RegistrationInfo(registration)
                    }

                    "rejected" -> {
                        Text(
                            "Your application has been rejected",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            // Cho phép gửi lại
                            hasFetchedRegistration = false
                        }) {
                            Text("Resend application")
                        }
                    }

                    "confirmed" -> {
                        Text(
                            "Your application has been approved!",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        RegistrationInfo(registration)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Complete to become a Provider",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Monthly Fee: ${registration.monthlyFee}VND")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                providerService.createPayment(registration.id, registration.userId)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.colors.mainColor,
                            ),
                            modifier = Modifier.fillMaxWidth()

                        ) {
                            if (state.isGetPayment) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Continue to checkout")
                            }
                        }
                        state.getPaymentError?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Lỗi: $it",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    else -> {
                        Text(
                            "Trạng thái đơn không xác định",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegistrationInfo(registration: Registration) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Registration Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.mainColor
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Experience",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                    Text(
                        "${registration.yearsOfExperience} years",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Status",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.colors.onBackgroundVariant
                    )
                    registration.status?.let { StatusChip(it) }
                }
            }

            // Bio Section
            Column {
                Text(
                    "Professional Bio",
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.colors.onBackgroundVariant
                )
                Text(
                    registration.bio,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Certificate Image
            Column {
                Text(
                    "Certificate",
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.colors.onBackgroundVariant
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    AsyncImage(
                        model = registration.certificateUrl,
                        contentDescription = "Certificate",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Registration Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "Submitted: ${formatDate(registration.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.onBackgroundVariant
                )
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    Surface(
        modifier = Modifier
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        color = when(status) {
            "pending" -> Color(0xFFFFF3E0)
            "confirmed" -> Color(0xFFE8F5E9)
            "rejected" -> Color(0xFFFFEBEE)
            else -> Color.LightGray
        }
    ) {
        Text(
            text = status.capitalize(Locale.ROOT),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = when(status) {
                "pending" -> Color(0xFFF57C00)
                "confirmed" -> Color(0xFF2E7D32)
                "rejected" -> Color(0xFFD32F2F)
                else -> Color.DarkGray
            }
        )
    }
}

// Add this utility function
private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}
