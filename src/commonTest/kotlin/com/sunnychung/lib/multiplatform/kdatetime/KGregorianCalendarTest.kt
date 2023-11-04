package com.sunnychung.lib.multiplatform.kdatetime

import kotlinx.datetime.Clock
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class KGregorianCalendarTest {
    val calendar = KGregorianCalendar

    @Test
    fun leapYear() {
        assertEquals(true, calendar.isLeapYear(2004))
        assertEquals(true, calendar.isLeapYear(2008))
        assertEquals(true, calendar.isLeapYear(2012))
        assertEquals(true, calendar.isLeapYear(2000))
        assertEquals(true, calendar.isLeapYear(2400))
        assertEquals(true, calendar.isLeapYear(1600))
        assertEquals(false, calendar.isLeapYear(1700))
        assertEquals(false, calendar.isLeapYear(1800))
        assertEquals(false, calendar.isLeapYear(1900))
        assertEquals(true, calendar.isLeapYear(1904))

        assertEquals(false, calendar.isLeapYear(2001))
        assertEquals(false, calendar.isLeapYear(2002))
        assertEquals(false, calendar.isLeapYear(2003))
        assertEquals(false, calendar.isLeapYear(2005))
        assertEquals(false, calendar.isLeapYear(2010))
    }

//    @Test
    fun binarySearch() {
        val a = listOf(1, 4, 5, 8, 10, 12)
        (0..15).forEach { find ->
            var r = a.binarySearch { it.compareTo(find) }
            if (r < 0) r = -(r + 1 + 1)
            println("$find -> [${r}]")
        }
    }

    @Test
    fun convertTimestampToUTCDate() {
        calendar.utcDateFromTimestamp(0).run {
            assertEquals(1970, year)
            assertEquals(1, month)
            assertEquals(1, day)
        }
        calendar.utcDateFromTimestamp(946684799_999).run {
            assertEquals(1999, year)
            assertEquals(12, month)
            assertEquals(31, day)
        }
        calendar.utcDateFromTimestamp(1694366356_456).run {
            assertEquals(2023, year)
            assertEquals(9, month)
            assertEquals(10, day)
        }
        calendar.utcDateFromTimestamp(967173533_000).run {
            assertEquals(2000, year)
            assertEquals(8, month)
            assertEquals(25, day)
        }
        calendar.utcDateFromTimestamp(4129409933_600).run {
            assertEquals(2100, year)
            assertEquals(11, month)
            assertEquals(9, day)
        }
        calendar.utcDateFromTimestamp(-31877452801_000).run {
            assertEquals(959, year)
            assertEquals(11, month)
            assertEquals(4, day)
        }
        calendar.utcDateFromTimestamp(-31877452801_000 + 789).run {
            assertEquals(959, year)
            assertEquals(11, month)
            assertEquals(4, day)
        }
        calendar.utcDateFromTimestamp(-31877452801_000 + 1000).run {
            assertEquals(959, year)
            assertEquals(11, month)
            assertEquals(5, day)
        }
    }

    @Test
    fun convertTimestampToUTCDate_performance() {
        val SAMPLE_SIZE = 300_000 /* JS is slow (500,000), iOS simulator is even slower (300,000) */
        val TIME_LIMIT_SECONDS = 1

        val random = Random.Default
        val randomTimestamps = (1..SAMPLE_SIZE).map { random.nextLong(29374971533_000) }

        val startTime = Clock.System.now()
        randomTimestamps.forEach {
            calendar.utcDateFromTimestamp(it)
        }
        val endTime = Clock.System.now()
        val duration = endTime - startTime
        println("Converted $SAMPLE_SIZE timestamps in ${duration.inWholeMilliseconds} ms")
        assertTrue(duration.inWholeMilliseconds <= TIME_LIMIT_SECONDS * 1000)
    }

    @Test
    fun kZonedInstantFromLocalDate() {
        calendar.kZonedInstantFromLocalDate(
            year = 1970,
            month = 1,
            day = 1,
            hour = 0,
            minute = 0,
            second = 0,
            millisecond = 0,
            zoneOffset = KZoneOffset(0, 0)
        ).run {
            assertEquals(0, toMilliseconds())
        }

        calendar.kZonedInstantFromLocalDate(
            year = 1999,
            month = 12,
            day = 31,
            hour = 23,
            minute = 59,
            second = 59,
            millisecond = 999,
            zoneOffset = KZoneOffset(0, 0)
        ).run {
            assertEquals(946684799_999, toMilliseconds())
        }

        calendar.kZonedInstantFromLocalDate(
            year = 2023,
            month = 9,
            day = 10,
            hour = 17,
            minute = 19,
            second = 16,
            millisecond = 456,
            zoneOffset = KZoneOffset(0, 0)
        ).run {
            assertEquals(1694366356_456, toMilliseconds())
        }

        calendar.kZonedInstantFromLocalDate(
            year = 2000,
            month = 8,
            day = 25,
            hour = 3,
            minute = 18,
            second = 53,
            zoneOffset = KZoneOffset(0, 0)
        ).run {
            assertEquals(967173533_000, toMilliseconds())
        }

        calendar.kZonedInstantFromLocalDate(
            year = 2100,
            month = 11,
            day = 9,
            hour = 2,
            minute = 18,
            second = 53,
            millisecond = 600,
            zoneOffset = KZoneOffset(0, 0)
        ).run {
            assertEquals(4129409933_600, toMilliseconds())
        }

        calendar.kZonedInstantFromLocalDate(
            year = 959,
            month = 11,
            day = 4,
            hour = 7,
            minute = 33,
            second = 21,
            millisecond = 456,
            zoneOffset = KZoneOffset(0, 0)
        ).run {
            assertEquals(-31877511999_000 + 456, toMilliseconds())
        }
    }

    @Test
    fun validateInvalidDateShouldThrowError() {
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 2, day = 29)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 8, day = 32)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 4, day = 31)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 6, day = 31)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 9, day = 31)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 11, day = 31)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 13, day = 1)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 12, day = 0)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 0, day = 12)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = -1, day = 12)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2021, month = 1, day = -1)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2020, month = 2, day = 30)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 2000, month = 2, day = 30)
        }
        assertFailsWith(IllegalArgumentException::class) {
            calendar.validateDate(year = 1900, month = 2, day = 29)
        }
    }

    @Test
    fun validateValidDateShouldPass() {
        calendar.validateDate(year = 2023, month = 1, day = 1)
        calendar.validateDate(year = 2023, month = 7, day = 25)
        calendar.validateDate(year = 2023, month = 1, day = 31)
        calendar.validateDate(year = 2023, month = 3, day = 31)
        calendar.validateDate(year = 2023, month = 5, day = 31)
        calendar.validateDate(year = 2023, month = 7, day = 31)
        calendar.validateDate(year = 2023, month = 8, day = 31)
        calendar.validateDate(year = 2023, month = 10, day = 31)
        calendar.validateDate(year = 2023, month = 12, day = 31)
        calendar.validateDate(year = 2023, month = 10, day = 30)
        calendar.validateDate(year = 2023, month = 2, day = 28)
        calendar.validateDate(year = 2020, month = 2, day = 29)
        calendar.validateDate(year = 2000, month = 2, day = 29)
        calendar.validateDate(year = 1900, month = 2, day = 28)
        calendar.validateDate(year = 12345678, month = 7, day = 31)
        calendar.validateDate(year = -1000000, month = 8, day = 31)
    }
}
