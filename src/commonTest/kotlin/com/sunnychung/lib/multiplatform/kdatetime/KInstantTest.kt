package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.KInstant
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue
import kotlin.test.Test
import kotlin.test.assertTrue

class KInstantTest {
    @Test
    fun currentTime() {
        val now = Clock.System.now()
        val kInstant = KInstant.now()
        assertTrue((kInstant.toMilliseconds() - now.toEpochMilliseconds()).absoluteValue < 100)
    }
}
