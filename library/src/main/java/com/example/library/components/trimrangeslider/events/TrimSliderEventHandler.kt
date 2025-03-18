package com.example.library.components.trimrangeslider.events

import com.example.library.components.trimrangeslider.model.TrimSliderState

interface TrimSliderEventHandler<T : TrimSliderEvent> {
    fun handle(
        state: TrimSliderState,
        event: T,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState
}