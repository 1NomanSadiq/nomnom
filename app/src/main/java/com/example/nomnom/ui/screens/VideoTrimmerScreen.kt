package com.example.nomnom.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.library.components.trimrangeslider.ui.HandleType
import com.example.library.components.trimrangeslider.ui.TrimRangeSlider
import com.example.library.components.trimrangeslider.logic.TrimRangeSliderState
import com.example.library.nomnom.R
import java.util.Locale

class VideoTrimmerScreen : Screen {
    @Preview(showBackground = true)
    @Composable
    override fun Content() {
        val state = remember {
            object : TrimRangeSliderState() {
                override var leftHandlePosition: Float
                    get() = 0f
                    set(value) {}
                override var rightHandlePosition: Float
                    get() = 1f
                    set(value) {}
                override var absoluteCursorPosition: Float
                    get() = 0.25f
                    set(value) {}
                override val currentDragHandle: HandleType?
                    get() = null
                override val minSlidersDistance: Float
                    get() = 0.10f
                override val maxSlidersDistance: Float
                    get() = 1f

            }
        }

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = String.format(
                    Locale.US,
                    "0 .. %.2f .. %.2f .. %.2f .. 1",
                    state.leftHandlePosition,
                    state.absoluteCursorPosition,
                    state.rightHandlePosition
                )
            )

            //Dimens
            val containerHeight = 64.dp
            val containerHorizontalPadding = 16.dp
            val cornerRadius = 16.dp
            val handleWidth = 18.dp
            val handleHeight = 36.dp
            val handleCornerRadius = 4.dp
            val iconSize = 14.dp
            val cursorWidth = 2.dp
            val borderWidth = 2.dp

            // Calculations
            val thumbnailVerticalPadding = 4.dp // Cursor's length outside vertically
            val cursorOffset = -cursorWidth / 2
            val handleOffset = -handleWidth / 2
            val unselectedAreaVerticalPadding =
                thumbnailVerticalPadding + borderWidth // We do not want to show it on top of the border

            //Colors
            val unselectedAreaOverlayColor = Color(0x80000000)
            val handleBackgroundColor = Color.Black
            val handleIconTint = Color.White
            val cursorColor = Color.Red

            fun createHandle(
                iconId: Int,
                contentDescription: String
            ): @Composable BoxScope.() -> Unit = {
                Box(
                    modifier = Modifier
                        .width(handleWidth)
                        .height(handleHeight)
                        .offset(x = handleOffset) // Center the handle on position
                        .clip(RoundedCornerShape(handleCornerRadius))
                        .background(handleBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = contentDescription,
                        tint = handleIconTint,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            TrimRangeSlider(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = containerHorizontalPadding)
                    .height(containerHeight),
                backgroundContent = {
                    Image(
                        painter = painterResource(id = R.drawable.video_thumbnail_strip),
                        contentDescription = "Video Frames",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = thumbnailVerticalPadding)
                            .clip(RoundedCornerShape(cornerRadius))
                            .border(
                                borderWidth,
                                handleBackgroundColor,
                                RoundedCornerShape(cornerRadius)
                            ),
                        contentScale = ContentScale.FillBounds
                    )
                },
                leftSliderContent = createHandle(
                    iconId = R.drawable.ic_left_handle,
                    contentDescription = "Left handle"
                ),
                rightSliderContent = createHandle(
                    iconId = R.drawable.ic_right_handle,
                    contentDescription = "Right handle"
                ),
                cursorContent = {
                    Box(
                        modifier = Modifier
                            .width(cursorWidth)
                            .fillMaxHeight()
                            .offset(x = cursorOffset) // Center the cursor line
                            .background(cursorColor)
                    )
                },
                unselectedAreaLeftContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = borderWidth,
                                top = unselectedAreaVerticalPadding,
                                bottom = unselectedAreaVerticalPadding
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStart = cornerRadius,
                                    bottomStart = cornerRadius
                                )
                            )
                            .background(unselectedAreaOverlayColor)
                    )
                },
                unselectedAreaRightContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                end = borderWidth,
                                top = unselectedAreaVerticalPadding,
                                bottom = unselectedAreaVerticalPadding
                            )
                            .clip(
                                RoundedCornerShape(
                                    topEnd = cornerRadius,
                                    bottomEnd = cornerRadius
                                )
                            )
                            .background(unselectedAreaOverlayColor)
                    )
                },
                onDragStart = {
                    when (it) {
                        HandleType.LEFT -> Unit
                        HandleType.RIGHT -> Unit
                        HandleType.CURSOR -> {
                            // Pause the video
                        }
                    }
                },
                onDragEnd = {
                    when (it) {
                        HandleType.LEFT -> Unit
                        HandleType.RIGHT -> Unit
                        HandleType.CURSOR -> {
                            // Resume the video
                        }
                    }
                },
                onHandleMoved = { type, position ->
                    when (type) {
                        HandleType.LEFT -> {
                            state.leftHandlePosition = position
                            if (state.absoluteCursorPosition < position) {
                                state.absoluteCursorPosition = position
                            }
                        }

                        HandleType.RIGHT -> {
                            state.rightHandlePosition = position

                            if (state.absoluteCursorPosition > position) {
                                state.absoluteCursorPosition = position
                            }
                        }

                        HandleType.CURSOR -> {
                            state.absoluteCursorPosition = position
                        }
                    }
                }
            )
        }
    }
}