package com.open.net.okhttp.request

import java.io.File

interface ProgressListener {
    fun onProgress(progress: Int) {}

    fun onComplete(file: File) {}

    fun onUploadComplete() {}

    fun onError(error: Exception) {}
}