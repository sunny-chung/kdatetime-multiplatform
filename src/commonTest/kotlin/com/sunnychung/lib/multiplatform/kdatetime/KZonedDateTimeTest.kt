package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test
import kotlin.test.assertEquals

class KZonedDateTimeTest {

    @Test
    fun copy() {
        KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).run {
            assertEquals(toMilliseconds(), toKZonedDateTime().copy().toKZonedInstant().toMilliseconds())
        }
        KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = -7, minutes = 0)).run {
            assertEquals(toMilliseconds(), toKZonedDateTime().copy().toKZonedInstant().toMilliseconds())
        }

        // Monday, September 11, 2023 2:31:39 PM GMT+08:00
        KZonedInstant(timestampMs = 1694413899_001, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).toKZonedDateTime().run {
            assertEquals(1694529067_666, copy(
                day = 12,
                hour = 22,
                second = 7,
                millisecond = 666
            ).toKZonedInstant().toMilliseconds())
            assertEquals(1694413899_001, toKZonedInstant().toMilliseconds()) // assert original instance is not modified
        }

        // Monday, September 11, 2023 2:31:39 PM GMT+08:00
        KZonedInstant(timestampMs = 1694413899_001, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).toKZonedDateTime().run {
            // Monday, September 11, 2023 2:31:39 PM GMT-07:00
            val newZonedDateTime = copy(zoneOffset = KZoneOffset(hours = -7, minutes = 0))
            assertEquals(datePart(), newZonedDateTime.datePart())
            assertEquals(hour, newZonedDateTime.hour)
            assertEquals(minute, newZonedDateTime.minute)
            assertEquals(second, newZonedDateTime.second)
            assertEquals(millisecond, newZonedDateTime.millisecond)
        }
    }

    @Test
    fun plus() {
        val dateTime1 = KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).toKZonedDateTime()
        val dateTime2 = dateTime1 + KDuration.of(2, KFixedTimeUnit.Hour)
        assertEquals(1694409632_999 + 2 * 60 * 60 * 1000, dateTime2.toKZonedInstant().toEpochMilliseconds())
    }

    @Test
    fun minus() {
        val dateTime1 = KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).toKZonedDateTime()
        val dateTime2 = dateTime1 - KDuration.of(3, KFixedTimeUnit.Hour)
        assertEquals(1694409632_999 - 3 * 60 * 60 * 1000, dateTime2.toKZonedInstant().toEpochMilliseconds())
    }
}
