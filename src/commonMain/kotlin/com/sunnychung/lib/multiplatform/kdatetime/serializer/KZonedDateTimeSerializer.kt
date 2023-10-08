package com.sunnychung.lib.multiplatform.kdatetime.serializer

import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import com.sunnychung.lib.multiplatform.kdatetime.KZonedInstant
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class KZonedDateTimeSerializer : KSerializer<KZonedDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("KZonedInstant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): KZonedDateTime {
        val input = decoder.decodeString()
        return KZonedInstant.parseFromIso8601String(input = input).toKZonedDateTime()
    }

    override fun serialize(encoder: Encoder, value: KZonedDateTime) {
        encoder.encodeString(value.toKZonedInstant().format(KDateTimeFormat.FULL.pattern))
    }
}
