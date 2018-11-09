package com.sven.huinews.international.main.home.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ap.ApBanner;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.entity.UserInfoResponse;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.response.FacebookRegResponse;
import com.sven.huinews.international.entity.response.PushTaskResponse;
import com.sven.huinews.international.main.earn.EarnActivity;
import com.sven.huinews.international.main.home.activity.MyVideoActivity;
import com.sven.huinews.international.entity.response.TwitterRegResponse;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.main.home.contract.MeFragmentContract;
import com.sven.huinews.international.main.home.dialog.OtherActivityDialog;
import com.sven.huinews.international.main.home.model.MeFragmentModel;
import com.sven.huinews.international.main.home.presenter.MeFragmentPresenter;
import com.sven.huinews.international.main.login.activity.LoginActivity;
import com.sven.huinews.international.main.me.FeedbackActivity;
import com.sven.huinews.international.main.me.MessageActivity;
import com.sven.huinews.international.main.me.SettingActivity;
import com.sven.huinews.international.main.permsg.PersonActivity;
import com.sven.huinews.international.main.task.activity.TaskActivity;
import com.sven.huinews.international.main.web.PersonalJs;
import com.sven.huinews.international.main.web.WebActivity;
import com.sven.huinews.international.tplatform.facebook.FaceBookShare;
import com.sven.huinews.international.tplatform.facebook.FacebookPlatform;
import com.sven.huinews.international.tplatform.google.GoogleLogin;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.tplatform.whatsapp.WhatsAppShare;
import com.sven.huinews.international.utils.AirpushAdUtils;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.ImageUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.MyRefreshLayout;
import com.sven.huinews.international.view.dialog.CustomLoginDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.sven.huinews.international.main.home.activity.MainActivity.googleLogin;


public class MeFragment extends BaseFragment<MeFragmentPresenter, MeFragmentModel> implements MeFragmentContract.View {
    private TextView tvMane, tvMeCoins, tvMeVideo, tvMeTotalIncome,
            tvMelExchange, tvMeContact, tvMeSettings, tvAgree, tvMeInput, tvMeRank;
    private MyRefreshLayout swipRfLayout;
    private SimpleDraweeView headImg;
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
    private ImageButton btn_msg;
    private ImageView iv_tips, iv_faceShareLink, iv_ttShareLink, iv_InsShareLink, iv_WhatsShareLink;
    private PushTaskResponse.DataBean.PageListBean pageListBean;
    private String otherActivityUrl = "";
    private long delayMillis = 2000;//首页活动推送弹框
    private OtherActivityDialog mOtherActivityDialog;
    private int counter;
    private PersonalJs mPersonalJs;
    private boolean isShow = true, isLogin = false;
    Timer timer = new Timer();
    private Window window;
    private View ll_un_login;
    private View cl_login;
    private View ll_invition_code;
    private TextView tv_invition_code;
    private TextView tv_copy;

    private ClipboardManager myClipboard;
    private TextView tv_login;
    private CustomLoginDialog loginDialog;

    private FrameLayout fAdView;
    private ImageButton close_ad;
    private AdView mAdView;

    //广告
    private TranslateAnimation mShowAction;//显示动画
    private TranslateAnimation mHiddenAction;//隐藏动画

    //airpush广告
    private AirpushAdUtils airpushAdUtils;
    private LinearLayout ll_friend, ll_coins, ll_videos;
    private int otherLogin = 0;
    private FacebookPlatform mFacebookPlatform;
    private TwitterLogin mTwitterLogin;
    //    private GoogleLogin googleLogin;
    private FacebookRegResponse mFacebookRegResponse;
    private TwitterRegResponse mTwitterRegResponse;
    //    private GoogleSignInAccount mGoogleLogin;
    private WhatsAppShare whatsAppShare;
    private TextView tv_sign, tv_age, tv_location;

    public void initdata() {
        setMVP();
        mPresenter.getPersonMsg();
    }

    @Override
    protected int getLayoutResource() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_me_new;
    }

    @Override
    public void initObject() {
        setMVP();
        mPresenter.getPersonMsg();
        pageListBean = mUserSpCache.getPageListBean();
        mPersonalJs = new PersonalJs(getActivity());
        startTimer();
        setTips();

        adLoad();
    }

    @Override
    public void onResume() {
        super.onResume();
        swipRfLayout.autoRefresh();
    }

    @Override
    protected void loadData() {
    }

    private void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                counter = mUserSpCache.getInt("counter");
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 1000);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (pageListBean != null) {
                if (msg.what == 0x123) {
                    if (0 == pageListBean.getCenter().getTime() || counter == pageListBean.getCenter().getTime()) {
                        if (pageListBean.getCenter().isIsShow()) {
                            otherActivityUrl = pageListBean.getCenter().getRedirect();
                            ImageUtils.loadImgGetResult(getActivity(), pageListBean.getCenter().getPicUrl(), new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(final Drawable resource, Transition<? super Drawable> transition) {
                                    //延迟弹出
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isShow) {
                                                mOtherActivityDialog.showDialog(resource);
                                            }
                                        }
                                    }, delayMillis);
                                }
                            });
                        }
                    }
                }
            }
            return false;
        }
    });

    @Override
    protected void initView(View v) {
        swipRfLayout = v.findViewById(R.id.person_sr);
        swipRfLayout.finishLoadmore();
        swipRfLayout.setEnableLoadmore(false);
        headImg = v.findViewById(R.id.iv_me_head);
//        googleLogin = new GoogleLogin(getActivity(), this);

        whatsAppShare = new WhatsAppShare(mContext);
        RoundingParams rp = new RoundingParams();
        rp.setRoundAsCircle(true);
        GenericDraweeHierarchy genericDraweeHierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(getResources())
                .setRoundingParams(RoundingParams.fromCornersRadii(15f, 15f, 15f, 15f))
                .build();
        headImg.setHierarchy(genericDraweeHierarchyBuilder);
        tvMane = v.findViewById(R.id.tv_me_name);

        tvMeCoins = v.findViewById(R.id.tv_me_w_coins);
        tvMeVideo = v.findViewById(R.id.tv_me_videos);
        tvMeTotalIncome = v.findViewById(R.id.tv_me_w_total_income);

        tvMeInput = v.findViewById(R.id.tv_me_l_input_num); //输入邀请码
        tvMelExchange = v.findViewById(R.id.tv_me_l_exchange);  //提现
        tvMeRank = v.findViewById(R.id.tv_me_l_rank); //排行榜
        tvMeContact = v.findViewById(R.id.tv_me_l_contact); //联系我们
        tvAgree = v.findViewById(R.id.tv_agree_l_enter);  //意见反馈
        tvMeSettings = v.findViewById(R.id.tv_me_L_settings);  //设置

        ll_un_login = v.findViewById(R.id.ll_un_login); //未登录
        cl_login = v.findViewById(R.id.cl_login);  //已登陆
        ll_invition_code = v.findViewById(R.id.ll_invition_code);

        tv_invition_code = v.findViewById(R.id.tv_invition_code);

        tv_copy = v.findViewById(R.id.tv_copy);

        btn_msg = v.findViewById(R.id.btn_message);
        iv_tips = v.findViewById(R.id.iv_tips);

        tv_login = v.findViewById(R.id.tv_login);

        loginDialog = new CustomLoginDialog(getActivity(),(BaseActivity) getActivity());

        //中间按钮
        ll_friend = v.findViewById(R.id.ll_friend);
        ll_coins = v.findViewById(R.id.ll_coins);
        ll_videos = v.findViewById(R.id.ll_videos);

        //分享
        iv_faceShareLink = v.findViewById(R.id.iv_faceShareLink);
        iv_ttShareLink = v.findViewById(R.id.iv_ttShareLink);
        iv_InsShareLink = v.findViewById(R.id.iv_InsShareLink);
        iv_WhatsShareLink = v.findViewById(R.id.iv_WhatsShareLink);
        fAdView = v.findViewById(R.id.f_ad_view);
        close_ad = v.findViewById(R.id.close_ad);
        mAdView = v.findViewById(R.id.video_banner_adView);

        tv_sign = v.findViewById(R.id.tv_sign);
        tv_location = v.findViewById(R.id.tv_location);
        tv_age = v.findViewById(R.id.tv_age);


        myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);


        mOtherActivityDialog = new OtherActivityDialog(getActivity());
        window = mOtherActivityDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(window.getAttributes());

        mOtherActivityDialog.setOnOpenBagListener(new OtherActivityDialog.OnOpenBagListener() {
            @Override
            public void onOpen(int pageType) {
                //跳转网页
                mPersonalJs.openNewWebPage(otherActivityUrl);
                isShow = false;
                timer.cancel();
            }
        });

        //初始化动画
        initAnimation();

        //初始化google广告
        initGoogleBannerAd();
        /**
         * 初始化airpush广告
         */
        initAirPushBannerAd();

    }

    public void initAnimation() {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f);
        mHiddenAction.setDuration(500);
    }

    public void initGoogleBannerAd() {
        if (mAdView != null) {
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (fAdView != null && mAdView != null) {
                        if (fAdView.getVisibility() == View.GONE) {
                            fAdView.startAnimation(mShowAction);
                            fAdView.setVisibility(View.VISIBLE);
                            MobclickAgent.onEvent(getContext(), Common.AD_TYPE_GOOGLE_ME_LOOK);
                            MobclickAgent.onEvent(getContext(), Common.AD_TYPE_GOOGLE_ME_LOOK, "google_me_look");
                        }
                    }
                }

                @Override
                public void onAdOpened() {
                    MobclickAgent.onEvent(getContext(), Common.AD_TYPE_GOOGLE_ME_CLICK);
                    MobclickAgent.onEvent(getContext(), Common.AD_TYPE_GOOGLE_ME_CLICK, "google_me");
                    super.onAdOpened();
                }
            });
        }
    }

    public void initAirPushBannerAd() {
        if (airpushAdUtils == null) {
            airpushAdUtils = new AirpushAdUtils(getActivity());
        }
        //airpush监听
        airpushAdUtils.setmAirpushAdLoadLisenter(new AirpushAdUtils.airpushAdLoadLisenter() {
            @Override
            public void onLoad() {
                if (fAdView != null && fAdView.getVisibility() == View.GONE) {
                    fAdView.startAnimation(mShowAction);
                    fAdView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onClick() {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //循环加载广告
    private void adLoad() {
        int adType = UserSpCache.getInstance(getActivity()).getInt(UserSpCache.ME_PAGE_AD_TYPE);
        adType = 2;
        switch (adType) {
            case 1://原来是google
                UserSpCache.getInstance(getActivity()).putInt(UserSpCache.ME_PAGE_AD_TYPE, 2);
                //airpush显示广告
                mAdView.setVisibility(View.GONE);
                break;
            case 2://原来是airpush
                UserSpCache.getInstance(getActivity()).putInt(UserSpCache.ME_PAGE_AD_TYPE, 1);
                //显示google广告
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                mAdView.loadAd(adRequest);
                mAdView.setVisibility(View.VISIBLE);
                break;
            default:
                UserSpCache.getInstance(getActivity()).putInt(UserSpCache.ME_PAGE_AD_TYPE, 1);
                //显示google广告
                adRequest = new AdRequest.Builder()
                        .build();
                mAdView.loadAd(adRequest);
                mAdView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void initEvents() {
        headImg.setOnClickListener(this);
        tvMelExchange.setOnClickListener(this);
        tvMeContact.setOnClickListener(this);
        tvAgree.setOnClickListener(this);
        tvMeSettings.setOnClickListener(this);
        btn_msg.setOnClickListener(this);
        tvMeInput.setOnClickListener(this);
        tvMeRank.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        tv_login.setOnClickListener(this);


        ll_friend.setOnClickListener(this);
        ll_coins.setOnClickListener(this);
        ll_videos.setOnClickListener(this);

        iv_faceShareLink.setOnClickListener(this);
        iv_ttShareLink.setOnClickListener(this);
        iv_InsShareLink.setOnClickListener(this);
        iv_WhatsShareLink.setOnClickListener(this);
        close_ad.setOnClickListener(this);
        swipRfLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getPersonMsg();

            }
        });

//        loginDialog.setThirdLogin(new CustomLoginDialog.ThirdLoginResult() {
//            @Override
//            public void onOtherfail() {
//                loginDialog.dismiss();
//            }
//
//            @Override
//            public void onCheckFail(BaseResponse response) {
//                loginDialog.dismiss();
//            }
//
//            @Override
//            public void onLoginSuccess(String json) {
//                loginDialog.dismiss();
//            }
//
//            @Override
//            public void onStartCheck() {
//
//            }
//        });

        loginDialog.setThirdLogin(new CustomLoginDialog.ThirdLoginResult() {
            @Override
            public void onOtherfail() {

            }

            @Override
            public void onCheckFail(BaseResponse response) {

            }

            @Override
            public void onLoginSuccess(String json, boolean isBind) {
                //getPersonMsg();
            }

            @Override
            public void onStartCheck() {

            }

            @Override
            public void onGetGoldSuccess(boolean isBind) {

            }

            @Override
            public void onGetGoldFail() {

            }

            @Override
            public void onLogin() {

            }
        });

    }

    public void getPersonMsg() {
        mPresenter.getPersonMsg();
    }

    @Override
    public void OnClickEvents(View v) {
        if (v == headImg) {
            if (!tvMane.getText().toString().trim().equals("")) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", tvMane.getText().toString().trim());
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                loginDialog.show();
            }
        } else if (v == tvMelExchange) {
            WebActivity.toThis(mContext, Api.MelExchange);
        } else if (v == tvMeContact) {
            WebActivity.toThis(mContext, Api.CONTACT);
        } else if (v == tvMeSettings) {//设置界面
            startActivity(SettingActivity.class);
        } else if (v == btn_msg) {
            startActivity(MessageActivity.class);
        } else if (v == tvAgree) {
//            if (isLogin) {
            startActivity(FeedbackActivity.class);
//            } else {
////                startActivity(LoginActivity.class);
////            }

        } else if (v == tvMeInput) {  //输入邀请码
            if (isLogin) {
                WebActivity.toThis(mContext, Api.ENTER);
            } else {
                //startActivity(LoginActivity.class);
                loginDialog.show();
            }
        } else if (v == tvMeRank) {  //收入排行榜
            WebActivity.toThis(mContext, Api.MelRankingList);
        } else if (v == tv_copy) {
            String text = tv_invition_code.getText().toString();
            ClipData myClip = ClipData.newPlainText("text", text);
            myClipboard.setPrimaryClip(myClip);
            ToastUtils.showShort(getContext(), R.string.copy_tip);
        } else if (v == tv_login) {
            loginDialog.show();
        } else if (v == ll_friend) { //邀请人数
            startActivity(EarnActivity.class);

        }else if(v == ll_coins){ //金币
            WebActivity.toThis(mContext, Api.MelExchange);
        } else if (v == ll_videos) { //视频列表
            startActivity(MyVideoActivity.class);
        }else if (v == close_ad){
            fAdView.startAnimation(mHiddenAction);
            fAdView.setVisibility(View.GONE);

        } else if (v == iv_faceShareLink) {
            mPresenter.getLinkShareUrl(Common.SHARE_TYPE_FACEBOOK);
        } else if (v == iv_ttShareLink) {
            mPresenter.getLinkShareUrl(Common.SHARE_TYPE_TWITTER);
        } else if (v == iv_InsShareLink) {
            //不行
            mPresenter.getLinkShareUrl(Common.SHARE_TYPE_INS);
        } else if (v == iv_WhatsShareLink) {
            mPresenter.getLinkShareUrl(Common.SHARE_TYPE_WHATS);
        }
    }

    @Override
    public void showLoading() {
        showBaseLoading("");
    }

    @Override
    public void hideLoading() {
        hideBaseLoading();
    }

    @Override
    public void showErrorTip(int code, String msg) {
        hideLoading();
        ToastUtils.showShort(mContext, msg);
        if (isLogin) {
            ll_un_login.setVisibility(View.GONE);
            ll_invition_code.setVisibility(View.VISIBLE);
            cl_login.setVisibility(View.VISIBLE);
        } else {
            ll_un_login.setVisibility(View.VISIBLE);
            ll_invition_code.setVisibility(View.GONE);
            cl_login.setVisibility(View.GONE);
        }

        if (swipRfLayout != null) {
            swipRfLayout.finishRefresh();
        }
    }

    @Override
    public void getPersonMsgSuccess(UserInfoResponse userInfoResponse) {
        tvMane.setText(userInfoResponse.getData().getUserMsg().getNickname());
        if (swipRfLayout != null) {
            if (swipRfLayout.isRefreshing()) swipRfLayout.finishRefresh();
        }
        if (userInfoResponse != null) {
            isLogin = mUserSpCache.getBoolean(Constant.KEY_IS_USER_LOGIN);
            UserInfoResponse.UserInfo.UserMsg userMsg = userInfoResponse.getData().getUserMsg();
            if (isLogin) {
                // tvInvaTationCode.setText(getString(R.string.earn_my_in_code, userMsg.getInvitation_code()));
                ll_un_login.setVisibility(View.GONE);
                ll_invition_code.setVisibility(View.VISIBLE);
                cl_login.setVisibility(View.VISIBLE);
                tv_invition_code.setText(userMsg.getInvitation_code());

                tv_sign.setText(userMsg.getSignature());
                //tv_location.setText(userMsg.getc);
                //tv_age.setText(userMsg.get);
            } else {
                ll_un_login.setVisibility(View.VISIBLE);
                ll_invition_code.setVisibility(View.GONE);
                cl_login.setVisibility(View.GONE);
            }

            if (getContext() != null) {
                headImg.setImageURI(userMsg.getHeadimg());
            }
            tvMeCoins.setText(userMsg.getGold_flag() + ""); //金币
            tvMeTotalIncome.setText(userMsg.getFriend_num() + ""); //徒弟
            tvMeVideo.setText(userMsg.getVideo_num() + "");  //视频数量
            if (TextUtils.isEmpty(userMsg.getBirthday())) {
                tv_age.setText(getString(R.string.me_age, 0));
            } else {
                tv_age.setText(getString(R.string.me_age, getAge(userMsg.getBirthday())));
            }

            String country = ACache.get(getActivity()).getAsString(Constant.CACHE_COUNTRY);
            if(TextUtils.isEmpty(country)){
                tv_location.setText(getString(R.string.unknow));
            }else{
                tv_location.setText(country);
            }

            if (userMsg.isShare_code_status()) {//填写邀请码 ture就隐藏
                tvMeInput.setVisibility(View.GONE);
            } else {
                tvMeInput.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setTips() {
        int tips_service_count = UserSpCache.getInstance(mContext).getInt(UserSpCache.TIPS_FLAG);
        int tips_local_count = UserSpCache.getInstance(mContext).getInt(UserSpCache.TIPS_LOCAL_FLAG);

        if (tips_service_count > tips_local_count) {
            iv_tips.setVisibility(View.VISIBLE);
        } else {
            iv_tips.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshTips(String str) {
        if (str.equals(Common.REFRESH_TIPS)) {
            iv_tips.setVisibility(View.GONE);
        } else if (Common.REFRESH_USERINFO.equals(str)) {
           // swipRfLayout.autoRefresh();
            getPersonMsg();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        timer.cancel();
    }

    @Override
    public void toShare(int type, JsShareType jsShareType) {
        if (type == Common.SHARE_TYPE_FACEBOOK) {
            mFaceBookShare = new FaceBookShare(getActivity(), new FacebookCallback() {
                @Override
                public void onSuccess(Object o) {
                    LogUtil.showLog("msg---分享成功:");
                    ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));

                }

                @Override
                public void onCancel() {
                    LogUtil.showLog("msg---取消分享:");
                }

                @Override
                public void onError(FacebookException error) {
                    ToastUtils.showShort(mContext, getString(R.string.sharedFialed));


                }
            });
            mFaceBookShare.share(jsShareType);
        } else if (type == Common.SHARE_TYPE_TWITTER) {
            if (mTwitterLogin == null) mTwitterLogin = new TwitterLogin();
            mTwitterLogin.setTwitterShareLisenter(new TwitterLogin.TwitterShareLisenter() {
                @Override
                public void getShareOk(String response) {
                    LogUtil.showLog("msg---分享成功:");
                    ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                }

                @Override
                public void getShareFail(String response) {
                    LogUtil.showLog("msg---分享失败:");
                }

                @Override
                public void getShareCancel(String response) {
                    LogUtil.showLog("msg---分享取消:");
                }
            });
            mTwitterLogin.twitterShare(getActivity(), jsShareType, Common.TWITTER_SHARE_IAMGE);
        } else if (type == Common.SHARE_TYPE_INS) {
            ToastUtils.showShort(mContext, "暂未开放");
        } else if (type == Common.SHARE_TYPE_WHATS) {
            whatsAppShare.shareLink(jsShareType.getUrl());
        }

    }

    private FaceBookShare mFaceBookShare;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginDialog.registerCallBack(requestCode,resultCode,data,true);

        if (mFaceBookShare != null) {
            mFaceBookShare.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }

        //linkedin分享
//        LISessionManager.getInstance(getActivity()).onActivityResult(getActivity(), requestCode, resultCode, data);

//        if (requestCode == 10) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            googleLogin.handleSignInResult(result);
//        }
    }

    private int getAge(String birth) {
        int age = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthDay = df.parse(birth);
            age = getAge(birthDay);
        } catch (Exception e) {
            e.printStackTrace();
            return  0;
        }
        return age;
    }

    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;
    }

}