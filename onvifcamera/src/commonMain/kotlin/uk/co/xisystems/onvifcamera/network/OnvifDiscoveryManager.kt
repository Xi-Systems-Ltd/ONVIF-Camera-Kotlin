package uk.co.xisystems.onvifcamera.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import uk.co.xisystems.onvifcamera.DiscoveredOnvifDevice

public interface OnvifDiscoveryManager {
    public fun discoverDevices(
        retryCount: Int = 1,
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    ): Flow<List<DiscoveredOnvifDevice>>
}