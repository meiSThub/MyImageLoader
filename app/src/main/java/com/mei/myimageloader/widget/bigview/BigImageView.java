package com.mei.myimageloader.widget.bigview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;

/**
 * @author mxb
 * @date 2020/10/25
 * @desc 加载长图的ImageView
 * @desired
 */
public class BigImageView extends View implements View.OnTouchListener,
        GestureDetector.OnGestureListener {

    private static final String TAG = "BigImageView";

    // 滑动处理
    private Scroller mScroller;

    // 手势处理
    private GestureDetector mGestureDetector;

    // 图片解码参数
    private BitmapFactory.Options mOptions;

    // 图片解码的区域
    private Rect mRect;

    // 要加载的图片的宽度
    private int mImageWidth;

    // 要加载的图片的高度
    private int mImageHeight;

    // 图片区域解码器
    private BitmapRegionDecoder mDecoder;

    // 控件的宽度
    private int mViewWidth;

    // 控件的高度
    private int mViewHeight;

    // 图片缩放的比例
    private float mScale;

    // 解码出来的区域图片
    private Bitmap mBitmap;

    // 图片缩放矩阵
    private Matrix mMatrix = new Matrix();

    public BigImageView(Context context) {
        super(context);
        init(context);
    }

    public BigImageView(Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 指定要加载的矩形区域
        mRect = new Rect();

        // 解码图片的配置
        mOptions = new BitmapFactory.Options();

        mGestureDetector = new GestureDetector(context, this);
        setOnTouchListener(this::onTouch);

        // 滑动帮助
        mScroller = new Scroller(context);
    }

    /**
     * 设置需要加载的图片
     *
     * @param inputStream 图片的输入流
     */
    public void setImageInputStream(InputStream inputStream) {
        // 读取图片的宽高
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, mOptions);

        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        Log.i(TAG, "setImageInputStream: mImageWidth=" + mImageWidth + ";mImageHeight="
                + mImageHeight);
        // 允许图片复用
        mOptions.inMutable = true;
        // 设置图片像素格式
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = false;

        // 创建图片区域解码器，用于解码图片
        try {
            mDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 重新计算控件的宽高，并重绘
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 1. 获取控件测量的宽高
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        Log.i(TAG, "onMeasure: mViewWidth=" + mViewWidth + ";mViewHeight=" + mViewHeight);
        // 如果解码器是null 表示没有设置过要现实的图片
        if (null == mDecoder) {
            return;
        }
        // 2. 初始化默认加载的图片区域
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mImageWidth;// 按照图片的宽度来加载
        // 图片宽度放大的比例，控件的宽度比图片的宽度大
        mScale = mViewWidth / (float) mImageWidth;
        // 需要加载的高 * 缩放因子 = 视图view的高
        // x * mScale = mViewHeight
        mRect.bottom = (int) (mViewHeight / mScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 如果解码器是null 表示没有设置过要现实的图片
        if (mDecoder == null) {
            return;
        }

        // 1. 复用上一张Bitmap的内存
        mOptions.inBitmap = mBitmap;

        // 2. 解码指定区域
        mBitmap = mDecoder.decodeRegion(mRect, mOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i(TAG, "onDraw: mBitmap size=" + mBitmap.getAllocationByteCount() / 1024 + "KB");
        } else {
            Log.i(TAG, "onDraw: mBitmap size=" + mBitmap.getByteCount() / 1024 + "KB");
        }
        mMatrix.reset();
        mMatrix.setScale(mScale, mScale);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // 如果滑动还没有停止,则强制停止滚动
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        // 继续接收后续事件
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * 手指 不离开屏幕 拖动
     *
     * @param e1        手指按下去 的事件 -- 获取开始的坐标
     * @param e2        当前手势事件  -- 获取当前的坐标
     * @param distanceX x轴 方向移动的距离
     * @param distanceY y方向移动的距离
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // 手指从下往上，图片也要往上，distanceY是负数，top和bottom在减
        // 手指从上往下，图片也要往下，distanceY是正数，top和bottom在加
        // 改变加载图片的区域
        mRect.offset(0, (int) distanceY);

        // bottom大于图片高了，
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - (int) (mViewHeight / mScale);
        }

        // top 小于0了，
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = (int) (mViewHeight / mScale);
        }
        // 重新绘制图片
        invalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 手指离开屏幕 滑动 惯性
     *
     * @param velocityX 速度 每秒x方向 移动的像素
     * @param velocityY y
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        /**
         * startX: 滑动开始的x坐标
         * startY: 滑动开始的y坐标
         * 两个速度
         * minX: x方向的最小值
         * max 最大
         * y
         */
        // 计算滚动距离
        mScroller.fling(0,
                mRect.top,
                0,
                (int) (-velocityY),
                0,
                0,
                0,
                mImageHeight - (int) (mViewHeight / mScale));
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 事件交给手势处理
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 获取计算结果并重绘
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 滚动已经结束
        if (mScroller.isFinished()) {
            return;
        }

        // 当前滚动还没有结束
        if (mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom = (int) (mRect.top + mViewHeight / mScale);
            invalidate();
        }
    }
}
