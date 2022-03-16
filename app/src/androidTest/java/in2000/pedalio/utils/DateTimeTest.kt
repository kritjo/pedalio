package in2000.pedalio.utils

import org.junit.Assert.*
import org.junit.Test

class DateTimeTest{
    @Test
    fun iso_to_milli() {
        assertEquals(970106400000, DateTime.iso_to_milli("2000-09-28T04:00:00.000+02:00"))
    }

    @Test
    fun milli_to_iso() {
        assertEquals("2000-09-28T02:00:00Z", DateTime.milli_to_iso(970106400000))
    }

    @Test
    fun closest_hour() {
        assertEquals("2001-07-11T22:00:00Z", DateTime.closest_hour(994888804837))
        assertEquals("2001-07-11T23:00:00Z", DateTime.closest_hour(994891999837))
    }

}