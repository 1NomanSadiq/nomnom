package com.example.library.components.trimrangeslider.handlers

import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.events.TrimSliderEventHandler
import com.example.library.components.trimrangeslider.model.TrimSliderState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CursorMovedHandler : TrimSliderEventHandler<TrimSliderEvent.OnCursorMoved> {
    override fun handle(
        state: TrimSliderState,
        event: TrimSliderEvent.OnCursorMoved,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState {
        val newPosition = event.absolutePosition
        val newState = state.copy(absoluteCursorPosition = newPosition)

        sendEffect(
            TrimSliderEffect.UpdatePosition(
                shouldSeek = true,
                cursorPosition = newPosition,
                leftHandlePosition = state.leftHandlePosition,
                rightHandlePosition = state.rightHandlePosition
            )
        )

        return newState
    }
}