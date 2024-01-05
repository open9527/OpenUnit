package com.open.pkg.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.open.pkg.ui.article.ArticleFragment
import com.open.pkg.ui.mine.MineFragment
import com.open.pkg.ui.navi.NaviFragment
import com.open.pkg.ui.project.ProjectFragment

class MainFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var dataList: List<Pair<String,Int>> = listOf()
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                ArticleFragment.newInstance(dataList[position].first)
            }

            1 -> {
                NaviFragment.newInstance(dataList[position].first)
            }
            2 -> {
                ProjectFragment.newInstance(dataList[position].first)
            }

            3 -> {
                MineFragment.newInstance(dataList[position].first)
            }

            else -> {
                ArticleFragment.newInstance(dataList[position].first)
            }
        }
    }

    fun setDataList(dataList: List<Pair<String,Int>>) {
        this.dataList = dataList
    }
}