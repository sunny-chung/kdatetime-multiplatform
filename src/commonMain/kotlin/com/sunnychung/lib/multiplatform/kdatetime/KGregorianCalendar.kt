package com.sunnychung.lib.multiplatform.kdatetime

object KGregorianCalendar {
    private val DAYS_PER_MONTH_IN_NON_LEAP_YEAR = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val DAYS_PER_MONTH_IN_LEAP_YEAR = listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    private val ACCUMULATED_DAYS_PER_MONTH_IN_NON_LEAP_YEAR = accumulateList(DAYS_PER_MONTH_IN_NON_LEAP_YEAR)
    private val ACCUMULATED_DAYS_PER_MONTH_IN_LEAP_YEAR = accumulateList(DAYS_PER_MONTH_IN_LEAP_YEAR)

//    private val TIMESTAMP_MS_TO_YEAR: Map<Long, Int>
    private val YEAR_TO_TIMESTAMP_MS_MAP: Map<Int, Long>
    private val YEAR_TO_TIMESTAMP_MS_LIST: List<YearTimestampPair>

    data class YearTimestampPair(val year: Int, val timestampMs: Long)

    init {

        val yearTimestampPairsOnOrAfter1970 = mutableListOf<YearTimestampPair>()
        val yearTimestampPairsBefore1970 = mutableListOf<YearTimestampPair>()
        // TODO: year range can be extended manually
        (1970..4000).forEachIndexed { index, it ->
            yearTimestampPairsOnOrAfter1970 += YearTimestampPair(
                year = it,
                timestampMs = if (index == 0) 0 else (yearTimestampPairsOnOrAfter1970.last().timestampMs +
                        numOfDaysInYear(it - 1) * KFixedTimeUnit.Day.ratioToMillis)
            )
        }
        (1969 downTo 1753).forEachIndexed { index, it ->
            yearTimestampPairsBefore1970 += YearTimestampPair(
                year = it,
                timestampMs = (if (index == 0) 0 else yearTimestampPairsBefore1970.last().timestampMs) -
                        numOfDaysInYear(it) * KFixedTimeUnit.Day.ratioToMillis
            )
        }
        val yearTimestampPairs = yearTimestampPairsBefore1970.reversed() + yearTimestampPairsOnOrAfter1970
        YEAR_TO_TIMESTAMP_MS_LIST = yearTimestampPairs
        YEAR_TO_TIMESTAMP_MS_MAP = yearTimestampPairs.associate { it.year to it.timestampMs }
    }

    private fun accumulateList(a : List<Int>): List<Int> {
        val r = mutableListOf(0)
        a.forEach {
            r += r.last() + it
        }
        return r
    }

    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0) && ((year % 400 == 0) || (year % 100 != 0))
    }

    fun numOfDaysInYear(year: Int): Int {
        return 365 + if (isLeapYear(year)) 1 else 0
    }

    private fun <T> binarySearchIndexForInclusiveStartExclusiveEnd(data: List<T>, comparator: (data: T) -> Int): Int {
        val search = data.binarySearch { comparator(it) }
        return if (search >= 0) {
            search
        } else {
            -(search + 1) - 1
        }
    }

    internal fun utcDateFromTimestamp(timestampMs: Long): KDate {
        val index = binarySearchIndexForInclusiveStartExclusiveEnd(YEAR_TO_TIMESTAMP_MS_LIST) {
            it.timestampMs.compareTo(timestampMs)
        }
        if (index < 0 || index >= YEAR_TO_TIMESTAMP_MS_LIST.size - 1) { // year 3000+ is not supported
            throw UnsupportedOperationException("This timestamp is not in the supported range")
        }
        val year = YEAR_TO_TIMESTAMP_MS_LIST[index].year

        var remainTimestampMs = timestampMs - YEAR_TO_TIMESTAMP_MS_LIST[index].timestampMs
        val daysInTheYear = remainTimestampMs / KFixedTimeUnit.Day.ratioToMillis
        val isLeapYear = isLeapYear(year)
        val monthLookupList = if (isLeapYear) ACCUMULATED_DAYS_PER_MONTH_IN_LEAP_YEAR else ACCUMULATED_DAYS_PER_MONTH_IN_NON_LEAP_YEAR
        val monthIndex = binarySearchIndexForInclusiveStartExclusiveEnd(monthLookupList) {
            it.compareTo(daysInTheYear)
        }
        val month = monthIndex + 1

        remainTimestampMs -= monthLookupList[monthIndex] * KFixedTimeUnit.Day.ratioToMillis
        val dayInMonth = (remainTimestampMs / KFixedTimeUnit.Day.ratioToMillis).toInt() + 1

        return KDate(year = year, month = month, day = dayInMonth)
    }

    fun validateDate(year: Int, month: Int, day: Int) {
        if (year < 1) {
            throw UnsupportedOperationException("Years earlier than AD 1 are not supported.")
        }
        if (month !in 1..12) {
            throw IllegalArgumentException("`month` must be within 1 ~ 12.")
        }
        val numDaysInTheMonth = if (isLeapYear(year)) {
            DAYS_PER_MONTH_IN_LEAP_YEAR[month - 1]
        } else {
            DAYS_PER_MONTH_IN_NON_LEAP_YEAR[month - 1]
        }
        if (day !in 1..numDaysInTheMonth) {
            throw IllegalArgumentException("`day` must be within 1 ~ $numDaysInTheMonth for this month.")
        }
        if (year < 1752 || (year == 1752 && month < 9 || (month == 9 && day < 14))) {
            "Warning: the dates are inaccurate to be used with the Gregorian calendar. See https://www.timeanddate.com/calendar/julian-gregorian-switch.html"
        }
    }

    internal fun kZonedInstantFromLocalDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Int = 0, zoneOffset: KZoneOffset): KZonedInstant {
        if (!(1..12).contains(month)) {
            throw IllegalArgumentException("`month` must be 1 ~ 12.")
        }
        // TODO: validate all fields

        val monthLookupList = if (isLeapYear(year)) ACCUMULATED_DAYS_PER_MONTH_IN_LEAP_YEAR else ACCUMULATED_DAYS_PER_MONTH_IN_NON_LEAP_YEAR
        val msWithoutTimeZoneAdjustment = YEAR_TO_TIMESTAMP_MS_MAP[year]!! +
                (monthLookupList[month - 1] + (day - 1)) * KFixedTimeUnit.Day.ratioToMillis +
                hour * KFixedTimeUnit.Hour.ratioToMillis +
                minute * KFixedTimeUnit.Minute.ratioToMillis +
                second * KFixedTimeUnit.Second.ratioToMillis +
                millisecond
        val offsetMs = zoneOffset.toMilliseconds()
        val timestamp = msWithoutTimeZoneAdjustment - offsetMs
        return KZonedInstant(timestampMs = timestamp, zoneOffset = zoneOffset)
    }

}
