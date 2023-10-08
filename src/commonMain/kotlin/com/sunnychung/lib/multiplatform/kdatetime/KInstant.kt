package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.annotation.AndroidParcelize
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantSerializer
import kotlinx.serialization.Serializable

@Serializable(with = KInstantSerializer::class)
@AndroidParcelize
open class KInstant(private val timestampMs: Long) : KDateTimeFormattable, KPointOfTime(), AndroidParcelable {

    operator fun plus(duration: KDuration): KInstant {
        return KInstant(timestampMs + duration.millis)
    }

    operator fun minus(duration: KDuration): KInstant {
        return KInstant(timestampMs - duration.millis)
    }

    override fun toMilliseconds(): Long {
        return timestampMs
    }

    override fun toEpochMilliseconds(): Long {
        return timestampMs
    }

    override fun toString(): String {
        return "${this::class.simpleName}(${KDateTimeFormat.FULL.format(this)})"
    }

    fun atZoneOffset(zoneOffset: KZoneOffset): KZonedInstant {
        return KZonedInstant(timestampMs = timestampMs, zoneOffset = zoneOffset)
    }

    override fun hashCode(): Int {
        return timestampMs.hashCode()
    }


    companion object {
        fun now(): KInstant = kInstantOfCurrentTime()

        /**
         * @param formats Order matters. Formats are tried one by one.
         * @throws ParseDateTimeException
         */
        fun parseFrom(input: String, formats: List<KDateTimeFormat>): KInstant {
            formats.forEach { format ->
                try {
                    return format.parseToKInstant(input = input)
                } catch (e: ParseDateTimeException) { /* ignore */ }
            }
            throw ParseDateTimeException("$input cannot be parsed")
        }
    }
}

internal expect fun kInstantOfCurrentTime(): KInstant
