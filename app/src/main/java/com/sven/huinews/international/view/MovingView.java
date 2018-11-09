package com.sven.huinews.international.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MovingView extends View {
    private float mActualX;
    private float mActualY;
    private float mPreX;
    private float mPreY;
    private float mTranslationX;
    private float mTranslationY;
    private ViewGroup mParentViewGroup;
    private float mDefaultTopTranslationX;
    private float mDefaultTopTranslationY;
    private float mDefaultBottomTranslationX;
    private float mDefaultBottomTranslationY;
    private boolean mFirstMain = true;
    private long downCurrent;
    private OnClickListener mOnClickListener;
    private float pressDown;
    private static final int DEFAULT_MOVIE_DURATION = 1000;
    private int mGifId;
    private Movie mMovie;
    private long mMovieStart;
    private int mCurrentAnimationTime = 0;
    private float mLeft;
    private float mTop;
    private float mScale;
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;
    private boolean mVisible = true;
    private volatile boolean mPaused = false;
    private int width, iconWidth;


    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public MovingView(Context context) {
        this(context, null);
    }

    public MovingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setViewAttributes(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    private void setViewAttributes(Context context, AttributeSet attrs,
                                   int defStyle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifView);
        mGifId = array.getResourceId(R.styleable.GifView_gif, -1);
        mPaused = array.getBoolean(R.styleable.GifView_paused, false);
        array.recycle();
        if (mGifId != -1) {
            byte[] bytes = getGiftBytes();
            mMovie = Movie.decodeByteArray(bytes, 0, bytes.length);
        }

    }

    public void setMovieResource(int giftResId) {
        this.mGifId = giftResId;
        byte[] bytes = getGiftBytes();
        mMovie = Movie.decodeByteArray(bytes, 0, bytes.length);
        requestLayout();
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
        requestLayout();
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
        invalidate();
    }

    public void setPaused(boolean paused) {
        this.mPaused = paused;
        if (!paused) {
            mMovieStart = android.os.SystemClock.uptimeMillis()
                    - mCurrentAnimationTime;
        }
        invalidate();
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMovie != null) {
            int movieWidth = mMovie.width();
            int movieHeight = mMovie.height();
            int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
            float scaleW = (float) movieWidth / (float) maximumWidth;
            mScale = 1f / scaleW;
            mMeasuredMovieWidth = maximumWidth;
            mMeasuredMovieHeight = (int) (movieHeight * mScale);
            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);
        } else {
            setMeasuredDimension(getSuggestedMinimumWidth(),
                    getSuggestedMinimumHeight());
        }
        width = PhoneUtils.getScreenWidth(AppConfig.getAppContext());
        iconWidth = PhoneUtils.dp2px(AppConfig.getAppContext(), 70.0f);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
        mTop = (getHeight() - mMeasuredMovieHeight) / 2f;
        mVisible = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    @SuppressLint("NewApi")
    private void invalidateView() {
        if (mVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }

    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();
        // 如果第一帧，记录起始时间
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        // 取出动画的时长
        int dur = mMovie.duration();
        if (dur == 0) {
            dur = DEFAULT_MOVIE_DURATION;
        }
        // 算出需要显示第几帧
        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    private void drawMovieFrame(Canvas canvas) {
        // 设置要显示的帧，绘制即可
        mMovie.setTime(mCurrentAnimationTime);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.scale(mScale, mScale);
        mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
        canvas.restore();
    }

    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        mVisible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    private byte[] getGiftBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = getResources().openRawResource(mGifId);
        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = is.read(b, 0, 1024)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downCurrent = System.currentTimeMillis();
                if (mFirstMain) {
                    mParentViewGroup = (ViewGroup) getParent();
                    mDefaultTopTranslationX = getTranslationX() + getLeft();
                    mDefaultTopTranslationY = getTranslationY() + getTop();
                    mDefaultBottomTranslationX = mParentViewGroup.getMeasuredWidth() - getRight();
                    mDefaultBottomTranslationY = mParentViewGroup.getMeasuredHeight() - getBottom();
                    mFirstMain = false;
                }
                mPreX = event.getRawX();
                mPreY = event.getRawY();
                pressDown = getX();

                break;
            case MotionEvent.ACTION_MOVE:
                mActualX = event.getRawX();
                mActualY = event.getRawY();
                mTranslationX = getTranslationX() + mActualX - mPreX;
                mTranslationY = getTranslationY() + mActualY - mPreY;
                if (mTranslationX < -mDefaultTopTranslationX) {
                    mTranslationX = -mDefaultTopTranslationX;
                }
                if (mTranslationY < -mDefaultTopTranslationY) {
                    mTranslationY = -mDefaultTopTranslationY;
                }

                if (mTranslationX > mDefaultBottomTranslationX) {
                    mTranslationX = mDefaultBottomTranslationX;
                }
                if (mTranslationY > mDefaultBottomTranslationY) {
                    mTranslationY = mDefaultBottomTranslationY;
                }
                setTranslationX(mTranslationX);
                setTranslationY(mTranslationY);
                //
                mPreX = mActualX;
                mPreY = mActualY;
                break;
            case MotionEvent.ACTION_UP:

                if (mOnClickListener != null && System.currentTimeMillis() - downCurrent < 200) {
                    mOnClickListener.onClick(this);
                }
                //手指离开时  mPreY、mPrex是松手时的值 width height
                if (getY() > getX() && getX() + iconWidth > width / 2.0) { //吸附到右边
                    setX(width - iconWidth);
                    setY(getY());
                    invalidateView();
                } else if (mPreY > mPreX && getX() + iconWidth < width / 2.0) {//吸附到左边
                    setX(0);
                    setY(getY());
                    invalidateView();
                } else if (getY() < getX() && getX() + iconWidth < width / 2.0) {
                    setX(0);
                    setY(getY());
                    invalidateView();
                } else {
                    setX(width - iconWidth);
                    setY(getY());
                    invalidateView();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {

        }
    }


}
