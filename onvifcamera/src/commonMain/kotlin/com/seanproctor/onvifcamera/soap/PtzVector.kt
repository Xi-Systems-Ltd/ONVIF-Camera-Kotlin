package com.seanproctor.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("PTZVector", "http://www.onvif.org/ver10/schema", "tt")
internal class PtzVector(
    @XmlElement(true)
    @XmlSerialName("PanTilt", "http://www.onvif.org/ver10/schema", "tt")
    val panTilt: Vector2d,
    @XmlElement(true)
    @XmlSerialName("Zoom", "http://www.onvif.org/ver10/schema", "tt")
    val zoom: Vector1d
)