package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test
import kotlin.test.assertEquals

class KZonedInstantTest {
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

    @Test
    fun copy() {
        KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).run {
            assertEquals(toMilliseconds(), copy().toMilliseconds())
        }
        KZonedInstant(timestampMs = 1694409632_999, zoneOffset = KZoneOffset(hours = -7, minutes = 0)).run {
            assertEquals(toMilliseconds(), copy().toMilliseconds())
        }

        // Monday, September 11, 2023 2:31:39 PM GMT+08:00
        KZonedInstant(timestampMs = 1694413899_001, zoneOffset = KZoneOffset(hours = 8, minutes = 0)).run {
            assertEquals(1694529067_666, copy(
                day = 12,
                hour = 22,
                second = 7,
                millisecond = 666
            ).toMilliseconds())
            assertEquals(1694413899_001, toMilliseconds()) // assert original instance is not modified
        }
    }
}
