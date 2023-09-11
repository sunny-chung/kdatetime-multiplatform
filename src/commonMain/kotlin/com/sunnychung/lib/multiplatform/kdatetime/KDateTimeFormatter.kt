package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.math.absoluteValue

/**
 * Formats:
 *
 * Symbol   Meaning             Examples
 * ------   -------             --------
 * y        year                2023; 23
 * M        month               7; 07
 * d        day of month        9; 09; 31
 *
 * H        hour (0~23)         0; 00; 18
 * h        hour (1~12)         6; 06; 10
 * m        minute              4; 04; 58
 * s        second              5; 05; 59
 * l        millisecond         12; 120; 123
 *
 * aa       am/pm               am; pm
 * AA       AM/PM               AM; PM
 *
 * Z        Time zone offset    +08:00
 *
 * '        Literal         '
 */
class KDateTimeFormatter(pattern: String) {

    enum class FormatTokenType(val hasLengthLimit: Boolean = true, val allowedLengths: List<Int> = listOf()) {
        Literial(hasLengthLimit = false),
        Year(allowedLengths = listOf(2, 4)),
        Month(allowedLengths = listOf(1, 2)),
        DayOfMonth(allowedLengths = listOf(1, 2)),
        Hour_0_23(allowedLengths = listOf(1, 2)),
        Hour_1_12(allowedLengths = listOf(1, 2)),
        Minute(allowedLengths = listOf(1, 2)),
        Second(allowedLengths = listOf(1, 2)),
        Millisecond(allowedLengths = listOf(1, 2, 3)),
        ampm(allowedLengths = listOf(2)),
        AMPM(allowedLengths = listOf(2)),
        TimeZone(allowedLengths = listOf(1))
    }

    data class FormatToken(val type: FormatTokenType, val length: Int, val literal: String? = null)

    val tokens: List<FormatToken>

    init {
        val tokens = mutableListOf<FormatToken>()
        var isConsumingLiteral = false
        var literal = StringBuilder()
        var type: FormatTokenType? = null
        var prevType: FormatTokenType? = null
        var length = 0

        fun addPrevToken() {
            prevType?.let { prevType ->
                if (prevType.hasLengthLimit && !prevType.allowedLengths.contains(length)) {
                    throw IllegalArgumentException("Invalid length of the token type $prevType. Allowed length: ${prevType.allowedLengths}; actual length: $length")
                }
                val token = if (prevType == FormatTokenType.Literial) {
                    FormatToken(type = prevType, length = literal.length, literal = literal.toString())
                } else {
                    FormatToken(type = prevType, length = length)
                }
                tokens += token
            }
        }

        pattern.forEach {
            var ignoreCurrentChar = false

            if (isConsumingLiteral && it != '\'') {
                type = FormatTokenType.Literial
            } else if (it == '\'') {
                type = FormatTokenType.Literial
                isConsumingLiteral = !isConsumingLiteral
                ignoreCurrentChar = true
            } else {
                type = when (it) {
                    'y' -> FormatTokenType.Year
                    'M' -> FormatTokenType.Month
                    'd' -> FormatTokenType.DayOfMonth
                    'H' -> FormatTokenType.Hour_0_23
                    'h' -> FormatTokenType.Hour_1_12
                    'm' -> FormatTokenType.Minute
                    's' -> FormatTokenType.Second
                    'l' -> FormatTokenType.Millisecond
                    'a' -> FormatTokenType.ampm
                    'A' -> FormatTokenType.AMPM
                    'Z' -> FormatTokenType.TimeZone
                    else -> FormatTokenType.Literial
                }
            }

            if (type == prevType) {
                ++length
            } else {
                addPrevToken()
                prevType = type
                literal.clear()
                length = 1
            }
            if (!ignoreCurrentChar && type == FormatTokenType.Literial) {
                literal.append(it)
            }
        }
        prevType = type
        addPrevToken()

        this.tokens = tokens.toList()
    }

    fun format(datetime: KDateTimeFormattable): String {
        val localDateTime = if (datetime is KZonedInstant) {
            datetime + KDuration.of(datetime.zoneOffset.toMilliseconds(), KFixedTimeUnit.MilliSecond)
        } else {
            datetime
        }
        val localDate by lazy {
            KGregorianCalendar.utcDateFromTimestamp(localDateTime.toMilliseconds())
        }

        val s = StringBuilder()
        tokens.forEach {
            when (it.type) {
                FormatTokenType.Literial -> s.append(it.literal)
                FormatTokenType.Year -> s.append(localDate.year.toString().substring(if (it.length == 2) 2 else 0))
                FormatTokenType.Month -> s.append(localDate.month.toString().padStart(it.length, '0'))
                FormatTokenType.DayOfMonth -> s.append(localDate.day.toString().padStart(it.length, '0'))
                FormatTokenType.Hour_0_23 -> s.append(localDateTime.hourPart().toString().padStart(it.length, '0'))
                FormatTokenType.Hour_1_12 -> {
                    val halfHour = localDateTime.hourPart() % 12
                    s.append((if (halfHour == 0) 12 else halfHour).toString().padStart(it.length, '0'))
                }
                FormatTokenType.Minute -> s.append(localDateTime.minutePart().toString().padStart(it.length, '0'))
                FormatTokenType.Second -> s.append(localDateTime.secondPart().toString().padStart(it.length, '0'))
                FormatTokenType.Millisecond -> s.append(localDateTime.millisecondPart().toString().padStart(3, '0').trimEnd('0').padEnd(it.length, '0'))
                FormatTokenType.ampm -> s.append(if (localDateTime.hourPart() < 12) "am" else "pm")
                FormatTokenType.AMPM -> s.append(if (localDateTime.hourPart() < 12) "AM" else "PM")
                FormatTokenType.TimeZone -> when (datetime) {
                    is KZonedInstant -> s.append(datetime.zoneOffset.toDisplayString())
                    is KInstant -> s.append("Z")
                    else -> throw UnsupportedOperationException("${datetime::class.simpleName} has no timezone support")
                }
            }
        }
        return s.toString()
    }

    companion object {
        val ISO8601_DATETIME = KDateTimeFormatter("yyyy-MM-dd'T'HH:mm:ssZ")
        val FULL = KDateTimeFormatter("yyyy-MM-dd'T'HH:mm:ss.lllZ")
    }
}
