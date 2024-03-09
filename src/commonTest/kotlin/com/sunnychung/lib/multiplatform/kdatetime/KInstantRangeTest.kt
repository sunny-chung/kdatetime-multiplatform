package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.extension.contains
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KInstantRangeTest {
    @Test
    fun `KInstantInClosedKInstantRange`() {
        val range = KInstant(1709975163000) .. KInstant(1709975173000)
        listOf(1709975163000, 1709975173000, 1709975165000, 1709975168901).forEach {
            assertTrue { KInstant(it) in range }
        }
        listOf(1709975163000 - 1, 1709975173000 + 1).forEach {
            assertFalse { KInstant(it) in range }
        }
    }

    @Test
    fun `KZonedInstantInClosedKInstantRange`() {
        val range = KInstant(1709975163000) .. KInstant(1709975173000)
        listOf(1709975163000, 1709975173000, 1709975165000, 1709975168901).forEach {
            assertTrue { KZonedInstant(it, KZoneOffset(8, 0)) in range }
        }
        listOf(1709975163000 - 1, 1709975173000 + 1).forEach {
            assertFalse { KZonedInstant(it, KZoneOffset(8, 0)) in range }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `KInstantInOpenKInstantRange`() {
        val range = KInstant(1709975163000) ..< KInstant(1709975173000)
        listOf(1709975163000, 1709975165000, 1709975168901).forEach {
            assertTrue { KInstant(it) in range }
        }
        listOf(1709975163000 - 1, 1709975173000, 1709975173000 + 1).forEach {
            assertFalse { KInstant(it) in range }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `KZonedInstantInOpenKInstantRange`() {
        val range = KInstant(1709975163000) ..< KInstant(1709975173000)
        listOf(1709975163000, 1709975165000, 1709975168901).forEach {
            assertTrue { KZonedInstant(it, KZoneOffset(-7, 0)) in range }
        }
        listOf(1709975163000 - 1, 1709975173000, 1709975173000 + 1).forEach {
            assertFalse { KZonedInstant(it, KZoneOffset(-7, 0)) in range }
        }
    }
}
