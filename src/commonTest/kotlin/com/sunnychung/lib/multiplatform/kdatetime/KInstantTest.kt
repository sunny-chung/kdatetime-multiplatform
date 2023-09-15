package com.sunnychung.lib.multiplatform.kdatetime

import kotlinx.datetime.Clock
import kotlin.math.absoluteValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KInstantTest {
    @Test
    fun currentTime() {
        val now = Clock.System.now()
        val kInstant = KInstant.now()
        assertTrue((kInstant.toMilliseconds() - now.toEpochMilliseconds()).absoluteValue < 100)
    }

    @Test
    fun minusInstantGivesDuration() {
        val offset = 1000L * 60 * 60 // 1 hour
        val t1 = KInstant(1694404171789)
        val t2 = KInstant(1694404171789 - offset)
        val difference = t1 - t2
        assertTrue(difference is KDuration)
        assertEquals(offset, difference.toMilliseconds())

        val difference2 = t2 - t1
        assertEquals(-1 * offset, difference2.toMilliseconds())
    }
}
