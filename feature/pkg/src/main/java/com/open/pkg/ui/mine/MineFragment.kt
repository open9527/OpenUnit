package com.open.pkg.ui.mine

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.open.base.BaseFragment
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.MineFragmentBinding
import com.open.pkg.ui.article.ArticleFragment
import com.open.pkg.ui.main.MainViewModel

class MineFragment: BaseFragment(R.layout.mine_fragment) {

    private val binding: MineFragmentBinding by binding(this)

    private val viewModel: MineViewModel by viewModels()

    private val mainViewModel: MainViewModel by applicationViewModels()
    override fun initView() {

    }

    companion object {
        private const val TAG: String = "MineFragment"
        private const val BUNDLE_KEY: String = "BUNDLE_KEY_MINE_FRAGMENT"
        fun newInstance(string: String): MineFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY, string)
            val fragment = MineFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}