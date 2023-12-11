@file:Suppress("unused")
package com.open.core

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.contentValuesOf

inline val contentResolver: ContentResolver get() = ContextHolder.get().contentResolver

inline val EXTERNAL_MEDIA_IMAGES_URI: Uri
  get() = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

inline val EXTERNAL_MEDIA_VIDEO_URI: Uri
  get() = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

inline val EXTERNAL_MEDIA_AUDIO_URI: Uri
  get() = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

@get:RequiresApi(Build.VERSION_CODES.Q)
inline val EXTERNAL_MEDIA_DOWNLOADS_URI: Uri
  get() = MediaStore.Downloads.EXTERNAL_CONTENT_URI

inline fun <R> ContentResolver.query(
  uri: Uri,
  projection: Array<String>? = null,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null,
  block: (Cursor) -> R
): R? =
  query(uri, projection, selection, selectionArgs, sortOrder)?.use(block)

inline fun <R> ContentResolver.queryFirst(
  uri: Uri,
  projection: Array<String>? = null,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null,
  block: (Cursor) -> R
): R? =
  query(uri, projection, selection, selectionArgs, sortOrder) {
    if (it.moveToFirst()) block(it) else null
  }

inline fun <R> ContentResolver.queryLast(
  uri: Uri,
  projection: Array<String>? = null,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null,
  block: (Cursor) -> R
): R? =
  query(uri, projection, selection, selectionArgs, sortOrder) {
    if (it.moveToLast()) block(it) else null
  }

inline fun <R> ContentResolver.queryMediaImages(
  projection: Array<String>? = null,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null,
  block: (Cursor) -> R
): R? =
  query(EXTERNAL_MEDIA_IMAGES_URI, projection, selection, selectionArgs, sortOrder, block)

inline fun <R> ContentResolver.queryMediaVideos(
  projection: Array<String>? = null,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null,
  block: (Cursor) -> R
): R? =
  query(EXTERNAL_MEDIA_VIDEO_URI, projection, selection, selectionArgs, sortOrder, block)

inline fun <R> ContentResolver.queryMediaAudios(
  projection: Array<String>? = null,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null,
  block: (Cursor) -> R
): R? =
  query(EXTERNAL_MEDIA_AUDIO_URI, projection, selection, selectionArgs, sortOrder, block)

@RequiresApi(Build.VERSION_CODES.Q)
inline fun <R> ContentResolver.queryMediaDownloads(
  projection: Array<String>? = null,
  selection: String? = null,
  selectionArgs: Array<String>? = null,
  sortOrder: String? = null,
  block: (Cursor) -> R
): R? =
  query(EXTERNAL_MEDIA_DOWNLOADS_URI, projection, selection, selectionArgs, sortOrder, block)

fun ContentResolver.insert(uri: Uri, vararg pairs: Pair<String, Any?>): Uri? =
  contentResolver.insert(uri, contentValuesOf(*pairs))

fun ContentResolver.insertMediaImage(vararg pairs: Pair<String, Any?>): Uri? =
  contentResolver.insert(EXTERNAL_MEDIA_IMAGES_URI, *pairs)

fun ContentResolver.insertMediaVideo(vararg pairs: Pair<String, Any?>): Uri? =
  contentResolver.insert(EXTERNAL_MEDIA_VIDEO_URI, *pairs)

fun ContentResolver.insertMediaAudio(vararg pairs: Pair<String, Any?>): Uri? =
  contentResolver.insert(EXTERNAL_MEDIA_AUDIO_URI, *pairs)

@RequiresApi(Build.VERSION_CODES.Q)
fun ContentResolver.insertMediaDownload(vararg pairs: Pair<String, Any?>): Uri? =
  contentResolver.insert(EXTERNAL_MEDIA_DOWNLOADS_URI, *pairs)

fun ContentResolver.update(
  @RequiresPermission.Write uri: Uri,
  vararg pairs: Pair<String, Any?>,
  where: String? = null,
  selectionArgs: Array<String>? = null
): Int =
  update(uri, contentValuesOf(*pairs), where, selectionArgs)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun ContentResolver.delete(
  @RequiresPermission.Write uri: Uri,
  where: String? = null,
  selectionArgs: Array<String>? = null
): Int =
  delete(uri, where, selectionArgs)
