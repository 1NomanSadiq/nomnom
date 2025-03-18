package com.example.library.components.trimrangeslider.events

import com.example.library.components.trimrangeslider.ui.HandleType

sealed class TrimSliderEvent {
    data class OnLeftHandleMoved(val position: Float) : TrimSliderEvent()
    data class OnRightHandleMoved(val position: Float) : TrimSliderEvent()
    data class OnCursorMoved(val absolutePosition: Float) : TrimSliderEvent()
    data class OnDragStarted(val handleType: HandleType) : TrimSliderEvent()
    data class OnDragEnded(val handleType: HandleType) : TrimSliderEvent()
    data class OnLeftHandleClicked(val position: Float) : TrimSliderEvent()
    data class OnRightHandleClicked(val position: Float) : TrimSliderEvent()
    data class SetCursorPosition(val position: Float) : TrimSliderEvent()
}