package com.example.library.components.trimrangeslider.handlers

import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.events.TrimSliderEventHandler
import com.example.library.components.trimrangeslider.model.TrimSliderState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DragStartedHandler : TrimSliderEventHandler<TrimSliderEvent.OnDragStarted> {
    override fun handle(
        state: TrimSliderState,
        event: TrimSliderEvent.OnDragStarted,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState {
        val newState = state.copy(currentDragHandle = event.handleType)

        sendEffect(TrimSliderEffect.PauseVideo)

        return newState
    }
}