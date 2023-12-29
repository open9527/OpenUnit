package com.open.pkg.ui.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.open.base.BaseFragment
import com.open.core.LogUtils
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.ProjectFragmentBinding
import com.open.pkg.net.api.WanApiImpl
import com.open.pkg.net.vo.ProjectClassificationVo
import com.open.pkg.ui.main.MainViewModel
import com.open.pkg.ui.project.content.ProjectContentFragment
import com.open.serialization.JsonClient

class ProjectFragment : BaseFragment(R.layout.project_fragment) {

    private val binding: ProjectFragmentBinding by binding(this)

    private val viewModel: ProjectViewModel by viewModels()

    private val mainViewModel: MainViewModel by applicationViewModels()

    private var fragmentList: MutableList<Fragment> = mutableListOf()
    private var projectClassificationList: MutableList<ProjectClassificationVo> = mutableListOf()

    private val pageAdapter by lazy {
        ProjectFragmentPagerAdapter(childFragmentManager, lifecycle)
    }

    override fun initView() {

    }

    override fun initData() {
        requestProject()
    }


    private fun requestProject() {
        WanApiImpl.requestProject().observe(viewLifecycleOwner) { projectResponse ->
            LogUtils.d("projectResponse:${JsonClient.toJson(projectResponse)}")
            if (projectResponse.isSuccessful) {
                projectResponse.data?.apply {
                    forEach { projectClassificationVo ->
                        projectClassificationList.add(projectClassificationVo)
                    }
                    pageAdapter.setDataList(this)
                }

                binding.viewPager.apply {
                    adapter = pageAdapter
                    offscreenPageLimit = 5
                }
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    tab.text = projectClassificationList[position].name
                }.attach()
            }
        }
    }
    companion object {
        private const val TAG: String = "ProjectFragment"
        private const val BUNDLE_KEY: String = "BUNDLE_KEY_PROJECT_FRAGMENT"
        fun newInstance(string: String): ProjectFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY, string)
            val fragment = ProjectFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}