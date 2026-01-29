package uk.co.xisystems.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("PTZMoveStatus", "http://www.onvif.org/ver10/media/wsdl", "trt")
internal class PtzMoveStatus(
    @XmlElement(true)
    @XmlSerialName("PanTilt", "http://www.onvif.org/ver10/schema", "tt")
    val panTilt: String,
    @XmlElement(true)
    @XmlSerialName("Zoom", "http://www.onvif.org/ver10/schema", "tt")
    val zoom: String
)
