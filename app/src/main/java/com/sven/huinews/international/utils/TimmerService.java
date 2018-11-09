package com.sven.huinews.international.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class TimmerService extends Service {
    private Handler serviceHandler = null;
    private TestCounterTask myTask = new TestCounterTask();

    private int counter = 0;

    public TimmerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void onStart(Intent intent, int startId) {
        // 开启计数器
        serviceHandler.postDelayed(myTask, 1000);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    private class TestCounterTask implements Runnable {
        public void run() {
            ++counter;
            serviceHandler.postDelayed(myTask, 1000);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceHandler.removeCallbacks(myTask); //停止计数器
        serviceHandler = null;

    }
}
