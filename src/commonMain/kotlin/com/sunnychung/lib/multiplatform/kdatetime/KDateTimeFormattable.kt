package com.sunnychung.lib.multiplatform.kdatetime

interface KDateTimeFormattable {
    fun toMilliseconds(): Long

    fun hourPart(): Int {
        val ms = toMilliseconds()
        val timePartMs = ms % KFixedTimeUnit.Day.ratioToMillis
        return (timePartMs / KFixedTimeUnit.Hour.ratioToMillis).toInt()
    }

    fun minutePart(): Int {
        val ms = toMilliseconds()
        val timePartMs = ms % KFixedTimeUnit.Hour.ratioToMillis
        return (timePartMs / KFixedTimeUnit.Minute.ratioToMillis).toInt()
    }

    fun secondPart(): Int {
        val ms = toMilliseconds()
        val timePartMs = ms % KFixedTimeUnit.Minute.ratioToMillis
        return (timePartMs / KFixedTimeUnit.Second.ratioToMillis).toInt()
    }

    fun millisecondPart(): Int {
        val ms = toMilliseconds()
        val timePartMs = ms % KFixedTimeUnit.Second.ratioToMillis
        return timePartMs.toInt()
    }

    fun format(pattern: String) = KDateTimeFormat(pattern).format(this)
}
