package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents.OrderItemCard
import com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents.OrdersTabRow
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar

@Composable
fun OrdersScreen(
    navController: NavController,
     // Inject BookingService để gọi API
) {
    var bookingService: BookingService = BookingService()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "History", "Saved")
    LaunchedEffect(Unit) {
        bookingService.getBookingsForUser()
    }

    val state by Store.stateFlow.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 10.dp, end = 10.dp)
    ) {
        TopBar(navController, "Orders")

        OrdersTabRow(selectedTabIndex, tabs) { index -> selectedTabIndex = index }

        val filteredBookings = when (selectedTabIndex) {
            0 -> state.bookings.filter { it.status in listOf("Pending", "Confirmed") } // Upcoming
            1 -> state.bookings.filter { it.status in listOf("Completed", "Cancelled") } // History
            2 -> emptyList<DetailBooking>() // Saved: Chưa xử lý
            else -> emptyList()
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredBookings) { booking ->
                OrderItemCard(booking)
            }
        }
    }
}