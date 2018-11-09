package com.sven.huinews.international.main.login.contract;


import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.requst.PushTokenRequest;

public interface LoginFragmentContract {

    /**
     * 登录
     */

    abstract class Model extends BaseModel {

        public abstract void getLogin(LoginRequest request, DataCallBack callBack);

        public abstract void chackIsLogin(PlatformLogin request, DataResponseCallback<String> cacheBack);

        public abstract void pushToken(PushTokenRequest request, DataCallBack callBack);

        public abstract void requstIsLogin(ThirdRequest request, DataResponseCallback<String> callback);

    }

    interface View extends BaseView {

        LoginRequest getLoginRequest();

        void responseLoginOk();

        void bindEmail(String type);

        void loginSucceed();

        PushTokenRequest onPushTokenRequest();

        ThirdRequest getThirdRequest(String s);

    }

    abstract class Presenter extends BasePresenter<LoginFragmentContract.View, LoginFragmentContract.Model> {

        public abstract void getLogin();

        public abstract void checkLogin(PlatformLogin request, String str);

        public abstract void pushToken();

        public abstract void checkLogin(String type);
    }


}
