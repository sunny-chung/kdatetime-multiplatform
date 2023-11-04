package com.sunnychung.lib.multiplatform.kdatetime

fun validateTime(hour: Int, minute: Int, second: Int, millisecond: Int = 0) {
    if (hour !in 0..23) {
        throw IllegalArgumentException("`hour` must be within 0 ~ 23.")
    }
    if (minute !in 0..59) {
        throw IllegalArgumentException("`minute` must be within 0 ~ 59.")
    }
    if (second !in 0..59) {
        throw IllegalArgumentException("`second` must be within 0 ~ 59.")
    }
    if (millisecond !in 0..999) {
        throw IllegalArgumentException("`millisecond` must be within 0 ~ 999.")
    }
}
