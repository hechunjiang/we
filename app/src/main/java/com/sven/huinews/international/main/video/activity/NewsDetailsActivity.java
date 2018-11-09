package com.sven.huinews.international.main.video.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.main.video.contract.NewsDetailsContract;
import com.sven.huinews.international.main.video.model.NewsDetailsModel;
import com.sven.huinews.international.main.video.presenter.NewsDetailsPresenter;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.GifView;
import com.sven.huinews.international.view.MyVideoProgress;

import java.util.Timer;
import java.util.TimerTask;

public class NewsDetailsActivity extends BaseActivity<NewsDetailsPresenter, NewsDetailsModel> implements NewsDetailsContract.View, View.OnClickListener {
    private NewsInfo mNewsInfo;
    private WebView webView;
    private ScrollView scrollView;
    private MyVideoProgress mProgress;
    private RelativeLayout mProgressLayout, fWebLoading;
    private TextView tvGold, tvFrequency;
    private SwipeRefreshLayout webSr;
    private GifView gifWebLoading;
    private AdView mAdView;
    private FrameLayout fAdView;
    private LinearLayout llRewardTips;
    private ImageButton close_ad;

    private Timer timer = new Timer();
    private TimerTask task;

    private Boolean isNewsDrow = false;//网页是否加载完成
    private long lookTime = 60000;//1000 = 1秒  加金币需停留的时间
    private Boolean isFinish = false;//当前activity是否结束
    private Boolean isStratProgress = false;//是否加载金币  判断是否领取过金币

    private TextView action_bar_close_tv;
    private ImageView action_bar_back_iv;

    //广告
    private TranslateAnimation mShowAction;//显示动画
    private TranslateAnimation mHiddenAction;//隐藏动画

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                //判断新闻是否可以拿金币 是否已经拿到金币 是否是第一次滑动 是否正在播放
                if (mNewsInfo.getIs_gold() == 1 && isStratProgress && !mProgress.getAnimatorStatus()) {
                    mProgress.animStart(lookTime);
                }
                isStratProgress = false;
            } else if (msg.what == 1) {
                if (fWebLoading != null) {
                    isNewsDrow = true;
                    fWebLoading.setVisibility(View.GONE);
                    //隐藏loading的时候加载广告
                    AdRequest adRequest = new AdRequest.Builder()
                            .setGender(1)
                            .build();
                    mAdView.loadAd(adRequest);

                }
            }
        }
    };


    public static void toThis(Context mContext, NewsInfo data) {
        Intent intent = new Intent(mContext, NewsDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.NEWSINFO, data);
        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_details;
    }

    @Override
    public void initView() {
        setMVP();
        mNewsInfo = (NewsInfo) getIntent().getSerializableExtra(Constant.NEWSINFO);
        webView = findViewById(R.id.wv_news_details);
        scrollView = findViewById(R.id.sv_news_details);
        mProgress = findViewById(R.id.video_progress);
        mProgressLayout = findViewById(R.id.video_progress_layout);
        tvGold = findViewById(R.id.tv_gold);
        tvFrequency = findViewById(R.id.tv_frequency);
        webSr = findViewById(R.id.web_sr);
        gifWebLoading = findViewById(R.id.gif_web_loading);
        fWebLoading = findViewById(R.id.f_web_loading);
        mAdView = findViewById(R.id.video_banner_adView);
        fAdView = findViewById(R.id.f_ad_view);
        llRewardTips = findViewById(R.id.ll_reward_tips);
        webSr.setColorSchemeColors(getResources().getColor(R.color.tab_tv_color_cli));
        action_bar_close_tv = findViewById(R.id.action_bar_close_tv);
        action_bar_back_iv = findViewById(R.id.action_bar_back_iv);

        //判断是否为可以拿到金币的data  数据库获取是否为已经获取到金币的data
        if (mNewsInfo.getIs_gold() == 1 && mPresenter.isCanGetCoinByReadNews(mNewsInfo)) {
            isStratProgress = true;
            if (mNewsInfo.getNews_stop_second() > 0) {//如果能拿到观看时间则设置
                lookTime = mNewsInfo.getNews_stop_second();
            }
        } else {
            isStratProgress = false;
        }
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //判断是否已经获取过金币了  判断网页是否加载完成了
                if (isStratProgress && isNewsDrow) mHandler.sendEmptyMessage(0);
                return false;
            }
        });

        webSr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

    }

    @Override
    public void initEvents() {
        action_bar_close_tv.setOnClickListener(this);
        action_bar_back_iv.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == action_bar_close_tv || v == action_bar_back_iv) {
            finish();
        } else if (v == close_ad) {
            fAdView.startAnimation(mHiddenAction);
            fAdView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initObject() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(mNewsInfo.getHref());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载完成后显示是否获取到金币
                if (isStratProgress) mProgressLayout.setVisibility(View.VISIBLE);
                if (webSr != null) webSr.setRefreshing(false);
                if (task == null) {
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(1);
                        }
                    };
                    timer.schedule(task, 2000);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        mProgress.OnVideoProgressLisenter(new MyVideoProgress.OnVideoProgressLisenter() {
            @Override
            public void end() {
                if (!isFinish) {
                    mProgressLayout.setVisibility(View.INVISIBLE);
                    mPresenter.readAnyNewsGetGold(TaskRequest.TASK_ID_READ_AD, mNewsInfo.getId() + "");
                }
            }
        });

        //初始化动画
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f);
        mHiddenAction.setDuration(500);

        mPresenter.newsStatistics(mNewsInfo.getId() + "");
    }


    @Override
    public void showGoldCome(int count, int type, String masgess) {
        //数据库添加该data已经领取了金币
        mPresenter.addReadNews(mNewsInfo);
        //显示获得金币的ui
        tvGold.setText("+" + count + getString(R.string.me_coins));
        tvFrequency.setText(masgess);
        llRewardTips.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                llRewardTips.setVisibility(View.INVISIBLE);
            }
        }, 2000);
        LogUtil.showLog("msg---领取金币:新效果");
        //保存金币打开次数
        mPresenter.saveGoldOpenCount();
    }

    @Override
    public void showNetWorkError() {

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) mAdView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFinish = true;
        if (mAdView != null) mAdView.destroy();
    }

}
