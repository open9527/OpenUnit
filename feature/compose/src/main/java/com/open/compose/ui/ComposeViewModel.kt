package com.open.compose.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ComposeViewModel: ViewModel() {
    var message= mutableStateOf("Hello Compose")
    var imageUrl= mutableStateOf("https://picsum.photos/300/300")
}