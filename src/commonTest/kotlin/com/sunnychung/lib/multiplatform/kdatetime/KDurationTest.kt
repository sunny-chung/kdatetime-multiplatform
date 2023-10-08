package com.sunnychung.lib.multiplatform.kdatetime

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
}
