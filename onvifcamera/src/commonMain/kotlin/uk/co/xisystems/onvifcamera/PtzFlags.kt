package uk.co.xisystems.onvifcamera

public data class PtzFlags(
    val statusEnabled: Boolean,
    val positionEnabled: Boolean,
    val fieldOfViewEnabled: Boolean? = null
)
