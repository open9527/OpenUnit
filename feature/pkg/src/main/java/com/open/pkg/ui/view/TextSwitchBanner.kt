package com.open.pkg.ui.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.annotation.AnimRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.open.core.HandlerAction
import com.open.core.LogUtils
import com.open.core.ViewClickUtils.addClick


class TextSwitchBanner(val context: Context) :
    ViewSwitcher.ViewFactory, LifecycleEventObserver, HandlerAction {
    private var index = 0
    private val delayTime = 2000L
    private lateinit var textSwitcher: TextSwitcher
    private var textList: List<String> = listOf()
    private var iTextSwitchBannerListener: ITextSwitchBannerListener? = null

    override fun makeView(): View {
        return defaultTextView(context)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                start()
            }

            Lifecycle.Event.ON_PAUSE -> {
                stop()
            }

            Lifecycle.Event.ON_DESTROY -> {
                stop()
            }

            else -> {

            }
        }
    }


    fun bindTextSwitcher(textSwitcher: TextSwitcher): TextSwitchBanner {
        if (textSwitcher.childCount > 0) {
            textSwitcher.removeAllViews()
        }
        this.textSwitcher = textSwitcher
        textSwitcher.setFactory(this)
        textSwitcher.setText("")
        textSwitcher.addClick({
            iTextSwitchBannerListener?.let {
                it.onClick(textSwitcher, textList[index], index)
            }
        }, viewScale = false)
        textSwitcher.inAnimation = AnimationUtils.loadAnimation(
            context,
            com.open.dialog.R.anim.window_bottom_in
        )
        textSwitcher.outAnimation = AnimationUtils.loadAnimation(
            context,
            com.open.dialog.R.anim.window_top_out
        )
        return this
    }


    fun setTextList(textList: List<String>?): TextSwitchBanner {
        textList?.let {
            this.textList = textList
        }
        return this
    }

    fun setInAnimation(@AnimRes id: Int): TextSwitchBanner {
        textSwitcher.inAnimation = AnimationUtils.loadAnimation(
            context,
            id
        )
        return this
    }
    fun setOutAnimation(@AnimRes id: Int): TextSwitchBanner {
        textSwitcher.outAnimation = AnimationUtils.loadAnimation(
            context,
            id
        )
        return this
    }


    fun addListener(iTextSwitchBannerListener: ITextSwitchBannerListener): TextSwitchBanner {
        this.iTextSwitchBannerListener = iTextSwitchBannerListener
        return this
    }

    fun addLifecycleOwner(owner: LifecycleOwner?): TextSwitchBanner {
        owner?.lifecycle?.addObserver(this)
        return this
    }

    private fun start(): TextSwitchBanner {
        stop()
        postAtTime(task, delayTime)
        return this
    }

    private fun stop(): TextSwitchBanner {
        removeCallbacks(task)
        return this
    }

    private fun nextView() {
        index = ++index % textList.size
        textSwitcher.setText(textList[index])
    }


    private val task: Runnable = object : Runnable {
        override fun run() {
            nextView()
            postDelayed(this, delayTime * 2)
        }
    }

    private fun defaultTextView(context: Context): View {
        val textView = TextView(context)
        textView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ).apply {
            setMargins(50, 0, 0, 0)
        }
        textView.isSingleLine = true
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.setTextColor(Color.BLACK)
        return textView
    }

    interface ITextSwitchBannerListener {
        fun onClick(view: View, string: String, index: Int)
    }

    companion object {
        private fun newInstance(context: Context): TextSwitchBanner {
            LogUtils.d("newInstance")
            return TextSwitchBanner(context)
        }

        fun with(context: Context): TextSwitchBanner {
            LogUtils.d("with")
            return newInstance(context)
        }
    }
}