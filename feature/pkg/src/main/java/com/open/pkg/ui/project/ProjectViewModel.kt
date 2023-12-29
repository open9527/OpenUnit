package com.open.pkg.ui.project

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import com.open.pkg.net.vo.ProjectClassificationVo

class ProjectViewModel: ViewModel() {

    val dataList =ObservableArrayList<ProjectClassificationVo>()
}