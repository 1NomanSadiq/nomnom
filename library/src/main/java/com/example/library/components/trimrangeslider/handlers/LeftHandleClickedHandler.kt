package com.example.library.components.trimrangeslider.handlers

import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.events.TrimSliderEventHandler
import com.example.library.components.trimrangeslider.model.TrimSliderState

class LeftHandleClickedHandler : TrimSliderEventHandler<TrimSliderEvent.OnLeftHandleClicked> {
    override fun handle(
        state: TrimSliderState,
        event: TrimSliderEvent.OnLeftHandleClicked,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState {
        sendEffect(
            TrimSliderEffect.UpdatePosition(
                shouldSeek = true,
                cursorPosition = event.position,
                leftHandlePosition = event.position,
                rightHandlePosition = state.rightHandlePosition
            )
        )
        return state
    }
}