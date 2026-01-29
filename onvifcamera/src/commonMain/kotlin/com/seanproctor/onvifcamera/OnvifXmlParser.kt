package com.seanproctor.onvifcamera

import com.seanproctor.onvifcamera.soap.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import nl.adaptivity.xmlutil.serialization.XML
import kotlin.String

private inline fun <reified T : Any> parseSoap(input: String): T {
    val module = SerializersModule {
        polymorphic(Any::class) {
            subclass(T::class, serializer())
        }
    }

    val xml = XML(module) {
        // xmlDeclMode = XmlDeclMode.Minimal
        autoPolymorphic = true
        defaultPolicy {
            pedantic = false
            ignoreUnknownChildren()
        }
    }
    val serializer = serializer<Envelope<T>>()

    return xml.decodeFromString(serializer, input).data
}

internal fun parseOnvifProfiles(input: String): List<MediaProfile> {
    val result = parseSoap<GetProfilesResponse>(input)

    return result.profiles.map {
        MediaProfile(
            name = it.name,
            token = it.token,
            encoding = it.encoder?.encoding,
            configuration = it.ptzConfiguration?.toConfiguration(),
            ptzFilter = it.metadataConfiguration?.ptzStatus?.toPtzFlags()
        )
    }
}

private fun PtzFilter.toPtzFlags(): PtzFlags {
    return PtzFlags(
        statusEnabled = status,
        positionEnabled = position,
        fieldOfViewEnabled = fieldOfView
    )
}

internal fun parseOnvifStreamUri(input: String): String {
    val result = parseSoap<GetStreamUriResponse>(input)
    return result.uri
}

internal fun parseOnvifSnapshotUri(input: String): String {
    val result = parseSoap<GetSnapshotUriResponse>(input)
    return result.uri
}

internal fun parseOnvifServices(input: String): List<OnvifService> {
    return parseSoap<GetServicesResponse>(input).services
}

internal fun parseOnvifGetHostnameResponse(input: String): String? {
    return parseSoap<GetHostnameResponse>(input).hostnameInformation.name
}

internal fun parseOnvifProbeResponse(input: String): List<ProbeMatch> {
    return parseSoap<ProbeMatches>(input).matches
}

internal fun parseOnvifDeviceInformation(input: String): OnvifDeviceInformation {
    val result = parseSoap<GetDeviceInformationResponse>(input)
    return OnvifDeviceInformation(
        manufacturer = result.manufacturer,
        model = result.model,
        firmwareVersion = result.firmwareVersion,
        serialNumber = result.serialNumber,
        hardwareId = result.hardwareId,
    )
}

internal fun parseOnvifConfigurations(input: String): List<Configuration> {
    val result = parseSoap<GetConfigurationsResponse>(input)
    return result.ptzConfiguration.map(PtzConfiguration::toConfiguration)
}

internal fun parseOnvifStatus(input: String): Status {
    val result = parseSoap<GetStatusResponse>(input)

    return Status(
        ptz = PanTiltZoom(
            pan = result.ptzStatus.position.panTilt.x,
            tilt = result.ptzStatus.position.panTilt.y,
            zoom = result.ptzStatus.position.zoom.x
        )
    )
}

internal fun PtzConfiguration.toConfiguration(): Configuration =
    Configuration(
        token = nodeToken,
        panMin = panTiltLimits.range.xRange.min,
        panMax = panTiltLimits.range.xRange.max,
        tiltMin = panTiltLimits.range.yRange.min,
        tiltMax = panTiltLimits.range.yRange.max,
        zoomMin = zoomLimits.range.xRange.min,
        zoomMax = zoomLimits.range.xRange.min
    )