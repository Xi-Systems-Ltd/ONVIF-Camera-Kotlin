package uk.co.xisystems.onvifcamera.network

/**
 * Network config for ONVIF discovery.
 *
 * @param interfaceNames Names of interfaces to use for multicast discovery messages. Will use all
 * multicast-capable interfaces if null.
 * @param ttl Time-to-live of multicast discovery messages.
 */
public data class DiscoveryNetworkConfig(
    val interfaceNames: Set<String>? = null, // null = all multicast-capable
    val ttl: Int = 1,                        // WS-Discovery is link-local; 1 is typical
)