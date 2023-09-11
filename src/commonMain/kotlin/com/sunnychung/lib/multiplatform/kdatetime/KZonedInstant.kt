package com.sunnychung.lib.multiplatform.kdatetime

open class KZonedInstant(timestampMs: Long, val zoneOffset: KZoneOffset) : KInstant(timestampMs) {

    constructor(timestampMs: Long, zoneOffsetMs: Long) : this(
        timestampMs = timestampMs,
        zoneOffset = KZoneOffset.fromMilliseconds(zoneOffsetMs)
    )

    constructor(timestampMs: Long, zoneOffsetString: String) : this(
        timestampMs = timestampMs,
        zoneOffset = KZoneOffset.parseFrom(zoneOffsetString)
    )

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
}
