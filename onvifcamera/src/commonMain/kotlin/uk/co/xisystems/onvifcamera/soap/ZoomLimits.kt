package uk.co.xisystems.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("ZoomLimits", "http://www.onvif.org/ver10/schema", "tt")
internal class ZoomLimits(
    @XmlElement(true)
    @XmlSerialName("Range", "http://www.onvif.org/ver10/schema", "tt")
    val range: Space1dDescription
)
