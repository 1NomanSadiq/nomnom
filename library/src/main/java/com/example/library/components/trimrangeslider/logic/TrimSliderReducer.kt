package com.example.library.components.trimrangeslider.logic

import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.handlers.CursorMovedHandler
import com.example.library.components.trimrangeslider.handlers.DragEndedHandler
import com.example.library.components.trimrangeslider.handlers.DragStartedHandler
import com.example.library.components.trimrangeslider.handlers.LeftHandleClickedHandler
import com.example.library.components.trimrangeslider.handlers.LeftHandleMovedHandler
import com.example.library.components.trimrangeslider.handlers.RightHandleClickedHandler
import com.example.library.components.trimrangeslider.handlers.RightHandleMovedHandler
import com.example.library.components.trimrangeslider.handlers.SetCursorPositionHandler
import com.example.library.components.trimrangeslider.model.TrimSliderState

class TrimSliderReducer {
    private val leftHandleMovedHandler = LeftHandleMovedHandler()
    private val rightHandleMovedHandler = RightHandleMovedHandler()
    private val cursorMovedHandler = CursorMovedHandler()
    private val dragStartedHandler = DragStartedHandler()
    private val dragEndedHandler = DragEndedHandler()
    private val leftHandleClickedHandler = LeftHandleClickedHandler()
    private val rightHandleClickedHandler = RightHandleClickedHandler()
    private val setCursorPositionHandler = SetCursorPositionHandler()

    fun reduce(
        state: TrimSliderState,
        event: TrimSliderEvent,
        sendEffect: (TrimSliderEffect) -> Unit
    ): TrimSliderState = when (event) {
        is TrimSliderEvent.OnLeftHandleMoved -> leftHandleMovedHandler.handle(state, event, sendEffect)
        is TrimSliderEvent.OnRightHandleMoved -> rightHandleMovedHandler.handle(state, event, sendEffect)
        is TrimSliderEvent.OnCursorMoved -> cursorMovedHandler.handle(state, event, sendEffect)
        is TrimSliderEvent.OnDragStarted -> dragStartedHandler.handle(state, event, sendEffect)
        is TrimSliderEvent.OnDragEnded -> dragEndedHandler.handle(state, event, sendEffect)
        is TrimSliderEvent.SetCursorPosition -> setCursorPositionHandler.handle(state, event, sendEffect)
        is TrimSliderEvent.OnLeftHandleClicked -> leftHandleClickedHandler.handle(state, event, sendEffect)
        is TrimSliderEvent.OnRightHandleClicked -> rightHandleClickedHandler.handle(state, event, sendEffect)
    }
}