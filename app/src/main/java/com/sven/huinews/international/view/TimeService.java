package com.sven.huinews.international.view;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.util.Timer;
import java.util.TimerTask;

public class TimeService extends Service {

    int counter;
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
    MyBinder myBinder = new MyBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        counter = 0;
        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {

        return myBinder;
    }


    public class MyBinder extends Binder {

        public TimeService getService() {
            return TimeService.this;
        }
    }


    public void startCounter() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mUserSpCache.putInt("counter", counter);
                counter++;
            }
        }, 0, 1000);
    }


}
