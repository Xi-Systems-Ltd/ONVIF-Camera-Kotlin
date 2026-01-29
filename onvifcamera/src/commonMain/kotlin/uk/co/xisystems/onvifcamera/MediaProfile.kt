package uk.co.xisystems.onvifcamera

/**
 * Created by Remy Virin on 05/03/2018.
 * @MediaProfile: is used to store an Onvif media profile (token and name)
 */
public data class MediaProfile(val name: String, val token: String, val encoding: String?, val configuration: Configuration? = null, val ptzFilter: PtzFlags? = null) {
    public fun canStream(): Boolean =
            encoding == "MPEG4" || encoding == "H264"

    public fun canSnapshot(): Boolean =
            encoding == "JPEG"

    public fun ptzStatusEnabled(): Boolean =
        ptzFilter?.statusEnabled == true

    public fun ptzPositionEnabled(): Boolean =
        ptzFilter?.positionEnabled == true

    public fun ptzFieldOfViewEnabled(): Boolean =
        ptzFilter?.fieldOfViewEnabled == true
}
