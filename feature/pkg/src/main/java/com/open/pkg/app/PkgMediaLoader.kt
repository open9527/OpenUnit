package com.open.pkg.app

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Size
import com.open.core.ContextHolder
import com.open.pkg.ui.media.vo.AlbumType
import com.open.pkg.ui.media.vo.MediaBean
import java.io.File

object PkgMediaLoader {
    private const val SIZE_1024 = 1024
    private const val SIZE_1000 = 1000
    private const val SIZE_300 = 300

    private const val DEFAULT_BUCKET_NAME = "所有媒体"
    private const val DEFAULT_VIDEO_NAME = "所有视频"
    private const val DEFAULT_IMAGE_NAME = "所有图片"
    private const val DEFAULT_AUDIO_NAME = "所有音频"

    private val CONTENT_URI = MediaStore.Files.getContentUri("external")
    private val IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val VIDEO_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    private val AUDIO_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    private const val BUCKET_DISPLAY_NAME = "bucket_display_name"
    private const val ID = MediaStore.Files.FileColumns._ID
    private const val DATA = MediaStore.MediaColumns.DATA
    private const val DATE_MODIFIED = MediaStore.MediaColumns.DATE_MODIFIED
    private const val DISPLAY_NAME = MediaStore.MediaColumns.DISPLAY_NAME
    private const val MEDIA_TYPE = MediaStore.Files.FileColumns.MEDIA_TYPE

    //    private const val MIME_TYPE = MediaStore.Files.FileColumns.MIME_TYPE
    private const val MIME_TYPE = MediaStore.MediaColumns.MIME_TYPE
    private const val WIDTH = MediaStore.MediaColumns.WIDTH
    private const val HEIGHT = MediaStore.MediaColumns.HEIGHT
    private const val DURATION = MediaStore.MediaColumns.DURATION
    private const val SIZE = MediaStore.MediaColumns.SIZE

    private const val MEDIA_TYPE_IMAGE = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
    private const val MEDIA_TYPE_VIDEO = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
    private const val MEDIA_TYPE_AUDIO = MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO


    private val projection_media = arrayOf(
        ID,
        DATA,
        BUCKET_DISPLAY_NAME,
        DATE_MODIFIED,
        DISPLAY_NAME,
        MEDIA_TYPE,
        MIME_TYPE,
        HEIGHT,
        WIDTH,
        DURATION,
        SIZE,
    )

    private const val selectionMedia: String = "($MEDIA_TYPE=?) AND $SIZE>0"


    private const val sortOrderMedia = "$DATE_MODIFIED DESC"


    fun queryMediaImage(
        projection: Array<String>? = projection_media,
        selection: String? = selectionMedia,
        selectionArgs: Array<String>? = arrayOf(MEDIA_TYPE_IMAGE.toString()),
        sortOrder: String? = sortOrderMedia,
    ): Pair<HashMap<String, MutableList<MediaBean>>, MutableList<MediaBean>> =
        queryMediaAll(projection, selection, selectionArgs, sortOrder)

    fun queryMediaAudio(
        projection: Array<String>? = projection_media,
        selection: String? = selectionMedia,
        selectionArgs: Array<String>? = arrayOf(MEDIA_TYPE_AUDIO.toString()),
        sortOrder: String? = sortOrderMedia,
    ): Pair<HashMap<String, MutableList<MediaBean>>, MutableList<MediaBean>> =
        queryMediaAll(projection, selection, selectionArgs, sortOrder)

    fun queryMediaVideo(
        projection: Array<String>? = projection_media,
        selection: String? = selectionMedia,
        selectionArgs: Array<String>? = arrayOf(MEDIA_TYPE_VIDEO.toString()),
        sortOrder: String? = sortOrderMedia,
    ): Pair<HashMap<String, MutableList<MediaBean>>, MutableList<MediaBean>> =
        queryMediaAll(projection, selection, selectionArgs, sortOrder)


    fun queryMediaAll(
        projection: Array<String>? = projection_media,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = sortOrderMedia,
    ): Pair<HashMap<String, MutableList<MediaBean>>, MutableList<MediaBean>> {
        val allAlbum = HashMap<String, MutableList<MediaBean>>()
        val mediaResult: MutableList<MediaBean> = mutableListOf()
        ContextHolder.get().contentResolver.query(
            CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder,
        ).apply {
            if (this != null && this.moveToFirst()) {
                val pathIndex: Int = this.getColumnIndex(DATA)
                val mimeTypeIndex: Int = this.getColumnIndex(MIME_TYPE)
                val sizeIndex: Int = this.getColumnIndex(SIZE)
                val mediaTypeIndex = this.getColumnIndex(MEDIA_TYPE)
                val idIndex = this.getColumnIndex(ID)
//                val bucketNameIndex = this.getColumnIndex(BUCKET_DISPLAY_NAME)
                val nameIndex = this.getColumnIndex(DISPLAY_NAME)
                val widthIndex = this.getColumnIndex(WIDTH)
                val heightIndex = this.getColumnIndex(HEIGHT)
                val durationIndex = this.getColumnIndex(DURATION)
                do {
                    val mediaType = this.getInt(mediaTypeIndex)
                    val size: Long = this.getLong(sizeIndex)
                    val duration = this.getLong(durationIndex)

                    // 视频,音频 大小不得小于 10 KB,时长不得小于 1 秒
                    if ((MEDIA_TYPE_AUDIO == mediaType || MEDIA_TYPE_VIDEO == mediaType) && size < SIZE_1024 * 10 && duration < SIZE_1000) {
                        continue
                    }
                    // 图片大小不得小于 1 KB
                    if (MEDIA_TYPE_IMAGE == mediaType && size < SIZE_1024) {
                        continue
                    }
                    val mimeType: String? = this.getString(mimeTypeIndex)
                    val path: String = this.getString(pathIndex)
                    if (TextUtils.isEmpty(path) || TextUtils.isEmpty(mimeType)) {
                        continue
                    }
                    val file = File(path)
                    if (!file.exists() || !file.isFile) {
                        continue
                    }
                    val parentFile: File = file.parentFile ?: continue


                    val name = this.getString(nameIndex)
                    val width = this.getInt(widthIndex)
                    val height = this.getInt(heightIndex)


                    val id = this.getLong(idIndex)

                    val mediaUri: Uri?
                    @AlbumType var type: String

                    when (mediaType) {
                        MEDIA_TYPE_IMAGE -> {
                            mediaUri = ContentUris.withAppendedId(IMAGE_URI, id)
                            type = AlbumType.ALBUM_TYPE_IMAGE
                        }

                        MEDIA_TYPE_VIDEO -> {
                            mediaUri = ContentUris.withAppendedId(VIDEO_URI, id)
                            type = AlbumType.ALBUM_TYPE_VIDEO

                        }

                        MEDIA_TYPE_AUDIO -> {
                            mediaUri = ContentUris.withAppendedId(AUDIO_URI, id)
                            type = AlbumType.ALBUM_TYPE_AUDIO
                        }

                        else -> {
                            continue
                        }
                    }
                    // 获取目录名作为专辑名称
                    // val bucketName = this.getString(bucketNameIndex)
                    val albumName: String = parentFile.name
                    var data: MutableList<MediaBean>? = allAlbum[albumName]
                    if (data == null) {
                        data = ArrayList()
                        allAlbum[albumName] = data
                    }

                    val media = MediaBean(
                        name,
                        mediaUri,
                        path,
                        width,
                        height,
                        duration,
                        size,
                        mimeType,
                        null,
                        type,
                    )
                    data.add(media)
                    mediaResult.add(media)
                } while (this.moveToNext())
                this.close()
            }
        }
        return Pair(allAlbum, mediaResult)
    }


    private fun thumbnailImage(
        path: String, @AlbumType
        type: String
    ): Bitmap? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                if (AlbumType.ALBUM_TYPE_IMAGE == type) {
                    ThumbnailUtils.createImageThumbnail(
                        File(path), Size(100, 100),
                        CancellationSignal()
                    )
                } else if (AlbumType.ALBUM_TYPE_VIDEO == type) {
                    ThumbnailUtils.createVideoThumbnail(
                        File(path), Size(SIZE_300, SIZE_300),
                        CancellationSignal()
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

        } else {
            null
        }

}

