package com.example.fixzy_ketnoikythuatvien.ui.components.oderComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun OrdersTabRow(
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.padding(vertical = 8.dp).shadow(elevation = 10.dp, shape = RoundedCornerShape(12.dp)), // Kết hợp màu nền và padding
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp),
                color = AppTheme.colors.mainColor
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        color = if (selectedTabIndex == index) Color.White else AppTheme.colors.mainColor,
                        style = AppTheme.typography.body
                    )
                },
                modifier = Modifier
                    .background(
                        if (selectedTabIndex == index) AppTheme.colors.mainColor else Color.White
                    )

            )

        }
    }
}