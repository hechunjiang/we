package wedemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import wedemo.utils.ScreenUtils;


public class CircleView extends android.support.v7.widget.AppCompatTextView {

    private Context context;
    private Paint mPaint;
    private Paint mPaintCircle;

    private int radius;
    private int left;
    private int top;
    private int right;
    private int bottom;

    private int templeft;
    private int temptop;
    private int tempright;
    private int tempbottom;

    public static final int TYPE_START = 0;  //圆变矩阵
    public static final int TYPE_RESTART = 1;  //矩阵变圆
    private int type = 0;

    private int size;
    private int strokWidth;  //线宽

    private OnCircleClickListener onCircleClickListener;

    private boolean animEnd = false;

    public void setOnCircleClickListener(OnCircleClickListener onCircleClickListener) {
        this.onCircleClickListener = onCircleClickListener;
    }

    public CircleView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#EBBA5C"));
        mPaint.setStyle(Paint.Style.FILL);

        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.parseColor("#EBBA5C"));
        mPaintCircle.setStyle(Paint.Style.STROKE);

        templeft = 0;
        temptop = 0;
        tempright = 0;
        tempbottom = 0;

        type = TYPE_START;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(size / 2 + strokWidth / 2, size / 2 + strokWidth / 2, size / 2 - strokWidth, mPaintCircle);

        if (type == TYPE_START) {
            //圆变矩阵
            RectF rectF = new RectF(left + templeft, top + temptop, right - tempright, bottom - tempbottom);
            canvas.drawRoundRect(rectF, radius, radius, mPaint);
            if (animEnd) {
                type = TYPE_RESTART;
                left += templeft;
                top += temptop;
                right -= tempright;
                bottom -= tempbottom;
            }
        } else {
            //矩阵变圆
            RectF rectF = new RectF(left - templeft, top - temptop, right + tempright, bottom + tempbottom);
            canvas.drawRoundRect(rectF, radius, radius, mPaint);

            if (animEnd) {
                type = TYPE_START;
                left -= templeft;
                top -= temptop;
                right += tempright;
                bottom += tempbottom;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int minimumWidth = getSuggestedMinimumWidth();

        size = ScreenUtils.dip2px(context, 80);
        strokWidth = ScreenUtils.dip2px(context, 5);

        size = measureWidth(minimumWidth, widthMeasureSpec);
        setMeasuredDimension(size + strokWidth * 2, size + strokWidth * 2);

        //根据测量高度计算初始值
        left = strokWidth;
        top = strokWidth;
        right = size;
        bottom = size;
        radius = size / 2;
        mPaintCircle.setStrokeWidth(strokWidth);
    }

    private int measureWidth(int defaultWidth, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth = size;
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultWidth = Math.max(defaultWidth, specSize);
        }
        return defaultWidth;
    }

    //圆变矩阵
    public void startAnimCircle2Rect() {
        ValueAnimator animator = ValueAnimator.ofInt(size / 2, size / 8);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator animator1 = ValueAnimator.ofInt(0, size / 4);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                templeft = (Integer) animation.getAnimatedValue();
                temptop = (Integer) animation.getAnimatedValue();
                tempright = (Integer) animation.getAnimatedValue();
                tempbottom = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animator, animator1);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animEnd = true;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animEnd = false;
            }
        });
        set.start();
    }

    //矩阵变圆
    public void startAnimRect2Circle() {
        ValueAnimator animator = ValueAnimator.ofInt(size / 8, size / 2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator animator1 = ValueAnimator.ofInt(0, size / 4);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                templeft = (Integer) animation.getAnimatedValue();
                temptop = (Integer) animation.getAnimatedValue();
                tempright = (Integer) animation.getAnimatedValue();
                tempbottom = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animator, animator1);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animEnd = true;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animEnd = false;
                Log.e("weiwei", "drawn=- left = " + left + ",tem==" + templeft);
            }
        });
        set.start();

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                return true;
//            case MotionEvent.ACTION_UP:
//                if (type == TYPE_START) {
//                    startAnimCircle2Rect();
//                    if(onCircleClickListener != null){
//                        onCircleClickListener.onClick(type);
//                    }
//                } else if (type == TYPE_RESTART) {
//                    startAnimRect2Circle();
//                    if(onCircleClickListener != null){
//                        onCircleClickListener.onClick(type);
//                    }
//                }
//
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }

    public void startAnim() {
        if (type == TYPE_START) {
            startAnimCircle2Rect();
        } else if (type == TYPE_RESTART) {
            startAnimRect2Circle();
        }
    }

    public interface OnCircleClickListener {
        void onClick(int type);
    }
}
