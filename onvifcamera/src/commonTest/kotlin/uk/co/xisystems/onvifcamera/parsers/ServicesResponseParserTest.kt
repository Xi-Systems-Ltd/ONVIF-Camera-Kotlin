package uk.co.xisystems.onvifcamera.parsers

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertNotNull
import uk.co.xisystems.onvifcamera.parseOnvifServices
import uk.co.xisystems.onvifcamera.readResourceFile

class ServicesResponseParserTest {
    @Test
    fun testServicesResponseParser() {
        val input = readResourceFile("services.xml")
        val result = parseOnvifServices(input)
        val namespace = result.first { it.namespace == "http://www.onvif.org/ver10/search/wsdl" }
        assertNotNull(namespace)
        assertContains(namespace.address, "/onvif/services")
    }
}