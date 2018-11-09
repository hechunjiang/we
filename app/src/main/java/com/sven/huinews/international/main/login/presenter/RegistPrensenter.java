package com.sven.huinews.international.main.login.presenter;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.requst.RegistRequst;
import com.sven.huinews.international.entity.response.LoginResponse;
import com.sven.huinews.international.entity.response.LoginResponses;
import com.sven.huinews.international.main.login.contract.RegistFragmentContract;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class RegistPrensenter extends RegistFragmentContract.Presenter {
    private UserSpCache mUserSpCache;
    @Override
    public void getRegistCode() {
        mModel.requestCode(mView.getCodeRequest(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                mView.getCodeSuccess();
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void regist() {
        final RegistRequst request =mView.getRegistRequst();
        mModel.requestRegist(request, new DataCallBack() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onSucceed(String json) {
                mUserSpCache = UserSpCache.getInstance(mContext.getApplicationContext());
                LoginResponses response = new Gson().fromJson(json, LoginResponses.class);
                mUserSpCache.putString(UserSpCache.KEY_TICKET, response.getData().getLoginTicket());
                mUserSpCache.putBoolean(UserSpCache.KEY_IS_USER_LOGIN, response.getData().isUserLogin());
                mUserSpCache.putString(UserSpCache.KEY_PHONE, request.getMail());
                ACache.get(mContext).put(UserSpCache.KEY_PHONE,request.getMail(),ACache.STORAGE_TIME);
                mUserSpCache.putString(UserSpCache.KEY_PASS, request.getPass());
                mUserSpCache.putString(UserSpCache.KEY_USER, new Gson().toJson(response.getData().getUser()));
                mUserSpCache.putUser(response.getData().getUser());
                mView.RegistSuccess(response.getData().getUser());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }

        });
    }
}
