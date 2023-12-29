package com.open.pkg.ui.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.open.pkg.net.vo.ProjectClassificationVo
import com.open.pkg.ui.project.content.ProjectContentFragment

class ProjectFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var dataList: List<ProjectClassificationVo> = listOf()
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createFragment(position: Int): Fragment {
        return ProjectContentFragment.newInstance(dataList[position])
    }

    fun setDataList(dataList: List<ProjectClassificationVo>) {
        this.dataList = dataList
    }
}