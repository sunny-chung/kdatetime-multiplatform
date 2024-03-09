---
weight: 5
---

# Comparables and Ranges

`KInstant`, `KZonedInstant` and `KDuration` are comparable. That means you can compare them, sort them, or put it as keys of a sorted map.

`KInstant` and `KZonedInstant` are compared by the epoch timestamp regardless of timezones.

## Comparison

```kotlin
println(tomorrow.atZoneOffset(KZoneOffset(-7, 0)) > now.atZoneOffset(KZoneOffset(8, 0))) // true

println(10_000.milliseconds() > 9.seconds()) // true
println(8_999.milliseconds() > 9.seconds()) // false
```

## Sorting

```kotlin
val sortedInstants = listOf(1694618242720, 1694618242723, 1694618242721, 1694618242722)
    .map { KInstant(it) }
    .sorted()
println(sortedInstants) // [2023-09-13T15:17:22.720Z, 2023-09-13T15:17:22.721Z, 2023-09-13T15:17:22.722Z, 2023-09-13T15:17:22.723Z]
```

## Ranges

```kotlin
import com.sunnychung.lib.multiplatform.kdatetime.extension.contains

val closedTimeRange = KInstant(1709975163000) .. KInstant(1709975173000)
println(KZonedInstant(timestampMs = 1709975165000, zoneOffset = KZoneOffset(8, 0)) in closedTimeRange) // true

val openTimeRange = KZonedInstant(1709975163000, KZoneOffset(1, 0)) ..< KZonedInstant(1709975173000, KZoneOffset(1, 0))
println(KInstant(timestampMs = 1709975165000) in openTimeRange) // true
```

!!! note
    To use the `in` operator, manual importing an extension function is needed:
    ```kotlin
    import com.sunnychung.lib.multiplatform.kdatetime.extension.contains
    ```
