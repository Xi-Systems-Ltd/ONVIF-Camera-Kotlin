package com.seanproctor.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("PTZConfiguration", "http://www.onvif.org/ver10/schema", "tt")
internal class PtzConfiguration(
    @XmlElement(true)
    @XmlSerialName("NodeToken", "http://www.onvif.org/ver10/schema", "tt")
    val nodeToken: String,
    @XmlElement(true)
    @XmlSerialName("PanTiltLimits", "http://www.onvif.org/ver10/schema", "tt")
    val panTiltLimits: PanTiltLimits,
    @XmlElement(true)
    @XmlSerialName("ZoomLimits", "http://www.onvif.org/ver10/schema", "tt")
    val zoomLimits: ZoomLimits
)
