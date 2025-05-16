package com.example.fixzy_ketnoikythuatvien.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.store.Store
import com.example.fixzy_ketnoikythuatvien.service.BookingService
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents.OrderItemCard
import com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents.OrdersTabRow
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar
import com.example.fixzy_ketnoikythuatvien.utils.NotificationHelper

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(
    navController: NavController,
) {
    val bookingService = remember { BookingService() }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "History", "Saved")
    val state by Store.stateFlow.collectAsState()
    val bookings = remember { mutableStateListOf<DetailBooking>() }

    LaunchedEffect(state.bookings) {
        bookings.clear()
        bookings.addAll(state.bookings)
    }

    // Fetch bookings on initial load
    LaunchedEffect(Unit) {
        bookingService.getBookingsForUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 10.dp, end = 10.dp)
    ) {
        TopBar(navController, "Orders")

        OrdersTabRow(selectedTabIndex, tabs) { index -> selectedTabIndex = index }

        val filteredBookings = when (selectedTabIndex) {
            0 -> bookings.filter { it.status in listOf("Pending", "Confirmed","WaitingForCustomerConfirmation") } // Upcoming
            1 -> bookings.filter { it.status in listOf("Completed", "Cancelled") } // History
            2 -> emptyList<DetailBooking>()
            else -> emptyList()
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredBookings, key = { it.booking_id }) { booking ->
                OrderItemCard(
                    booking = booking,
                    onBookingUpdated = { updatedBooking ->
                        // Cập nhật cả local state và Redux store
                        val index = bookings.indexOfFirst { it.booking_id == updatedBooking.booking_id }
                        if (index != -1) {
                            bookings[index] = updatedBooking
                        }
                        Store.store.dispatch(Action.UpdateBookingAction(updatedBooking)) // Thêm dòng này
                    },
                    navController = navController
                )
            }
        }
    }
}