package com.open.pkg.ui.mine

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.open.base.BaseFragment
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.MineFragmentBinding
import com.open.pkg.ui.main.MainViewModel
import com.open.pkg.ui.mine.cell.MineContentCell
import com.open.pkg.ui.mine.cell.MineUserInfoCell
import com.open.recyclerview.adapter.BaseAdapter
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.diffCallback
import com.open.recyclerview.animations.ItemAnimation
import com.open.recyclerview.layoutmanager.WrapContentLinearLayoutManager

class MineFragment : BaseFragment(R.layout.mine_fragment) {

    private val binding: MineFragmentBinding by binding(this)

    private val viewModel: MineViewModel by viewModels()

    private val mainViewModel: MainViewModel by applicationViewModels()


    private var cellList: MutableList<BaseCell> = mutableListOf()
    private lateinit var mineUserInfoCell: MineUserInfoCell


    private val rvAdapter by lazy {
        BaseAdapter(diffCallback(), ItemAnimation.create().apply {
            duration(300)
            enabled(true)
            firstOnly(false)
            animation(animationType = ItemAnimation.FADE_IN)
        })
    }

    private val startActivityForResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ALBUM_RESULT_CODE) {
                val data = result.data?.extras?.getString(ALBUM_RESULT_URI, "")
                data?.let {
                    mineUserInfoCell.valueAvatar.set(it)
                }
            }
        }


    override fun initView() {
        binding.rvList.apply {
            layoutManager = WrapContentLinearLayoutManager(context)
            adapter = rvAdapter
        }
    }

    override fun initData() {
        mineUserInfoCell = MineUserInfoCell(startActivityForResultLauncher)
        cellList.add(0, mineUserInfoCell)
        mutableListOf("我的积分", "我的收藏", "我的分享", "系统设置").forEach {
            cellList.add(MineContentCell(it))
        }
        rvAdapter.submitList(cellList)
    }


    companion object {
        private const val TAG: String = "MineFragment"
        private const val BUNDLE_KEY: String = "bundle_key_mine_fragment"
        const val ALBUM_RESULT_CODE = 9527001
        const val ALBUM_RESULT_URI: String = "album_result_uri"
        fun newInstance(string: String): MineFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY, string)
            val fragment = MineFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}