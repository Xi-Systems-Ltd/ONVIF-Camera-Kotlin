package uk.co.xisystems.onvifcamera

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.io.IOException
import uk.co.xisystems.onvifcamera.OnvifCommands.absoluteMoveCommand
import uk.co.xisystems.onvifcamera.OnvifCommands.continuousMoveCommand
import uk.co.xisystems.onvifcamera.OnvifCommands.deviceInformationCommand
import uk.co.xisystems.onvifcamera.OnvifCommands.getConfigurationsCommand
import uk.co.xisystems.onvifcamera.OnvifCommands.getSnapshotURICommand
import uk.co.xisystems.onvifcamera.OnvifCommands.getStatusCommand
import uk.co.xisystems.onvifcamera.OnvifCommands.getStreamURICommand
import uk.co.xisystems.onvifcamera.OnvifCommands.profilesCommand
import uk.co.xisystems.onvifcamera.OnvifCommands.relativeMoveCommand
import uk.co.xisystems.onvifcamera.OnvifCommands.servicesCommand
import uk.co.xisystems.onvifcamera.OnvifCommands.stopCommand

/**
 * @author Remy Virin on 04/03/2018.
 * This class represents an ONVIF device and contains the methods to interact with it
 * (getDeviceInformation, getProfiles and getStreamURI).
 * @param username the username to login on the camera
 * @param password the password to login on the camera
 * @param namespaceMap a mapping of SOAP services to paths
 */
public class OnvifDevice internal constructor(
    private val address: Url,
    private val username: String?,
    private val password: String?,
    private val namespaceMap: Map<String, String>,
    private val logger: OnvifLogger?,
) {
    public suspend fun getDeviceInformation(): OnvifDeviceInformation {
        val endpoint = getEndpointForRequest(OnvifRequestType.GetDeviceInformation)
        val response = execute(endpoint, deviceInformationCommand, username, password, logger)
        return parseOnvifDeviceInformation(response)
    }

    public suspend fun getProfiles(): List<MediaProfile> {
        val endpoint = getEndpointForRequest(OnvifRequestType.GetProfiles)
        val response = execute(endpoint, profilesCommand, username, password, logger)
        return parseOnvifProfiles(response)
    }

    public suspend fun getStreamURI(profile: MediaProfile): String {
        val endpoint = getEndpointForRequest(OnvifRequestType.GetStreamURI)
        val response = execute(endpoint, getStreamURICommand(profile), username, password, logger)
        return fixHost(parseOnvifStreamUri(response))
    }

    public suspend fun getSnapshotURI(profile: MediaProfile): String {
        val endpoint = getEndpointForRequest(OnvifRequestType.GetSnapshotURI)
        val response = execute(endpoint, getSnapshotURICommand(profile), username, password, logger)
        return fixHost(parseOnvifSnapshotUri(response))
    }

    public suspend fun getConfigurations(): List<Configuration> {
        val endpoint = getEndpointForRequest(OnvifRequestType.GetConfigurations)
        val response = execute(endpoint, getConfigurationsCommand, username, password, logger)
        return parseOnvifConfigurations(response)
    }

    public suspend fun absoluteMove(
        profile: MediaProfile,
        position: PanTiltZoom,
        speed: PanTiltZoom? = null
    ) {
        val endpoint = getEndpointForRequest(OnvifRequestType.AbsoluteMove)
        execute(endpoint, absoluteMoveCommand(profile, position, speed), username, password, logger)
    }

    public suspend fun relativeMove(
        profile: MediaProfile,
        translation: PanTiltZoom,
        speed: PanTiltZoom? = null
    ) {
        val endpoint = getEndpointForRequest(OnvifRequestType.RelativeMove)
        execute(endpoint, relativeMoveCommand(profile, translation, speed), username, password, logger)
    }

    public suspend fun continuousMove(
        profile: MediaProfile,
        speed: PanTiltZoom
    ) {
        val endpoint = getEndpointForRequest(OnvifRequestType.ContinuousMove)
        execute(endpoint, continuousMoveCommand(profile, speed), username, password, logger)
    }

    public suspend fun stop(
        profile: MediaProfile,
        panTilt: Boolean? = null,
        zoom: Boolean? = null
    ) {
        val endpoint = getEndpointForRequest(OnvifRequestType.Stop)
        execute(endpoint, stopCommand(profile, panTilt, zoom), username, password, logger)
    }

    public suspend fun getStatus(profile: MediaProfile): Status {
        val endpoint = getEndpointForRequest(OnvifRequestType.GetStatus)
        val response = execute(endpoint, getStatusCommand(profile), username, password, logger)
        return parseOnvifStatus(response)
    }

    private fun getEndpointForRequest(requestType: OnvifRequestType): String {
        val path = namespaceMap[requestType.namespace()] ?: throw OnvifServiceUnavailable()
        return buildUrl(path)
    }

    private fun fixHost(url: String): String {
        return URLBuilder(url).apply {
            host = address.host
        }
            .buildString()
    }

    private fun buildUrl(path: String): String {
        return URLBuilder().apply {
            protocol = address.protocol
            host = address.host
            port = address.port
            encodedPath = path
        }
            .buildString()
    }

    public companion object {
        public suspend fun requestDevice(
            url: String,
            username: String?,
            password: String?,
            logger: OnvifLogger? = null,
        ): OnvifDevice {
            val result = execute(
                url,
                servicesCommand,
                username,
                password,
                logger,
            )
            logger?.debug("Addresses: $result")
            val services = parseOnvifServices(result)
            // Work around bug in some cameras that return the incorrect IP address in the services
            val serviceAddresses = services.associate {
                val url = Url(it.address)
                it.namespace to url.encodedPath
            }
            return OnvifDevice(Url(url), username, password, serviceAddresses, logger)
        }

        public suspend fun isReachableEndpoint(url: String, logger: OnvifLogger? = null): Boolean {
            try {
                HttpClient {
                    if (logger != null) {
                        install(Logging) {
                            this.logger = object : Logger {
                                override fun log(message: String) {
                                    logger.debug(message)
                                }
                            }
                            level = LogLevel.ALL
                        }
                    }
                }.use { client ->
                    val response = client.post(url) {
                        contentType(soapContentType)
                        setBody(OnvifCommands.getSystemDateAndTimeCommand)
                    }
                    return response.status.isSuccess()
                }
            } catch (_: IOException) {
                return false
            }
        }

        public suspend fun getHostname(url: String, logger: OnvifLogger? = null): String? {
            val result = execute(
                url,
                OnvifCommands.getHostnameCommand,
                null,
                null,
                logger,
            )
            return parseOnvifGetHostnameResponse(result)
        }

        internal suspend fun execute(
            endpoint: String,
            body: String,
            username: String?,
            password: String?,
            logger: OnvifLogger?,
        ): String {
            HttpClient {
                if (username != null && password != null) {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = username, password = password)
                            }
                        }
                        digest {
                            credentials {
                                DigestAuthCredentials(username = username, password = password)
                            }
                        }
                    }
                }
                if (logger != null) {
                    install(Logging) {
                        this.logger = object : Logger {
                            override fun log(message: String) {
                                logger.debug(message)
                            }
                        }
                        level = LogLevel.ALL
                    }
                }
            }.use { client ->
                val response = client.post(endpoint) {
                    contentType(soapContentType)
                    setBody(body)
                }
                if (response.status.value in 200..299) {
                    return response.bodyAsText()
                } else {
                    throw when (response.status.value) {
                        401 -> OnvifUnauthorized("Unauthorized")
                        403 -> OnvifForbidden("Forbidden")
                        else -> OnvifInvalidResponse("Invalid response from device: ${response.status}")
                    }
                }
            }
        }
    }
}

private val soapContentType: ContentType =
    ContentType(
        contentType = "application",
        contentSubtype = "soap+xml",
        parameters = listOf(HeaderValueParam("charset", "utf-8"))
    )