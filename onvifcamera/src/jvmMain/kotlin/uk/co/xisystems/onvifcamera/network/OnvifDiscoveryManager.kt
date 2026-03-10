package uk.co.xisystems.onvifcamera.network

import uk.co.xisystems.onvifcamera.OnvifLogger

public fun OnvifDiscoveryManager(
    logger: OnvifLogger? = null,
    netConfig: DiscoveryNetworkConfig = DiscoveryNetworkConfig()
): OnvifDiscoveryManager {
    val socketListener = JvmSocketListener(logger, netConfig)
    return OnvifDiscoveryManagerImpl(socketListener, logger)
}