package com.example.library.components.trimrangeslider.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SelectedArea(styles: TrimSliderStyles) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(
                styles.borderWidth,
                styles.handleBackgroundColor,
                RoundedCornerShape(styles.cornerRadius)
            )
    )
}