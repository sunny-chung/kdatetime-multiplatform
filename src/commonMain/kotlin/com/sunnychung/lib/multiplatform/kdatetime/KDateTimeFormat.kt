package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.KGregorianCalendar.dayOfWeek

/**
 * Formats:
 *
 * Symbol   Meaning                     Examples
 * ------   -------                     --------
 * y        year                        2023; 23
 * M        month                       7; 07
 * d        day of month                9; 09; 31
 *
 * H        hour (0~23)                 0; 00; 18
 * h        hour (1~12)                 6; 06; 10
 * m        minute                      4; 04; 58
 * s        second                      5; 05; 59
 * l        millisecond                 12; 120; 123
 *
 * aa       am/pm                       am; pm
 * AA       AM/PM                       AM; PM
 * e        Day of week. 0=Sun, 6=Sat.  1
 * E        Day of week.                Mon
 *
 * Z        Time zone offset or 'Z'     Z; +08:00
 * z        Time zone offset            +00:00; +08:00
 *
 * '        Literal         '
 */
class KDateTimeFormat(val pattern: String) {

    var weekDayNames = WEEKDAY_NAMES
        set(value) {
            if (value.size != 7) throw IllegalArgumentException("`weekDayNames` must be a list of 7 string elements")
            field = value.toList()
        }

    protected var ampmUppercaseNames = AMPM_UPPERCASE_NAMES

    protected var ampmLowercaseNames = AMPM_LOWERCASE_NAMES

    fun setAmPmNames(amLowercase: String, pmLowercase: String, amUppercase: String = amLowercase.uppercase(), pmUppercase: String = pmLowercase.uppercase()) {
        ampmLowercaseNames = listOf(amLowercase, pmLowercase)
        ampmUppercaseNames = listOf(amUppercase, pmUppercase)
    }

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
        ampm(hasLengthLimit = false),
        AMPM(hasLengthLimit = false),
        DayOfWeekNumber(allowedLengths = listOf(1)),
        DayOfWeek(allowedLengths = listOf(1)),
        TimeZoneOffsetOrZ(allowedLengths = listOf(1)),
        TimeZoneOffset(allowedLengths = listOf(1)),
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
                } else if (prevType == FormatTokenType.AMPM || prevType == FormatTokenType.ampm) {
                    FormatToken(type = prevType, length = literal.length)
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
                    'e' -> FormatTokenType.DayOfWeekNumber
                    'E' -> FormatTokenType.DayOfWeek
                    'Z' -> FormatTokenType.TimeZoneOffsetOrZ
                    'z' -> FormatTokenType.TimeZoneOffset
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
            if (type == FormatTokenType.ampm || type == FormatTokenType.AMPM) {
                literal.append(it)
            }
        }
        prevType = type
        addPrevToken()

        this.tokens = tokens.toList()
    }

    fun format(datetime: KDateTimeFormattable): String {
        val localDateTime = datetime
        val localDate by lazy {
            if (datetime is KZonedInstant) {
                datetime.datePart()
            } else {
                KGregorianCalendar.utcDateFromTimestamp(localDateTime.toMilliseconds())
            }
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
                FormatTokenType.ampm -> s.append(ampmLowercaseNames[if (localDateTime.hourPart() < 12) 0 else 1])
                FormatTokenType.AMPM -> s.append(ampmUppercaseNames[if (localDateTime.hourPart() < 12) 0 else 1])
                FormatTokenType.DayOfWeekNumber -> s.append(localDate.dayOfWeek())
                FormatTokenType.DayOfWeek -> s.append(weekDayNames[localDate.dayOfWeek()])
                FormatTokenType.TimeZoneOffsetOrZ -> when (datetime) {
                    is KZonedInstant -> s.append(datetime.zoneOffset.toDisplayString())
                    is KInstant -> s.append("Z")
                    else -> throw UnsupportedOperationException("${datetime::class.simpleName} has no timezone support")
                }
                FormatTokenType.TimeZoneOffset -> when (datetime) {
                    is KZonedInstant -> s.append(datetime.zoneOffset.toDisplayString(isDisplayZ = false))
                    is KInstant -> s.append("+00:00")
                    else -> throw UnsupportedOperationException("${datetime::class.simpleName} has no timezone support")
                }
            }
        }
        return s.toString()
    }

    protected fun validateForFullValidParser() {
        val tokenTypesList = tokens.map { it.type }
            .filter { it != FormatTokenType.Literial }
        val tokenTypesSet = tokenTypesList.toSet()

        if (tokenTypesSet.size != tokenTypesList.size) {
            throw IllegalArgumentException("A parser cannot have repeated fields")
        }

        if (!tokenTypesSet.containsAll(PARSER_COMPULSORY_INPUT_TYPES)) {
            throw IllegalArgumentException("Missing compulsory fields in the pattern")
        }

        if (tokenTypesSet.contains(FormatTokenType.Hour_0_23)) {
            if (tokenTypesSet.contains(FormatTokenType.Hour_1_12) || tokenTypesSet.contains(FormatTokenType.AMPM) || tokenTypesSet.contains(FormatTokenType.ampm)) {
                throw IllegalArgumentException("Cannot contain contradicting fields in the pattern")
            }
        } else if (tokenTypesSet.contains(FormatTokenType.Hour_1_12)) {
            if (!tokenTypesSet.contains(FormatTokenType.AMPM) && !tokenTypesSet.contains(FormatTokenType.ampm)) {
                throw IllegalArgumentException("Missing compulsory fields in the pattern")
            }
        } else {
            throw IllegalArgumentException("Missing compulsory fields in the pattern")
        }
    }

    protected fun parseAmPmLowercase(input: String): Int {
        return when (input) {
            ampmLowercaseNames[0] -> AM
            ampmLowercaseNames[1] -> PM
            else -> throw ParseDateTimeException()
        }
    }

    protected fun parseAmPmUppercase(input: String): Int {
        return when (input) {
            ampmUppercaseNames[0] -> AM
            ampmUppercaseNames[1] -> PM
            else -> throw ParseDateTimeException()
        }
    }

    fun parseToKZonedDateTime(input: String): KZonedDateTime {
        validateForFullValidParser()

        var year: Int? = null
        var month: Int? = null
        var dayOfMonth: Int? = null
        var hour: Int? = null
        var minute: Int? = null
        var second: Int? = null
        var millisecond: Int? = null
        var amPm: Int? = null // am = 0, pm = 1
        var zoneOffset: KZoneOffset? = null

        var startIndex = 0
        tokens.forEach { token ->
            val inputSubstring = input.substring(startIndex, startIndex + token.length)
            var length = token.length
            if (token.type == FormatTokenType.Literial) {
                if (inputSubstring != token.literal!!) {
                    throw ParseDateTimeException()
                }
                startIndex += length
                return@forEach // skip because no field to input
            }

            try {
                when (token.type) {
                    FormatTokenType.Year -> {
                        year = inputSubstring.toInt()
                        if (token.length == 2) {
                            year = year!! + if (year!! >= 70) 1900 else 2000
                        }
                    }

                    FormatTokenType.Month -> month = inputSubstring.toInt()
                    FormatTokenType.DayOfMonth -> dayOfMonth = inputSubstring.toInt()
                    FormatTokenType.Hour_0_23 -> hour = inputSubstring.toInt()
                    FormatTokenType.Hour_1_12 -> hour = inputSubstring.toInt()
                    FormatTokenType.Minute -> minute = inputSubstring.toInt()
                    FormatTokenType.Second -> second = inputSubstring.toInt()
                    FormatTokenType.Millisecond -> millisecond = inputSubstring.toInt()
                    FormatTokenType.ampm -> amPm = parseAmPmLowercase(inputSubstring)
                    FormatTokenType.AMPM -> amPm = parseAmPmUppercase(inputSubstring)
                    FormatTokenType.TimeZoneOffsetOrZ -> {
                        val longerSubstring = input.substring(startIndex)
                        length = if (longerSubstring.startsWith("Z")) {
                            1
                        } else if (longerSubstring.startsWith("UTC")) {
                            3
                        } else {
                            "+00:00".length
                        }
                        zoneOffset = try {
                            KZoneOffset.parseFrom(longerSubstring.substring(0, length))
                        } catch (e: IllegalArgumentException) {
                            throw ParseDateTimeException(e.message)
                        }
                    }

                    else -> {}
                }
            } catch (e: NumberFormatException) {
                throw ParseDateTimeException(e.message)
            }
            startIndex += length
        }

        if (!(1..12).contains(month)) {
            throw ParseDateTimeException()
        }

        if (amPm != null) {
            if (!(1..12).contains(hour)) {
                throw ParseDateTimeException()
            }
            if (amPm == AM) {
                if (hour == 12) hour = 0
            } else if (amPm == PM) {
                hour = (hour!! + 12) % 24
            }
        }

        return try {
            KZonedDateTime(
                year = year!!,
                month = month!!,
                day = dayOfMonth!!,
                hour = hour ?: 0,
                minute = minute ?: 0,
                second = second ?: 0,
                millisecond = millisecond ?: 0,
                zoneOffset = zoneOffset!!
            )
        } catch (e: IllegalArgumentException) {
            throw ParseDateTimeException(e.message)
        }
    }

    @Deprecated("Use parseToKZonedDateTime(String).toKZonedInstant() instead")
    fun parseToKZonedInstant(input: String): KZonedInstant {
        return parseToKZonedDateTime(input).toKZonedInstant()
    }

    @Deprecated("Use parseToKZonedDateTime(String).toKZonedInstant().dropZoneOffset() instead")
    fun parseToKInstant(input: String): KInstant {
        val instantWithZone = parseToKZonedInstant(input = input)
        return instantWithZone.dropZoneOffset()
    }

    companion object {
        val ISO8601_DATETIME = KDateTimeFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        val FULL = KDateTimeFormat("yyyy-MM-dd'T'HH:mm:ss.lllZ")

        // order of the list matters
        val ISO8601_FORMATS = listOf(FULL, ISO8601_DATETIME)
        val IOS_DATE_FORMATS = listOf(FULL, ISO8601_DATETIME, KDateTimeFormat("yyyy-MM-dd'T'HH:mmZ"))

        val WEEKDAY_NAMES = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val AMPM_UPPERCASE_NAMES = listOf("AM", "PM")
        val AMPM_LOWERCASE_NAMES = listOf("am", "pm")

        internal val PARSER_COMPULSORY_INPUT_TYPES = setOf(
            FormatTokenType.Year,
            FormatTokenType.Month,
            FormatTokenType.DayOfMonth,
            FormatTokenType.Minute,
//            FormatTokenType.Second,
            FormatTokenType.TimeZoneOffsetOrZ
        )

        private val AM = 0
        private val PM = 1
    }
}
