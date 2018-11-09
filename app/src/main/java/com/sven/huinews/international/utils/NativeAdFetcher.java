/*
 * Copyright 2015 Yahoo Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sven.huinews.international.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdNative;
import com.flurry.android.ads.FlurryAdNativeListener;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Handles fetching, caching, and destroying of native gemini ads.</p>
 *  yahoo 获取广告的工厂类
 */
public class NativeAdFetcher {
    private static final String LOG_TAG = "MainActivity";
    /**
     * Maximum number of ads to prefetch.
     */
    private int my_ad_size= 3;
    /**
     * Maximum number of times to try fetch an ad after failed attempts.
     */
    private static final int MAX_FETCH_ATTEMPT = 3;
    private AdNativeListener mAdNativeListener;
    private List<FlurryAdNative> mPrefetchedAdList = new ArrayList<>();//拿到的广告数据
    private List<FlurryAdNative> mFetchingAdsList = new ArrayList<>();//去请求的广告数据 其实应该是同一个adNative
    private List<View> adViewList = new ArrayList<>();//存放view的list
    private int mNoOfFetchedAds;
    private int mFaildeAttempts;
    private Context mContext;
    private Boolean isGetAd = false;
    private FlurryAdNativeListener mFlurryAdNativeListener = new FlurryAdNativeListener() {
        @Override
        public void onFetched(FlurryAdNative adNative) {
            Log.d("广告", "onFetched: "+mNoOfFetchedAds);
            if (canUseThisAd(adNative)) {
                mPrefetchedAdList.add(adNative);

                View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_yahoo_ad,null);
                // for instance, you can parse the adNative for the assets returned and create the view to display the ad
                parseAssets(adNative,v ,mPrefetchedAdList.size()-1);
                //required to support ad tracking
                adNative.setTrackingView(v.findViewById(R.id.adContainerLayout));
                adViewList.add(v);

            }else if (mFetchingAdsList.contains(adNative)) {
                mFetchingAdsList.remove(adNative);
            }

            mNoOfFetchedAds = mPrefetchedAdList.size();

            ensurePrefetchAmount();

            if (isLoaded()){
                notifyObserversOfAdSizeChange();
            }
        }

        @Override
        public void onShowFullscreen(FlurryAdNative adNative) {
        }

        @Override
        public void onCloseFullscreen(FlurryAdNative adNative) {
            AnalyticsHelper.logEvent(AnalyticsHelper.EVENT_AD_CLOSEBUTTON_CLICK, null, false);
        }

        @Override
        public void onAppExit(FlurryAdNative adNative) {
        }

        @Override
        public void onClicked(FlurryAdNative adNative) {
            //点击
            Log.d("广告", "onClicked: ");
            AnalyticsHelper.logEvent(AnalyticsHelper.EVENT_STREAM_AD_CLICK, null, false);
            MobclickAgent.onEvent(mContext, Common.AD_TYPE_YAHOO_CLICK);
            MobclickAgent.onEvent(mContext, Common.AD_TYPE_YAHOO_CLICK, "yahoo");
        }

        @Override
        public void onImpressionLogged(FlurryAdNative adNative) {
            //曝光
            Log.d("广告", "onImpressionLogged: ");
            MobclickAgent.onEvent(mContext, Common.AD_TYPE_YAHOO_LOOK);
            MobclickAgent.onEvent(mContext, Common.AD_TYPE_YAHOO_LOOK, "yahoo_look");
        }

        @Override
        public void onExpanded(FlurryAdNative flurryAdNative) {
        }

        @Override
        public void onCollapsed(FlurryAdNative flurryAdNative) {
        }

        @Override
        public void onError(FlurryAdNative adNative, FlurryAdErrorType adErrorType, int errorCode) {
            Log.d("广告", "onError: "+errorCode);
            if (adErrorType.equals(FlurryAdErrorType.FETCH)) {
                Log.i(LOG_TAG, "onFetchFailed " + errorCode);

                if (mFetchingAdsList.contains(adNative)) {
                    mFaildeAttempts++;
                    adNative.destroy(); // destroy the native ad, as we are not going to render it.
                    mFetchingAdsList.remove(adNative); // Remove from the tracking list
                }
                //如果失败次数小于最大失败测试的话 继续获取广告
                //否则返回
                if (mFaildeAttempts < MAX_FETCH_ATTEMPT) ensurePrefetchAmount();
                else isGetAd = false;
            }
        }
    };

    public NativeAdFetcher(Context context){
        this.mContext = context;
    }

    /**
     * Adds an {@link AdNativeListener} that would be notified for any changes to the native ads
     * list.
     *  
     * @param listener the listener to be notified
     */
    public void addListener(AdNativeListener listener) {
        this.mAdNativeListener = listener;
    }


    /**
     * Gets the number of ads that have been fetched so far.
     *
     * @return the number of ads that have been fetched
     */
    private int getFetchedAdsCount() {
        return mNoOfFetchedAds;
    }

    /**
     * Fetches a new native ad.
     *                
     * @see #destroyAllAds()
     */
    public void prefetchAds(int size) {
        if (isGetAd || size <= 0) return;
        else {
            isGetAd = true;
            this.my_ad_size = size;
        }
        Log.d("广告", "请求yahoo广告: ");
        //如果是再次请求的话 处理之前的数据
        for (FlurryAdNative ad : mPrefetchedAdList) {
            ad.destroy();
        }
        mPrefetchedAdList.clear();
        mNoOfFetchedAds = 0;//已经获取到的数据数置0

        for (FlurryAdNative ad : mFetchingAdsList) {
            ad.destroy();
        }
        mFetchingAdsList.clear();
        mFaildeAttempts = 0;//已经失败的数据数置0

        for (View view : adViewList){
            view.destroyDrawingCache();
        }
        adViewList.clear();
        mAdNativeListener.onLoad();
        fetchAd();
    }

    /**
     * Destroys ads that have been fetched, that are still being fetched and removes all resource
     * references that this instance still has. This should only be called when the Activity that
     * is showing ads is closing, preferably from the {@link android.app.Activity#onDestroy()}.
     * </p>
     * The converse of this call is {@link #prefetchAds(int size)}.
     */
    public void destroyAllAds() {

        for (FlurryAdNative ad : mPrefetchedAdList) {
            ad.destroy();
        }
        mPrefetchedAdList.clear();
        mNoOfFetchedAds = 0;

        for (FlurryAdNative ad : mFetchingAdsList) {
            ad.destroy();
        }
        mFetchingAdsList.clear();

        for (View view : adViewList){
            view.destroyDrawingCache();
        }
        adViewList.clear();

        Log.i(LOG_TAG, "destroyAllAds adViewList " + adViewList.size() + " prefetched " +
                mPrefetchedAdList.size() + " fetched " + mFetchingAdsList.size());

        mContext = null;

//        notifyObserversOfAdSizeChange();
    }

    /**
     *  Notifies all registered {@link AdNativeListener} of a change in ad data count.
     */
    private void notifyObserversOfAdSizeChange() {
        isGetAd = false;
        if (mAdNativeListener != null)
            mAdNativeListener.onAdCountChanged(getAdViewList());
    }

    /**
     * Fetches a new native ad.
     */
    private void fetchAd() {
        if (mContext != null) {
            Log.i(LOG_TAG, "Fetching Ad now");
            FlurryAdNative nativeAd = new FlurryAdNative(
                    mContext, AppConfig.FLURRY_ADSPACE);
            nativeAd.setListener(mFlurryAdNativeListener);
            mFetchingAdsList.add(nativeAd);
            nativeAd.fetchAd();
        } else {
            Log.i(LOG_TAG, "Context is null, not fetching Ad");
        }
    }

    /**
     * Ensures that the necessary amount of prefetched native ads are available.
     */
    private void ensurePrefetchAmount() {
        if (getFetchedAdsCount() < my_ad_size ) {
            fetchAd();
        }
    }

    /**
     * Determines if the native ad can be used.
     *
     * @param adNative  the native ad object
     * @return <code>true</code> if the ad object can be used, false otherwise
     */
    private boolean canUseThisAd(FlurryAdNative adNative) {
        return adNative != null && adNative.isReady() && !adNative.isExpired();
    }

    /**
     * Listener that is notified when changes to the list of fetched native ads are made.
     */
    public interface AdNativeListener {
        /**
         * Triggered when the number of ads have changed. Adapters that implement this class
         * should notify their data views that the dataset has changed.
         */
        void onAdCountChanged(List<View> yhAdViews);

        void onLoad();
    }

    /**
     * 判断广告是否拿完
     * by w.mago
     * */
    private Boolean isLoaded(){
        return (mNoOfFetchedAds >= my_ad_size);
    }

    /**
     * adNative 与 view绑定
     * by w.mago
     * */
    private void parseAssets (FlurryAdNative adNative , View v , int isVideo) {

        if (adNative.getAsset("headline") != null) {
            adNative.getAsset("headline").loadAssetIntoView(v.findViewById(R.id.newsTitle));
        }
        if (adNative.getAsset("summary") != null) {
            adNative.getAsset("summary").loadAssetIntoView(v.findViewById(R.id.newsSummary));
        }
        if (adNative.getAsset("source") != null) {
            adNative.getAsset("source").loadAssetIntoView(v.findViewById(R.id.sponsoredPublisher));
        }

        // video ads do not have an explicit asset in the assetList
        // use isVideoAd() property instead to check.
        if(adNative.isVideoAd()) {
            // use "videoUrl" string to instruct the loader to load the video
            // mAdVideo is a RelativeLayout where the video is presented
            Log.d(LOG_TAG, "视频广告");
            final FrameLayout mAdVideo = v.findViewById(R.id.mAdVideo);
            mAdVideo.setVisibility(View.VISIBLE);
            v.findViewById(R.id.mainImage).setVisibility(View.GONE);
            adNative.getAsset("videoUrl").loadAssetIntoView(mAdVideo);
            AppConfig.getInstance().getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    mAdVideo.requestLayout();
                }
            });
            return;
        } else {
            v.findViewById(R.id.mAdVideo).setVisibility(View.GONE);
            v.findViewById(R.id.mainImage).setVisibility(View.VISIBLE);
            // adNative is not a video ad, so render an image as usual
            // video ads do not expose image assets
            if (adNative.getAsset("secHqBrandingLogo") != null) {
                adNative.getAsset("secHqBrandingLogo").loadAssetIntoView(v.findViewById(R.id.sponsoredImage));
            }
            adNative.getAsset("secHqImage").loadAssetIntoView(v.findViewById(R.id.mainImage));
        }
    }

    /**
     * 拿到广告views
     * by w.mago
     * */
    public List<View> getAdViewList(){
        return adViewList;
    }
}
