package com.example.library.components.trimrangeslider.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun UnselectedLeftArea(styles: TrimSliderStyles = TrimSliderStyles()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                val width = size.width
                val height = size.height
                val radius = styles.cornerRadius.toPx()
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)
                    drawContent()
                    drawRoundRect(
                        color = Color.Transparent,
                        topLeft = Offset(
                            width - radius, 0f
                        ),
                        size = Size(2 * radius, height),
                        cornerRadius = CornerRadius(radius, radius),
                        blendMode = BlendMode.SrcOut
                    )
                    restoreToCount(checkPoint)
                }
            }
            .background(styles.unselectedAreaOverlayColor)
    )
}

@Composable
fun UnselectedRightArea(styles: TrimSliderStyles = TrimSliderStyles()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                val height = size.height
                val radius = styles.cornerRadius.toPx()
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)
                    drawContent()
                    drawRoundRect(
                        color = Color.Transparent,
                        topLeft = Offset(-radius, 0f),
                        size = Size(2 * radius, height),
                        cornerRadius = CornerRadius(radius, radius),
                        blendMode = BlendMode.SrcOut
                    )
                    restoreToCount(checkPoint)
                }
            }
            .background(styles.unselectedAreaOverlayColor)
    )
}
