package com.example.fixzy_ketnoikythuatvien.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


val Pink = Color(0xFFF28482)
val Green = Color(0xFF84A59D)
val Yellow = Color(0xFFF7EDE2)
val YellowLight = Color(0xFFFFFFF2)
val Dark = Color(0xFF3D405B)

@Immutable
data class AppColors(
    val mainColor: Color,
    val background: Color,
    val onBackground: Color,
    val onBackgroundVariant: Color,
    val surface: Color,
    val onSurface: Color,
    val secondarySurface: Color,
    val onSecondarySurface: Color,
    val regularSurface: Color,
    val onRegularSurface: Color,
    val actionSurface: Color,
    val onActionSurface: Color,
    val highlightSurface: Color,
    val onHighlightSurface: Color
)

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        mainColor = Color.Unspecified,
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        onBackgroundVariant= Color.Unspecified,
        surface = Color.Unspecified,
        onSurface = Color.Unspecified,
        secondarySurface = Color.Unspecified,
        onSecondarySurface = Color.Unspecified,
        regularSurface = Color.Unspecified,
        onRegularSurface = Color.Unspecified,
        actionSurface = Color.Unspecified,
        onActionSurface = Color.Unspecified,
        highlightSurface = Color.Unspecified,
        onHighlightSurface = Color.Unspecified,

    )
}

val extendedColor = AppColors(
    mainColor = Color(0xFF026E9B),
    background = Color.White,
    onBackground = Dark,
    onBackgroundVariant = Color(0xFF4C7C6C),
    surface = Color.White,
    onSurface = Dark,
    secondarySurface = Pink,
    onSecondarySurface = Color.White,
    regularSurface = YellowLight,
    onRegularSurface = Dark,
    actionSurface = Yellow,
    onActionSurface = Pink,
    highlightSurface = Green,
    onHighlightSurface = Color.White
)