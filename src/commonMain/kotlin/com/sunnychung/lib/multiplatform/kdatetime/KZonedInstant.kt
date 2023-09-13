package com.sunnychung.lib.multiplatform.kdatetime

open class KZonedInstant(private val timestampMs: Long, val zoneOffset: KZoneOffset) : KDateTimeFormattable {

    constructor(timestampMs: Long, zoneOffsetMs: Long) : this(
        timestampMs = timestampMs,
        zoneOffset = KZoneOffset.fromMilliseconds(zoneOffsetMs)
    )

    constructor(timestampMs: Long, zoneOffsetString: String) : this(
        timestampMs = timestampMs,
        zoneOffset = KZoneOffset.parseFrom(zoneOffsetString)
    )

    override fun toMilliseconds(): Long = timestampMs

    override fun toString(): String {
        return "${this::class.simpleName}(${KDateTimeFormatter.FULL.format(this)})"
    }

    operator fun plus(duration: KDuration): KZonedInstant {
        return KZonedInstant(timestampMs + duration.millis, zoneOffset)
    }

    fun startOfDay(): KZonedInstant {
        return copy(hour = 0, minute = 0, second = 0, millisecond = 0)
    }

    fun copy(
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
        hour: Int? = null,
        minute: Int? = null,
        second: Int? = null,
        millisecond: Int? = null
   ): KZonedInstant {
        val localDateTime by lazy {
            KInstant(timestampMs + zoneOffset.toMilliseconds())
        }
        val localDate by lazy {
            KGregorianCalendar.utcDateFromTimestamp(localDateTime.toMilliseconds())
        }
        return KGregorianCalendar.kZonedInstantFromLocalDate(
            year = year ?: localDate.year,
            month = month ?: localDate.month,
            day = day ?: localDate.day,
            hour = hour ?: localDateTime.hourPart(),
            minute = minute ?: localDateTime.minutePart(),
            second = second ?: localDateTime.secondPart(),
            millisecond = millisecond ?: localDateTime.millisecondPart(),
            zoneOffset = zoneOffset
        )
    }

    companion object {
        fun nowAtLocalZoneOffset(): KZonedInstant = KZonedInstant(
            timestampMs = KInstant.now().toMilliseconds(),
            zoneOffset = KZoneOffset.local()
        )
    }
}
