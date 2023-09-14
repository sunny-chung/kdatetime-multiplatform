package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.js.Date

fun KInstant.toJsDate(): Date {
    return Date(toMilliseconds())
}

fun Date.toKInstant(): KInstant {
    return KInstant(getTime().toLong())
}
