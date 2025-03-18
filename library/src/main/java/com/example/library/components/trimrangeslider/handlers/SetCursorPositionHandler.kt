package com.example.library.components.trimrangeslider.handlers

import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.events.TrimSliderEventHandler
import com.example.library.components.trimrangeslider.model.TrimSliderState

class SetCursorPositionHandler : TrimSliderEventHandler<TrimSliderEvent.SetCursorPosition> {
    override fun handle(
        state: TrimSliderState,
        event: TrimSliderEvent.SetCursorPosition,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState {
        val newPosition = event.position
        val newState = state.copy(absoluteCursorPosition = newPosition)

        sendEffect(
            TrimSliderEffect.UpdatePosition(
                shouldSeek = false,
                cursorPosition = newPosition,
                leftHandlePosition = state.leftHandlePosition,
                rightHandlePosition = state.rightHandlePosition
            )
        )

        return newState
    }
}