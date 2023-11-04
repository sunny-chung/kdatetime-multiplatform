package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.annotation.AndroidParcelize
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KZoneOffsetSerializer
import kotlinx.serialization.Serializable
import kotlin.math.absoluteValue
import kotlin.math.sign

@Serializable(with = KZoneOffsetSerializer::class)
@AndroidParcelize
class KZoneOffset(val hours: Int, val minutes: Int) : AndroidParcelable {
    private val ms = hours.sign *
            (hours.absoluteValue * KFixedTimeUnit.Hour.ratioToMillis + minutes * KFixedTimeUnit.Minute.ratioToMillis)

    fun toMilliseconds(): Long = ms

    fun toDisplayString(): String {
        val s = StringBuilder()
        if (ms == 0L) {
            s.append("Z")
        } else {
            val offset = KDuration.of(ms.absoluteValue, KFixedTimeUnit.MilliSecond)
            s.append(if (ms < 0) '-' else '+')
            s.append(
                "${offset.hourPart().toString().padStart(2, '0')}:${
                    offset.minutePart().toString().padStart(2, '0')
                }"
            )
        }
        return s.toString()
    }

    override fun toString(): String {
        return toDisplayString()
    }

    companion object {
        val UTC = KZoneOffset(0, 0)

        /**
         * @param string format: "+08:00" or "-07:00" or "Z"
         */
        fun parseFrom(string: String): KZoneOffset {
            if (string == "Z" || string == "UTC") {
                return KZoneOffset(0, 0)
            }

            val hour = string.substringBefore(":").let {
                if (it[0] == '+') {
                    it.substring(1)
                } else if (it[0] != '-') {
                    throw IllegalArgumentException("The first character in zone offset should be either '+' or '-'.")
                } else {
                    it
                }
            }.toInt()
            val minute = string.substringAfter(":").toInt()
            return KZoneOffset(hours = hour, minutes = minute)
        }

        fun fromMilliseconds(millis: Long): KZoneOffset {
            val sign = if (millis >= 0) 1 else -1
            val duration = KDuration.of(millis.absoluteValue, KFixedTimeUnit.MilliSecond)
            return KZoneOffset(sign * duration.hourPart(), duration.minutePart())
        }

        fun local(): KZoneOffset = localZoneOffset()
    }
}

internal expect fun localZoneOffset(): KZoneOffset
