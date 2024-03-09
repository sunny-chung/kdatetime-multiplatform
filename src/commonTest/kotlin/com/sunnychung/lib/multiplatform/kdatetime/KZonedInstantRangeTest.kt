package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.extension.contains
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KZonedInstantRangeTest {
    @Test
    fun `KInstantInClosedKZonedInstantRange`() {
        val range = KZonedInstant(1709975163000, KZoneOffset(1, 0)) .. KZonedInstant(1709975173000, KZoneOffset(1, 0))
        listOf(1709975163000, 1709975173000, 1709975165000, 1709975168901).forEach {
            assertTrue { KInstant(it) in range }
        }
        listOf(1709975163000 - 1, 1709975173000 + 1).forEach {
            assertFalse { KInstant(it) in range }
        }
    }

    @Test
    fun `KZonedInstantInClosedKZonedInstantRange`() {
        val range = KZonedInstant(1709975163000, KZoneOffset(1, 0)) .. KZonedInstant(1709975173000, KZoneOffset(1, 0))
        listOf(1709975163000, 1709975173000, 1709975165000, 1709975168901).forEach {
            assertTrue { KZonedInstant(it, KZoneOffset(8, 0)) in range }
        }
        listOf(1709975163000 - 1, 1709975173000 + 1).forEach {
            assertFalse { KZonedInstant(it, KZoneOffset(8, 0)) in range }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `KInstantInOpenKZonedInstantRange`() {
        val range = KZonedInstant(1709975163000, KZoneOffset(1, 0)) ..< KZonedInstant(1709975173000, KZoneOffset(1, 0))
        listOf(1709975163000, 1709975165000, 1709975168901).forEach {
            assertTrue { KInstant(it) in range }
        }
        listOf(1709975163000 - 1, 1709975173000, 1709975173000 + 1).forEach {
            assertFalse { KInstant(it) in range }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `KZonedInstantInOpenKZonedInstantRange`() {
        val range = KZonedInstant(1709975163000, KZoneOffset(1, 0)) ..< KZonedInstant(1709975173000, KZoneOffset(1, 0))
        listOf(1709975163000, 1709975165000, 1709975168901).forEach {
            assertTrue { KZonedInstant(it, KZoneOffset(-7, 0)) in range }
        }
        listOf(1709975163000 - 1, 1709975173000, 1709975173000 + 1).forEach {
            assertFalse { KZonedInstant(it, KZoneOffset(-7, 0)) in range }
        }
    }
}
