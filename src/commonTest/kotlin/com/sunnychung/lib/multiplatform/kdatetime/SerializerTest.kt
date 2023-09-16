package com.sunnychung.lib.multiplatform.kdatetime

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializerTest {
    @Serializable data class KInstantData(val data: KInstant)
    @Serializable data class KZonedInstantData(val data: KZonedInstant)

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

}