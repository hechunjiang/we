package com.sven.huinews.international.utils;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.sven.huinews.international.R;
import com.sven.huinews.international.main.web.WebActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


public class GoogleNativeAdsUtils {
    private String AGE = "GoogleNativeAdsUtils";
    private Activity activity;
    private static final String ADMOB_APP_ID = "ca-app-pub-1331916574504070~7058639311";
    //ca-app-pub-1331916574504070/2194442051 正式号   ca-app-pub-3940256099942544/2247696110   测试号
//    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";  //测试号
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-1331916574504070/2194442051";  //正式号
    private AdLoader adLoader;
    private GoogleAdLisenter mGoogleAdLisenter;
    private List<View> mlistview;
    private int adNumber = 2;
    private List<UnifiedNativeAd> listADData;
    private int i = 1;

    public GoogleNativeAdsUtils(final Activity activity) {

        this.activity = activity;
        MobileAds.initialize(activity, ADMOB_APP_ID);
        AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_AD_UNIT_ID);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"Advertiser:"+unifiedNativeAd.getAdvertiser());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"Body:"+unifiedNativeAd.getBody());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"CallToAction:"+unifiedNativeAd.getCallToAction());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"Headline:"+unifiedNativeAd.getHeadline());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"MediationAdapterClassName:"+unifiedNativeAd.getMediationAdapterClassName());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"Price:"+unifiedNativeAd.getPrice());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"Store:"+unifiedNativeAd.getStore());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"AdChoicesInfo:"+unifiedNativeAd.getAdChoicesInfo());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"Extras:"+unifiedNativeAd.getExtras());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"Icon:"+unifiedNativeAd.getIcon());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"Images:"+unifiedNativeAd.getImages());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"StarRating:"+unifiedNativeAd.getStarRating());
//                LogUtil.showLog(AGE+":"+"onUnifiedNativeAdLoaded:"+i+"VideoController:"+unifiedNativeAd.getVideoController().toString());
//                i++;
                if (mlistview == null) {
                    mlistview = new ArrayList<>();
                }
                if (listADData == null) {
                    listADData = new ArrayList<>();
                }
                if (unifiedNativeAd.getImages() != null && unifiedNativeAd.getImages().size() > 0) {
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater()
                            .inflate(R.layout.ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    listADData.add(unifiedNativeAd);
                    mlistview.add(adView);
                    if (adNumber != 0 && adNumber == mlistview.size()) {
                        if (!mAdisLoading()) {
                            if (mGoogleAdLisenter != null) {
                                mGoogleAdLisenter.onAdLoaded(mlistview, listADData);
                            }
                        }
                    }
                }

            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mGoogleAdLisenter != null) {
                    mGoogleAdLisenter.onError(errorCode);
                }
                LogUtil.showLog(AGE + ":" + "Failed to load native ad: "
                        + errorCode);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                LogUtil.showLog(AGE + ":" + "onAdOpened");

            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                LogUtil.showLog(AGE + ":" + "onAdClosed");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                LogUtil.showLog(AGE + ":" + "onAdLoaded");
                MobclickAgent.onEvent(activity, Common.AD_TYPE_GOOGLE_NATIVE_LOOK);
                MobclickAgent.onEvent(activity, Common.AD_TYPE_GOOGLE_NATIVE_LOOK, "ad_type_google_native_look");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                LogUtil.showLog(AGE + ":" + "onAdClicked");
                MobclickAgent.onEvent(activity, Common.AD_TYPE_GOOGLE_NATIVE_CLICK);
                MobclickAgent.onEvent(activity, Common.AD_TYPE_GOOGLE_NATIVE_CLICK, "ad_type_google_native_click");
            }
        }).build();
    }

    public void loadAd() {
        adNumber = 1;
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loadAds(int number) {
        if(number<=0)return;
        adNumber = number;
        if (listADData != null && listADData.size() > 0) {
            for (int i = 0; i < listADData.size(); i++) {
                listADData.get(i).destroy();
            }
        }
        if (mlistview != null && mlistview.size() > 0) {
            mlistview.clear();
        }

        if (!adLoader.isLoading()) {
            adLoader.loadAds(new AdRequest.Builder().build(), number > 0 && number < 4 ? number + 2 : 5);
        }
    }

    public void destroy() {
        if (listADData != null && listADData.size() > 0) {
            for (int i = 0; i < listADData.size(); i++) {
                listADData.get(i).destroy();
            }
        }
    }


    /**
     * 是否正在加载广告
     *
     * @return
     */
    public boolean mAdisLoading() {
        return adLoader.isLoading();
    }

    public void setGoogleAdLisenter(GoogleAdLisenter mGoogleAdLisenter) {
        this.mGoogleAdLisenter = mGoogleAdLisenter;
    }

    /**
     * google  广告监听接口
     */
    public interface GoogleAdLisenter {
        void onError(int errorCode);

        void onAdLoaded(List<View> list, List<UnifiedNativeAd> listADData);
    }


    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView   the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);


        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {/*
            videoStatus.setText(String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    vc.getAspectRatio()));*/

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
//                    refresh.setEnabled(true);
//                    videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
//            videoStatus.setText("Video status: Ad does not contain a video asset.");
//            refresh.setEnabled(true);
        }
    }
}
