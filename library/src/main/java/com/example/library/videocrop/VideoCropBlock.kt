package com.example.library.videocrop

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.library.components.media.ExoPlayerController
import com.example.library.utils.KeepScreenOn
import com.example.library.components.trimrangeslider.Constants
import com.example.library.components.trimrangeslider.Constants.TRIM_SLIDER_UPDATE_INTERVAL_MILLIS
import com.example.library.components.trimrangeslider.events.TrimSliderEffect
import com.example.library.components.trimrangeslider.events.TrimSliderEvent
import com.example.library.components.trimrangeslider.logic.TrimSliderReducer
import com.example.library.components.trimrangeslider.model.TrimSliderState
import com.example.library.components.trimrangeslider.TrimSlider
// import com.example.myapplication.ui.video_cropping.ContentCropper
import com.example.myapplication.ui.video_cropping.CroppingData
import com.example.nomnom.videotools.VidSize
import com.example.nomnom.videotools.VideoPlayerComponent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay

@Composable
fun VideoCropView(
    localPath: Uri,
    onCroppingDataChange: (CroppingData) -> Unit,
    setExoPlayer: (ExoPlayer) -> Unit,
    onIsPlayingChanged: (Boolean) -> Unit,
    setDuration: (Long) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        val maxHeight = 600.dp
        val defaultSize = VidSize(300, 200)
        val (videoSize, setVideoSize) = remember { mutableStateOf(defaultSize) }

        val aspectRatio = if (videoSize.width > 0 && videoSize.height > 0) {
            videoSize.width.toFloat() / videoSize.height.toFloat()
        } else {
            defaultSize.width.toFloat() / defaultSize.height.toFloat()
        }

        val calculatedHeight = (LocalConfiguration.current.screenWidthDp.dp / aspectRatio)
            .coerceAtMost(maxHeight)

        val videoResolution = videoSize.width to videoSize.height
        ContentCropper(
            videoResolution,
            onCroppingDataChange = onCroppingDataChange
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                VideoPlayerComponent(
                    url = localPath.toString(),
                    setPlayer = setExoPlayer,
                    setDuration = setDuration,
                    setSize = setVideoSize,
                    onIsPlayingChanged = onIsPlayingChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(calculatedHeight)
                )
            }
        }
    }
}

@Composable
fun ContentCropper(
    videoResolution: Pair<Int, Int>,
    onCroppingDataChange: (CroppingData) -> Unit,
    content: @Composable () -> Unit
) {
    content()
}


@Composable
fun VideoTrimmerView(
    exoPlayerController: ExoPlayerController,
    duration: Long,
    onTrimSliderEventsChannel: (Channel<TrimSliderEvent>) -> Unit = {},
    thumbnailView: @Composable () -> Unit,
) {
    // Text("Detected video size: $videoSize")

    var position by remember { mutableFloatStateOf(0f) }
    Text("position: $position / $duration")

    key(duration) {
        val events = remember { Channel<TrimSliderEvent>(Channel.BUFFERED) }
        LaunchedEffect(events) {
            onTrimSliderEventsChannel(events)
        }
        TrimSlider(
            initialState = TrimSliderState(),
            reducer = TrimSliderReducer(),
            eventsChannel = events,
            onEffect = { effect ->
                when (effect) {
                    is TrimSliderEffect.PauseVideo -> {
                        exoPlayerController.pause()
                    }

                    is TrimSliderEffect.ResumeVideo -> {
                        exoPlayerController.play()
                    }

                    is TrimSliderEffect.UpdatePosition -> {
                        position = effect.cursorPosition * duration
                        if(effect.shouldSeek) {
                            exoPlayerController.seekToPosition(effect.cursorPosition, duration)
                        }
                        if (effect.cursorPosition >= effect.rightHandlePosition)
                            exoPlayerController.seekToPosition(effect.leftHandlePosition, duration)
                    }

                    is TrimSliderEffect.PreviewVideo -> {
                        exoPlayerController.preview(
                            rightHandlePosition = effect.rightHandlePosition,
                            duration = duration,
                            maxPreviewDurationMillis = Constants.PREVIEW_VIDEO_DURATION_MILLIS
                        )
                    }
                }
            },
            Background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                    }
                    thumbnailView()
                }
            }
        )
    }
}


@Composable
fun VideoTrimCropFeature(
    localPath: Uri,
    onCroppingDataChange: (CroppingData) -> Unit = {},
    thumbnailView: @Composable () -> Unit = {},
    // exoPlayer: ExoPlayer,
    setExoPlayerController: (ExoPlayerController) -> Unit = {},
    content: @Composable (
        @Composable () -> Unit,
        @Composable (  ) -> Unit,
    ) -> Unit = { videoView, trimView -> },
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val (exoPlayer, setExoPlayer) = remember { mutableStateOf<ExoPlayer?>(null) }
        var exoPlayerController by remember { mutableStateOf<ExoPlayerController?>(null) }
        var duration by remember { mutableLongStateOf(0) }
        var isPlaying by remember { mutableStateOf(false) }
        KeepScreenOn(isPlaying)

        val lifecycle = LocalLifecycleOwner.current.lifecycle
        DisposableEffect(lifecycle, exoPlayer) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        exoPlayer?.let { player ->
                            val position = player.currentPosition

                            val mediaItem = MediaItem.fromUri(localPath)
                            player.setMediaItem(mediaItem)
                            player.prepare()

                            if (position > 0) {
                                player.seekTo(position)
                            }
                            player.play()
                        }
                    }
                    else -> Unit
                }
            }

            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }

        LaunchedEffect(exoPlayer) {
            exoPlayer?.let {
                val controller = ExoPlayerController(it)
                exoPlayerController = controller
                setExoPlayerController(controller)
            }
        }

        val trimSliderEventsChannel = remember { mutableStateOf<Channel<TrimSliderEvent>?>(null) }
        LaunchedEffect(isPlaying) {
            while (isPlaying) {
                val currentTime = exoPlayerController?.currentPosition?: 0
                trimSliderEventsChannel.value?.let { channel ->
                    val position = (currentTime.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
                    channel.send(TrimSliderEvent.SetCursorPosition(position))
                }
                delay(TRIM_SLIDER_UPDATE_INTERVAL_MILLIS)
            }
        }

        content(
            {
                VideoCropView(
                    localPath = localPath,
                    onCroppingDataChange = onCroppingDataChange,
                    setExoPlayer = setExoPlayer,
                    onIsPlayingChanged = { isPlaying = it },
                    setDuration = { duration = it }
                )
            },
            {
                VideoTrimmerView(
                    exoPlayerController = exoPlayerController ?: return@content,
                    duration = duration,
                    onTrimSliderEventsChannel = { channel ->
                        trimSliderEventsChannel.value = channel
                    }
                ) {
                    thumbnailView()
                }
            }
        )
    }
}