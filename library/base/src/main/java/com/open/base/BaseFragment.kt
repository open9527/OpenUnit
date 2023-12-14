package com.open.base


import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId), BaseView {

    private var initialized: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        if (!initialized && !isHidden) {
            initView()
            initData()
            initialized = true
        }
    }

    override fun initData() {
    }

}