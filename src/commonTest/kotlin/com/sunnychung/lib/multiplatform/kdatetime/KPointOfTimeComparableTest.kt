package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KPointOfTimeComparableTest {

    @Test
    fun simpleCompareInstant() {
        assertEquals(true, KInstant(1694400859230) < KInstant(1694400859231))
        assertEquals(false, KInstant(1694400859230) > KInstant(1694400859231))
        assertEquals(false, KInstant(1694400859232) < KInstant(1694400859231))
        assertEquals(true, KInstant(1694400859232) > KInstant(1694400859231))
        assertEquals(true, KInstant(1694400859232) == KInstant(1694400859232))
        assertEquals(true, KInstant(1694400859231) != KInstant(1694400859232))
    }

    @Test
    fun simpleCompareZonedInstant() {
        val timeZoneOffset = KZoneOffset.local()
        assertEquals(true, KZonedInstant(timestampMs = 1694400859230, zoneOffset = timeZoneOffset) < KZonedInstant(timestampMs = 1694400859231, zoneOffset = timeZoneOffset))
        assertEquals(false, KZonedInstant(timestampMs = 1694400859230, zoneOffset = timeZoneOffset) > KZonedInstant(timestampMs = 1694400859231, zoneOffset = timeZoneOffset))
        assertEquals(false, KZonedInstant(timestampMs = 1694400859232, zoneOffset = timeZoneOffset) < KZonedInstant(timestampMs = 1694400859231, zoneOffset = timeZoneOffset))
        assertEquals(true, KZonedInstant(timestampMs = 1694400859232, zoneOffset = timeZoneOffset) > KZonedInstant(timestampMs = 1694400859231, zoneOffset = timeZoneOffset))
        assertEquals(true, KZonedInstant(timestampMs = 1694400859232, zoneOffset = timeZoneOffset) == KZonedInstant(timestampMs = 1694400859232, zoneOffset = timeZoneOffset))
        assertEquals(true, KZonedInstant(timestampMs = 1694400859231, zoneOffset = timeZoneOffset) != KZonedInstant(timestampMs = 1694400859232, zoneOffset = timeZoneOffset))
    }

    @Test
    fun compareBetweenInstantAndZonedInstant() {
        val timeZoneOffset = KZoneOffset.local()
        (1694400859230 .. 1694400859232).forEach { timestamp1 ->
            (1694400859230 .. 1694400859232).forEach { timestamp2 ->
                (1 .. 2).forEach { direction ->
                    val t1: KPointOfTime
                    val t2: KPointOfTime
                    if (direction == 1) {
                        t1 = KInstant(timestampMs = timestamp1)
                        t2 = KZonedInstant(timestampMs = timestamp2, zoneOffset = timeZoneOffset)
                    } else {
                        t1 = KZonedInstant(timestampMs = timestamp1, zoneOffset = timeZoneOffset)
                        t2 = KInstant(timestampMs = timestamp2)
                    }
                    assertEquals(timestamp1.compareTo(timestamp2), t1.compareTo(t2))
                    if (timestamp1 == timestamp2) {
                        assertTrue(t1 == t2)
                    }
                }
            }
        }
    }

    @Test
    fun comparisonIsIndependentOfTimeZone() {
        assertEquals(true, KZonedInstant(timestampMs = 1694400859230, zoneOffset = KZoneOffset(9, 0)) < KZonedInstant(timestampMs = 1694400859232, zoneOffset = KZoneOffset(7, 0)))
        assertEquals(false, KZonedInstant(timestampMs = 1694400859230, zoneOffset = KZoneOffset(8, 0)) >= KZonedInstant(timestampMs = 1694400859232, zoneOffset = KZoneOffset(-7, 0)))
        assertEquals(true, KZonedInstant(timestampMs = 1694400859231, zoneOffset = KZoneOffset(8, 0)) == KZonedInstant(timestampMs = 1694400859231, zoneOffset = KZoneOffset(-7, 0)))
    }
}
