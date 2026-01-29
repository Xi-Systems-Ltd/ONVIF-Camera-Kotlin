package com.seanproctor.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("MetadataConfiguration", "http://www.onvif.org/ver10/schema", "tt")
internal class MetadataConfiguration(
    @XmlElement(true)
    @XmlSerialName("PTZStatus", "http://www.onvif.org/ver10/schema", "tt")
    val ptzStatus: PtzFilter? = null
)