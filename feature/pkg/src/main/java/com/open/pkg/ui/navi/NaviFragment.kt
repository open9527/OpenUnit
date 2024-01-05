package com.open.pkg.ui.navi

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.open.base.BaseFragment
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.NaviFragmentBinding
import com.open.pkg.net.api.WanApiImpl
import com.open.pkg.ui.main.MainViewModel
import com.open.pkg.ui.navi.cell.NaviContentCell
import com.open.pkg.ui.navi.cell.NaviTitleCell
import com.open.recyclerview.adapter.BaseAdapter
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.diffCallback
import com.open.recyclerview.animations.ItemAnimation
import com.open.recyclerview.layoutmanager.grid

class NaviFragment : BaseFragment(R.layout.navi_fragment) {

    private val binding: NaviFragmentBinding by binding(this)

    private val viewModel: NaviViewModel by viewModels()

    private val mainViewModel: MainViewModel by applicationViewModels()


    private var cellList: MutableList<BaseCell> = mutableListOf()


    private val rvAdapter by lazy {
        BaseAdapter(diffCallback(), ItemAnimation.create().apply {
            duration(300)
            enabled(true)
            firstOnly(false)
            animation(animationType = ItemAnimation.FADE_IN)
        })
    }

    override fun initView() {
        binding.rvList.apply {
            grid(context, 2, spanSize = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (binding.rvList.adapter?.getItemViewType(position)) {
                        R.layout.navi_title_cell -> 2
                        R.layout.navi_content_cell -> 1
                        else -> 1
                    }
                }
            })
            adapter = rvAdapter
        }
    }

    override fun initData() {
        requestNavi()
    }


    private fun requestNavi() {
        WanApiImpl.requestNavi().observe(viewLifecycleOwner) { naviResponse ->
//            LogUtils.d("requestNavi:${JsonClient.toJson(naviResponse)}")
            if (naviResponse.isSuccessful) {
                naviResponse.data?.let { naviList ->
                    naviList.forEach { navigationVo ->
                        cellList.add(NaviTitleCell(navigationVo))
                        navigationVo.articles.forEach { article ->
                            cellList.add(NaviContentCell(article))
                        }
                    }

                }
                rvAdapter.submitList(cellList)
            }

        }
    }

    companion object {
        private const val TAG: String = "NaviFragment"
        private const val BUNDLE_KEY: String = "bundle_key_navi_fragment"
        fun newInstance(string: String): NaviFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY, string)
            val fragment = NaviFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}