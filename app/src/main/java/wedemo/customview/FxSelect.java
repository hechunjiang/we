package wedemo.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sven.huinews.international.R;

import java.util.ArrayList;

/**
 * Created by zd on 2017/6/26.
 */

public class FxSelect extends View {

    Paint m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    float firstValue = 0;
    float maxValue = 100;
    float secondValue = 70;
    boolean isTwoProgressIndicator = true;
    int colorMode = 0;
    int soulAndshakeMode = 1;
    int actionAndPlayMode = 0;
    int seekMode = 3;

    ArrayList<Float> leftValue = new ArrayList<Float>();
    ArrayList<Float> rightValue = new ArrayList<Float>();
    ArrayList<Integer> previousColor = new ArrayList<Integer>();

    ArrayList<RectInfo> rectInfoList = new ArrayList<RectInfo>();
    int curIndex = 0;

    int soulAndShakeColor = Color.TRANSPARENT;
    int progressColor = Color.TRANSPARENT;
    int bgColor = Color.TRANSPARENT;
    int m_first_indicator_color = Color.parseColor("#EBBA5C");
    int m_second_indicator_color = Color.GREEN;
    int controlRadius;
    int leftRightSpace;
    int topBottomSpace;
    float len;

    private RectF m_first_indicator_rectf;
    private RectF m_second_indicator_rectf;
    private Bitmap m_second_indicator_img = BitmapFactory.decodeResource(getResources(), R.mipmap.bar);
    private int m_4_px = dip2px(getContext(), 4);
    private int m_5_px = dip2px(getContext(), 4);
    private int m_28_px = dip2px(getContext(), 28);
    private float curPos;
    private boolean isMoving = false;
    private float m_width = 0;
    private RectInfo cur;

    public FxSelect(Context context) {
        super(context);
    }


    public void setSoulAndShakeColor(int color) {
        this.soulAndShakeColor = color;
        invalidate();
    }

    public void setActionMode(int Mode) {
        this.actionAndPlayMode = Mode;
        invalidate();
    }

    public void setLeftValue(float value) {
        this.leftValue.add(value);
    }

    public void setRightValue(float value) {
        this.rightValue.add(value);
    }

    private void initViews() {
        dip5 = dip2px(getContext(), 5);
        leftRightSpace = dip5 * 3;
        topBottomSpace = dip5 * 2;
        len = getWidth() - 2 * leftRightSpace;
        m_paint.setStyle(Paint.Style.FILL);
        controlRadius = dip5 * 3;
        m_width = getWidth();
    }

    public void setProgressColor(int color) {
        this.progressColor = color;
        invalidate();
    }

    public void setPreviousColor(int color) {
        this.previousColor.add(color);
    }


    public void setFirstControlColor(int color) {
        this.m_first_indicator_color = color;
        refresh();
    }

    public void setSecondControlColor(int color) {
        this.m_second_indicator_color = color;
        refresh();
    }

    public void exchangeProgressBar(Boolean isTwoProgress) {
        this.isTwoProgressIndicator = isTwoProgress;
        invalidate();
    }

    RectF rectf = new RectF();

    public void setcolorMode(int Mode) {
        this.colorMode = Mode;
    }


    private float getRate(float value) {
        return value / maxValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() == 0) {
            return;
        }
        //画背景
        m_paint.setColor(bgColor);
        rectf.left = 0;
        rectf.top = m_4_px;
        rectf.right = getWidth();
        rectf.bottom = m_28_px;

        m_first_indicator_rectf = new RectF(curPos - m_5_px, m_4_px, curPos + m_4_px, m_28_px);
        m_second_indicator_rectf = new RectF(secondPointX - m_second_indicator_img.getWidth() / 2, 0,
                secondPointX + m_second_indicator_img.getWidth() / 2, m_second_indicator_img.getHeight());


        //画进度条
        if (!isTwoProgressIndicator) {

            if (colorMode == soulAndshakeMode) {

//                if (leftValue.size() != 0 && rightValue.size() != 0) {
//                    for (int i = 0; i < rightValue.size(); i++) {
//                        firstPointX = getX(leftValue.get(i));
//                        rectf.left = firstPointX;
//                        rectf.right = getX(rightValue.get(i));
//                        m_paint.setColor(previousColor.get(i));
//                        canvas.drawRect(rectf, m_paint);
//                    }
//                }
//
//                if (actionAndPlayMode == 0) {
//                    rectf.left = getX(leftValue.get(leftValue.size()-1));
//                    rectf.right = getX(firstValue);
//                    m_paint.setColor(soulAndShakeColor);
//                    canvas.drawRect(rectf, m_paint);
//                }


                firstPointX = getX(firstValue);
                m_paint.setColor(m_first_indicator_color);
                drawRects(canvas);
                m_paint.setColor(m_first_indicator_color);
                canvas.drawRect(m_first_indicator_rectf, m_paint);


            } else {
                firstPointX = getX(firstValue);
                rectf.right = firstPointX;
                m_paint.setColor(m_first_indicator_color);
                canvas.drawRect(m_first_indicator_rectf, m_paint);
            }

        } else {
            firstPointX = getX(firstValue);
            secondPointX = getX(secondValue);


            m_paint.setColor(m_first_indicator_color);
            canvas.drawRect(m_first_indicator_rectf, m_paint);
            canvas.drawBitmap(m_second_indicator_img, null, m_second_indicator_rectf, m_paint);
        }

    }

    private float getX(float value) {
        Log.d("abcdegf", "m_width:" + m_width);
        return value * m_width * 1.0f / 100f;
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
    boolean autoAdjust = true;

    public void setAutoAdjust(boolean b) {
        autoAdjust = b;
    }

    public void setFirstValue(float firstValue, boolean auto) {

        this.firstValue = firstValue;
        curPos = getX(firstValue);
        Log.d("abcdegf", "firstValue: " + firstValue);
        Log.d("abcdegf", "curPos: " + curPos);
        if (auto) {
            adjustRects();
        }

        refresh();
    }

    public void setSecondValue(float secondValue) {
        this.secondValue = secondValue;
        refresh();
    }

    public float getFirstValue() {
        return this.firstValue;
    }

    public float getSecondValue() {

        return this.secondValue;
    }

    private void refresh() {
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        curPos = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionAndPlayMode = seekMode;
                if (isInArea(firstPointX, event.getX())) {
                    isMovedFirst = true;
                }
                if (isInArea(secondPointX, event.getX())) {
                    isMovedSecond = true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float newValue = maxValue * getRatio(event.getX());
                if (isMovedFirst) {
                    if (isTwoProgressIndicator) {
                        if (ondataChanged != null) {
                            ondataChanged.onFirstDataChange(newValue);
                            setFirstValue(newValue, false);
                            invalidate();
                        }
                    } else {
                        if (ondataChanged != null) {
                            ondataChanged.onFirstDataChange(newValue);
                        }
                        setFirstValue(newValue, false);
                        invalidate();
                    }

                } else if (isMovedSecond) {
                    if (ondataChanged != null) {
                        ondataChanged.onSecondDataChange(newValue);
                        setSecondValue(newValue);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isMovedSecond = false;
                isMovedFirst = false;
                break;
        }
        return true;
    }

    private float getRatio(float x) {
        float f = x / getWidth();
        return f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViews();
    }


    RangeProgressBar.OnDataChanged ondataChanged;

    public void setOndataChanged(RangeProgressBar.OnDataChanged ondataChanged) {
        this.ondataChanged = ondataChanged;
    }

    public interface OnDataChanged {

        void onFirstDataChange(float var);

        void onSecondDataChange(float var);
    }

    public boolean isApplay() {
        return (firstValue <= 99);
    }


    public void adjustRects() {

        int size = rectInfoList.size();
        if (size == 0 || curPos == 0 || !autoAdjust || firstValue >= 99) {
            clearRect();
            return;
        }

        if (!isMoving) {

            for (int i = 0; i < rectInfoList.size(); i++) {
                RectInfo t = rectInfoList.get(i);
                if (t.rect.contains(curPos, getHeight() / 2)) {

                    RectInfo info = new RectInfo();
                    info.rect.right = t.rect.right;
                    info.rect.left = curPos;
                    info.color = t.color;
                    rectInfoList.add(i + 1, info);

                    t.rect.right = curPos;
                    t.flag = true;
                    break;
                }
            }

        }

        cur.rect.right = curPos;

        if (isMoving) {

            for (int i = 0; i < rectInfoList.size() - 1; i++) {
                RectInfo t = rectInfoList.get(i);

                if (t.rect.right <= t.rect.left) {
                    t.rect.bottom = 0;
                }


                if (t.rect.contains(curPos, getHeight() / 2)) {
                    if (t.flag) {

                        continue;
                    } else {
                        t.rect.left = curPos;
                    }

                } else {
                    t.flag = false;
                    if (startX < t.rect.left && curPos > t.rect.right) {
                        t.rect.bottom = 0;
                    }

                    // 进度条走完了
                    if (t.rect.left < curPos && firstValue == 100 && cur.rect.left < t.rect.left) {
                        t.rect.bottom = 0;
                        break;
                    }
                }
            }
        }
        clearRect();
    }

    private void clearRect() {
        for (int i = 0; i < rectInfoList.size(); i++) {
            RectInfo rectInfo = rectInfoList.get(i);
            if (rectInfo.rect.bottom == 0) {
                rectInfoList.remove(i);
            }
        }
    }

    public void removeRect() {
        clearRect();
        if (rectInfoList.size() == 0) {
            return;
        }
        rectInfoList.remove(rectInfoList.size() - 1);
        invalidate();
    }


    public void drawRects(Canvas canvas) {
        for (int i = 0; i < rectInfoList.size(); i++) {
            RectInfo rectInfo = rectInfoList.get(i);
            m_paint.setColor(rectInfo.color);
            canvas.drawRect(rectInfo.rect, m_paint);
        }
    }

    private float startX = 0;

    public void downEvent(float leftValue, int color) {
        cur = new RectInfo();
        cur.rect.left = getX(leftValue);
        startX = cur.rect.left;
        rectInfoList.add(cur);
        curIndex = rectInfoList.size() - 1;
        cur.color = color;
        adjustRects();
        isMoving = true;
    }

    public void upEvent(float rightValue) {
        RectInfo rectInfo = rectInfoList.get(rectInfoList.size() - 1);
        rectInfo.rect.right = getX(rightValue);
        Log.d("abcdegf", "upEvent: " + rectInfo.rect.right);
        isMoving = false;
    }

    private class RectInfo {
        RectF rect = new RectF(0, m_4_px, 0, m_28_px);
        int color;
        boolean flag = false;
    }
}
