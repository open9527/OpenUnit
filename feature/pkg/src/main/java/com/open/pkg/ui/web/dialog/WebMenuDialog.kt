package com.open.pkg.ui.web.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.databinding.ObservableField
import com.open.core.binding.binding
import com.open.dialog.BaseDialogFragment
import com.open.pkg.R
import com.open.pkg.databinding.WebMenuDialogBinding
import com.open.recyclerview.adapter.BaseAdapter
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.diffCallback
import com.open.recyclerview.animations.ItemAnimation
import com.open.recyclerview.layoutmanager.grid

class WebMenuDialog : BaseDialogFragment() {

    private val binding: WebMenuDialogBinding by binding(this)

    private val valueCells = ObservableField<List<BaseCell>>()

    private val valueListener = ObservableField<(String) -> Unit>()


    private val rvAdapter by lazy {
        BaseAdapter(diffCallback(), ItemAnimation.create().apply {
            duration(300)
            enabled(true)
            animation(animationType = ItemAnimation.FADE_IN)
        })
    }

    override fun bindLayout() = R.layout.web_menu_dialog

    override fun setWindowStyle(window: Window?) {
        window?.apply {
            decorView.setPadding(0, 0, 0, 0)
            attributes = attributes.apply {
                dimAmount = 0.3f
//                alpha = 0.5f
                gravity = Gravity.BOTTOM
            }
            setWindowAnimations(com.open.dialog.R.style.BottomAnimStyle)
        }
    }

    override fun bindView(contentView: View) {
        isCancelable = true
        setCanceledOnTouchOutside(true)
        binding.dialog = this

        binding.rvList.apply {
            grid(context,4)
            adapter = rvAdapter
        }
        initData()
        rvAdapter.submitList(valueCells.get())
    }

    private fun initData() {
        val menuTitles: MutableList<String> = mutableListOf(
            "分享",
            "刷新",
            "夜间模式",
            "浏览器打开",
            "投诉",
            "复制链接",
            "听全文",
            "退出"
        )
        val cells = mutableListOf<BaseCell>()
        menuTitles.forEach {
            cells.add(WebMenuCell(it, valueListener.get()))
        }
        valueCells.set(cells)
    }

    fun addListener(listener: (string: String) -> Unit): WebMenuDialog {
        valueListener.set(listener)
        return this
    }

    fun showDialog(context: Context): WebMenuDialog {
        show(context)
        return this
    }

    companion object {
        private fun newInstance(): WebMenuDialog {
            return WebMenuDialog()
        }

        fun with(): WebMenuDialog {
            return newInstance()
        }
    }
}