package com.sven.huinews.international.view;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;

/**
 * Created by Sven on 2018/2/3.
 */

public class LoginCodeCountDown extends CountDownTimer {

    private TextView mTextView;
    private static final long mMillisInFuture = 60 * 1000;
    private static final long mCountDownInterval = 1000;

    public void setNotClick() {
        mTextView.setTextColor(Color.parseColor("#9a9b9f"));
        mTextView.setClickable(false);
    }

    public LoginCodeCountDown(TextView textView) {
        super(mMillisInFuture, mCountDownInterval);
        mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setText(millisUntilFinished / 1000 + AppConfig.getAppContext().getResources().getString(R.string.s_re_try));
    }

    @Override
    public void onFinish() {
        mTextView.setText("0"+AppConfig.getAppContext().getResources().getString(R.string.s_re_try));
        mTextView.setText(AppConfig.getAppContext().getResources().getString(R.string.get_code));
        mTextView.setTextColor(Color.parseColor("#ff0000"));
        mTextView.setClickable(true);
    }
}
