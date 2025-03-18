package com.example.library.components.trimrangeslider.handlers

import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.events.TrimSliderEventHandler
import com.example.library.components.trimrangeslider.model.TrimSliderState

class RightHandleClickedHandler : TrimSliderEventHandler<TrimSliderEvent.OnRightHandleClicked> {
    override fun handle(
        state: TrimSliderState,
        event: TrimSliderEvent.OnRightHandleClicked,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState {
        sendEffect(
            TrimSliderEffect.PreviewVideo(rightHandlePosition = event.position)
        )
        return state
    }
}