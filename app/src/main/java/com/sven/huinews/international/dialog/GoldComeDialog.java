package com.sven.huinews.international.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duapps.ad.AdError;
import com.duapps.ad.DuAdListener;
import com.duapps.ad.DuNativeAd;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.TaskRequestAdVideo;
import com.sven.huinews.international.entity.response.ShareResponse;
import com.sven.huinews.international.main.web.JsWebView;
import com.sven.huinews.international.main.web.presenter.WebPresenter;
import com.sven.huinews.international.publicclass.ShareModel;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.ImageLoaderHelper;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.NativeAdFetcher;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.util.List;


/**
 * Created by Sven on 2018/2/5.
 */

public class GoldComeDialog extends Dialog implements JsWebView {
    private TextView tv_coins_size, tv_share;
    private ImageView img_close;
    private LinearLayout ll_ad;
    private static final String TAG = "OpenGoldComeDialog";
    private NativeAdFetcher nativeAdFetcher;//yahoo广告工厂类
    private LinearLayout ll_yahoo_ad;
    private TwitterLogin twitterLogin;

    /**
     * ad recommend id 广告推荐位
     */
    private static final int PID = 156605;//郭雪飞申请
    /**
     * cache data size 缓存大小
     */
    private static final int CACHESZIE = 2;
    /**
     * mobula ad data mobula广告数据类
     */
    private DuNativeAd nativeAd;
    private ImageLoader imageLoader;

    /**
     * the title of the AD View 广告标题
     */
    private TextView titleView;
    private TextView smallTitleView;
    /**
     * the icon of the AD View 广告的icon
     */
    private ImageView iconView;
    private ImageView smallIconView;
    /**
     * the star rating of the AD View 广告星级
     */
    private RatingBar ratingView;
    private RatingBar smallRatingView;
    /**
     * ad describe 广告介绍
     */
    private TextView descView;
    private TextView smallDescView;
    /**
     * ad big image 广告大图
     */
    private ImageView bigImgView;
    /**
     * ad button 广告按钮
     */
    private TextView btnView;
    private TextView smallBtnView;
    /**
     * the loading view 加载视图
     */
    private ProgressBar loadView;
    /**
     * big image ad view 大图广告
     */
    private RelativeLayout bigADLayout;
    /**
     * small image ad view 小图广告
     */
    private RelativeLayout smallADLayout;
    /**
     * load background view 加载背景视图
     */
    private View bgView;
    private FrameLayout fl_du_ad;

    private Context mContext;
    private JsShareType jsShareType;
    private WebPresenter mWebPresenter;
    private ShareModel mShareModel;

    public GoldComeDialog(@NonNull Context context) {
        super(context, R.style.my_dialog);
        setContentView(R.layout.activity_dialog_gold_sigin_in);
        this.mContext = context;
        mWebPresenter = new WebPresenter(this, mContext);
        init();
    }

    public void init() {
        tv_coins_size = findViewById(R.id.tv_coins_size);
        tv_share = findViewById(R.id.tv_share);
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jsShareType == null) {
                    mWebPresenter.getShareInfo();
                } else {
                    share();
                }
            }
        });
        img_close = findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (nativeAdFetcher!=null){
                    nativeAdFetcher.destroyAllAds();
                }

            }
        });
        ll_ad = findViewById(R.id.ll_ad);

        //Ud广告
        bigADLayout = (RelativeLayout) findViewById(R.id.big_ad);
        smallADLayout = (RelativeLayout) findViewById(R.id.small_ad);
        // big image ad
        titleView = (TextView) findViewById(R.id.card_name);
        iconView = (ImageView) findViewById(R.id.card_icon);
        ratingView = (RatingBar) findViewById(R.id.card_rating);
        descView = (TextView) findViewById(R.id.card__des);
        bigImgView = (ImageView) findViewById(R.id.card_image);
        btnView = (TextView) findViewById(R.id.card_btn);
        // small image ad
        smallTitleView = (TextView) findViewById(R.id.small_card_name);
        smallIconView = (ImageView) findViewById(R.id.small_card_icon);
        smallRatingView = (RatingBar) findViewById(R.id.small_card_rating);
        smallDescView = (TextView) findViewById(R.id.small_card__des);
        smallBtnView = (TextView) findViewById(R.id.small_card_btn);

        loadView = (ProgressBar) findViewById(R.id.load_view);
        bgView = findViewById(R.id.white_bg);

        fl_du_ad = findViewById(R.id.fl_du_ad);
        ll_yahoo_ad = findViewById(R.id.ll_yahoo_ad);
    }

    public void initdataDU() {
        imageLoader = ImageLoaderHelper.getInstance(mContext);
        nativeAd = new DuNativeAd(mContext, PID, CACHESZIE);
        if (nativeAd != null) {
            nativeAd.setMobulaAdListener(mListener);
            nativeAd.load();
            ll_ad.setVisibility(View.GONE);
        }
    }

    DuAdListener mListener = new DuAdListener() {

        @Override
        public void onError(DuNativeAd ad, AdError error) {
            Log.d(TAG, "onError : " + error.getErrorCode());
        }

        @Override
        public void onClick(DuNativeAd ad) {
            Log.d(TAG, "onClick : click ad");
        }

        @Override
        public void onAdLoaded(final DuNativeAd ad) {
            Log.d(TAG, "onAdLoaded : " + ad.getTitle());
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ll_ad.setVisibility(View.VISIBLE);
                    showSmallAdView(ad);
                }
            });
        }
    };

    /**
     * show small ad 显示小图广告
     *
     * @param
     */
    private void showSmallAdView(DuNativeAd ad) {
        smallTitleView.setText(ad.getTitle());
        smallRatingView.setRating(ad.getRatings());
        imageLoader.displayImage(ad.getIconUrl(), smallIconView);
        smallDescView.setText(ad.getShortDesc());
        smallBtnView.setText(ad.getCallToAction());
        nativeAd.registerViewForInteraction(smallBtnView);

        bigADLayout.setVisibility(View.GONE);
        smallADLayout.setVisibility(View.VISIBLE);
        loadView.setVisibility(View.GONE);
        bgView.setVisibility(View.GONE);
    }

    private void initYaHoo(){
        //创建yahoo工厂类
        if (nativeAdFetcher == null) {
            nativeAdFetcher = new NativeAdFetcher(AppConfig.getAppContext());
            nativeAdFetcher.addListener(new NativeAdFetcher.AdNativeListener() {
                @Override
                public void onAdCountChanged(List<View> yhAdViews) {
                    Log.d("广告", "yahoo收到广告: " + yhAdViews.size());
//                    mAdapter.addYHAdView(yhAdViews);
                    ll_ad.setVisibility(View.VISIBLE);
                    ll_yahoo_ad.addView(yhAdViews.get(0));
                }

                @Override
                public void onLoad() {
                    ll_ad.setVisibility(View.GONE);
                }
            });
        }
        if (nativeAdFetcher != null) {//请求yahoo广告
            nativeAdFetcher.prefetchAds(1);
        }
    }
    public void showDialog(int count) {
        tv_coins_size.setText("+" + count + " " + mContext.getString(R.string.me_coins));
        adLoad();
        show();
    }

    //循环加载广告
    private void adLoad() {
        int adType = UserSpCache.getInstance(getContext()).getInt(UserSpCache.H5_PAGE_AD_PROJECTILE_FRAME_TYPE);
        switch (adType) {
            case 1://原来是yahoo
                UserSpCache.getInstance(getContext()).putInt(UserSpCache.H5_PAGE_AD_PROJECTILE_FRAME_TYPE, 2);
                //百度海外版显示广告
                initdataDU();//显示百度海外版广告
                fl_du_ad.setVisibility(View.VISIBLE);
                ll_yahoo_ad.setVisibility(View.GONE);
                break;
            case 2://原来是百度海外版
                UserSpCache.getInstance(getContext()).putInt(UserSpCache.H5_PAGE_AD_PROJECTILE_FRAME_TYPE, 1);
                //显示yahoo广告
                initYaHoo();
                fl_du_ad.setVisibility(View.GONE);
                ll_yahoo_ad.setVisibility(View.VISIBLE);
                break;
            default:
                UserSpCache.getInstance(getContext()).putInt(UserSpCache.H5_PAGE_AD_PROJECTILE_FRAME_TYPE, 2);
                //百度海外版显示广告
                initdataDU();//显示百度海外版广告
                fl_du_ad.setVisibility(View.VISIBLE);
                ll_yahoo_ad.setVisibility(View.GONE);
                break;
        }
    }


    public void share() {
        dismiss();
        if (twitterLogin == null) {
            twitterLogin = new TwitterLogin();
            twitterLogin.register();
        }
        twitterLogin.setTwitterShareLisenter(new TwitterLogin.TwitterShareLisenter() {
            @Override
            public void getShareOk(String response) {
                showBaseToast(mContext.getResources().getString(R.string.s_shared));
                mWebPresenter.shareVisit(response, jsShareType.getActivity_type(), jsShareType.getType());
                if (mShareModel == null) {
                    mShareModel = new ShareModel(mContext);
                }
                //2018-7-20 18:50 burgess
                TaskRequestAdVideo taskRequest = new TaskRequestAdVideo();
                taskRequest.setId(TaskRequest.TASK_ID_READ_SHARE_AD + "");
                mShareModel.getAdVideoByTask(taskRequest, new DataCallBack() {
                    @Override
                    public void onSucceed(String josn) {
                    }

                    @Override
                    public void onFail(BaseResponse response) {
                        Toast.makeText(mContext, mContext.getString(R.string.network_unavailable_try_again_later), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                }, 0, null);

            }

            @Override
            public void getShareFail(String response) {
                showBaseToast(mContext.getResources().getString(R.string.s_share_faild));
            }

            @Override
            public void getShareCancel(String response) {
                LogUtil.showLog("msg---分享取消");
            }
        });
        twitterLogin.twitterShare((Activity) mContext, jsShareType, Common.TWITTER_SHARE_IAMGE);
    }

    public void showBaseToast(String msg) {
        ToastUtils.showLong(mContext, msg);
    }

    @Override
    public void onBindWxSucceed(int count) {

    }

    @Override
    public void onShareInfo(ShareResponse shareResponse) {
        JsShareType jsShareType = new JsShareType();
        jsShareType.setUrl(shareResponse.getData().getDefaultX().getUrl());
        jsShareType.setTitle(shareResponse.getData().getDefaultX().getTitle());
        jsShareType.setContent(shareResponse.getData().getDefaultX().getContent());
        jsShareType.setImgUrl(shareResponse.getData().getDefaultX().getImgUrl());
        jsShareType.setImgArray(shareResponse.getData().getDefaultX().getImgArray());
        this.jsShareType = jsShareType;
        share();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {

    }


}
