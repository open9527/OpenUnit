package com.open.pkg.ui.media

import android.annotation.SuppressLint
import androidx.activity.viewModels
import com.open.base.BaseActivity
import com.open.core.LogUtils
import com.open.core.MediaUtils
import com.open.core.ViewClickUtils.addClick
import com.open.core.binding.binding
import com.open.permission.PermissionManager
import com.open.permission.PermissionsCallback
import com.open.permission.requestRecordAudio
import com.open.pkg.R
import com.open.pkg.app.media.MediaRecorderTask
import com.open.pkg.app.media.MusicPlayer
import com.open.pkg.databinding.RecorderActivityBinding

class RecorderActivity : BaseActivity(R.layout.recorder_activity) {
    private val binding: RecorderActivityBinding by binding(this)

    private val viewModel: AlbumViewModel by viewModels()

    private lateinit var permissionManager: PermissionManager

    private val recorderUri by lazy {
        MediaUtils.createTempAudioUri()
    }
    private val mediaRecorderTask by lazy {
        MediaRecorderTask()
    }

    private var player: MusicPlayer? = null


    override fun initView() {
        binding.tvText.text = "需要开启录音->结束录音->开始播放"
        permissionManager = PermissionManager(this)
        binding.tvStartRecorder.addClick({
            permissionManager.requestRecordAudio(object : PermissionsCallback {
                override fun allow() {
                    startRecorder()
                }
            })


        }, viewAlpha = true)

        binding.tvPauseRecorder.addClick({
            pauseRecorder()
        }, viewAlpha = true)

        binding.tvResumeRecorder.addClick({
            resumeRecorder()
        }, viewAlpha = true)
        binding.tvStopRecorder.addClick({
            stopRecorder()
            player = MusicPlayer(this, recorderUri.first)
            player?.setOnSeekListener {
                binding.tvText.text = "正在播放录音,播放进度:${it}%"
            }
        }, viewAlpha = true)

        binding.tvStart.addClick({
            player?.start()

        }, viewAlpha = true)

        binding.tvPause.addClick({
            player?.pause()
        }, viewAlpha = true)
        binding.tvStop.addClick({
            player?.onDestroyed()
        }, viewAlpha = true)

    }

    @SuppressLint("SetTextI18n")
    private fun startRecorder() {
        mediaRecorderTask.start(recorderUri.second)
        mediaRecorderTask.setOnVolumeChangeListener {
            LogUtils.d("startRecorder: $it")
            binding.tvText.text = "正在录音,音量大小:${it}"
        }
    }

    private fun stopRecorder() {
        mediaRecorderTask.stop()
    }

    private fun pauseRecorder() {
        mediaRecorderTask.pause()
    }

    private fun resumeRecorder() {
        mediaRecorderTask.resume()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopRecorder()
        player?.onDestroyed()
    }


}