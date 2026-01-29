package uk.co.xisystems.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("GetStatusResponse", "http://www.onvif.org/ver20/ptz/wsdl", "tptz")
internal class GetStatusResponse(
    @XmlElement(true)
    @XmlSerialName("PTZStatus", "http://www.onvif.org/ver10/schema", "tt")
    val ptzStatus: PtzStatus
)
