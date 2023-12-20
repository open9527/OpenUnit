package com.open.core

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.random.Random

object MediaUtils {
    private const val CAMERA_CACHE = "camera_cache"
    private const val SUFFIX_IMAGE = ".jpg"
    private const val SUFFIX_VIDEO = ".mp4"
    private const val SUFFIX_AUDIO = ".m4a"


    fun createTempPictureUri(
        storageDir: File? = ContextHolder.get().getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )
    ): Pair<Uri, File> {
        val pictureFileName: String = "IMG_" + createTimeTemp() + "_"

//        创建指定文件夹 需要在file_paths 中定义
//        val storageDir = File(ContextHolder.get().filesDir, CAMERA_CACHE)
//        if (!storageDir.exists()) {
//            storageDir.mkdirs()
//        }
        val file = File.createTempFile(pictureFileName, SUFFIX_IMAGE, storageDir)
        return Pair(UriUtils.fileToUri(ContextHolder.get(), file), file)
    }

    fun createTempVideoUri(
        storageDir: File? = ContextHolder.get().getExternalFilesDir(
            Environment.DIRECTORY_MOVIES
        )
    ): Pair<Uri, File> {
        val videoFileName: String = "VID_" + createTimeTemp() + "_"

//        创建指定文件夹 需要在file_paths 中定义
//        val storageDir = File(ContextHolder.get().filesDir, CAMERA_CACHE)
//        if (!storageDir.exists()) {
//            storageDir.mkdirs()
//        }

        val file = File.createTempFile(videoFileName, SUFFIX_VIDEO, storageDir)
        return Pair(UriUtils.fileToUri(ContextHolder.get(), file), file)
    }


    fun createTempAudioUri(
        storageDir: File? = ContextHolder.get().getExternalFilesDir(
            Environment.DIRECTORY_MUSIC
        )
    ): Pair<Uri, File> {
        val audioFileName: String = "AUD_" + createTimeTemp() + "_"

//        创建指定文件夹 需要在file_paths 中定义
//        val storageDir = File(ContextHolder.get().filesDir, CAMERA_CACHE)
//        if (!storageDir.exists()) {
//            storageDir.mkdirs()
//        }

        val file = File.createTempFile(audioFileName, SUFFIX_AUDIO, storageDir)
        return Pair(UriUtils.fileToUri(ContextHolder.get(), file), file)
    }


    // 更新图片文件到系统相册
    //MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    //MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    suspend fun updateFileToGallery(
        context: Context,
        file: File,
        contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    ): Uri? {
        return suspendCancellableCoroutine { cont ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = ContentValues().run {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
                    put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(file.path))
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                    context.contentResolver.insert(
                        contentUri,
                        this
                    )

                }?.also { uri ->
                    context.contentResolver.openOutputStream(uri)?.let {
                        val fis = file.inputStream()
                        FileUtils.copy(fis, it)
                        fis.close()
                        it.close()
                    }

                }
                cont.resume(uri)
            } else {
                MediaScannerConnection.scanFile(
                    context, arrayOf(file.path), arrayOf(getMimeType(file.path))
                ) { _, uri ->
                    cont.resume(uri)
                }
            }
        }

    }

    fun deleteByUri(resUri: Uri) {
        ContextHolder.get().contentResolver.delete(resUri, null, null)
    }


    private fun createTimeTemp(): String = "${System.currentTimeMillis()}${Random.nextInt(0, 9999)}"

    //根据文件后缀名判断 MIME 类型
    private fun getMimeType(filePath: String): String? =
        MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filePath))

}