package com.example.library.components.trimrangeslider.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

@Composable
fun SliderHandle(
    iconId: Int,
    contentDescription: String,
    styles: TrimSliderStyles
) {
    Box(
        modifier = Modifier
            .width(styles.handleWidth)
            .height(styles.handleHeight)
            .offset(x = styles.handleOffset)
            .clip(RoundedCornerShape(styles.handleCornerRadius))
            .background(styles.handleBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription,
            tint = styles.handleIconTint,
            modifier = Modifier.size(styles.iconSize)
        )
    }
}
