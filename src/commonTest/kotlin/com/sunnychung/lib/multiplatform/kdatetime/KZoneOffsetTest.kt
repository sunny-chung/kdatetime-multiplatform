package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test
import kotlin.test.assertEquals

class KZoneOffsetTest {
    @Test
    fun toMilliseconds() {
        assertEquals(0L, KZoneOffset(0, 0).toMilliseconds())
        assertEquals(8L * 60 * 60 * 1000, KZoneOffset(8, 0).toMilliseconds())
        assertEquals(-7L * 60 * 60 * 1000, KZoneOffset(-7, 0).toMilliseconds())
        assertEquals(-5L * 60 * 60 * 1000 - 30L * 60 * 1000, KZoneOffset(-5, 30).toMilliseconds())
        assertEquals(13L * 60 * 60 * 1000 + 45L * 60 * 1000, KZoneOffset(13, 45).toMilliseconds())
    }

    @Test
    fun toDisplayString() {
        assertEquals("Z", KZoneOffset(0, 0).toDisplayString())
        assertEquals("+08:00", KZoneOffset(8, 0).toDisplayString())
        assertEquals("-07:00", KZoneOffset(-7, 0).toDisplayString())
        assertEquals("-05:30", KZoneOffset(-5, 30).toDisplayString())
        assertEquals("+13:45", KZoneOffset(13, 45).toDisplayString())
    }
}
