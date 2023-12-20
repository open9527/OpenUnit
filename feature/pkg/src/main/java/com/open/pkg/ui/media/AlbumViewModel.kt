package com.open.pkg.ui.media

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.open.pkg.app.PkgMediaLoader
import com.open.pkg.ui.media.AlbumActivity.Companion.QUERY_ALL
import com.open.pkg.ui.media.AlbumActivity.Companion.QUERY_AUDIO
import com.open.pkg.ui.media.AlbumActivity.Companion.QUERY_IMAGE
import com.open.pkg.ui.media.AlbumActivity.Companion.QUERY_VIDEO
import com.open.pkg.ui.media.vo.AlbumBean
import com.open.pkg.ui.media.vo.MediaBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumViewModel : ViewModel(), Runnable {

    val allAlbum = MutableLiveData<List<AlbumBean>>()
    val queryType = ObservableField(QUERY_ALL)
    val mediaResult = MutableLiveData<List<MediaBean>?>()


    override fun run() {
        when (queryType.get()) {
            QUERY_VIDEO -> {
                PkgMediaLoader.queryMediaVideo().apply {
                    mediaResult.postValue(this.second)
                }
            }

            QUERY_IMAGE -> {
                PkgMediaLoader.queryMediaImage().apply {
                    mediaResult.postValue(this.second)
                }
            }

            QUERY_AUDIO -> {
                PkgMediaLoader.queryMediaAudio().apply {
                    mediaResult.postValue(this.second)
                }
            }

            else -> {
                PkgMediaLoader.queryMediaAll().apply {
                    mediaResult.postValue(this.second)
                }
            }
        }
    }

    fun queryAlbum() {
        viewModelScope.launch(Dispatchers.IO) {
            run()
        }
    }


}
