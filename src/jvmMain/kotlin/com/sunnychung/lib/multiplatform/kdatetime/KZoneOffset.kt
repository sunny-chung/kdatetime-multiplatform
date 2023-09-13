package com.sunnychung.lib.multiplatform.kdatetime

import java.time.ZonedDateTime

internal actual fun localZoneOffset(): KZoneOffset {
    val offsetSeconds = ZonedDateTime.now().offset.totalSeconds
    return KZoneOffset.fromMilliseconds(KDuration.of(offsetSeconds, KFixedTimeUnit.Second).toMilliseconds())
}
