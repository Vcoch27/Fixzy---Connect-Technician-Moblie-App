package com.example.fixzy_ketnoikythuatvien.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceDetail
import com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents.OrderItemCard
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun ServiceModeScreen(
    navController: NavController,
    service: ServiceDetail,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    var isServiceActive by remember { mutableStateOf(true) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var serviceSchedules by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Service Management") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Service Details") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Bookings") }
                )
            }

            when (selectedTab) {
                0 -> {
                    // Service Details Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = service.service_name,
                                        style = AppTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Price: ${service.service_price} VND",
                                        style = AppTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Rating: ${service.service_rating}",
                                        style = AppTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Orders Completed: ${service.service_orders_completed}",
                                        style = AppTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { isServiceActive = !isServiceActive },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isServiceActive) Color.Red else AppTheme.colors.mainColor
                                    )
                                ) {
                                    Text(if (isServiceActive) "Stop Service" else "Activate Service")
                                }
                                Button(
                                    onClick = { showDeleteConfirmDialog = true },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red
                                    )
                                ) {
                                    Text("Delete Service")
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Service Schedules",
                                style = AppTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (serviceSchedules.size < 5) {
                                        Button(
                                            onClick = { /* TODO: Add schedule */ },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.Default.Add, "Add Schedule")
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Add Schedule")
                                        }
                                    }
                                    
                                    serviceSchedules.forEach { schedule ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(schedule)
                                            IconButton(onClick = { /* TODO: Delete schedule */ }) {
                                                Icon(Icons.Default.Delete, "Delete Schedule")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Bookings Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // TODO: Replace with actual bookings data
                        items(emptyList()) { booking ->
                            OrderItemCard(
                                booking = booking,
                                onBookingUpdated = { /* TODO: Handle booking update */ }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Service") },
            text = { Text("Are you sure you want to delete this service?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // TODO: Handle service deletion
                        showDeleteConfirmDialog = false
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}