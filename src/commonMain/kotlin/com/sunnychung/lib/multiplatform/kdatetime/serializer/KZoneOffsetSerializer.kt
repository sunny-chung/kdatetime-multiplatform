package com.sunnychung.lib.multiplatform.kdatetime.serializer

import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class KZoneOffsetSerializer : KSerializer<KZoneOffset> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("KZoneOffset", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): KZoneOffset {
        val input = decoder.decodeString()
        return KZoneOffset.parseFrom(input)
    }

    override fun serialize(encoder: Encoder, value: KZoneOffset) {
        encoder.encodeString(value.toDisplayString())
    }
}
