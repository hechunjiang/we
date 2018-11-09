package com.sven.huinews.international.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.ThridLoginRequest;
import com.sven.huinews.international.entity.response.FacebookRegResponse;
import com.sven.huinews.international.entity.response.LoginUserResponse;
import com.sven.huinews.international.entity.response.TaskFinishResponse;
import com.sven.huinews.international.entity.response.TwitterRegResponse;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.entity.requst.LinkedInRegRequest;
import com.sven.huinews.international.main.forgetpass.activity.FindPassActivity;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.main.login.activity.PrivacyActivity;
import com.sven.huinews.international.tplatform.facebook.FacebookPlatform;
import com.sven.huinews.international.tplatform.google.GoogleLogin;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.utils.AnimationUtils;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

import org.greenrobot.eventbus.EventBus;


public class CustomLoginDialog extends Dialog implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleLogin.GoogleSignListener {

    private Context mContext;
    private ImageView fb_login, tt_login, ins_login, gmail_login;

    private LinearLayout ll_login_input;
    private LinearLayout ll_login_select;

    private TextView tv_login, tv_reLogin, tv_privacy, login_find_pass_tv;
    private EditText et_loginEamail, et_password;
    private FragmentActivity activity;

    private GoogleSignInAccount mGoogleLogin;
    private GoogleLogin googleLogin;
    private ShowProgressDialog mDialog;

    private boolean hasGoogleLogin = false;

    @Override
    public void dismiss() {
        super.dismiss();
        ll_login_input.setVisibility(View.GONE);
        ll_login_select.setVisibility(View.VISIBLE);

    }

    @Override
    public void show() {
        super.show();

        hasGoogleLogin = false;
    }

    public CustomLoginDialog(@NonNull Context context, FragmentActivity activity) {
        super(context, R.style.my_dialog);
        this.activity = activity;
        mContext = context;
        setContentView(R.layout.dialog_login);

        tv_privacy = findViewById(R.id.tv_privacy);
        ll_login_input = findViewById(R.id.ll_login_input);
        ll_login_select = findViewById(R.id.ll_login_select);

        tv_login = findViewById(R.id.tv_login);

        fb_login = findViewById(R.id.fb_login);
        tt_login = findViewById(R.id.tt_login);
        ins_login = findViewById(R.id.ins_login);
        gmail_login = findViewById(R.id.gmail_login);

        et_loginEamail = findViewById(R.id.et_loginEamail);
        et_password = findViewById(R.id.et_password);
        tv_reLogin = findViewById(R.id.tv_reLogin);

        login_find_pass_tv = findViewById(R.id.login_find_pass_tv);
        fb_login.setOnClickListener(this);
        tt_login.setOnClickListener(this);
        ins_login.setOnClickListener(this);
        gmail_login.setOnClickListener(this);
        tv_reLogin.setOnClickListener(this);
        login_find_pass_tv.setOnClickListener(this);
        agreementEvent();

        Window mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        mParams.gravity = Gravity.BOTTOM;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setAttributes(mParams);

        tv_login.setOnClickListener(this);

        if(googleLogin == null) {
            googleLogin = new GoogleLogin(activity, this);
            googleLogin.setGoogleSignListener(this);
        }

        mDialog = ShowProgressDialog.initGrayDialog(activity);
        mDialog.setCancelable(false);
        mDialog.setMessage("");
    }

    @Override
    public void onClick(View view) {
        bind = false;
        switch (view.getId()) {
            case R.id.fb_login:

                // mThirdLogin.facebookLogin();
                faceBookLogin(false);
                dismiss();
                break;
            case R.id.tt_login:
                //mThirdLogin.TterLogin();
                twitterLogin(false);
                dismiss();
                break;
            case R.id.ins_login:
                //mThirdLogin.InsLogin();
                dismiss();
                break;
            case R.id.gmail_login:
                //mThirdLogin.GmailLogin();
                googleLogin(false);
                dismiss();
                break;

            case R.id.tv_login:
                ll_login_select.setVisibility(View.GONE);
                AnimationUtils.showAndHiddenAnimation(ll_login_input, AnimationUtils.AnimationState.STATE_SHOW, 300);
                //AnimationUtils.showAndHiddenAnimation(ll_login_select,AnimationUtils.AnimationState.STATE_HIDDEN,300);
                break;
            case R.id.tv_reLogin:
                checkCanLogin();
                dismiss();
                break;
            case R.id.login_find_pass_tv:
                mContext.startActivity(new Intent(mContext, FindPassActivity.class));
                dismiss();
                break;
        }
    }

    private void agreementEvent() {
        final String agreement = " Privacy Policy";
        tv_privacy.setText("By signing up,you agree to the");
        String endTv = "，Including Cookie Use.";
        SpannableString string = new SpannableString(agreement);

        string.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openActivity(PrivacyActivity.class);
            }
        }, 0, agreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#FD3598")), 0,
                agreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_privacy.setHighlightColor(Color.TRANSPARENT);
        tv_privacy.append(string);
        tv_privacy.append(endTv);

        tv_privacy.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

    }


    public void openActivity(Class targetClass) {
        Intent intent = new Intent(mContext, targetClass);
        mContext.startActivity(intent);
    }

    public String mPhoneNum, mPass;

    private void checkCanLogin() {
        mPhoneNum = et_loginEamail.getText().toString().trim();
        mPass = et_password.getText().toString().trim();
        if (!CommonUtils.checkIsEmail(mPhoneNum)) {
            ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.enter_e_mail_address));
            return;
        }
        if (!CommonUtils.checkPassWord(mPass)) {
            ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.password_must_be_6_to_22_numbers));
            return;
        }
        //登录请求
        //mThirdLogin.emailLogin(mPhoneNum, mPass);

        LoginRequest request = new LoginRequest();
        request.setAccount(mPhoneNum);
        request.setPassword(mPass);
        emailLogin(request);

    }

    private ThirdLoginResult mThirdLogin;

    public void setThirdLogin(ThirdLoginResult thirdLogin) {
        mThirdLogin = thirdLogin;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ToastUtils.showShort(mContext, mContext.getString(R.string.thrid_fail));
        if (mThirdLogin != null) {
            mThirdLogin.onOtherfail();
        }
        // dismiss();
    }

    @Override
    public void googleLoginSuccess(GoogleSignInAccount acct) {
        mGoogleLogin = acct;

        checkLogin(Constant.GOOGLELOGIN, bind);

    }

    @Override
    public void googleLoginFail() {

    }

    public interface ThirdLoginResult {
        void onOtherfail();  //三方授权失败

        void onCheckFail(BaseResponse response);  //三方登陆失败

        void onLoginSuccess(String json, boolean isBind); //三方登陆成功

        void onStartCheck();  //开始调用登陆接口

        void onGetGoldSuccess(boolean isBind);  //获取金币成功

        void onGetGoldFail();  //获取金币失败

        void onLogin(); //直接登陆成功
    }

    public void registerCallBack(int requestCode, int resultCode, Intent data, boolean isFragment) {
        if (mFacebookPlatform != null) {
            mFacebookPlatform.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }


        if (mTwitterLogin != null) {
            mTwitterLogin.setActivityResult(requestCode, resultCode, data);
        }

        if (!isFragment) {
            if (requestCode == 10) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                googleLogin.handleSignInResult(result);
            }
        }
    }

    private FacebookPlatform mFacebookPlatform;
    private TwitterLogin mTwitterLogin;

    private FacebookRegResponse mFacebookRegResponse;
    private TwitterRegResponse mTwitterRegResponse;

    public FacebookPlatform getmFacebookPlatform() {
        return mFacebookPlatform;
    }

    public TwitterLogin getmTwitterLogin() {
        return mTwitterLogin;
    }

    public GoogleLogin getGooGLeLogin() {
        return googleLogin;
    }

    private boolean bind = false;

    public void googleLogin(boolean isBind) {
        bind = isBind;
        googleLogin.signIn();
    }

    public void faceBookLogin(final boolean isBind) {
        if (mFacebookPlatform == null) {
            mFacebookPlatform = new FacebookPlatform(activity);
        }

        mFacebookPlatform.login();

        mFacebookPlatform.setFacebookListener(new FacebookPlatform.FacebookListener() {

            @Override
            public void facebookLoginSuccess(PlatformLogin platformLogin, FacebookRegResponse response) {
                mFacebookRegResponse = response;
                checkLogin(Constant.FACEBOOK, isBind);
            }

            @Override
            public void facebookLoginFail() {
                ToastUtils.showShort(mContext, mContext.getString(R.string.thrid_fail));
                if (mThirdLogin != null) {
                    mThirdLogin.onOtherfail();
                }

                // dismiss();
            }
        });
    }

    public void twitterLogin(final boolean isBind) {
        if (!CommonUtils.isApplicationAvilible(activity, "com.twitter.android")) {
            ToastUtils.showShort(activity, activity.getString(R.string.you_not_installed_twitter));
            return;
        }
        if (mTwitterLogin == null) {
            mTwitterLogin = new TwitterLogin();
        }

        mTwitterLogin.loginTwitter(activity, new TwitterLogin.titterLoginCallback() {
            @Override
            public void onSuccess(TwitterRegResponse data) {
                mTwitterRegResponse = data;
                checkLogin(Constant.TWITTER, isBind);
            }

            @Override
            public void onFailure() {
                if (mThirdLogin != null) {
                    mThirdLogin.onOtherfail();
                }

                // dismiss();
                ToastUtils.showShort(mContext, mContext.getString(R.string.thrid_fail));

            }
        });
    }

    /**
     * email直接登陆
     *
     * @param request
     */
    private void emailLogin(final LoginRequest request) {
        if (mThirdLogin != null) {
            mThirdLogin.onStartCheck();
        }

        //  dismiss();
        mDialog.show();

        final UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
        MyRetrofit.getInstance(mContext)
                .onLogin(request, new DataCallBack() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSucceed(String json) {
                        //存储user信息
                        User user = new Gson().fromJson(json, User.class);
                        mUserSpCache.putString(Constant.KEY_TICKET, user.getData().getTicket());
                        mUserSpCache.putBoolean(Constant.KEY_IS_USER_LOGIN, user.getData().isLogin_flag());
                        mUserSpCache.putString(Constant.KEY_PHONE, request.getAccount());
                        mUserSpCache.putString(Constant.NIKE_NAME, user.getData().getUser_info().getNickname());
                        mUserSpCache.putString(Constant.KEY_PASS, request.getPassword());
                        mUserSpCache.putString(Constant.C_USERID, user.getData().getUser_info().getC_user_id());
                        mUserSpCache.putString(Constant.OPENID, user.getData().getUser_info().getOpenId());
                        mUserSpCache.putString(Constant.KEY_USER, new Gson().toJson(user.getData().getUser_info()));
                        mUserSpCache.putString(Constant.KEY_IS_SECEND_OPEN_APP, "isSecend");
                        mUserSpCache.putUser(user);
                        ACache.get(AppConfig.getAppContext()).put(UserSpCache.KEY_PHONE, request.getAccount(), ACache.STORAGE_TIME);
                        if (mThirdLogin != null) {
                            mThirdLogin.onLoginSuccess(json, false);
                            mThirdLogin.onLogin();
                        }
                        EventBus.getDefault().post(Common.REFRESH_USERINFO);
                        mDialog.dismiss();
                    }

                    @Override
                    public void onFail(BaseResponse baseResponse) {
                        if (mThirdLogin != null) {
                            mThirdLogin.onCheckFail(baseResponse);
                        }
                        ToastUtils.showShort(mContext, baseResponse.getMsg());
                        mDialog.dismiss();
                    }
                });
    }


    /**
     * 三方登陆
     *
     * @param type
     */
    private void checkLogin(final String type, final boolean isBind) {
        if (mThirdLogin != null) {
            mThirdLogin.onStartCheck();
        }
        //  dismiss();
        mDialog.show();

        ThirdRequest thirdRequest = getThirdRequest(type);

        if(isBind){
            thirdRequest.setTask("1");
        }

        MyRetrofit.getInstance(mContext)
                .checkIsLogin(thirdRequest, new DataCallBack() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSucceed(String s) {
                        BaseResponse baseResponse = new Gson().fromJson(s, BaseResponse.class);
                        if (baseResponse.getCode() == 200) {
                            LoginUserResponse response = new Gson().fromJson(s, LoginUserResponse.class);
                            UserSpCache mUserSpCache = UserSpCache.getInstance(mContext);
                            mUserSpCache.putString(UserSpCache.KEY_TICKET, response.getData().getTicket());
                            mUserSpCache.putBoolean(UserSpCache.KEY_IS_USER_LOGIN, response.getData().isLogin_flag());
                            mUserSpCache.putString(UserSpCache.KEY_USER, new Gson().toJson(response.getData().getUser_info()));
                            mUserSpCache.putString(UserSpCache.KEY_PHONE, response.getData().getUser_info().getNickname());
                            ACache.get(mContext).put(UserSpCache.KEY_PHONE, response.getData().getUser_info().getNickname(), ACache.STORAGE_TIME);
                            mUserSpCache.putString(UserSpCache.KEY_PASS, response.getData().getUser_info().getNickname());
                            mUserSpCache.putString(UserSpCache.KEY_IS_SECEND_OPEN_APP, "isSecend");
                            // mView.loginSucceed();
                            onThridSuccess(type, response.getData().getUser_info().getC_user_id() + "",isBind);

                            if (mThirdLogin != null) {
                                mThirdLogin.onLoginSuccess(s, isBind);
                            }

                            EventBus.getDefault().post(Common.REFRESH_USERINFO);
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail(BaseResponse baseResponse) {
                        if (mThirdLogin != null) {
                            mThirdLogin.onCheckFail(baseResponse);
                        }
                        ToastUtils.showShort(mContext, baseResponse.getMsg());
                        mDialog.dismiss();
                    }
                });
    }

    /**
     * 三方登陆成功加金币接口
     *
     * @param loginType
     * @param user_id
     */
    private void onThridSuccess(String loginType, String user_id, final boolean isBind) {
        ThridLoginRequest request = new ThridLoginRequest();
        request.setUser_id(user_id);
        request.setLogin_source(loginType);
        MyRetrofit.getInstance(mContext)
                .thridLogin(request, new DataCallBack() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSucceed(String json) {
                        mDialog.dismiss();
                        LogUtil.showLog("加金币成功");
                        TaskFinishResponse taskFinishResponse = new Gson().fromJson(json, TaskFinishResponse.class);

                        if(mThirdLogin != null){
                            mThirdLogin.onGetGoldSuccess(isBind);
                        }
                        //todo:加金币弹框
                        ToastUtils.showGoldCoinToast(mContext, taskFinishResponse.getMsg(), "+" + taskFinishResponse.getData().getGold());
                    }

                    @Override
                    public void onFail(BaseResponse baseResponse) {
                        //ToastUtils.showShort(mContext, baseResponse.getMsg());
                        LogUtil.showLog("加金币失败");

                        mDialog.dismiss();
                        if(mThirdLogin != null){
                            mThirdLogin.onGetGoldFail();
                        }

                    }
                });
    }

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

    public void clearData(){
        et_loginEamail.setText("");
        et_password.setText("");
    }
}
