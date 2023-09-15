package com.sunnychung.lib.multiplatform.kdatetime

abstract class KPointOfTime : Comparable<KPointOfTime> {
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
}