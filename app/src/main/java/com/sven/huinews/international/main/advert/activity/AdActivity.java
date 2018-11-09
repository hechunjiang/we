package com.sven.huinews.international.main.advert.activity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.internal.InternalTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.requst.PushTokenRequest;
import com.sven.huinews.international.main.advert.contract.AdvertContract;
import com.sven.huinews.international.main.advert.model.AdvertModel;
import com.sven.huinews.international.main.advert.presenter.AdvertPresenter;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.main.login.activity.LoginActivity;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.NetWorkUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.utils.statusbar.Eyes;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class AdActivity extends BaseActivity<AdvertPresenter, AdvertModel> implements AdvertContract.View {
    private UserSpCache mUserSpCache;
    private TextView tvCountDown;
    private CountTimeUtils mCountTimeUtils;
    private Boolean isTimeDown = false;
    private Boolean isNeedLogin = false;
    private int count = 1;
    private ImageView gif_logo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ad;
    }

    @Override
    public void initView() {
        Eyes.translucentStatusBar(this, true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
        hideBottomUIMenu();
        mCountTimeUtils = new CountTimeUtils(4000, 1000);
        mUserSpCache = UserSpCache.getInstance(getApplicationContext());
        tvCountDown = findViewById(R.id.ad_count_time_tv);
        gif_logo = findViewById(R.id.gif_logo);
        tvCountDown.setVisibility(View.VISIBLE);
        mCountTimeUtils.start();
        Glide.with(this).load(R.drawable.homepagelog).into(gif_logo);

        new RxPermissions(this).request(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            initLocation();
                        }
                    }
                });

    }

    @Override
    public void initObject() {
        setMVP();
        /**
         * 检查是否临时登录
         */
        if (!mUserSpCache.getBoolean(UserSpCache.KEY_IS_USER_LOGIN)) {
            isNeedLogin = true;
            mPresenter.checkIstemp();
        } else {//推送
            mPresenter.pushToken();
        }

        if (!TextUtils.isEmpty(mUserSpCache.getStringData(UserSpCache.KEY_PHONE)) && TextUtils.isEmpty(ACache.get(AdActivity.this).getAsString(UserSpCache.KEY_PHONE))) {
            isNeedLogin = true;
            mPresenter.checkIstemp();
            //登录过期 请重新登录
//            ToastUtils.showShort(AdActivity.this, getString(R.string.please_re_log_in));
        } else {
            ACache.get(AdActivity.this).put(UserSpCache.KEY_PHONE, mUserSpCache.getStringData(UserSpCache.KEY_PHONE), ACache.STORAGE_TIME);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountTimeUtils.cancel();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        isNeedLogin = false;
        if (isTimeDown) {
            toActivity();
        }
        if (!NetWorkUtils.isNetworkAvailable(this)) {
            ToastUtils.showShort(this, getString(R.string.network_down));
        }
    }

    /**
     * 临时登录成功
     *
     * @param s
     */
    @Override
    public void tempLoginSuccess(String s) {
        isNeedLogin = false;
        mPresenter.pushToken();
        if (isTimeDown) {
            toActivity();
        }
    }

    @Override
    public PushTokenRequest onPushTokenRequest() {
        PushTokenRequest request = new PushTokenRequest();
        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (statusCode == ConnectionResult.SUCCESS) {
            request.setToken(FirebaseInstanceId.getInstance().getToken());
            FirebaseMessaging.getInstance().subscribeToTopic("total");
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, 1, 1);
        }
        boolean isLogin = UserSpCache.getInstance(mContext).getBoolean(Constant.KEY_IS_USER_LOGIN);
        FirebaseMessaging.getInstance().subscribeToTopic(isLogin ? "login_in" : "login_no");
        String topic = isLogin ? "total,login_in" : "total,login_no";
        request.setTopic(topic);
        return request;
    }

    private void toActivity() {
        if (isNeedLogin) {
            return;
        }
        if (TextUtils.isEmpty(mUserSpCache.getStringData(UserSpCache.KEY_IS_FIRST_OPEN_APP))) {
            mUserSpCache.putInt(UserSpCache.OPEN_COUNT, 1);
            mUserSpCache.putString(UserSpCache.KEY_IS_FIRST_OPEN_APP, "has_open_app");
            mUserSpCache.putBoolean(UserSpCache.KEY_IS_SHOW_INTRODUCE, true);
            startActivity(MainActivity.class);
            finish();
            return;
        } else {
            saveOpenCount();
            startActivity(MainActivity.class);
            finish();
        }
    }

    private void saveOpenCount() {
        count = mUserSpCache.getInt(UserSpCache.OPEN_COUNT);
        count++;
        UserSpCache.getInstance(this).putInt(UserSpCache.OPEN_COUNT, count);
    }

    private class CountTimeUtils extends CountDownTimer {

        public CountTimeUtils(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvCountDown.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            tvCountDown.setText(getString(R.string.length_0));
            isTimeDown = true;
            toActivity();
        }
    }


    @Override
    public void initEvents() {

    }

    @Override
    public void onClickEvent(View v) {

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
}
