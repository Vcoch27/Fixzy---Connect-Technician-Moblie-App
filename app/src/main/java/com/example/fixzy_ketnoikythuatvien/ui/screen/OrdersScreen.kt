package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.data.model.OrderStatus
import com.example.fixzy_ketnoikythuatvien.data.model.orderList
import com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents.OrderItemCard
import com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents.OrdersTabRow
import com.example.fixzy_ketnoikythuatvien.ui.components.publicComponents.TopBar

@Composable
fun OrdersScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "History", "Saved")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 10.dp, end = 10.dp)
    ) {
        // Tiêu đề
        TopBar(navController, "Orders")

        // Tabs
        OrdersTabRow(selectedTabIndex, tabs) { index -> selectedTabIndex = index }

        // Danh sách đơn hàng theo trạng thái
        val filteredOrders = when (selectedTabIndex) {
            0 -> orderList.filter { it.status == OrderStatus.CONFIRMED }  // Upcoming
            1 -> orderList.filter { it.status == OrderStatus.DONE }       // History
            2 -> orderList.filter { it.status == OrderStatus.NOT_BOOKED } // Saved
            else -> emptyList()
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredOrders) { order ->
                OrderItemCard(order)
            }
        }
    }
}
