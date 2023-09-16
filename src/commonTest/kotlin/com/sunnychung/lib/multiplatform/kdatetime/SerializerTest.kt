package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializerTest {
    @Serializable data class KInstantData(val data: KInstant)
    @Serializable data class KInstantAsLongData(val data: KInstantAsLong)
    @Serializable data class KZonedInstantData(val data: KZonedInstant)
    @Serializable data class KZoneOffsetData(val data: KZoneOffset)

    @Test
    fun serializeKZonedInstant() {
        val data = KZonedInstantData(KZonedInstant.parseFrom(input = "2023-09-16T19:03:09.674+08:00", formats = listOf(KDateTimeFormat.FULL)))
        val json = Json.encodeToString(data)
        assertEquals("{\"data\":\"2023-09-16T19:03:09.674+08:00\"}", json)
    }

    @Test
    fun deserializeKZonedInstant() {
        val json = "{\"data\":\"2023-09-16T19:03:09.674+08:00\"}"
        val data = Json.decodeFromString<KZonedInstantData>(json)
        assertEquals(1694862189674, data.data.toEpochMilliseconds())
    }

    @Test
    fun serializeKInstant() {
        val data = KInstantData(KInstant.parseFrom(input = "2023-09-16T19:03:09.674+08:00", formats = listOf(KDateTimeFormat.FULL)))
        val json = Json.encodeToString(data)
        assertEquals("{\"data\":\"2023-09-16T11:03:09.674Z\"}", json)
    }

    @Test
    fun deserializeKInstant() {
        val json = "{\"data\":\"2023-09-16T11:03:09.674Z\"}"
        val data = Json.decodeFromString<KInstantData>(json)
        assertEquals(1694862189674, data.data.toEpochMilliseconds())
    }

    @Test
    fun serializeKInstantAsLong() {
        val data = KInstantAsLongData(KInstant.parseFrom(input = "2023-09-16T19:03:09.674+08:00", formats = listOf(KDateTimeFormat.FULL)))
        val json = Json.encodeToString(data)
        assertEquals("{\"data\":1694862189674}", json)
    }

    @Test
    fun deserializeKInstantAsLong() {
        val json = "{\"data\":1694862189674}"
        val data = Json.decodeFromString<KInstantAsLongData>(json)
        assertEquals(1694862189674, data.data.toEpochMilliseconds())
    }

    @Test
    fun serializeKZoneOffset() {
        KZoneOffset(9, 0).let {
            val json = Json.encodeToString(KZoneOffsetData(it))
            assertEquals("{\"data\":\"+09:00\"}", json)
        }
        KZoneOffset(13, 45).let {
            val json = Json.encodeToString(KZoneOffsetData(it))
            assertEquals("{\"data\":\"+13:45\"}", json)
        }
        KZoneOffset(-5, 30).let {
            val json = Json.encodeToString(KZoneOffsetData(it))
            assertEquals("{\"data\":\"-05:30\"}", json)
        }
        KZoneOffset(0, 0).let {
            val json = Json.encodeToString(KZoneOffsetData(it))
            assertEquals("{\"data\":\"Z\"}", json)
        }
    }

    @Test
    fun deserializeKZoneOffset() {
        "{\"data\":\"+09:00\"}".let {
            val data = Json.decodeFromString<KZoneOffsetData>(it)
            assertEquals(9, data.data.hours)
            assertEquals(0, data.data.minutes)
        }
        "{\"data\":\"+13:45\"}".let {
            val data = Json.decodeFromString<KZoneOffsetData>(it)
            assertEquals(13, data.data.hours)
            assertEquals(45, data.data.minutes)
        }
        "{\"data\":\"-05:30\"}".let {
            val data = Json.decodeFromString<KZoneOffsetData>(it)
            assertEquals(-5, data.data.hours)
            assertEquals(30, data.data.minutes)
        }
        "{\"data\":\"Z\"}".let {
            val data = Json.decodeFromString<KZoneOffsetData>(it)
            assertEquals(0, data.data.hours)
            assertEquals(0, data.data.minutes)
        }
    }

}