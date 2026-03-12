package uk.co.xisystems.onvifcamera.network

import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import uk.co.xisystems.onvifcamera.OnvifCommands
import uk.co.xisystems.onvifcamera.OnvifLogger
import java.net.DatagramPacket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.MulticastSocket
import java.net.NetworkInterface
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.Enumeration
import java.util.UUID

internal abstract class BaseSocketListener(
    private val logger: OnvifLogger?,
    private val netConfig: DiscoveryNetworkConfig = DiscoveryNetworkConfig(),
) : SocketListener {

    private val multicastAddress: InetAddress by lazy {
        InetAddress.getByName(MULTICAST_ADDRESS)
    }

    private fun eligibleInterfaces(): List<NetworkInterface> {
        val all = NetworkInterface.getNetworkInterfaces()
        return all
            .asSequence()
            .filter { it.isUp && !it.isLoopback && it.supportsMulticast() }
            .filter { it.index > 0 } // Avoid unknown/invalid index that breaks IPV6_MULTICAST_IF
            .filter { ni ->
                // Only non-loopback IPv4 addresses.
                ni.inetAddresses.asSequence().any { it is Inet4Address && !it.isLoopbackAddress }
            }
            .filter { ni ->
                netConfig.interfaceNames?.let { ni.name in it } ?: true
            }
            .toList()
    }

    private fun setupSocket(ni: NetworkInterface, timeoutMillis: Int): MulticastSocket {
        val s = MulticastSocket(null).apply {
            reuseAddress = true
            broadcast = true
            @Suppress("DEPRECATION")
            loopbackMode = true
            soTimeout = timeoutMillis
            timeToLive = netConfig.ttl

                // Bind before join on many stacks (safer ordering).
                bind(InetSocketAddress(MULTICAST_PORT))

                // Force outbound interface
                networkInterface = ni

                // Join group on that interface
                joinGroup(InetSocketAddress(multicastAddress, MULTICAST_PORT), ni)
        }

        logger?.debug("MulticastSocket setup on interface ${ni.name}")
        return s
    }

    override fun listenForPackets(retryCount: Int, timeoutMillis: Int): Flow<DatagramPacket> {
        logger?.debug("Setting up datagram packet flow")

        return flow {
            acquireMulticastLock()

            val interfaces = eligibleInterfaces()
            if (interfaces.isEmpty()) {
                logger?.error("No multicast-capable interfaces found (or none matched filter)", null)
                return@flow
            }

            // Create one socket per interface
            val sockets = interfaces.mapNotNull { ni ->
                try {
                    setupSocket(ni, timeoutMillis)
                } catch (e: SocketException) {
                    logger?.debug("Skipping interface ${ni.name} due to ${e.message}")
                    null
                }
            }

            try {
                val messageId = UUID.randomUUID()
                val requestMessage = OnvifCommands.probeCommand(messageId.toString()).toByteArray()
                val requestDatagram = DatagramPacket(
                    requestMessage,
                    requestMessage.size,
                    multicastAddress,
                    MULTICAST_PORT
                )

                // Send probe from each interface socket
                repeat(1 + retryCount) {
                    sockets.forEach { s ->
                        if (!s.isClosed) s.send(requestDatagram)
                    }
                }

                // Receive on all sockets (simple approach: loop and poll each)
                // For higher throughput you can dedicate a coroutine per socket and merge flows.
                while (currentCoroutineContext().isActive) {
                    sockets.forEach { s ->
                        if (s.isClosed) return@forEach
                        try {
                            val buf = ByteArray(MULTICAST_DATAGRAM_SIZE)
                            val pkt = DatagramPacket(buf, buf.size)
                            s.receive(pkt)
                            emit(pkt)
                        } catch (_: SocketTimeoutException) {
                            // ignore per-socket timeout; overall loop continues until coroutine cancelled
                        }
                    }
                }
            } finally {
                teardownSockets(sockets)
                releaseMulticastLock()
            }
        }
            .catch { cause -> logger?.error("Error during discovery", cause) }
    }

    private fun teardownSockets(sockets: List<MulticastSocket>) {
        sockets.forEach { s ->
            try {
                if (!s.isClosed) {
                    // Leave group requires the same iface you joined on; if you want exact leaving,
                    // track iface→socket mapping. Closing without leave usually works too.
                    s.close()
                }
            } catch (_: Throwable) {}
        }
    }

    protected abstract fun acquireMulticastLock()

    protected abstract fun releaseMulticastLock()

    private companion object {
        const val MULTICAST_DATAGRAM_SIZE = 64 * 1024
        const val MULTICAST_PORT = 3702
        const val MULTICAST_ADDRESS = "239.255.255.250"
    }
}

private fun <T> Enumeration<T>?.asSequence(): Sequence<T> = sequence {
    if (this@asSequence != null){
        while (hasMoreElements()) {
            yield(nextElement())
        }
    }
}