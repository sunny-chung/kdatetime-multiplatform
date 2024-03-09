---
weight: 3
---

# Serialization, Deserialization

## KDateTime <--> String

This library supports `kotlinx.serialization` out of the box (except `KDuration`), to allow conversion between KDateTime classes and `String` from/to JSON, protobuf, etc..

Supported classes:
- KInstant
- KZonedInstant
- KZoneDateTime
- KZoneOffset

### Example

```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class TransitConnect {
    lateinit var summary: Summary

    @Serializable
    class Summary {
        lateinit var startAt: KZonedInstant
        lateinit var endAt: KZonedInstant
        var walkingSeconds: Long = -1
        var waitingSeconds: Long = -1
        var numOfTrips: Int = -1
    }
}

fun TransitConnect.serializeToJson(): String {
    return Json.encodeToString(this)
}

fun TransitConnect.Companion.deserializeFromJson(json: String): TransitConnect {
    return Json.decodeFromString<TransitConnect>(json)
}
```

## KDateTime <--> Long

In additional to above, the type `KInstantAsLong` is provided to convert between `KInstant` and `Long` in milliseconds. `KInstantAsLong` is a [type alias](https://kotlinlang.org/docs/type-aliases.html) of `KInstant`.

### Example

```kotlin
@Serializable
data class MyData(val time: KInstantAsLong)

fun getTime(): KInstant {
    val json = "{\"time\":1694862189674}"
    val data = Json.decodeFromString<MyData>(json)
    return data.time
}
```
