package com.example.nomnom.ui.screens.croppingscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.library.videocrop.VideoTrimCropFeature
import com.example.nomnom.videotools.copyUriContentToTempInternalFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import testlib.compose.media.galerypicker.pickVisualVideo
import testlib.ui.FeatureButton


class _3_CroppingScreen : Screen {
    @Preview
    @Composable
    override fun Content() {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val (contentUrl, launcher) = pickVisualVideo()
            FeatureButton("Step 1: choosing video") { launcher() }
            Text("contentUrl: ${contentUrl.value}")
            val localPath by produceState("") {
                snapshotFlow { contentUrl.value }
                    .filterNotNull()
                    .map { copyUriContentToTempInternalFile(context, it) }
                    .filterNotNull()
                    .flowOn(Dispatchers.IO)
                    .onEach { value = it }
                    .launchIn(this)
            }
            Text("localPath: $localPath")

            FeatureButton("Step 2: cropping video") {}

            contentUrl.value?.let { url ->
                VideoTrimCropFeature(url) { videoView, trimView ->
                    videoView()
                    trimView()
                }
            }
        }
    }
}
