package com.sven.huinews.international.main.advert.model;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.requst.PushTokenRequest;
import com.sven.huinews.international.entity.requst.TempLoginRequest;
import com.sven.huinews.international.main.advert.contract.AdvertContract;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class AdvertModel extends AdvertContract.Model {

    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());

    @Override
    public void requestIstemp(final DataCallBack callBack) {
        TempLoginRequest request = new TempLoginRequest();
        request.setMobileBrand(PhoneUtils.getPhoneBrand());
        getRetrofit().onTempLogin(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showLog(json);
                User response = new Gson().fromJson(json, User.class);
                mUserSpCache.putString(UserSpCache.KEY_TICKET, response.getData().getTicket());
                mUserSpCache.putBoolean(UserSpCache.KEY_IS_USER_LOGIN, response.getData().isLogin_flag());
                mUserSpCache.putUser(response);
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }

        });
    }

    @Override
    public void pushToken(PushTokenRequest request, final DataCallBack callBack) {
        getRetrofit().onPushToken(request, new DataCallBack() {
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
