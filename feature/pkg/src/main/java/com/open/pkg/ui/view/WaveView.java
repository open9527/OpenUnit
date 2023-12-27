package com.open.pkg.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class WaveView extends View {
    private ArrayList<Float> dataList = new ArrayList<>();
    private ArrayList<Float> datas = new ArrayList<>();
    private float max = 300;
    private float mWidth;
    private float mHeight;
    private float space = 20f;
    private Paint mWavePaint;
    private Paint baseLinePaint;
    private int mWaveColor = Color.RED;
    private int mBaseLineColor = Color.GRAY;
    private float waveStrokeWidth = 10f;
    private int invalidateTime = 1000 / 100;
    private long drawTime;
    private boolean isMaxConstant = false;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        initPainters();

    }

    private void initPainters() {
        mWavePaint = new Paint();
        mWavePaint.setColor(mWaveColor);// 画笔为color
        mWavePaint.setStrokeWidth(waveStrokeWidth);// 设置画笔粗细
        mWavePaint.setAntiAlias(true);
        mWavePaint.setFilterBitmap(true);
        mWavePaint.setStrokeCap(Paint.Cap.ROUND);
        mWavePaint.setStyle(Paint.Style.FILL);

        baseLinePaint = new Paint();
        baseLinePaint.setColor(mBaseLineColor);// 画笔为color
        baseLinePaint.setStrokeWidth(1f);// 设置画笔粗细
        baseLinePaint.setAntiAlias(true);
        baseLinePaint.setFilterBitmap(true);
        baseLinePaint.setStyle(Paint.Style.FILL);
    }

    public float getMax() {
        return max;
    }

    public void setMax(short max) {
        this.max = max;
    }

    public float getSpace() {
        return space;
    }

    public void setSpace(float space) {
        this.space = space;
    }

    public int getWaveColor() {
        return mWaveColor;
    }

    public void setWaveColor(int mWaveColor) {
        this.mWaveColor = ContextCompat.getColor(getContext(), mWaveColor);
        invalidateNow();
    }

    public int getBaseLineColor() {
        return mBaseLineColor;
    }

    public void setBaseLineColor(int mBaseLineColor) {
        this.mBaseLineColor = ContextCompat.getColor(getContext(), mBaseLineColor);
        invalidateNow();
    }

    public float getWaveStrokeWidth() {
        return waveStrokeWidth;
    }

    public void setWaveStrokeWidth(float waveStrokeWidth) {
        this.waveStrokeWidth = waveStrokeWidth;
        invalidateNow();
    }

    public int getInvalidateTime() {
        return invalidateTime;
    }

    public void setInvalidateTime(int invalidateTime) {
        this.invalidateTime = invalidateTime;
    }

    public boolean isMaxConstant() {
        return isMaxConstant;
    }

    public void setMaxConstant(boolean maxConstant) {
        isMaxConstant = maxConstant;
    }

    /**
     * 如果改变相应配置  需要刷新相应的paint设置
     */
    public void invalidateNow() {
        initPainters();
        invalidate();
    }

    public void addData(float data) {

        if (data < 0) {
            data = 0;
        }
        if (data > max && !isMaxConstant) {
            max = data;
        }
        if (datas.size() > mWidth / space) {
            synchronized (this) {
                datas.remove(0);
                datas.add(data);
            }
        } else {
            datas.add(data);
        }
        dataList.add(data);
        if (System.currentTimeMillis() - drawTime > invalidateTime) {
            invalidate();
            drawTime = System.currentTimeMillis();
        }
    }

    public List<Float> getDataList() {
        return dataList;
    }

    public void clear() {
        datas.clear();
        dataList.clear();
        invalidateNow();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(0, mHeight / 2);
        drawBaseLine(canvas);
        drawWave(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    private void drawWave(Canvas mCanvas) {
        for (int i = 0; i < datas.size(); i++) {
            float x = (i + 1) * space;
            float y = datas.get(i) / max * mHeight / 2;
            mCanvas.drawLine(x, -y, x, y, mWavePaint);
        }

    }

    private void drawBaseLine(Canvas mCanvas) {
        mCanvas.drawLine(0, 0, mWidth, 0, baseLinePaint);
    }
}
