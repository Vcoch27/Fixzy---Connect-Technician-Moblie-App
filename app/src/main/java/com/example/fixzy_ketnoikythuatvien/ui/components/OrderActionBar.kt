package com.example.fixzy_ketnoikythuatvien.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fixzy_ketnoikythuatvien.R
import com.example.fixzy_ketnoikythuatvien.data.model.OrderData
import com.example.fixzy_ketnoikythuatvien.data.model.OrderState
import com.example.fixzy_ketnoikythuatvien.ui.theme.AppTheme

@Composable
fun OrderActionBar(
    modifier: Modifier = Modifier,
    state: OrderState = OrderData,
    onAddItemClick: () -> Unit,
    onRemoveItemClick: () -> Unit,
    onCheckOutClick: () -> Unit,
    navController: NavController

) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(76.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { navController.navigate("home_page") }
            ) {
                Text("Click Me")
            }
            // Hiển thị Selector chiếm 50% chiều rộng của Row
            Selector(
                amount = state.amount,
                onAddItemClicked = onAddItemClick,
                onRemoveItemClicked = onRemoveItemClick,
                modifier = Modifier.weight(1f)
            )

            // Hiển thị Cart chiếm 50% chiều rộng của Row
            Cart(
                totalPrice = state.totalPrice,
                onClicked = onAddItemClick,
                modifier = Modifier.weight(1f)
            )
        }

    }
}

@Composable
private fun Selector(
    modifier: Modifier = Modifier,
    amount: Int,
    onAddItemClicked: () -> Unit,
    onRemoveItemClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = AppTheme.colors.secondarySurface,
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectorButton(
                iconRes = R.drawable.ic_minus,
                contentColor = AppTheme.colors.onActionSurface,
                containerColor = AppTheme.colors.actionSurface,
                onClicked = onRemoveItemClicked
            )
            Text(
                text = amount.toString(),
                style = AppTheme.typography.titleLarge,
                color = AppTheme.colors.onSurface
            )
            SelectorButton(
                iconRes = R.drawable.ic_plus,
                contentColor = AppTheme.colors.onActionSurface,
                containerColor = AppTheme.colors.actionSurface,
                onClicked = onAddItemClicked
            )
        }

    }
}

@Composable
private fun SelectorButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    containerColor: Color,
    contentColor: Color,
    onClicked: () -> Unit
) {
    Surface(
        modifier = modifier.size(24.dp),
        shape = CircleShape, //bo tròn hoàn toàn
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClicked),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes), //tải icon từ tài nguyên
                contentDescription = null,
                modifier = Modifier.size(10.dp)

            )
        }
    }
}

@Composable
private fun Cart(
    modifier: Modifier = Modifier,
    totalPrice: String,
    onClicked: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClicked),
        color = AppTheme.colors.secondarySurface,
        contentColor = AppTheme.colors.onSecondarySurface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add to Cart",
                    style = AppTheme.typography.titleSmall
                )
                Text(
                    text = totalPrice,
                    style = AppTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
