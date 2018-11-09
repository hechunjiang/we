package com.sven.huinews.international.main.home.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.meicam.sdk.NvsStreamingContext;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.dialog.GoldComeDialog;
import com.sven.huinews.international.entity.Comment;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.Progress;
import com.sven.huinews.international.entity.event.LogoutEvent;
import com.sven.huinews.international.entity.event.OpenDialogEvent;
import com.sven.huinews.international.entity.event.OpenLoginPageEvent;
import com.sven.huinews.international.entity.event.OpenNewPageEvent;
import com.sven.huinews.international.entity.event.RefreshTaskEvent;
import com.sven.huinews.international.entity.event.ToMainPageEvent;
import com.sven.huinews.international.entity.event.ToMePageEvent;
import com.sven.huinews.international.entity.jspush.JsGoToRarn;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.TipOffRequest;
import com.sven.huinews.international.entity.requst.VideoCommentLikeRequest;
import com.sven.huinews.international.entity.requst.VideoCommentRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlResponse;
import com.sven.huinews.international.entity.response.ComentsResponse;
import com.sven.huinews.international.entity.response.CommentReponse;
import com.sven.huinews.international.entity.response.FacebookRegResponse;
import com.sven.huinews.international.entity.response.PushTaskResponse;
import com.sven.huinews.international.entity.response.TwitterRegResponse;
import com.sven.huinews.international.main.earn.EarnActivity;
import com.sven.huinews.international.main.home.dialog.OtherActivityDialog;
import com.sven.huinews.international.main.home.fragment.FollowFragment;
import com.sven.huinews.international.main.home.fragment.MeFragment;
import com.sven.huinews.international.main.home.fragment.VideoAndNewsFragment;
import com.sven.huinews.international.main.task.fragment.TaskFragment;
import com.sven.huinews.international.main.video.adapter.VideoCommentAdapter;
import com.sven.huinews.international.main.video.contract.FragmentVideoComents;
import com.sven.huinews.international.main.video.fragment.FirstVideoFragment;
import com.sven.huinews.international.main.video.model.VideoComentsModel;
import com.sven.huinews.international.main.video.presenter.VideoComentsPresenter;
import com.sven.huinews.international.main.web.PersonalJs;
import com.sven.huinews.international.main.web.WebActivity;
import com.sven.huinews.international.tplatform.facebook.FaceBookShare;
import com.sven.huinews.international.tplatform.facebook.FacebookPlatform;
import com.sven.huinews.international.tplatform.google.GoogleLogin;
import com.sven.huinews.international.tplatform.linkedin.LinkedInPlatform;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.utils.ActivityManager;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.ImageUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.ScreensUitls;
import com.sven.huinews.international.utils.StatusBarUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.utils.db.ReadNewsDb;
import com.sven.huinews.international.utils.update.UpdateInfo;
import com.sven.huinews.international.view.EdittextUtlis;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.EnterLoginDialog;
import com.sven.huinews.international.view.MyRefreshLayout;
import com.sven.huinews.international.view.RefreshButtom;
import com.sven.huinews.international.view.ReportDialog;
import com.sven.huinews.international.view.ShotDialog;
import com.sven.huinews.international.view.TimeService;
import com.sven.huinews.international.view.dialog.CustomLoginDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import rx.Observer;
import rx.functions.Action1;
import wedemo.MessageEvent;
import wedemo.MusicActivity;
import wedemo.ShotActivity;
import wedemo.utils.Constants;
import wedemo.utils.TimelineManager;

public class MainActivity extends BaseActivity<VideoComentsPresenter, VideoComentsModel> implements FragmentVideoComents.View
        , FirstVideoFragment.OnComentViewLisenter, GoogleApiClient.OnConnectionFailedListener, GoogleLogin.GoogleSignListener {
    private LinearLayout ll_foryou, ll_follow, ll_video, ll_me, video_comment_ll, ll_bottom;
    private RefreshButtom img_foryou;
    private ImageView img_follow, img_video, img_me, iv_shoot, iv_tips, img_shot_toast;
    private TextView tv_foryou, tv_follow, tv_video, tv_me, btn_send, tv_comment_count, tvMsg, et_video_comment;
    private List<TextView> item_bottom_tv;
    private ArrayList<Fragment> mMainFragments = new ArrayList<>();
    private Fragment mVideoAndNewsFragment, mFollowFragment, mMainPersonFragment, myVideoFragment;
    private FragmentTransaction fragmentTransaction;
    private EmptyLayout comment_empty;
    private RecyclerView comment_rv;
    private MyRefreshLayout refresh_view;
    private ImageButton btn_close;
    private MyNews myNews;
    private Animation animationIn, animationOut;
    private int pageIndex = 1;
    private ShotDialog mShotDialog;
    private String etComment, du_type, mCommentId, videoId;
    private ReportDialog reportDialog; //举报
    private VideoCommentAdapter mVideoCommentAdapter;//评论Adapter
    //活动
    private long delayMillis = 2000;//首页活动推送弹框
    private OtherActivityDialog mOtherActivityDialog;
    private PersonalJs mPersonalJs;
    private TimeService timeService;
    private Boolean isLogin = false;
    private UpdateInfo mUpdateInfo;
    private RelativeLayout topComment;
    private Window window;
    private CustomLoginDialog loginDialog;
    private FacebookPlatform mFacebookPlatform;
    private TwitterLogin mTwitterLogin;
    @SuppressLint("StaticFieldLeak")
    public static GoogleLogin googleLogin;
    private FacebookRegResponse mFacebookRegResponse;
    private TwitterRegResponse mTwitterRegResponse;
    private GoogleSignInAccount mGoogleLogin;
    private FaceBookShare mFaceBookShare;
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
    Timer timer = new Timer();
    private GoldComeDialog mGoldComeDialog;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timeService = ((TimeService.MyBinder) service).getService();
            timeService.startCounter();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timeService = null;
        }
    };
    private MyNews shareBean;

    @Override
    public int getLayoutId() {
        ActivityManager.getInstance().finishAllActivity();
        ActivityManager.getInstance().pushOneActivity(this);

        initNvs();
        return R.layout.activity_main;
    }

    private void initNvs() {
        String path = Constants.SDK_LIC;
        // path = null;
        NvsStreamingContext.init(this, path, NvsStreamingContext.STREAMING_CONTEXT_FLAG_SUPPORT_4K_EDIT);

        //NvsStreamingContext.init(this, null, NvsStreamingContext.STREAMING_CONTEXT_FLAG_SUPPORT_4K_EDIT);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mVideoAndNewsFragment = new VideoAndNewsFragment();
        mFollowFragment = new FollowFragment();
        mMainPersonFragment = new MeFragment();
        myVideoFragment = new TaskFragment();
        //布局
        ll_foryou = findViewById(R.id.ll_foryou);
        ll_follow = findViewById(R.id.ll_follow);
        ll_video = findViewById(R.id.ll_video);
        ll_me = findViewById(R.id.ll_me);

        //评论相关
        video_comment_ll = findViewById(R.id.video_comment_ll);
        comment_rv = findViewById(R.id.comment_rv);
        comment_empty = findViewById(R.id.comment_empty);
        tv_comment_count = findViewById(R.id.tv_comment_count);
        et_video_comment = findViewById(R.id.et_video_comment);
        topComment = findViewById(R.id.topComment);

        refresh_view = findViewById(R.id.refreshView);
        btn_close = findViewById(R.id.btn_close);
        ll_bottom = findViewById(R.id.ll_bottom);
        //图片
        iv_shoot = findViewById(R.id.iv_shoot);
        img_foryou = findViewById(R.id.img_foryou);
        img_follow = findViewById(R.id.img_follow);
        img_video = findViewById(R.id.img_video);
        img_me = findViewById(R.id.img_me);

        //文字
        tv_foryou = findViewById(R.id.tv_foryou);
        tv_follow = findViewById(R.id.tv_follow);
        tv_video = findViewById(R.id.tv_video);
        tv_me = findViewById(R.id.tv_me);
        btn_send = findViewById(R.id.btn_send);
        iv_tips = findViewById(R.id.iv_tips);

       // googleLogin = new GoogleLogin(MainActivity.this, this);
        loginDialog = new CustomLoginDialog(MainActivity.this,this);
        item_bottom_tv = new ArrayList<>();
        item_bottom_tv.add(tv_foryou);
        item_bottom_tv.add(tv_follow);
        item_bottom_tv.add(tv_video);
        item_bottom_tv.add(tv_me);

        mMainFragments.add(mVideoAndNewsFragment);
        mMainFragments.add(mFollowFragment);
        mMainFragments.add(myVideoFragment);
        mMainFragments.add(mMainPersonFragment);

        //tempLogin();
        mShotDialog = new ShotDialog();

        //活动
        mOtherActivityDialog = new OtherActivityDialog(AppConfig.getAppContext());
//        window = mOtherActivityDialog.getWindow();
//        window.setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        window.setAttributes(lp);
        mOtherActivityDialog.setOnOpenBagListener(new OtherActivityDialog.OnOpenBagListener() {
            @Override
            public void onOpen(int typePage) {
                //跳转网页
                mPersonalJs.openNewWebPage(otherActivityUrl);
            }
        });

        initGuide();

        initTopShare();
    }

    private LinearLayout top_share;
    private TextView toast_share_facebok_zoom_tv;
    private TextView toast_share_twitter_tv;
    private ImageButton iv_close;

    private void initTopShare() {
        top_share = findViewById(R.id.top_share);
        toast_share_facebok_zoom_tv = findViewById(R.id.toast_share_facebok_zoom_tv);
        toast_share_twitter_tv = findViewById(R.id.toast_share_twitter_tv);
        iv_close = findViewById(R.id.iv_close);
    }


    private void initGuide() {
        final GuidePage page = GuidePage
                .newInstance()
                .setLayoutRes(R.layout.info_main_top)
                .addHighLight(findViewById(R.id.tv_guid_tip))
                .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                    @Override
                    public void onLayoutInflated(View view, final Controller controller) {
                        view.findViewById(R.id.tv_know).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (controller != null) {
                                    controller.showPage(1);
                                }
                            }
                        });
                    }
                });

        final GuidePage page1 = GuidePage
                .newInstance()
                .setLayoutRes(R.layout.info_main_bottom_1)
                .addHighLight(iv_shoot, HighLight.Shape.CIRCLE)
                .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                    @Override
                    public void onLayoutInflated(View view, final Controller controller) {
                        view.findViewById(R.id.tv_know).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (controller != null) {
                                    controller.showPage(2);
                                }
                            }
                        });
                    }
                });

        final GuidePage page2 = GuidePage
                .newInstance()
                .setLayoutRes(R.layout.info_main_bottom_2)
                .addHighLight(ll_video, HighLight.Shape.CIRCLE)
                .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                    @Override
                    public void onLayoutInflated(View view, final Controller controller) {
                        view.findViewById(R.id.tv_know).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (controller != null) {
                                    controller.remove();
                                }
                            }
                        });
                    }
                });

        NewbieGuide.with(this)
                .setLabel("guide")
                .alwaysShow(Constants.GUIDE_TEST)//总是显示，调试时可以打开
                .addGuidePage(page)
                .addGuidePage(page1)
                .addGuidePage(page2)
                .show();

        img_shot_toast = findViewById(R.id.img_shot_toast);
    }


    @Override
    public void initEvents() {
        ll_foryou.setOnClickListener(this);
        ll_follow.setOnClickListener(this);
        ll_video.setOnClickListener(this);
        ll_me.setOnClickListener(this);
        iv_shoot.setOnClickListener(this);
        topComment.setOnClickListener(this);
        toast_share_facebok_zoom_tv.setOnClickListener(this);
        toast_share_twitter_tv.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        mShotDialog.onPublishDilaogClickLisenter(new ShotDialog.onPublishDilaogClickLisenter() {
            @Override
            public void dialogCancleClick(final int index) {

                if (Constants.UPLOAD_TEST) {
                    EventBus.getDefault().post(new MessageEvent("test", "finish"));
                }

                new RxPermissions(MainActivity.this)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    if (index == 1) {

                                        Intent intent = new Intent(mContext, MusicActivity.class);
                                        intent.putExtra("intype", 3);
                                        startActivity(intent);

                                        //ShotActivity.toThis(MainActivity.this, 1);
                                    } else if (index == 2) {
                                        ShotActivity.toThis(MainActivity.this, 2);
                                    }
                                } else {
                                    ToastUtils.showShort(MainActivity.this, R.string.camera_permission);
                                }
                            }
                        });

            }
        });
        btn_close.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        et_video_comment.setOnClickListener(this);


//        //点赞回调
        mVideoCommentAdapter.setOnLikeListener(new VideoCommentAdapter.OnLikeListener() {
            @Override
            public void onLikeChange(Comment comment) {
                mCommentId = comment.getId();
                mPresenter.onVideoCommentLike(getVideoCommentLikes());
            }
        });

        //举报
        mVideoCommentAdapter.setItemOnLongClickLisenter(new VideoCommentAdapter.OnLongClickLisenter() {
            @Override
            public void onLongClick(final Comment comment) {

                if (reportDialog == null) {
                    reportDialog = new ReportDialog(mContext);
                }
                reportDialog.show();
                reportDialog.setOnReportLisenter(new ReportDialog.onReportLisenter() {
                    @Override
                    public void report() {
                        mCommentId = comment.getId();
                        mPresenter.onVideoCommentReport(getTipOffRequest());
                        reportDialog.dismiss();
                    }
                });
            }

        });

        refresh_view.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                isRefresh = false;
                PAGE += 1;
                //评论上拉加载
                pageIndex = PAGE;
                mPresenter.getVideoComents(getVideoComentsRequest(du_type, PAGE, videoId, Constant.COMMENT_UP));
            }
        });

        refresh_view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                isRefresh = true;
                PAGE = 1;
                //评论刷新
                mPresenter.getVideoComents(getVideoComentsRequest(du_type, PAGE, videoId, Constant.COMMENT_UP));
            }
        });


        enterLoginDialog.setOnLoginLisenter(new EnterLoginDialog.OnLoginLisenter() {
            @Override
            public void toLogin() {
                enterLoginDialog.dismiss();
                loginDialog.show();
            }
        });

    }

    @Override
    public ThirdRequest getThirdRequest(String s) {
        ThirdRequest request = new ThirdRequest();
        if (s.equals(Constant.FACEBOOK)) {
            request.setMobile_brand(PhoneUtils.getPhoneBrand());
            request.setHeadimg(mFacebookRegResponse.getHeadImg());
            request.setNickname(mFacebookRegResponse.getNickName());
            request.setFb_id(mFacebookRegResponse.getFb_id());
            request.setFb_access_token(mFacebookRegResponse.getFb_access_token());
            request.setSex(mFacebookRegResponse.getSex().equals("male") ? "1" : "2");
            request.setLogin_source(Constant.FACEBOOK);
        } else if (s.equals(Constant.TWITTER)) {
            request.setMobile_brand(PhoneUtils.getPhoneBrand());
            request.setHeadimg(mTwitterRegResponse.getHeadImg());
            request.setNickname(mTwitterRegResponse.getNickName());
            request.setTwitter_id(mTwitterRegResponse.getTwitter_id());
            request.setSex(mTwitterRegResponse.getSex().equals("male") ? "1" : "2");
            request.setLogin_source(Constant.TWITTER);
        } else if (s.contains(Constant.GOOGLELOGIN)) {
            request.setMobile_brand(PhoneUtils.getPhoneBrand());
            request.setHeadimg(mGoogleLogin.getPhotoUrl() == null ? "123" : mGoogleLogin.getPhotoUrl() + "");
            request.setNickname(mGoogleLogin.getDisplayName());
            request.setGm_id(mGoogleLogin.getId());
            request.setSex("1");
            request.setLogin_source(Constant.GOOGLELOGIN);
        }
        return request;
    }

    @Override
    public void loginSucceed() {
        hideLoading();
        loginDialog.dismiss();
        ToastUtils.showShort(mContext, getString(R.string.logged_in));
    }

    //
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClickEvent(View v) {
        if (v == ll_foryou) {
            choseToMenuItem(0);
        } else if (v == ll_follow) {
            if (isLogin()) {
                choseToMenuItem(1);
            }
        } else if (v == ll_video) {
            //if (isLogin()) {
            choseToMenuItem(2);
            // }
        } else if (v == ll_me) {
            choseToMenuItem(3);
        } else if (v == btn_send) {
            sendComment();
        } else if (v == topComment) {
            hideCommentView();
        } else if (v == btn_close) {
            hideCommentView();
        } else if (v == et_video_comment) {
            setEtListener();
        } else if (v == iv_shoot) {
            //重置草稿箱数据库缓存id
            TimelineManager.getInstance().setCacheId(-1);
            if (Constants.SHARE_TEST) {
                starShow();
            } else {
                mShotDialog.dialogShow(getFragmentManager(), "");
            }
        } else if (v == toast_share_facebok_zoom_tv) {
            mPresenter.getVideoShareUrl(getVideSharedRequest(Common.SHARE_TYPE_FACEBOOK), Common.SHARE_TYPE_FACEBOOK);
        } else if (v == toast_share_twitter_tv) {
            mPresenter.getVideoShareUrl(getVideSharedRequest(Common.SHARE_TYPE_TWITTER), Common.SHARE_TYPE_TWITTER);
        } else if (v == iv_close) {
            endShow();
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
        if (mPresenter.isNotCurrentDataTime()) {
            ReadNewsDb.getInstance(getApplicationContext()).deleteDb();
        }
        choseToMenuItem(0);
        //当fragment与activity绑定时初始化评论区
        setVideoCommentInfo();
        mPresenter.getTaskPush();
        mPersonalJs = new PersonalJs(this);
        isLogin = mUserSpCache.getBoolean(Constant.KEY_IS_USER_LOGIN);

        Intent bindIntent = new Intent(MainActivity.this, TimeService.class);
        if (isLogin) {
            bindService(bindIntent, connection, BIND_AUTO_CREATE);
        }//此时使用bindService开启服务
        updateApk();
    }

    private void updateApk() {
        if (mUpdateInfo == null) {
            mUpdateInfo = new UpdateInfo(this);
        }
        if (permissionWRITE()) {
            mUpdateInfo.setPermission(true);
        } else {
            mUpdateInfo.setPermission(false);
        }
        mUpdateInfo.getVersionInfo(true);

        mUpdateInfo.setCheckPermissionLinsenter(new UpdateInfo.checkPermissionLinsenter() {
            @Override
            public void checkPermission() {
                MainActivity.this.checkPermission();
            }
        });
    }

    private boolean permissionWRITE() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//自定义的code
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (permissionWRITE()) {
                mUpdateInfo.setPermission(true);
                mUpdateInfo.setClickUptate(true);
                mUpdateInfo.uploadApk();
                // updateInfo.getVersionInfo(true);
            }
        } else {
            checkPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setVideoCommentInfo() {
        comment_rv.setLayoutManager(new LinearLayoutManager(mContext));
        mVideoCommentAdapter = new VideoCommentAdapter(mContext);
        comment_rv.setAdapter(mVideoCommentAdapter);

    }

    /**
     * 选中
     * by burgess
     */

    @SuppressLint("NewApi")
    private void choseToMenuItem(int pos) {
        backToAllBg();
        EventBus.getDefault().removeStickyEvent(new Progress());
        switch (pos) {
            case 0:
                img_foryou.setBackground(ContextCompat.getDrawable(this, R.drawable.foryou_background));
                break;
            case 1:
                img_follow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.follow));
                break;
            case 2:
                img_video.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.video));
                break;
            case 3:
                img_me.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.me));
                break;
        }
        item_bottom_tv.get(pos).setTextColor(Color.parseColor("#6FB5C5"));
        switchFragment(pos);
    }

    @SuppressLint("NewApi")
    private void backToAllBg() {
        for (int i = 0; i < item_bottom_tv.size(); i++) {
            item_bottom_tv.get(i).setTextColor(Color.parseColor("#999999"));
        }
        img_foryou.setBackground(ContextCompat.getDrawable(this, R.drawable.foryou_background_nu));
        img_follow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.follow_un));
        img_video.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.video_nu));
        img_me.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.me_nu));
    }

    private void switchFragment(int pos) {
        img_shot_toast.setVisibility(View.INVISIBLE);
        VideoViewManager.instance().releaseVideoPlayer();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mMainFragments.size(); i++) {
            Fragment fragment = mMainFragments.get(i);
            if (pos == i) {
                if (fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(R.id.new_main_content_frame_layout, fragment);
                }
                //if (i != 3) {
                StatusBarUtils.setColor(MainActivity.this, Color.parseColor("#FFFFFF"));
//                } else {
//                    StatusBarUtils.setColor(MainActivity.this, Color.parseColor("#6FB5C5"));
//                }
                if (i == 1) {
                    ((FollowFragment) fragment).initdata();
                }
                if (i == 2) {
                    // ((TaskFragment) fragment).initdata();
                    EventBus.getDefault().post(new RefreshTaskEvent());
                }
                if (i == 3) {
                    ((MeFragment) fragment).initdata();
                }


            } else {
                if (fragment.isAdded()) {
                    fragmentTransaction.hide(fragment);
                }
            }
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
    }

    // 要申请的权限
    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private void checkPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions).subscribe(new Observer<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                LogUtil.showLog("msg---checkPermissions:onNext");
            }

            @Override
            public void onCompleted() {
                LogUtil.showLog("msg---checkPermissions:onCompleted");
                // LocationUtils locationUtils = new LocationUtils(mContext);
                // locationUtils.init();
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.showLog("msg---checkPermissions:onError");
            }
        });
    }

    //请求评论数据
    @Override
    public void showCommentView(MyNews mData, TextView tvmsg) {
        this.myNews = mData;
        this.tvMsg = tvmsg;
        isRefresh = true;
        if (PAGE > 1) {
            PAGE = 1;
        }
        if (ll_bottom.getVisibility() == View.VISIBLE) {
            ll_bottom.setVisibility(View.GONE);
        }
        comment_empty.setErrorType(EmptyLayout.LOADING, -1);
        if (animationIn == null) {
            animationIn = android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.movie_count_in);
        }
        video_comment_ll.startAnimation(animationIn);
        if (mVideoCommentAdapter != null) {
            mVideoCommentAdapter.clearData();
        }
        video_comment_ll.setVisibility(View.VISIBLE);

        tv_comment_count.setText(getString(R.string.comment) + ":" + mData.getCommentCount());
        videoId = mData.getId();

        du_type = mData.getDu_type();
        //根据视频id获取评论数据
        mPresenter.getVideoComents(getVideoComentsRequest(du_type, PAGE, videoId, Constant.COMMENT_UP));

    }

    @Override
    public void hideCommentView() {
        if (animationOut == null) {
            animationOut = android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.movie_count_out);
        }
        video_comment_ll.startAnimation(animationOut);//开始动画
        video_comment_ll.setVisibility(View.GONE);
        ll_bottom.setVisibility(View.VISIBLE);
    }

    @Override
    public VideoCommentRequest getVideoComentsRequest(String du_type, int page, String videoId, String sort) {
        VideoCommentRequest request = new VideoCommentRequest();
        request.setId(videoId + "");
        request.setPage(PAGE + "");
        request.setSize(LIMIT + "");
        request.setDu_type(du_type);
        request.setOrder(sort);
        return request;
    }

    @Override
    public TipOffRequest getTipOffRequest() {
        TipOffRequest tipOffRequest = new TipOffRequest();
        tipOffRequest.setDu_type(du_type);
        tipOffRequest.setCid(mCommentId);
        tipOffRequest.setVideo_id(videoId);
        return tipOffRequest;
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
    public VideoCommentLikeRequest getVideoCommentLikes() {
        VideoCommentLikeRequest request = new VideoCommentLikeRequest();
        request.setCid(Integer.parseInt(mCommentId));
        request.setDu_type(Integer.parseInt(du_type));
        request.setVideo_id(videoId);
        return request;
    }

    @Override
    public void setVideoComent(CommentReponse videoComent) {
        if (isRefresh) {
            if (videoComent.getData().size() > 0) {
                comment_empty.setVisibility(View.GONE);
            } else {
                comment_empty.setVisibility(View.VISIBLE);
                comment_empty.setErrorType(EmptyLayout.NO_DATA, EmptyLayout.NO_COMMENT);
            }
            refresh_view.finishRefresh();
        } else {
            if (videoComent.getData().size() <= 0) {
                ToastUtils.showLong(mContext, getString(R.string.no_more_datas));//No More Datas
            }
            PAGE = pageIndex;
            refresh_view.finishLoadmore();
        }
        mVideoCommentAdapter.setData(videoComent.getData(), isRefresh);
    }

    @Override
    public void setAddComents(ComentsResponse comentsResponse) {
        //添加评论成功刷新进行显示
        if (comentsResponse.getData().getGold() > 0) {
            ToastUtils.showGoldCoinToast(MainActivity.this, getString(R.string.commentary_award)
                    , "+"+comentsResponse.getData().getGold());
            EventBus.getDefault().post(new RefreshTaskEvent());
        }
//        ToastUtils.showShort(mContext, getString(R.string.successful));//Successful~
        mPresenter.getVideoComents(getVideoComentsRequest(du_type, 1, videoId, Constant.COMMENT_TIME));
        myNews.setCommentCount(myNews.getCommentCount() + 1);
        tv_comment_count.setText(getString(R.string.comments) + CommonUtils.getLikeCount(myNews.getCommentCount()));
        tvMsg.setText(CommonUtils.getLikeCount(myNews.getCommentCount()));
    }

    Handler handler1 = new Handler();
    String otherActivityUrl = "";

    @Override
    public void showOtherTask(PushTaskResponse.DataBean task) {
//        //将数据存入内存中
        mUserSpCache.putPageListBean(task.getPageList());
        int tips_service_count = task.getMaxId();
        LogUtil.showLog("tips_service_count:" + tips_service_count);
        UserSpCache.getInstance(mContext).putInt(UserSpCache.TIPS_FLAG, tips_service_count);
        int tips_local_count = UserSpCache.getInstance(mContext).getInt(UserSpCache.TIPS_LOCAL_FLAG);
        if (tips_local_count <= 0) {
            UserSpCache.getInstance(mContext).putInt(UserSpCache.TIPS_LOCAL_FLAG, 0);
        } else {
            UserSpCache.getInstance(mContext).putInt(UserSpCache.TIPS_LOCAL_FLAG, tips_local_count);
        }

        if (tips_local_count != -1 && tips_service_count > tips_local_count) {
            iv_tips.setVisibility(View.VISIBLE);
        } else {
            iv_tips.setVisibility(View.GONE);
        }

        if (task.getType() == 2 && !task.getActive_push().getPicUrl().equals("") && !mOtherActivityDialog.isShowing()) {
            /**
             * 其他活动任务
             * */
            otherActivityUrl = task.getActive_push().getRedirect();
            ImageUtils.loadImgGetResult(this, task.getActive_push().getPicUrl(), new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(final Drawable resource, Transition<? super Drawable> transition) {
                    //延迟弹出
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mOtherActivityDialog.showDialog(resource);
                        }
                    }, delayMillis);
                }
            });
        }
    }

    @Override
    public VideoShareUrlRequest getVideSharedRequest(int type) {
        VideoShareUrlRequest request = new VideoShareUrlRequest();
        request.setType(type + "");
        request.setVideoId(shareBean.getId() + "");
        return request;
    }

    //分享


    @Override
    public void setSharedVideoUrl(VideoShareUrlResponse videoShareUrlResponse, int type) {
        shareBean.setVideoUrl(videoShareUrlResponse.getData().getUrl());

        String shareJson = mPresenter.getShareJson(shareBean, type);
        final JsShareType jsShareType = new Gson().fromJson(shareJson, JsShareType.class);
        if (type == Common.SHARE_TYPE_TWITTER) {
            if (mTwitterLogin == null) mTwitterLogin = new TwitterLogin();
            mTwitterLogin.setTwitterShareLisenter(new TwitterLogin.TwitterShareLisenter() {
                @Override
                public void getShareOk(String response) {
                    //ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                    //分享成功后计数
                    mPresenter.shareVisit(response, jsShareType.getActivity_type(), jsShareType.getType(), shareBean.getId(), shareBean.getDu_type());
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
            mTwitterLogin.twitterShare(MainActivity.this, jsShareType, Common.TWITTER_SHARE_IAMGE);
        } else if (type == Common.SHARE_TYPE_FACEBOOK) {
            if (mFaceBookShare == null)

                mFaceBookShare = new FaceBookShare(MainActivity.this, new FacebookCallback() {
                    @Override
                    public void onSuccess(Object o) {
                       // ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                        mPresenter.shareVisit(CommonUtils.getShareSuccesResponse(), jsShareType.getActivity_type(), jsShareType.getType(), shareBean.getId(), shareBean.getDu_type());
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


            mFaceBookShare.shareVideo(jsShareType);
        } else if (type == Common.SHARE_TYPE_LINKEDIN) {
            LinkedInPlatform mLinkedInPlatform = new LinkedInPlatform(MainActivity.this);
            mLinkedInPlatform.linkedInShareLisenter(new LinkedInPlatform.linkedInShareLisenter() {
                @Override
                public void getShareOk(String response) {
                   // ToastUtils.showShort(mContext, getString(R.string.sharedSuccess));
                    mPresenter.shareVisit(CommonUtils.getShareSuccesResponse(), jsShareType.getActivity_type(), jsShareType.getType(), shareBean.getId(), shareBean.getDu_type());
                }

                @Override
                public void getShareFail(String response) {
                    ToastUtils.showShort(mContext, getString(R.string.sharedFialed));
                }
            });
            mLinkedInPlatform.linkedInShare(jsShareType);
        }
    }

    public void faceBookLogin() {
        if (mFacebookPlatform == null) {
            mFacebookPlatform = new FacebookPlatform(MainActivity.this);
        }

        mFacebookPlatform.login();

        mFacebookPlatform.setFacebookListener(new FacebookPlatform.FacebookListener() {

            @Override
            public void facebookLoginSuccess(PlatformLogin platformLogin, FacebookRegResponse response) {
                showBaseLoading("");
                mFacebookRegResponse = response;
//                mPresenter.checkLogin(platformLogin, Api.TYPE_FACEBOOK);

                mPresenter.checkLogin(Constant.FACEBOOK);
            }

            @Override
            public void facebookLoginFail() {
                ToastUtils.showShort(mContext, getString(R.string.sendCodeerror));
            }
        });
    }

    private void twitterLogin() {
        if (!CommonUtils.isApplicationAvilible(MainActivity.this, "com.twitter.android")) {
            ToastUtils.showShort(MainActivity.this, mContext.getString(R.string.you_not_installed_twitter));
            return;
        }
        if (mTwitterLogin == null) {
            mTwitterLogin = new TwitterLogin();
        }

        mTwitterLogin.loginTwitter(MainActivity.this, new TwitterLogin.titterLoginCallback() {
            @Override
            public void onSuccess(TwitterRegResponse data) {
                showLoading();
                mTwitterRegResponse = data;
                mPresenter.checkLogin(Constant.TWITTER);
            }

            @Override
            public void onFailure() {
                LogUtil.showLog("msg---Tiwtter:onFailure");

            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        ToastUtils.showShort(this, msg);
    }


    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (video_comment_ll.getVisibility() == View.VISIBLE) {
                hideCommentView();
                return true;
            }
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showLong(mContext, getString(R.string.click_once_more_to_exit));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void finishByLogView(FinishMainEvent event) {
//        //  finish();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (isLogin) {
            unbindService(connection);
            mUserSpCache.putInt("counter", 0);
        }
        timer.cancel();

    }

    public boolean isLogin() {
        if (!mUserSpCache.getBoolean(Constant.KEY_IS_USER_LOGIN)) {
//            startActivity(LoginActivity.class);
            loginDialog.show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * JS调用 eventbus处理
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openLoginPageEvent(OpenLoginPageEvent event) {
//        startActivity(LoginActivity.class);
        loginDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenNewPageEvent(OpenNewPageEvent event) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WEB_URL, event.getUrl());
        startActivity(WebActivity.class, bundle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToMainPageEvent(ToMainPageEvent event) {
        EventBus.getDefault().removeStickyEvent(new ToMainPageEvent());
        choseToMenuItem(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent event) {
        startActivity(MainActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToMePageEvent(ToMePageEvent event) {
        EventBus.getDefault().removeStickyEvent(new ToMePageEvent());
        choseToMenuItem(2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showJsGoToRarn(JsGoToRarn jsGoToRarn) {
        startActivity(EarnActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshTips(String str) {
        if (str.equals(Common.REFRESH_TIPS)) {
            iv_tips.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpload(MessageEvent event) {
        if (event.getMessage().equals(Common.REFRESH_VIDEO)) {
            shareBean = (MyNews) event.getData();
//            initShare(this);
            if (shareBean != null) {
//                mCustomShareDialog.show();
                starShow();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTaskEvent(MessageEvent event) {

        if (event.getMessage().equals(Common.SELECT_FRAGMENT)) {
            choseToMenuItem((int) event.getData());
        } else if (event.getMessage().equals(Common.SELECT_SHOT)) {
            //重置草稿箱数据库缓存id
            TimelineManager.getInstance().setCacheId(-1);

            mShotDialog.dialogShow(getFragmentManager(), "");
        }
    }

    private boolean isCancel = false;
    private final int SHARE_SHOW_TIME = 6 * 1000;

    private void starShow() {
        isCancel = false;
        top_share.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(top_share, "translationY", -ScreensUitls.dip2px(100), 0f);
        animator.setDuration(300);
        animator.start();
        top_share.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isCancel) {
                    endShow();
                }
            }
        }, SHARE_SHOW_TIME);
    }

    private void endShow() {
        isCancel = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(top_share, "translationY", 0f, -ScreensUitls.dip2px(100));
        animator.setDuration(300);
        animator.start();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        loginDialog.registerCallBack(requestCode,resultCode,data,false);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f instanceof MeFragment) {
                f.onActivityResult(requestCode, resultCode, data);
            }
            if (f instanceof TaskFragment) {
                f.onActivityResult(requestCode, resultCode, data);
            }

            if (f instanceof FirstVideoFragment) {
                f.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void googleLoginSuccess(GoogleSignInAccount acct) {
        mGoogleLogin = acct;
        mPresenter.checkLogin(Constant.GOOGLELOGIN);
    }

    @Override
    public void googleLoginFail() {

    }
}
