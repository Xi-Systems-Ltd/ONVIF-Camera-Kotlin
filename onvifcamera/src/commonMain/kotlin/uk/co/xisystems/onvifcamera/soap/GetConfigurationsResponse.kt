package uk.co.xisystems.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("GetConfigurationsResponse", "http://www.onvif.org/ver20/ptz/wsdl", "tptz")
internal class GetConfigurationsResponse(
    @XmlSerialName("PTZConfiguration", "http://www.onvif.org/ver10/schema", "tt")
    val ptzConfiguration: List<PtzConfiguration>
)
