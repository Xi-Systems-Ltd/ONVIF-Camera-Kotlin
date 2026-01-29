package com.seanproctor.onvifcamera.soap

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("PTZStatus", "http://www.onvif.org/ver10/schema", "tt")
internal class PtzStatus(
    @XmlElement(true)
    @XmlSerialName("Position", "http://www.onvif.org/ver10/schema", "tt")
    val position: PtzVector,
    @XmlElement(true)
    @XmlSerialName("MoveStatus", "http://www.onvif.org/ver10/schema", "tt")
    val moveStatus: PtzMoveStatus
)