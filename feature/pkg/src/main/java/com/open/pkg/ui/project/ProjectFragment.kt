package com.open.pkg.ui.project

import androidx.fragment.app.viewModels
import com.open.base.BaseFragment
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.ProjectFragmentBinding
import com.open.pkg.ui.main.MainViewModel

class ProjectFragment: BaseFragment(R.layout.project_fragment) {

    private val binding: ProjectFragmentBinding by binding(this)

    private val viewModel: ProjectViewModel by viewModels()

    private val mainViewModel: MainViewModel by applicationViewModels()

    override fun initView() {

    }
}