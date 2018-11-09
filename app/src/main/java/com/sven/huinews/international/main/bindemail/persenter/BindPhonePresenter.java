package com.sven.huinews.international.main.bindemail.persenter;

import com.google.gson.Gson;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.FacebookRegRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.LinkedInRegRequest;
import com.sven.huinews.international.entity.requst.TwitterRegRequest;
import com.sven.huinews.international.entity.response.LoginResponse;
import com.sven.huinews.international.main.bindemail.contract.BindPhoneContract;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

/**
 * Created by Burgess on 2018/9/21 0021.
 */
public class BindPhonePresenter extends BindPhoneContract.Presenter {


    @Override
    public void getSmsCode(GetCodeRequest request) {
        mModel.getSmsCode(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.Successful));
                mView.getMsgSuccessful();
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });
    }

    @Override
    public void twitterRegister() {

        TwitterRegRequest request = null;
        request = mView.getTwitterRequest();
        request.setMobileBrand(PhoneUtils.getPhoneBrand());
        mModel.onTwitterReg(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                LoginResponse loginResponse = new Gson().fromJson(json, LoginResponse.class);
                if (loginResponse.getCode() == Api.API_CODE_SUCCEED) {
                    UserSpCache.getInstance(mContext).putString(UserSpCache.KEY_TICKET, loginResponse.getData().getLoginTicket());
                    ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.Successful));
                    mView.registSuccess();
                }
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void facebookRegister() {
        FacebookRegRequest request = null;
        request = mView.getFaceBookRequest();
        request.setMobileBrand(PhoneUtils.getPhoneBrand());
        mModel.onFacebookReg(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                BaseResponse baseResponse = new Gson().fromJson(json, BaseResponse.class);
                if (baseResponse.getCode() == Api.API_CODE_SUCCEED) {
                    ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.Successful));
                    mView.registSuccess();
                }
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void linkedInRegister() {
        LinkedInRegRequest request = mView.getLinkedInRequest();
        request.setMobileBrand(PhoneUtils.getPhoneBrand());
        mModel.onLinkedInReg(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                LoginResponse response = new Gson().fromJson(json, LoginResponse.class);
                if (response.getCode() == Api.API_CODE_SUCCEED) {
                    UserSpCache mUserSpCache = UserSpCache.getInstance(mContext);
                    mUserSpCache.putString(UserSpCache.KEY_TICKET, response.getData().getLoginTicket());
                    mUserSpCache.putBoolean(UserSpCache.KEY_IS_USER_LOGIN, response.getData().isUserLogin());
                    mUserSpCache.putString(UserSpCache.NIKE_NAME, response.getData().getUser().getData().getUser_info().getNickname());
                    mUserSpCache.putString(UserSpCache.KEY_USER, new Gson().toJson(response.getData().getUser()));
                    mUserSpCache.putString(UserSpCache.KEY_PHONE, response.getData().getUser().getData().getUser_info().getNickname());
                    ACache.get(mContext).put(UserSpCache.KEY_PHONE, response.getData().getUser().getData().getUser_info().getNickname(), ACache.STORAGE_TIME);
                    mUserSpCache.putString(UserSpCache.KEY_PASS, response.getData().getUser().getData().getUser_info().getNickname());
                    mUserSpCache.putString(UserSpCache.KEY_IS_SECEND_OPEN_APP, "isSecend");
                    mUserSpCache.putUser(response.getData().getUser());
                    mView.registSuccess();
                }
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });

    }
}
