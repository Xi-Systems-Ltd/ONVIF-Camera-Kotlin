package uk.co.xisystems.onvifcamera.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.co.xisystems.onvifcamera.DiscoveredOnvifDevice
import uk.co.xisystems.onvifcamera.OnvifLogger
import uk.co.xisystems.onvifcamera.parseOnvifProbeResponse
import java.net.DatagramPacket
import java.net.InetAddress

internal class OnvifDiscoveryManagerImpl(
    private val socketListener: SocketListener,
    private val logger: OnvifLogger?,
): OnvifDiscoveryManager {
    override fun discoverDevices(
        retryCount: Int,
        timeoutMillis: Int,
        scope: CoroutineScope
    ): Flow<List<DiscoveredOnvifDevice>> = channelFlow {
        require(retryCount >= 0) { "Retry count cannot be negative" }

        val discoveredDevices = mutableMapOf<InetAddress, DiscoveredOnvifDevice>()

        val job = scope.launch {
            socketListener.listenForPackets(retryCount, timeoutMillis)
                .catch { cause ->
                    logger?.error("Error listening for devices", cause)
                }
                .collect { packet: DatagramPacket ->
                    launch {
                        val data = packet.data.decodeToString(
                            startIndex = packet.offset,
                            endIndex = packet.offset + packet.length,
                        )
                        try {
                            val result = parseOnvifProbeResponse(data)
                            if (result.size == 1) {
                                val probeMatch = result.first()
                                val device = DiscoveredOnvifDevice(
                                    id = probeMatch.endpointReference.address,
                                    types = probeMatch.types?.split(" ") ?: emptyList(),
                                    scopes = probeMatch.scopes?.split(" ") ?: emptyList(),
                                    addresses = probeMatch.xaddrs?.split(" ")
                                        ?: emptyList(),
                                )
                                discoveredDevices[packet.address] = device
                                send(discoveredDevices.values.toList())
                            }
                        } catch (e: Throwable) {
                            logger?.error("Error parsing probe response: $data", e)
                        }
                    }
                }
        }

        job.invokeOnCompletion { cause ->
            close(cause)
        }

        // Ensure cancellation from downstream closes the job
        awaitClose {
            job.cancel()
        }
    }
}