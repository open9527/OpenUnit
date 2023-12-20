package com.open.pkg.ui.media.vo

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringDef
import com.open.serialization.BitmapBase64Serializer
import com.open.serialization.UriAsStringSerializer
import kotlinx.serialization.Serializable


@Serializable
data class AlbumBean(
    val name: String,
    val size: String,
)


@Serializable
data class MediaBean(
    val name: String,
    @Serializable(with = UriAsStringSerializer::class)
    val uri: Uri,
    val path: String,
    val width: Int = 0,
    val height: Int = 0,
    val duration: Long = 0,
    val size: Long = 0,
    val mimeType: String?,
    @Serializable(with = BitmapBase64Serializer::class)
    val bitmapThumbnail: Bitmap?,
    @AlbumType
    val type: String = AlbumType.ALBUM_TYPE_OTHER,
) {

    /**
     * 长图
     */
    fun longImage(): Boolean {
        val h = width * 3
        return height > h
    }

    /**
     * gif图
     */
    fun gifImage(): Boolean {
        return when (type) {
            "image/gif", "image/GIF" -> true
            else -> false
        }
    }

    fun isImage(): Boolean {
        return when (mimeType) {
            AlbumType.ALBUM_TYPE_IMAGE -> true
            else -> false
        }
    }

    fun isVideo(): Boolean {
        return when (mimeType) {
            AlbumType.ALBUM_TYPE_VIDEO -> true
            else -> false
        }
    }

    fun isAudio(): Boolean {
        return when (mimeType) {
            AlbumType.ALBUM_TYPE_AUDIO -> true
            else -> false
        }
    }


    override fun toString(): String {
        return "MediaBean(name='$name', uri=$uri, path='$path', width=$width, height=$height, mimeType='$mimeType')"
    }

}

@Target(
    AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.LOCAL_VARIABLE
)
@MustBeDocumented
@StringDef(
    AlbumType.ALBUM_TYPE_IMAGE,
    AlbumType.ALBUM_TYPE_VIDEO,
    AlbumType.ALBUM_TYPE_AUDIO,
    AlbumType.ALBUM_TYPE_OTHER

)
@Retention(AnnotationRetention.SOURCE)
annotation class AlbumType {
    companion object {
        const val ALBUM_TYPE_IMAGE = "image"
        const val ALBUM_TYPE_VIDEO = "video"
        const val ALBUM_TYPE_AUDIO = "audio"
        const val ALBUM_TYPE_OTHER = "other"
    }
}
