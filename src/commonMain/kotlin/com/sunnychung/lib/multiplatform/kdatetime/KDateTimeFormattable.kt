package com.sunnychung.lib.multiplatform.kdatetime

interface KDateTimeFormattable {
    fun toMilliseconds(): Long

    private infix fun Long.modPositive(y: Long): Long = (this % y).let { if (it < 0) it + y else it }

    fun hourPart(): Int {
        val ms = toMilliseconds()
        val timePartMs = ms modPositive KFixedTimeUnit.Day.ratioToMillis
        return (timePartMs / KFixedTimeUnit.Hour.ratioToMillis).toInt()
    }

    fun minutePart(): Int {
        val ms = toMilliseconds()
        val timePartMs = ms modPositive KFixedTimeUnit.Hour.ratioToMillis
        return (timePartMs / KFixedTimeUnit.Minute.ratioToMillis).toInt()
    }

    fun secondPart(): Int {
        val ms = toMilliseconds()
        val timePartMs = ms modPositive KFixedTimeUnit.Minute.ratioToMillis
        return (timePartMs / KFixedTimeUnit.Second.ratioToMillis).toInt()
    }

    fun millisecondPart(): Int {
        val ms = toMilliseconds()
        val timePartMs = ms modPositive KFixedTimeUnit.Second.ratioToMillis
        return timePartMs.toInt()
    }

    fun format(pattern: String) = KDateTimeFormat(pattern).format(this)
}
