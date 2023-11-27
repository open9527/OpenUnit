package com.open.core

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.util.Size
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

object UriUtils {

    fun fileToUri(context: Context, filePath: String): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(
                context,
                context.applicationInfo.packageName + ".provider",
                File(filePath)
            )
        }
        return Uri.fromFile(File(filePath))
    }

    fun fileToUri(context: Context,file: File) =
        fileToUri(context,file.path)


    fun uriToFile(context: Context, uri: Uri?): File? =
        if (uri == null) {
            null
        } else if (uri.scheme == ContentResolver.SCHEME_FILE)
            File(requireNotNull(uri.path))
        else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件保存到沙盒
            val contentResolver =context.contentResolver
            val displayName = "${System.currentTimeMillis()}${Random.nextInt(0, 9999)}.${
                MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(uri))
            }"
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                File("${context.cacheDir.absolutePath}/$displayName")
                    .apply {
                        val outputStream = FileOutputStream(this)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            FileUtils.copy(inputStream, outputStream)
                        }
                        outputStream.close()
                        inputStream.close()
                    }
            } else null
        } else null

    @RequiresApi(Build.VERSION_CODES.Q)
    fun uriToBitmap(context: Context,uri: Uri?): Bitmap? =
        if (uri == null) null
        else
            context.contentResolver.loadThumbnail(
                uri, Size(300, 300), null
            )

}