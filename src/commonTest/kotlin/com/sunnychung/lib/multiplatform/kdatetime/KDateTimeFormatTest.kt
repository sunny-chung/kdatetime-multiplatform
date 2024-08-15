package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KDateTimeFormatTest {
    @Test
    fun formatInstant() {
        val dateTime = KInstant(1694400859230) // Monday, September 11, 2023 2:54:19.230 AM
        assertEquals("2023/09/11 2:54:19.23 AM", dateTime.format("yyyy/MM/dd h:mm:ss.l AA"))
        assertEquals("23-09-11 02:54:19am", dateTime.format("yy-MM-dd hh:mm:ssaa"))

        assertEquals("2023-09-11T02:54:19Z", KDateTimeFormat.ISO8601_DATETIME.format(dateTime))
        assertEquals("2023-09-11T02:54:19.230Z", dateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ"))

        val dateTime2 = KInstant(1694433600000) // Monday, September 11, 2023 12:00:00 PM
        assertEquals("2023/9/11 12:00:00.00 pm", dateTime2.format("yyyy/M/d h:mm:ss.ll aa"))

        val dateTime3 = KInstant(1694455980000) // Monday, September 11, 2023 6:13:00 PM
        assertEquals("2023-09-11T18:13:00.000Z", dateTime3.format("yyyy-MM-dd'T'HH:mm:ss.lllZ"))
        assertEquals("2023-09-11T18:13:00.000+00:00", dateTime3.format("yyyy-MM-dd'T'HH:mm:ss.lllz"))
    }

    @Test
    fun formatZonedInstant() {
        val dateTime = KZonedInstant(1694404171789, "+08:00") // Monday, September 11, 2023 11:49:31.789 AM GMT+08:00
        assertEquals("2023-09-11T11:49:31.789+08:00", dateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ"))
        assertEquals("2023-09-11T11:49:31.789+08:00", dateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllz"))

        val dateTime2 = KZonedInstant(1694391533000, "-07:00") // 2023-09-10 17:18:53 -07:00
        assertEquals("2023-09-10T17:18:53-07:00", KDateTimeFormat.ISO8601_DATETIME.format(dateTime2))

        val dateTime3 = KZonedInstant(1694360033000, "+13:45") // 2023-09-11T05:18:53+13:45
        assertEquals("2023-09-11T05:18:53+13:45", KDateTimeFormat.ISO8601_DATETIME.format(dateTime3))

        val dateTime4 = KZonedInstant(1694404171789, "UTC") // Monday, September 11, 2023 3:49:31.789 AM GMT
        assertEquals("2023-09-11T03:49:31.789Z", dateTime4.format("yyyy-MM-dd'T'HH:mm:ss.lllZ"))
        assertEquals("2023-09-11T03:49:31.789+00:00", dateTime4.format("yyyy-MM-dd'T'HH:mm:ss.lllz"))
    }

    @Test
    fun formatDuration() {
        val duration = KDuration.of(1, KFixedTimeUnit.Hour) +
                KDuration.of(2, KFixedTimeUnit.Minute) +
                KDuration.of(34, KFixedTimeUnit.Second) +
                KDuration.of(567, KFixedTimeUnit.MilliSecond)
        assertEquals("1:02:34.567", duration.format("H:mm:ss.lll"))
    }

    @Test
    fun parseDateTimeToKZonedInstant() {
        KDateTimeFormat.FULL.parseToKZonedDateTime("2023-09-11T11:49:31.789+08:00").toKZonedInstant().let { dateTime ->
            assertEquals(1694404171789, dateTime.toMilliseconds())
            assertEquals(8, dateTime.zoneOffset.hours)
            assertEquals(0, dateTime.zoneOffset.minutes)
        }

        KDateTimeFormat.ISO8601_DATETIME.parseToKZonedDateTime("2023-09-10T17:18:53-07:00").toKZonedInstant().let { dateTime ->
            assertEquals(1694391533000, dateTime.toMilliseconds())
            assertEquals(-7, dateTime.zoneOffset.hours)
            assertEquals(0, dateTime.zoneOffset.minutes)
        }

        KDateTimeFormat.ISO8601_DATETIME.parseToKZonedDateTime("2023-09-11T05:18:53+13:45").toKZonedInstant().let { dateTime ->
            assertEquals(1694360033000, dateTime.toMilliseconds())
            assertEquals(13, dateTime.zoneOffset.hours)
            assertEquals(45, dateTime.zoneOffset.minutes)
        }

        KDateTimeFormat("yy-MM-dd hh:mm:ss.lllaaZ").parseToKZonedDateTime("23-09-11 02:54:19.230amZ").toKZonedInstant().let { dateTime ->
            assertEquals(1694400859230, dateTime.toMilliseconds())
            assertEquals(0, dateTime.zoneOffset.hours)
            assertEquals(0, dateTime.zoneOffset.minutes)
        }

        KDateTimeFormat("yy-MM-dd hh:mm:ssaaZ").parseToKZonedDateTime("23-09-11 02:54:19pmUTC").toKZonedInstant().let { dateTime ->
            assertEquals(1694444059000, dateTime.toMilliseconds())
            assertEquals(0, dateTime.zoneOffset.hours)
            assertEquals(0, dateTime.zoneOffset.minutes)
        }
    }

    @Test
    fun parseDateTimeToKInstant() {
        KDateTimeFormat.FULL.parseToKZonedDateTime("2023-09-11T11:49:31.789+08:00").toKInstant().let { dateTime ->
            assertEquals(1694404171789, dateTime.toMilliseconds())
        }
    }

    @Test
    fun cannotParseWithoutEnoughFields() {
        assertFailsWith(IllegalArgumentException::class) {
            KDateTimeFormat(pattern = "mm:ss").parseToKZonedDateTime("anything")
        }
        assertFailsWith(IllegalArgumentException::class) {
            KDateTimeFormat(pattern = "yyyy-MM-dd").parseToKZonedDateTime("anything")
        }
        assertFailsWith(IllegalArgumentException::class) {
            KDateTimeFormat(pattern = "yyyy-MM-dd hh:ss.lll").parseToKZonedDateTime("anything")
        }
        assertFailsWith(IllegalArgumentException::class) {
            KDateTimeFormat(pattern = "MM-dd hh:mm:ss.lll").parseToKZonedDateTime("anything")
        }
    }

    @Test
    fun parseIncorrectDateTimeThrowError() {
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat.FULL.parseToKZonedDateTime("2023-02-29T01:23:45.678+08:00")
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat.FULL.parseToKZonedDateTime("2023-04-31T01:23:45.678+08:00")
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat.FULL.parseToKZonedDateTime("1900-02-29T01:23:45.678+08:00")
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat.FULL.parseToKZonedDateTime("2023-04-30T24:00:00.000+08:00")
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat.FULL.parseToKZonedDateTime("2023-04-30T23:60:00.000+08:00")
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat.FULL.parseToKZonedDateTime("2023-04-30T23:00:60.000+08:00")
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat.FULL.parseToKZonedDateTime("2023-04-30T23:00:00.1000+08:00")
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat.FULL.parseToKZonedDateTime("2023-04-30T23:00:00.-12+08:00")
        }
    }

    @Test
    fun parseAmbiguousFormat() {
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat("yy-MM-dd hh:mm:ss.lllaaZ").parseToKZonedDateTime("23-09--1 02:54:19.230amZ")
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat("yy-MM-dd hh:mm:ss.lllaaZ").parseToKZonedDateTime("23-09-01 2:54:19.230amZ")
        }

        KDateTimeFormat("yyMMddhhmmsslllaaZ").parseToKZonedDateTime("230911025419230amZ").toKZonedInstant().let { dateTime ->
//            println(dateTime)
            assertEquals(1694400859230, dateTime.toMilliseconds())
        }
        assertFailsWith(ParseDateTimeException::class) {
            KDateTimeFormat("yyMMddhhmmsslllaaZ").parseToKZonedDateTime("23091102541923amZ")
        }
    }

    @Test
    fun formatDayOfWeek() {
        val dateTime = KInstant(1705677172000) // Friday, January 19, 2024 3:12:52 PM GMT
        assertEquals("5", dateTime.format("e"))
        val formatter = KDateTimeFormat("E")
        assertEquals("Fri", formatter.format(dateTime))
        formatter.weekDayNames = listOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        assertEquals("星期五", formatter.format(dateTime))
    }

    @Test
    fun formatAmPm() {
        val dateTime = KInstant(1723698748231) // Thursday, August 15, 2024 5:12:28 AM GMT
        assertEquals("am", dateTime.format("aa"))
        assertEquals("AM", dateTime.format("AA"))
        val dateTime2 = KInstant(1723582800000) // Tuesday, August 13, 2024 9:00:00 PM GMT
        assertEquals("pm", dateTime2.format("aa"))
        assertEquals("PM", dateTime2.format("AA"))

        val formatter = KDateTimeFormat("aa AA")
        formatter.ampmNames = listOf("上午", "下午")
        formatter.ampmNamesCaps = listOf("오전", "오후")

        assertEquals("上午 오전", formatter.format(dateTime))
        assertEquals("下午 오후", formatter.format(dateTime2))
    }
}
