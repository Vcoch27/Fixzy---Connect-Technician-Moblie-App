package com.example.fixzy_ketnoikythuatvien.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SwipeBackWrapper(
    navController: NavController,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX)

    Box(
        modifier = modifier
            .offset(x = animatedOffsetX.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        offsetX = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        if (offsetX + dragAmount >= 0) {
                            offsetX += dragAmount
                        }
                    },
                    onDragEnd = {
                        if (offsetX > 200 && navController.previousBackStackEntry != null) {
                            navController.navigateUp()
                        }
                        offsetX = 0f
                    }
                )
            }
        ,
        content = content
    )
}