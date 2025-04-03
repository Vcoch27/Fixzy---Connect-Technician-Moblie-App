package com.example.fixzy_ketnoikythuatvien.ui.components.notificationComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun NotificationFilterDropdown(
    selectedFilter: String,
    filters: List<String>,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedFilter, style = AppTheme.typography.body, color = AppTheme.colors.mainColor)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown", tint = AppTheme.colors.mainColor)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            filters.forEach { filter ->
                DropdownMenuItem(
                    text = { Text(text = filter, color = AppTheme.colors.mainColor, style = AppTheme.typography.bodySmall) },
                    onClick = {
                        onFilterSelected(filter)
                        expanded = false
                    }
                )
            }
        }
    }
}
