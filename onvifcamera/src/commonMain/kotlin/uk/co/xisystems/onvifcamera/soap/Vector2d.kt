package uk.co.xisystems.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Vector2D", "http://www.onvif.org/ver10/schema", "tt")
internal class Vector2d(
    val x: Float,
    val y: Float
)
