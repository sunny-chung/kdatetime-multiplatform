package com.sunnychung.lib.multiplatform.kdatetime

import kotlin.js.Date

internal actual fun localZoneOffset(): KZoneOffset {
    val offsetMinutes = -1 * Date().getTimezoneOffset()
    return KZoneOffset.fromMilliseconds(KDuration.of(offsetMinutes, KFixedTimeUnit.Minute).toMilliseconds())
}