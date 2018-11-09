package com.sven.huinews.international.main.bindemail.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.FacebookRegRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.LinkedInRegRequest;
import com.sven.huinews.international.entity.requst.SmsCodeRequest;
import com.sven.huinews.international.entity.requst.TwitterRegRequest;

/**
 * Created by Burgess on 2018/9/21 0021.
 */
public interface BindPhoneContract {
    abstract class Model extends BaseModel {
        public abstract void getSmsCode(GetCodeRequest request, DataCallBack callBack);

        public abstract void onTwitterReg(TwitterRegRequest request, DataCallBack callBack);

        public abstract void onFacebookReg(FacebookRegRequest request, DataCallBack callBack);

        public abstract void onLinkedInReg(LinkedInRegRequest request, DataCallBack callBack);
    }

    interface View extends BaseView {
        GetCodeRequest getSmsRequest();

        TwitterRegRequest getTwitterRequest();

        FacebookRegRequest getFaceBookRequest();

        LinkedInRegRequest getLinkedInRequest();

        void getMsgSuccessful();

        void registSuccess();


    }

    abstract class Presenter extends BasePresenter<BindPhoneContract.View, BindPhoneContract.Model> {
        public abstract void getSmsCode(GetCodeRequest request);

        public abstract void twitterRegister();

        public abstract void facebookRegister();

        public abstract void linkedInRegister();
    }
}
