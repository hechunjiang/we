package com.sven.huinews.international.utils;

import android.content.Context;
import android.util.Log;

import com.ap.ApBanner;
import com.ap.IAirPushAdListener;
import com.ap.IAirPushPreparedAd;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by W.mago on 2018/9/8 0008.
 */
public class AirpushAdUtils {
    private int failed=0;
    private Context mContext;
    public AirpushAdUtils(Context context) {
        this.mContext = context;
    }

    public void inintAirPush(final ApBanner banner) {
        banner.setEventsListener(new IAirPushAdListener() {
            @Override
            public void onLoaded(IAirPushPreparedAd ad) {
                Log.d("bannerAd", "onLoaded");
                ad.show();
                if (mAirpushAdLoadLisenter != null) {
                    mAirpushAdLoadLisenter.onLoad();
                }
                MobclickAgent.onEvent(mContext, Common.AD_TYPE_VIDEO_AIRPUSH_LOOK);
                MobclickAgent.onEvent(mContext, Common.AD_TYPE_VIDEO_AIRPUSH_LOOK, "ad_type_video_airpush_look");
            }

            @Override
            public void onFailed(String s) {
                Log.d("bannerAd", "onFailed:"+s);
                failed++;
                if (failed<=3){
                    banner.load();
                }
            }



            @Override
            public void onClicked() {
                Log.d("bannerAd", "onClicked");
                if (mAirpushAdLoadLisenter != null) {
                    mAirpushAdLoadLisenter.onClick();
                }
                MobclickAgent.onEvent(mContext, Common.AD_TYPE_VIDEO_AIRPUSH_CLICK);
                MobclickAgent.onEvent(mContext, Common.AD_TYPE_VIDEO_AIRPUSH_CLICK, "ad_type_video_airpush_click");
            }

            @Override
            public void onOpened() {
                Log.d("bannerAd", "onOpened");
            }

            @Override
            public void onClosed() {
                Log.d("bannerAd", "onClosed");
            }

            @Override
            public void onLeaveApplication() {
                Log.d("bannerAd", "onLeaveApplication");
            }
        });
        banner.load();
    }

    public interface airpushAdLoadLisenter {
        void onLoad();
        void onClick();
    }

    private airpushAdLoadLisenter mAirpushAdLoadLisenter;

    public void setmAirpushAdLoadLisenter(airpushAdLoadLisenter a) {
        this.mAirpushAdLoadLisenter = a;
    }


}
