package com.seanproctor.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("PanTiltLimits", "http://www.onvif.org/ver10/schema", "tt")
internal class PanTiltLimits(
    @XmlElement(true)
    @XmlSerialName("Range", "http://www.onvif.org/ver10/schema", "tt")
    val range: Space2dDescription
)
