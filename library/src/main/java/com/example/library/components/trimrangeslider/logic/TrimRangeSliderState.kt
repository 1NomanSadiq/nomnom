package com.example.library.components.trimrangeslider.logic

import com.example.library.components.trimrangeslider.ui.HandleType

abstract class TrimRangeSliderState {
    abstract val leftHandlePosition: Float
    abstract val rightHandlePosition: Float
    abstract val absoluteCursorPosition: Float
    abstract val currentDragHandle: HandleType?
    abstract val minSlidersDistance: Float
    abstract val maxSlidersDistance: Float

    val relativeCursorPosition: Float
        get() {
            val range = rightHandlePosition - leftHandlePosition
            return if (range > 0) {
                ((absoluteCursorPosition - leftHandlePosition) / range).coerceIn(0f, 1f)
            } else 0f
        }

    val boundedCursorPosition: Float
        get() = absoluteCursorPosition.coerceIn(leftHandlePosition, rightHandlePosition)

    val selectedAreaWidth: Float
        get() = rightHandlePosition - leftHandlePosition

    val leftUnselectedAreaWidth: Float
        get() = leftHandlePosition

    val rightUnselectedAreaWidth: Float
        get() = 1f - rightHandlePosition
}