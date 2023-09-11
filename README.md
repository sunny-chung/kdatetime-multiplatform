# KDateTime Multiplatform

A Kotlin Multiplatform library to provide regular datetime functionality needed with very minimal platform dependencies. Same API set is provided to all JVM, JS, native targets.

This library is currently under active development. Suggestions and contributions are welcomed!

# APIs

Public classes / objects:
- `KInstant` (single point of time)
- `KZonedInstant` (single point of time + time zone offset)
- `KZoneOffset` (time zone offset)
- `KDuration` (a date-time range)
- `KFixedTimeUnit` (time unit)
- `KDateTimeFormatter` (format dates and times)
- `KGregorianCalendar` (conversion between timestamps and calendar dates)

Unlike Java, there is no local date or local datetime class here. That creates lots of issues. `KZonedInstant` can be used instead.

# Examples

## Now, Time Zone Offset, Formatting, Calendar
```kotlin
val now = KInstant.now()
println(now.toMilliseconds()) // 1694423218459

val localDateTime = KZonedInstant(now.toMilliseconds(), KZoneOffset(8, 0))
println(localDateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ")) // 2023-09-11T17:06:58.459+08:00

val lastTrainTime = localDateTime.copy(hour = 23, minute = 10, second = 0, millisecond = 0)
println(KDateTimeFormatter.ISO8601_DATETIME.format(lastTrainTime)) // 2023-09-11T23:10:00+08:00
```

## Arithmetic
```kotlin
val tomorrow = now + KDuration.of(1, KFixedTimeUnit.Day)
println(KDateTimeFormatter.ISO8601_DATETIME.format(tomorrow)) // 2023-09-12T09:06:58Z

val duration = KDuration.of(1, KFixedTimeUnit.Minute) + KDuration.of(35, KFixedTimeUnit.Second)
println(duration.format("mm:ss")) // 01:35
```
