---
weight: 6
---

# Platform Interoperability

## Android

The classes `KInstant`, `KZonedInstant`, `KZonedDateTime`, `KZoneOffset`, `KDuration` implement `Parcelable`. To use it, just annotate the data classes with `@AndroidParcelize` and extend the interface `AndroidParcelable`.

For example:

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

## Kotlin/JVM, Java and Android

Conversions between KDateTime and Java 8 Date Time classes are available in Kotlin:

```kotlin
fun KInstant.toJavaInstant(): Instant
fun Instant.toKInstant(): KInstant

fun KZoneOffset.toJavaZoneOffset(): ZoneOffset
fun ZoneOffset.toKZoneOffset(): KZoneOffset

fun KZonedInstant.toJavaOffsetDateTime(): OffsetDateTime
fun OffsetDateTime.toKZonedInstant(): KZonedInstant

fun KZonedInstant.toJavaZonedDateTime(): ZonedDateTime
fun ZonedDateTime.toKZonedInstant(): KZonedInstant
```

## iOS / macOS / watchOS / tvOS / Swift

In Kotlin, conversions between KDateTime and native classes are available.

```kotlin
fun NSDate.toKInstant(): KInstant
fun KInstantFrom(date: NSDate): KInstant
fun KInstant.toNSDate(): NSDate

fun NSDate.toKZonedInstant(zoneOffset: KZoneOffset): KZonedInstant
fun KZonedInstantFrom(date: NSDate, zoneOffset: KZoneOffset): KZonedInstant

fun NSTimeZone.toKZoneOffset(): KZoneOffset
fun KZoneOffsetFrom(timeZone: NSTimeZone): KZoneOffset
fun KZoneOffset.toNSTimeZone(): NSTimeZone
```

All KDateTime classes and functions can be called from Swift:
```swift title="Swift"
let instant: KInstant = KInstant.companion.now() as KInstant
let iosDate: Date = instant.toNSDate()
let instant2: KInstant = KDateTimeKt.KInstantFrom(date: iosDate)
```

When working with native iOS, date time strings can omit seconds and/or milliseconds, but you don't know which format would be used at when. With KDateTime, no more pain when parsing these strings.
```kotlin
val zonedInstant = KZonedInstant.parseFrom(input = string, formats = KDateTimeFormat.IOS_DATE_FORMATS)
```

## JavaScript / TypeScript

There is a limited set of conversions between KDateTime and JS native APIs exposed from Kotlin side.
```kotlin
fun KInstant.toJsDate(): Date
fun Date.toKInstant(): KInstant
```
