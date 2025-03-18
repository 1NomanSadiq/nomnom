package com.example.library.components.trimrangeslider.events

sealed class TrimSliderEffect {
    data object PauseVideo : TrimSliderEffect()
    data object ResumeVideo : TrimSliderEffect()
    data class UpdatePosition(val shouldSeek: Boolean = true, val cursorPosition: Float, val leftHandlePosition: Float, val rightHandlePosition: Float) : TrimSliderEffect()
    data class PreviewVideo(val rightHandlePosition: Float) : TrimSliderEffect()
}