package uk.co.xisystems.onvifcamera.parsers

import kotlin.test.Test
import kotlin.test.assertEquals
import uk.co.xisystems.onvifcamera.parseOnvifProbeResponse
import uk.co.xisystems.onvifcamera.readResourceFile

class ProbeParserTest {
    @Test
    fun testProbeResponseParser() {
        val input = readResourceFile("probeResponse.xml")
        val result = parseOnvifProbeResponse(input)
        assertEquals(1, result.size)
    }

    @Test
    fun testProbeResponse2Parser() {
        val input = readResourceFile("probeResponse2.xml")
        val result = parseOnvifProbeResponse(input)
        assertEquals(1, result.size)
    }
}