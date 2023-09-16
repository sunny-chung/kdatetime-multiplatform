package com.sunnychung.lib.multiplatform.kdatetime.serializer

import com.sunnychung.lib.multiplatform.kdatetime.KInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias KInstantAsLong = @Serializable(with = KInstantAsLongSerializer::class) KInstant

class KInstantAsLongSerializer : KSerializer<KInstantAsLong> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("KInstantAsLong", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): KInstantAsLong {
        val input = decoder.decodeLong()
        return KInstant(timestampMs = input)
    }

    override fun serialize(encoder: Encoder, value: KInstantAsLong) {
        encoder.encodeLong(value.toEpochMilliseconds())
    }
}
