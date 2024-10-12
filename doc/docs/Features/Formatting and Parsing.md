---
weight: 2
---

# Formatting and Parsing

`KInstant`, `KZonedInstant`, `KZonedDateTime` and `KDuration` can be serialized to Strings with custom formats. In reverse, Strings can be parsed into `KZonedDateTime`, which can be further converted to `KInstant` and `KZonedInstant`, using custom formats. Needless to say, the standard ISO 8601 format is supported out-of-the-box.

Supported formats are as follows:

| Symbol | Meaning                       | Examples          |
|--------|-------------------------------|-------------------|
| y      | year                          | 2023; 23          |
| M      | month                         | 7; 07             |
| d      | day of month                  | 9; 09; 31         |
| H      | hour (0~23)                   | 0; 00; 18         |
| h      | hour (1~12)                   | 6; 06; 10         |
| m      | minute                        | 4; 04; 58         |
| s      | second                        | 5; 05; 59         |
| l      | millisecond                   | 12; 012; 120; 123 |
| aa     | am/pm                         | am; pm            |
| AA     | AM/PM                         | AM; PM            |
| e      | Day of week. 0 = Sun, 6 = Sat | 1                 |
| E      | Day of week                   | Mon               |
| Z      | Time zone offset or 'Z'       | Z; +08:00         |
| z      | Time zone offset              | +00:00; +08:00    |
| '      | Literal                       | '                 |

## Examples

### Formatting Dates, Times and Timezone Offsets

Custom Formats

```kotlin
val now = KInstant(timestampMs = 1694618242720)

val localDateTime = now.atLocalZoneOffset()
println(localDateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ")) // 2023-09-13T23:17:22.720+08:00
println(localDateTime.format("yyyy-M-d h:mm:ss aa")) // 2023-9-13 11:17:22 pm

val utcTime = now at KZoneOffset(0, 0)
println(localDateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ")) // 2023-09-13T15:17:22.720Z
println(localDateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllz")) // 2023-09-13T15:17:22.720+00:00
```

ISO 8601 Formats

```kotlin
val instant = KInstant(timestampMs = 1710212523_999)
println(instant.toIso8601String()) // 2024-03-12T03:02:03Z
println(instant.toIso8601StringWithMilliseconds()) // 2024-03-12T03:02:03.999Z

val zonedInstant = KZonedInstant(timestampMs = 1710212523_999, zoneOffset = KZoneOffset(hours = 8, minutes = 0))
println(zonedInstant.toIso8601String()) // 2024-03-12T11:02:03+08:00
println(zonedInstant.toIso8601StringWithMilliseconds()) // 2024-03-12T11:02:03.999+08:00

val zonedDateTime = KZonedDateTime(year = 2024, month = 3, day = 12, hour = 5, minute = 12, second = 3, millisecond = 999, zoneOffset = KZoneOffset(hours = 8, minutes = 0))
println(zonedDateTime.toIso8601String()) // 2024-03-12T05:12:03+08:00
println(zonedDateTime.toIso8601StringWithMilliseconds()) // 2024-03-12T05:12:03.999+08:00
```

### Formatting Durations and Literals

```kotlin
val duration1 = 95.seconds()
println(duration1.format("m:ss")) // 1:35
println(duration1.format("m'm' s's'")) // 1m 35s
```

### Formatting Localized Weekdays
```kotlin
val dateTime = KInstant(1705677172000) // Friday, January 19, 2024 3:12:52 PM GMT
val formatter = KDateTimeFormat("E")
println(formatter.format(dateTime)) // Fri
formatter.weekDayNames = listOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
println(formatter.format(dateTime)) // 星期五
```

### Formatting Localized AM/PM
```kotlin
val dateTime = KInstant(1723698748231) // Thursday, August 15, 2024 5:12:28 AM GMT
val formatter3 = KDateTimeFormat("AA aa")
formatter3.setAmPmNames("오전", "오후")
println(formatter3.format(dateTime)) // 오전 오전
```

### Parsing from Strings

Parse from ISO 8601 format without milliseconds

```kotlin
val time1: KZonedInstant = KZonedInstant.parseFromIso8601String("2023-09-10T17:18:53-07:00")

val time2: KZonedInstant = KDateTimeFormat.ISO8601_DATETIME
    .parseToKZonedDateTime("2023-09-10T17:18:53-07:00")
    .toKZonedInstant()
    .also { dateTime ->
        assertEquals(1694391533000, dateTime.toEpochMilliseconds())
        assertEquals(-7, dateTime.zoneOffset.hours)
        assertEquals(0, dateTime.zoneOffset.minutes)
    }
```

Parse from ISO 8601 format with milliseconds

```kotlin
// `KZonedInstant.parseFromIso8601String` is tolerable
val time1: KZonedInstant = KZonedInstant.parseFromIso8601String("2023-09-10T17:18:53.123-07:00")

val time2: KZonedInstant = KDateTimeFormat.FULL
    .parseToKZonedDateTime("2023-09-10T17:18:53.123-07:00")
    .toKZonedInstant()
    .also { dateTime ->
        assertEquals(1694391533123, dateTime.toEpochMilliseconds())
        assertEquals(-7, dateTime.zoneOffset.hours)
        assertEquals(0, dateTime.zoneOffset.minutes)
    }
```

Parse from custom formats

```kotlin
val time1: KZonedInstant = KZonedInstant.parseFrom(
    input = "23-09-11 02:54:19pmUTC",
    formats = listOf(KDateTimeFormat("yy-MM-dd hh:mm:ssaaZ"))
)

val time2: KZonedInstant = KDateTimeFormat("yy-MM-dd hh:mm:ssaaZ")
    .parseToKZonedDateTime("23-09-11 02:54:19pmUTC")
    .toKZonedInstant()
    .also { dateTime ->
        assertEquals(1694444059000, dateTime.toEpochMilliseconds())
        assertEquals(0, dateTime.zoneOffset.hours)
        assertEquals(0, dateTime.zoneOffset.minutes)
    }
```

Try to parse from multiple formats
```kotlin
val time: KZonedInstant = KZonedInstant.parseFrom(
    input = "23-09-11 02:54:19pmUTC",
    formats = listOf(
        KDateTimeFormat("yyyy-MM-dd hh:mm:ssaaZ"),
        KDateTimeFormat("yyyy-MM-dd hh:mm:ssaa Z"),
        KDateTimeFormat("yy-MM-dd hh:mm:ssaaZ"),
        KDateTimeFormat("yy-MM-dd hh:mm:ssaa Z"),
    )
)
```

Parse localized AM/PM
```kotlin
val time: KZonedInstant = KDateTimeFormat("yy-MM-dd hh:mm:ss A Z")
            .apply { setAmPmNames("a.m.", "p.m.") }
            .parseToKZonedDateTime("23-09-11 02:54:19 P.M. -01:00")
            .toKZonedInstant()
```

## Optimizing for Performance

If formatting or parsing of a specific format is frequent, please consider creating a reusable `KDateTimeFormat` instance to optimize runtime performance.

```kotlin
val formatter = KDateTimeFormat("yyyy-MM-dd'T'HH:mm:ss.lllZ")
println(formatter.format(KInstant(timestampMs = 1694618242720))) // 2023-09-13T23:17:22.720+08:00
println(formatter.format(KInstant(timestampMs = 1694618242723))) // 2023-09-13T23:17:22.723+08:00
```

There are also a few builtin `KDateTimeFormat` instances can be used directly.

```kotlin
println(KDateTimeFormat.ISO8601_DATETIME.format(KInstant(timestampMs = 1694618242720))) // 2023-09-13T23:17:22+08:00
println(KDateTimeFormat.FULL.format(KInstant(timestampMs = 1694618242720))) // 2023-09-13T23:17:22.720+08:00
```
