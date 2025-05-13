package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
            .padding(top = 28.dp, start = 10.dp, end = 10.dp),
        topBar = {
            TopBar(
                navController = navController,
                title = "Chỉnh sửa hồ sơ",

                )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar section
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                Log.d("EditProfileScreen", "currentAvatarUrl: $currentAvatarUrl")
                val painter = when {
                    isAvatarChanged && tempAvatarUri != null -> rememberAsyncImagePainter(
                        tempAvatarUri
                    )

                    currentAvatarUrl.isNotEmpty() -> rememberAsyncImagePainter(currentAvatarUrl)
                    else -> painterResource(R.drawable.coc)
                }

                Image(
                    painter = painter,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .background(AppTheme.colors.mainColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "Change avatar",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form fields

            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    fullNameError = if (it.isBlank()) "Họ tên không được để trống" else null
                },
                label = { Text("Họ và tên") },
                modifier = Modifier.fillMaxWidth(),
                isError = fullNameError != null,
                supportingText = { fullNameError?.let { Text(it, color = Color.Red) } },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    if (it.length <= 15 && (it.isEmpty() || it.all { c -> c.isDigit() })) {
                        phone = it
                        phoneError = when {
                            it.isBlank() -> "Số điện thoại không được để trống"
                            it.length < 10 -> "Số điện thoại quá ngắn"
                            !it.startsWith("0") -> "Số điện thoại phải bắt đầu bằng 0"
                            else -> null
                        }
                    }
                },
                label = { Text("Số điện thoại") },
                modifier = Modifier.fillMaxWidth(),
                isError = phoneError != null,
                supportingText = { phoneError?.let { Text(it, color = Color.Red) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                    addressError = if (it.isBlank()) "Địa chỉ không được để trống" else null
                },
                label = { Text("Địa chỉ") },
                modifier = Modifier.fillMaxWidth(),
                isError = addressError != null,
                supportingText = { addressError?.let { Text(it, color = Color.Red) } },
                singleLine = false,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.mainColor,
                    contentColor = Color.White
                )
            ) {
                when {
                    isUploading -> Text("Updating.....")
                    isLoading -> Text("Updating.....")
                    else -> Text("Update")
                }
            }
        }
    }
}