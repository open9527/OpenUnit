package com.open.pkg.ui.project.content

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.open.base.BaseFragment
import com.open.core.LogUtils
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.ProjectContentFragmentBinding
import com.open.pkg.net.api.WanApiImpl
import com.open.pkg.net.vo.ProjectClassificationVo
import com.open.pkg.ui.project.ProjectViewModel
import com.open.pkg.ui.project.content.cell.ProjectContentCell
import com.open.recyclerview.adapter.BaseAdapter
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.diffCallback
import com.open.recyclerview.animations.ItemAnimation
import com.open.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.open.serialization.JsonClient
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class ProjectContentFragment : BaseFragment(R.layout.project_content_fragment) {

    private val binding: ProjectContentFragmentBinding by binding(this)

    private val viewModel: ProjectContentViewModel by viewModels()

    private val projectViewModel: ProjectViewModel by applicationViewModels()


    private var page: Int = 1
    private var id: String? = null
    private var cellList: MutableList<BaseCell> = mutableListOf()


    private val rvAdapter by lazy {
        BaseAdapter(diffCallback(), ItemAnimation.create().apply {
            duration(300)
            enabled(true)
            firstOnly(false)
            animation(animationType = ItemAnimation.SCALE_IN)
        })
    }

    override fun initView() {
        binding.rvList.apply {
            layoutManager = WrapContentLinearLayoutManager(context)
            adapter = rvAdapter
        }

        binding.refresh.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    refreshLayout.resetNoMoreData()
                    requestProjectList(page = 1)
                    refreshLayout.finishRefresh()
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    requestProjectList(page = ++page)
                    refreshLayout.finishLoadMore()
                }
            })
        }
    }

    override fun initData() {
        val data: ProjectClassificationVo? =
            JsonClient.fromJson<ProjectClassificationVo>(arguments?.getString(BUNDLE_KEY))
        id = data?.id
        requestProjectList(page)
    }

    private fun requestProjectList(page: Int) {
        if (1 == page) cellList.clear()
        WanApiImpl.requestProjectList(page, id).observe(viewLifecycleOwner) { projectListResponse ->
            LogUtils.d("projectListResponse:${JsonClient.toJson(projectListResponse)}")
            if (projectListResponse.isSuccessful) {
                if (page > 1 && projectListResponse.data?.list?.size == 0) {
                    binding.refresh.finishLoadMoreWithNoMoreData()
                }
                projectListResponse.data?.list?.forEach { projectVo ->
                    cellList.add(ProjectContentCell(projectVo))
                }
                if (1 == page) {
                    rvAdapter.submitList(cellList)
                } else {
                    rvAdapter.notifyItemRangeChanged(0, cellList.size, cellList.size)
                }
            }

        }
    }

    companion object {
        private const val TAG: String = "ProjectContentFragment"
        private const val BUNDLE_KEY: String = "BUNDLE_KEY_PROJECT_CONTENT_FRAGMENT"

        fun newInstance(projectClassificationVo: ProjectClassificationVo): ProjectContentFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY, JsonClient.toJson(projectClassificationVo))
            val fragment = ProjectContentFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}