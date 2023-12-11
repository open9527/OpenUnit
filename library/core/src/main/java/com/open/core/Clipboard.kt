@file:Suppress("unused")

package com.open.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.content.Context
import android.content.Intent
import android.net.Uri

fun CharSequence.copyToClipboard(label: CharSequence? = null) =
  (ContextHolder.get().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newPlainText(label, this))

fun Uri.copyToClipboard(label: CharSequence? = null) =
  (ContextHolder.get().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newUri(contentResolver, label, this))

fun Intent.copyToClipboard(label: CharSequence? = null) =
  (ContextHolder.get().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newIntent(label, this))

fun getTextFromClipboard(): CharSequence? =
  (ContextHolder.get().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(ContextHolder.get())

fun clearClipboard() =
  (ContextHolder.get().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newPlainText(null, ""))

fun doOnClipboardChanged(listener: OnPrimaryClipChangedListener): OnPrimaryClipChangedListener =
  (ContextHolder.get().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .addPrimaryClipChangedListener(listener).let { listener }

fun OnPrimaryClipChangedListener.cancel() =
  (ContextHolder.get().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .removePrimaryClipChangedListener(this)
