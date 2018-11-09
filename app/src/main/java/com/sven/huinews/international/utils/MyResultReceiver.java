package com.sven.huinews.international.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sven.huinews.international.entity.event.ShareResponseEvent;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by sfy. on 2018/9/25 0025.
 */

public class MyResultReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ShareResponseEvent shareResponseEvent = new ShareResponseEvent();
//        Bundle intentExtras = intent.getExtras();
        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {

            LogUtil.showLog("twitter回调--分享成功");
            shareResponseEvent.setShareResponseType(Common.JS_RESPONSE_CODE_SUCCEED);
        } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
            // failure
//            final Intent retryIntent = intentExtras.getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
            LogUtil.showLog("twitter回调--分享失败");
            shareResponseEvent.setShareResponseType(Common.JS_RESPONSE_CODE_FAIL);
        } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
            LogUtil.showLog("twitter回调--取消分享");
            // cancel
            shareResponseEvent.setShareResponseType(Common.JS_RESPONSE_CODE_CANCEL);
        }
        EventBus.getDefault().post(shareResponseEvent);
    }
}