package com.open.pkg.app.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer {
    private MediaPlayer mPlayer;
    private Context mContext;

    private boolean isInitialized = false;//是否已初始化
    private Thread initThread;

    private Timer mTimer;
    private final Handler mHandler;

    public MusicPlayer(Context context, Uri uri) {
        mContext = context;

        mTimer = new Timer();//创建Timer
        mHandler = new Handler();//创建Handler

        initThread = new Thread(() -> init(uri));
        initThread.start();
    }

    private void init(Uri uri) {
        mPlayer = MediaPlayer.create(mContext, uri);
        isInitialized = true;
        mPlayer.setOnErrorListener((mp, what, extra) -> {
            //处理错误
            return false;
        });

//        //当装载流媒体完毕的时候回调
//        mPlayer.setOnPreparedListener(mp -> {
//
//        });
//
//        //播放完成监听
//        mPlayer.setOnCompletionListener(mp -> {
//            start();//播放完成再播放--实现单曲循环
//        });
//
//        //seekTo方法完成回调
//        mPlayer.setOnSeekCompleteListener(mp -> {
//        });
//
        //网络流媒体的缓冲变化时回调
        mPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            if (mOnBufferListener != null) {
                mOnBufferListener.OnSeek(percent);
            }
        });

    }

    /**
     * 播放
     */
    public void start() {
        //未初始化和正在播放时return
        if (!isInitialized && mPlayer.isPlaying()) {
            return;
        }
        mPlayer.start();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isPlaying()) {
                    int pos = mPlayer.getCurrentPosition();
                    int duration = mPlayer.getDuration();
                    mHandler.post(() -> {
                        if (mOnSeekListener != null) {
                            mOnSeekListener.OnSeek((int) (pos * 1.f / duration * 100));
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    /**
     * 是否正在播放
     */
    public boolean isPlaying() {
        //未初始化和正在播放时return
        if (!isInitialized) {
            return false;
        }
        return mPlayer.isPlaying();
    }

    /**
     * 销毁播放器
     */
    public void onDestroyed() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();//释放资源
            mPlayer = null;
        }
        mTimer.cancel();
        mHandler.removeCallbacksAndMessages(null);
        isInitialized = false;
    }

    /**
     * 停止播放器
     */
    private void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    /**
     * 暂停播放器
     */
    public void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }


    //------------设置缓存进度监听-----------
    public interface OnBufferListener {
        void OnSeek(int per_100);
    }

    private MusicPlayer.OnBufferListener mOnBufferListener;

    public void setOnBufferListener(MusicPlayer.OnBufferListener onBufferListener) {
        mOnBufferListener = onBufferListener;
    }


    public interface OnSeekListener {
        void OnSeek(int per_100);
    }

    private OnSeekListener mOnSeekListener;

    public void setOnSeekListener(OnSeekListener onSeekListener) {
        mOnSeekListener = onSeekListener;
    }
}


