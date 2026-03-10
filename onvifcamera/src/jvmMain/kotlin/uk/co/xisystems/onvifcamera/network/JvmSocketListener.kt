package uk.co.xisystems.onvifcamera.network

import uk.co.xisystems.onvifcamera.OnvifLogger

/** Specific implementation of [SocketListener] */
internal class JvmSocketListener(
    logger: OnvifLogger?,
    netConfig: DiscoveryNetworkConfig = DiscoveryNetworkConfig()
) : BaseSocketListener(logger, netConfig) {
    override fun acquireMulticastLock() {
        // Nothing to do on JVM
    }

    override fun releaseMulticastLock() {
        // Nothing to do on JVM
    }
}
