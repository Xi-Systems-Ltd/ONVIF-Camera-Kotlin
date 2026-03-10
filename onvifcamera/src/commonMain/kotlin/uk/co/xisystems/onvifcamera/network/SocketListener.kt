package uk.co.xisystems.onvifcamera.network

import kotlinx.coroutines.flow.Flow
import java.net.DatagramPacket

internal interface SocketListener {
    /**
     * Sends a multicast probe and starts listening for packets that come back from the multicast
     * group. Emits each response via a [DatagramPacket] in a Kotlin flow
     *
     * @param retryCount The number of times to send to the probe
     * @param timeoutMillis The time in milliseconds to listen for responses
     * 
     * @return Flow of [DatagramPacket] - each emission represents a single received packet
     */
    fun listenForPackets(retryCount: Int, timeoutMillis: Int = 5000): Flow<DatagramPacket>
}
