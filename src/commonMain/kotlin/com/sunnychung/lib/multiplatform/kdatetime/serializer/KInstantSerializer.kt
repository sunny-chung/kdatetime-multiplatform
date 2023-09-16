package com.sunnychung.lib.multiplatform.kdatetime.serializer

import com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat
import com.sunnychung.lib.multiplatform.kdatetime.KInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class KInstantSerializer : KSerializer<KInstant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("KInstant", PrimitiveKind.STRING)

    // order of the list matters
    val FORMATS = listOf(KDateTimeFormat.FULL, KDateTimeFormat.ISO8601_DATETIME)

    override fun deserialize(decoder: Decoder): KInstant {
        val input = decoder.decodeString()
        return KInstant.parseFrom(input = input, formats = FORMATS)
    }

    override fun serialize(encoder: Encoder, value: KInstant) {
        encoder.encodeString(value.format(KDateTimeFormat.FULL.pattern))
    }
}
