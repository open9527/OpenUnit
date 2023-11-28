package com.open.net.okhttp.request

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressRequestBody(
    private val file: File,
    private val contentType: String,
    private val listener: ProgressListener
) : RequestBody() {

    override fun contentType(): MediaType? {
        return contentType.toMediaTypeOrNull()
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(file)
        var uploaded: Long = 0
        var uploadProgress: Int = 0
        inputStream.use { input ->
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                val progress = (100 * uploaded / fileLength).toInt()

                onProgressUpdate(uploadProgress, progress, listener)
                uploadProgress = progress

            }
        }
    }

    private fun onProgressUpdate(
        uploadProgress: Int,
        progress: Int,
        listener: ProgressListener
    ) {
        val handler = Handler(Looper.getMainLooper())
        if (progress == uploadProgress) {
            return
        }
        handler.post {
            if (100 == progress) {
                listener.onUploadComplete()

            }
            listener.onProgress(progress)
        }
    }

    private companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}
