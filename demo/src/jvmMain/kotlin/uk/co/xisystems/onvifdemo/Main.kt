package uk.co.xisystems.onvifdemo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import uk.co.xisystems.onvifcamera.OnvifLogger
import uk.co.xisystems.onvifcamera.network.OnvifDiscoveryManager

fun main() {
    Napier.base(DebugAntilog())
    val logger = object : OnvifLogger {
        override fun error(message: String, e: Throwable?) {
            Napier.e(message, e)
        }

        override fun debug(message: String) {
            Napier.d(message)
        }
    }
    val onvifDiscoveryManager = OnvifDiscoveryManager(logger)
    val viewModel = MainViewModel(onvifDiscoveryManager, logger)
    application {
        Window(
            title = "ONVIF Camera Demo",
            onCloseRequest = ::exitApplication
        ) {
            MainContent(viewModel)
        }
    }
}