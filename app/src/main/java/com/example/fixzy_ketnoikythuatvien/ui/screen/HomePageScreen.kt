package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.CategoriesSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.GreetingSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.OffersSection
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.SearchBar
import com.example.fixzy_ketnoikythuatvien.ui.components.homePageComponents.TopTechniciansSection
import com.example.fixzy_ketnoikythuatvien.ui.components.notifications.CustomAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHomePageScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val state by Store.stateFlow.collectAsState()
    val bookingService = remember { BookingService() }
    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }
    var notificationTitle by remember { mutableStateOf("") }
    val store = Store.store
    // Gọi API để lấy dữ liệu và thiết lập thông báo
    LaunchedEffect(Unit) {
    Log.d("ProductHomePageScreen", "howw??? ${state.hasShownBookingNotification})")
        // Thiết lập thông báo dựa trên vai trò người dùng
        if (!state.hasShownBookingNotification) {
            bookingService.getSummaryStatus()
            if (state.user?.role == "technician") {
                if (state.needAction.isNotEmpty() || state.todayBookings.isNotEmpty()) {
                    notificationTitle = "Thông báo đặt lịch"
                    val parts = mutableListOf<String>()
                    if (state.needAction.isNotEmpty()) {
                        parts.add("Bạn có ${state.needAction.size} đặt lịch cần xác nhận")
                    }
                    if (state.todayBookings.isNotEmpty()) {
                        parts.add("${state.todayBookings.size} đặt lịch hôm nay")
                    }
                    notificationMessage = parts.joinToString(" và ")
                    showNotification = true
                    state.hasShownBookingNotification = true
                }
            } else {
                if (state.needAction.isNotEmpty() || state.todayBookings.isNotEmpty()) {
                    notificationTitle = "Thông báo đặt lịch"
                    val parts = mutableListOf<String>()
                    if (state.needAction.isNotEmpty()) {
                        parts.add("Bạn có ${state.needAction.size} đặt lịch chờ bạn xác nhận hoàn thành")
                    }
                    if (state.todayBookings.isNotEmpty()) {
                        parts.add("${state.todayBookings.size} đặt lịch hôm nay")
                    }
                    notificationMessage = parts.joinToString(" và ")
                    showNotification = true
                    state.hasShownBookingNotification = true
                }
            }
        }

    }

    CustomAlertDialog(
        title = notificationTitle,
        message = notificationMessage,
        showDialog = showNotification,
        onDismiss = { showNotification = false },
        modifier = Modifier
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 38.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(top = 0.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
        ) {
            GreetingSection()
            SearchBar()
        }
        OffersSection()
        CategoriesSection(navController = navController)
        TopTechniciansSection(navController = navController)
    }
}