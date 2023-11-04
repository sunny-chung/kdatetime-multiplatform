package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.extension.days
import com.sunnychung.lib.multiplatform.kdatetime.extension.milliseconds
import com.sunnychung.lib.multiplatform.kdatetime.extension.minutes
import com.sunnychung.lib.multiplatform.kdatetime.extension.seconds
import kotlin.test.Test

class Demo {

    @Test
    fun demo() {
        /* Now, Time Zone Offset, Formatting, Calendar, Parsing */

        val now = KInstant.now()
        println(now.toMilliseconds()) // 1694618242720

        val localZoneOffset = KZoneOffset.local()
        println(localZoneOffset.toDisplayString()) // +08:00

        val localDateTime = now.atZoneOffset(localZoneOffset)
        println(localDateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ")) // 2023-09-13T23:17:22.720+08:00
        println(localDateTime.format("yyyy-MM-dd h:mm:ss aa")) // 2023-09-13 11:17:22 pm

        println(KZonedInstant.nowAtLocalZoneOffset()) // 2023-09-13T23:17:22.722+08:00

        val japanDateTime = now.atZoneOffset(KZoneOffset(9, 0))
        println(japanDateTime) // 2023-09-14T00:17:22.720+09:00

        val lastTrainTime = localDateTime.toKZonedDateTime().copy(hour = 23, minute = 10, second = 0, millisecond = 0)
        println(KDateTimeFormat.ISO8601_DATETIME.format(lastTrainTime.toKZonedInstant())) // 2023-09-13T23:10:00+08:00

        val parsedDateTime = KDateTimeFormat.ISO8601_DATETIME.parseToKZonedInstant("2023-09-10T17:18:53-07:00")
        println(parsedDateTime.toMilliseconds()) // 1694391533000

        val duration1 = 95.seconds()
        println(duration1.format("m:ss")) // 1:35
        println(duration1.format("m'm' s's'")) // 1m 35s

        /* Conversions */

        val twoMinutes = 2.minutes()
        println(twoMinutes.toTimeUnitValue(KFixedTimeUnit.Second)) // 120

        /* Arithmetic, Comparison */

        val tomorrow = now + 1.days()
        println(KDateTimeFormat.ISO8601_DATETIME.format(tomorrow)) // 2023-09-14T15:17:22Z

        val duration2 = 1.minutes() + 35.seconds()
        println(duration2.format("mm:ss")) // 01:35

        println(tomorrow.atZoneOffset(KZoneOffset(-7, 0)) > now.atZoneOffset(KZoneOffset(8, 0))) // true

        val oneDay = tomorrow - now
        println(oneDay.toMilliseconds()) // 86400000

        val sortedInstants = listOf(1694618242720, 1694618242723, 1694618242721, 1694618242722)
            .map { KInstant(it) }
            .sorted()
        println(sortedInstants) // [2023-09-13T15:17:22.720Z, 2023-09-13T15:17:22.721Z, 2023-09-13T15:17:22.722Z, 2023-09-13T15:17:22.723Z]

        val zonedDateTime = KZonedDateTime(
            year = 2023,
            month = 10,
            day = 4,
            hour = 13,
            minute = 8,
            second = 40,
            zoneOffset = KZoneOffset.parseFrom("+08:00")
        )
        val zonedDateTime2 = zonedDateTime + duration2
        println(zonedDateTime2) // 2023-10-04T13:10:15.000+08:00

        println(10_000.milliseconds() > 9.seconds()) // true
    }
}
