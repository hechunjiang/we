package com.sven.huinews.international.utils.shot;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.logging.LogRecord;

public class LongClickHandler extends Handler implements View.OnTouchListener {
    private String LOG_TAG = "ClickHandlerLog";
    private static final int MAX_INTERVAL_FOR_CLICK = 250;
    private static final int MAX_DISTANCE_FOR_CLICK = 100;
    private static final int MAX_DOUBLE_CLICK_INTERVAL = 500;
    private static final int MAX_LONG_CLICK = 700;
    int mDownX = 0;
    int mDownY = 0;
    int mTempX = 0;
    int mTempY = 0;
    boolean mIsWaitUpEvent = false;
    boolean mIsWaitDoubleClick = false;
    boolean mIsWaitLongCick = false;
    private long touchDownTime = 0;
    private OnTouchListener onTouchListener;

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    long downTime = 0;
    Runnable longListener = new Runnable() {
        public void run() {
            if (mIsWaitLongCick) {
                if (onTouchListener != null) {
                    Log.d(LOG_TAG, "长按");

                    onTouchListener.longClick();
                }
            }
        }
    };

    private View view;

    public void start(View view) {
        this.view = view;
        view.setOnTouchListener(this);
    }

    private MotionEvent event;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.event = event;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsWaitLongCick = true;
                touchDownTime = System.currentTimeMillis();
                postDelayed(longListener, MAX_LONG_CLICK);
                break;
            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                mIsWaitLongCick = false;
                removeCallbacks(longListener);

                if (onTouchListener != null) {
                    onTouchListener.up();
                    Log.d(LOG_TAG, "is up>>>>>>>>>>>>>>");
                }

            case MotionEvent.ACTION_CANCEL:
                //mIsWaitLongCick=false;
                //if(onTouchListener!=null){
                //    onTouchListener.up();
                //    Log.d(LOG_TAG, "is up>>>>>>>>>>>>>>");
                //}
                break;
            default:
                Log.d(LOG_TAG, "irrelevant MotionEvent state:" + event.getAction());
        }
        return true;
    }


    //Listening
    public interface OnTouchListener {
        void longClick();

        void up();
    }

}
