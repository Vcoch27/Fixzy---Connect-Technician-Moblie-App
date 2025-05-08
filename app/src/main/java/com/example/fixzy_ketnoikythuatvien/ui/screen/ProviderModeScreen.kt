package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
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

            errorMessage != null -> Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            isTechnician -> {
                Text("Bạn đã là Technician!", style = MaterialTheme.typography.headlineSmall)
            }

            registration == null -> {
                // Registration form
                Text("Đăng ký Provider Mode", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = yearsOfExperience,
                    onValueChange = { yearsOfExperience = it },
                    label = { Text("Số năm kinh nghiệm") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { if (it.length <= 500) bio = it },
                    label = { Text("Giới thiệu bản thân") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { imagePicker.launch("image/*") }) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Chọn ảnh chứng chỉ"
                        )
                    }
                    Text(if (imageUri != null) "Đã chọn ảnh" else "Chưa chọn ảnh")
                }

                formError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                }

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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Đăng ký")
                }

                if (showConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showConfirmDialog = false },
                        title = { Text("Xác nhận đăng ký") },
                        text = { Text("Bạn có chắc chắn muốn gửi đơn đăng ký?") },
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
                        Text("Đơn đăng ký của bạn đang được xử lý", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        RegistrationInfo(registration)
                    }

                    "rejected" -> {
                        Text("Đơn đăng ký của bạn đã bị từ chối", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            // Cho phép gửi lại
                            hasFetchedRegistration = false
                        }) {
                            Text("Gửi lại đơn")
                        }
                    }

                    "confirmed" -> {
                        Text("Đơn đăng ký của bạn đã được duyệt!", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        RegistrationInfo(registration)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Hoàn thành để trở thành Provider", style = MaterialTheme.typography.titleMedium)
                        Text("Phí hàng tháng: ${registration.monthlyFee}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                // Thanh toán
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Tiếp tục thanh toán")
                        }
                    }

                    else -> {
                        Text("Trạng thái đơn không xác định", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationInfo(registration: Registration) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("THÔNG TIN ĐĂNG KI:")
        Text("Số năm kinh nghiệm: ${registration.yearsOfExperience}")
        Text("Giới thiệu: ${registration.bio}")
        Image(
            painter = rememberAsyncImagePainter(
                model = registration.certificateUrl
            ),
            contentDescription = "Profile Picture",
            modifier = Modifier.size(200.dp)
        )
        Text("Trạng thái: ${registration.status}")
        Text("Ngày tạo: ${registration.createdAt}")
    }
}
