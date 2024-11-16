package com.sunnychung.lib.multiplatform.kdatetime

import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.*

fun NSDate.toKInstant(): KInstant {
    return KInstant((timeIntervalSince1970() * 1000L).toLong())
}

fun KInstantFrom(date: NSDate): KInstant = date.toKInstant()

fun KInstant.toNSDate(): NSDate {
    return NSDate.dateWithTimeIntervalSince1970(toMilliseconds() / 1000.0)
}

fun NSDate.toKZonedInstant(zoneOffset: KZoneOffset): KZonedInstant {
    return KZonedInstant(timestampMs = toKInstant().toMilliseconds(), zoneOffset = zoneOffset)
}

fun KZonedInstantFrom(date: NSDate, zoneOffset: KZoneOffset): KZonedInstant {
    return date.toKZonedInstant(zoneOffset)
}

@OptIn(UnsafeNumber::class)
fun NSTimeZone.toKZoneOffset(): KZoneOffset {
    return KZoneOffset.fromMilliseconds(KDuration.of(secondsFromGMT, KFixedTimeUnit.Second).toMilliseconds())
}

fun KZoneOffsetFrom(timeZone: NSTimeZone): KZoneOffset = timeZone.toKZoneOffset()

@OptIn(UnsafeNumber::class)
fun KZoneOffset.toNSTimeZone(): NSTimeZone {
    return NSTimeZone.timeZoneForSecondsFromGMT(KDuration.of(toMilliseconds(), KFixedTimeUnit.MilliSecond).toTimeUnitValue(KFixedTimeUnit.Second).toNSInteger())
}
