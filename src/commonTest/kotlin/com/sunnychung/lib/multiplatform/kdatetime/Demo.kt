package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.test.Test

class Demo {

    @Test
    fun demo() {
        val now = KInstant.now()
        println(now.toMilliseconds()) // 1694423218459

        val localDateTime = KZonedInstant(now.toMilliseconds(), KZoneOffset(8, 0))
        println(localDateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ")) // 2023-09-11T17:06:58.459+08:00

        val lastTrainTime = localDateTime.copy(hour = 23, minute = 10, second = 0, millisecond = 0)
        println(KDateTimeFormatter.ISO8601_DATETIME.format(lastTrainTime)) // 2023-09-11T23:10:00+08:00

        val twoMinutes = KDuration.of(2, KFixedTimeUnit.Minute)
        println(twoMinutes.toTimeUnitValue(KFixedTimeUnit.Second)) // 120

        val tomorrow = now + KDuration.of(1, KFixedTimeUnit.Day)
        println(KDateTimeFormatter.ISO8601_DATETIME.format(tomorrow)) // 2023-09-12T09:06:58Z

        val duration = KDuration.of(1, KFixedTimeUnit.Minute) + KDuration.of(35, KFixedTimeUnit.Second)
        println(duration.format("mm:ss")) // 01:35
    }
}
