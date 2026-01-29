package uk.co.xisystems.onvifcamera

public data class Configuration(
    val token: String,
    val panMin: Float,
    val panMax: Float,
    val tiltMin: Float,
    val tiltMax: Float,
    val zoomMin: Float,
    val zoomMax: Float
)