package com.open.image


import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.imageLoader
import coil.load
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.util.DebugLogger
import okhttp3.Dispatcher
import okhttp3.OkHttpClient


fun initCoil(app: Application, okHttpClient: OkHttpClient = OkHttpClient.Builder().build()) {
    Coil.setImageLoader(
        ImageLoader.Builder(app).memoryCache {
            MemoryCache.Builder(app)
                .maxSizePercent(0.25)
                .build()
        }.components {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
            add(SvgDecoder.Factory())
            add(VideoFrameDecoder.Factory())

        }.okHttpClient {
            okHttpClient.apply {
                Dispatcher().apply { maxRequestsPerHost = maxRequests }
            }
        }.diskCache {
            DiskCache.Builder()
                .directory(app.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }.logger(DebugLogger())
            .fallback(ColorDrawable(Color.GRAY))
            .crossfade(true)
            .error(ColorDrawable(Color.GRAY))
            .build()
    )
}


fun ImageView.loadCircleCrop(data: Any, placeholderId: Int = 0, errorId: Int = 0) {
    this.load(data) {
        crossfade(true)
        placeholder(placeholderId)
        error(errorId)
        transformations(CircleCropTransformation())
    }
}

fun ImageView.loadRoundedCorners(data: Any, radius: Float) {
    this.loadRoundedCorners(data, radius, radius, radius, radius, 0, 0)
}

fun ImageView.loadRoundedCorners(
    data: Any,
    topLeft: Float,
    topRight: Float,
    bottomLeft: Float,
    bottomRight: Float
) {
    this.loadRoundedCorners(data, topLeft, topRight, bottomLeft, bottomRight, 0, 0)
}

fun ImageView.loadRoundedCorners(
    data: Any,
    topLeft: Float,
    topRight: Float,
    bottomLeft: Float,
    bottomRight: Float,
    placeholderId: Int = 0,
    errorId: Int = 0
) {
    this.load(data) {
        crossfade(true)
        placeholder(placeholderId)
        error(errorId)
        transformations(
            RoundedCornersTransformation(
                topLeft,
                topRight,
                bottomLeft,
                bottomRight
            )
        )
    }
}

fun Context.imageDownload(
    data: Any,
    onSuccess: (result: Drawable) -> Unit = {},
    onStart: (placeholder: Drawable?) -> Unit = {},
    onError: (error: Drawable?) -> Unit = {}
) {
    val request = ImageRequest.Builder(this)
        .data(data)
        .target(onStart, onError, onSuccess)
        .apply {}
        .build()
    this.imageLoader.enqueue(request)
}