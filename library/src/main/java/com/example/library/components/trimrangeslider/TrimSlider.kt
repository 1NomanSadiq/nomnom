package com.example.library.components.trimrangeslider
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.library.nomnom.R
import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.logic.TrimRangeSliderState
import com.example.library.components.trimrangeslider.logic.TrimSliderReducer
import com.example.library.components.trimrangeslider.model.TrimSliderState
import com.example.library.components.trimrangeslider.components.SelectedArea
import com.example.library.components.trimrangeslider.components.SliderBackground
import com.example.library.components.trimrangeslider.components.SliderCursor
import com.example.library.components.trimrangeslider.components.SliderHandle
import com.example.library.components.trimrangeslider.components.TrimSliderStyles
import com.example.library.components.trimrangeslider.components.UnselectedLeftArea
import com.example.library.components.trimrangeslider.components.UnselectedRightArea
import com.example.library.components.trimrangeslider.ui.HandleType
import com.example.library.components.trimrangeslider.ui.TrimRangeSlider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Preview
@Composable
fun TrimSlider(
    initialState: TrimSliderState = TrimSliderState(),
    reducer: TrimSliderReducer = TrimSliderReducer(),
    eventsChannel: Channel<TrimSliderEvent>? = null,
    onEffect: (TrimSliderEffect) -> Unit = {},
    styles: TrimSliderStyles = TrimSliderStyles(),
    Background: @Composable () -> Unit = {},
) {
    // To set UDF up
    val events = eventsChannel ?: remember { Channel(Channel.BUFFERED) }
    var state by remember { mutableStateOf(initialState) }

    LaunchedEffect(Unit) {
        events.receiveAsFlow().collect { event ->
            state = reducer.reduce(state, event, onEffect)
        }
    }

    TrimRangeSlider(
        state = object : TrimRangeSliderState() {
            override val leftHandlePosition: Float
                get() = state.leftHandlePosition
            override val rightHandlePosition: Float
                get() = state.rightHandlePosition
            override val absoluteCursorPosition: Float
                get() = state.absoluteCursorPosition
            override val currentDragHandle: HandleType?
                get() = state.currentDragHandle
            override val minSlidersDistance: Float
                get() = state.minSlidersDistance
            override val maxSlidersDistance: Float
                get() = state.maxSlidersDistance
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(styles.containerHeight),
        styles = styles,
        backgroundContent = {
            SliderBackground(styles = styles, Background)
        },
        leftSliderContent = {
            SliderHandle(
                iconId = R.drawable.ic_left_handle,
                contentDescription = "Left handle",
                styles = styles
            )
        },
        rightSliderContent = {
            SliderHandle(
                iconId = R.drawable.ic_right_handle,
                contentDescription = "Right handle",
                styles = styles
            )
        },
        cursorContent = {
            SliderCursor(styles = styles)
        },
        selectedAreaContent = {
            SelectedArea(styles = styles)
        },
        unselectedAreaLeftContent = {
            UnselectedLeftArea(styles = styles)
        },
        unselectedAreaRightContent = {
            UnselectedRightArea(styles = styles)
        },
        onDragStart = { handleType ->
            events.trySend(TrimSliderEvent.OnDragStarted(handleType))
        },
        onDragEnd = { handleType ->
            events.trySend(TrimSliderEvent.OnDragEnded(handleType))
        },
        onHandleMoved = { type, position ->
            when (type) {
                HandleType.LEFT -> events.trySend(TrimSliderEvent.OnLeftHandleMoved(position))
                HandleType.RIGHT -> events.trySend(TrimSliderEvent.OnRightHandleMoved(position))
                HandleType.CURSOR -> events.trySend(TrimSliderEvent.OnCursorMoved(position))
            }
        },

        onHandleClicked = { type, position ->
            when (type) {
                HandleType.LEFT -> events.trySend(TrimSliderEvent.OnLeftHandleClicked(position))
                HandleType.RIGHT -> events.trySend(TrimSliderEvent.OnRightHandleClicked(position))
                HandleType.CURSOR -> Unit // No action needed
            }
        }
    )
}
