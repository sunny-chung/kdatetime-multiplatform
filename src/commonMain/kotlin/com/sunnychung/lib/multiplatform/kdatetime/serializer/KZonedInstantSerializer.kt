package com.sunnychung.lib.multiplatform.kdatetime.serializer

import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import com.sunnychung.lib.multiplatform.kdatetime.KZonedInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class KZonedInstantSerializer : KSerializer<KZonedInstant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("KZonedInstant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): KZonedInstant {
        val input = decoder.decodeString()
        return KZonedInstant.parseFromIso8601String(input = input)
    }

    override fun serialize(encoder: Encoder, value: KZonedInstant) {
        encoder.encodeString(value.format(KDateTimeFormat.FULL.pattern))
    }
}
