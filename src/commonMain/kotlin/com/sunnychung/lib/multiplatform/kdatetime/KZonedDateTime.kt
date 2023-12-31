package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.annotation.AndroidParcelize
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KZonedDateTimeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = KZonedDateTimeSerializer::class)
@AndroidParcelize
class KZonedDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val millisecond: Int = 0,
    val zoneOffset: KZoneOffset
) : AndroidParcelable {
    init {
        validateTime(hour = hour, minute = minute, second = second, millisecond = millisecond)
        // TODO: allow other calendars
        KGregorianCalendar.validateDate(year = year, month = month, day = day)
    }

    private val calendar = KGregorianCalendar

    fun toKZonedInstant(): KZonedInstant {
        return calendar.kZonedInstantFromLocalDate(
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute,
            second = second,
            millisecond = millisecond,
            zoneOffset = zoneOffset
        )
    }

    fun datePart(): KDate = KDate(year, month, day)

    fun startOfDay(): KZonedDateTime {
        return copy(hour = 0, minute = 0, second = 0, millisecond = 0)
    }

    fun copy(
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
        hour: Int? = null,
        minute: Int? = null,
        second: Int? = null,
        millisecond: Int? = null,
        zoneOffset: KZoneOffset? = null
    ): KZonedDateTime {
        return KZonedDateTime(
            year = year ?: this.year,
            month = month ?: this.month,
            day = day ?: this.day,
            hour = hour ?: this.hour,
            minute = minute ?: this.minute,
            second = second ?: this.second,
            millisecond = millisecond ?: this.millisecond,
            zoneOffset = zoneOffset ?: this.zoneOffset
        )
    }

    override fun toString(): String {
        return KDateTimeFormat.FULL.format(toKZonedInstant())
    }

    operator fun plus(duration: KDuration): KZonedDateTime {
        return (toKZonedInstant() + duration).toKZonedDateTime()
    }

    operator fun minus(duration: KDuration): KZonedDateTime {
        return (toKZonedInstant() - duration).toKZonedDateTime()
    }
}

fun KZonedInstant.toKZonedDateTime(): KZonedDateTime {
    val localDate = datePart()
    val localTime = offsetedInstant()
    return KZonedDateTime(
        year = localDate.year,
        month = localDate.month,
        day = localDate.day,
        hour = localTime.hourPart(),
        minute = localTime.minutePart(),
        second = localTime.secondPart(),
        millisecond = localTime.millisecondPart(),
        zoneOffset = zoneOffset
    )
}
