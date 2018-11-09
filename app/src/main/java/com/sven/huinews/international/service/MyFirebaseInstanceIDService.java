package com.sven.huinews.international.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by weiwei on 2018/9/13.
 * 设备Token的获取
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("weiwei","token get = "+ FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("weiwei", "Refreshed token: " + refreshedToken);


        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshToken) {
        Log.i("weiwei", "token:"+refreshToken);
    }
}
