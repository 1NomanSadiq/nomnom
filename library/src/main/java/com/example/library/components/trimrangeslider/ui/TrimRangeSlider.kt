package com.example.library.components.trimrangeslider.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.example.library.components.trimrangeslider.logic.TrimRangeSliderLogic
import com.example.library.components.trimrangeslider.logic.TrimRangeSliderState
import com.example.library.components.trimrangeslider.components.TrimSliderStyles
import testlib.compose.px
import testlib.compose.pxToDp

@Preview
@Composable
fun TrimRangeSlider(
    state: TrimRangeSliderState = object : TrimRangeSliderState() {
        override val leftHandlePosition: Float
            get() = 0f
        override val rightHandlePosition: Float
            get() = 1f
        override val absoluteCursorPosition: Float
            get() = 0.25f
        override val currentDragHandle: HandleType?
            get() = null
        override val minSlidersDistance: Float
            get() = 0.10f
        override val maxSlidersDistance: Float
            get() = 1f
    },
    modifier: Modifier = Modifier,
    styles: TrimSliderStyles = TrimSliderStyles(),
    leftSliderContent: @Composable BoxScope.() -> Unit = {},
    rightSliderContent: @Composable BoxScope.() -> Unit = {},
    cursorContent: @Composable BoxScope.() -> Unit = {},
    unselectedAreaLeftContent: @Composable BoxScope.() -> Unit = {},
    selectedAreaContent: @Composable BoxScope.() -> Unit = {},
    unselectedAreaRightContent: @Composable BoxScope.() -> Unit = {},
    backgroundContent: @Composable BoxScope.() -> Unit = {},
    onHandleMoved: (type: HandleType, position: Float) -> Unit = { _, _ -> },
    onHandleClicked: (type: HandleType, position: Float) -> Unit = { _, _ -> },
    onDragStart: (type: HandleType) -> Unit = { _ -> },
    onDragEnd: (type: HandleType) -> Unit = { _ -> }
) {
    var sliderWidthPx by remember { mutableFloatStateOf(0f) }
    var backgroundWidthPx by remember { mutableFloatStateOf(0f) }
    var handleWidthPx by remember { mutableFloatStateOf(0f) }
    val logic = TrimRangeSliderLogic(state, onHandleMoved, onHandleClicked)


    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                sliderWidthPx = coordinates.size.width.toFloat()
            }
    ) {
        // Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    backgroundWidthPx = coordinates.size.width.toFloat()
                },
            content = backgroundContent
        )

        // Areas container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = styles.overreachPadding)
                .clip(RoundedCornerShape(styles.cornerRadius))
        ) {
            // Left unselected area
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(logic.getLeftUnSelectedAreaWidth(backgroundWidthPx, handleWidthPx)),
                content = unselectedAreaLeftContent
            )

            // Selected area
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(logic.getSelectedAreaWidth(backgroundWidthPx))
                    .offset { logic.getSelectedAreaOffset(backgroundWidthPx) },
                content = selectedAreaContent
            )

            // Transparent overlay for gestures
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width((logic.getSelectedAreaWidth(backgroundWidthPx).px - (handleWidthPx * 2)).pxToDp())
                    .offset {
                        IntOffset(
                            (logic.getSelectedAreaOffset(backgroundWidthPx).x + handleWidthPx).toInt(),
                            logic.getSelectedAreaOffset(backgroundWidthPx).y
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            logic.handleSelectedAreaTap(
                                offset.x + handleWidthPx,
                                backgroundWidthPx
                            )
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                onDragStart(HandleType.CURSOR)
                                logic.handleSelectedAreaDragStart(
                                    offset.x + handleWidthPx,
                                    backgroundWidthPx
                                )
                            },
                            onDragEnd = {
                                onDragEnd(HandleType.CURSOR)
                            },
                            onDrag = { _, dragAmount ->
                                logic.handleCursorDrag(dragAmount.x, backgroundWidthPx)
                            }
                        )
                    }
            )

            // Right unselected area
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(logic.getRightUnselectedAreaWidth(backgroundWidthPx, handleWidthPx))
                    .offset { logic.getRightAreaOffset(backgroundWidthPx, handleWidthPx) },
                content = unselectedAreaRightContent
            )
        }

        val offset = logic.getCursorOffset(backgroundWidthPx)
        // Cursor
        Box(
            modifier = Modifier
                .offset {
                    offset
                }
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { onDragStart(HandleType.CURSOR) },
                        onDragEnd = { onDragEnd(HandleType.CURSOR) },
                        onDrag = { _, dragAmount ->
                            logic.handleCursorDrag(dragAmount.x, backgroundWidthPx)
                        }
                    )
                },
            content = cursorContent
        )

        // Left handle
        Box(
            modifier = Modifier
                .offset { logic.getLeftHandleOffset(backgroundWidthPx) }
                .align(Alignment.CenterStart)
                .onGloballyPositioned { coordinates ->
                    handleWidthPx = coordinates.size.width.toFloat()
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        logic.handleHandleTap(HandleType.LEFT)
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            logic.handleHandleTap(HandleType.LEFT)
                            onDragStart(HandleType.LEFT)
                        },
                        onDragEnd = { onDragEnd(HandleType.LEFT) },
                        onDrag = { _, dragAmount ->
                            logic.handleLeftHandleDrag(dragAmount.x, backgroundWidthPx)
                        }
                    )
                },
            content = leftSliderContent
        )

        // Right handle
        Box(
            modifier = Modifier
                .offset { logic.getRightHandleOffset(backgroundWidthPx) }
                .align(Alignment.CenterStart)
                .pointerInput(Unit) {
                    detectTapGestures {
                        logic.handleHandleTap(HandleType.RIGHT)
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            logic.handleHandleTap(HandleType.RIGHT)
                            onDragStart(HandleType.RIGHT)
                        },
                        onDragEnd = { onDragEnd(HandleType.RIGHT) },
                        onDrag = { _, dragAmount ->
                            logic.handleRightHandleDrag(dragAmount.x, backgroundWidthPx)
                        }
                    )
                },
            content = rightSliderContent
        )
    }
}