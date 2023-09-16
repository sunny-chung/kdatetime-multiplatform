# KDateTime Multiplatform

A Kotlin Multiplatform library to provide regular datetime functionality needed with very minimal platform dependencies. It means upgrading OS / platform SDK target versions would not break your application. Same core API set is provided to all JVM, iOS, JS targets.

Before using this library, please read the relevant unit tests for well tested use cases. This library may not be stable to use out of these tested use cases.

This library is currently under active development. Suggestions and contributions are welcomed!

# APIs

Public classes / objects:
- `KInstant` (single point of time)
- `KZonedInstant` (single point of time + time zone offset)
- `KZoneOffset` (time zone offset)
- `KDuration` (a date-time length)
- `KFixedTimeUnit` (time unit)
- `KDateTimeFormat` (format and parse dates and times)
- `KGregorianCalendar` (conversion between timestamps and calendar dates)

Unlike Java, there is no local date or local datetime class here. That creates lots of usage issues. `KZonedInstant` can be used instead.

There is also no time zone but time zone offset at this moment.

All of these classes are thread-safe.

Supported custom format pattern symbols can be checked [here](src/commonMain/kotlin/com/sunnychung/lib/multiplatform/kdatetime/KDateTimeFormat.kt). It has some difference with Java APIs.

# Examples

## Now, Time Zone Offset, Formatting, Calendar, Parsing
```kotlin
val now = KInstant.now()
println(now.toMilliseconds()) // 1694618242720

val localZoneOffset = KZoneOffset.local()
println(localZoneOffset.toDisplayString()) // +08:00

val localDateTime = now.atZoneOffset(localZoneOffset)
println(localDateTime.format("yyyy-MM-dd'T'HH:mm:ss.lllZ")) // 2023-09-13T23:17:22.720+08:00
println(localDateTime.format("yyyy-MM-dd h:mm:ss aa")) // 2023-09-13 11:17:22 pm

println(KZonedInstant.nowAtLocalZoneOffset()) // KZonedInstant(2023-09-13T23:17:22.722+08:00)

val japanDateTime = now.atZoneOffset(KZoneOffset(9, 0))
println(japanDateTime) // KZonedInstant(2023-09-14T00:17:22.720+09:00)

val lastTrainTime = localDateTime.copy(hour = 23, minute = 10, second = 0, millisecond = 0)
println(KDateTimeFormat.ISO8601_DATETIME.format(lastTrainTime)) // 2023-09-13T23:10:00+08:00

val parsedDateTime = KDateTimeFormat.ISO8601_DATETIME.parseToKZonedInstant("2023-09-10T17:18:53-07:00")
println(parsedDateTime.toMilliseconds()) // 1694391533000

val duration1 = KDuration.of(95, KFixedTimeUnit.Second)
println(duration1.format("m:ss")) // 1:35
println(duration1.format("m'm' s's'")) // 1m 35s
```

## Conversions
```kotlin
val twoMinutes = KDuration.of(2, KFixedTimeUnit.Minute)
println(twoMinutes.toTimeUnitValue(KFixedTimeUnit.Second)) // 120
```

## Arithmetic, Comparison
```kotlin
val tomorrow = now + KDuration.of(1, KFixedTimeUnit.Day)
println(KDateTimeFormat.ISO8601_DATETIME.format(tomorrow)) // 2023-09-14T15:17:22Z

val duration2 = KDuration.of(1, KFixedTimeUnit.Minute) + KDuration.of(35, KFixedTimeUnit.Second)
println(duration2.format("mm:ss")) // 01:35

println(tomorrow.atZoneOffset(KZoneOffset(-7, 0)) > now.atZoneOffset(KZoneOffset(8, 0))) // true

val oneDay = tomorrow - now
println(oneDay.toMilliseconds()) // 86400000
```

## Compatibility with platform-specific datetime APIs
### From Kotlin side
[iOS](src/darwinMain/kotlin/com/sunnychung/lib/multiplatform/kdatetime/KDateTime.kt),
[JS](src/jsMain/kotlin/com/sunnychung/lib/multiplatform/kdatetime/JsPlatformDatetimeConversion.kt),
[JVM](src/jvmMain/kotlin/com/sunnychung/lib/multiplatform/kdatetime/JvmPlatformDatetimeConversion.kt)

For example, for iOS targets:
```kotlin
val instant: KInstant = NSDate().toKInstant()
val date: NSDate = instant.toNSDate()
```

### From native side
iOS
```swift
let instant: KInstant = KInstant.Companion.shared.now() as KInstant
let iosDate: Date = instant.toNSDate()
let instant2: KInstant = KDateTimeKt.KInstantFrom(date: iosDate)
```

# Getting Started

## First Step

In the KMM / KMP application build.gradle.kts, include the dependency in the `commonMain` source set.
```kotlin
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.sunnychung.multiplatform:kdatetime-multiplatform:0.1.0-SNAPSHOT")
                // ...
            }
        }
        // ...
```

After that, you can use KDateTime in any source set that depends on `commonMain` in Kotlin side.

## To Use KDateTime in Swift / Objective-C
Add a transitive export to the `framework` DSL:
```kotlin
        framework {
            baseName = "shared"
            transitiveExport = true
            export("com.sunnychung.multiplatform:kdatetime-multiplatform:0.1.0-SNAPSHOT")
        }
```

In native side, import your common framework to use.
```swift
import shared
```

# Limitations
- Only timestamps between year 1970 to 2999 are supported
- Minimum time unit is millisecond
- Only English is supported for locale-specific inputs and outputs

# Known Issues
- Date-time parser can only parse integers with exact length specified in the pattern. If this condition does not meet, exceptions might not be thrown, instead wrong result might be returned. 
- In most of the cases, input values are not validated.
