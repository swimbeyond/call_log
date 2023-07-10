package org.bogucki.calllog.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class CommonExtensionsTest {

    @Test
    fun `given a number of seconds verify that Long_toHHmmssString returns correct representation of time`() {

        assertEquals("00:00", 0L.secondstoHHmmssString())

        assertEquals("00:05", 5L.secondstoHHmmssString())
        assertEquals("01:00", 60L.secondstoHHmmssString())
        assertEquals("02:05", 125L.secondstoHHmmssString())
        assertEquals("10:00", 600L.secondstoHHmmssString())
        assertEquals("1:00:00", 3600L.secondstoHHmmssString())
        assertEquals("1:01:00", 3660L.secondstoHHmmssString())
        assertEquals("1:01:01", 3661L.secondstoHHmmssString())
    }
}