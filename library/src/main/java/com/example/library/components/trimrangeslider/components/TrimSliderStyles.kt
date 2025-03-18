package com.example.library.components.trimrangeslider.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class TrimSliderStyles(
    val containerHeight: Dp = 68.dp,
    val cornerRadius: Dp = 16.dp,
    val handleWidth: Dp = 16.dp,
    val handleHeight: Dp = 32.dp,
    val handleCornerRadius: Dp = 4.dp,
    val iconSize: Dp = 10.dp,
    val cursorWidth: Dp = 4.dp,
    val borderWidth: Dp = 3.dp,
    val overreachPadding: Dp = 4.dp,

    // Calculated values
    val cursorOffset: Dp = -cursorWidth / 2,
    val handleOffset: Dp = -handleWidth / 2,

    // Colors
    val unselectedAreaOverlayColor: Color = Color(0x80000000),
    val handleBackgroundColor: Color = Color.Black,
    val handleIconTint: Color = Color.White,
    val cursorColor: Color = Color.Black
)