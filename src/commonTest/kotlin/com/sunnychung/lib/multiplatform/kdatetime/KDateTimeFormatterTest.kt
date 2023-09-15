package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KDateTimeFormatterTest {
    @Test
    fun formatInstant() {
        val dateTime = KInstant(1694400859230) // Monday, September 11, 2023 2:54:19.230 AM
        assertEquals("2023/09/11 2:54:19.23 AM", dateTime.format("yyyy/MM/dd h:mm:ss.l AA"))
        assertEquals("23-09-11 02:54:19am", dateTime.format("yy-MM-dd hh:mm:ssaa"))
        assertEquals("2023-09-11T02:54:19Z", KDateTimeFormatter.ISO8601_DATETIME.format(dateTime))
        assertEquals("2023-09-11T02:54:19.230Z", dateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ"))

        val dateTime2 = KInstant(1694433600000) // Monday, September 11, 2023 12:00:00 PM
        assertEquals("2023/9/11 12:00:00.00 pm", dateTime2.format("yyyy/M/d h:mm:ss.ll aa"))

        val dateTime3 = KInstant(1694455980000) // Monday, September 11, 2023 6:13:00 PM
        assertEquals("2023-09-11T18:13:00.000Z", dateTime3.format("yyyy-MM-dd'T'HH:mm:ss.lllZ"))
    }

    @Test
    fun formatZonedInstant() {
        val dateTime = KZonedInstant(1694404171789, "+08:00") // Monday, September 11, 2023 11:49:31.789 AM GMT+08:00
        assertEquals("2023-09-11T11:49:31.789+08:00", dateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ"))

        val dateTime2 = KZonedInstant(1694391533000, "-07:00") // 2023-09-10 17:18:53 -07:00
        assertEquals("2023-09-10T17:18:53-07:00", KDateTimeFormatter.ISO8601_DATETIME.format(dateTime2))

        val dateTime3 = KZonedInstant(1694360033000, "+13:45") // 2023-09-11T05:18:53+13:45
        assertEquals("2023-09-11T05:18:53+13:45", KDateTimeFormatter.ISO8601_DATETIME.format(dateTime3))
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
        KDateTimeFormatter.FULL.parseToKZonedInstant("2023-09-11T11:49:31.789+08:00").let { dateTime ->
            assertEquals(1694404171789, dateTime.toMilliseconds())
            assertEquals(8, dateTime.zoneOffset.hours)
            assertEquals(0, dateTime.zoneOffset.minutes)
        }

        KDateTimeFormatter.ISO8601_DATETIME.parseToKZonedInstant("2023-09-10T17:18:53-07:00").let { dateTime ->
            assertEquals(1694391533000, dateTime.toMilliseconds())
            assertEquals(-7, dateTime.zoneOffset.hours)
            assertEquals(0, dateTime.zoneOffset.minutes)
        }

        KDateTimeFormatter.ISO8601_DATETIME.parseToKZonedInstant("2023-09-11T05:18:53+13:45").let { dateTime ->
            assertEquals(1694360033000, dateTime.toMilliseconds())
            assertEquals(13, dateTime.zoneOffset.hours)
            assertEquals(45, dateTime.zoneOffset.minutes)
        }

        KDateTimeFormatter("yy-MM-dd hh:mm:ss.lllaaZ").parseToKZonedInstant("23-09-11 02:54:19.230amZ").let { dateTime ->
            assertEquals(1694400859230, dateTime.toMilliseconds())
            assertEquals(0, dateTime.zoneOffset.hours)
            assertEquals(0, dateTime.zoneOffset.minutes)
        }

        KDateTimeFormatter("yy-MM-dd hh:mm:ssaaZ").parseToKZonedInstant("23-09-11 02:54:19pmUTC").let { dateTime ->
            assertEquals(1694444059000, dateTime.toMilliseconds())
            assertEquals(0, dateTime.zoneOffset.hours)
            assertEquals(0, dateTime.zoneOffset.minutes)
        }
    }

    @Test
    fun parseDateTimeToKInstant() {
        KDateTimeFormatter.FULL.parseToKInstant("2023-09-11T11:49:31.789+08:00").let { dateTime ->
            assertEquals(1694404171789, dateTime.toMilliseconds())
        }
    }

    @Test
    fun cannotParseWithoutEnoughFields() {
        assertFailsWith(IllegalArgumentException::class) {
            KDateTimeFormatter(pattern = "mm:ss").parseToKZonedInstant("anything")
        }
        assertFailsWith(IllegalArgumentException::class) {
            KDateTimeFormatter(pattern = "yyyy-MM-dd").parseToKZonedInstant("anything")
        }
        assertFailsWith(IllegalArgumentException::class) {
            KDateTimeFormatter(pattern = "yyyy-MM-dd hh:ss.lll").parseToKZonedInstant("anything")
        }
        assertFailsWith(IllegalArgumentException::class) {
            KDateTimeFormatter(pattern = "MM-dd hh:mm:ss.lll").parseToKZonedInstant("anything")
        }
    }
}
