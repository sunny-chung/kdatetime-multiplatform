# KDateTime Multiplatform

![GitHub](https://img.shields.io/github/license/sunny-chung/kdatetime-multiplatform)
![Maven Central](https://img.shields.io/maven-central/v/io.github.sunny-chung/kdatetime-multiplatform)

![Android](https://img.shields.io/badge/Android-blue)
![JVM](https://img.shields.io/badge/JVM-blue)
![js](https://img.shields.io/badge/js-blue)
![iOS](https://img.shields.io/badge/iOS-blue)
![macOS](https://img.shields.io/badge/macOS-blue)
![watchOS](https://img.shields.io/badge/watchOS-blue)
![tvOS](https://img.shields.io/badge/tvOS-blue)

A Kotlin Multiplatform library to provide **regular date-time functionality needed with very minimal platform dependencies**. It means upgrading OS / platform SDK target versions or moving to another platform would not break your application. Same and consistent core API set is provided to all JVM, Apple, JS targets.

Before using this library, please read the limitations, known issues and relevant unit tests for well tested use cases. This library may not be stable to use out of these tested use cases.

This library is currently under active development. Suggestions and contributions are welcomed!

## TL;DR
* [APIs](#apis)
* [Examples](#examples)
* [Getting Started](#getting-started)
* [Limitations](#limitations)
* [Known Issues](#known-issues)
* [For Developers](#for-developers)

# Supported Platforms
- Android (Tested against compileSdk = 33, 34)
- Non-Android JVM (macOS tested against 14.0 beta, Java 17; Windows and Linux untested)
- JS (Legacy compiler + IR compiler)
- iOS (Tested against 16.4)
- macOS Native (Tested against 14.0 beta)
- watchOS (Tested against 10.0 beta)
- tvOS (Tested against 17.0 beta)

# APIs

Public classes / objects:
- `KInstant` (single point of time)
- `KZonedInstant` (single point of time + time zone offset)
- `KZoneOffset` (time zone offset)
- `KZonedDateTime` (date time + time zone offset)
- `KDuration` (a date-time length)
- `KFixedTimeUnit` (time unit)
- `KDateTimeFormat` (format and parse dates and times)
- `KGregorianCalendar` (conversion between timestamps and calendar dates)

Unlike Java, there is no local date or local datetime class here. That creates lots of usage issues. `KZonedInstant` or `KZonedDateTime` can be used instead.

There is also no time zone but time zone offset at this moment. (Help needed to tell me how these time zone ID, rule changes, DST work)

All of these classes are **thread-safe**, **serializable** and **parcelable**.

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

val lastTrainTime = localDateTime.toKZonedDateTime().copy(hour = 23, minute = 10, second = 0, millisecond = 0)
println(KDateTimeFormat.ISO8601_DATETIME.format(lastTrainTime.toKZonedInstant())) // 2023-09-13T23:10:00+08:00

val parsedDateTime = KDateTimeFormat.ISO8601_DATETIME.parseToKZonedInstant("2023-09-10T17:18:53-07:00")
println(parsedDateTime.toMilliseconds()) // 1694391533000

val duration1 = KDuration.of(95, KFixedTimeUnit.Second)
println(duration1.format("m:ss")) // 1:35
println(duration1.format("m'm' s's'")) // 1m 35s
```

## Conversions
Time unit conversions.
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

val sortedInstants = listOf(1694618242720, 1694618242723, 1694618242721, 1694618242722)
    .map { KInstant(it) }
    .sorted()
println(sortedInstants) // [KInstant(2023-09-13T15:17:22.720Z), KInstant(2023-09-13T15:17:22.721Z), KInstant(2023-09-13T15:17:22.722Z), KInstant(2023-09-13T15:17:22.723Z)]

val zonedDateTime = KZonedDateTime(
    year = 2023,
    month = 10,
    day = 4,
    hour = 13,
    minute = 8,
    second = 40,
    zoneOffset = KZoneOffset.parseFrom("+08:00")
)
val zonedDateTime2 = zonedDateTime + duration2
println(zonedDateTime2) // KZonedDateTime(2023-10-04T13:10:15.000+08:00)
```

## Serialization, Deserialization
This library supports `kotlinx.serialization` out of the box (except `KDuration`), to allow conversion between KDateTime classes and string from/to JSON, protobuf, etc.. Read [examples here](src/commonTest/kotlin/com/sunnychung/lib/multiplatform/kdatetime/SerializerTest.kt).

Besides, an additional type `KInstantAsLong` is provided for converting between timestamp in milliseconds (`Long`) and `KInstant`.

```kotlin
@Serializable
@AndroidParcelize
class TransitConnect : AndroidParcelable {
    lateinit var summary: Summary

    @Serializable
    @AndroidParcelize
    class Summary : WithDuration, AndroidParcelable {
        override lateinit var startAt: KZonedInstant
        override lateinit var endAt: KZonedInstant
        var walkingSeconds: Long = -1
        var waitingSeconds: Long = -1
        var numOfTrips: Int = -1
    }
}
```

## Compatibility with platform-specific datetime APIs
### From Kotlin side
[iOS](src/darwinMain/kotlin/com/sunnychung/lib/multiplatform/kdatetime/KDateTime.kt),
[JS](src/jsMain/kotlin/com/sunnychung/lib/multiplatform/kdatetime/JsPlatformDatetimeConversion.kt),
[JVM](src/commonJvmMain/kotlin/com/sunnychung/lib/multiplatform/kdatetime/JvmPlatformDatetimeConversion.kt)

For example, for iOS targets:
```kotlin
val instant: KInstant = NSDate().toKInstant()
val date: NSDate = instant.toNSDate()
```

### From native side
iOS
```swift
let instant: KInstant = KInstant.companion.now() as KInstant
let iosDate: Date = instant.toNSDate()
let instant2: KInstant = KDateTimeKt.KInstantFrom(date: iosDate)
```

### For Android
The classes `KInstant`, `KZonedInstant`, `KZonedDateTime`, `KZoneOffset`, `KDuration` implement `Parcelable`.

# Getting Started

## First Step

Add the repository.
```kotlin
    repositories {
        mavenCentral()
    }
```

In the KMM / KMP application build.gradle.kts, include the dependency in the `commonMain` source set.
```kotlin
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.github.sunny-chung:kdatetime-multiplatform:<version>")
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
            export("io.github.sunny-chung:kdatetime-multiplatform:<version>")
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

# For Developers

This section is only for developers who are interested in compiling or modifying this library.

1. This project does not support IntelliJ IDEA but Android Studio. Because IntelliJ IDEA of the latest version does not support Android Gradle plugin 8.0.2, but downgrading it to 7.3.1 would encounter some compilation bugs. 
