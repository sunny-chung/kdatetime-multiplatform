package com.sunnychung.lib.multiplatform.kdatetime

import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.secondsFromGMT

internal actual fun localZoneOffset(): KZoneOffset {
    val offsetSeconds = NSTimeZone.localTimeZone().secondsFromGMT()
    return KZoneOffset.fromMilliseconds(KDuration.of(offsetSeconds, KFixedTimeUnit.Second).toMilliseconds())
}
