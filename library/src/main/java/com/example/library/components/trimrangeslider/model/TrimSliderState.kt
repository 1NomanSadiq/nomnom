package com.example.library.components.trimrangeslider.model

import com.example.library.components.trimrangeslider.logic.TrimRangeSliderState
import com.example.library.components.trimrangeslider.ui.HandleType

data class TrimSliderState(
    override val leftHandlePosition: Float = 0f,
    override val rightHandlePosition: Float = 1f,
    override val absoluteCursorPosition: Float = 0f,
    override val currentDragHandle: HandleType? = null,
    override val minSlidersDistance: Float = 0.1f,
    override val maxSlidersDistance: Float = 1f,
) : TrimRangeSliderState()