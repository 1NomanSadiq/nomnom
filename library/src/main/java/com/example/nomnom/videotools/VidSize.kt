package com.example.nomnom.videotools

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonSkippableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

data class VidSize(
    val width: Int,
    val height: Int,
)

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(UnstableApi::class)
@Composable
@NonSkippableComposable
fun VideoPlayerComponent(
    url: String,
    setPlayer: (ExoPlayer) -> Unit,
    setDuration: (Long) -> Unit,
    setSize: (VidSize) -> Unit,
    onIsPlayingChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    if(LocalInspectionMode.current) Text("Видео", modifier = modifier).also { return }
    VideoPlayer(
        modifier = modifier,
        mediaItems = remember(url) { listOf(VideoPlayerMediaItem.NetworkMediaItem(url)) },
        repeatMode = remember { RepeatMode.ONE },
        autoPlay = true,
        usePlayerController = false,
        playerInstance = remember {
            {
                println("EVERY RECOMPOSITION")
                val exoplayer = this
                exoplayer.setSeekParameters(SeekParameters.EXACT)
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        onIsPlayingChanged(isPlaying)
                    }
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        setPlayer(exoplayer)

                        if (playbackState == Player.STATE_READY) {
                            setDuration(exoplayer.duration)
                        }
                        super.onPlaybackStateChanged(playbackState)
                    }

                    override fun onVideoSizeChanged(videoSize: VideoSize) {
                        setSize(VidSize(videoSize.width, videoSize.height))
                    }
                })
                // Player instance is returned here
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun VideoPlayerComponentPreview() {
    //VideoPlayerComponent(url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
}