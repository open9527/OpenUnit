package com.open.pkg.ui.article

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class ArticleViewModel: ViewModel() {

    val valueContent = ObservableField<String>()
}