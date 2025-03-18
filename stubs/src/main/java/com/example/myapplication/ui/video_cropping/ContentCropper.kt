package com.example.myapplication.ui.video_cropping

import androidx.compose.runtime.Composable

@Composable
fun ContentCropper(
    videoResolution: Pair<Int, Int>,
    onCroppingDataChange: (CroppingData) -> Unit,
    content: @Composable () -> Unit
) {
    content()
}