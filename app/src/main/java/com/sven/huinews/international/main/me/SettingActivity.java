package com.sven.huinews.international.main.me;

import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.event.LogoutEvent;
import com.sven.huinews.international.entity.requst.PushTokenRequest;
import com.sven.huinews.international.main.advert.contract.AdvertContract;
import com.sven.huinews.international.main.advert.model.AdvertModel;
import com.sven.huinews.international.main.advert.presenter.AdvertPresenter;
import com.sven.huinews.international.utils.ActivityManager;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.utils.update.UpdateInfo;
import com.sven.huinews.international.view.dialog.UploadProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

public class SettingActivity extends BaseActivity<AdvertPresenter, AdvertModel> implements AdvertContract.View {

    private TextView tvClearCache, tvUpData, tvLogOut,tv_version;
    private RelativeLayout relativeLayout, checkSetting;
    private UploadProgressDialog mDialog;


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        EventBus.getDefault().post(this);
        tvClearCache = findViewById(R.id.tv_settings_clear_cache);
        tvUpData = findViewById(R.id.tv_settings_up_data);
        tvLogOut = findViewById(R.id.tv_settings_logout);
        relativeLayout = findViewById(R.id.relativeLayout);
        checkSetting = findViewById(R.id.checking_settings);
        tv_version = findViewById(R.id.tv_version);
        mDialog = UploadProgressDialog.initGrayDialog(this);
        mDialog.setMessage("");
        tv_version.setText("V"+BuildConfig.VERSION_NAME);
    }

    @Override
    public void initEvents() {
        tvClearCache.setOnClickListener(this);
        tvUpData.setOnClickListener(this);
        tvLogOut.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == relativeLayout) {
            SettingActivity.this.finish();
        } else if (v == tvClearCache) {
            deleteDir(getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                deleteDir(getExternalCacheDir());
            }
            ToastUtils.showShort(mContext, getResources().getString(R.string.clearUp));
        } else if (v == tvUpData) {
            //更新Apk
            UpdateInfo updateInfo = new UpdateInfo(SettingActivity.this);
            updateInfo.getVersionIsUpdate();
        } else if (v == tvLogOut) {
            mDialog.show();
            logout();
        }
    }

    @Override
    public void initObject() {
        setMVP();
        if (!UserSpCache.getInstance(getApplicationContext()).getBoolean(UserSpCache.KEY_IS_USER_LOGIN)) {
            if (tvLogOut != null) {
                tvLogOut.setVisibility(View.GONE);
            }
        }
    }

    public void logout() {
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
        mPresenter.checkIstemp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void tempLoginSuccess(String s) {
        mPresenter.pushToken();
        mDialog.dismiss();
        int count = UserSpCache.getInstance(getApplicationContext()).getInt(UserSpCache.OPEN_COUNT);
        count++;
        UserSpCache.getInstance(this).putInt(UserSpCache.OPEN_COUNT, count);

        SettingActivity.this.finish();
        EventBus.getDefault().post(new LogoutEvent());
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        mDialog.dismiss();
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
