package wedemo.customview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sven.huinews.international.R;

/**
 * Created by zd on 2017/6/21.
 */

public class RangeProgressBar extends View {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float firstValue = 0;
    private float maxValue = 100;
    private float secondValue = 100;
    private boolean isTwoProgress = true;
    private int progressColor = Color.parseColor("#3db5fe");
    private int bgColor = Color.parseColor("#66000000");
    private int controlColor = Color.BLACK;
    private int controlRadius;
    private int leftRightSpace;
    private int topBottomSpace;
    private Bitmap ctlBtnImg = BitmapFactory.decodeResource(getResources(), R.mipmap.vibrato_seek_bar);

    public RangeProgressBar(Context context) {
        super(context);
    }

    float len;

    public RangeProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void initViews() {
        dip5 = dip2px(getContext(), 5);
        leftRightSpace = dip5 * 3;
        topBottomSpace = dip5 * 2;
        len = getWidth() - 2 * leftRightSpace;
        mPaint.setStyle(Paint.Style.FILL);
        controlRadius = dip5 * 3;

    }

    public void exchangeProgressBar() {
        isTwoProgress = !isTwoProgress;
        invalidate();
    }

    RectF rectf = new RectF();

    private float getRate(float value) {
        return value / maxValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() == 0) {
            return;
        }
        //画背景
        mPaint.setColor(bgColor);//
        rectf.left = leftRightSpace;
        rectf.top = topBottomSpace;
        rectf.right = len + leftRightSpace;
        rectf.bottom = getHeight() - topBottomSpace;
        canvas.drawRoundRect(rectf, dip5, dip5, mPaint);
        //画进度条

        firstPointX = getValueLen(firstValue);
        secondPointX = getValueLen(secondValue);
        rectf.left = firstPointX;
        rectf.right = secondPointX;

        mPaint.setColor(progressColor);
        canvas.drawRoundRect(rectf, dip5, dip5, mPaint);

        mPaint.setColor(controlColor);
//        canvas.drawCircle(firstPointX, getHeight() / 2, controlRadius, mPaint);
//        canvas.drawCircle(secondPointX, getHeight() / 2, controlRadius, mPaint);
        RectF rightRect = new RectF(firstPointX - ctlBtnImg.getWidth() / 2, 0, firstPointX + ctlBtnImg.getWidth() / 2, getHeight());
        RectF leftRect = new RectF(secondPointX - ctlBtnImg.getWidth() / 2, 0, secondPointX + ctlBtnImg.getWidth() / 2, getHeight());
        canvas.drawBitmap(ctlBtnImg, null, rightRect, mPaint);
        canvas.drawBitmap(ctlBtnImg, null, leftRect, mPaint);
    }

    private float getValueLen(float value) {
        float rate = getRate(value);
        float positionX = leftRightSpace + len * rate;
        return positionX;
    }


    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    int dip5;

    float firstPointX, secondPointX;


    private boolean isInArea(float lastPosition, float x) {
        return x < lastPosition + controlRadius && lastPosition - controlRadius < x;
    }

    boolean isMovedFirst = false;
    boolean isMovedSecond = false;

    public void setFirstValue(float firstValue) {
        this.firstValue = firstValue;
        invalidate();
    }

    public void setSecondValue(float secondValue) {
        this.secondValue = secondValue;
        invalidate();
    }

    public float getFirstValue() {
        return this.firstValue;
    }

    public float getSecondValue() {
        return this.secondValue;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInArea(firstPointX, event.getX())) {
                    isMovedFirst = true;
                }
                if (isInArea(secondPointX, event.getX())) {
                    isMovedSecond = true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float newValue = maxValue * x2Rate(event.getX());
                if (isMovedFirst) {
                    if (isTwoProgress) {
                        if (newValue + 10 < secondValue) {
                            if (ondataChanged != null) {
                                ondataChanged.onFirstDataChange(newValue);
                            }
                            setFirstValue(newValue);
                            invalidate();
                        }
                    } else {
                        if (ondataChanged != null) {
                            ondataChanged.onFirstDataChange(newValue);
                        }
                        setFirstValue(newValue);

                    }
                } else if (isMovedSecond) {
                    if (newValue > firstValue + 10) {
                        if (ondataChanged != null) {
                            ondataChanged.onSecondDataChange(newValue);
                        }
                        setSecondValue(newValue);

                    }
                }
                break;
            default:
                isMovedSecond = false;
                isMovedFirst = false;
                break;
        }
        return true;
    }

    private float x2Rate(float x) {
        float f = (x - leftRightSpace) / len;
        if (f < 0) {
            f = 0;
        }
        if (f > 1) {
            f = 1;
        }
        return f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViews();
    }


    OnDataChanged ondataChanged;

    public void setOndataChanged(OnDataChanged ondataChanged) {
        this.ondataChanged = ondataChanged;
    }

    public interface OnDataChanged {

        void onFirstDataChange(float var);

        void onSecondDataChange(float var);
    }
}
