package com.open.pkg.ui.media

import android.annotation.SuppressLint
import android.graphics.SweepGradient
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.open.base.BaseActivity
import com.open.core.CountDown
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
import kotlinx.coroutines.Job

class RecorderActivity : BaseActivity(R.layout.recorder_activity) {
    private val binding: RecorderActivityBinding by binding(this)

    private val viewModel: AlbumViewModel by viewModels()

    private val permissionManager = PermissionManager(this)


    private var countdownJob: Job? = null


    private val recorderUri by lazy {
        MediaUtils.createTempAudioUri()
    }
    private val mediaRecorderTask by lazy {
        MediaRecorderTask()
    }

    private var player: MusicPlayer? = null


    override fun initView() {
        binding.tvText.text = "需要开启录音->结束录音->开始播放"
        initCircleProgress()
        initWaveView()
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
            play()
        }, viewAlpha = true)

        binding.tvPause.addClick({
            player?.pause()
        }, viewAlpha = true)
        binding.tvStop.addClick({
            player?.onDestroyed()
        }, viewAlpha = true)

    }

    private fun play() {
        player?.start()
    }

    private fun initCircleProgress() {
        binding.circleView.setProgress(0, 1000)
        binding.circleView.width = 20
        binding.circleView.setColor(R.color.window_background_color)
//
//
        binding.circleView.setProgressWidth(18)
//        binding.circleView.setProgressColor(R.color.accent_color)
        binding.circleView.setProgressColor(R.color.purple_200, R.color.purple_700)
//        binding.circleView.setProgressColor(
//            intArrayOf(
//                R.color.purple_200,
//                R.color.purple_500,
//                R.color.purple_700
//            )
//        )
    }


    private fun initWaveView() {
        binding.waveView.waveColor = R.color.accent_color
        binding.waveView.baseLineColor = R.color.window_background_color
    }

    @SuppressLint("SetTextI18n")
    private fun startCountDownRecorder(duration: Int) {
        var count = 0
        countdownJob = CountDown.countDownCoroutine(duration, 1, 0, {
            LogUtils.d("倒计时变更: ${it}s")
            count++
            binding.circleView.setProgress(((count / 60f) * 100).toInt(), 1000)
            binding.tvCount.text = "${it}s"
        }, {
            LogUtils.d("倒计时开始: ${binding.waveView.dataList.size}")
        }, {
            LogUtils.d("倒计时结束: ${binding.waveView.dataList.size}")
            binding.tvCount.text = "完成"
            stopRecorder()
        }, lifecycleScope)

    }

    @SuppressLint("SetTextI18n")
    private fun startRecorder() {
        mediaRecorderTask.setPeriod(200)
        mediaRecorderTask.start(recorderUri.second)
        binding.waveView.visibility = View.VISIBLE
        startCountDownRecorder(59)
        mediaRecorderTask.setOnVolumeChangeListener {
//            LogUtils.d("startRecorder: $it")
            binding.tvText.text = "正在录音,音量大小:${it}"
            binding.waveView.addData(it * 500)
        }
    }

    private fun stopRecorder() {
        mediaRecorderTask.stop()
        countdownJob?.cancel()
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