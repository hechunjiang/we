package com.sven.huinews.international.service;

import com.sven.huinews.international.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

public class ActiviteTimer {
    private Timer mTimer;
    private TimerTask mTimerTask;
    public ActiviteTimer() {

    }

    public void startTimer(long delay, long period) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        LogUtil.showLog("msg---sleep:-timer:");
                        Thread.sleep(5000);
                        LogUtil.showLog("msg----timer:");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

        }
        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, delay, period);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
}
