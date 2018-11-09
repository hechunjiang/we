package com.sven.huinews.international.main.login.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.linkedin.platform.LISessionManager;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.requst.PushTokenRequest;
import com.sven.huinews.international.entity.response.FacebookRegResponse;
import com.sven.huinews.international.entity.response.LinkedInResponse;
import com.sven.huinews.international.entity.response.TwitterRegResponse;
import com.sven.huinews.international.main.bindemail.BindPhoneActivity;
import com.sven.huinews.international.main.forgetpass.activity.FindPassActivity;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.main.login.contract.LoginFragmentContract;
import com.sven.huinews.international.main.login.model.LoginFragmentModel;
import com.sven.huinews.international.main.login.presenter.LoginFragmentPresenter;
import com.sven.huinews.international.tplatform.facebook.FacebookPlatform;
import com.sven.huinews.international.tplatform.google.GoogleLogin;
import com.sven.huinews.international.tplatform.linkedin.LinkedInPlatform;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.dialog.UploadProgressDialog;

import org.greenrobot.eventbus.EventBus;

import wedemo.MessageEvent;

public class LoginFragment extends BaseFragment<LoginFragmentPresenter, LoginFragmentModel> implements LoginFragmentContract.View,
        GoogleApiClient.OnConnectionFailedListener, GoogleLogin.GoogleSignListener {
    public EditText loginPhoneEt, loginPpassEt;
    public TextView loginFindPassTv, loginBtn, facebookbtn, tiwtterBtn, linkedinBtn;
    public String mPhoneNum, mPass;
    private FacebookPlatform mFacebookPlatform;
    private FacebookRegResponse mFacebookRegResponse;
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());

    private TwitterLogin mTwitterLogin;
    private TwitterRegResponse mTwitterRegResponse;
    private LinkedInResponse mLinkedInResponse;
    private GoogleSignInAccount mGoogleLogin;
    private int otherLogin = 0;
    private UploadProgressDialog mDialog;
    private GoogleLogin googleLogin;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_login;
    }

    @Override
    public void initObject() {
        setMVP();
        mDialog = UploadProgressDialog.initGrayDialog(getActivity());
        mDialog.setCancelable(false);
        mDialog.setMessage("");
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(View v) {
        //账号
        loginPhoneEt = v.findViewById(R.id.login_phone_et);
        //密码
        loginPpassEt = v.findViewById(R.id.login_pass_et);

        loginFindPassTv = v.findViewById(R.id.login_find_pass_tv);

        loginBtn = v.findViewById(R.id.login_btn);

        facebookbtn = v.findViewById(R.id.btn_facebook);

        tiwtterBtn = v.findViewById(R.id.btn_tiwtter);

        linkedinBtn = v.findViewById(R.id.btn_linkedin);

        googleLogin = new GoogleLogin(getActivity(), this);
        googleLogin.setGoogleSignListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            otherLogin = arguments.getInt(Constant.OTHER_LOGIN, 0);
        }
    }

    @Override
    public void initEvents() {
        loginBtn.setOnClickListener(this);
        loginFindPassTv.setOnClickListener(this);
        loginPhoneEt.addTextChangedListener(textWatcher);
        loginPpassEt.addTextChangedListener(textWatcher);
        facebookbtn.setOnClickListener(this);
        tiwtterBtn.setOnClickListener(this);
        linkedinBtn.setOnClickListener(this);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void afterTextChanged(Editable editable) {
            String inputPhone = loginPhoneEt.getText().toString().trim();
            String inputPass = loginPpassEt.getText().toString().trim();
            if (TextUtils.isEmpty(inputPhone) || TextUtils.isEmpty(inputPass)) {
                loginBtn.setEnabled(false);
                return;
            }

            if (inputPass.length() >= 6) {
                loginBtn.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_login));
                loginBtn.setEnabled(true);
            } else {
                loginBtn.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_login_nor));
                loginBtn.setEnabled(false);
            }
        }
    };

    @Override
    public void OnClickEvents(View v) {
        if (v == loginBtn) {//登录
            checkCanLogin();
        } else if (v == loginFindPassTv) {//忘记密码
            openActivity(FindPassActivity.class);
        } else if (v == facebookbtn) {//facebook登录
            facebookLogin();
        } else if (v == tiwtterBtn) {//twitter登录
            twitterLogin();
        } else if (v == linkedinBtn) {//Google登录
//            initLinkedIn();
            googleLogin.signIn();
        }

    }

    private void facebookLogin() {
        if (mFacebookPlatform == null) {
            mFacebookPlatform = new FacebookPlatform(getActivity());
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
        if (!CommonUtils.isApplicationAvilible(getActivity(), "com.twitter.android")) {
            ToastUtils.showShort(getActivity(), getActivity().getString(R.string.you_not_installed_twitter));
            return;
        }
        if (mTwitterLogin == null) {
            mTwitterLogin = new TwitterLogin();
        }

        mTwitterLogin.loginTwitter(getActivity(), new TwitterLogin.titterLoginCallback() {
            @Override
            public void onSuccess(TwitterRegResponse data) {
                showLoading();
                mTwitterRegResponse = data;
//                PlatformLogin platformLogin = new PlatformLogin();
//                platformLogin.setPlatform(Api.TYPE_TIWTTER);
//                platformLogin.setTwitter_id(data.getTwitter_id());
//                mPresenter.checkLogin(platformLogin, Api.TYPE_TIWTTER);
                mPresenter.checkLogin(Constant.TWITTER);
            }

            @Override
            public void onFailure() {
                LogUtil.showLog("msg---Tiwtter:onFailure");

            }
        });
    }

    private void initLinkedIn() {
        LinkedInPlatform linkedInPlatform = new LinkedInPlatform(getActivity());
        linkedInPlatform.initLinkedInLogin();

        linkedInPlatform.setLinkedInLoginLisenter(new LinkedInPlatform.linkedInLoginLisenter() {
            @Override
            public void getLinkedInfoSucceed(LinkedInResponse response) {
                showLoading();
//                PlatformLogin mPlatformLogin = new PlatformLogin();
//                mPlatformLogin.setLk_id(response.getId());
//                mPlatformLogin.setPlatform(Api.TYPE_LINKEDIN);
                mLinkedInResponse = response;
//                mPresenter.checkLogin(mPlatformLogin, Api.TYPE_LINKEDIN);
                mPresenter.checkLogin(Constant.LINKEDIN);
            }

            @Override
            public void getLinkedInfoError() {
                ToastUtils.showLong(mContext, mContext.getResources().getString(R.string.sendCodeerror));
            }
        });
    }

    private void checkCanLogin() {
        mPhoneNum = loginPhoneEt.getText().toString().trim();
        mPass = loginPpassEt.getText().toString().trim();
        if (!CommonUtils.checkIsEmail(mPhoneNum)) {
            ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.enter_e_mail_address));
            return;
        }
        if (!CommonUtils.checkPassWord(mPass)) {
            ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.password_must_be_6_to_22_numbers));
            return;
        }
        //登录请求
        mPresenter.getLogin();
        mDialog.show();
    }


    @Override
    public LoginRequest getLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setAccount(mPhoneNum);
        request.setPassword(mPass);
        return request;
    }

    @Override
    public void responseLoginOk() {
        mPresenter.pushToken();
        ToastUtils.showShort(mContext, mContext.getString(R.string.logged_in));
//        ToastUtils.showShort(mContext, mUserSpCache.getStringData(Constant.OPENID));
        mDialog.cancel();
        if (otherLogin == 1) {
            EventBus.getDefault().post(new MessageEvent(Constant.LOGIN_SUCCESS));
            getActivity().finish();
        } else {
            startActivity(MainActivity.class);
            getActivity().finish();
        }
    }

    @Override
    public void bindEmail(String type) {
        Bundle bundle = new Bundle();
        if (type.equals(Api.TYPE_TIWTTER)) {
            bundle.putSerializable(Common.BUNDLE_TO_BIND_PHONE, mTwitterRegResponse);
            bundle.putString(Common.BUNDLE_TO_BIND_PHONE_TYPE, Api.TYPE_TIWTTER);
        } else if (type.equals(Api.TYPE_FACEBOOK)) {
            bundle.putSerializable(Common.BUNDLE_TO_BIND_PHONE, mFacebookRegResponse);
            bundle.putString(Common.BUNDLE_TO_BIND_PHONE_TYPE, Api.TYPE_FACEBOOK);
        } else if (type.equals(Api.TYPE_LINKEDIN)) {
            bundle.putSerializable(Common.BUNDLE_TO_BIND_PHONE, mLinkedInResponse);
            bundle.putString(Common.BUNDLE_TO_BIND_PHONE_TYPE, Api.TYPE_LINKEDIN);
        }
        hideLoading();
        startActivity(BindPhoneActivity.class, bundle);
    }

    @Override
    public void loginSucceed() {
        FirebaseMessaging.getInstance().subscribeToTopic("login_in");
        hideLoading();
        if (otherLogin == 1) {
            EventBus.getDefault().post(new MessageEvent(Constant.LOGIN_SUCCESS));
            getActivity().finish();
        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public PushTokenRequest onPushTokenRequest() {
        PushTokenRequest request = new PushTokenRequest();
        request.setToken(FirebaseInstanceId.getInstance().getToken());
        boolean isLogin = UserSpCache.getInstance(mContext).getBoolean(Constant.KEY_IS_USER_LOGIN);
        FirebaseMessaging.getInstance().subscribeToTopic(isLogin ? "login_in" : "login_no");
        String topic = isLogin ? "total,login_in" : "total,login_no";
        request.setTopic(topic);
        return request;
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
        } else if (s.equals(Constant.LINKEDIN)) {
            request.setMobile_brand(PhoneUtils.getPhoneBrand());
            request.setHeadimg(mLinkedInResponse.getPictureUrl() == null ? "123" : mLinkedInResponse.getPictureUrl());
            request.setNickname(mLinkedInResponse.getFirstName() + mLinkedInResponse.getLastName());
            request.setLk_id(mLinkedInResponse.getId());
            request.setSex("1");
            request.setLogin_source(Constant.LINKEDIN);
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
    public void showLoading() {
        showBaseLoading("");
    }

    @Override
    public void hideLoading() {
        hideBaseLoading();
    }

    @Override
    public void showErrorTip(int code, String msg) {
        mDialog.cancel();
        ToastUtils.showLong(getActivity(), msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFacebookPlatform != null) {
            mFacebookPlatform.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (mTwitterLogin != null) {
            mTwitterLogin.setActivityResult(requestCode, resultCode, data);
        }

        LISessionManager.getInstance(getActivity()).onActivityResult(getActivity(), requestCode, resultCode, data);
        if (requestCode == 10) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleLogin.handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void googleLoginSuccess(GoogleSignInAccount acct) {
        mGoogleLogin = acct;
        showLoading();
        mPresenter.checkLogin(Constant.GOOGLELOGIN);
    }

    @Override
    public void googleLoginFail() {

    }

}
