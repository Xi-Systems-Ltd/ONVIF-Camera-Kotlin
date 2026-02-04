package com.seanproctor.onvifdemo

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter

@Composable
actual fun StreamPlayer(url: String, modifier: Modifier) {
    var currentFrame by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            val grabber = FFmpegFrameGrabber(url).apply {
                start()
            }
            val converter = Java2DFrameConverter()

            try {
                while (true) {
                    currentCoroutineContext().ensureActive()
                    val frame = grabber.grab() ?: continue
                    val bufferedImage = converter.convert(frame)
                    if (bufferedImage != null) {
                        currentFrame = bufferedImage.toComposeImageBitmap()
                    }
                }
            } finally {
                grabber.stop()
                grabber.release()
            }
        }
    }

    currentFrame?.let {
        Image(bitmap = it, contentDescription = null, modifier = modifier)
    }
}
