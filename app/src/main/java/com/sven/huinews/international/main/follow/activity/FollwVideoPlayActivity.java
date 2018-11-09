package com.sven.huinews.international.main.follow.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dueeeke.videoplayer.listener.VideoListener;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.Comment;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.event.VideoLikeEvent;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.TipOffRequest;
import com.sven.huinews.international.entity.requst.VideoCollectionCancelRequest;
import com.sven.huinews.international.entity.requst.VideoCollectionRequest;
import com.sven.huinews.international.entity.requst.VideoCommentLikeRequest;
import com.sven.huinews.international.entity.requst.VideoCommentRequest;
import com.sven.huinews.international.entity.requst.VideoDeleteRequest;
import com.sven.huinews.international.entity.requst.VideoListRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.ComentsResponse;
import com.sven.huinews.international.entity.response.CommentReponse;
import com.sven.huinews.international.entity.response.FollowVideoResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.main.follow.adapter.VideoVerticalAdapter;
import com.sven.huinews.international.main.follow.video.VerticalVideoController;
import com.sven.huinews.international.main.userdetail.activity.UserDetailedDataActivity;
import com.sven.huinews.international.main.video.adapter.VideoCommentAdapter;
import com.sven.huinews.international.main.video.contract.FirstVideoDetailContract;
import com.sven.huinews.international.main.video.listener.ProgressBarListener;
import com.sven.huinews.international.main.video.model.FirstVideoDetailModel;
import com.sven.huinews.international.main.video.presenter.FirstVideoDetailPresenter;
import com.sven.huinews.international.publicclass.ReportPresenter;
import com.sven.huinews.international.publicclass.VideoAndNewsPresenter;
import com.sven.huinews.international.tplatform.facebook.FaceBookShare;
import com.sven.huinews.international.tplatform.linkedin.LinkedInPlatform;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.FrescoUtil;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.EdittextUtlis;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.MyRefreshLayout;
import com.sven.huinews.international.view.MyVideoProgress;
import com.sven.huinews.international.view.VerticalViewPager;
import com.sven.huinews.international.view.dialog.CustomShareDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class FollwVideoPlayActivity extends BaseActivity<FirstVideoDetailPresenter, FirstVideoDetailModel> implements FirstVideoDetailContract.View {
    private VerticalViewPager mVerticalViewPager;
    private ImageButton btn_back, btn_close;
    private LinearLayout video_comment_ll;
    private RecyclerView comment_rv;//评论列表

    private EmptyLayout comment_empty;
    private TextView tv_comment_count, tvMsg; //评论数
    private TextView et_video_comment; //评论编辑
    private TextView btn_send;// 发送ØØ
    private MyRefreshLayout refresh_view;
    //金币相关
    private TextView tv_gold_count;
    private Drawable mCollectDrawable, mCollectedDrawable, mLikedDrawable, mLikeDrawable;
    private IjkVideoView mIjkVideoView; //播放器
    private VerticalVideoController mVerticalVideoController;//垂直播放Vp
    private List<View> mViews = new ArrayList<>();

    private VideoVerticalAdapter mAdapter;
    private int mCurrentPosition;
    private int mPlayingPosition;
    private Animation animationIn, animationOut;
    private String etComment;
    private String videoId;
    Timer timer2 = new Timer();
    private TimerTask timerTask2;
    MyNews myNews;
    List<MyNews> toDatas = new ArrayList<>();
    int type;
    int PAGE = 1;
    private int videoType;
    private int pageIndex, counter;
    private String du_type;
    private MyVideoProgress mVideoProgress;
    private RelativeLayout video_progress_layout;
    private Boolean isLookGoldProgress = false;//金币进度条是否可见
    private CustomShareDialog mCustomShareDialog;

    private TextView tvGold;
    private TextView tv_frequency;
    private LinearLayout ll_reward_tips;
    private Handler mHandler = new Handler();
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());

    //分享
    FaceBookShare mFaceBookShare;
    TwitterLogin mTwitterLogin;
    private String mCommentId;
    private Boolean isNetworkAccess = false;
    ReportPresenter mReportPresenter;
    private boolean isTurnOnTimer = false;
    boolean isFirstBroadcast = true;
    private long lasttime;
    private boolean isFirstImplement = true;//第一次执行

    private VideoAndNewsPresenter mVideoAndNewsPresenter;//视频播放统计

    private FrameLayout fAdView;
    private AdView mAdView;
    private ImageView close_ad;
    private TranslateAnimation mShowAction;//显示动画
    private TranslateAnimation mHiddenAction;//隐藏动画

    /**
     * 从其他页面传值
     *
     * @return
     */
    public static void toThis(Context mContext, MyNews data, int position, int type, int videoType) {
        Intent intent = new Intent(mContext, FollwVideoPlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.BUNDLE_FORVIDEO_LIST, data);
        bundle.putInt(Constant.BUNDLE_VIDEO_LIST_TYPE, type);
        bundle.putInt(Constant.BUNDLE_VIDEO_LIST_POSITION, position);
        bundle.putInt("videoType", videoType);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        //获取bundle
        Bundle bundle = getIntent().getExtras();
        myNews = bundle.getParcelable(Constant.BUNDLE_FORVIDEO_LIST);
        videoId = myNews.getVideo_id();
        mCurrentPosition = bundle.getInt(Constant.BUNDLE_VIDEO_LIST_POSITION);
        type = bundle.getInt(Constant.BUNDLE_VIDEO_LIST_TYPE);
        videoType = bundle.getInt("videoType");
        toDatas.add(myNews);
        hideBottomUIMenu();//设置全屏  隐藏虚拟键
        return R.layout.activity_follw_video_play;
    }

    @Override
    public void initView() {
        initShare();
        mVerticalViewPager = findViewById(R.id.viewpager);
        btn_back = findViewById(R.id.btn_back);
        btn_close = findViewById(R.id.btn_close);
        video_comment_ll = findViewById(R.id.video_comment_ll);
        comment_rv = findViewById(R.id.comment_rv);
        comment_empty = findViewById(R.id.comment_empty);
        tv_comment_count = findViewById(R.id.tv_comment_count);
        et_video_comment = findViewById(R.id.et_video_comment);
        btn_send = findViewById(R.id.btn_send);
        refresh_view = findViewById(R.id.refreshView);
        tv_gold_count = findViewById(R.id.tv_gold_count);
        tvGold = findViewById(R.id.tv_gold);
        tv_frequency = findViewById(R.id.tv_frequency);
        ll_reward_tips = findViewById(R.id.ll_reward_tips);

        fAdView = findViewById(R.id.f_ad_view);
        mAdView = findViewById(R.id.video_banner_adView);
        close_ad = findViewById(R.id.close_ad);

        mCollectDrawable = getResources().getDrawable(R.mipmap.icon_zan4);
        mCollectDrawable.setBounds(0, 0, mCollectDrawable.getMinimumWidth(), mCollectDrawable.getMinimumHeight());
        mCollectedDrawable = getResources().getDrawable(R.mipmap.icon_zan4ed);
        mCollectedDrawable.setBounds(0, 0, mCollectedDrawable.getMinimumWidth(), mCollectedDrawable.getMinimumHeight());
        mLikedDrawable = getResources().getDrawable(R.mipmap.video_liked);
        mLikedDrawable.setBounds(0, 0, mLikedDrawable.getMinimumWidth(), mLikedDrawable.getMinimumHeight());
        mLikeDrawable = getResources().getDrawable(R.mipmap.video_like);
        mLikeDrawable.setBounds(0, 0, mLikeDrawable.getMinimumWidth(), mLikeDrawable.getMinimumHeight());

        mVideoProgress = findViewById(R.id.video_progress);
        video_progress_layout = findViewById(R.id.video_progress_layout);
    }

    public void initShare() {
        //初始化分享
        if (mCustomShareDialog == null) {
            mCustomShareDialog = new CustomShareDialog(FollwVideoPlayActivity.this);
            mCustomShareDialog.setOnShareListener(new CustomShareDialog.OnShareListener() {
                @Override
                public void onShare(int type) {
                    if (type != Common.SHARE_TYPE_REPORT) {
                        //分享 网络链接
                        mPresenter.getVideoShareUrl(getVideSharedRequest(type), type);
                    } else {
                        //举报  网络链接
                        mReportPresenter.videoReport(toDatas.get(mCurrentPosition).getVideo_id());
                    }
                }
            });
        }
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 0x222) {
                if (isTurnOnTimer && isFirstBroadcast) {
                    if (lasttime <= 0) {
                        isTurnOnTimer = false;
                        if (!UserSpCache.getInstance(mContext).getStringData(UserSpCache.REQUEST_CODE).equals("")) {
                            //倒计时完成 做处理 网络
                            isNetworkAccess = true;
                            mPresenter.OnlineTimeLength(TaskRequest.TASK_ID_READ_TIME_LONG, UserSpCache.getInstance(mContext).getStringData(UserSpCache.REQUEST_CODE), TaskRequest.GET_CONIS);
                        }
                    } else {
                        lasttime = lasttime - 1;
                    }
                }
            }
            return false;
        }

    });

    @Override
    public void initEvents() {
        btn_back.setOnClickListener(this);//退回
        btn_close.setOnClickListener(this);//关闭
        btn_send.setOnClickListener(this);//发送评论
        et_video_comment.setOnClickListener(this);//评论输入框
        close_ad.setOnClickListener(this);
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
                    MobclickAgent.onEvent(FollwVideoPlayActivity.this, Common.AD_TYPE_GOOGLE_VIDEO_LOOK);
                    MobclickAgent.onEvent(FollwVideoPlayActivity.this, Common.AD_TYPE_GOOGLE_VIDEO_LOOK, "google_video_look");
//                    getRootView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);//沉浸式模式
                }
            }

            @Override
            public void onAdOpened() {
                MobclickAgent.onEvent(FollwVideoPlayActivity.this, Common.AD_TYPE_GOOGLE_VIDEO_CLICK);
                MobclickAgent.onEvent(FollwVideoPlayActivity.this, Common.AD_TYPE_GOOGLE_VIDEO_CLICK, "google_video");
                super.onAdOpened();
            }
        });
        //显示google广告

        AdRequest adRequest = new AdRequest.Builder()
                .setGender(1)
                .build();
        mAdView.loadAd(adRequest);


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
            FollwVideoPlayActivity.this.finish();
        } else if (v == btn_close) {
            hideCommentView();
        } else if (v == et_video_comment) {
            setEtListener();
        } else if (v == btn_send) {
            sendComment();
        } else if (v == close_ad) {
            if (fAdView != null) {
                fAdView.startAnimation(mHiddenAction);
                fAdView.setVisibility(View.GONE);
            }
        }


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

    @Override
    public void initObject() {
        setMVP();
        setVideo();
        if (videoType == 1 ) {
            mPresenter.getDetailVideoList(type, PAGE, false);
        }
        if (mReportPresenter == null) {
            mReportPresenter = new ReportPresenter(this);
        }

        if (mVideoAndNewsPresenter == null) {
            mVideoAndNewsPresenter = new VideoAndNewsPresenter(this);
        }

        setVideoCommentInfo();
    }


    //记录上一次滑动的positionOffsetPixels值
    private int lastValue = -1;

    private void setVideo() {
        mIjkVideoView = new IjkVideoView(this);
        PlayerConfig config = new PlayerConfig.Builder().enableCache().setLooping().build();
        mIjkVideoView.setPlayerConfig(config);
        mVerticalVideoController = new VerticalVideoController(this);
        mIjkVideoView.setVideoController(mVerticalVideoController);
        //为了第一次能够在页面显示先加载传过来的数据
        myNews = toDatas.get(0);
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_video_n, null);
        SimpleDraweeView imageView = view.findViewById(R.id.thumb);
        FrescoUtil.loadDefImg(imageView, toDatas.get(mCurrentPosition).getCoverUrl());
        setVideoInfo(view, myNews);//item
        mViews.add(view);

        mAdapter = new VideoVerticalAdapter(mViews);
        mVerticalViewPager.setAdapter(mAdapter);
        mVerticalViewPager.setCurrentItem(mCurrentPosition);
        mVerticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                //当播放为倒数第三个时再次拉取数据
                if (mCurrentPosition == toDatas.size() - 3) {
                    PAGE += 1;
                    mPresenter.getDetailVideoList(type, PAGE, true);
                }

                if (toDatas.get(position).getIsGold() == 1 && mPresenter.isCanGetCoinByReadNews(toDatas.get(mCurrentPosition))) {
                    mVideoProgress.setProgress(0);
                    video_progress_layout.setVisibility(View.VISIBLE);
                    isLookGoldProgress = true;
                } else {
                    mVideoProgress.stopProgress();
                    video_progress_layout.setVisibility(View.INVISIBLE);//播放完成隐藏金币那个圈
                    isLookGoldProgress = false;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPlayingPosition == mCurrentPosition) return;
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mIjkVideoView.release();
                    ViewParent parent = mIjkVideoView.getParent();
                    if (parent != null && parent instanceof FrameLayout) {
                        ((FrameLayout) parent).removeView(mIjkVideoView);
                    }

                    startPlay();
                }
            }
        });

        //自动播放
        mVerticalViewPager.post(new Runnable() {
            @Override
            public void run() {
                startPlay();
            }
        });

        mIjkVideoView.setVideoListener(new VideoListener() {
            @Override
            public void onVideoStarted() {
                //启动
                if (toDatas.get(mCurrentPosition).getIsGold() == 1 && mPresenter.isCanGetCoinByReadNews(toDatas.get(mCurrentPosition))) { // && mPresenter.isCanGetCoinByReadNews(item)
                    mVideoProgress.setProgress(0);
                    video_progress_layout.setVisibility(View.VISIBLE);
                    isLookGoldProgress = true;
                    if (mVideoProgress != null) {
                        if (mIjkVideoView.getDuration() > toDatas.get(mCurrentPosition).getAddGoldTime()) {
                            mVideoProgress.animStart(toDatas.get(mCurrentPosition).getAddGoldTime());
                        } else {
                            mVideoProgress.animStart(toDatas.get(mCurrentPosition).getDuration());
                        }
                        mVideoProgress.resumeAnim();
                        //初始化金币进度条
                        mVideoProgress.OnVideoProgressLisenter(new MyVideoProgress.OnVideoProgressLisenter() {
                            @Override
                            public void end() {
                                mVideoProgress.stopProgress();
                                video_progress_layout.setVisibility(View.INVISIBLE);//播放完成隐藏金币那个圈
                                isLookGoldProgress = false;
                                //进度条完成 去获取金币
                                mPresenter.readAnyNewsGetGold(TaskRequest.TASK_ID_READ_NEWS, toDatas.get(mCurrentPosition).getId());
                            }
                        });
                    } else {
                        video_progress_layout.setVisibility(View.INVISIBLE);
                        isLookGoldProgress = false;
                        if (mVideoProgress.getAnimatorStatus()) mVideoProgress.stopProgress();
                    }
                }

                if (lasttime > 0) {
                    UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                }
                //开始计时
                if (UserSpCache.getInstance(mContext).getStringData(UserSpCache.REQUEST_CODE).equals("")) {
                    lasttime = UserSpCache.getInstance(mContext).getLong(UserSpCache.REQUEST_LONG);
                    if (lasttime > 0) {
                        if (isFirstImplement) {
                            if (timer2 != null) {
                                isFirstBroadcast = true;
                                timerTask2 = new TimerTask() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.arg1 = 0x222;
                                        handler.sendMessage(message);
                                    }
                                };
                                timer2.schedule(timerTask2, 0, 1000);
                                isFirstImplement = false;
                            }
                        }
                        isTurnOnTimer = true;
                    }
                }

            }

            @Override
            public void onVideoPaused() {
                //暂停的时候调用
                if (isLookGoldProgress) {
                    mVideoProgress.pauseAnim();
                }

                //暂停计时
                isTurnOnTimer = false;
                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                if (timer2 != null) {
                    timer2.cancel();
                }
            }

            @Override
            public void onComplete() {
                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                ToastUtils.showLong(getApplication(), "asd");
                mVideoAndNewsPresenter.videoStatistics(toDatas.get(mCurrentPosition).getId(),toDatas.get(mCurrentPosition).getType()+"",toDatas.get(mCurrentPosition).getDu_type());
            }

            @Override
            public void onPrepared() {
            }

            @Override
            public void onError() {

                //暂停计时
                isTurnOnTimer = false;
                UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, lasttime);
                if (timer2 != null) {
                    timer2.cancel();
                }
                //错误
                if (isLookGoldProgress) {
                    mVideoProgress.stopProgress();
                    video_progress_layout.setVisibility(View.INVISIBLE);
                    isLookGoldProgress = false;
                }
            }

            @Override
            public void onInfo(int what, int extra) {
            }
        });

        mVerticalVideoController.setProgressBarListener(new ProgressBarListener() {
            @Override
            public void onMove() {
                //移动
                if (isLookGoldProgress) {
                    mVideoProgress.stopProgress();
                    video_progress_layout.setVisibility(View.INVISIBLE);
                    isLookGoldProgress = false;
                }
            }
            @Override
            public void onBuffering() {
                LogUtil.showLog("onStop");
            }

            @Override
            public void onPreparing() {

            }

            @Override
            public void onProgressCompletion() {

            }

            @Override
            public void onProgress(int position, int duration) {

            }

            @Override
            public void onStop() {

            }
        });

    }


    private SimpleDraweeView user_head;
    private TextView video_content, tv_like, btn_comment, btn_share, tv_follw, user_name, tv_collection;
    private SeekBar mSeekBar;

    private void setVideoInfo(View v, final MyNews myNews) {
        //找到视频播放的页面
        user_head = v.findViewById(R.id.user_head);
        video_content = v.findViewById(R.id.video_content);
        tv_like = v.findViewById(R.id.tv_like);
        btn_comment = v.findViewById(R.id.btn_comment);
        btn_share = v.findViewById(R.id.btn_share);
        user_name = v.findViewById(R.id.user_name);
        tv_collection = v.findViewById(R.id.tv_collection);
        mSeekBar = v.findViewById(R.id.seek_bar);

        //赋值
        user_head.setImageURI(myNews.getUserIcon());
        video_content.setText(myNews.getTitle().equals("") ? "" : myNews.getTitle());
        btn_comment.setText(CommonUtils.getLikeCount(myNews.getCommentCount()));
        btn_share.setText(CommonUtils.getLikeCount(myNews.getShareCount() / 2));
        tv_collection.setText(CommonUtils.getLikeCount(myNews.getLikeCount()));
        user_name.setText(myNews.getUserName());

        setIsColection(myNews);

        //头像点击事件
        user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myNews.getDu_type().equals("2")) {
//                    UserDetailsActivity.toThis(mContext, 2, myNews.getOtherId());
                    LogUtil.showLog("msg--otherId:" + myNews.getOtherId());
                    UserDetailedDataActivity.toThis(mContext, 2, myNews.getOtherId());
                } else {
                    UserDetailedDataActivity.toThis(mContext, 1, myNews.getOtherId());
                }
            }
        });

        //评论点击
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentView(myNews, (TextView) v);

            }
        });
        //分享点击
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomShareDialog.show();
                myNews.setShareCount(myNews.getShareCount() + 1);
                TextView tv = v.findViewById(R.id.btn_share);
                tv.setText(CommonUtils.getLikeCount(myNews.getShareCount() / 2));
            }
        });
        //垂直视频点赞
        tv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = v.findViewById(R.id.tv_collection);
                myNews.setLike(!myNews.isLike());
                myNews.setLikeCount(myNews.isLike() ? myNews.getLikeCount() + 1 : myNews.getLikeCount() - 1);

                tv.setCompoundDrawables(null, myNews.isLike() ? mCollectedDrawable : mCollectDrawable, null, null);
                tv.setTextColor(myNews.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.bg_white));
                tv.setText(CommonUtils.getLikeCount(myNews.getLikeCount()));
                mPresenter.onCollection(getVideoCollectionRequest(myNews));
                EventBus.getDefault().post(new VideoLikeEvent(myNews));

            }
        });

    }

    private void setIsColection(MyNews data) {
        Drawable drawable;
        if (!data.isLike()) {
            drawable = getResources().getDrawable(R.mipmap.icon_zan4);
        } else {
            drawable = getResources().getDrawable(R.mipmap.icon_zan4ed);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_collection.setCompoundDrawables(null, drawable, null, null);
        tv_collection.setTextColor(data.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.bg_white));

    }

    //让activity退出时mIjkVideoView立即停止播放
    @Override
    protected void onPause() {
        super.onPause();
        mIjkVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIjkVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIjkVideoView.release();
        timer2.cancel();
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
    public VideoCommentRequest getVideoCommentRequest(String du_type, int page, String videoId, String sort) {
        VideoCommentRequest request = new VideoCommentRequest();
        request.setPage(PAGE + "");
        request.setId(videoId + "");
        request.setDu_type(du_type);
        request.setOrder(sort);
        return request;

    }

    //获得数据进行展示
    @Override
    public void getVideoDetailList(List<MyNews> myNew) {
        //去重 去除广告
        for (int i = 0; i < myNew.size(); i++) {
            if (myNew.get(i).getIsAd() == 1) {
                myNew.remove(i);
            }
        }

        toDatas.addAll(myNew);
        for (int i = 0; i < myNew.size(); i++) {
            MyNews item = myNew.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_video_n, null);
            SimpleDraweeView imageView = view.findViewById(R.id.thumb);
            FrescoUtil.loadDefImg(imageView, item.getCoverUrl());
            setVideoInfo(view, item);
            mViews.add(view);
        }

        mAdapter.notifyDataSetChanged();


    }

    private VideoCommentAdapter mVideoCommentAdapter;

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
                ToastUtils.showLong(mContext, "No More Datas");
            }
            PAGE = pageIndex;
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
        ToastUtils.showShort(mContext, "Successful~");
        mPresenter.onVideoComment(getVideoCommentRequest(du_type, 1, videoId, Constant.COMMENT_TIME));
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
        VideoCollectionCancelRequest request = new VideoCollectionCancelRequest();
        request.setType(Constant.CHANNEL_TYPE_VIDEO);
        request.setrId(videoId + "");
        return request;
    }

    @Override
    public void showGoldCome(int count, int type, String masgess) {
        //添加到数据库
        mPresenter.addReadNews(toDatas.get(mCurrentPosition));
        tvGold.setText("+" + count + "Coins");
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

    @Override
    public VideoShareUrlRequest getVideSharedRequest(int type) {
        VideoShareUrlRequest request = new VideoShareUrlRequest();
        request.setType(type + "");
        request.setVideoId(toDatas.get(mCurrentPosition).getVideo_id());
        return request;
    }

    @Override
    public void setSharedVideoUrl(String url, int type) {

        this.toDatas.get(mCurrentPosition).setVideoUrl(url);

        String shareJson = mPresenter.getShareJson(this.toDatas.get(mCurrentPosition), type);
        final JsShareType jsShareType = new Gson().fromJson(shareJson, JsShareType.class);
        if (type == Common.SHARE_TYPE_TWITTER) {
            if (mTwitterLogin == null) mTwitterLogin = new TwitterLogin();
            mTwitterLogin.setTwitterShareLisenter(new TwitterLogin.TwitterShareLisenter() {
                @Override
                public void getShareOk(String response) {
                    ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                    //分享成功后计数
                    mPresenter.shareVisit(response, jsShareType.getActivity_type(), jsShareType.getType(), toDatas.get(mCurrentPosition).getVideo_id(), toDatas.get(mCurrentPosition).getDu_type());
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
            mTwitterLogin.twitterShare(FollwVideoPlayActivity.this, jsShareType, Common.TWITTER_SHARE_IAMGE);
        } else if (type == Common.SHARE_TYPE_FACEBOOK) {
            if (mFaceBookShare == null)
                mFaceBookShare = new FaceBookShare(FollwVideoPlayActivity.this, new FacebookCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                        mPresenter.shareVisit(CommonUtils.getShareSuccesResponse(), jsShareType.getActivity_type(), jsShareType.getType(), toDatas.get(mCurrentPosition).getVideo_id(), toDatas.get(mCurrentPosition).getDu_type());
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
            LinkedInPlatform mLinkedInPlatform = new LinkedInPlatform(FollwVideoPlayActivity.this);
            mLinkedInPlatform.linkedInShareLisenter(new LinkedInPlatform.linkedInShareLisenter() {
                @Override
                public void getShareOk(String response) {
                    ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                    mPresenter.shareVisit(CommonUtils.getShareSuccesResponse(), jsShareType.getActivity_type(), jsShareType.getType(), toDatas.get(mCurrentPosition).getVideo_id(), toDatas.get(mCurrentPosition).getDu_type());
                }

                @Override
                public void getShareFail(String response) {
                    ToastUtils.showShort(mContext, getString(R.string.sharedFialed));
                }
            });
            mLinkedInPlatform.linkedInShare(jsShareType);
        }
    }

    @Override
    public void getAliNewData(AliVideoResponse response) {
        mIjkVideoView.setUrl(response.getData().getVideo_url());
        mIjkVideoView.start();
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

        return request;
    }


    @Override
    public void getCodeView() {
        isNetworkAccess = false;
    }

    @Override
    public void showNetWorkError() {
        isNetworkAccess = false;
    }

    @Override
    public void shareSucceed() {
//        tv.setCompoundDrawables(null, myNews.isLike() ? mCollectedDrawable : mCollectDrawable, null, null);
//        tv.setTextColor(myNews.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.bg_white));
//        tv.setText(CommonUtils.getLikeCount(myNews.getLikeCount()));
    }

    @Override
    public VideoListRequest getVideoListRequest() {
        return null;
    }

    @Override
    public void setVideoList(List<MyNews> mDatas) {

    }

    @Override
    public FollowRequest getFollowRequest() {
        return null;
    }

    @Override
    public void setFollowData(List<FollowVideoResponse.DataBean> datas, boolean isRefresh, boolean isLoadMore) {

    }

    @Override
    public void hideRefresh() {

    }

    @Override
    public void hideLoadMore(Boolean isHide) {

    }

    @Override
    public void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType) {

    }

    @Override
    public void setPersonLikesData(PersonLikeResponse response, int selectType, boolean isRefresh) {

    }

    @Override
    public void videoDeleteSuccess(VideoDeleteRequest request) {

    }

    @Override
    public void videoDeleteError(String msg) {

    }

    private void startPlay() {
        View view = mViews.get(mCurrentPosition);
        FrameLayout frameLayout = view.findViewById(R.id.container);
        //封面
        mVerticalVideoController.getThumb().setImageURI(toDatas.get(mCurrentPosition).getCoverUrl());
        frameLayout.addView(mIjkVideoView, 1);

        if (mCurrentPosition != 0 && toDatas.get(mCurrentPosition).getDu_type().equals("1")) {//真
            mPresenter.getAliPlayUrl(toDatas.get(mCurrentPosition).getId());
        } else {//假
            //视频地址
            mIjkVideoView.setUrl(toDatas.get(mCurrentPosition).getVideoUrl());
            mIjkVideoView.start();
        }
        mIjkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_CENTER_CROP);
        mPlayingPosition = mCurrentPosition;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (video_comment_ll.getVisibility() == View.VISIBLE) {
                hideCommentView();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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


    /**
     * 显示评论界面
     */
    //请求评论数据
    public void showCommentView(MyNews mData, TextView textView) {
        this.myNews = mData;
        this.tvMsg = textView;
        isRefresh = true;
        if (PAGE > 1) {
            PAGE = 1;
        }

//        comment_empty.setErrorType(EmptyLayout.LOADING, -1);
        if (animationIn == null) {
            animationIn = android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.movie_count_in);
        }
        video_comment_ll.startAnimation(animationIn);
        video_comment_ll.setVisibility(View.VISIBLE);

        tv_comment_count.setText(getString(R.string.comment) + ":" + mData.getCommentCount());
        videoId = mData.getVideo_id();
        du_type = mData.getDu_type();
        if (du_type == null) {
            du_type = "1";
        }
        //根据视频id获取评论数据
        mPresenter.onVideoComment(getVideoCommentRequest(du_type, PAGE, videoId, Constant.COMMENT_UP));
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        // 隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTwitterLogin != null) {
            mTwitterLogin.setActivityResult(requestCode, resultCode, data);
        }
    }

}
