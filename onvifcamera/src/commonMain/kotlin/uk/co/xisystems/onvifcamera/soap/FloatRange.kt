package uk.co.xisystems.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("FloatRange", "http://www.onvif.org/ver10/schema", "tt")
internal class FloatRange(
    @XmlElement(true)
    @XmlSerialName("Min", "http://www.onvif.org/ver10/schema", "tt")
    val min: Float,
    @XmlElement(true)
    @XmlSerialName("Max", "http://www.onvif.org/ver10/schema", "tt")
    val max: Float
)
