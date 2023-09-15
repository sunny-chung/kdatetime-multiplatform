package com.sunnychung.lib.multiplatform.kdatetime

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

        println(KZonedInstant.nowAtLocalZoneOffset()) // KZonedInstant(2023-09-13T23:17:22.722+08:00)

        val japanDateTime = now.atZoneOffset(KZoneOffset(9, 0))
        println(japanDateTime) // KZonedInstant(2023-09-14T00:17:22.720+09:00)

        val lastTrainTime = localDateTime.copy(hour = 23, minute = 10, second = 0, millisecond = 0)
        println(KDateTimeFormat.ISO8601_DATETIME.format(lastTrainTime)) // 2023-09-13T23:10:00+08:00

        val parsedDateTime = KDateTimeFormat.ISO8601_DATETIME.parseToKZonedInstant("2023-09-10T17:18:53-07:00")
        println(parsedDateTime.toMilliseconds()) // 1694391533000

        val duration1 = KDuration.of(95, KFixedTimeUnit.Second)
        println(duration1.format("m:ss")) // 1:35

        /* Conversions */

        val twoMinutes = KDuration.of(2, KFixedTimeUnit.Minute)
        println(twoMinutes.toTimeUnitValue(KFixedTimeUnit.Second)) // 120

        /* Arithmetic, Comparison */

        val tomorrow = now + KDuration.of(1, KFixedTimeUnit.Day)
        println(KDateTimeFormat.ISO8601_DATETIME.format(tomorrow)) // 2023-09-14T15:17:22Z

        val duration2 = KDuration.of(1, KFixedTimeUnit.Minute) + KDuration.of(35, KFixedTimeUnit.Second)
        println(duration2.format("mm:ss")) // 01:35

        println(tomorrow.atZoneOffset(KZoneOffset(-7, 0)) > now.atZoneOffset(KZoneOffset(8, 0))) // true
    }
}
