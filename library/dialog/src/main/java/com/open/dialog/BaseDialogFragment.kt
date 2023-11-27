package com.open.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.open.core.HandlerAction
import com.open.core.KeyboardAction


abstract class BaseDialogFragment : DialogFragment(), HandlerAction, KeyboardAction,
    DialogInterface.OnKeyListener {

    @LayoutRes
    protected abstract fun bindLayout(): Int

    open fun bindTheme() = R.style.BaseDialogTheme

    open fun bindView(contentView: View) {

    }

    open fun setWindowStyle(window: Window?) {

//        设置 各种属性(动画,宽高,偏移量)
//        val attributes = window?.attributes
//        attributes?.let {
//            it.gravity = Gravity.CENTER
//            it.width = ViewGroup.LayoutParams.MATCH_PARENT
//            it.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            it.alpha = 0f
//            it.x = 100
//            it.y = 100
//            //设置背景遮盖层的透明度（前提条件是背景遮盖层开关必须是为开启状态）
//            it.dimAmount = 0f
//        }
//        window?.attributes = attributes
//        window?.setWindowAnimations(R.style.IOSAnimStyle)
//        //设置背景遮盖层开关
//        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    }


    override fun onStart() {
        super.onStart()
        setWindowStyle(dialog?.window)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (bindLayout() > 0) {
            return inflater.inflate(bindLayout(), container, false)

//            val binding: ViewDataBinding =
//                DataBindingUtil.inflate(inflater, bindLayout(), container, false)
//            binding.lifecycleOwner = this
//            return binding.root
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(this, view)
        dialog?.setOnKeyListener(this)

    }

    override fun getTheme(): Int {
        if (bindTheme() > 0) {
            val theme = bindTheme()
            if (theme != View.NO_ID) {
                return theme
            }
        }
        return super.getTheme()
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            dismiss()
            return true
        }
        return false
    }

    override fun onDetach() {
        super.onDetach()
        removeCallbacks()
        closeSoftInput()
    }

    override fun dismiss() {
        removeCallbacks()
        closeSoftInput()
        super.dismiss()
    }

    override fun onDestroyView() {
        removeCallbacks()
        closeSoftInput()
        super.onDestroyView()
    }


    open fun initView(dialog: BaseDialogFragment, contentView: View) {
        bindView(contentView)
    }

    open fun setCanceledOnTouchOutside(cancel: Boolean) {
        dialog?.setCanceledOnTouchOutside(cancel)
        isCancelable = cancel
    }

    open fun show(context: Context) {
        show(context, javaClass.simpleName.toString())
    }

    @SuppressLint("CommitTransaction")
    open fun show(context: Context, tag: String?) {
        getHandler().post {
            if (context is FragmentActivity) {
                if (isActivityAlive(context)) {
                    val fragmentManager = context.supportFragmentManager
                    val fragment = fragmentManager.findFragmentByTag(tag)
                    if (fragment != null) {
                        fragmentManager.beginTransaction().remove(fragment)
                    }
                    super@BaseDialogFragment.show(fragmentManager, tag)
                }
            }
        }
    }


    /**
     * 设置状态栏
     */
    open fun setStatusBar(view: View, @ColorInt color: Int, isLight: Boolean) {
        val window: Window = try {
            (context as Activity).window
        } catch (e: Exception) {
            ((context as ContextThemeWrapper).baseContext as Activity).window
        }
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color //设置状态栏底色
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = isLight //设置状态栏亮起
        }
    }


    /**
     * 设置导航栏
     */
    open fun setNavigationBar(view: View, @ColorInt color: Int, isLight: Boolean) {
        val window: Window = try {
            (context as Activity).window
        } catch (e: Exception) {
            ((context as ContextThemeWrapper).baseContext as Activity).window
        }
        window.navigationBarColor = color //设置导航栏底色
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightNavigationBars = isLight //设置导航栏亮起
        }
    }


    private fun closeSoftInput() {
        val focusView: View? = dialog?.currentFocus
        hideKeyboard(focusView)
//        dialog?.window?.context?.let {
//            ContextCompat.getSystemService(it, InputMethodManager::class.java)
//                ?.hideSoftInputFromWindow(focusView.windowToken, 0)
//        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun isActivityAlive(activity: Activity?): Boolean {
        return (activity != null && !activity.isFinishing
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed))
    }


}