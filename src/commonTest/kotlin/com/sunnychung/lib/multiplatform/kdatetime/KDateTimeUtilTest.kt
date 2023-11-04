package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test
import kotlin.test.assertFailsWith

class KDateTimeUtilTest {

    @Test
    fun validateInvalidTimeShouldThrowError() {
        assertFailsWith(IllegalArgumentException::class) {
            validateTime(hour = 24, minute = 12, second = 34, millisecond = 567)
        }
        assertFailsWith(IllegalArgumentException::class) {
            validateTime(hour = 9, minute = 61, second = 34, millisecond = 567)
        }
        assertFailsWith(IllegalArgumentException::class) {
            validateTime(hour = 9, minute = 21, second = 60, millisecond = 567)
        }
        assertFailsWith(IllegalArgumentException::class) {
            validateTime(hour = 9, minute = 21, second = 59, millisecond = 1000)
        }
        assertFailsWith(IllegalArgumentException::class) {
            validateTime(hour = -9, minute = 21, second = 59, millisecond = 555)
        }
        assertFailsWith(IllegalArgumentException::class) {
            validateTime(hour = 9, minute = -21, second = 59, millisecond = 555)
        }
        assertFailsWith(IllegalArgumentException::class) {
            validateTime(hour = 9, minute = 21, second = -59, millisecond = 555)
        }
        assertFailsWith(IllegalArgumentException::class) {
            validateTime(hour = 9, minute = 21, second = 59, millisecond = -1)
        }
    }

    @Test
    fun validateValidTimeShouldPass() {
        validateTime(hour = 12, minute = 34, second = 56, millisecond = 789)
        validateTime(hour = 23, minute = 59, second = 59, millisecond = 999)
        validateTime(hour = 0, minute = 0, second = 0, millisecond = 0)
        validateTime(hour = 1, minute = 2, second = 3, millisecond = 4)
    }
}
