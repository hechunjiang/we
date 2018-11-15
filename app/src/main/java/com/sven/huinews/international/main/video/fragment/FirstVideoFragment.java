package com.sven.huinews.international.main.video.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.method.Touch;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.duapps.ad.AdError;
import com.duapps.ad.entity.strategy.NativeAd;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.dialog.FinishTheTaskDialog;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.entity.Progress;
import com.sven.huinews.international.entity.event.VideoLikeEvent;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.NewsListRequst;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.VideoLikeRequest;
import com.sven.huinews.international.entity.requst.VideoListRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoStayRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.HomeTab;
import com.sven.huinews.international.entity.response.PushTaskResponse;
import com.sven.huinews.international.main.earn.EarnActivity;
import com.sven.huinews.international.main.follow.activity.FollowVideoPlay1Activity;
import com.sven.huinews.international.main.follow.activity.FollwVideoPlayActivity;
import com.sven.huinews.international.main.home.adapter.HomeHVideoAdapter;
import com.sven.huinews.international.main.home.adapter.NewsAdapter;
import com.sven.huinews.international.main.home.contract.HomeVideoContract;
import com.sven.huinews.international.main.home.dialog.OtherActivityDialog;
import com.sven.huinews.international.main.home.model.HomeVideoModel;
import com.sven.huinews.international.main.home.presenter.HomeVideoPresenter;
import com.sven.huinews.international.main.userdetail.activity.UserDetailedDataActivity;
import com.sven.huinews.international.main.video.activity.NewsDetailsActivity;
import com.sven.huinews.international.main.web.PersonalJs;
import com.sven.huinews.international.publicclass.ReportPresenter;
import com.sven.huinews.international.publicclass.VideoAndNewsPresenter;
import com.sven.huinews.international.test.SelectableTextHelper;
import com.sven.huinews.international.tplatform.facebook.FaceBookShare;
import com.sven.huinews.international.tplatform.linkedin.LinkedInPlatform;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.tplatform.whatsapp.WhatsAppShare;
import com.sven.huinews.international.utils.BaiDuAdUtils;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.GoogleInterstitialAdsUtils;
import com.sven.huinews.international.utils.GoogleNativeAdsUtils;
import com.sven.huinews.international.utils.ImageUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.NativeAdFetcher;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.VungleAdUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.utils.itemDecoration.SmallVideoGridItem;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.MyRefreshLayout;
import com.sven.huinews.international.view.MyVideoProgress;
import com.sven.huinews.international.view.dialog.CustomShareDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import wedemo.MessageEvent;
import wedemo.utils.Constants;

import static com.facebook.FacebookSdk.getApplicationContext;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class FirstVideoFragment extends BaseFragment<HomeVideoPresenter, HomeVideoModel> implements HomeVideoContract.View {
    private MyRefreshLayout refresh_view;
    private RecyclerView rv;
    private HomeHVideoAdapter mHVideoAdapter; //视频适配器
    private NewsAdapter mNewsAdapter; //新闻适配器
    private HomeTab mHomeTab;
    private SmallVideoGridItem mSmallVideoGridItem;
    private CustomShareDialog mCustomShareDialog;
    private RelativeLayout video_progress_layout;
    private MyVideoProgress mVideoProgress;
    private Boolean isLookGoldProgress = false;//金币进度条是否可见
    private ReportPresenter mReportPresenter;//举报网络访问
    private VideoAndNewsPresenter mVideoAndNewsPresenter;//视频播放统计
    //播放统计
    private TextView tvGold;
    private TextView tv_frequency;
    private LinearLayout ll_reward_tips;
    private Handler mHandler = new Handler();
    private String Video_id = "";
    private long lasttime;

    private HashMap<Integer, MyNews> myAdViewsIndex;//记录当前广告位置和类型  video
    private HashMap<Integer, NewsInfo> myAdViewsIndexNews;//记录当前广告位置和类型  news
    private List<MyNews> myNewsList;
    private int UDAdSize = 0;

    private int counter;
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
    PushTaskResponse.DataBean.PageListBean pageListBean;
    //分享
    FaceBookShare mFaceBookShare;
    TwitterLogin mTwitterLogin;
    String otherActivityUrl = "";
    private long delayMillis = 2000;//首页活动推送弹框
    private OtherActivityDialog mOtherActivityDialog;
    private Timer timer1, timer2;

    boolean isShow = true;
    private PersonalJs mPersonalJs;
    private TimerTask timerTask2;
    private Boolean isFinsh = false;//当前页面是否结束
    private MyNews addGoldDB;
    private int adtype = 1;//加载广告类型 列表  1 视频 2新闻
    //yahoo广告
    private NativeAdFetcher nativeAdFetcher;//yahoo广告工厂类
    private int yahooAdSize = 0;//yahoo 请求广告数
    //百度广告
    private BaiDuAdUtils mBaiDuAdUtils;
    private String saveVideoId = "";
    private Boolean isLogin = false;
    private int mCurrentProgress = -1; //停止时 的进度
    private long mCurrentTime = -1;
    private boolean mPlayback = false;
    private String mCurrentPlaybackVideo = "";
    private MyNews myNewsShare;
    private boolean isPause = false;
    private GoogleInterstitialAdsUtils mGoogleInterstitialAdsUtils;//Google插页广告

    private GoogleNativeAdsUtils mGoogleNativeAdsUtils;//google原生
    private int googleAdSize = 0;//google原生广告数

    private WhatsAppShare whatsAppShare;

    private boolean isStart = false;
    private long satrtTime = 0;
    private long endTime = 0;

    VungleAdUtils mVungleAd;//vungle激励视频广告

    private int mCurrentPlaybackNumber = 0;//当前播放视频数量

    private boolean videoIsOnePlay = true;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnComentViewLisenter) {
            onComentViewLisenter = (OnComentViewLisenter) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public static FirstVideoFragment getInstance(HomeTab mHomeTab) {
        FirstVideoFragment fragment = new FirstVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.HOME_LAYOUT_TYPE, mHomeTab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        mHomeTab = (HomeTab) getArguments().getSerializable(Common.HOME_LAYOUT_TYPE);
        EventBus.getDefault().register(this);
        return R.layout.fragment_first_video;
    }

    @Override
    public void initObject() {
        setMVP();
        initRecycler();
        clearOtherFragmentDatas();//清空页面数据
        initShare();
        if (mReportPresenter == null) {
            mReportPresenter = new ReportPresenter(getActivity());
        }

        if (mVideoAndNewsPresenter == null) {
            mVideoAndNewsPresenter = new VideoAndNewsPresenter(getActivity());
        }

        //创建yahoo工厂类
        if (nativeAdFetcher == null) {
            nativeAdFetcher = new NativeAdFetcher(getContext());
            nativeAdFetcher.addListener(new NativeAdFetcher.AdNativeListener() {
                @Override
                public void onAdCountChanged(List<View> yhAdViews) {
                    Log.d("广告", "yahoo收到广告: " + yhAdViews.size());
                    if (isFinsh) return;
                    if (adtype == 1) {
                        mHVideoAdapter.addYHAdView(yhAdViews);
                    } else {
                        mNewsAdapter.addYHAdView(yhAdViews);
                    }
                }

                @Override
                public void onLoad() {

                }
            });
        }

        //创建百度海外版工具类
        //创建我的广告工具类
        if (mBaiDuAdUtils == null) {
            mBaiDuAdUtils = new BaiDuAdUtils(getActivity(), 0);
            mBaiDuAdUtils.setUdAdLisenter(
                    new BaiDuAdUtils.UdAdLisenter() {
                        @Override
                        public void onError(AdError error) {

                        }

                        @Override
                        public void onAdLoaded(List<View> adViews) {
                            if (isFinsh) return;
                            if (adtype == 1) {
                                mHVideoAdapter.addUDAdView(adViews);
                            } else {
                                mNewsAdapter.addUDAdView(adViews);
                            }
                            MobclickAgent.onEvent(getActivity(), Common.AD_TYPE_NES_LIST_DU_LOOK);
                            MobclickAgent.onEvent(getActivity(), Common.AD_TYPE_NES_LIST_DU_LOOK, "ad_type_new_list_du_look");
                        }

                        @Override
                        public void onAdLoadedListData(LinkedList<NativeAd> listData) {
                            LogUtil.showLog("listData:" + listData.size());
                        }
                    }
            );
        }

        //google插页广告
        if (mGoogleInterstitialAdsUtils == null) {
            mGoogleInterstitialAdsUtils = new GoogleInterstitialAdsUtils(getActivity());
        }

        //google原生广告
        if (mGoogleNativeAdsUtils == null) {
            mGoogleNativeAdsUtils = new GoogleNativeAdsUtils(getActivity());
            mGoogleNativeAdsUtils.setGoogleAdLisenter(new GoogleNativeAdsUtils.GoogleAdLisenter() {
                @Override
                public void onError(int errorCode) {
                    //错误代码
                    LogUtil.showLog("mGoogleNativeAdsUtils:" + errorCode);
                }

                @Override
                public void onAdLoaded(List<View> listview, List<UnifiedNativeAd> listADData) {
                    LogUtil.showLog("mGoogleNativeAdsUtils:" + listview.size());
                    mHVideoAdapter.addGoogleAdView(listview);
                }
            });
        }




    }


    @Override
    protected void loadData() {

        emptyLayout.setErrorType(EmptyLayout.LOADING, -1);
        getVideoList(true);
        mPersonalJs = new PersonalJs(getActivity());
        isLogin = mUserSpCache.getBoolean(Constant.KEY_IS_USER_LOGIN);
        String s = mHomeTab.getTabName();
        if (isLogin && mHomeTab.getTabType() == 2) {
            pageListBean = mUserSpCache.getPageListBean();
            startTimer();
        }
        judgeRequest1();
        if (mHomeTab.getTabType() == Common.V_VIDEO) {
            MobclickAgent.onEvent(mContext, Common.PAGE_VLOG_CLICK);
            MobclickAgent.onEvent(mContext, Common.PAGE_VLOG_CLICK, "page_vlog_click");
        } else if (mHomeTab.getTabType() == Common.H_VIDEO) {
            MobclickAgent.onEvent(mContext, Common.PAGE_VIDEOS_CLICK);
            MobclickAgent.onEvent(mContext, Common.PAGE_VIDEOS_CLICK, "page_videos_click");
        } else if (mHomeTab.getTabType() == Common.H_NEWS) {
            MobclickAgent.onEvent(mContext, Common.PAGE_NEWS_CLICK);
            MobclickAgent.onEvent(mContext, Common.PAGE_NEWS_CLICK, "page_news_click");
        }



    }

    private void judgeRequest1() {
        if (!"".equals(mUserSpCache.getStringData(UserSpCache.REQUEST_CODE))) {
            lasttime = mUserSpCache.getLong(UserSpCache.REQUEST_LONG);
            if (lasttime <= 0) {
                UserSpCache.getInstance(mContext).putString(UserSpCache.REQUEST_CODE, "");
                mPresenter.getThirtyGold(TaskRequest.TASK_ID_READ_TIME_LONG + "", "");
            }
        } else {
            mPresenter.getThirtyGold(TaskRequest.TASK_ID_READ_TIME_LONG + "", "");
        }
    }

    //弹窗timmer
    private void startTimer() {
        if (timer1 == null) {
            timer1 = new Timer();
        }
        timer1.schedule(new TimerTask() {
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
            if (msg.what == 0x123) {//活动弹窗
                if (pageListBean != null) {
                    if (counter == pageListBean.getHome_first().getTime()
                            && !mOtherActivityDialog.isShowing() && !((Activity) mContext).isFinishing()) {
                        if (pageListBean.getHome_first().isIsShow()) {
                            otherActivityUrl = pageListBean.getHome_first().getRedirect();
                            ImageUtils.loadImgGetResult(getActivity(), pageListBean.getHome_first().getPicUrl(), new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(final Drawable resource, Transition<? super Drawable> transition) {
                                    //延迟弹出
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mOtherActivityDialog.showDialog(resource);
                                        }
                                    }, delayMillis);
                                }
                            });
                        }
                    } else if (counter == pageListBean.getHome_second().getTime()
                            && !mOtherActivityDialog.isShowing() && !((Activity) mContext).isFinishing()) {
                        if (pageListBean.getHome_second().isIsShow()) {
                            otherActivityUrl = pageListBean.getHome_first().getRedirect();
                            ImageUtils.loadImgGetResult(getActivity(), pageListBean.getHome_first().getPicUrl(), new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(final Drawable resource, Transition<? super Drawable> transition) {
                                    //延迟弹出
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mOtherActivityDialog.showDialog(resource);
                                        }
                                    }, delayMillis);
                                }
                            });
                        }
                    }
                }
            } else if (msg.arg1 == 0x222) {
                if (lasttime > 0) {
                    lasttime = lasttime - 1;
                    LogUtil.showLog(lasttime + "");
                } else {
                    if (!"".equals(mUserSpCache.getStringData(UserSpCache.REQUEST_CODE))) {
                        mPresenter.getThirtyGold(TaskRequest.TASK_ID_READ_TIME_LONG + "", mUserSpCache.getStringData(UserSpCache.REQUEST_CODE));
                        ToastUtils.showShort(mContext, "我就看看加金币没有");
                    }
                }

            }
            return false;
        }

    });

    public void initShare() {
        //初始化分享
        if (mCustomShareDialog == null) {
            mCustomShareDialog = new CustomShareDialog(getActivity());
            mCustomShareDialog.setOnShareListener(new CustomShareDialog.OnShareListener() {
                @Override
                public void onShare(int type) {
                    if (type != Common.SHARE_TYPE_REPORT) {
                        //获取分享数据
                        mPresenter.getVideoShareUrl(getVideSharedRequest(type), type);
                    } else {
                        //举报  网络链接
                        mReportPresenter.videoReport(Video_id);
                    }
                }
            });
        }
    }


    @Override
    protected void initView(View v) {
        emptyLayout = v.findViewById(R.id.mEmptyLayout);
        refresh_view = v.findViewById(R.id.refresh_view);
        tvGold = v.findViewById(R.id.tv_gold);
        tv_frequency = v.findViewById(R.id.tv_frequency);
        ll_reward_tips = v.findViewById(R.id.ll_reward_tips);
        rv = v.findViewById(R.id.rv_videos_list);
        mVideoProgress = v.findViewById(R.id.video_progress);
        video_progress_layout = v.findViewById(R.id.video_progress_layout);
        mOtherActivityDialog = new OtherActivityDialog(getActivity());
        Window window = mOtherActivityDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);

        whatsAppShare = new WhatsAppShare(mContext);

        mOtherActivityDialog.setOnOpenBagListener(new OtherActivityDialog.OnOpenBagListener() {
            @Override
            public void onOpen(int pageType) {
                if (pageType != -1) {
                    startActivity(EarnActivity.class);
                } else {
                    //跳转网页
                    if (isShow) {
                        if (otherActivityUrl.equals("shoutu") && !otherActivityUrl.contains("http") && !otherActivityUrl.contains("https")) {
                            startActivity(EarnActivity.class);
                        } else {
                            mPersonalJs.openNewWebPage(otherActivityUrl);
                        }
                    }
                }
            }
        });

        emptyLayout.setOnEmptyRefreshLisenter(new EmptyLayout.onEmptyRefreshLisenter() {
            @Override
            public void onEmptyRefresh() {
                getVideoList(true);
            }
        });

        tv_test = v.findViewById(R.id.tv_test);
        tv_test.setTextIsSelectable(true);
        tv_test.setMovementMethod(ScrollingMovementMethod.getInstance());

        SelectableTextHelper mSelectableTextHelper = new SelectableTextHelper.Builder(tv_test)
                .setSelectedColor(getResources().getColor(R.color.tw__composer_red))
                .setCursorHandleSizeInDp(20)
                .setCursorHandleColor(getResources().getColor(R.color.banner_press_blue_color))
                .build();
    }

    private TextView tv_test;

    StringBuilder stringBuilder = new StringBuilder();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void testEvent(MessageEvent event) {

        if (Constants.UPLOAD_TEST) {
            if (event.getMessage().equals("test")) {
                String s = (String) event.getData();

                if (s.equals("finish")) {
                    stringBuilder = new StringBuilder();
                    tv_test.setText(stringBuilder);
                    tv_test.setVisibility(View.GONE);
                    return;
                }
                stringBuilder.append(s);
                tv_test.setText(stringBuilder);
                tv_test.setVisibility(View.VISIBLE);
            }
        } else {
            tv_test.setVisibility(View.GONE);
        }

    }


    @Override
    public void initEvents() {
        refresh_view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                VideoViewManager.instance().releaseVideoPlayer();
                isRefresh = true;
                PAGE = 1;
                getVideoList(true);
            }
        });
        refresh_view.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                VideoViewManager.instance().releaseVideoPlayer();
                isRefresh = false;
                PAGE += 1;
                getVideoList(false);
            }
        });

        video_progress_layout.setOnClickListener(this);
    }


    @Override
    public void OnClickEvents(View v) {
        if (v == video_progress_layout) {
            //Vungle激励视频
//            if (mVungleAd != null && mVungleAd.isTheCacheComplete()) {
//                mVungleAd.openNoLoadAd();
//            }
            if (mGoogleInterstitialAdsUtils.isLoad()) {
                mGoogleInterstitialAdsUtils.showAd(Common.AD_TYPE_GOOGLE_INTERSTITIAL_LOOK, Common.AD_TYPE_GOOGLE_INTERSTITIAL_CLICK);
            }
        }
    }

    private int videoType;

    private void initRecycler() {
        if (mHomeTab.getTabType() == Common.V_VIDEO) {
            setGridLayoutManager();
            videoType = MyNews.V_VIDEO;
            video_progress_layout.setVisibility(View.INVISIBLE);
            if (mHVideoAdapter == null) {
                mHVideoAdapter = new HomeHVideoAdapter(new ArrayList<MyNews>());
            }
            rv.setAdapter(mHVideoAdapter);
        } else if (mHomeTab.getTabType() == Common.H_VIDEO) {
            setLinearLayoutManager();
            videoType = MyNews.H_VIDEO;
            video_progress_layout.setVisibility(View.VISIBLE);
            if (mHVideoAdapter == null) {
                mHVideoAdapter = new HomeHVideoAdapter(new ArrayList<MyNews>());
            }
            rv.setAdapter(mHVideoAdapter);
        } else if (mHomeTab.getTabType() == Common.H_NEWS) {
            rv.setLayoutManager(new LinearLayoutManager(mContext));
            video_progress_layout.setVisibility(View.INVISIBLE);
            if (mNewsAdapter == null) {
                mNewsAdapter = new NewsAdapter(new ArrayList<NewsInfo>());
            }
            rv.setAdapter(mNewsAdapter);
        }

        rv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                IjkVideoView ijkVideoView = view.findViewById(R.id.video_player);
                if (ijkVideoView != null && !ijkVideoView.isFullScreen()) {
                    ijkVideoView.stopPlayback();
                }
            }
        });


    }


    private boolean isshowComment = false;

    private void setLinearLayoutManager() {
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, lastVisibleItem, visibleCount;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onComentViewLisenter != null && isshowComment) {
                    onComentViewLisenter.hideCommentView();
                    isshowComment = false;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    autoPlayVideo(recyclerView);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;//记录可视区域item个数
            }

            private void autoPlayVideo(RecyclerView view) {

                for (int i = 0; i < visibleCount + 1; i++) {
                    if (view == null && view.getChildAt(i) == null) {
                        continue;
                    }
                    IjkVideoView ijkVideoView = view.getChildAt(i).findViewById(R.id.video_player);
                    if (ijkVideoView != null) {
                        Rect rect = new Rect();
                        ijkVideoView.getLocalVisibleRect(rect);
                        int videoHeight = ijkVideoView.getHeight();
                        if (rect.top == 0 && rect.bottom == videoHeight) {
                            if (!ijkVideoView.isPlaying()) {
                                mCurrentTime = mVideoProgress.surplusTime();
                                mVideoProgress.stopProgress();
                                mCurrentProgress = mVideoProgress.getProgressCurrent();
                                ijkVideoView.start();
                                //  AnimationUtils.showAndHiddenAnimation(mMask, AnimationUtils.AnimationState.STATE_HIDDEN, 300);
                            } /*  else {
                                AnimationUtils.showAndHiddenAnimation(mMask, AnimationUtils.AnimationState.STATE_SHOW, 300);
                            }*/
                            return;
                        }
                    }
                }
            }
        });
    }

    private void setGridLayoutManager() {
        if (mSmallVideoGridItem == null) {
            mSmallVideoGridItem = new SmallVideoGridItem(mContext);
        }
        rv.removeItemDecoration(mSmallVideoGridItem);
        rv.addItemDecoration(mSmallVideoGridItem);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                staggeredGridLayoutManager.invalidateSpanAssignments();
            }
        });
    }

    private void getVideoList(boolean isRefresh) {
        if (mHomeTab.getTabType() == 3) {
            mPresenter.onNewsList(isRefresh);
        } else {
            mPresenter.onVideoList(isRefresh);
        }
    }

    @Override
    public VideoListRequest getVideoListRequest() {
        VideoListRequest request = new VideoListRequest();
        request.setR_type(mHomeTab.getTabType() + "");
        request.setPage(PAGE + "");
        return request;
    }

    @Override
    public NewsListRequst getNewsListRequst() {
        NewsListRequst requst = new NewsListRequst();
        requst.setPage(PAGE);
        requst.setR_type(1);
        return requst;
    }

    @Override
    public VideoShareUrlRequest getVideSharedRequest(int type) {
        VideoShareUrlRequest request = new VideoShareUrlRequest();
        request.setType(type + "");
        request.setVideoId(myNewsShare.getId());
        return request;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefeshHome(VideoLikeEvent event) {
        mHVideoAdapter.notifyOneChange(event.getmData());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGold(String s) {
        if (Common.FRAGMENT_CHANGE.equals(s)) {
            if (isLookGoldProgress) {
                mCurrentTime = mVideoProgress.surplusTime();
                mVideoProgress.stopProgress();
                mCurrentProgress = mVideoProgress.getProgressCurrent();
                video_progress_layout.setVisibility(View.INVISIBLE);//播放完成隐藏金币那个圈
                isLookGoldProgress = false;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void stopProgress(Progress process) {
        if (mVideoProgress != null) {
            mPlayback = false;
            mCurrentProgress = 0;
            mVideoProgress.setProgress(0);
        }
    }

    String user_id;
    String user_avter;

    @Override
    public void setVideoList(final List<MyNews> mDatas) {
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
        if (isRefresh) {
            if (mDatas.size() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NO_DATA, 10002);
                if (video_progress_layout.getVisibility() == View.VISIBLE) {
                    video_progress_layout.setVisibility(View.INVISIBLE);
                }
                if (mVideoProgress != null) {
                    mPlayback = false;
                    mCurrentProgress = 0;
                    mVideoProgress.setProgress(0);
                }

            }
            refresh_view.finishRefresh();
        } else {
            refresh_view.finishLoadmore();
        }
        if (myAdViewsIndex == null) {//记录当前广告位置和类型
            myAdViewsIndex = new HashMap<>();
        } else {
            myAdViewsIndex.clear();
        }
        //拿到广告的位置  把原来的视频移除 方便之后加入广告
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setType(videoType);
            if (mDatas.get(i).getIsAd() == 1) {
                mDatas.get(i).setType(MyNews.AD_VIDEO);
                if (mDatas.get(i).getAd_type().equals(Common.AD_TYPE_UD)) {
                    UDAdSize += 1;
                } else if (mDatas.get(i).getAd_type().equals(Common.AD_TYPE_YAHOO)) {
                    yahooAdSize += 1;
                } else if (mDatas.get(i).getAd_type().equals(Common.AD_TYPE_GOOGLE)) {
                    googleAdSize += 1;
                }
                myAdViewsIndex.put(i, mDatas.get(i));
                mDatas.remove(i);
                i--;
            }
        }
        if (isRefresh) {
            if (myNewsList != null) {
                myNewsList.clear();
            }
            myNewsList = mDatas;//记录新增的data
        } else {
            myNewsList.addAll(mDatas);//记录新增的data
        }
        mHVideoAdapter.addData(mDatas, isRefresh);
        mHVideoAdapter.setAdPostionAndAdType(myAdViewsIndex);//添加广告的位置和类型到adapter

        if (videoType == MyNews.H_VIDEO) {
            if (nativeAdFetcher != null) {//请求yahoo广告
                nativeAdFetcher.prefetchAds(yahooAdSize);
                yahooAdSize = 0;
                adtype = 1;
            }

            if (mGoogleNativeAdsUtils != null) {
                if (!mGoogleNativeAdsUtils.mAdisLoading()) {
                    mGoogleNativeAdsUtils.loadAds(googleAdSize);
                    googleAdSize = 0;
                    adtype = 1;
                }
            }

            if (mBaiDuAdUtils != null) {//请求百度
                mBaiDuAdUtils.getUDAds(UDAdSize);
                UDAdSize = 0;
                adtype = 1;
            }
        }
        mHVideoAdapter.setOnItemClickListener(new HomeHVideoAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(MyNews data, int position) {
                //跳转到视频详情，播放瀑布流
                //判断用户
                FollowVideoPlay1Activity.toThis(mContext, myNewsList, position, Common.STORIES_PAGE, 1, -1, "0", PAGE);
            }

            @Override
            public void onComment(MyNews news, TextView tv_msg) {
                //评论展示
                isshowComment = true;
                onComentViewLisenter.showCommentView(news, tv_msg);
            }

            @Override
            public void onLike(MyNews item, ImageView ivLike, TextView tvLike, int position, IjkVideoView videoPlayer) {
                item.setLike(!item.isLike());
                item.setLikeCount(item.isLike() ? item.getLikeCount() + 1 : item.getLikeCount() - 1);
                ivLike.setImageResource(item.isLike() ? R.mipmap.icon_item_liekd : R.mipmap.icon_item_like);
                tvLike.setTextColor(item.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.c999999));
                tvLike.setText(CommonUtils.getLikeCount(item.getLikeCount()));
                //视频点赞
                VideoLikeRequest request = new VideoLikeRequest();
                request.setDu_type(item.getDu_type());
                request.setVideo_id(item.getId());
                mPresenter.onVideoLike(request);
            }

            @Override
            public void onHead(MyNews news) {
                //点击头像跳转follow界面 2虚拟用户 1真实用户
                if (news.getDu_type().equals("2")) {
                    UserDetailedDataActivity.toThis(mContext, 2, news.getOtherId());
                } else {
                    UserDetailedDataActivity.toThis(mContext, 1, news.getOtherId());
                    LogUtil.showLog("true", "" + news.toString());
                    //ToastUtils.showShort(mContext, "Thanks Click Me");
                }
            }

            @Override
            public void onShare(MyNews myNews, int position) {
                if (myNews == null) {
                    return;
                }
                Video_id = myNews.getId();
                myNewsShare = myNews;
                mCustomShareDialog.show();
            }
        });

        //播放器监听  加金币逻辑
        mHVideoAdapter.setmOnVideoPlayStatusLisenter(new HomeHVideoAdapter.onVideoPlayStatusLisenter() {
            @Override
            public void onVideoStarted(IjkVideoView videoPlayer, final MyNews item) {
                mCurrentPlaybackVideo = item.getId();
                addGoldDB = item;
                //开始播放视频
                LogUtil.showLog("VideoPlay:" + "onVideoStarted");
                if (mUserSpCache.getInt(UserSpCache.GOLD_NUMBERS) >= 1) {//&& mPresenter.isCanGetCoinByReadNews(item)
                    if (video_progress_layout.getVisibility() == View.INVISIBLE) {
                        video_progress_layout.setVisibility(View.VISIBLE);
                    }
                    if (mCurrentProgress > -1) {
                        mVideoProgress.setProgress(mCurrentProgress);
                    } else {
                        mVideoProgress.setProgress(0);
                    }
                    if (mVideoProgress != null) {
                        initVideoProgress();
                        //监听 金币进度条
                        mVideoProgress.OnVideoProgressLisenter(new MyVideoProgress.OnVideoProgressLisenter() {
                            @Override
                            public void end() {
                                mPlayback = false;
                                mCurrentProgress = 0;
                                mVideoProgress.setProgress(0);
                                //进度条完成 去获取金币
                                if (!TextUtils.isEmpty(mCurrentPlaybackVideo)) {
                                    mPresenter.readAnyNewsGetGold(TaskRequest.TASK_ID_READ_NEWS, mCurrentPlaybackVideo);
                                }
                            }
                        });
                    } else {
                        if (mVideoProgress.getAnimatorStatus()) {
                            mCurrentTime = mVideoProgress.surplusTime();
                            mVideoProgress.stopProgress();
                            mCurrentProgress = mVideoProgress.getProgressCurrent();
                        }
                    }
                } else {
                    if (video_progress_layout.getVisibility() == View.VISIBLE) {
                        video_progress_layout.setVisibility(View.INVISIBLE);
                    }
                    mCurrentTime = mVideoProgress.surplusTime();
                    mVideoProgress.stopProgress();
                    mCurrentProgress = mVideoProgress.getProgressCurrent();
                }


                lasttime = mUserSpCache.getLong(UserSpCache.REQUEST_LONG);
                if (lasttime > 0) {
                    if (isPause) {//isPause是否暂停
                        destroyTimer();
                        initTimerTask();
                        timer2.schedule(timerTask2, 0, 1000);
                    } else {
                        destroyTimer();
                        initTimerTask();
                        timer2.schedule(timerTask2, 0, 1000);
                    }
                }


                //页面停留统计
                if (!isStart) {
                    satrtTime = System.currentTimeMillis();
                    isStart = true;
                }

                if (videoIsOnePlay && mPresenter.isCanGetCoinByReadNews(item)) {
                    //视频统计
                    mVideoAndNewsPresenter.videoStatistics(item.getId(), item.getType() + "", item.getDu_type());
                    mPresenter.addReadNews(item);
                    videoIsOnePlay = false;
                }
            }

            @Override
            public void onVideoPaused() {
                //视频暂停
                LogUtil.showLog("VideoPlay:" + "onVideoPaused");
                //暂停计时
                if (!isPause) {
                    isPause = true;
                    if (timer2 != null) {
                        timer2.cancel();
                    }
                }

                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                LogUtil.showLog("剩余时间" + lasttime);

                //暂停的时候调用
                mVideoProgress.pauseAnim();
                mCurrentProgress = mVideoProgress.getProgressCurrent();
                mCurrentTime = mVideoProgress.surplusTime();
            }

            @Override
            public void onComplete(MyNews item) {
                if (!isPause) {
                    isPause = true;
                    if (timer2 != null) {
                        timer2.cancel();
                    }
                }
                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);

                //播放完成 记录视频id
                saveVideoId = saveVideoId + item.getVideo_id();
                UserSpCache.getInstance(mContext).putString(UserSpCache.VIDEO_ID, saveVideoId);


                mVideoProgress.pauseAnim();
                mCurrentProgress = mVideoProgress.getProgressCurrent();
                mCurrentTime = mVideoProgress.surplusTime();

                mCurrentPlaybackNumber++;
                if (mCurrentPlaybackNumber >= 10) {
                    mCurrentPlaybackNumber = 0;
                    if (mGoogleInterstitialAdsUtils.isLoad()) {
                        mGoogleInterstitialAdsUtils.showAd(Common.AD_TYPE_GOOGLE_INTERSTITIAL_LOOK, Common.AD_TYPE_GOOGLE_INTERSTITIAL_CLICK);
                    }
                }

                //观看完成 视频统计
                mVideoAndNewsPresenter.videoLookTask();
                videoIsOnePlay = true;

            }

            @Override
            public void onPrepared(IjkVideoView videoPlayer, MyNews item) {
                //准备完成
//                LogUtil.showLog("VideoPlay:" + "onPrepared");
            }


            @Override
            public void onError() {
                //视频播放错误
//                LogUtil.showLog("VideoPlay:" + "onError");
                //暂停计时
                if (!isPause) {
                    isPause = true;
                    if (timer2 != null) {
                        timer2.cancel();
                    }
                }

                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                mVideoProgress.stopProgress();
                mCurrentProgress = 0;
                mCurrentTime = -1;
            }

            @Override
            public void onMove() {
                //移动进度
//                LogUtil.showLog("VideoPlay:" + "onMove");

            }

            @Override
            public void onInfo() {
                //准备完成
//                LogUtil.showLog("VideoPlay:" + "onInfo");
            }

            @Override
            public void onStop(MyNews mData) {
                //停止  控制器
//                LogUtil.showLog("VideoPlay:" + "onStop");
                if (!isPause) {
                    isPause = true;
                    if (timer2 != null) {
                        timer2.cancel();
                    }
                }
                if (mVideoProgress != null) {
                    mVideoProgress.pauseAnim();
                    mCurrentProgress = mVideoProgress.getProgressCurrent();
                    mCurrentTime = mVideoProgress.surplusTime();
                }

                //页面停留统计
                if (isStart) {
                    endTime = System.currentTimeMillis();
                    isStart = false;
                    mPresenter.videoStay(new VideoStayRequest((endTime - satrtTime) + "", mData.getVideo_id(), mData.getDu_type(), "2"));
                }

                videoIsOnePlay = true;
            }
        });
        if (videoType == MyNews.H_VIDEO) {
            if (isRefresh) {
                rv.post(new Runnable() {
                    @Override
                    public void run() {
                        mCurrentTime = mVideoProgress.surplusTime();
                        //自动播放第一个
                        View view = rv.getChildAt(0);
                        if (view != null) {
                            IjkVideoView ijkVideoView = view.findViewById(R.id.video_player);
                            ijkVideoView.start();
                        }
                    }
                });
            }
        }
    }

    public void initVideoProgress() {
        if (!mPlayback) {
            if (UserSpCache.getInstance(getActivity()).getInt(UserSpCache.GOLD_TIME) != -1) {
                mVideoProgress.animStart(UserSpCache.getInstance(getActivity()).getInt(UserSpCache.GOLD_TIME) * 1000);
//                mVideoProgress.animStart(10 * 1000);
            } else {
                mVideoProgress.animStart(30 * 1000);
            }
        } else {
            if (mCurrentTime != -1) {
                if (mCurrentTime > 0) {
                    mVideoProgress.animStart(mCurrentTime);
                } else {
                    mVideoProgress.animStart(30 * 1000);
                }
            } else {
                mVideoProgress.animStart(30 * 1000);
            }
        }
        mVideoProgress.resumeAnim();
        mPlayback = true;
    }

    public interface OnComentViewLisenter {
        void showCommentView(MyNews mData, TextView tvmsg);

        void hideCommentView();
    }


    OnComentViewLisenter onComentViewLisenter;

    @Override
    public void setNewsList(List<NewsInfo> mDatas, boolean isRefresh) {
        if (isFinsh) return;
        if (refresh_view != null) {
            refresh_view.finishLoadmore();
            refresh_view.finishRefresh();
        }
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
        if (myAdViewsIndexNews == null) {//记录当前广告位置和类型
            myAdViewsIndexNews = new HashMap<>();
        } else {
            myAdViewsIndexNews.clear();
        }

        Random r = new Random();
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            int look = r.nextInt(500000);
            mDatas.get(i).setOpen_browser(look);
            if (mDatas.get(i).getDisplay_type() > 2 || mDatas.get(i).getDisplay_type() < 0) {//如果没有类型就小图
                mDatas.get(i).setType(NewsInfo.NEWS_SMELL);
            } else {
                mDatas.get(i).setType(mDatas.get(i).getDisplay_type() - 1);//设置布局类型
            }
            if (mDatas.get(i).getIs_ad() == 1) {//如果是广告给广告布局
                mDatas.get(i).setType(NewsInfo.AD_ONE);
                if (mDatas.get(i).getAd_type().equals(Common.AD_TYPE_UD)) {
                    UDAdSize += 1;
                } else if (mDatas.get(i).getAd_type().equals(Common.AD_TYPE_YAHOO)) {
                    yahooAdSize += 1;
                }
                myAdViewsIndexNews.put(i, mDatas.get(i));
                mDatas.remove(i);
            }
        }

        mNewsAdapter.addData(mDatas, isRefresh);
        mNewsAdapter.setAdPostionAndAdType(myAdViewsIndexNews);//添加广告的位置和类型到adapter

        if (nativeAdFetcher != null) {//请求yahoo广告
            nativeAdFetcher.prefetchAds(yahooAdSize);
            yahooAdSize = 0;
            adtype = 2;
        }

        if (mBaiDuAdUtils != null) {//请求百度
            mBaiDuAdUtils.getUDAds(UDAdSize);
            UDAdSize = 0;
            adtype = 2;
        }

        mNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsInfo data = (NewsInfo) adapter.getItem(position);
                if (!CommonUtils.isFastClick()) {
                    if (!data.getHref().equals(""))
                        if (data.getIs_ad() == 0) {
                            NewsDetailsActivity.toThis(getContext(), data);
                        }
                }
            }
        });
    }


    @Override
    public void getAliNewData(AliVideoResponse aliVideoResponse) {
        MyNews myNews = new MyNews();
        myNews.setTitle(aliVideoResponse.getData().getTitle());
        myNews.setLikeCount(Integer.parseInt(aliVideoResponse.getData().getLike_count()));
        myNews.setLike(aliVideoResponse.getData().isIs_liked());
        myNews.setCoverUrl(aliVideoResponse.getData().getVideo_cover());
        myNews.setVideoUrl(aliVideoResponse.getData().getVideo_url());
        myNews.setDu_type("1");
        myNews.setOtherId(user_id);
        myNews.setVideo_id(aliVideoResponse.getData().getId());
        myNews.setUserName(aliVideoResponse.getData().getUser_nickname());
        myNews.setUserIcon(user_avter);
        myNews.setCommentCount(Integer.parseInt(aliVideoResponse.getData().getComment_count()));
        FollwVideoPlayActivity.toThis(mContext, myNews, 0, 1, 1);
    }

    @Override
    public void showGoldCome(int count, int type, String masgess) {
        //添加到数据库
//        mPresenter.addReadNews(addGoldDB);
        tvGold.setText("+" + count + " " + mContext.getString(R.string.me_coins));
        tv_frequency.setText(masgess);
        ll_reward_tips.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_reward_tips.setVisibility(View.INVISIBLE);
            }
        }, 2000);
        //数据库保存金币次数
        mPresenter.saveGoldOpenCount();
    }


    /**
     * 清除其他页面数据
     */
    private void clearOtherFragmentDatas() {
        if (myNewsList != null && myNewsList.size() > 0) {
            myNewsList.clear();
        }
        if (myAdViewsIndex != null && myAdViewsIndex.size() > 0) {
            myAdViewsIndex.clear();
        }
        if (myAdViewsIndexNews != null && myAdViewsIndexNews.size() > 0) {
            myAdViewsIndexNews.clear();
        }
    }

    //    分享具体工作
    @Override
    public void setSharedVideoUrl(String url, int type) {
        this.myNewsShare.setVideoUrl(url);
        String shareJson = mPresenter.getShareJson(this.myNewsShare, type);
        final JsShareType jsShareType = new Gson().fromJson(shareJson, JsShareType.class);
        if (type == Common.SHARE_TYPE_TWITTER) {
            if (mTwitterLogin == null) mTwitterLogin = new TwitterLogin();
            mTwitterLogin.setTwitterShareLisenter(new TwitterLogin.TwitterShareLisenter() {
                @Override
                public void getShareOk(String response) {
                    LogUtil.showLog("twtter分享成功");
                    //ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                    //分享成功后计数
                    mPresenter.shareVisit(response, "video", jsShareType.getType(), myNewsShare.getId(), myNewsShare.getDu_type());
                }

                @Override
                public void getShareFail(String response) {
                    LogUtil.showLog("twtter分享失败");
                    ToastUtils.showShort(mContext, getString(R.string.sharedFialed));
                }

                @Override
                public void getShareCancel(String response) {
                    LogUtil.showLog("twtter分享取消");
                    ToastUtils.showShort(mContext, getString(R.string.sharedCancel));
                }
            });
            mTwitterLogin.twitterShare(getActivity(), jsShareType, Common.TWITTER_SHARE_IAMGE);
        } else if (type == Common.SHARE_TYPE_FACEBOOK) {
            if (mFaceBookShare == null)
                mFaceBookShare = new FaceBookShare(getActivity(), new FacebookCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        LogUtil.showLog("face 分享成功");
                        //ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                        mPresenter.shareVisit(CommonUtils.getShareSuccesResponse(), "video", jsShareType.getType(), myNewsShare.getId(), myNewsShare.getDu_type());
                    }

                    @Override
                    public void onCancel() {
                        LogUtil.showLog("face 分享失败");
                        ToastUtils.showShort(mContext, getString(R.string.sharedCancel));
                    }

                    @Override
                    public void onError(FacebookException error) {
                        LogUtil.showLog("face 分享取消");
                        ToastUtils.showShort(mContext, getString(R.string.sharedFialed));
                    }
                });


            mFaceBookShare.share(jsShareType);
        } else if (type == Common.SHARE_TYPE_LINKEDIN) {
            LinkedInPlatform mLinkedInPlatform = new LinkedInPlatform(getActivity());
            mLinkedInPlatform.linkedInShareLisenter(new LinkedInPlatform.linkedInShareLisenter() {
                @Override
                public void getShareOk(String response) {
                    //ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                    mPresenter.shareVisit(CommonUtils.getShareSuccesResponse(), "video", jsShareType.getType(), myNewsShare.getId(), myNewsShare.getDu_type());

                }

                @Override
                public void getShareFail(String response) {
                    ToastUtils.showShort(mContext, getString(R.string.sharedFialed));
                }
            });
            mLinkedInPlatform.linkedInShare(jsShareType);
        } else if (type == Common.SHARE_TYPE_WHATS) {
            whatsAppShare.shareLink(jsShareType.getUrl());
        }

    }


    @Override
    public void onDestroyView() {
        isFinsh = true;
        super.onDestroyView();
        nativeAdFetcher.destroyAllAds();
    }

    @Override
    public void getCodeView() {

    }

    @Override
    public void showNetWorkError() {

    }

    @Override
    public void showGoldComes(int count, int type, String masgess) {
        //添加到数据库
//        mPresenter.addReadNews(addGoldDB);
        if (!TextUtils.isEmpty(count + "")) {
            UserSpCache.getInstance(getApplicationContext()).putInt(UserSpCache.GOLD_NUMBERS, count);//保存获取金币次数
        } else {
            UserSpCache.getInstance(getApplicationContext()).putInt(UserSpCache.GOLD_NUMBERS,
                    (UserSpCache.getInstance(getApplicationContext()).getInt(UserSpCache.GOLD_NUMBERS) - 1));//保存获取金币次数
        }
        tvGold.setText("+" + count + " " + mContext.getString(R.string.me_coins));
        tv_frequency.setText(masgess);
        ll_reward_tips.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_reward_tips.setVisibility(View.INVISIBLE);
            }
        }, 2000);
        //数据库保存金币次数
        mPresenter.saveGoldOpenCount();
        initVideoProgress();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        if (refresh_view != null) {
            refresh_view.finishRefresh();
            refresh_view.finishLoadmore();
        }
        if (isRefresh) {
            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR, EmptyLayout.NET_WORK_ERROR);
        } else {
            ToastUtils.showLong(getActivity(), msg);
        }

        if (video_progress_layout.getVisibility() == View.VISIBLE) {
            video_progress_layout.setVisibility(View.INVISIBLE);
        }
        if (mVideoProgress != null) {
            mPlayback = false;
            mCurrentProgress = 0;
            mVideoProgress.setProgress(0);
        }
    }

    public void initTimerTask() {
        if (timer2 == null) {
            timer2 = new Timer();
        }
        timerTask2 = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.arg1 = 0x222;
                handler.sendMessage(message);
            }
        };
        isPause = false;
    }

    public void destroyTimer() {
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        if (timerTask2 != null) {
            timerTask2.cancel();
            timerTask2 = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        if (timer1 != null) {
            timer1.cancel();
            timer1 = null;
        }
        if (mGoogleNativeAdsUtils != null) {
            mGoogleNativeAdsUtils.destroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mFaceBookShare != null) {
            mFaceBookShare.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

}
