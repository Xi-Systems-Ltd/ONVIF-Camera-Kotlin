package com.seanproctor.onvifcamera

internal object OnvifCommands {
    /**
     * The header for SOAP 1.2 with digest authentication
     */
    private const val soapHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<soap:Envelope " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
            "xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" >" +
            "<soap:Body>"

    private const val envelopeEnd = "</soap:Body></soap:Envelope>"

    internal const val profilesCommand = (
            soapHeader
                    + "<GetProfiles xmlns=\"http://www.onvif.org/ver10/media/wsdl\"/>"
                    + envelopeEnd
            )

    internal fun getStreamURICommand(profile: MediaProfile, protocol: String = "RTSP"): String {
        return (soapHeader
                + "<GetStreamUri xmlns=\"http://www.onvif.org/ver20/media/wsdl\">"
                + "<ProfileToken>${profile.token}</ProfileToken>"
                + "<Protocol>${protocol}</Protocol>"
                + "</GetStreamUri>"
                + envelopeEnd
                )
    }

    internal fun getSnapshotURICommand(profile: MediaProfile): String {
        return (soapHeader + "<GetSnapshotUri xmlns=\"http://www.onvif.org/ver20/media/wsdl\">"
                + "<ProfileToken>${profile.token}</ProfileToken>"
                + "</GetSnapshotUri>" + envelopeEnd)
    }

    internal const val getConfigurationsCommand = (
            soapHeader
                    + "<GetConfigurations xmlns=\"http://www.onvif.org/ver20/ptz/wsdl\"/>"
                    + envelopeEnd
            )

    internal fun absoluteMoveCommand(
        profile: MediaProfile,
        position: PanTiltZoom,
        speed: PanTiltZoom? = null
    ): String = buildString {
        append(soapHeader)
        append("<AbsoluteMove xmlns=\"http://www.onvif.org/ver20/media/wsdl\">")
        append("<ProfileToken>${profile.token}</ProfileToken>")
        append("<Position>")
        append("<PanTilt x=\"${position.pan}\" y=\"${position.tilt}\"/>")
        append("<Zoom x=\"${position.zoom}\"/>")
        append("</Position>")
        speed?.let {
            append("<Speed>")
            append("<PanTilt x=\"${it.pan}\" y=\"${it.tilt}\"/>")
            append("<Zoom x=\"${it.zoom}\"/>")
            append("</Speed>")
        }
        append("</AbsoluteMove>")
        append(envelopeEnd)
    }

    internal fun relativeMoveCommand(
        profile: MediaProfile,
        translation: PanTiltZoom,
        speed: PanTiltZoom? = null
    ): String = buildString {
        append(soapHeader)
        append("<RelativeMove xmlns=\"http://www.onvif.org/ver20/media/wsdl\">")
        append("<ProfileToken>${profile.token}</ProfileToken>")
        append("<Translation>")
        append("<PanTilt x=\"${translation.pan}\" y=\"${translation.tilt}\"/>")
        append("<Zoom x=\"${translation.zoom}\"/>")
        append("</Translation>")
        speed?.let {
            append("<Speed>")
            append("<PanTilt x=\"${it.pan}\" y=\"${it.tilt}\"/>")
            append("<Zoom x=\"${it.zoom}\"/>")
            append("</Speed>")
        }
        append("</RelativeMove>")
        append(envelopeEnd)
    }


    internal fun continuousMoveCommand(
        profile: MediaProfile,
        speed: PanTiltZoom
    ): String {
        return (soapHeader
                + "<ContinuousMove xmlns=\"http://www.onvif.org/ver20/media/wsdl\">"
                + "<ProfileToken>${profile.token}</ProfileToken>"
                + "<Velocity>"
                + "<PanTilt x=\"${speed.pan}\" y=\"${speed.tilt}\"/>"
                + "<Zoom x=\"${speed.zoom}\"/>"
                + "</Velocity>"
                + "</ContinuousMove>"
                + envelopeEnd
                )
    }

    internal fun stopCommand(
        profile: MediaProfile,
        panTilt: Boolean = true,
        zoom: Boolean = true
    ): String {
        return (soapHeader
                + "<Stop xmlns=\"http://www.onvif.org/ver20/media/wsdl\">"
                + "<ProfileToken>${profile.token}</ProfileToken>"
                + "<PanTilt>"
                + panTilt.toString()
                + "</PanTilt>"
                + "<Zoom>"
                + zoom.toString()
                + "</Zoom>"
                + "</Stop>"
                + envelopeEnd
                )
    }


    internal fun getStatusCommand(profile: MediaProfile): String {
        return (soapHeader
                + "<GetStatus xmlns=\"http://www.onvif.org/ver20/ptz/wsdl\">"
                + "<ProfileToken>${profile.token}</ProfileToken>"
                + "</GetStatus>"
                + envelopeEnd
                )
    }

    internal fun probeCommand(messageId: String): String {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
                "xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">" +
                "<s:Header>" +
                "<a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</a:Action>" +
                "<a:MessageID>uuid:$messageId</a:MessageID>" +
                "<a:ReplyTo>" +
                "<a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address>" +
                "</a:ReplyTo>" +
                "<a:To s:mustUnderstand=\"1\">urn:schemas-xmlsoap-org:ws:2005:04:discovery</a:To>" +
                "</s:Header>" +
                "<s:Body>" +
                "<Probe xmlns=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\">" +
                "<d:Types " +
                "xmlns:d=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\" " +
                "xmlns:dp0=\"http://www.onvif.org/ver10/network/wsdl\">" +
                "dp0:NetworkVideoTransmitter" +
                "</d:Types>" +
                "</Probe>" +
                "</s:Body>" +
                "</s:Envelope>"
    }

    internal const val deviceInformationCommand = (
            soapHeader
                    + "<GetDeviceInformation xmlns=\"http://www.onvif.org/ver10/device/wsdl\">"
                    + "</GetDeviceInformation>"
                    + envelopeEnd
            )

    internal const val servicesCommand = (
            soapHeader
                    + "<GetServices xmlns=\"http://www.onvif.org/ver10/device/wsdl\">"
                    + "<IncludeCapability>false</IncludeCapability>"
                    + "</GetServices>"
                    + envelopeEnd
            )

    internal const val getSystemDateAndTimeCommand = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"" +
            " xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\">" +
            " <SOAP-ENV:Body>" +
            "  <tds:GetSystemDateAndTime/>" +
            " </SOAP-ENV:Body>" +
            "</SOAP-ENV:Envelope>"

    internal val getHostnameCommand = """
            <?xml version="1.0" encoding="UTF-8"?>
            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://www.w3.org/2003/05/soap-envelope"
                    xmlns:tds="http://www.onvif.org/ver10/device/wsdl">
                <SOAP-ENV:Body>
                    <tds:GetHostname/>
                </SOAP-ENV:Body>
            </SOAP-ENV:Envelope>
            """.trimIndent()
}