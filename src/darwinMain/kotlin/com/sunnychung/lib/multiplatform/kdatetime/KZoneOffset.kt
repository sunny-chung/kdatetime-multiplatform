package com.sunnychung.lib.multiplatform.kdatetime

import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.secondsFromGMT

@OptIn(UnsafeNumber::class)
internal actual fun localZoneOffset(): KZoneOffset {
    val offsetSeconds = NSTimeZone.localTimeZone().secondsFromGMT()
    return KZoneOffset.fromMilliseconds(KDuration.of(offsetSeconds, KFixedTimeUnit.Second).toMilliseconds())
}
