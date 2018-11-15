package com.sven.huinews.international.main.task.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dueeeke.videoplayer.player.IjkVideoView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.dialog.FinishTheTaskDialog;
import com.sven.huinews.international.dialog.OpenTheTreasureBoxDialog;

import com.sven.huinews.international.entity.event.LogoutEvent;
import com.sven.huinews.international.entity.event.RefreshTaskEvent;
import com.sven.huinews.international.entity.requst.InfoRequest;
import com.sven.huinews.international.entity.requst.TaskFinishRequest;
import com.sven.huinews.international.entity.requst.TaskListRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.response.InfoResponse;
import com.sven.huinews.international.entity.response.TaskFinishResponse;
import com.sven.huinews.international.entity.response.TaskListNewResponse;
import com.sven.huinews.international.entity.response.TaskListResponse;
import com.sven.huinews.international.main.bindemail.BindEmailActivity;
import com.sven.huinews.international.main.bindemail.BindPhoneActivity;
import com.sven.huinews.international.main.earn.EarnActivity;

import com.sven.huinews.international.main.follow.activity.FollowVideoPlay1Activity;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.main.home.activity.MyVideoActivity;

import com.sven.huinews.international.main.me.FeedbackActivity;
import com.sven.huinews.international.main.me.SettingActivity;
import com.sven.huinews.international.main.novicevideo.VideoPlayerActivity;
import com.sven.huinews.international.main.permsg.PersonActivity;
import com.sven.huinews.international.main.task.TaskTest;
import com.sven.huinews.international.main.task.adapter.TaskAdapter;
import com.sven.huinews.international.main.task.adapter.TimeLineAdapter;
import com.sven.huinews.international.main.task.contract.TaskContract;
import com.sven.huinews.international.main.task.model.TaskListModel;
import com.sven.huinews.international.main.task.presenter.TaskListPresenter;
import com.sven.huinews.international.main.web.WebActivity;
import com.sven.huinews.international.utils.AirpushAdUtils;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.GoogleInterstitialAdsUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.UnityAdUtils;
import com.sven.huinews.international.utils.VungleAdUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.MyRefreshLayout;
import com.sven.huinews.international.view.VerticalTextview;
import com.sven.huinews.international.view.dialog.CustomLoginDialog;
import com.sven.huinews.international.view.dialog.UploadProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wedemo.MessageEvent;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TaskFragment extends BaseFragment<TaskListPresenter, TaskListModel> implements TaskContract.View {

    private RecyclerView rv_task;
    private RecyclerView rv_time_line;
    private TaskAdapter adapter;
    private TimeLineAdapter timeLineAdapter;
    private UploadProgressDialog mDialog;
    private OpenTheTreasureBoxDialog mOpenTheTreasureBoxDialog;
    private LinearLayout ll_open;
    private LinearLayout id_un;
    private TextView tv_time;

    private FrameLayout fAdView;
    private ImageButton close_ad;
    private AdView mAdView;

    //广告
    private TranslateAnimation mShowAction;//显示动画
    private TranslateAnimation mHiddenAction;//隐藏动画
    //airpush广告
    private AirpushAdUtils airpushAdUtils;

    private MyRefreshLayout refresh;

    private CustomLoginDialog loginDialog;
    private ArrayList<String> titleList;
    private Subscription subscribe;

    private GoogleInterstitialAdsUtils mGoogleInterstitialAdsUtils;//Google插页广告

    private VungleAdUtils mVungleAd;//vungle激励视频广告
    //unity广告
    private UnityAdUtils unityAdUtils;

    private FinishTheTaskDialog mFinishTheTaskDialog;//任务完成金币显示

    @Override
    protected int getLayoutResource() {
        EventBus.getDefault().register(this);
        return R.layout.activity_task;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if(subscribe != null && !subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
        }
    }

    @Override
    public void initObject() {
        setMVP();
        emptyLayout.setErrorType(EmptyLayout.LOADING, -1);
        mPresenter.getTaskList(new TaskListRequest());
        //google插页广告
        if (mGoogleInterstitialAdsUtils == null) {
            mGoogleInterstitialAdsUtils = new GoogleInterstitialAdsUtils(getActivity());
        }
        //Vungled 激励视频广告
        if (mVungleAd == null) {
            mVungleAd = new VungleAdUtils(getApplicationContext(), true);
        }
        if (unityAdUtils == null) {
            unityAdUtils = new UnityAdUtils(getActivity());
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(View v) {
        rv_task = v.findViewById(R.id.rv_task);
        refresh = v.findViewById(R.id.refresh);
        refresh.setEnableLoadmore(false);

        adapter = new TaskAdapter(new ArrayList<TaskTest>());
        rv_task.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_task.setAdapter(adapter);

        mDialog = UploadProgressDialog.initGrayDialog(getActivity());
        mDialog.setCancelable(false);
        mDialog.setMessage("");

        loginDialog = new CustomLoginDialog(getContext(), getActivity());

        emptyLayout = v.findViewById(R.id.mEmptyLayout);
    }

    @Override
    public void initEvents() {
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //adapter.removeAllHeaderView();
                mPresenter.getTaskListNew(new TaskListRequest());

            }
        });

        adapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onClick(TaskTest item) {
                if (item.t.getIs_award() == 1) {  //可以领取金币
                    mDialog.show();
                    TaskFinishRequest request = new TaskFinishRequest();
                    request.setId(item.t.getId() + "");
                    request.setDebug(BuildConfig.DEBUG ? "ok" : null);
                    mPresenter.taskFinish(request);
                } else if (item.t.getIs_award() == 0) { //金币不可领取才执行任务跳转
                    String button_url = item.t.getButton_url();
                    LogUtil.showLog("button_url=====" + button_url);
                    if (button_url.startsWith("app:")) {
                        goTask(button_url);
                    } else if (button_url.startsWith("h5:")) {
                        String[] split = button_url.split(":");
                        WebActivity.toThis(mContext, Api.BASE_H5_URL + split[1]);
                    } else if (button_url.startsWith("http")) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(button_url));
                        intent.setAction(Intent.ACTION_VIEW);
                        mContext.startActivity(intent);
                    }
                } else if (item.t.getIs_award() > 2) {
                    String button_url = item.t.getButton_url();
                    if (button_url.startsWith("app:")) {
                        goTask(button_url);
                    }
                }
            }
        });

        loginDialog.setThirdLogin(new CustomLoginDialog.ThirdLoginResult() {
            @Override
            public void onOtherfail() {
                loginDialog.dismiss();
            }

            @Override
            public void onCheckFail(BaseResponse response) {
                loginDialog.dismiss();
            }

            @Override
            public void onLoginSuccess(String json, boolean isBind) {

            }

            @Override
            public void onStartCheck() {

            }

            @Override
            public void onGetGoldSuccess(boolean isBind) {
                LogUtil.showLog("调用bind");
                mPresenter.getTaskListNew(new TaskListRequest());
            }

            @Override
            public void onGetGoldFail() {
                mPresenter.getTaskListNew(new TaskListRequest());
            }

            @Override
            public void onLogin() {

            }
        });
    }

    @Override
    public void OnClickEvents(View v) {

    }

    private VerticalTextview vtv;

    private View getHeader(TaskListResponse.DataBeanX.SignBean signBean, TaskListResponse.DataBeanX.ChestBean chestBean) {

        int signDay = 0;
        View view = getLayoutInflater().inflate(R.layout.item_task_header, (ViewGroup) rv_task.getParent(), false);

        List<TaskListResponse.DataBeanX.SignBean.Day7Bean> day7 = signBean.getDay7();
        if (day7 != null) {
            for (int i = 0; i < day7.size(); i++) {
                if (day7.get(i).getStatus() == 1) {  //0.不可签到 1.签到成功 2.可签到
                    signDay = i;
                }
            }
        }

        rv_time_line = view.findViewById(R.id.tv_time_line);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 7);
        rv_time_line.setLayoutManager(gridLayoutManager);
        timeLineAdapter = new TimeLineAdapter(signBean.getDay7());
        rv_time_line.setAdapter(timeLineAdapter);
        timeLineAdapter.setDay(signDay);

        ll_open = view.findViewById(R.id.ll_open);
        id_un = view.findViewById(R.id.id_un);
        tv_time = view.findViewById(R.id.tv_time);

        fAdView = view.findViewById(R.id.f_ad_view);
        close_ad = view.findViewById(R.id.close_ad);
        mAdView = view.findViewById(R.id.video_banner_adView);
        close_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAdView.startAnimation(mHiddenAction);
                fAdView.setVisibility(View.GONE);
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

        adLoad();

        if (chestBean.getIs() == 0) {  //如果没有开过宝箱
            ll_open.setVisibility(View.VISIBLE);
            id_un.setVisibility(View.GONE);
            ll_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mVungleAd != null && mVungleAd.isTheCacheComplete()) {
                        mVungleAd.setType(VungleAdUtils.COMMONLY);
                        mVungleAd.openNoLoadAd();
                    } else {
                        if (mGoogleInterstitialAdsUtils.isLoad()) {
                            mGoogleInterstitialAdsUtils.showAd(Common.AD_TYPE_GOOGLE_INTERSTITIAL_LOOK, Common.AD_TYPE_GOOGLE_INTERSTITIAL_CLICK);
                        }
                    }
                    mPresenter.getOpenTreasureBox(TaskRequest.TASK_ID_OPEN_BOX);
                    mDialog.show();
                }
            });
        } else {  //已经开过宝箱进入倒计时
            //倒计时
            mPresenter.getGoldTime();
            //点击事件  广告
            id_un.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mVungleAd != null && mVungleAd.isTheCacheComplete()) {
                        mVungleAd.setType(VungleAdUtils.COMMONLY);
                        mVungleAd.openNoLoadAd();
                    } else {
                        if (mGoogleInterstitialAdsUtils.isLoad()) {
                            mGoogleInterstitialAdsUtils.showAd(Common.AD_TYPE_GOOGLE_INTERSTITIAL_LOOK, Common.AD_TYPE_GOOGLE_INTERSTITIAL_CLICK);
                        }
                    }
                }
            });
        }

        vtv = view.findViewById(R.id.vtv);
        //initScroll(vtv);

        return view;
    }

    @SuppressLint("StringFormatInvalid")
    private void initScroll(VerticalTextview mTextview, InfoResponse response) {

        if (mTextview == null) return;
        titleList = new ArrayList<>();

        List<InfoResponse.DataBean> data = response.getData();
        if (data == null || data.size() == 0) {
            return;
        }

        for (InfoResponse.DataBean d : data) {
            titleList.add(getString(R.string.task_info, d.getNickname(), d.getTotal_balance()));
        }

        mTextview.setTextList(titleList);

        mTextview.setText(14, 0, Color.WHITE);//设置属性
        mTextview.setTextStillTime(3000);//设置停留时长间隔
        mTextview.setAnimTime(300);//设置进入和退出的时间间隔
        mTextview.startAutoScroll();

        mTextview.setOnItemClickListener(new VerticalTextview.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

    }


    private void setTimeDown(final TextView tv_time, int time) {

        if(subscribe != null && !subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
        }

        if (time < 0) time = 0;
        final int downTime = time;
         subscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return formatTimeStrWithUs((downTime - aLong.intValue()));
                    }
                })
                .take(time + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("")) {
                            ll_open.setVisibility(View.VISIBLE);
                            id_un.setVisibility(View.GONE);
                            ll_open.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mPresenter.getOpenTreasureBox(TaskRequest.TASK_ID_OPEN_BOX);
                                }
                            });
                        } else {
                            tv_time.setText(s);
                        }
                    }
                });
    }

    private String formatTimeStrWithUs(int secTotal) {
        String result = null;
        if (secTotal > 0) {
            int hour = secTotal / 3600;
            int min = (secTotal % 3600) / 60;
            int sec = (secTotal % 3600) % 60;
            result = to2Str(hour) + ":" + to2Str(min) + ":" + to2Str(sec);
        } else {
            result = "";
        }

        return result;
    }

    private String to2Str(int i) {
        if (i > 9) {
            return i + "";
        } else {
            return "0" + i;
        }
    }


    @Override
    public void setTask(TaskListResponse response) {
        //初始化签到
        adapter.addHeaderView(getHeader(response.getData().getSign(), response.getData().getChest()));
        mPresenter.getInfo(new InfoRequest());
//        TaskListResponse.DataBeanX.DataBean data = response.getData().getData();
//        adapter.setNewData(wapperData(data));
        mPresenter.getTaskListNew(new TaskListRequest());

    }

    @Override
    public void setTask(TaskListNewResponse response) {
        adapter.setNewData(wapperData(response.getData()));
        refresh.finishRefresh();
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void showGoldCome(int count, int type, String masgess) {
        mDialog.cancel();
        if (mOpenTheTreasureBoxDialog == null) {
            mOpenTheTreasureBoxDialog = new OpenTheTreasureBoxDialog(getActivity());
        }
        if (!mOpenTheTreasureBoxDialog.isShowing()) {
            mOpenTheTreasureBoxDialog.setCanceledOnTouchOutside(false);//点击空白处不消失
            mOpenTheTreasureBoxDialog.showDialog(count);
            mPresenter.getGoldTime();
        }
    }

    /**
     * 开宝箱后获取时间
     *
     * @param time
     */
    @Override
    public void showGoldTime(int time) {
        ll_open.setVisibility(View.GONE);
        id_un.setVisibility(View.VISIBLE);
        setTimeDown(tv_time, time);
    }

    @Override
    public void taskFinish(TaskFinishResponse response) {
        mDialog.dismiss();
        refresh.autoRefresh();
        //todo:加金币弹窗
//        ToastUtils.showGoldCoinToast(mContext, response.getMsg(), "+" + response.getData().getGold());
        if (mFinishTheTaskDialog == null) {
            mFinishTheTaskDialog = new FinishTheTaskDialog(getActivity());
        }
        if (!mFinishTheTaskDialog.isShowing()) {
            mFinishTheTaskDialog.setCanceledOnTouchOutside(false);//点击空白处不消失
            mFinishTheTaskDialog.showDialog(response.getData().getGold());
        }
    }

    @Override
    public void getInfo(InfoResponse response) {
        initScroll(vtv, response);
        if (response.getData() != null && response.getData().size() > 0) {
            vtv.setVisibility(View.VISIBLE);
            initScroll(vtv, response);
        } else {
            vtv.setVisibility(View.GONE);
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        mDialog.dismiss();
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
        emptyLayout.setVisibility(View.GONE);
        ToastUtils.showShort(mContext, msg);
        refresh.finishRefresh();
    }

    /**
     * 数据封装
     *
     * @param dataBean
     * @return
     */
    private List<TaskTest> wapperData(List<TaskListNewResponse.DataBean> dataBean) {
        List<TaskTest> list = new ArrayList<>();

        for (TaskListNewResponse.DataBean d : dataBean) {
            TaskTest taskTest = new TaskTest(true, d.getName());

            list.add(taskTest);
            List<TaskListNewResponse.DataBean.ListBean> list1 = d.getList();
            if (list1 != null) {
                for (TaskListNewResponse.DataBean.ListBean listBean : list1) {
                    TaskTest taskTest1 = new TaskTest(listBean);

                    list.add(taskTest1);
                }
            }

        }
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vtv != null && (titleList != null && titleList.size() > 0)) {
            vtv.startAutoScroll();
        }
//        mPresenter.getTaskListNew(new TaskListRequest());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (vtv != null && (titleList != null && titleList.size() > 0)) {
            vtv.stopAutoScroll();
        }
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

    public boolean isLogin() {
        UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
        if (!mUserSpCache.getBoolean(Constant.KEY_IS_USER_LOGIN)) {
            //startActivity(LoginActivity.class);
            loginDialog.show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginDialog.registerCallBack(requestCode, resultCode, data, true);

    }

    private void goTask(String taskUrl) {

        if (TextUtils.isEmpty(taskUrl)) {
            return;
        }

        taskUrl = taskUrl.trim();

        String[] split = taskUrl.split(":");

        if (split.length < 2 || TextUtils.isEmpty(split[1])) {
            return;
        }

        if ("indexHorizontal".equals(split[1])) {  //首页横屏
            EventBus.getDefault().post(new MessageEvent(Common.SELECT_FRAGMENT, 0));
            EventBus.getDefault().post(new MessageEvent(Common.SELECT_TASK, 0));
        } else if ("indexVertical".equals(split[1])) { //首页竖屏
            EventBus.getDefault().post(new MessageEvent(Common.SELECT_FRAGMENT, 0));
            EventBus.getDefault().post(new MessageEvent(Common.SELECT_TASK, 1));
        } else if ("indexNews".equals(split[1])) {  //首页新闻
            EventBus.getDefault().post(new MessageEvent(Common.SELECT_FRAGMENT, 0));
            EventBus.getDefault().post(new MessageEvent(Common.SELECT_TASK, 2));
        } else if ("login".equals(split[1])) {  //登陆
            if (isLogin()) {

            }
        } else if ("register".equals(split[1])) { //注册

        } else if ("bindEmail".equals(split[1])) { //绑定邮箱页面
            startActivity(BindEmailActivity.class);
        } else if ("bindFacebook".equals(split[1])) { //绑定facebook操作   （直接调接口）
            //startActivity(BindEmailActivity.class);

            loginDialog.faceBookLogin(true);

        } else if ("bindTwitter".equals(split[1])) { // 绑定Twitter操作   （直接调接口）
            //startActivity(BindEmailActivity.class);

            loginDialog.twitterLogin(true);
        } else if ("bindLinkedin".equals(split[1])) { //绑定linkedin操作   （直接调接口）
            startActivity(BindEmailActivity.class);
        } else if ("bindIns".equals(split[1])) { //绑定Gmail操作   （直接调接口）
            //startActivity(BindEmailActivity.class);

        } else if ("bindGmail".equals(split[1])) {

            loginDialog.googleLogin(true);
        } else if ("followVideo".equals(split[1])) { // 关注列表页面
            if (isLogin()) {
                startActivity(MyVideoActivity.class);
            }
        } else if ("shot".equals(split[1])) { //拍摄页面
            EventBus.getDefault().post(new MessageEvent(Common.SELECT_SHOT));
        } else if ("workCenter".equals(split[1])) { //作品中心
            if (isLogin()) {
                startActivity(MyVideoActivity.class);
            }
        } else if ("fans".equals(split[1])) { //粉丝列表
            if (isLogin()) {
                startActivity(MyVideoActivity.class);
            }
        } else if ("follows".equals(split[1])) { //关注列表
            if (isLogin()) {
                EventBus.getDefault().post(new MessageEvent(Common.SELECT_FRAGMENT, 1));
            }
        } else if ("likeVideo".equals(split[1])) { //喜欢视频列表

            startActivity(MyVideoActivity.class);

        } else if ("center".equals(split[1])) { //  个人中心
            EventBus.getDefault().post(new MessageEvent(Common.SELECT_FRAGMENT, 3));
        } else if ("update".equals(split[1])) { //修改信息界面
            if (isLogin()) {
                startActivity(PersonActivity.class);
            }
        } else if ("opinion".equals(split[1])) { //意见反馈
            startActivity(FeedbackActivity.class);
        } else if ("frends".equals(split[1])) { // 收徒界面
            startActivity(EarnActivity.class);
        } else if ("setting".equals(split[1])) { // 退出登陆界面
            startActivity(SettingActivity.class);
        } else if ("teachVideo".equals(split[1])) {  //新手教学视频
            startActivity(VideoPlayerActivity.class);
        } else if ("excitingVideo".equals(split[1])) { //观看激励视频
            //Vungle激励视频
            if (mVungleAd != null && mVungleAd.isTheCacheComplete()) {
                mVungleAd.setType(VungleAdUtils.GOLD_COIN);
                mVungleAd.openNoLoadAd();
            } else if(unityAdUtils!=null && unityAdUtils.isReady()){//unity广告
                unityAdUtils.show();
            } else {
                ToastUtils.showLong(getActivity(), getActivity().getString(R.string.ads_later));
            }
        }
    }


    @Subscribe
    public void refreshTask(String msg) {
        if (msg.equals(Common.REFRESH_USERINFO)) {
            LogUtil.showLog("登陆成功,刷新任务大厅");
            mPresenter.getTaskListNew(new TaskListRequest());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshTaskEvent(RefreshTaskEvent refreshTaskEvent) {
        if (mPresenter != null) {
            mPresenter.getTaskListNew(new TaskListRequest());
        }
    }

}
