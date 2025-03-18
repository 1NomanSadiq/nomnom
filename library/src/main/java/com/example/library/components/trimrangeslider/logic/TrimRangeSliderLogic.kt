package com.example.library.components.trimrangeslider.logic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.example.library.components.trimrangeslider.Constants.TRIM_SLIDER_UPDATE_INTERVAL_MILLIS
import com.example.library.components.trimrangeslider.ui.HandleType
import testlib.compose.pxToDp
import kotlin.math.roundToInt

class TrimRangeSliderLogic(
    private val state: TrimRangeSliderState,
    private val onHandleMoved: (type: HandleType, position: Float) -> Unit,
    private val onHandleClicked: (type: HandleType, position: Float) -> Unit
) {
    fun handleCursorDrag(dragAmount: Float, backgroundWidth: Float) {
        if (backgroundWidth <= 0f) return

        val dragAmountRelative = dragAmount / backgroundWidth
        val newAbsolutePosition = (state.boundedCursorPosition + dragAmountRelative)
            .coerceIn(state.leftHandlePosition, state.rightHandlePosition)

        if (newAbsolutePosition != state.boundedCursorPosition) {
            onHandleMoved(HandleType.CURSOR, newAbsolutePosition)
        }
    }

    fun handleLeftHandleDrag(dragAmount: Float, backgroundWidth: Float) {
        if (backgroundWidth <= 0f) return

        val dragAmountRelative = dragAmount / backgroundWidth
        val newPosition = state.leftHandlePosition + dragAmountRelative

        val maxAllowedPosition = calculateMaxLeftHandlePosition()
        val boundedPosition = newPosition.coerceIn(0f, maxAllowedPosition)

        onHandleMoved(HandleType.LEFT, boundedPosition)
        onHandleMoved(HandleType.CURSOR, boundedPosition)
    }

    fun handleRightHandleDrag(dragAmount: Float, backgroundWidth: Float) {
        if (backgroundWidth <= 0f) return

        val dragAmountRelative = dragAmount / backgroundWidth
        val newPosition = state.rightHandlePosition + dragAmountRelative

        val minAllowedPosition = calculateMinRightHandlePosition()
        val boundedPosition = newPosition.coerceIn(minAllowedPosition, 1f)

        if (boundedPosition != state.rightHandlePosition) {
            onHandleMoved(HandleType.CURSOR, boundedPosition)
            onHandleMoved(HandleType.RIGHT, boundedPosition)
        }
    }

    private fun calculateMaxLeftHandlePosition(): Float {
        val maxPositionByMaxDistance = state.leftHandlePosition + state.maxSlidersDistance
        val effectiveRightPosition = if (maxPositionByMaxDistance < state.rightHandlePosition) {
            maxPositionByMaxDistance
        } else {
            state.rightHandlePosition
        }

        return (effectiveRightPosition - state.minSlidersDistance).coerceAtLeast(0f)
    }

    private fun calculateMinRightHandlePosition(): Float {
        val minLeftPositionByMaxDistance = state.rightHandlePosition - state.maxSlidersDistance
        val effectiveLeftPosition = if (minLeftPositionByMaxDistance > state.leftHandlePosition) {
            minLeftPositionByMaxDistance
        } else {
            state.leftHandlePosition
        }

        return (effectiveLeftPosition + state.minSlidersDistance).coerceAtMost(1f)
    }

    @Composable
    fun getLeftUnSelectedAreaWidth(backgroundWidth: Float, handleWidth: Float): Dp {
        return (state.leftUnselectedAreaWidth * backgroundWidth + handleWidth).pxToDp()
    }

    @Composable
    fun getSelectedAreaWidth(backgroundWidth: Float): Dp {
        return (state.selectedAreaWidth * backgroundWidth).pxToDp()
    }

    @Composable
    fun getRightUnselectedAreaWidth(backgroundWidth: Float, handleWidth: Float): Dp {
        return ((state.rightUnselectedAreaWidth * backgroundWidth) + handleWidth).pxToDp()
    }

    fun getLeftHandleOffset(backgroundWidth: Float): IntOffset {
        return IntOffset((state.leftHandlePosition * backgroundWidth).roundToInt(), 0)
    }

    fun getRightHandleOffset(backgroundWidth: Float): IntOffset {
        return IntOffset((state.rightHandlePosition * backgroundWidth).roundToInt(), 0)
    }

    fun getSelectedAreaOffset(backgroundWidth: Float): IntOffset {
        return IntOffset((state.leftHandlePosition * backgroundWidth).roundToInt(), 0)
    }

    fun getRightAreaOffset(backgroundWidth: Float, handleWidth: Float): IntOffset {
        return IntOffset((state.rightHandlePosition * backgroundWidth).roundToInt(), 0) - IntOffset(
            handleWidth.toInt(),
            0
        )
    }

    @Composable
    fun getCursorOffset(backgroundWidth: Float): IntOffset {
        val durationMillis =
            if (state.currentDragHandle == null) TRIM_SLIDER_UPDATE_INTERVAL_MILLIS else 0
        val targetOffset =
            IntOffset((state.boundedCursorPosition * backgroundWidth).roundToInt(), 0)
        val animatedOffset = animateIntOffsetAsState(
            targetValue = targetOffset,
            animationSpec = tween(
                durationMillis = durationMillis.toInt(),
                easing = LinearEasing
            ),
            label = "cursor"
        )
        return animatedOffset.value
    }

    fun handleSelectedAreaDragStart(offsetX: Float, backgroundWidth: Float) {
        if (backgroundWidth <= 0f) return
        val currentCursorPosInPx = state.boundedCursorPosition * backgroundWidth
        val targetPosInPx = offsetX + (state.leftHandlePosition * backgroundWidth)
        val dragAmountInPx = targetPosInPx - currentCursorPosInPx
        handleCursorDrag(dragAmountInPx, backgroundWidth)
    }

    fun handleSelectedAreaTap(offsetX: Float, backgroundWidth: Float) {
        if (backgroundWidth <= 0f) return
        val absolutePosition =
            (offsetX + (state.leftHandlePosition * backgroundWidth)) / backgroundWidth
        val boundedPosition =
            absolutePosition.coerceIn(state.leftHandlePosition, state.rightHandlePosition)
        if (boundedPosition != state.boundedCursorPosition) {
            onHandleMoved(HandleType.CURSOR, boundedPosition)
        }
    }

    fun handleHandleTap(handleType: HandleType) {
        when (handleType) {
            HandleType.LEFT -> {
                if (state.boundedCursorPosition != state.leftHandlePosition) {
                    onHandleClicked(HandleType.LEFT, state.leftHandlePosition)
                }
            }

            HandleType.RIGHT -> {
                if (state.boundedCursorPosition != state.rightHandlePosition) {
                    onHandleClicked(HandleType.RIGHT, state.rightHandlePosition)
                }
            }

            HandleType.CURSOR -> Unit /* Nothing for cursor tap */
        }
    }
}