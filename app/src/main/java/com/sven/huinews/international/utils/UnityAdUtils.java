package com.sven.huinews.international.utils;

import android.app.Activity;

import com.sven.huinews.international.publicclass.AddGoldPresenter;
import com.sven.huinews.international.publicclass.AddGoldView;
import com.umeng.analytics.MobclickAgent;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

/**
 * 作者：burgess by Burgess on 2018/9/8 02:15
 * 作用：wechatEarn
 */
public class UnityAdUtils implements AddGoldView {
    private String gameId = "2776392";//郭雪飞申请
    private Activity mActivity;
    final private UnityAdsListener unityAdsListener = new UnityAdsListener();
    private AddGoldPresenter mAddGoldPresenter;

    public UnityAdUtils(Activity activity){
        this.mActivity = activity;
        mAddGoldPresenter = new AddGoldPresenter(this, mActivity);
        initUnityAd();
    }

    public void initUnityAd(){
        UnityAds.initialize(mActivity,gameId,unityAdsListener);
    }

    public void show(){
        UnityAds.show(mActivity);
    }

    public boolean isReady(){
        return UnityAds.isReady();
    }

    @Override
    public void showGoldCome(int count, int type, String masgess) {

    }

    @Override
    public void showGoldTime(int time) {

    }

    @Override
    public void showGoldSignInCome(int count, int type, String masgess) {

    }

    private class UnityAdsListener implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady(String s) {
            //加载完成
            LogUtil.showLog("msg---UnityAdUtils:"+"onUnityAdsReady");
        }

        @Override
        public void onUnityAdsStart(String s) {
            LogUtil.showLog("msg---UnityAdUtils:"+"onUnityAdsStart");
            //曝光
            MobclickAgent.onEvent(mActivity, Common.AD_TYPE_UNITY_VIDEO_LOOK);
            MobclickAgent.onEvent(mActivity, Common.AD_TYPE_UNITY_VIDEO_LOOK, "ad_unity_video_look");
        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
            LogUtil.showLog("msg---UnityAdUtils:"+"onUnityAdsFinish");
            //播放完成  包括跳过的
            MobclickAgent.onEvent(mActivity, Common.AD_TYPE_UNITY_VIDEO_COMPLETED);
            MobclickAgent.onEvent(mActivity, Common.AD_TYPE_UNITY_VIDEO_COMPLETED, "ad_unity_video_completed");
            if (mAddGoldPresenter!=null){
                mAddGoldPresenter.getExcitingVideo();
            }

        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
            LogUtil.showLog("msg---UnityAdUtils:"+"onUnityAdsError");
        }
    }
}
