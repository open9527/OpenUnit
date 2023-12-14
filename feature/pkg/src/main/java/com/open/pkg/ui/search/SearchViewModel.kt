package com.open.pkg.ui.search

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    val valueKeyword = ObservableField<String>("")
}