package com.sven.huinews.international.main.follow.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duapps.ad.AdError;
import com.duapps.ad.entity.strategy.NativeAd;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.dialog.DislikeDialog;
import com.sven.huinews.international.dialog.GoldComeDialog;
import com.sven.huinews.international.entity.Comment;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.event.VideoLikeEvent;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.DisLikeVideoRequest;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.TipOffRequest;
import com.sven.huinews.international.entity.requst.VideoCollectionCancelRequest;
import com.sven.huinews.international.entity.requst.VideoCollectionRequest;
import com.sven.huinews.international.entity.requst.VideoCommentLikeRequest;
import com.sven.huinews.international.entity.requst.VideoCommentRequest;
import com.sven.huinews.international.entity.requst.VideoDeleteRequest;
import com.sven.huinews.international.entity.requst.VideoListRequest;
import com.sven.huinews.international.entity.requst.VideoPlayTimeSize;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoStayRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.ComentsResponse;
import com.sven.huinews.international.entity.response.CommentReponse;
import com.sven.huinews.international.entity.response.FollowVideoResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.main.follow.adapter.VideoAdapter;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.main.userdetail.activity.UserDetailedDataActivity;
import com.sven.huinews.international.main.video.adapter.VideoCommentAdapter;
import com.sven.huinews.international.main.video.contract.FirstVideoDetailContract;
import com.sven.huinews.international.main.video.model.FirstVideoDetailModel;
import com.sven.huinews.international.main.video.presenter.FirstVideoDetailPresenter;
import com.sven.huinews.international.publicclass.AddGoldModel;
import com.sven.huinews.international.publicclass.ReportPresenter;
import com.sven.huinews.international.publicclass.VideoAndNewsPresenter;
import com.sven.huinews.international.tplatform.facebook.FaceBookShare;
import com.sven.huinews.international.tplatform.linkedin.LinkedInPlatform;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.tplatform.whatsapp.WhatsAppShare;
import com.sven.huinews.international.utils.BaiDuAdUtils;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.GoogleInterstitialAdsUtils;
import com.sven.huinews.international.utils.GoogleNativeAdsUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.VungleAdUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.utils.statusbar.Eyes;
import com.sven.huinews.international.view.EdittextUtlis;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.MyRefreshLayout;
import com.sven.huinews.international.view.MyVideoProgress;
import com.sven.huinews.international.view.dialog.CustomShareDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FollowVideoPlay1Activity extends BaseActivity<FirstVideoDetailPresenter, FirstVideoDetailModel> implements FirstVideoDetailContract.View {
    private ImageButton btn_back, btn_close;
    private RecyclerView video_rv;
    private VideoAdapter mVideoAdapter;
    private List<MyNews> videos = new ArrayList<>();
    private int mCurrentPosition;
    private Animation animationIn, animationOut;
    private EmptyLayout comment_empty;
    private LinearLayout video_comment_ll;
    private RecyclerView comment_rv;//评论列表
    private MyRefreshLayout refresh_view;
    private TextView tv_comment_count, tvMsg; //评论数
    private TextView et_video_comment; //评论编辑
    private int pageIndex;//评论数据下标
    private TextView btn_send;// 发送
    private String etComment;
    private String videoId;
    private String du_type;
    private int videoType;
    private CustomShareDialog mCustomShareDialog;
    private ReportPresenter mReportPresenter;
    private MyNews myNews;
    private int pageType = -1;//页面类型
    private int duty_type;
    private String Other_id;
    private int PAGES;
    private MyVideoProgress mVideoProgress;
    private MyNews mMyNewsDB;
    TwitterLogin mTwitterLogin;
    FaceBookShare mFaceBookShare;
    private FrameLayout fAdView;
    private AdView mAdView;
    private ImageView close_ad;
    private TranslateAnimation mShowAction;//显示动画
    private TranslateAnimation mHiddenAction;//隐藏动画
    private long lasttime;
    private boolean isFirstImplement = true;//第一次执行
    Timer timer2 = new Timer();
    boolean isFirstBroadcast = true;
    private boolean isTurnOnTimer = false;
    private Boolean isNetworkAccess = false;
    private TimerTask timerTask2;
    private RelativeLayout topComment;
    //百度广告
    private BaiDuAdUtils mBaiDuAdUtils;
    private int mADNumber = 0;
    //vungle广告
    private VungleAdUtils vungleAdUtils;

    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
    //金币转转圈
    private int mCurrentProgress = -1; //停止时 的进度
    private long mDuration = -1;
    RelativeLayout video_progress_layout;
    private boolean mPlayback = false;
    private TextView tv_gold, tv_frequency;
    private LinearLayout ll_reward_tips;
    private int sharePosition;
    private boolean mVideoPlayback = false;
    private String videoTimeId = "";

    private int lastTimeNumber = 0;//上一次加入广告的数组长度
    //Google插页广告
    GoogleInterstitialAdsUtils mGoogleInterstitialAdsUtils;

    private GoogleNativeAdsUtils mGoogleNativeAdsUtils;//google原生
    private int googleAdSize = 0;//google原生广告数
    private HashMap<Integer, MyNews> myAdViewsIndex;//记录当前广告位置和类型  video

    private boolean isPause = false;
    private DislikeDialog mDislikeDialog;
    private boolean videoIsPlay = true;
    private WhatsAppShare whatsAppShare;
    private boolean isStart = false;
    private long satrtTime = 0;
    private long endTime = 0;
    private VideoAndNewsPresenter mVideoAndNewsPresenter;//视频播放统计
    private Handler mTimeHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    VideoPlayTimeSize videoPlayTimeSize = new VideoPlayTimeSize();
                    videoPlayTimeSize.setUse_time((int) time);
                    videoPlayTimeSize.setVideo_id(videoTimeId);
                    new AddGoldModel(FollowVideoPlay1Activity.this).videoPlayTime(videoPlayTimeSize, new DataCallBack() {
                        @Override
                        public void onSucceed(String json) {

                        }

                        @Override
                        public void onFail(BaseResponse response) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                    break;
            }
        }
    };

    public static void toThis(Context mContext, List<MyNews> data, int position, int type, int videoType, int duType, String otherId, int PAGE) {//
        Intent intent = new Intent(mContext, FollowVideoPlay1Activity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.BUNDLE_FORVIDEO_LIST, (ArrayList<MyNews>) data);
        bundle.putInt(Constant.BUNDLE_VIDEO_LIST_TYPE, type);
        bundle.putInt(Constant.BUNDLE_VIDEO_LIST_POSITION, position);
        bundle.putInt("duty_type", duType);
        bundle.putString("otherId", otherId);
        bundle.putInt("videoType", videoType);
        bundle.putInt("PAGE", PAGE);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            videos = bundle.getParcelableArrayList(Constant.BUNDLE_FORVIDEO_LIST);
            mCurrentPosition = bundle.getInt(Constant.BUNDLE_VIDEO_LIST_POSITION);
            pageType = bundle.getInt(Constant.BUNDLE_VIDEO_LIST_TYPE);
            videoType = bundle.getInt("videoType");
            duty_type = bundle.getInt("duty_type");
            Other_id = bundle.getString("otherId");
            PAGES = bundle.getInt("PAGE");
        }
        return R.layout.activity_follow_video_play1;
    }

    @Override
    public void initView() {
        btn_back = findViewById(R.id.btn_back);
        video_rv = findViewById(R.id.video_rv);
        comment_empty = findViewById(R.id.comment_empty);
        refresh_view = findViewById(R.id.refreshView);
        video_comment_ll = findViewById(R.id.video_comment_ll);
        comment_rv = findViewById(R.id.comment_rv);
        tv_comment_count = findViewById(R.id.tv_comment_count);
        et_video_comment = findViewById(R.id.et_video_comment);
        topComment = findViewById(R.id.topComment);
        btn_send = findViewById(R.id.btn_send);
        btn_close = findViewById(R.id.btn_close);

        fAdView = findViewById(R.id.f_ad_view);
        mAdView = findViewById(R.id.video_banner_adView);
        close_ad = findViewById(R.id.close_ad);
        video_progress_layout = findViewById(R.id.video_progress_layout);
        mVideoProgress = findViewById(R.id.video_progress);

        tv_gold = findViewById(R.id.tv_gold);
        tv_frequency = findViewById(R.id.tv_frequency);
        ll_reward_tips = findViewById(R.id.ll_reward_tips);

        video_rv.post(new Runnable() {
            @Override
            public void run() {
                supportPostponeEnterTransition();
            }
        });
    }


    @Override
    public void initEvents() {
        btn_back.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        et_video_comment.setOnClickListener(this);
        close_ad.setOnClickListener(this);
        topComment.setOnClickListener(this);
        video_progress_layout.setOnClickListener(this);
        refresh_view.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                isRefresh = false;
                PAGE += 1;
                //评论上拉加载
                pageIndex = PAGE;
                mPresenter.onVideoComment(getVideoCommentRequest(du_type, PAGE, videoId, Constant.COMMENT_UP));
            }
        });
        refresh_view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                isRefresh = true;
                PAGE = 1;
                //评论刷新
                mPresenter.onVideoComment(getVideoCommentRequest(du_type, PAGE, videoId, Constant.COMMENT_UP));
            }
        });
        //google广告监听
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                isGetAd = true;
                // google ads visible
                if (fAdView.getVisibility() == View.GONE) {
                    fAdView.startAnimation(mShowAction);
                    fAdView.setVisibility(View.VISIBLE);
                    MobclickAgent.onEvent(mContext, Common.AD_TYPE_GOOGLE_VIDEO_LOOK);
                    MobclickAgent.onEvent(mContext, Common.AD_TYPE_GOOGLE_VIDEO_LOOK, "google_video_look");
//                    getRootView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);//沉浸式模式
                }
            }

            @Override
            public void onAdOpened() {
                MobclickAgent.onEvent(mContext, Common.AD_TYPE_GOOGLE_VIDEO_CLICK);
                MobclickAgent.onEvent(mContext, Common.AD_TYPE_GOOGLE_VIDEO_CLICK, "google_video");
                super.onAdOpened();
            }
        });
        //显示google广告
        AdRequest adRequest = new AdRequest.Builder()
                .setGender(1)
                .build();
        mAdView.loadAd(adRequest);


//        if (vungleAdUtils == null) {
//            vungleAdUtils = new VungleAdUtils(getApplicationContext(), true);
//        }


        if (mBaiDuAdUtils == null) {
            mBaiDuAdUtils = new BaiDuAdUtils(FollowVideoPlay1Activity.this, 1);
            mBaiDuAdUtils.setUdAdLisenter(
                    new BaiDuAdUtils.UdAdLisenter() {
                        @Override
                        public void onError(AdError error) {

                        }

                        @Override
                        public void onAdLoaded(List<View> adViews) {
                            LogUtil.showLog("adViews:" + adViews.size());
                        }

                        @Override
                        public void onAdLoadedListData(LinkedList<NativeAd> listData) {
                            int n = 0;
                            for (int i = 0; i < myAdIndex.size(); i++) {
                                NativeAd mNativeAD = listData.get(n);
                                MyNews news = new MyNews();
                                news.setmNativeAd(mNativeAD);
                                news.setIsAd(1);
                                if (lastTimeNumber + myAdIndex.get(i).get("postion") < videos.size() - 1) {
                                    videos.add(lastTimeNumber + myAdIndex.get(i).get("postion"), news);
                                    mVideoAdapter.notifyItemInserted(myAdIndex.get(i).get("postion"));
                                    n++;
                                }
                            }
//                            mVideoAdapter.notifyDataSetChanged();//myAdIndex.get(i).get("postion")
                            lastTimeNumber = videos.size();
                        }
                    }
            );
        }

        if (pageType == Common.STORIES_PAGE) {
            if (myAdIndex == null) {//记录当前广告位置和类型
                myAdIndex = new ArrayList<>();
            } else {
                myAdIndex.clear();
            }
            int postion = 0;
            int interval = 0;
            boolean duAndGoogle = true;
            if (videos != null && videos.size() > 0) {
                interval = videos.size() / 5;
            }
            for (int i = 0; i < 5; i++) {
                HashMap map = new HashMap<String, Integer>();
                postion = postion + interval;
                map.put("postion", postion);
                if (duAndGoogle) {
                    map.put("type", Common.AD_TYPE_UD);
                    mADNumber += 1;
                } else {
                    map.put("type", Common.AD_TYPE_GOOGLE);
                    googleAdSize += 1;
                }
//                duAndGoogle = !duAndGoogle;
                myAdIndex.add(map);
            }

            if (mBaiDuAdUtils != null) {//请求百度
                mBaiDuAdUtils.getUDAds(mADNumber);
                mADNumber = 0;
            }

//            if (mGoogleNativeAdsUtils != null) {
//                if (!mGoogleNativeAdsUtils.mAdisLoading()){
//                    mGoogleNativeAdsUtils.loadAds(googleAdSize);
//                    googleAdSize = 0;
//                }
//            }
        }

        //初始化动画
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(500);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == btn_back) {
            finish();
        } else if (v == topComment) {
            hideCommentView();
        } else if (v == btn_close) {
            hideCommentView();
        } else if (v == et_video_comment) {
            setEtListener();
        } else if (v == close_ad) {
            if (fAdView != null) {
                fAdView.startAnimation(mHiddenAction);
                fAdView.setVisibility(View.GONE);
                ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0).setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        } else if (v == video_progress_layout) {
            if (mGoogleInterstitialAdsUtils.isLoad()) {
                mGoogleInterstitialAdsUtils.showAd(Common.AD_TYPE_GOOGLE_INTERSTITIAL_LOOK, Common.AD_TYPE_GOOGLE_INTERSTITIAL_CLICK);
            }
        }
    }

    @Override
    public void initObject() {
        setMVP();
        if (mReportPresenter == null) {
            mReportPresenter = new ReportPresenter(this);
        }
        hideBottomUIMenu();
        mVideoAdapter = new VideoAdapter(mContext, FollowVideoPlay1Activity.this);
        setManager();
        setVideoCommentInfo();
        initShare();


        //初始化google插页广告
        if (mGoogleInterstitialAdsUtils == null) {
            mGoogleInterstitialAdsUtils = new GoogleInterstitialAdsUtils(FollowVideoPlay1Activity.this);
        }

        //google原生广告
//        if (mGoogleNativeAdsUtils == null) {
//            mGoogleNativeAdsUtils = new GoogleNativeAdsUtils(this);
//            mGoogleNativeAdsUtils.setGoogleAdLisenter(new GoogleNativeAdsUtils.GoogleAdLisenter() {
//                @Override
//                public void onError(int errorCode) {
//                    //错误代码
//                    LogUtil.showLog("mGoogleNativeAdsUtils:"+errorCode);
//                }
//
//                @Override
//                public void onAdLoaded(List<View> listview,List<UnifiedNativeAd> listADData) {
//                    LogUtil.showLog("mGoogleNativeAdsUtils:"+listADData.size());
//                    int n = 0;
//                    for (int i = 0; i < myAdIndex.size(); i++) {
//                        if ((myAdIndex.get(i).get("type")+"").equals(Common.AD_TYPE_GOOGLE)){
//                            UnifiedNativeAd mUnifiedNativeAd = listADData.get(n);
//                            MyNews news = new MyNews();
//                            news.setUnifiedNativeAd(mUnifiedNativeAd);
//                            news.setIsAd(2);
//                            videos.add(lastTimeNumber+myAdIndex.get(i).get("postion"), news);
//                            mVideoAdapter.notifyItemInserted(myAdIndex.get(i).get("postion"));
//                            n++;
//                        }
//                    }
//                    lastTimeNumber = videos.size();
//                }
//            });
//        }


        if (mVideoAndNewsPresenter == null) {
            mVideoAndNewsPresenter = new VideoAndNewsPresenter(FollowVideoPlay1Activity.this);
        }
    }


    private void setManager() {
        PagerSnapHelper snapHelper = new PagerSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                int mCurrentPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                LogUtil.showLog("mCurrentPosition:" + mCurrentPosition);
                videoIsPlay = true;
                mVideoPlayback = false;
                if (mCurrentPosition == videos.size() - 3) {
                    PAGES += 1;
                    loadMoreData();
                }
                if (mCurrentPosition % 7 == 0) {
                    AdRequest adRequest = new AdRequest.Builder()
                            .setGender(1)
                            .build();
                    mAdView.loadAd(adRequest);
                }

                if (mCurrentPosition % 6 == 0) {
                    //vungle广告
//                    if (vungleAdUtils.isTheCacheComplete()) {
//                        vungleAdUtils.openNoLoadAd();
//                    }
                }


                if (mCurrentPosition < videos.size()) {
                    FollowVideoPlay1Activity.this.mCurrentPosition = mCurrentPosition;
                }


                if (mCurrentPosition < videos.size()
                        && videos.get(mCurrentPosition).getIsAd() == 0
                        && videos.get(mCurrentPosition).getDu_type().equals("1")
                        && videos.get(mCurrentPosition).getVideoUrl().equals("")) {
                    mPresenter.getAliPlayUrl(videos.get(mCurrentPosition).getId());
                    videoId = videos.get(mCurrentPosition).getVideo_id();
                }

                return mCurrentPosition;
            }

            @Nullable
            @Override
            public View findSnapView(RecyclerView.LayoutManager layoutManager) {
                View itemView = super.findSnapView(layoutManager);
                IjkVideoView ijkVideoView = itemView.findViewById(R.id.videoPlayer);
                if (!ijkVideoView.isPlaying()) {
                    ijkVideoView.start();
                }
                return itemView;
            }

            @Override
            public boolean onFling(int velocityX, int velocityY) {
                return super.onFling(velocityX, velocityY);
            }
        };
        snapHelper.attachToRecyclerView(video_rv);
        final LinearLayoutManager manager = new LinearLayoutManager(mContext);
        video_rv.setLayoutManager(manager);
        video_rv.setAdapter(mVideoAdapter);
        mVideoAdapter.refreshItem(videos);
        video_rv.post(new Runnable() {
            @Override
            public void run() {
                startPlay(mCurrentPosition);
            }
        });
        adapterLisenter();
    }

    private void startPlay(final int position) {
        video_rv.scrollToPosition(position);
        if (videos.get(mCurrentPosition).getIsAd() == 0 && videos.get(mCurrentPosition).getDu_type().equals("1") && TextUtils.isEmpty(videos.get(mCurrentPosition).getVideoUrl())) {
            mPresenter.getAliPlayUrl(videos.get(mCurrentPosition).getId());
        } else {
            video_rv.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager manager = (LinearLayoutManager) video_rv.getLayoutManager();
                    View itemView = manager.findViewByPosition(position);
                    if (itemView != null) {
                        IjkVideoView ijkVideoView = itemView.findViewById(R.id.videoPlayer);
                        ijkVideoView.start();
                    }
                }
            });
        }
    }

    private void loadMoreData() {
        switch (pageType) {
            case -1:
                break;
            case Common.STORIES_PAGE:
                mPresenter.onVideoList(false);
                break;
            case Common.FOLLOW_PAGE:
                mPresenter.getFollowList(false);
                break;
            case Common.VIDEO_VIDEOS_PAGE:
                mPresenter.getUserDetailsInfo(getPersonWorkRequest(), isRefresh, Constant.SELECT_USER_VIDEOS);
                break;
            case Common.VIDEO_LIKES_PAGE:
                mPresenter.getUserDetailsLikesInfo(getLikeRequest(), isRefresh, Constant.SELECT_USER_LIKE_VIDEOS);
                break;
        }
    }

    public PersonWorkRequest getPersonWorkRequest() {
        PersonWorkRequest request = new PersonWorkRequest();
        if (duty_type != -1) {//
            request.setDu_type(duty_type + "");
            request.setOther_id(Other_id);
        }
        request.setPage_size(LIMIT + "");
        request.setPage(PAGES + "");
        return request;
    }

    public LikesRequest getLikeRequest() {
        LikesRequest request = new LikesRequest();
        if (duty_type != -1) {
            request.setDu_type(duty_type + "");
            request.setOther_id(Other_id);
        }
        request.setPage_size(LIMIT + "");
        request.setPage(PAGES + "");
        return request;
    }

    private IjkVideoView videoPlayer = null;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 0x222) {
                if (lasttime > 0) {
                    lasttime = lasttime - 100;
                    LogUtil.showLog(lasttime + "");
                } else {
                    if (!"".equals(mUserSpCache.getStringData(UserSpCache.REQUEST_CODE))) {
                        mPresenter.getThirtyGold(TaskRequest.TASK_ID_READ_TIME_LONG + "", mUserSpCache.getStringData(UserSpCache.REQUEST_CODE));
//                        ToastUtils.showShort(mContext, "我就看看加金币没有");
                    }
                }
            }

            return false;
        }

    });

    private void adapterLisenter() {
        mVideoAdapter.setOnItemClickLisenter(new VideoAdapter.OnItemClickLisenter() {
            @Override
            public void onClick(MyNews data, int position) {

            }

            @Override
            public void onComment(MyNews news, TextView tv_msg) {
                myNews = news;
                showCommentView(news, tv_msg);
            }

            @Override
            public void onLike(MyNews item) {
                mPresenter.onCollection(getVideoCollectionRequest(item));
                EventBus.getDefault().post(new VideoLikeEvent(item));
            }

            @Override
            public void onHead(MyNews news) {
                LogUtil.showLog("msg----头像点击");
                if (news.getIsAd() != 1 && news.getDu_type().equals("2")) {
                    UserDetailedDataActivity.toThis(mContext, 2, news.getOtherId());
                } else {
                    UserDetailedDataActivity.toThis(mContext, 1, news.getOtherId());
                }
            }

            @Override
            public void onShare(MyNews item, int pos) {
                sharePosition = pos;
                videoId = item.getVideo_id();
//                mCustomShareDialog.show();
                if (duty_type != 0) {
                    mCustomShareDialog.show(CustomShareDialog.OTHER);
                } else {
                    if (pageType == Common.VIDEO_VIDEOS_PAGE) {
                        mCustomShareDialog.show(CustomShareDialog.PERSONAL);
                    } else {
                        mCustomShareDialog.show(CustomShareDialog.OTHER);
                    }
                }
            }

            @Override
            public void onGetGold(MyNews items) {
                mMyNewsDB = items;
                mPresenter.readAnyNewsGetGold(TaskRequest.TASK_ID_READ_NEWS, items.getVideo_id());
            }

            @Override
            public void getVideoProgress(MyVideoProgress VideoProgress) {
                LogUtil.showLog("滑动的当前位置 getVideoProgress");
            }

            @Override
            public void onVideoStart(IjkVideoView v) {
                videoPlayer = v;
            }

            @Override
            public void onDisplayHideGold(boolean isDisplay) {

            }

            @Override
            public void onDisLikeVideo(MyNews news) {
                if (mDislikeDialog == null) {
                    mDislikeDialog = new DislikeDialog(FollowVideoPlay1Activity.this);
                }
                mDislikeDialog.setmDisLikeVideoRequest(new DisLikeVideoRequest(news.getVideo_id(), news.getDu_type()));
                mDislikeDialog.show();
            }
        });

        mVideoAdapter.setOnVideoPlayStatusLisenter(new VideoAdapter.onVideoPlayStatusLisenter() {
            @Override
            public void onVideoComplete(MyNews mData, int position) {
                isTurnOnTimer = false;
                mVideoPlayback = false;
                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                if (videoIsPlay) {
                    //视频统计
                    mPresenter.videoStatistics(mData.getId(), mData.getType() + "", mData.getDu_type() + "");
                    videoIsPlay = false;
                }
                //观看完成 视频统计
                mVideoAndNewsPresenter.videoLookTask();

                mVideoProgress.pauseAnim();
                mCurrentProgress = mVideoProgress.getProgressCurrent();
                mDuration = mVideoProgress.surplusTime();
            }

            @Override
            public void onVideoPlaying(final MyNews mData, int position) {
                timeIsContinue = false;
                mMyNewsDB = mData;
                lasttime = mUserSpCache.getLong(UserSpCache.REQUEST_LONG);
                if (lasttime > 0) {
                    if (isPause) {//isPause是否暂停
                        destroyTimer();
                        initTimerTask();
                        timer2.schedule(timerTask2, 0, 1000);
                    } else {
//                        destroyTimer();
                        initTimerTask();
                        timer2.schedule(timerTask2, 0, 1000);
                    }
                }

                if (UserSpCache.getInstance(getApplicationContext()).getInt(UserSpCache.GOLD_NUMBERS) >= 1
                        && mPresenter.isCanGetCoinByReadNews(mData)
                        && duty_type != 0) {
                    if (video_progress_layout.getVisibility() == View.INVISIBLE) {
                        video_progress_layout.setVisibility(View.VISIBLE);
                    }
                    if (mCurrentProgress != -1) {
                        mVideoProgress.setProgress(mCurrentProgress);
                    } else {
                        mVideoProgress.setProgress(0);
                    }

                    if (mVideoProgress != null) {
                        if (!mPlayback) {
                            if (UserSpCache.getInstance(mContext).getInt(UserSpCache.GOLD_TIME) != -1) {
                                mVideoProgress.animStart(UserSpCache.getInstance(getApplicationContext()).getInt(UserSpCache.GOLD_TIME) * 1000);
//                                mVideoProgress.animStart(10 * 1000);
                            } else {
                                mVideoProgress.animStart(30 * 1000);
                            }
                        } else {
                            if (mDuration > 0) {
                                mVideoProgress.animStart(mDuration);
                            } else {
                                mVideoProgress.animStart(30 * 1000);
                            }
                        }
                        mVideoProgress.resumeAnim();
                        mPlayback = true;
                        //监听 金币进度条
                        mVideoProgress.OnVideoProgressLisenter(new MyVideoProgress.OnVideoProgressLisenter() {
                            @Override
                            public void end() {
                                mPlayback = false;
                                mCurrentProgress = 0;
                                mVideoProgress.stopProgress();
                                mVideoProgress.setProgress(0);
                                //进度条完成 去获取金币
                                mPresenter.readAnyNewsGetGold(TaskRequest.TASK_ID_READ_NEWS, mMyNewsDB.getVideo_id());
                            }
                        });
                    } else {
                        if (mVideoProgress.getAnimatorStatus()) {
                            mDuration = mVideoProgress.surplusTime();
                            mVideoProgress.stopProgress();
                            mCurrentProgress = mVideoProgress.getProgressCurrent();
                        }
                    }
                } else {
                    if (video_progress_layout.getVisibility() == View.VISIBLE) {
                        video_progress_layout.setVisibility(View.INVISIBLE);
                    }
                    mDuration = mVideoProgress.surplusTime();
                    mVideoProgress.stopProgress();
                    mCurrentProgress = mVideoProgress.getProgressCurrent();
                }

                //页面停留统计
                if (!isStart) {
                    satrtTime = System.currentTimeMillis();
                    isStart = true;
                }

            }

            @Override
            public void onVideoPaused() {
                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                if (!isPause) {
                    isPause = true;
                    timer2.cancel();
                }

                mVideoProgress.pauseAnim();
                mCurrentProgress = mVideoProgress.getProgressCurrent();
                mDuration = mVideoProgress.surplusTime();

            }

            @Override
            public void onVideoError() {
                //暂停计时
                isTurnOnTimer = false;
                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                if (timer2 != null) {
                    timer2.cancel();
                }

                mVideoProgress.stopProgress();
                mCurrentProgress = 0;
                mDuration = -1;
            }

            @Override
            public void onVideoStop(MyNews mData) {
                mVideoProgress.pauseAnim();
                if (!isPause) {
                    isPause = true;
                    timer2.cancel();
                }
                mCurrentProgress = mVideoProgress.getProgressCurrent();
                mDuration = mVideoProgress.surplusTime();

                //页面停留统计
                if (isStart) {
                    endTime = System.currentTimeMillis();
                    isStart = false;
                    mPresenter.videoStay(new VideoStayRequest((endTime - satrtTime) + "", mData.getVideo_id(), mData.getDu_type(), "1"));
                }
            }

            @Override
            public void onBuffering(MyNews mData) {
                mVideoProgress.pauseAnim();
                mCurrentProgress = mVideoProgress.getProgressCurrent();
                mDuration = mVideoProgress.surplusTime();
//                mPresenter.videoStatistics(mData.getVideo_id());
            }

            @Override
            public void onPreparing(MyNews mData) {
                if (mData.getDu_type().equals("1") || mData.getDu_type() == "1") {
                    videoTimeId = mData.getVideo_id();
                    if (!mVideoPlayback) {
                        LinearLayoutManager manager = (LinearLayoutManager) video_rv.getLayoutManager();
                        View itemView = manager.findViewByPosition(mCurrentPosition);
                        if (itemView!=null){
                            IjkVideoView ijkVideoView = itemView.findViewById(R.id.videoPlayer);
                            if (ijkVideoView.getBufferPercentage() < 50) {
                                mVideoPlayback = true;
                                timeIsContinue = true;
                                startTime1();
                            }
                        }
                    }
                }
            }
        });

    }


    @Override
    public VideoCommentRequest getVideoCommentRequest(String du_type, int page, String videoId, String sort) {
        VideoCommentRequest request = new VideoCommentRequest();
        request.setPage(PAGE + "");
        request.setId(videoId + "");
        request.setDu_type(du_type);
        request.setSize(LIMIT + "");
        request.setOrder(sort);
        return request;
    }

    @Override
    public void getVideoDetailList(List<MyNews> myNews) {

    }

    private VideoCommentAdapter mVideoCommentAdapter;
    private String mCommentId;

    private void setVideoCommentInfo() {
        comment_rv.setLayoutManager(new LinearLayoutManager(mContext));
        mVideoCommentAdapter = new VideoCommentAdapter(mContext);
        comment_rv.setAdapter(mVideoCommentAdapter);

        mVideoCommentAdapter.setOnLikeListener(new VideoCommentAdapter.OnLikeListener() {
            @Override
            public void onLikeChange(Comment comment) {
                mCommentId = comment.getId();
                mPresenter.onVideoCommentLike(getVideoCommentLikes(comment.getVideo_id()));
            }
        });
    }

    @Override
    public void setComment(CommentReponse videoComent) {
        if (isRefresh) {
            if (videoComent.getData().size() > 0) {
                comment_empty.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
            } else {
                comment_empty.setErrorType(EmptyLayout.NO_DATA, EmptyLayout.NO_COMMENT);
            }
            refresh_view.finishRefresh();
        } else {
            if (videoComent.getData().size() <= 0) {
                ToastUtils.showLong(mContext, getString(R.string.no_more_datas));
            }
            refresh_view.finishLoadmore();
        }
        mVideoCommentAdapter.setData(videoComent.getData(), isRefresh);
    }

    @Override
    public AdCommentRequest getAdComentRequest() {
        AdCommentRequest request = new AdCommentRequest();
        request.setContent(etComment);
        request.setV_id(videoId);
        request.setDu_type(du_type);
        return request;
    }

    @Override
    public void setAddComents(ComentsResponse comentsResponse) {
        if (comentsResponse.getData().getGold() > 0) {
            ToastUtils.showGoldCoinToast(FollowVideoPlay1Activity.this, getString(R.string.commentary_award)
                    , "+" + comentsResponse.getData().getGold());
        }
        PAGE = 1;
        mPresenter.onVideoComment(getVideoCommentRequest(du_type, PAGE, videoId, Constant.COMMENT_TIME));
        myNews.setCommentCount(myNews.getCommentCount() + 1);
        tv_comment_count.setText(getString(R.string.comment) + ":" + CommonUtils.getLikeCount(myNews.getCommentCount()));
        tvMsg.setText(CommonUtils.getLikeCount(myNews.getCommentCount()));
        EventBus.getDefault().post(new VideoLikeEvent(myNews));
    }

    @Override
    public VideoCollectionRequest getVideoCollectionRequest(MyNews myNews) {
        if (myNews.getDu_type() == null) {
            if (videoType == 2) {
                myNews.setDu_type("2");
            } else {
                myNews.setDu_type("1");
            }
        }
        VideoCollectionRequest request = new VideoCollectionRequest();
        request.setVideo_id(myNews.getVideo_id());
        request.setDu_type(myNews.getDu_type());
        return request;
    }

    @Override
    public VideoCollectionCancelRequest getVideoCollectionCancelRequest(String videoId) {
        return null;
    }

    private Handler mHandler = new Handler();


    @Override
    public void showGoldCome(final int count, final int type, final String masgess) {
        video_rv.post(new Runnable() {
            @Override
            public void run() {
                //添加到数据库
                if (mMyNewsDB != null) {
                    mPresenter.addReadNews(mMyNewsDB);
                }
                if (type != 30) {
                    if (!TextUtils.isEmpty(count + "")) {
                        UserSpCache.getInstance(getApplicationContext()).putInt(UserSpCache.GOLD_NUMBERS, count);//保存获取金币次数
                    } else {
                        UserSpCache.getInstance(getApplicationContext()).putInt(UserSpCache.GOLD_NUMBERS,
                                (UserSpCache.getInstance(getApplicationContext()).getInt(UserSpCache.GOLD_NUMBERS) - 1));//保存获取金币次数
                    }
                }
                tv_gold.setText("+" + count + getString(R.string.me_coins));
                tv_frequency.setText(masgess);
                ll_reward_tips.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_reward_tips.setVisibility(View.INVISIBLE);
                    }
                }, 2000);
                mPresenter.saveGoldOpenCount();
            }
        });
    }

    @Override
    public VideoShareUrlRequest getVideSharedRequest(int type) {
        VideoShareUrlRequest request = new VideoShareUrlRequest();
        request.setType(type + "");
        request.setVideoId(videoId);
        return request;
    }

    public VideoDeleteRequest getVideDelete() {
        VideoDeleteRequest request = new VideoDeleteRequest();
        request.setVideoId(videos.get(sharePosition).getVideo_id());
        return request;
    }

    @Override
    public void setSharedVideoUrl(String url, int type) {
        this.videos.get(mCurrentPosition).setVideoUrl(url);

        String shareJson = mPresenter.getShareJson(this.videos.get(mCurrentPosition), type);
        final JsShareType jsShareType = new Gson().fromJson(shareJson, JsShareType.class);
        if (type == Common.SHARE_TYPE_TWITTER) {
            if (mTwitterLogin == null) mTwitterLogin = new TwitterLogin();
            mTwitterLogin.setTwitterShareLisenter(new TwitterLogin.TwitterShareLisenter() {
                @Override
                public void getShareOk(String response) {
                    //ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                    //分享成功后计数
                    mPresenter.shareVisit(response, jsShareType.getActivity_type(), jsShareType.getType(), videos.get(mCurrentPosition).getVideo_id(), videos.get(mCurrentPosition).getDu_type());
                }

                @Override
                public void getShareFail(String response) {
                    ToastUtils.showShort(mContext, getString(R.string.sharedFialed));
                }

                @Override
                public void getShareCancel(String response) {
                    ToastUtils.showShort(mContext, getString(R.string.sharedCancel));

                }
            });
            mTwitterLogin.twitterShare(FollowVideoPlay1Activity.this, jsShareType, Common.TWITTER_SHARE_IAMGE);
        } else if (type == Common.SHARE_TYPE_FACEBOOK) {
            if (mFaceBookShare == null)
                mFaceBookShare = new FaceBookShare(FollowVideoPlay1Activity.this, new FacebookCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        //ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                        mPresenter.shareVisit(CommonUtils.getShareSuccesResponse(), jsShareType.getActivity_type(), jsShareType.getType(), videos.get(mCurrentPosition).getVideo_id(), videos.get(mCurrentPosition).getDu_type());
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showShort(mContext, getString(R.string.sharedCancel));
                    }

                    @Override
                    public void onError(FacebookException error) {
                        ToastUtils.showShort(mContext, getString(R.string.sharedFialed));
                    }
                });
            mFaceBookShare.share(jsShareType);
        } else if (type == Common.SHARE_TYPE_LINKEDIN) {
            LinkedInPlatform mLinkedInPlatform = new LinkedInPlatform(FollowVideoPlay1Activity.this);
            mLinkedInPlatform.linkedInShareLisenter(new LinkedInPlatform.linkedInShareLisenter() {
                @Override
                public void getShareOk(String response) {
                    // ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                    mPresenter.shareVisit(CommonUtils.getShareSuccesResponse(), jsShareType.getActivity_type(), jsShareType.getType(), videos.get(mCurrentPosition).getVideo_id(), videos.get(mCurrentPosition).getDu_type());
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
    public void getAliNewData(AliVideoResponse response) {
        if (videos.get(mCurrentPosition).getVideoUrl().equals("")) {
            videos.get(mCurrentPosition).setVideoUrl(response.getData().getVideo_url());
        }
        video_rv.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = (LinearLayoutManager) video_rv.getLayoutManager();
                View itemView = manager.findViewByPosition(mCurrentPosition);
                IjkVideoView ijkVideoView = itemView.findViewById(R.id.videoPlayer);
                ijkVideoView.setUrl(videos.get(mCurrentPosition).getVideoUrl());
                ijkVideoView.start();
            }
        });
    }

    @Override
    public TipOffRequest getTipOffRequest() {
        return null;
    }

    @Override
    public VideoCommentLikeRequest getVideoCommentLikes(String videoId) {
        VideoCommentLikeRequest request = new VideoCommentLikeRequest();
        request.setCid(Integer.parseInt(mCommentId));
        request.setDu_type(Integer.parseInt(du_type));
        request.setVideo_id(videoId);
        return request;
    }

    @Override
    public void getCodeView() {

    }

    @Override
    public void showNetWorkError() {

    }

    @Override
    public void shareSucceed() {

    }

    @Override
    public VideoListRequest getVideoListRequest() {
        VideoListRequest request = new VideoListRequest();
        request.setR_type(1 + "");
        request.setPage(PAGES + "");
        return request;
    }

    private List<Map<String, Integer>> myAdIndex;

    @Override
    public void setVideoList(List<MyNews> mDatas) {
        if (myAdIndex == null) {//记录当前广告位置和类型
            myAdIndex = new ArrayList<>();
        } else {
            myAdIndex.clear();
        }
        if (myAdViewsIndex == null) {//记录当前广告位置和类型
            myAdViewsIndex = new HashMap<>();
        } else {
            myAdViewsIndex.clear();
        }
        /*//拿到广告的位置  把原来的视频移除 方便之后加入广告
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getIsAd() == 1) {
                mADNumber += 1;
                HashMap map = new HashMap<String, Integer>();
                map.put("postion", i);
                myAdIndex.add(map);
                mDatas.remove(i);
                i--;
            }
        }*/

        //拿到广告的位置  把原来的视频移除 方便之后加入广告
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setType(videoType);
            if (mDatas.get(i).getIsAd() == 1) {
                mDatas.get(i).setType(MyNews.AD_VIDEO);
//                if (mDatas.get(i).getAd_type().equals(Common.AD_TYPE_UD)) {
                mADNumber += 1;
                HashMap map = new HashMap<String, Integer>();
                map.put("postion", i);
                map.put("type", Common.AD_TYPE_UD);
                myAdIndex.add(map);
//                } else if (mDatas.get(i).getAd_type().equals(Common.AD_TYPE_YAHOO)
//                        ||mDatas.get(i).getAd_type().equals(Common.AD_TYPE_GOOGLE)) {
//                    googleAdSize += 1;
//                }
                myAdViewsIndex.put(i, mDatas.get(i));
                mDatas.remove(i);
                i--;
            }
        }

        videos.addAll(mDatas);

        if (mBaiDuAdUtils != null) {//请求百度
            mBaiDuAdUtils.getUDAds(mADNumber);
            mADNumber = 0;
        }

//        if (mGoogleNativeAdsUtils != null) {
//            if (!mGoogleNativeAdsUtils.mAdisLoading()){
//                mGoogleNativeAdsUtils.loadAds(googleAdSize);
//                googleAdSize = 0;
//            }
//        }

    }

    @Override
    public FollowRequest getFollowRequest() {
        FollowRequest request = new FollowRequest();
        request.setPage(PAGES);
        request.setPagesize(LIMIT);
        return request;
    }

    @Override
    public void setFollowData(List<FollowVideoResponse.DataBean> datas, boolean isRefresh, boolean isLoadMore) {
        videos.addAll(dataConversion(datas));
    }

    public List<MyNews> dataConversion(List<FollowVideoResponse.DataBean> datas) {
        List<MyNews> list = new ArrayList<>();
        MyNews myNews = null;
        for (int i = 0; i < datas.size(); i++) {
            myNews = new MyNews();
            FollowVideoResponse.DataBean dataBean = datas.get(i);
            myNews.setDu_type(dataBean.getDu_type() + "");
            myNews.setVideoUrl(dataBean.getVideo_url());
            myNews.setId(dataBean.getId());
            myNews.setVideo_id(dataBean.getId());
            myNews.setCoverUrl(dataBean.getVideo_cover());
            myNews.setUserIcon(dataBean.getUser_avatar());
            myNews.setTitle(dataBean.getTitle());
            myNews.setCommentCount(dataBean.getComment_count());
            myNews.setShareCount(dataBean.getShare_count());
            myNews.setLikeCount(dataBean.getLike_count());
            myNews.setUserName(dataBean.getUser_nickname());
            myNews.setLike(dataBean.isIs_up());
            myNews.setOtherId(dataBean.getUser_id() + "");
            list.add(myNews);
        }
        return list;
    }


    @Override
    public void hideRefresh() {

    }

    @Override
    public void hideLoadMore(Boolean isHide) {

    }


    @Override
    public void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType) {
        videos.addAll(dataConversionWorks(response.getData()));
    }

    public List<MyNews> dataConversionWorks(List<PerSonWorkResponse.DataBean> listWorkResponse) {
        List<MyNews> list = new ArrayList<>();
        MyNews myNews = null;
        for (int i = 0; i < listWorkResponse.size(); i++) {
            myNews = new MyNews();
            PerSonWorkResponse.DataBean dataBean = listWorkResponse.get(i);
            myNews.setDu_type(dataBean.getDu_type() + "");
            myNews.setVideoUrl(dataBean.getVideo_url());
            myNews.setId(dataBean.getId());
            myNews.setVideo_id(dataBean.getId());
            myNews.setCoverUrl(dataBean.getVideo_cover());
            myNews.setUserIcon(dataBean.getUser_avatar());
            myNews.setTitle(dataBean.getTitle());
            myNews.setCommentCount(dataBean.getComment_count());
            myNews.setShareCount(dataBean.getShare_count());
            myNews.setLikeCount(dataBean.getLike_count());
            myNews.setUserName(dataBean.getUser_nickname());
            myNews.setLike(dataBean.isIs_up());
            myNews.setOtherId(dataBean.getUser_id() + "");
            list.add(myNews);
        }
        return list;
    }

    @Override
    public void setPersonLikesData(PersonLikeResponse response, int selectType, boolean isRefresh) {
        videos.addAll(dataConversionLikes(response.getData()));
    }

    //删除视频成功做处理
    @Override
    public void videoDeleteSuccess(VideoDeleteRequest request) {
        if (videos.size() > 1) {
            videos.remove(sharePosition);
            mVideoAdapter.notifyItemRemoved(sharePosition);
            mVideoAdapter.notifyItemRangeChanged(sharePosition, videos.size());
            if (sharePosition == 1 && videos.size() == 1) {
                sharePosition = 0;
            } else if (sharePosition == videos.size()) {
                sharePosition = sharePosition - 1;
            }
            mCurrentPosition = sharePosition;
            startPlay(sharePosition);
        } else if (videos.size() == 1) {
            videos.remove(mCurrentPosition);
            FollowVideoPlay1Activity.this.finish();
        } else if (videos.size() < 1) {
            FollowVideoPlay1Activity.this.finish();
        }
        EventBus.getDefault().post(Common.REFRESH_VIDEO);
    }

    //删除视频错误
    @Override
    public void videoDeleteError(String msg) {
        ToastUtils.showLong(this, msg);
    }

    public List<MyNews> dataConversionLikes(List<PersonLikeResponse.DataBean> listLikeResponse) {
        List<MyNews> list = new ArrayList<>();
        MyNews myNews = null;
        for (int i = 0; i < listLikeResponse.size(); i++) {
            myNews = new MyNews();
            PersonLikeResponse.DataBean dataBean = listLikeResponse.get(i);
            myNews.setDu_type(dataBean.getDu_type() + "");
            myNews.setVideoUrl(dataBean.getVideo_url());
            myNews.setId(dataBean.getId());
            myNews.setVideo_id(dataBean.getId());
            myNews.setCoverUrl(dataBean.getVideo_cover());
            myNews.setUserIcon(dataBean.getUser_avatar());
            myNews.setTitle(dataBean.getTitle());
            myNews.setCommentCount(Integer.parseInt(dataBean.getComment_count()));
            myNews.setShareCount(Integer.parseInt(dataBean.getShare_count()));
            myNews.setLikeCount(Integer.parseInt(dataBean.getLike_count()));
            myNews.setUserName(dataBean.getUser_nickname());
            myNews.setLike(dataBean.isIs_up());
            myNews.setOtherId(dataBean.getUser_id() + "");
            list.add(myNews);
        }
        return list;
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

    /**
     * 显示评论界面
     */
    //请求评论数据
    public void showCommentView(MyNews mData, TextView textView) {
        this.tvMsg = textView;
        isRefresh = true;
        if (PAGE > 1) {
            PAGE = 1;
        }
        comment_empty.setErrorType(EmptyLayout.LOADING, -1);
        if (animationIn == null) {
            animationIn = android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.movie_count_in);
        }
        video_comment_ll.startAnimation(animationIn);
        video_comment_ll.setVisibility(View.VISIBLE);
        tv_comment_count.setText(getString(R.string.comment) + ":" + mData.getCommentCount());
        //根据视频id获取评论数据
        videoId = mData.getVideo_id();
        du_type = mData.getDu_type();
        mPresenter.onVideoComment(getVideoCommentRequest(mData.getDu_type(), PAGE, mData.getVideo_id(), Constant.COMMENT_UP));
    }

    /**
     * 隐藏评论界面
     */
    private void hideCommentView() {
        if (animationOut == null) {
            animationOut = android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.movie_count_out);
        }
        video_comment_ll.startAnimation(animationOut);//开始动画
        video_comment_ll.setVisibility(View.GONE);
    }


    private void setEtListener() {
        EdittextUtlis.showCommentEdit(this, et_video_comment, new EdittextUtlis.liveCommentResult() {
            @Override
            public void onResult(boolean confirmed, String comment) {
                if (confirmed) {
                    etComment = comment;
                    sendComment();
                }
            }
        });
    }

    private void sendComment() {
        if (TextUtils.isEmpty(etComment)) {
            ToastUtils.showLong(mContext, getString(R.string.null_commnet));
            return;
        }
        mPresenter.addComents();
        et_video_comment.setText("");
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        Eyes.translucentStatusBar(this, true);
    }

    public void initShare() {
        whatsAppShare = new WhatsAppShare(getApplicationContext());
        //初始化分享
        if (mCustomShareDialog == null) {
            mCustomShareDialog = new CustomShareDialog(FollowVideoPlay1Activity.this);
            mCustomShareDialog.setOnShareListener(new CustomShareDialog.OnShareListener() {
                @Override
                public void onShare(int type) {
                    if (type == Common.SHARE_TYPE_DELETE) {
                        new AlertDialog.Builder(FollowVideoPlay1Activity.this)
                                .setTitle(R.string.tip)
                                .setMessage(getString(R.string.delet_current))
                                .setNegativeButton(R.string.effect_str5, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).setPositiveButton(R.string.continues, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.getVideoDelete(getVideDelete());
                            }
                        }).show();
                    } else if (type != Common.SHARE_TYPE_REPORT) {
                        //分享 网络链接
                        mPresenter.getVideoShareUrl(getVideSharedRequest(type), type);
                    } else {
                        //举报  网络链接
                        mReportPresenter.videoReport(videoId);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoPlayer != null) {
            videoPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoPlayer != null) {
            videoPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeIsContinue = false;
        VideoViewManager.instance().releaseVideoPlayer();
        if (timer2 != null) {
            timer2.cancel();
        }
        if (pageType == Common.STORIES_PAGE) {
            if (mBaiDuAdUtils != null) {
                mBaiDuAdUtils.onDestroy();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTwitterLogin != null) {
            mTwitterLogin.setActivityResult(requestCode, resultCode, data);
        }

        if (mFaceBookShare != null) {
            mFaceBookShare.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    private Boolean timeIsContinue = true;
    private long time = 0;

    public void startTime1() {
        time = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (timeIsContinue) {
                    time++;
                    mTimeHandler.postDelayed(this, 1);
                } else {
                    mTimeHandler.sendEmptyMessage(0);
                }
            }
        };
        new Thread(runnable).start();
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

}
