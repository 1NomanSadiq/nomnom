package com.example.library.components.trimrangeslider.handlers

import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.events.TrimSliderEventHandler
import com.example.library.components.trimrangeslider.model.TrimSliderState
import com.example.library.components.trimrangeslider.ui.HandleType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DragEndedHandler : TrimSliderEventHandler<TrimSliderEvent.OnDragEnded> {
    override fun handle(
        state: TrimSliderState,
        event: TrimSliderEvent.OnDragEnded,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState {
        val newState = state.copy(currentDragHandle = null)

        sendEffect(TrimSliderEffect.ResumeVideo)

        when (event.handleType) {
            HandleType.RIGHT -> sendEffect(
                TrimSliderEffect.PreviewVideo(
                    rightHandlePosition = state.rightHandlePosition
                )
            )
            else -> Unit
        }

        return newState
    }
}