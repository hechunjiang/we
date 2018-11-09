package com.sven.huinews.international.main.home.model;

import android.util.Log;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.UserInfoResponse;
import com.sven.huinews.international.entity.requst.LinkShareUrlRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.SharedRequest;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.main.home.contract.MeFragmentContract;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

/**
 * Created by sfy. on 2018/9/8 0008.
 */

public class MeFragmentModel extends MeFragmentContract.Model {
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());

    @Override
    public void getPersonMsg(final DataResponseCallback<UserInfoResponse> callBack) {
        getRetrofit().userInfo(new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                UserInfoResponse userInfoResponse = new Gson().fromJson(json, UserInfoResponse.class);
                callBack.onSucceed(userInfoResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }


    @Override
    public void requestLinkUrl(SharedRequest request, final DataResponseCallback<String> callback) {
        getRetrofit().inviteShareDatas(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callback.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }
}
