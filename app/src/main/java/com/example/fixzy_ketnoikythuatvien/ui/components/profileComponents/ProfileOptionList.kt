package com.example.fixzy_ketnoikythuatvien.ui.components.profileComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ProfileOptionList(onLogout: () -> Unit) {
    val options = listOf(
        ProfileOption("Edit Profile", Icons.Default.Edit),
        ProfileOption("Notification", Icons.Default.Notifications),
        ProfileOption("Payment method", Icons.Default.CreditCard),
        ProfileOption("Help & support", Icons.AutoMirrored.Filled.Help),
        ProfileOption("Logout", Icons.AutoMirrored.Filled.Logout)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            ProfileOptionItem(
                option = option,
                onOptionClick = {
                    when (option.title) {
                        "Logout" -> onLogout()
                        "Edit Profile" -> { /* Handle edit profile */ }
                        "Notification" -> { /* Handle notification */ }
                        "Payment method" -> { /* Handle payment */ }
                        "Help & support" -> { /* Handle help */ }
                    }
                }
            )
            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}


data class ProfileOption(val title: String, val icon: ImageVector)