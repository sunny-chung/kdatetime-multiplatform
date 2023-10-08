package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.annotation.AndroidParcelize
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KZonedInstantSerializer
import kotlinx.serialization.Serializable

@Serializable(with = KZonedInstantSerializer::class)
@AndroidParcelize
open class KZonedInstant(private val timestampMs: Long, val zoneOffset: KZoneOffset) : KDateTimeFormattable, KPointOfTime(), AndroidParcelable {

    constructor(timestampMs: Long, zoneOffsetMs: Long) : this(
        timestampMs = timestampMs,
        zoneOffset = KZoneOffset.fromMilliseconds(zoneOffsetMs)
    )

    constructor(timestampMs: Long, zoneOffsetString: String) : this(
        timestampMs = timestampMs,
        zoneOffset = KZoneOffset.parseFrom(zoneOffsetString)
    )

    override fun toMilliseconds(): Long = timestampMs

    override fun toEpochMilliseconds(): Long = timestampMs

    override fun toString(): String {
        return "${this::class.simpleName}(${KDateTimeFormat.FULL.format(this)})"
    }

    operator fun plus(duration: KDuration): KZonedInstant {
        return KZonedInstant(timestampMs + duration.millis, zoneOffset)
    }

    fun startOfDay(): KZonedInstant {
        return toKZonedDateTime().startOfDay().toKZonedInstant()
    }

    internal fun offsetedInstant(): KInstant = KInstant(timestampMs + zoneOffset.toMilliseconds())

    @Deprecated("Use `toKZonedDateTime().datePart()` instead")
    fun datePart(): KDate = KGregorianCalendar.utcDateFromTimestamp(offsetedInstant().toMilliseconds())

    override fun hourPart(): Int {
        return offsetedInstant().hourPart()
    }

    override fun minutePart(): Int {
        return offsetedInstant().minutePart()
    }

    fun dropZoneOffset(): KInstant {
        return KInstant(timestampMs)
    }

    override fun hashCode(): Int {
        var result = timestampMs.hashCode()
        result = 31 * result + zoneOffset.hashCode()
        return result
    }

    companion object {
        fun nowAtLocalZoneOffset(): KZonedInstant = nowAtZoneOffset(zoneOffset = KZoneOffset.local())

        fun nowAtZoneOffset(zoneOffset: KZoneOffset): KZonedInstant = KZonedInstant(
            timestampMs = KInstant.now().toMilliseconds(),
            zoneOffset = zoneOffset
        )

        /**
         * @param formats Order matters. Formats are tried one by one.
         * @throws ParseDateTimeException
         */
        fun parseFrom(input: String, formats: List<KDateTimeFormat>): KZonedInstant {
            formats.forEach { format ->
                try {
                    return format.parseToKZonedInstant(input = input)
                } catch (e: ParseDateTimeException) { /* ignore */ }
            }
            throw ParseDateTimeException("$input cannot be parsed")
        }

        fun parseFromIso8601String(input: String): KZonedInstant {
            return KZonedInstant.parseFrom(input = input, formats = KDateTimeFormat.ISO8601_FORMATS)
        }
    }
}
