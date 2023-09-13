package com.sunnychung.lib.multiplatform.kdatetime

open class KInstant(private val timestampMs: Long) : KDateTimeFormattable {

    operator fun plus(duration: KDuration): KInstant {
        return KInstant(timestampMs + duration.millis)
    }

    override fun toMilliseconds(): Long {
        return timestampMs
    }

    override fun toString(): String {
        return "${this::class.simpleName}(${KDateTimeFormatter.FULL.format(this)})"
    }

    fun atZoneOffset(zoneOffset: KZoneOffset): KZonedInstant {
        return KZonedInstant(timestampMs = timestampMs, zoneOffset = zoneOffset)
    }

    companion object {
        fun now(): KInstant = kInstantOfCurrentTime()
    }
}

internal expect fun kInstantOfCurrentTime(): KInstant
