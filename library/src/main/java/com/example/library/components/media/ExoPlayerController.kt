package com.example.library.components.media

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

data class ExoPlayerController(
    val exoPlayer: ExoPlayer
) {
    fun pause() {
        exoPlayer.pause()
    }

    fun play() {
        exoPlayer.play()
    }

    val isPlaying get() = exoPlayer.isPlaying ?: false
    val currentPosition get() = exoPlayer.currentPosition

    val isMuted: Boolean get() = exoPlayer.volume == 0f
    fun switchMute() {
        if (isMuted)
            exoPlayer.apply { volume = 1f }
        else
            exoPlayer.apply { volume = 0f }
    }

    fun seekToPosition(position: Float, duration: Long) {
        val seekPosition = (position * duration).toLong().coerceAtLeast(0L)
        exoPlayer.seekTo(seekPosition)
    }

    @OptIn(UnstableApi::class)
    fun preview(
        rightHandlePosition: Float,
        duration: Long,
        maxPreviewDurationMillis: Long
    ) {
        val cursorPositionMs = (rightHandlePosition * duration).toLong()
        val startPositionMs = (cursorPositionMs - maxPreviewDurationMillis).coerceAtLeast(0L)

        if (startPositionMs > 0) {
            exoPlayer.seekTo(startPositionMs)
        }
    }
}