package com.sven.huinews.international.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dueeeke.videoplayer.player.VideoViewManager;
import com.sven.huinews.international.R;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.event.TokenExpireEvent;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.main.web.WebActivity;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.TUtil;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.EnterLoginDialog;
import com.sven.huinews.international.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import wedemo.utils.LocaltionUtils;

/**
 * Created by sfy. on 2018/5/7 0007.
 */

public abstract class BaseActivity<P extends BasePresenter, M extends BaseModel> extends AppCompatActivity implements View.OnClickListener {
    protected Context mContext;
    protected TitleBar mTitleBar;
    protected EmptyLayout mEmptyLayout;
    public P mPresenter;
    public M mModel;
    protected int LIMIT = 20;
    protected int PAGE = 1;
    protected boolean isRefresh = true;
    public Dialog mLoadingDialog;

    private MediaPlayer mediaPlayer;
    protected int statusBarColor = 0;
    protected View statusBarView = null;
    public EnterLoginDialog enterLoginDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //  checkAppStatus();
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        //        Eyes.setStatusBarLightMode(this, getResources().getColor(R.color.colorWhite));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
        setContentView(getLayoutId());
        mContext = this;
        enterLoginDialog = new EnterLoginDialog(this);


        initView();
        initObject();
        initEvents();
        initFontScale();
        showActiviteDialog();
        mLoadingDialog = new Dialog(this, R.style.dialog);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        //       mGoldComeDialog = new GoldComeDialog(this);
        //   mLoadingDialog = new Dialog(this, R.style.dialog);
        //     mLoadingDialog.setContentView(R.layout.dialog_loading);
        //     PushAgent.getInstance(this).onAppStart();
    }

    protected void initLocation() {
        new LocaltionUtils(this, new LocaltionUtils.OnLocationListener() {
            @Override
            public void onLocationSuccess(float lng, float lat) {
                LogUtil.showLog("longitude === " + lng + ",latitude ==== " + lat);
                ACache.get(mContext).put(Constant.CACHE_LAT, lat + "");
                ACache.get(mContext).put(Constant.CACHE_LNG, lng + "");
            }

            @Override
            public void onGetInfoSuccess(String c) {
                LogUtil.showLog("c === " + c);
                if (!TextUtils.isEmpty(c)) {
                    c = c.trim();
                }
                ACache.get(mContext).put(Constant.CACHE_COUNTRY, c);
            }

            @Override
            public void onFail() {
                ACache.get(mContext).put(Constant.CACHE_LAT, 0);
                ACache.get(mContext).put(Constant.CACHE_LNG, 0);
            }
        });
    }

    public void showBaseLoading(String msg) {
        mLoadingDialog.show();
    }


    public void hideBaseLoading() {
        if (mLoadingDialog == null) {
            return;
        }
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        }
    }


    protected int setStatusBarColor() {
        return getColorPrimary();
    }


    /**
     * 获取主题色
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 子类可以重写决定是否使用透明状态栏
     */
    protected boolean translucentStatusBar() {
        return false;
    }


    /**
     * 初始化MVP
     */
    public void setMVP() {
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        mPresenter.setVM(this, mModel);
    }

    /**
     * 获取布局文件
     */
    public abstract int getLayoutId();


    /**
     * 初始化View
     */
    public abstract void initView();

    /**
     * 设置监听
     */
    public abstract void initEvents();

    /**
     * 处理监听事件
     */
    public abstract void onClickEvent(View v);

    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }
    /*********************跳转相关**********************************/
    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 初始化事物
     */

    public abstract void initObject();

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getLocalClassName());
        VideoViewManager.instance().releaseVideoPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getLocalClassName());
    }

    private void initFontScale() {
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = (float) 1;
        //0.85 小, 1 标准大小, 1.15 大，1.3 超大 ，1.45 特大
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }


    private Handler mDialogDisMissHandler = new Handler();


    private void checkAppStatus() {
        /*if (AppStatusManager.getInstance().getAppStatus() == AppStatusManager.AppStatusConstant.APP_FORCE_KILLED) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }*/
    }


    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if (statusBarView != null) {
            statusBarView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if (statusBarView != null) {
            statusBarView.setBackgroundColor(statusBarColor);
        }

    }

    private void showActiviteDialog() {
        if (this instanceof MainActivity) {
            LogUtil.showLog("msg----首页");

        } else if (this instanceof WebActivity) {
            LogUtil.showLog("msg----H5");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokenExpire(TokenExpireEvent expireEvent) {
        if (!UserSpCache.getInstance(getApplicationContext()).getBoolean(UserSpCache.KEY_IS_USER_LOGIN)) {
            return;
        } else {
            UserSpCache.getInstance(getApplicationContext()).clearCache();
            int totalCount = UserSpCache.getInstance(mContext).getInt(UserSpCache.NEEDCOUNT_LOGIN);
            long signTime = UserSpCache.getInstance(mContext).getLong(UserSpCache.SIGN_SERVICE_TIME);
            long localTime = UserSpCache.getInstance(mContext).getLong(UserSpCache.SIGN_LOCAL_TIME);
            int openGoldCount = UserSpCache.getInstance(mContext).getInt(UserSpCache.OPEN_GOLD_COUNT);
            int openRedCount = UserSpCache.getInstance(mContext).getInt(UserSpCache.OPEN_RED_COUNT);
            UserSpCache.getInstance(mContext.getApplicationContext()).clearCache();
            UserSpCache.getInstance(mContext).putString(UserSpCache.KEY_IS_FIRST_OPEN_APP, "has_open_app");
            UserSpCache.getInstance(mContext).putString(UserSpCache.KEY_IS_SECEND_OPEN_APP, "isSecend");
            UserSpCache.getInstance(mContext).putInt(UserSpCache.NEEDCOUNT_LOGIN, totalCount);
            UserSpCache.getInstance(mContext).putLong(UserSpCache.SIGN_SERVICE_TIME, signTime);
            UserSpCache.getInstance(mContext).putLong(UserSpCache.SIGN_LOCAL_TIME, localTime);
            UserSpCache.getInstance(mContext).putInt(UserSpCache.OPEN_GOLD_COUNT, openGoldCount);
            UserSpCache.getInstance(mContext).putInt(UserSpCache.OPEN_RED_COUNT, openRedCount);
        }

        UserSpCache.getInstance(getApplicationContext()).putBoolean(UserSpCache.KEY_IS_USER_LOGIN, false);
        if (enterLoginDialog == null) {
            enterLoginDialog = new EnterLoginDialog(this);
        }
        if (!UserSpCache.getInstance(getApplicationContext()).getBoolean(UserSpCache.KEY_TOKEN_IS_EXPIRE)) {
            UserSpCache.getInstance(getApplicationContext()).clearCache();
            UserSpCache.getInstance(getApplicationContext()).putBoolean(UserSpCache.KEY_TOKEN_IS_EXPIRE, true);
        }
        try {
            enterLoginDialog.show();
        } catch (Exception e) {

        }
    }

}
