package com.sven.huinews.international.utils;

import android.content.Context;
import android.util.Log;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by W.mago on 2018/9/7 0007.
 */
public class VungleAdUtils {
//    private Context mContext;
//    private String appId = "5b7e9eebf6d8a002825ca943";//郭雪飞申请
//    private String adRewardCodeId = "REWARD-2913311";//奖励广告  无缓存
//    private String adInterstitial = "DEFAULT-0184226";//插页广告 有缓存
//    private String LOG_TAG = "VungleAdUtils";
////    private UserModel userModel;
//
//    public static boolean adInit = false;
//    private AdConfig adConfig = new AdConfig();
//    private boolean needLoad;//默认无缓存
//
//    public VungleAdUtils(Context context) {
//        this.mContext = context;
//        this.needLoad = false;
//    }
//
//    public VungleAdUtils(Context context, boolean needLoad) {
//        this.mContext = context;
//        this.needLoad = needLoad;
//        initSDK();
//    }
//
//    /**
//     * 初始化vungle广告
//     */
//    public void initSDK() {
//        /**
//         * 加载监听
//         */
//        Vungle.init(appId, mContext, new InitCallback() {
//            @Override
//            public void onSuccess() {//完成
////                Log.d(LOG_TAG,"onSuccess");
//                adInit = true;
//                if (needLoad) {
//                    /**
//                     * 需要缓存的时候
//                     */
//                    Vungle.loadAd(adRewardCodeId, vungleLoadAdCallback);
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                adInit = false;
////                Log.d(LOG_TAG, "InitCallback - onError: " + throwable.getLocalizedMessage());
//            }
//
//            @Override
//            public void onAutoCacheAdAvailable(String placementReferenceID) {
//                adInit = true;
////                Log.d(LOG_TAG,"onAutoCacheAdAvailable");
//            }
//        });
//    }
//
//
//    public void openNoLoadAd() {
//        if (needLoad){//需要缓存的时候
//            if (Vungle.isInitialized() && Vungle.canPlayAd(adRewardCodeId)) {
//                openConfigure();
//                Vungle.playAd(adRewardCodeId, null, vunglePlayAdCallback);
//            }
//        }else{//不需要缓存的时候
//            if (Vungle.isInitialized() && Vungle.canPlayAd(adInterstitial)) {
//                openConfigure();
//                Vungle.playAd(adInterstitial, null, vunglePlayAdCallback);
//            }
//        }
//    }
//
//    /**
//     * 播放前的配置
//     */
//    private void openConfigure(){
//        Vungle.setIncentivizedFields("TestUser", "Ad reward", "You won't be rewarded if you close the Ad ", "Watch", "STILL CLOSE");
//        adConfig.setBackButtonImmediatelyEnabled(true);
//        adConfig.setAutoRotate(true);
//        adConfig.setMuted(false);
//    }
//
//    /**
//     * 判断是否完成缓存
//     *
//     * @return
//     */
//    public boolean isTheCacheComplete() {
//        if (needLoad) {
//            return Vungle.isInitialized() && Vungle.canPlayAd(adInterstitial);
//        } else {
//            return Vungle.isInitialized() && Vungle.canPlayAd(adRewardCodeId);
//        }
//    }
//
//
//    /**
//     * 加载缓存监听
//     */
//    private final LoadAdCallback vungleLoadAdCallback = new LoadAdCallback() {
//        @Override
//        public void onAdLoad(final String placementReferenceID) {
//            Log.d(LOG_TAG, "LoadAdCallback - onAdLoad" +
//                    "\n\tPlacement Reference ID = " + placementReferenceID);
//        }
//
//        @Override
//        public void onError(final String placementReferenceID, Throwable throwable) {
//            Log.d(LOG_TAG, "LoadAdCallback - onError" +
//                    "\n\tPlacement Reference ID = " + placementReferenceID +
//                    "\n\tError = " + throwable.getLocalizedMessage());
//            checkInitStatus(throwable);
//        }
//    };
//
//    /**
//     * 播放状态/加载缓存，异常处理
//     *
//     * @param throwable
//     */
//    private void checkInitStatus(Throwable throwable) {
//        try {
//            VungleException ex = (VungleException) throwable;
//            Log.d(LOG_TAG, ex.getExceptionCode() + "");
//            if (ex.getExceptionCode() == VungleException.VUNGLE_NOT_INTIALIZED) {
//                initSDK();
//            }
//        } catch (ClassCastException cex) {
//            Log.d(LOG_TAG, cex.getMessage());
//        }
//    }
//
//    /**
//     * 播放监听
//     */
//    private final PlayAdCallback vunglePlayAdCallback = new PlayAdCallback() {
//        @Override
//        public void onAdStart(final String placementReferenceID) {
//            Log.d(LOG_TAG, "PlayAdCallback - onAdStart" +
//                    "\n\tPlacement Reference ID = " + placementReferenceID);
//            //添加友盟统计
//            //开始播放广告
//            //曝光
//            MobclickAgent.onEvent(mContext, Common.AD_TYPE_VUNGLE_VIDEO_LOOK);
//            MobclickAgent.onEvent(mContext, Common.AD_TYPE_VUNGLE_VIDEO_LOOK, "ad_vungle_video_look");
//        }
//
//        @Override
//        public void onAdEnd(final String placementReferenceID, final boolean completed, final boolean isCTAClicked) {
//            Log.d(LOG_TAG, "PlayAdCallback - onAdEnd" +
//                    "\n\tPlacement Reference ID = " + placementReferenceID +
//                    "\n\tView Completed = " + completed + "" +
//                    "\n\tDownload Clicked = " + isCTAClicked);
//
//            if (completed){
//                MobclickAgent.onEvent(mContext, Common.AD_TYPE_VUNGLE_VIDEO_COMPLETED);
//                MobclickAgent.onEvent(mContext, Common.AD_TYPE_VUNGLE_VIDEO_COMPLETED, "ad_vungle_video_completed");
//            }
//            if (isCTAClicked){
//                MobclickAgent.onEvent(mContext, Common.AD_TYPE_VUNGLE_VIDEO_CLICK);
//                MobclickAgent.onEvent(mContext, Common.AD_TYPE_VUNGLE_VIDEO_CLICK, "ad_vungle_video_click");
//            }
//
//            if (isCTAClicked || completed){
////                if (userModel == null) {
////                    userModel = new UserModel(mContext);
////                }
////                /*2018-7-20 18:50*/
////                TaskRequestAdVideo taskRequest = new TaskRequestAdVideo();
////                taskRequest.setId(TaskRequest.TASK_ID_READ_VIDEO_AD + "");
////                userModel.getAdVideoByTask(taskRequest, new DataCacheBack<Integer>() {
////                    @Override
////                    public void onSucceed(Integer integer) {
////                        initSDK();
////                    }
////
////                    @Override
////                    public void onFail(BaseResponse response) {
////                        Toast.makeText(mContext, "Network unavailable.Try again later.", Toast.LENGTH_SHORT).show();
////                    }
////
////                    @Override
////                    public void onComplete() {
////                    }
////                }, 0, null);
//            }
//        }
//
//        @Override
//        public void onError(final String placementReferenceID, Throwable throwable) {
//            Log.d(LOG_TAG, "PlayAdCallback - onError" +
//                    "\n\tPlacement Reference ID = " + placementReferenceID +
//                    "\n\tError = " + throwable.getLocalizedMessage());
//            checkInitStatus(throwable);
//        }
//    };


}
