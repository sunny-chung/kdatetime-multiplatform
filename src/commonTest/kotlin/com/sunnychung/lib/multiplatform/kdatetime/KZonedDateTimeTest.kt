package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    fun plusDuration() {
        val dateTime1 = KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).toKZonedDateTime()
        val dateTime2 = dateTime1 + KDuration.of(2, KFixedTimeUnit.Hour)
        assertEquals(1694409632_999 + 2 * 60 * 60 * 1000, dateTime2.toKZonedInstant().toEpochMilliseconds())
    }

    @Test
    fun minusDuration() {
        val dateTime1 = KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).toKZonedDateTime()
        val dateTime2 = dateTime1 - KDuration.of(3, KFixedTimeUnit.Hour)
        assertEquals(1694409632_999 - 3 * 60 * 60 * 1000, dateTime2.toKZonedInstant().toEpochMilliseconds())
    }

    @Test
    fun minusDateTime() {
        val dateTime1 = KZonedDateTime(year = 2024, month = 1, day = 20, hour = 13, minute = 42, second = 29, zoneOffset = KZoneOffset(-7, 0))
        val dateTime2 = KZonedDateTime(year = 2024, month = 1, day = 20, hour = 13, minute = 42, second = 27, millisecond = 600, zoneOffset = KZoneOffset(-7, 0))
        assertTrue { (dateTime1 - dateTime2) is KDuration }
        assertEquals(1400, (dateTime1 - dateTime2).toMilliseconds())
    }

    @Test
    fun format() {
        val dateTime = KZonedDateTime(year = 2024, month = 1, day = 20, hour = 13, minute = 42, second = 29, zoneOffset = KZoneOffset(-7, 0))
        val dateTime2 = KZonedDateTime(year = 2024, month = 1, day = 20, hour = 8, minute = 42, second = 29, zoneOffset = KZoneOffset(-7, 0))
        assertEquals("2024-1-20 1:42:29 pm (-07:00)", dateTime.format("yyyy-M-d h:mm:ss aa (Z)"))
        val formatter = KDateTimeFormat("yyyy-M-d h:mm:ss aa AA (Z)")
        formatter.setAmPmUppercaseNames("A.M.", "P.M.")
        assertEquals("2024-1-20 1:42:29 p.m. P.M. (-07:00)", formatter.format(dateTime.toKZonedInstant()))
        assertEquals("2024-1-20 8:42:29 a.m. A.M. (-07:00)", formatter.format(dateTime2.toKZonedInstant()))

        val formatter2 = KDateTimeFormat("yyyy-M-d h:mm:ss aa AA (Z)")
        formatter2.setAmPmUppercaseNames("U.V.", "N.V.")
        assertEquals("2024-1-20 1:42:29 n.v. N.V. (-07:00)", formatter2.format(dateTime.toKZonedInstant()))
        assertEquals("2024-1-20 8:42:29 u.v. U.V. (-07:00)", formatter2.format(dateTime2.toKZonedInstant()))

        val formatter3 = KDateTimeFormat("yyyy-M-d h:mm:ss aa AA (Z)")
        formatter3.setAmPmUppercaseNames("오전", "오후")
        assertEquals("2024-1-20 1:42:29 오후 오후 (-07:00)", formatter3.format(dateTime.toKZonedInstant()))
        assertEquals("2024-1-20 8:42:29 오전 오전 (-07:00)", formatter3.format(dateTime2.toKZonedInstant()))

        val formatter4 = KDateTimeFormat("yyyy-M-d h:mm:ss AA aa (Z)")
        formatter4.setAmPmNames("a.m.", "p.m.", "AK.MK.", "PK.MK.")
        assertEquals("2024-1-20 1:42:29 PK.MK. p.m. (-07:00)", formatter4.format(dateTime.toKZonedInstant()))
        assertEquals("2024-1-20 8:42:29 AK.MK. a.m. (-07:00)", formatter4.format(dateTime2.toKZonedInstant()))
    }

    @Test
    fun toIso8601String() {
        val zonedDateTime = KZonedDateTime(year = 2024, month = 3, day = 12, hour = 5, minute = 12, second = 3, millisecond = 999, zoneOffset = KZoneOffset(hours = 8, minutes = 0))
        assertEquals("2024-03-12T05:12:03+08:00", zonedDateTime.toIso8601String())
        assertEquals("2024-03-12T05:12:03.999+08:00", zonedDateTime.toIso8601StringWithMilliseconds())
    }
}
