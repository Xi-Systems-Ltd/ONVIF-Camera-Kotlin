package uk.co.xisystems.onvifcamera.network

import android.content.Context
import android.net.wifi.WifiManager
import uk.co.xisystems.onvifcamera.OnvifLogger

public fun OnvifDiscoveryManager(
    context: Context,
    logger: OnvifLogger? = null,
    netConfig: DiscoveryNetworkConfig = DiscoveryNetworkConfig()
): OnvifDiscoveryManager {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val socketListener = AndroidSocketListener(wifiManager, logger, netConfig)
    return OnvifDiscoveryManagerImpl(socketListener, logger)
}