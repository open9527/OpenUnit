package com.open.serialization

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.serializer
import java.io.ByteArrayOutputStream


@ExperimentalSerializationApi
class DynamicLookupSerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor =
        ContextualSerializer(Any::class, null, emptyArray()).descriptor

    @OptIn(InternalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Any) {
        val actualSerializer =
            encoder.serializersModule.getContextual(value::class) ?: value::class.serializer()
        encoder.encodeSerializableValue(actualSerializer as KSerializer<Any>, value)
    }

    override fun deserialize(decoder: Decoder): Any {
        error("Unsupported")
    }
}

object UriAsStringSerializer : KSerializer<Uri> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uri) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Uri = Uri.parse(decoder.decodeString())
}

@Serializable
data class BitmapSerialDescriptor(
    val width: Int,
    val height: Int,
    val hashCode: Int,
    val base64: String?,
    val byteArray: ByteArray?
)

object BitmapBase64Serializer : KSerializer<Bitmap> {
    override val descriptor: SerialDescriptor = BitmapSerialDescriptor.serializer().descriptor
    override fun serialize(encoder: Encoder, value: Bitmap) {
        val stream = ByteArrayOutputStream()
        val usingJson = encoder is JsonEncoder
        value.compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.toByteArray().also { byteArray ->
            BitmapSerialDescriptor(
                value.width, value.height, value.hashCode(),
                if (usingJson) Base64.encodeToString(byteArray, Base64.DEFAULT) else null,
                if (!usingJson) byteArray else null
            ).also {
                encoder.encodeSerializableValue(serializer(), it)
            }
        }
    }

    override fun deserialize(decoder: Decoder): Bitmap {
        decoder.decodeSerializableValue(BitmapSerialDescriptor.serializer()).also {
            val byteArray =
                if (decoder is JsonEncoder) Base64.decode(it.base64, 0) else it.byteArray!!
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }
}