package com.sven.huinews.international.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sven.huinews.international.R;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by weiwei on 2018/9/13.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("weiwei","消息服务已启动token get = "+ FirebaseInstanceId.getInstance().getToken());
    }


    //app已经打开
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("weiwei", "===============通知来啦=================");


        if (remoteMessage.getData().size() > 0) {
            for (String key : remoteMessage.getData().keySet()) {
                Log.e("weiwei","Key = " + key+",value = "+remoteMessage.getData().get(key));
            }
        }


//        Log.e("weiwei", "getCollapseKey = " + remoteMessage.getCollapseKey());
//        Log.e("weiwei", "getMessageId = " + remoteMessage.getMessageId());
//        Log.e("weiwei", "getFrom: " + remoteMessage.getFrom());
//        Log.e("weiwei", "getMessageType = " + remoteMessage.getMessageType());
//        Log.e("weiwei", "getTo = " + remoteMessage.getTo());
//        Log.e("weiwei", "getOriginalPriority = " + remoteMessage.getOriginalPriority());
//        Log.e("weiwei", "getPriority = " + remoteMessage.getPriority());
//        Log.e("weiwei", "getSentTime = " + remoteMessage.getSentTime());
//        Log.e("weiwei", "getTtl = " + remoteMessage.getTtl());

        if (remoteMessage.getNotification() != null) {

            LogUtil.showLog("msg-----remoteMessage tag："+remoteMessage.getNotification().getTag());
            LogUtil.showLog("msg-----remoteMessage title："+remoteMessage.getNotification().getTitle());
            LogUtil.showLog("msg-----remoteMessage  body："+remoteMessage.getNotification().getBody());
            LogUtil.showLog("msg-----remoteMessage  action："+remoteMessage.getNotification().getClickAction());
            LogUtil.showLog("msg-----remoteMessage  link："+remoteMessage.getNotification().getLink());
            LogUtil.showLog("msg-----remoteMessage  link："+remoteMessage.getNotification().getLink());


            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("weiwei", "onDeletedMessages: 删除");
    }

    @Override
    public void onNewToken(String token) {
        Log.e("weiwei", "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Log.e("weiwei", "token:"+token);
    }

    private NotificationManager mNotificationManager;

    private void sendNotification(String messageBody,String title) {
        String channelId = "weiwei";
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.mipmap.news_logo)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
