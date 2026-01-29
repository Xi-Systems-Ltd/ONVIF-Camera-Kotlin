package com.seanproctor.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Space1DDescription", "http://www.onvif.org/ver10/schema", "tt")
internal class Space1dDescription(
    @XmlElement(true)
    @XmlSerialName("URI", "http://www.onvif.org/ver10/schema", "tt")
    val uri: String,
    @XmlElement(true)
    @XmlSerialName("XRange", "http://www.onvif.org/ver10/schema", "tt")
    val xRange: FloatRange
)
