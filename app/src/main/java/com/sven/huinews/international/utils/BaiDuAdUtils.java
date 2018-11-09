package com.sven.huinews.international.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duapps.ad.AdError;
import com.duapps.ad.DuAdDataCallBack;
import com.duapps.ad.PullRequestController;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.list.AdListArrivalListener;
import com.duapps.ad.list.DuNativeAdsManager;
import com.google.android.gms.ads.MobileAds;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sven.huinews.international.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Burgess on 2018/9/27 0027.
 */

public class BaiDuAdUtils {
    private Boolean isGetAd = false;
    private static final int UDAdsPID = 156605;
    private Activity mActivity;
    private static BaiDuAdUtils instance;
    private List<View> udAdViewList = new ArrayList<>();//存放view的list
    private LinkedList<NativeAd> lists;
    private static int CACHESZIE;
    private ImageLoader imageLoader;
    private DuNativeAdsManager adsManager;
    private Handler mHandler = new Handler();
    private static final String UDTAG = "UD广告";
    private int mPositon = 0;
    private UdAdLisenter mUdAdLisenter;
    private NativeAd mNativeAD;
    private int mLoadDuType = 0;

    public BaiDuAdUtils(Activity activity, int mLoadDuType) {
        this.mLoadDuType = mLoadDuType;
        this.mActivity = activity;
    }

    /**
     * 获取百度广告数据
     */
    public void getUDAds(int size) {
        Log.d("广告", "百度广告是否正在获取=" + isGetAd + ";百度广告数量=" + size);
        if (isGetAd || size <= 0) return;
        else isGetAd = true;

        //判断是第一次获取还是重复获取 重复获取进行销毁原数据操作
        if (udAdViewList == null) {
            udAdViewList = new ArrayList<>();
        } else {
            for (View view : udAdViewList) {
                view.destroyDrawingCache();
            }
            udAdViewList.clear();
        }
        if (lists == null) {
            lists = new LinkedList<NativeAd>();
        } else {
            for (NativeAd nativeAd : lists) {
                nativeAd.destroy();
            }
            lists.clear();
        }

        this.CACHESZIE = size;
        imageLoader = ImageLoaderHelper.getInstance(mActivity);
        if (adsManager == null) {
            adsManager = new DuNativeAdsManager(mActivity, UDAdsPID, 5);
        }
        adsManager.setListener(listener);
        adsManager.load();
    }

    /**
     * 设置获取百度广告数据监听
     */
    AdListArrivalListener listener = new AdListArrivalListener() {
        NativeAd nativeAD;

        //返回广告list
        @Override
        public void onAdLoaded(List arg0) {
            if (!lists.isEmpty()) {
                lists.clear();
            }
            for (int i = 0; i < arg0.size(); i++) {
                //获取单个广告对象
                nativeAD = (NativeAd) arg0.get(i);
                if (!(nativeAD.equals(null))) {
                    lists.add(nativeAD);
                }
            }
            if (lists.size() >= CACHESZIE) {
                mUdAdLisenter.onAdLoadedListData(lists);
                if (mLoadDuType == 1) {
                    isGetAd = false;
                }
//                mHandler.removeCallbacksAndMessages(null);
                mHandler.post(mRunnable);
            }

        }

        //返回广告错误码
        @Override
        public void onAdError(com.duapps.ad.AdError arg0) {
            Log.d(UDTAG, "onError : " + arg0.getErrorCode());
            if (arg0.getErrorCode() == 1000) {
                Log.d(UDTAG, "onError : 客户端网络错误");
            } else if (arg0.getErrorCode() == 1001) {
                Log.d(UDTAG, "onError : 没有获取到广告数据");
            } else if (arg0.getErrorCode() == 1002) {
                Log.d(UDTAG, "onError : 请求接口过频繁");
            } else if (arg0.getErrorCode() == 1003) {
                Log.d(UDTAG, "onError : 展示超出限制");
            } else if (arg0.getErrorCode() == 2000) {
                Log.d(UDTAG, "onError : 服务器错误");
            } else if (arg0.getErrorCode() == 2001) {
                Log.d(UDTAG, "onError : 服务器网络错误");
            } else if (arg0.getErrorCode() == 3000) {
                Log.d(UDTAG, "onError : 获取广告数据等待时间超时");
            } else if (arg0.getErrorCode() == 3001) {
                Log.d(UDTAG, "onError : 未知错误");
            }
            isGetAd = false;
        }

    };


    /**
     * 数据绑定
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isGetAd) {
                if (mPositon < lists.size()) {
                    mNativeAD = lists.get(mPositon);
                    mNativeAD.setMobulaAdListener(callback);
                    String url = mNativeAD.getAdCoverImageUrl();
                    View v = LayoutInflater.from(mActivity).inflate(R.layout.fragment_ud_ad, null);
                    if (TextUtils.isEmpty(url)) {
                        showSmallAdView(mNativeAD, v);
                    } else {
                        showBigAdView(mNativeAD, v);
                    }
                    udAdViewList.add(v);
                    mPositon++;
                } else {
                    isGetAd = false;
                    mPositon = 0;
                    if (mUdAdLisenter != null) {
                        mUdAdLisenter.onAdLoaded(udAdViewList);
                        lists.clear();
                        PullRequestController.getInstance(mActivity).clearCache();
                        adsManager.setListener(null);
                        adsManager.destroy();
                    }
                }
            }
            mHandler.postDelayed(mRunnable, 3000);
        }
    };


    /**
     * show small ad 显示小图广告
     *
     * @param ad
     */
    private void showSmallAdView(NativeAd ad, View v) {
        ((TextView) v.findViewById(R.id.small_card_name)).setText(ad.getAdTitle());
        ((RatingBar) v.findViewById(R.id.small_card_rating)).setRating(ad.getAdStarRating());
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
////                .setControllerListener(controllerListener)
//                .setUri(ad.getAdIconUrl())
//                .build();
//        ((SimpleDraweeView)v.findViewById(R.id.small_card_icon)).setController(controller);
        imageLoader.displayImage(ad.getAdIconUrl(), (ImageView) v.findViewById(R.id.small_card_icon));
        ((TextView) v.findViewById(R.id.small_card__des)).setText(ad.getAdBody());
        ((TextView) v.findViewById(R.id.small_card_btn)).setText(ad.getAdCallToAction());
        mNativeAD.registerViewForInteraction((TextView) v.findViewById(R.id.small_card_btn));
        ((RelativeLayout) v.findViewById(R.id.big_ad)).setVisibility(View.GONE);
        ((RelativeLayout) v.findViewById(R.id.small_ad)).setVisibility(View.VISIBLE);
        ((ProgressBar) v.findViewById(R.id.load_view)).setVisibility(View.GONE);
        v.findViewById(R.id.white_bg).setVisibility(View.GONE);
    }

    /**
     * show big ad 显示da图广告
     *
     * @param ad
     */
    private void showBigAdView(NativeAd ad, View v) {
        ((RelativeLayout) v.findViewById(R.id.small_ad)).setVisibility(View.GONE);
        ((ImageView) v.findViewById(R.id.card_image)).setVisibility(View.VISIBLE);
        ((TextView) v.findViewById(R.id.card_name)).setText(ad.getAdTitle());
        ((RatingBar) v.findViewById(R.id.card_rating)).setRating(ad.getAdStarRating());
        final ProgressBar loadView = (ProgressBar) v.findViewById(R.id.load_view);
        imageLoader.displayImage(ad.getAdIconUrl(), (ImageView) v.findViewById(R.id.card_icon));
        imageLoader.displayImage(ad.getAdCoverImageUrl(), (ImageView) v.findViewById(R.id.card_image), new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String paramString, View paramView) {
            }

            @Override
            public void onLoadingFailed(String paramString, View paramView, FailReason paramFailReason) {
            }

            @Override
            public void onLoadingComplete(String paramString, View paramView, Bitmap paramBitmap) {
                loadView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String paramString, View paramView) {
            }
        });

        ((TextView) v.findViewById(R.id.card__des)).setText("");
        ((TextView) v.findViewById(R.id.card_btn)).setText(ad.getAdCallToAction());
        v.findViewById(R.id.white_bg).setVisibility(View.GONE);
        mNativeAD.registerViewForInteraction(((TextView) v.findViewById(R.id.card_btn)));
    }


    /**
     * 设置百度海外广告监听
     */
    DuAdDataCallBack callback = new DuAdDataCallBack() {

        @Override
        public void onAdLoaded(NativeAd data) {
        }

        @Override
        public void onAdError(com.duapps.ad.AdError error) {
        }

        @Override
        public void onAdClick() {
            Log.d("UD广告", "onClick : click list ad");
            AnalyticsHelper.logEvent(AnalyticsHelper.EVENT_STREAM_AD_CLICK, null, false);
            MobclickAgent.onEvent(mActivity, Common.AD_UD_LOOK);
            MobclickAgent.onEvent(mActivity, Common.AD_UD_LOOK, "ad_ud_look");
        }
    };

    public void onDestroy() {
//        adsManager.setListener(null);
        adsManager.destroy();
    }

    /**
     * 设置Ud  广告监听
     */
    public void setUdAdLisenter(UdAdLisenter udAdLisenter) {
        this.mUdAdLisenter = udAdLisenter;
    }

    /**
     * Ud  广告监听接口
     */
    public interface UdAdLisenter {
        void onError(AdError error);

        void onAdLoaded(List<View> adViews);

        void onAdLoadedListData(LinkedList<NativeAd> listData);
    }

}
