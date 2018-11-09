package com.sven.huinews.international.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by Sven on 2018/2/26.
 */

public class MyVideoProgress extends View {
    private int max = 100;//最大值
    private int roundColor; //圆形进度条的颜色
    private int roundProgressColor;//圆形进度条进度的颜色
    private float roundWidth; //圆的宽度
    private int progress; //当前进度
    private Paint mPaint; //画笔
    private int maxProgress; //总进度
    private int startAngle = -90; //起始位置
    private int stopProgress;
    private Context mContext;
    private long mCurrentPlayTime; //动画当前执行的时间

    public MyVideoProgress(Context context) {
        super(context);
        init(context);
    }

    public MyVideoProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyVideoProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        //初始化一只笔
        mPaint = new Paint();
        max = 100;
        //获取xml当中设置的属性，如果没有设置，则设置一个默认值
        roundColor = Color.parseColor("#FCD13F");//#B72123
        roundProgressColor = context.getResources().getColor(R.color.colorWhite);//themeYellow 原颜色
        roundWidth = CommonUtils.dip2px(context, 3);
        initAnim();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //画背景圆环
        int center = getWidth() / 2;
        //设置半径
        float radius = (float) (center - roundWidth / 2);
        //设置圆圈的颜色
        mPaint.setColor(roundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(roundWidth);//圆环的宽度
        mPaint.setAntiAlias(true);//设置抗锯齿

        //画外圈
        canvas.drawCircle(center, center, radius, mPaint);
        //画圆弧
        int nums = CommonUtils.dip2px(mContext, 3);
        RectF oval = new RectF(center - (radius - nums), center - (radius - nums), center + (radius - nums), center + (radius - nums));
//        oval.offset(3, 2);//使rectf_head所确定的矩形向右偏移100像素，向下偏移20像素
        mPaint.setColor(roundProgressColor);
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //设置笔帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //话进度
        canvas.drawArc(oval, startAngle, progress, false, mPaint);

    }

   /* public void setProgress(int progress){
        if(progress < 0){
            throw new IllegalArgumentException("进度progress不能小于0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }*/

    /**
     * 暂停后获取进度
     */
    public void stopProgress() {
        animStop();
    }

    private long mDuration;

    /**
     * 设置总时长
     */
    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
        mValueAnimator.setDuration(mDuration);
    }

    ValueAnimator mValueAnimator;

    private void initAnim() {
        mValueAnimator = ValueAnimator.ofInt(progress, 360);
        mValueAnimator.setIntValues();
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                mCurrentPlayTime = mValueAnimator.getCurrentPlayTime();
                if (progress == 360) {
                    if (onVideoProgressLisenter != null) {
                        onVideoProgressLisenter.end();
                    }
                } else {
                    postInvalidate();
                }
            }
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mValueAnimator.setIntValues(progress, 360);
            }

        });
        //设置匀速插值器
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(mDuration);
    }

    public void animStart(long mDuration) {
        this.mDuration = mDuration;
        if (mValueAnimator != null) {
            mValueAnimator.setDuration(mDuration);
            mValueAnimator.start();
        }
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            return;
        }
        stopProgress();

        this.progress = progress;
        mValueAnimator.setIntValues(progress, 360);
        postInvalidate();
    }

    public long surplusTime() {
        if (mValueAnimator == null) {
            return 0;
        }
        return mDuration - mCurrentPlayTime;
    }

    public boolean getAnimatorStatus() {
        return mValueAnimator.isRunning();
    }

    private void animStop() {
        mValueAnimator.cancel();
    }

    public interface OnVideoProgressLisenter {
        void end();
    }

    private OnVideoProgressLisenter onVideoProgressLisenter;

    public void OnVideoProgressLisenter(OnVideoProgressLisenter o) {
        if (this.onVideoProgressLisenter == null)
            this.onVideoProgressLisenter = o;
    }

    public int getProgressCurrent() {
        return progress;
    }

    public void pauseAnim() {
        if (mValueAnimator != null) {
            if (mValueAnimator.isRunning()) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    mValueAnimator.pause();
                }
            }
        }
    }

    public void resumeAnim() {
        if (mValueAnimator != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                mValueAnimator.resume();
            }
        }
    }
}
