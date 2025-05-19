package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cloudinary.Cloudinary
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.data_class.AppState
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.UserService
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    navController: NavController,
) {
    val userService = remember { UserService() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by Store.stateFlow.collectAsState()

    // Sử dụng safe call và giá trị mặc định
    val currentUser = state.user ?: run {
        navController.popBackStack()
        return
    }

    var fullName by remember { mutableStateOf(currentUser.name ?: "") }
    var phone by remember { mutableStateOf(currentUser.phone ?: "") }
    var address by remember { mutableStateOf(currentUser.address ?: "") }
    val currentAvatarUrl by remember { mutableStateOf(currentUser.avatarUrl ?: "") }
    var tempAvatarUri by remember { mutableStateOf<Uri?>(null) }
    var isAvatarChanged by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            tempAvatarUri = it
            isAvatarChanged = true
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp),
        topBar = {
            TopBar(
                navController = navController,
                title = "Edit Profile",
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8FF))
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar Section with improved design
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                shape = CircleShape,
                                border = BorderStroke(3.dp, AppTheme.colors.mainColor.copy(alpha = 0.1f)),
                                color = Color.White,
                                shadowElevation = 8.dp
                            ) {
                                Image(
                                    painter = when {
                                        isAvatarChanged && tempAvatarUri != null -> rememberAsyncImagePainter(tempAvatarUri)
                                        currentAvatarUrl.isNotEmpty() -> rememberAsyncImagePainter(currentAvatarUrl)
                                        else -> painterResource(R.drawable.coc)
                                    },
                                    contentDescription = "Avatar",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            IconButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier
                                    .size(40.dp)
                                    .offset(x = (-4).dp, y = (-4).dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(35.dp),
                                    shape = CircleShape,
                                    color = AppTheme.colors.mainColor,
                                    shadowElevation = 4.dp
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = "Change avatar",
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(20.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // Form Fields with improved styling
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Full Name Field
                        Column {
                            Text(
                                "Full Name",
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colors.onBackgroundVariant
                            )
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = {
                                    fullName = it
                                    fullNameError = if (it.isBlank()) "Name is required" else null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                isError = fullNameError != null,
                                supportingText = { fullNameError?.let { Text(it, color = Color.Red) } },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.colors.mainColor,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Phone Field
                        Column {
                            Text(
                                "Phone Number",
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colors.onBackgroundVariant
                            )
                            OutlinedTextField(
                                value = phone,
                                onValueChange = {
                                    if (it.length <= 15 && (it.isEmpty() || it.all { c -> c.isDigit() })) {
                                        phone = it
                                        phoneError = when {
                                            it.isBlank() -> "Phone number is required"
                                            it.length < 10 -> "Phone number is too short"
                                            !it.startsWith("0") -> "Phone number must start with 0"
                                            else -> null
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                isError = phoneError != null,
                                supportingText = { phoneError?.let { Text(it, color = Color.Red) } },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.colors.mainColor,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Address Field
                        Column {
                            Text(
                                "Address",
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colors.onBackgroundVariant
                            )
                            OutlinedTextField(
                                value = address,
                                onValueChange = {
                                    address = it
                                    addressError = if (it.isBlank()) "Address is required" else null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                isError = addressError != null,
                                supportingText = { addressError?.let { Text(it, color = Color.Red) } },
                                maxLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.colors.mainColor,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Update Button with loading state
                Button(
                    onClick = {
                        scope.launch {
                            fullNameError =
                                if (fullName.isBlank()) "Họ tên không được để trống" else null
                            phoneError = when {
                                phone.isBlank() -> "Số điện thoại không được để trống"
                                phone.length < 10 -> "Số điện thoại quá ngắn"
                                !phone.startsWith("0") -> "Số điện thoại phải bắt đầu bằng 0"
                                else -> null
                            }
                            addressError =
                                if (address.isBlank()) "Địa chỉ không được để trống" else null

                            if (fullNameError == null && phoneError == null && addressError == null) {
                                isLoading = true
                                try {
                                    var finalAvatarUrl = currentAvatarUrl

                                    // Chỉ upload ảnh nếu có thay đổi
                                    if (isAvatarChanged && tempAvatarUri != null) {
                                        isUploading = true
                                        try {
                                            finalAvatarUrl =
                                                userService.uploadAvatar(context, tempAvatarUri!!)
                                        } finally {
                                            isUploading = false
                                        }
                                    }

                                    userService.updateProfile(
                                        fullName = fullName.takeIf { it.isNotEmpty() },
                                        phone = phone.takeIf { it.isNotEmpty() },
                                        address = address.takeIf { it.isNotEmpty() },
                                        avatarUrl = finalAvatarUrl.takeIf { it.isNotEmpty() },
                                        context = context
                                    )

                                    navController.popBackStack()
                                } finally {
                                    isLoading = false
                                }
                            }

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.mainColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading && !isUploading
                ) {
                    if (isLoading || isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Update Profile",
                            style = AppTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}