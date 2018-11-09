package com.sven.huinews.international.utils;

import android.app.Activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by W.mago on 2018/9/12 0012.
 */
public class GoogleInterstitialAdsUtils {
    private Activity activity;
    private static final String ADMOB_APP_ID = "ca-app-pub-1331916574504070~7058639311";
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-1331916574504070/3001358977";
    private InterstitialAd mInterstitialAd;
    private String look = "";
    private String click = "";

    public GoogleInterstitialAdsUtils(final Activity activity) {
        this.activity = activity;
        MobileAds.initialize(activity, ADMOB_APP_ID);
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(ADMOB_AD_UNIT_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                if (!click.equals("")) {
                    MobclickAgent.onEvent(activity, look);
                    MobclickAgent.onEvent(activity, look, look);
                }
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                if (!click.equals("")) {
                    MobclickAgent.onEvent(activity, click);
                    MobclickAgent.onEvent(activity, click, click);
                }

            }

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }


    public boolean isLoad() {
        return mInterstitialAd.isLoaded();
    }

    public void showAd(String look, String click) {
        this.look = look;
        this.click = click;
        mInterstitialAd.show();
    }
}
