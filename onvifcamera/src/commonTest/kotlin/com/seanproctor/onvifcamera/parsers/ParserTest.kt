package com.seanproctor.onvifcamera.parsers

import com.seanproctor.onvifcamera.parseOnvifConfigurations
import com.seanproctor.onvifcamera.parseOnvifDeviceInformation
import com.seanproctor.onvifcamera.parseOnvifProfiles
import com.seanproctor.onvifcamera.parseOnvifSnapshotUri
import com.seanproctor.onvifcamera.parseOnvifStatus
import com.seanproctor.onvifcamera.parseOnvifStreamUri
import com.seanproctor.onvifcamera.readResourceFile
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun testStreamUriResponseParser() {
        val input = readResourceFile("./stream.xml")
        val result = parseOnvifStreamUri(input)
        assertEquals("rtsp://192.168.0.209/onvif-media/media.amp?profile=profile_1_h264&sessiontimeout=60&streamtype=unicast", result)
    }

    @Test
    fun testSnapshotUriResponseParser() {
        val input = readResourceFile("snapshot.xml")
        val result = parseOnvifSnapshotUri(input)
        assertEquals("http://192.168.0.209/onvif-cgi/jpg/image.cgi?resolution=1920x1080&compression=30", result)
    }

    @Test
    fun testProfilesResponseParser() {
        val input = readResourceFile("profiles.xml")
        val result = parseOnvifProfiles(input)
        assertEquals(2, result.size)
        result[0].let {
            assertEquals(0.0f, it.configuration?.zoomMin)
            assertEquals(true, it.ptzPositionEnabled())
        }
    }

    @Test
    fun testDeviceInfoResponseParser() {
        val input = readResourceFile("deviceInfo.xml")
        val result = parseOnvifDeviceInformation(input)
        assertEquals("AXIS", result.manufacturer)
    }

    @Test
    fun testOnvifConfigurationsParser() {
        val input = readResourceFile("configurations.xml")
        val result = parseOnvifConfigurations(input)
        assertEquals(1, result.size)
    }

    @Test
    fun testOnvifStatusParser() {
        val input = readResourceFile("status.xml")
        val result = parseOnvifStatus(input)
        assertEquals(0.12f, result.ptz.pan)
    }
}