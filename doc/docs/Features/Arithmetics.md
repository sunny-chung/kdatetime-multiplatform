---
weight: 4
---

# Arithmetics

Add, subtract times and convert among different time units painlessly.

```kotlin
val now = KInstant.now()
val tomorrow = now + 1.days()

val dateTime = KZonedDateTime(
    year = 2024,
    month = 1,
    day = 1,
    hour = 1,
    minute = 8,
    second = 40,
    zoneOffset = KZoneOffset.parseFrom("+08:00")
)
val yesterday = dateTime - 1.days()
```

```kotlin
val duration = 2.hours() + 33.minutes()
println(duration.toSeconds()) // 9180
```

The difference between two time instants is a duration.
```kotlin
val duration: KDuration = tomorrow - now // equivalent to `1.days()`
```
