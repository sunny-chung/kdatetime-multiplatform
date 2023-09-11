package com.sunnychung.lib.multiplatform.kdatetime

open class KDuration internal constructor(val millis: Long) : KDateTimeFormattable {

    override fun toMilliseconds(): Long = millis

    fun toTimeUnitValue(targetUnit: KFixedTimeUnit): Long {
        return millis / targetUnit.ratioToMillis
    }

    operator fun plus(other: KDuration): KDuration {
        return KDuration(millis + other.millis)
    }

    companion object {
        fun of(value: Int, unit: KFixedTimeUnit) = KDuration(value * unit.ratioToMillis)
        fun of(value: Long, unit: KFixedTimeUnit) = KDuration(value * unit.ratioToMillis)

        val ZERO = KDuration(0)
    }
}
