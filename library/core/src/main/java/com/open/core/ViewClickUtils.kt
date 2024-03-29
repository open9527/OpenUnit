package com.open.core

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View


object ViewClickUtils {
    @SuppressLint("ClickableViewAccessibility")
    fun View.addClick(
                listener: (view:View?) -> Unit,
//        listener: () -> Unit,
        debounce: Boolean = true,
        debounceDefault: Long = 500L,
        viewScale: Boolean = true,
        scaleDefault: Float = 0.9f,
        duration: Long = 120L,
        viewAlpha: Boolean = false,
        alphaDefault: Float = 0.7f
    ) {
        debounceClick(listener, debounce, debounceDefault)
        alphaScaleClick(viewScale, scaleDefault, duration, viewAlpha, alphaDefault)

    }


   private fun View.debounceClick(
        listener: (view:View?) -> Unit,
//        listener: () -> Unit,
        debounce: Boolean,
        debounceDefault: Long = 500L,
    ) {
        if (debounce) {
            var lastTime = 0L
            this.setOnClickListener {
                val tmpTime = System.currentTimeMillis()
                if (tmpTime - lastTime > debounceDefault) {
                    lastTime = tmpTime
                    listener(this)
                }
            }
        } else {
//            this.setOnClickListener { listener(this) }
            this.setOnClickListener (listener)
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private  fun View.alphaScaleClick(
        viewScale: Boolean = false,
        scale: Float = 0.9f,
        duration: Long = 120L,
        viewAlpha: Boolean = false,
        alpha: Float = 0.8f
    ) {
        this.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (viewScale)
                        this.animate().scaleX(scale).scaleY(scale).setDuration(duration)
                            .start()
                    if (viewAlpha) this.alpha = alpha
//                    if (viewAlpha) this.background.alpha = (alpha * 255).toInt()
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (viewScale) this.animate().scaleX(1f).scaleY(1f).setDuration(duration)
                        .start()
                    if (viewAlpha) this.alpha = 1.0f
//                    if (viewAlpha) this.background.alpha = (1.0f * 255).toInt()

                }
            }
            return@setOnTouchListener false
        }
    }
}
