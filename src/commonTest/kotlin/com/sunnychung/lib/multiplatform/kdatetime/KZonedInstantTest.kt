package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test
import kotlin.test.assertEquals

class KZonedInstantTest {
    @Test
    fun dateTimeParts() {
        // Monday, September 11, 2023 at 6:31:39 AM UTC
        KZonedInstant(timestampMs = 1694413899_001, zoneOffset = KZoneOffset.UTC).run {
            assertEquals(KDate(2023, 9, 11), datePart())
            assertEquals(6, hourPart())
            assertEquals(31, minutePart())
            assertEquals(39, secondPart())
            assertEquals(1, millisecondPart())
        }

        // Monday, September 11th 2023, 7:16:39 pm GMT+12:45
        KZonedInstant(timestampMs = 1694413899_001, zoneOffset = KZoneOffset(hours = 12, minutes = 45)).run {
            assertEquals(KDate(2023, 9, 11), datePart())
            assertEquals(19, hourPart())
            assertEquals(16, minutePart())
            assertEquals(39, secondPart())
            assertEquals(1, millisecondPart())
        }

        // Monday, September 11th 2023, 2:31:39 am GMT-04:00
        KZonedInstant(timestampMs = 1694413899_001, zoneOffset = KZoneOffset(hours = -4, minutes = 0)).run {
            assertEquals(KDate(2023, 9, 11), datePart())
            assertEquals(2, hourPart())
            assertEquals(31, minutePart())
            assertEquals(39, secondPart())
            assertEquals(1, millisecondPart())
        }

        // Monday, September 11th 2023, 2:16:39 am GMT-04:15
        KZonedInstant(timestampMs = 1694413899_001, zoneOffset = KZoneOffset(hours = -4, minutes = 15)).run {
            assertEquals(KDate(2023, 9, 11), datePart())
            assertEquals(2, hourPart())
            assertEquals(16, minutePart())
            assertEquals(39, secondPart())
            assertEquals(1, millisecondPart())
        }
    }

    @Test
    fun startOfDay() {
        // Monday, September 11, 2023 1:20:32 PM GMT+08:00
        val instant1 = KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = 8, minutes = 0))
        instant1.startOfDay().run {
            assertEquals(1694361600_000, toMilliseconds())
        }
        assertEquals(1694409632_999, instant1.toMilliseconds()) // assert original instance is not modified

        // Monday, September 11, 2023 3:00:00 AM GMT+08:00
        val instant2 = KZonedInstant(timestampMs = 1694372400_000, zoneOffset = KZoneOffset(hours = 8, minutes = 0))
        instant2.startOfDay().run {
            assertEquals(1694361600_000, toMilliseconds())
        }

        // Sunday, September 10, 2023 7:00:00 PM GMT
        val instant3 = KZonedInstant(timestampMs = 1694372400_000, zoneOffset = KZoneOffset(hours = 0, minutes = 0))
        instant3.startOfDay().run {
            assertEquals(1694304000_000, toMilliseconds())
        }
    }
}
