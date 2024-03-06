package com.sunnychung.lib.multiplatform.kdatetime.extension

import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit

fun Int.milliseconds() = KDuration.of(this, KFixedTimeUnit.MilliSecond)
fun Long.milliseconds() = KDuration.of(this, KFixedTimeUnit.MilliSecond)

fun Int.seconds() = KDuration.of(this, KFixedTimeUnit.Second)
fun Long.seconds() = KDuration.of(this, KFixedTimeUnit.Second)

fun Int.minutes() = KDuration.of(this, KFixedTimeUnit.Minute)

fun Int.hours() = KDuration.of(this, KFixedTimeUnit.Hour)

fun Int.days() = KDuration.of(this, KFixedTimeUnit.Day)

fun Int.weeks() = KDuration.of(this, KFixedTimeUnit.Week)
