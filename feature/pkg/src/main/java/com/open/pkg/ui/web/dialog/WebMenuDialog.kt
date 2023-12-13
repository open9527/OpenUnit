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
import com.open.recyclerview.layoutmanager.WrapContentGridLayoutManager

class WebMenuDialog : BaseDialogFragment() {

    private val binding: WebMenuDialogBinding by binding(this)

    private val valueCells = ObservableField<List<BaseCell>>()


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
            layoutManager = WrapContentGridLayoutManager(context, 4)
            adapter = rvAdapter
        }
        initData()
        rvAdapter.submitList(valueCells.get())
    }

    private fun initData() {
        val menuTitles: MutableList<String> = mutableListOf(
            "上一级",
            "下一级",
            "分享",
            "刷新",
            "夜间模式",
            "浏览器",
            "复制链接",
            "退出"
        )
        val cells = mutableListOf<BaseCell>()
        menuTitles.forEach {
            cells.add(WebMenuCell(it))
        }
        valueCells.set(cells)
    }

    fun with(): WebMenuDialog {
        return newInstance()
    }


    fun showDialog(context: Context): WebMenuDialog {
        show(context)
        return this
    }

    companion object {
        private fun newInstance(): WebMenuDialog {
            return WebMenuDialog()
        }

        fun build(): WebMenuDialog {
            return newInstance()
        }
    }
}