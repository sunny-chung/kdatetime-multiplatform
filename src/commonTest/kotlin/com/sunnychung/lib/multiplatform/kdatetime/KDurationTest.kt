package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.extension.hours
import com.sunnychung.lib.multiplatform.kdatetime.extension.minutes
import com.sunnychung.lib.multiplatform.kdatetime.extension.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KDurationTest {
    @Test
    fun compareTo() {
        val duration = KDuration.of(100, KFixedTimeUnit.Second)
        assertTrue(duration > KDuration.of(99, KFixedTimeUnit.Second))
        assertTrue(duration >= KDuration.of(99, KFixedTimeUnit.Second))
        assertEquals(false, duration < KDuration.of(99, KFixedTimeUnit.Second))
        assertTrue(duration >= KDuration.of(100, KFixedTimeUnit.Second))
        assertTrue(duration <= KDuration.of(100, KFixedTimeUnit.Second))
        assertEquals(false, duration > KDuration.of(100, KFixedTimeUnit.Second))
        assertEquals(false, duration < KDuration.of(100, KFixedTimeUnit.Second))
        assertEquals(false, duration > KDuration.of(101, KFixedTimeUnit.Second))
        assertTrue(duration < KDuration.of(101, KFixedTimeUnit.Second))
        assertTrue(duration <= KDuration.of(101, KFixedTimeUnit.Second))
    }

    @Test
    fun equality() {
        val duration = KDuration.of(100, KFixedTimeUnit.Second)
        assertTrue(duration == KDuration.of(100, KFixedTimeUnit.Second))
    }

    @Test
    fun shorthandConversions() {
        val duration = 2.hours() + 11.minutes() + 6.seconds() - 7.seconds()
        assertEquals(2 * 3600 + 11 * 60 + 6 - 7, duration.toSeconds())
    }
}
