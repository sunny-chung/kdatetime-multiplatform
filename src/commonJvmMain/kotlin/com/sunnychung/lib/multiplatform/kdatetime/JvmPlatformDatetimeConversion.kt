package com.sunnychung.lib.multiplatform.kdatetime

import java.time.*

fun KInstant.toJavaInstant(): Instant {
    return Instant.ofEpochMilli(toMilliseconds())
}

fun Instant.toKInstant(): KInstant {
    return KInstant(toEpochMilli())
}

fun KZoneOffset.toJavaZoneOffset(): ZoneOffset {
    return ZoneOffset.ofTotalSeconds(KDuration.of(toMilliseconds(), KFixedTimeUnit.MilliSecond).toTimeUnitValue(KFixedTimeUnit.Second).toInt())
}

fun ZoneOffset.toKZoneOffset(): KZoneOffset {
    return KZoneOffset.fromMilliseconds(KDuration.of(this.totalSeconds, KFixedTimeUnit.Second).toMilliseconds())
}

fun KZonedInstant.toJavaOffsetDateTime(): OffsetDateTime {
    return OffsetDateTime.ofInstant(dropZoneOffset().toJavaInstant(), zoneOffset.toJavaZoneOffset())
}

fun OffsetDateTime.toKZonedInstant(): KZonedInstant {
    return KZonedInstant(timestampMs = toInstant().toEpochMilli(), zoneOffset = offset.toKZoneOffset())
}

fun KZonedInstant.toJavaZonedDateTime(): ZonedDateTime {
    return toJavaOffsetDateTime().toZonedDateTime()
}

fun ZonedDateTime.toKZonedInstant(): KZonedInstant {
    return KZonedInstant(timestampMs = toInstant().toEpochMilli(), zoneOffset = offset.toKZoneOffset())
}
