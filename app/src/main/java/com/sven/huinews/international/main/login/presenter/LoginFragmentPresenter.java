package com.sven.huinews.international.main.login.presenter;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.response.LoginResponse;
import com.sven.huinews.international.entity.response.LoginUserResponse;
import com.sven.huinews.international.main.login.contract.LoginFragmentContract;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;


public class LoginFragmentPresenter extends LoginFragmentContract.Presenter {
    @Override
    public void getLogin() {
        mModel.getLogin(mView.getLoginRequest(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showLog(json);
                mView.responseLoginOk();
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }


    @Override
    public void checkLogin(final PlatformLogin request, final String str) {
        mModel.chackIsLogin(request, new DataResponseCallback<String>() {
            @Override
            public void onSucceed(String s) {
                BaseResponse baseResponse = new Gson().fromJson(s, BaseResponse.class);
                if (baseResponse.getCode() == 200) {
                    //跳转绑定界面
                    if (mView != null) {
                        mView.bindEmail(str);
                    }
                } else if (baseResponse.getCode() == 302) {
                    LogUtil.showLog("msg-----onSucceed");
                    //跳转登录界面
                    LoginUserResponse response = new Gson().fromJson(s, LoginUserResponse.class);
                    UserSpCache mUserSpCache = UserSpCache.getInstance(mContext);
                    mUserSpCache.putString(UserSpCache.KEY_TICKET, response.getData().getTicket());
                    mUserSpCache.putBoolean(UserSpCache.KEY_IS_USER_LOGIN, response.getData().isLogin_flag());
                    mUserSpCache.putString(UserSpCache.KEY_USER, new Gson().toJson(response.getData().getUser_info()));
                    mUserSpCache.putString(UserSpCache.KEY_PHONE, response.getData().getUser_info().getNickname());
                    ACache.get(mContext).put(UserSpCache.KEY_PHONE, response.getData().getUser_info().getNickname(), ACache.STORAGE_TIME);
                    mUserSpCache.putString(UserSpCache.KEY_PASS, response.getData().getUser_info().getNickname());
                    mUserSpCache.putString(UserSpCache.KEY_IS_SECEND_OPEN_APP, "isSecend");
                    mView.loginSucceed();

                }
            }

            @Override
            public void onFail(BaseResponse response) {
                LogUtil.showLog("msg-----response:" + response.toString());
                mView.hideLoading();
                mView.showErrorTip(response.getCode(), response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void pushToken() {
        mModel.pushToken(mView.onPushTokenRequest(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {

            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });
    }

    //new三方登录
    @Override
    public void checkLogin(String type) {
        mModel.requstIsLogin(mView.getThirdRequest(type), new DataResponseCallback<String>() {
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
                    mView.loginSucceed();
                }

            }

            @Override
            public void onFail(BaseResponse response) {
                LogUtil.showLog("msg-----response:" + response.toString());
                mView.hideLoading();
                mView.showErrorTip(response.getCode(), response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
