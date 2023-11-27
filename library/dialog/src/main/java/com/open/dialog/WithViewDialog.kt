//package com.open.dialog
//
//import android.content.Context
//import android.view.Gravity
//import android.view.View
//import android.view.Window
//import androidx.databinding.ObservableField
//import androidx.databinding.ObservableInt
//import com.open.binding.binding
//import com.open.core.BarUtils
//import com.open.dialog.BaseDialogFragment
//import com.open.libs.databinding.DialogWithViewBinding
//
//TODO:WithViewDialog

//class WithViewDialog : BaseDialogFragment() {
//
//    private val binding: DialogWithViewBinding by binding(this)
//
//    val valueTitle = ObservableField<String>()
//    private val valueDialogWidth = ObservableInt(300)
//    private val valueDialogHeight = ObservableInt(100)
//    private val valueX = ObservableInt()
//    private val valueY = ObservableInt()
//
//
//    override fun bindLayout() = 0
//
//    override fun setWindowStyle(window: Window?) {
//        window?.apply {
//            decorView.setPadding(0, 0, 0, 0)
//            attributes = attributes.apply {
//                dimAmount = 0f
//                alpha = 0.5f
//                width = valueDialogWidth.get()
//                height = valueDialogHeight.get()
//                gravity = Gravity.TOP or Gravity.START
//                x = valueX.get()
//                y = valueY.get()
//            }
//            setWindowAnimations(com.open.dialog.R.style.IOSAnimStyle)
//        }
//
//
//    }
//
//    override fun bindView(contentView: View) {
//        isCancelable = true
//        setCanceledOnTouchOutside(true)
//        binding.setVariable(BR.dialog, this)
//    }
//
//
//    private fun measurementTagView(tagView: View, gravity: Int): IntArray {
//        //TagView 宽高
//        val viewWidth: Int = tagView.width
//        val viewHeight: Int = tagView.height
//
//
//        //TagView 屏幕的绝对坐标
//        val location = IntArray(2)
//        tagView.getLocationOnScreen(location)
//        val viewX = location[0]//(ViewLeft)
//        val viewY = location[1] //(StatusBarHeight+ViewTop)
//
//        //状态栏高度
//        val statusBarHeight = BarUtils.getStatusBarHeight(tagView.context)
////        val statusBarHeight1 = BarUtils.getStatusBarHeight(ContextHolder.context)
////        val statusBarHeight2 = BarUtils.getStatusBarHeight()
////        Log.d(TAG, "statusBarHeight: $statusBarHeight")
////        Log.d(TAG, "statusBarHeight1: $statusBarHeight1")
////        Log.d(TAG, "statusBarHeight2: $statusBarHeight2")
//
//        // dialog 宽高 (非固定,需要测量)
//        val dialogWidth = valueDialogWidth.get()
//        val dialogHeight = valueDialogHeight.get()
//
//
//        //dialog 坐标
//        val dialogLocation = IntArray(2)
//
//
//        if (Gravity.TOP == gravity) {
//            val dialogX = (viewX - ((dialogWidth - viewWidth) / 2))//居中
//            val dialogY = (viewY - statusBarHeight - dialogHeight)
//
//            dialogLocation[0] = dialogX
//            dialogLocation[1] = dialogY
//        }
//
//        if (Gravity.BOTTOM == gravity) {
//            val dialogX = (viewX - ((dialogWidth - viewWidth) / 2))//居中
//            val dialogY = (viewY - statusBarHeight + viewHeight)
//
//            dialogLocation[0] = dialogX
//            dialogLocation[1] = dialogY
//        }
//
//        if (Gravity.START == gravity) {
//            val dialogX = (viewX - dialogWidth)
//            val dialogY = (viewY - statusBarHeight + ((viewHeight - dialogHeight) / 2))//居中
//
//            dialogLocation[0] = dialogX
//            dialogLocation[1] = dialogY
//        }
//
//        if (Gravity.END == gravity) {
//            val dialogX = (viewX + viewWidth)
//            val dialogY = (viewY - statusBarHeight + ((viewHeight - dialogHeight) / 2))//居中
//
//            dialogLocation[0] = dialogX
//            dialogLocation[1] = dialogY
//        }
//
//        return dialogLocation
//
//    }
//
//    /*========================================================================================*/
//
//
//    fun with(): WithViewDialog {
//        return newInstance()
//    }
//
//    fun setTitle(title: String): WithViewDialog {
//        valueTitle.set(title)
//        return this
//    }
//
//    fun setTagView(view: View, gravity: Int = Gravity.BOTTOM): WithViewDialog {
//        LogCatUtil.d(TAG, "gravity: $gravity")
//        val dialogLocation = measurementTagView(view, gravity)
//        valueX.set(dialogLocation[0])
//        valueY.set(dialogLocation[1])
//        return this
//    }
//
//    fun showDialog(context: Context): WithViewDialog {
//        show(context)
//        return this
//    }
//
//    companion object {
//        private var TAG: String = "WithViewDialog"
//        private fun newInstance(): WithViewDialog {
//            return WithViewDialog()
//        }
//
//        fun build(): WithViewDialog {
//            return newInstance()
//        }
//    }
//}