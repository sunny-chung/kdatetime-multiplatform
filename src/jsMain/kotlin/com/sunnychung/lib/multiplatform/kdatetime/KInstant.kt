package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.js.Date
import kotlin.math.roundToLong

internal actual fun kInstantOfCurrentTime(): KInstant {
    return KInstant(Date().getTime().roundToLong())
}
