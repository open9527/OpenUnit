package com.open.pkg.ui.web

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

class WebViewModel : ViewModel() {
    val valueProgressBar = ObservableInt(View.GONE)
    val valueProgress = ObservableInt(0)
}