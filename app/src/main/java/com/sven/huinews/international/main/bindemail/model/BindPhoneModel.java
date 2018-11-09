package com.sven.huinews.international.main.bindemail.model;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.FacebookRegRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.LinkedInRegRequest;
import com.sven.huinews.international.entity.requst.SmsCodeRequest;
import com.sven.huinews.international.entity.requst.TwitterRegRequest;
import com.sven.huinews.international.main.bindemail.contract.BindPhoneContract;

/**
 * Created by Burgess on 2018/9/21 0021.
 */
public class BindPhoneModel extends BindPhoneContract.Model {


    @Override
    public void getSmsCode(GetCodeRequest request, final DataCallBack callBack) {
        getRetrofit().getSmsCode(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void onTwitterReg(TwitterRegRequest request, final DataCallBack callBack) {
        getRetrofit().onTwitterReg(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void onFacebookReg(FacebookRegRequest request, final DataCallBack callBack) {
        getRetrofit().onFacebookReg(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void onLinkedInReg(LinkedInRegRequest request, final DataCallBack callBack) {
        getRetrofit().onLinkedInReg(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }
}
