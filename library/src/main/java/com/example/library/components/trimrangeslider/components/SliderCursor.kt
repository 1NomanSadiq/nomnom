package com.example.library.components.trimrangeslider.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SliderCursor(styles: TrimSliderStyles = TrimSliderStyles()) {
    Box(
        modifier = Modifier
            .width(styles.cursorWidth)
            .fillMaxHeight()
            .offset(x = styles.cursorOffset)
            .background(styles.cursorColor, shape = RoundedCornerShape(50))
    )
}
