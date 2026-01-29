package uk.co.xisystems.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("PTZFilter", "http://www.onvif.org/ver10/schema", "tt")
internal class PtzFilter(
    @XmlElement(true)
    @XmlSerialName("Status", "http://www.onvif.org/ver10/schema", "tt")
    val status: Boolean,
    @XmlElement(true)
    @XmlSerialName("Position", "http://www.onvif.org/ver10/schema", "tt")
    val position: Boolean,
    @XmlElement(true)
    @XmlSerialName("FieldOfView", "http://www.onvif.org/ver10/schema", "tt")
    val fieldOfView: Boolean? = null
)