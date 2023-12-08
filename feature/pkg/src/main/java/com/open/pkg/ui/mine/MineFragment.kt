package com.open.pkg.ui.mine

import androidx.fragment.app.viewModels
import com.open.base.BaseFragment
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.MineFragmentBinding
import com.open.pkg.ui.main.MainViewModel

class MineFragment: BaseFragment(R.layout.mine_fragment) {

    private val binding: MineFragmentBinding by binding(this)

    private val viewModel: MineViewModel by viewModels()

    private val mainViewModel: MainViewModel by applicationViewModels()
    override fun initView() {

    }
}