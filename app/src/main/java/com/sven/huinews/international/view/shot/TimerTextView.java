package com.sven.huinews.international.view.shot;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTextView extends TextView {
    public LinearLayout linearLayout;
    public Context context;
    public View view;
    public final int _COUNTTIME = 1;
    Timer timer;
    private TimerTask timerTask;
    private int currentTime = 3;
    private TimerBack timerBack;
    private int stopTime = 1;

    public TimerTextView(Context context) {
        super(context);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public interface TimerBack {
        void back(int time);
    }

    public void setTimerBack(TimerBack timerBack) {
        this.timerBack = timerBack;
    }

    private void setCurrentTime(int time) {
        this.currentTime = time;
    }

    public void setTotalTime(int time) {
        setCurrentTime(time);
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case _COUNTTIME: {
                    Log.i("TimerTextView", "handleMessage: currentTimer" + currentTime);
                    if (currentTime >= stopTime) {
                        setTimerText(String.valueOf(currentTime));
                        if (timerBack != null) {
                            timerBack.back(currentTime);
                        }
                        currentTime--;

                        if (currentTime == 0) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setTimerText("");
                                    removeCallbacks(this);
                                }
                            }, 1500);
                        }
                    }
                }
                break;
            }
            return false;
        }
    });

//    private Thread thread=new Thread(new Runnable() {
//        @Override
//        public void run() {
//
//        }
//    });

    private TextView setTimerText(String text) {
        TimerTextView.this.setText(text);
        return TimerTextView.this;
    }

    public void start() {
        initTimer();
        setTotalTime(3);
    }

    private void setEndTime(int time) {
        this.stopTime = time;
    }

    public void start(int start, int end) {
        setTotalTime(start);
        setEndTime(end);

        if (timer == null && timerTask == null) {
            initTimer();
        }
    }

    //TODO 销毁进程
    public void stop() {
        //   runnable.
    }


    private void initView() {
        //view = LayoutInflater.from(context).inflate(R.layout.custom_bottom_radio, this);


        //getActityaddContentView(view,lp);


    }

    /**
     * 初始化时间
     */
    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //Log.i("TimerTextView","run:"+new Date().toString());
                synchronized (timerTask) {
                    //Log.i("TimerTextView","synchronized  run:"+new Date().toString());
                    handler.sendEmptyMessage(_COUNTTIME);
                }
            }
        };

        /**
         * 第二个参数，执行timerTask的延迟
         * 第三个参数，多久执行一次run
         */
        timer.schedule(timerTask, 0, 1000);
    }
}
