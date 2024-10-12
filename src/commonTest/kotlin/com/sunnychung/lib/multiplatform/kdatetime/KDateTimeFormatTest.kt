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
    }

    @Test
    fun parseDateTimeToKZonedInstantAmPm() {
//        KDateTimeFormat("yy-MM-dd hh:mm:ss.lll aa Z").parseToKZonedDateTime("23-09-11 02:54:19.230 am Z").toKZonedInstant().let { dateTime ->
//            assertEquals(1694400859230, dateTime.toMilliseconds())
//            assertEquals(0, dateTime.zoneOffset.hours)
//            assertEquals(0, dateTime.zoneOffset.minutes)
//        }
//
//        KDateTimeFormat("yy-MM-dd hh:mm:ss aa AA Z").parseToKZonedDateTime("23-09-11 02:54:19 pm PM UTC").toKZonedInstant().let { dateTime ->
//            assertEquals(1694444059000, dateTime.toMilliseconds())
//            assertEquals(0, dateTime.zoneOffset.hours)
//            assertEquals(0, dateTime.zoneOffset.minutes)
//        }
//
//        KDateTimeFormat("yy-MM-dd hh:mm:ss AA aa Z").apply { setAmPmNames("오전", "오후") }.parseToKZonedDateTime("23-09-11 02:54:19 오후 오후 UTC").toKZonedInstant().let { dateTime ->
//            assertEquals(1694444059000, dateTime.toMilliseconds())
//            assertEquals(0, dateTime.zoneOffset.hours)
//            assertEquals(0, dateTime.zoneOffset.minutes)
//        }
//
//        KDateTimeFormat("yy-MM-dd hh:mm:ss AA aa Z").apply { setAmPmNames("오전", "오후", "上午", "下午") }.parseToKZonedDateTime("23-09-11 02:54:19 下午 오후 UTC").toKZonedInstant().let { dateTime ->
//            assertEquals(1694444059000, dateTime.toMilliseconds())
//            assertEquals(0, dateTime.zoneOffset.hours)
//            assertEquals(0, dateTime.zoneOffset.minutes)
//        }

        KDateTimeFormat("yy-MM-dd hh:mm:ss AAAA Z")
            .apply { setAmPmNames("a.m.", "p.m.") }
            .parseToKZonedDateTime("23-09-11 02:54:19 P.M. UTC")
            .toKZonedInstant()
            .let { dateTime ->
                assertEquals(1694444059000, dateTime.toMilliseconds())
                assertEquals(0, dateTime.zoneOffset.hours)
                assertEquals(0, dateTime.zoneOffset.minutes)
            }

        KDateTimeFormat("yy-MM-dd hh:mm:ss AAAAAAAAAA Z")
            .apply { setAmPmNames("a.m.", "p.m.") }
            .parseToKZonedDateTime("23-09-11 02:54:19 P.M. UTC")
            .toKZonedInstant()
            .let { dateTime ->
                assertEquals(1694444059000, dateTime.toMilliseconds())
                assertEquals(0, dateTime.zoneOffset.hours)
                assertEquals(0, dateTime.zoneOffset.minutes)
            }

        KDateTimeFormat("yy-MM-dd hh:mm:ss AAAAAAAAAA Z")
            .apply { setAmPmNames("a.m.", "p.m.") }
            .parseToKZonedDateTime("23-09-11 02:54:19 P.M. -01:00")
            .toKZonedInstant()
            .let { dateTime ->
                assertEquals(1694447659000, dateTime.toMilliseconds())
                assertEquals(-1, dateTime.zoneOffset.hours)
                assertEquals(0, dateTime.zoneOffset.minutes)
            }

        KDateTimeFormat("yy-MM-dd hh:mm:ss AA Z")
            .apply { setAmPmNames("a.m.", "p.m.") }
            .parseToKZonedDateTime("23-09-11 02:54:19 P.M. -01:00")
            .toKZonedInstant()
            .let { dateTime ->
                assertEquals(1694447659000, dateTime.toMilliseconds())
                assertEquals(-1, dateTime.zoneOffset.hours)
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
        assertEquals("am", dateTime.format("a"))
        assertEquals("AM", dateTime.format("A"))
        val dateTime2 = KInstant(1723582800000) // Tuesday, August 13, 2024 9:00:00 PM GMT
        assertEquals("pm", dateTime2.format("a"))
        assertEquals("PM", dateTime2.format("A"))

        val formatter = KDateTimeFormat("aaaa AAAA")
        formatter.setAmPmNames("a.m.", "p.m.")
        assertEquals("a.m. A.M.", formatter.format(dateTime))
        assertEquals("p.m. P.M.", formatter.format(dateTime2))

        val formatter2 = KDateTimeFormat("aaaa AAAA")
        formatter2.setAmPmNames("u.v.", "n.v.", "K.A.", "K.P.")
        assertEquals("u.v. K.A.", formatter2.format(dateTime))
        assertEquals("n.v. K.P.", formatter2.format(dateTime2))

        val formatter3 = KDateTimeFormat("AA aa")
        formatter3.setAmPmNames("오전", "오후")
        assertEquals("오전 오전", formatter3.format(dateTime))
        assertEquals("오후 오후", formatter3.format(dateTime2))

        val formatter4 = KDateTimeFormat("AA aa")
        formatter4.setAmPmNames("a.m.", "p.m.", "AK.MK.", "PK.MK.")
        assertEquals("AK.MK. a.m.", formatter4.format(dateTime))
        assertEquals("PK.MK. p.m.", formatter4.format(dateTime2))
    }

    @Test
    fun parseAmPm() {
        val formatter = KDateTimeFormat("yyyy-MM-dd'T'hh:mm:ss.lllZaa")
        val timeStr = "2023-04-30T11:00:00.000+08:00am"
        assertEquals(1682823600000, formatter.parseToKZonedDateTime(timeStr).toKInstant().toMilliseconds())

        val formatter2 = KDateTimeFormat("yyyy-MM-dd'T'hh:mm:ss.lllZa")
        formatter2.setAmPmNames("a.m.", "p.m.")
        val timeStr2 = "2023-04-30T11:00:00.000+08:00a.m."
        assertEquals(1682823600000, formatter2.parseToKZonedDateTime(timeStr2).toKInstant().toMilliseconds())

        val formatter3 = KDateTimeFormat("yyyy-MM-dd'T'hh:mm:ss.lllZAAA")
        formatter3.setAmPmNames("a.m.", "p.m.")
        val timeStr3 = "2023-04-30T11:00:00.000+08:00A.M."
        assertEquals(1682823600000, formatter3.parseToKZonedDateTime(timeStr3).toKInstant().toMilliseconds())

        val formatter4 = KDateTimeFormat("yyyy-MM-dd'T'hh:mm:ss.lllZAAA")
        formatter4.setAmPmNames("a.m.", "p.m.", "AM", "PM")
        val timeStr4 = "2023-04-30T11:00:00.000+08:00PM"
        assertEquals(1682866800000, formatter4.parseToKZonedDateTime(timeStr4).toKInstant().toMilliseconds())
    }
}
