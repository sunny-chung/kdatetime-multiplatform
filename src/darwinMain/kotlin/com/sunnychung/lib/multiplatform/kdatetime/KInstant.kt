package com.sunnychung.lib.multiplatform.kdatetime

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

internal actual fun kInstantOfCurrentTime(): KInstant {
    return KInstant((NSDate().timeIntervalSince1970() * 1000L).toLong())
}
