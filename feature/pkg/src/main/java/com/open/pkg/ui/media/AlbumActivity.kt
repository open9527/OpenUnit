package com.open.pkg.ui.media

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.open.base.BaseActivity
import com.open.core.LogUtils
import com.open.core.MediaUtils
import com.open.core.SizeUtils
import com.open.core.ViewClickUtils.addClick
import com.open.core.binding.binding
import com.open.core.toast
import com.open.permission.PermissionManager
import com.open.permission.PermissionsCallback
import com.open.permission.requestCamera
import com.open.permission.requestStorage
import com.open.pkg.R
import com.open.pkg.app.PkgRouter
import com.open.pkg.databinding.AlbumActivityBinding
import com.open.pkg.ui.media.cell.AlbumImageCell
import com.open.pkg.ui.mine.MineFragment
import com.open.recyclerview.adapter.BaseAdapter
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.diffCallback
import com.open.recyclerview.animations.ItemAnimation
import com.open.recyclerview.decoration.GridSpaceDecoration
import com.open.recyclerview.layoutmanager.WrapContentGridLayoutManager
import com.open.serialization.JsonClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AlbumActivity : BaseActivity(R.layout.album_activity) {

    private val binding: AlbumActivityBinding by binding(this)

    private val viewModel: AlbumViewModel by viewModels()

    private lateinit var permissionManager: PermissionManager

    private var cellList: MutableList<BaseCell> = mutableListOf()


    private val rvAdapter by lazy {
        BaseAdapter(diffCallback(), ItemAnimation.create().apply {
            duration(300)
            enabled(true)
            firstOnly(true)
            animation(animationType = ItemAnimation.FADE_IN)
        })
    }

    private val pictureUri by lazy {
        MediaUtils.createTempPictureUri()
    }
    private val videoUri by lazy {
        MediaUtils.createTempVideoUri()
    }


    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isPictureTaken: Boolean ->
            if (isPictureTaken) {
                lifecycleScope.launch(Dispatchers.IO) {
                    MediaUtils.updateFileToGallery(this@AlbumActivity, pictureUri.second)
                    MediaUtils.deleteByUri(pictureUri.first)
                    delay(1000L)
                    viewModel.queryAlbum()
                }

            } else {
                MediaUtils.deleteByUri(pictureUri.first)
            }
        }

    private val takeVideoLauncher =
        registerForActivityResult(ActivityResultContracts.CaptureVideo()) { isVideoTaken: Boolean ->
            if (isVideoTaken) {
                lifecycleScope.launch(Dispatchers.IO) {
                    MediaUtils.updateFileToGallery(
                        this@AlbumActivity,
                        videoUri.second,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    )
                    MediaUtils.deleteByUri(videoUri.first)
                    delay(1000L)
                    viewModel.queryAlbum()
                }

            } else {
                MediaUtils.deleteByUri(videoUri.first)

            }
        }


    override fun initView() {
        permissionManager = PermissionManager(this)
        binding.rvList.apply {
            layoutManager = WrapContentGridLayoutManager(context, 3)
            addItemDecoration(
                GridSpaceDecoration(
                    SizeUtils.dp2px(context, 2f).toInt(),
                )
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        RecyclerView.SCROLL_STATE_DRAGGING -> binding.faBtn.hide()
                        RecyclerView.SCROLL_STATE_IDLE -> binding.faBtn.show()
                    }
                }
            })

            adapter = rvAdapter
        }

        binding.faBtn.addClick({
            permissionManager.requestCamera(object : PermissionsCallback {
                override fun allow() {
                    when (viewModel.queryType.get()) {
                        QUERY_VIDEO -> {
                            takeVideo()
                        }

                        QUERY_IMAGE -> {
                            takePhoto()
                        }

                        else -> {
                            takePhoto()
                        }
                    }

                }

                override fun deny() {
                    this@AlbumActivity.toast("requestCamera is deny")
                }
            })

        }, viewAlpha = true)

        permissionManager.requestStorage(object : PermissionsCallback {
            override fun allow() {
                viewModel.queryAlbum()
            }
        })
    }

    override fun initData() {
        intent.extras?.let { bundle ->
            viewModel.queryType.set(bundle.getString(QUERY_TYPE, QUERY_ALL))
        }
        viewModel.mediaResult.observe(this) {
            cellList.clear()
            LogUtils.d("mediaResult: ${JsonClient.toJson(it)}")
            it?.forEach { mediaBean ->
                cellList.add(AlbumImageCell(mediaBean) { string ->
                    PkgRouter.navigationResult(this, MineFragment.ALBUM_RESULT_CODE,
                        Intent().apply {
                            putExtra(MineFragment.ALBUM_RESULT_URI, string)
                        })
                    finish()
                })
            }
            rvAdapter.submitList(cellList)

        }

    }

    private fun takePhoto() {
        takePictureLauncher.launch(pictureUri.first)
    }

    private fun takeVideo() {
        takeVideoLauncher.launch(videoUri.first)
    }


    companion object {
        const val QUERY_TYPE = "query_type"
        const val QUERY_ALL = "all"
        const val QUERY_IMAGE = "image"
        const val QUERY_VIDEO = "video"
        const val QUERY_AUDIO = "audio"
    }
}