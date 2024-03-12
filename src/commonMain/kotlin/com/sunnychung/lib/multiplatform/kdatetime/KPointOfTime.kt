package com.sunnychung.lib.multiplatform.kdatetime

abstract class KPointOfTime : Comparable<KPointOfTime>, KDateTimeFormattable {
    abstract fun toEpochMilliseconds(): Long

    override fun compareTo(other: KPointOfTime): Int {
        return toEpochMilliseconds().compareTo(other.toEpochMilliseconds())
    }

    override fun equals(other: Any?): Boolean {
        if (other is KPointOfTime) {
            return toEpochMilliseconds() == other.toEpochMilliseconds()
        }
        return false
    }

    operator fun minus(other: KPointOfTime): KDuration {
        return KDuration(this.toEpochMilliseconds() - other.toEpochMilliseconds())
    }

    fun toIso8601String(): String {
        return KDateTimeFormat.ISO8601_DATETIME.format(this)
    }

    fun toIso8601StringWithMilliseconds(): String {
        return KDateTimeFormat.FULL.format(this)
    }
}
