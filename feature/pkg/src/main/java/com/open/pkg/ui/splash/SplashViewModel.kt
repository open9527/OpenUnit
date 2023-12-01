package com.open.pkg.ui.splash

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
    public final val valueCount = ObservableField<String>("start")
}