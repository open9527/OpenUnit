package com.open.core

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View



object ViewClickUtils {
    @SuppressLint("ClickableViewAccessibility")
    fun View.addClick(
        listener: (view: View) -> Unit,
        debounce: Boolean = true,
        debounceDefault: Long = 500L,
        viewScale: Boolean = true,
        scaleDefault: Float = 0.9f,
        duration: Long = 120,
        viewAlpha: Boolean = false,
        alphaDefault: Float = 0.7f
    ) {
        debounceClick(listener, debounce, debounceDefault)
        alphaScaleClick(viewScale, scaleDefault, duration, viewAlpha, alphaDefault)

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun View.debounceClick(
        listener: (View) -> Unit,
        debounce: Boolean,
        debounceDefault: Long = 500L,
    ) {
        if (debounce) {
            var lastTime = 0L
            this.setOnClickListener {
                val tmpTime = System.currentTimeMillis()
                if (tmpTime - lastTime > debounceDefault) {
                    lastTime = tmpTime
                    listener.invoke(this)
                }
            }
        } else {
            this.setOnClickListener(listener)
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun View.alphaScaleClick(
        viewScale: Boolean = false,
        scale: Float = 0.9f,
        duration: Long = 120,
        viewAlpha: Boolean = false,
        alpha: Float = 0.5f
    ) {
        this.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (viewScale)
                        this.animate().scaleX(scale).scaleY(scale).setDuration(duration)
                            .start()
                    if (viewAlpha) this.alpha = alpha

                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (viewScale) this.animate().scaleX(1f).scaleY(1f).setDuration(duration)
                        .start()
                    if (viewAlpha) this.alpha = 1.0f

                }
            }
            return@setOnTouchListener false
        }
    }
}
