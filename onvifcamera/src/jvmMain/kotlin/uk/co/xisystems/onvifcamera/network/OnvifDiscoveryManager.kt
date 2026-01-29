package uk.co.xisystems.onvifcamera.network

import uk.co.xisystems.onvifcamera.OnvifLogger

public fun OnvifDiscoveryManager(logger: OnvifLogger? = null): OnvifDiscoveryManager {
    val socketListener = JvmSocketListener(logger)
    return OnvifDiscoveryManagerImpl(socketListener, logger)
}