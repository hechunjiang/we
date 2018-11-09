package com.sven.huinews.international.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.entity.event.OpenNewPageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by sfy. on 2018/10/9 0009.
 * <p>
 * 悬浮球
 */

public class FloatServive extends Service {

    WindowManager.LayoutParams mLayoutParams;
    WindowManager mManager;
    LinearLayout mLayout;

    @Override
    public void onCreate() {
        super.onCreate();
        initFloatBall();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initFloatBall() {
        mLayoutParams = new WindowManager.LayoutParams();
        mManager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);
        mLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.float_ball, null);
        final TextView tvFloatBall = (TextView) mLayout.findViewById(R.id.tvFloatBall);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = 100;
        mLayoutParams.y = 100;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mManager.addView(mLayout, mLayoutParams);
        mLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tvFloatBall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mLayoutParams.x = (int) (event.getRawX() - tvFloatBall.getMeasuredWidth() / 2);
                        mLayoutParams.y = (int) (event.getRawY() - tvFloatBall.getMeasuredHeight() / 2 - 25);
                        mManager.updateViewLayout(mLayout, mLayoutParams);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        /**
         * 点击事件
         */
        tvFloatBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OpenNewPageEvent(Api.WEB_HOW_TO_EARN));
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLayout != null) {
            mManager.removeView(mLayout);
        }
    }
}
