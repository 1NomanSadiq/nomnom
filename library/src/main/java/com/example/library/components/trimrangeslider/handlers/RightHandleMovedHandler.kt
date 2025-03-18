package com.example.library.components.trimrangeslider.handlers

import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.events.TrimSliderEventHandler
import com.example.library.components.trimrangeslider.model.TrimSliderState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RightHandleMovedHandler : TrimSliderEventHandler<TrimSliderEvent.OnRightHandleMoved> {
    override fun handle(
        state: TrimSliderState,
        event: TrimSliderEvent.OnRightHandleMoved,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState {
        val newPosition = event.position
        val newState = state.copy(
            rightHandlePosition = newPosition,
            absoluteCursorPosition = newPosition
        )

        sendEffect(
            TrimSliderEffect.UpdatePosition(
                shouldSeek = true,
                cursorPosition = newPosition,
                leftHandlePosition = state.leftHandlePosition,
                rightHandlePosition = newPosition
            )
        )

        return newState
    }
}