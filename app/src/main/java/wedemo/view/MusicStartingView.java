package wedemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MusicStartingView extends View {
    private Context context;
    private Paint mPaint;

    private RectF mRect = new RectF();
    private int offset = 5; //矩形X轴上起始位置
    private int valueOffSet;//矩形动画偏移量

    public MusicStartingView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MusicStartingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MusicStartingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();

    }


    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#EBBA5C"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);


    }

    public void startAim() {
        setUpViewAnimation();
    }

    public void stopAim() {
        if (mAnimator1 != null && mAnimator != null) {
            if (mAnimator1.isRunning() && mAnimator.isRunning()) {
                mAnimator.end();
                mAnimator1.end();
            }
        }

    }

    public void endAim() {
        if (mAnimator1 != null && mAnimator != null) {
            if (mAnimator1.isRunning() && mAnimator.isRunning()) {
                mAnimator.cancel();
                mAnimator1.cancel();
            }
        }
    }

    private int oldLeft, oldTop, oldRight, oldBottom;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();

        for (int i = 0; i < 4; i++) {
            int top = 0;
            int left = 0;
            if (i == 0) {
                top = 10 + valueOffSet;
                left = 10;
            } else if (i == 1) {
                top = 30 - valueOffSet;
                left = 20;
            } else if (i == 2) {
                top = 15 + valueOffSet;
                left = 30;
            } else if (i == 3) {
                top = 25 - valueOffSet;
                left = 40;
            }
            int right = left + 8;
            int bottom = h;

            mRect.set(left, top, right, bottom);
            canvas.drawRoundRect(mRect, 30, 30, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(50, 50);
    }

    ValueAnimator mAnimator;
    ValueAnimator mAnimator1;


    private void setUpViewAnimation() {
        mAnimator = ValueAnimator.ofInt(valueOffSet, 20);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                valueOffSet = (int) valueAnimator.getAnimatedValue();
                offset = 0;
                invalidate();
            }
        });


        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setDownViewAnimation();
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }
        });
        mAnimator.setDuration(200);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.start();
    }

    private void setDownViewAnimation() {
        mAnimator1 = ValueAnimator.ofInt(valueOffSet, 0);
        mAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                valueOffSet = (int) animation.getAnimatedValue();
                offset = 0;
                invalidate();
            }

        });
        mAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setUpViewAnimation();
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);

            }
        });
        //4.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator1.setDuration(200);
        mAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator1.start();
    }
}
