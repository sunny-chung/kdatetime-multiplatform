package com.sunnychung.lib.multiplatform.kdatetime

open class KInstant(private val timestampMs: Long) : KDateTimeFormattable, KPointOfTime() {

    operator fun plus(duration: KDuration): KInstant {
        return KInstant(timestampMs + duration.millis)
    }

    override fun toMilliseconds(): Long {
        return timestampMs
    }

    override fun toEpochMilliseconds(): Long {
        return timestampMs
    }

    override fun toString(): String {
        return "${this::class.simpleName}(${KDateTimeFormatter.FULL.format(this)})"
    }

    fun atZoneOffset(zoneOffset: KZoneOffset): KZonedInstant {
        return KZonedInstant(timestampMs = timestampMs, zoneOffset = zoneOffset)
    }

    override fun hashCode(): Int {
        return timestampMs.hashCode()
    }


    companion object {
        fun now(): KInstant = kInstantOfCurrentTime()
    }
}

internal expect fun kInstantOfCurrentTime(): KInstant
