package com.sven.huinews.international.main.permsg.model;

import android.util.Log;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.UserInfoResponse;
import com.sven.huinews.international.entity.requst.UserMsgRequest;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.permsg.contact.PersonMsgContract;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by sfy. on 2018/9/13 0013.
 */

public class PersonMsgModel extends PersonMsgContract.Model {

    @Override
    public void requestUserInfo(final DataResponseCallback<UserDatasResponse> callback) {
        getRetrofit().getUserPerSonWorkDetails(new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                UserDatasResponse datasResponse = new Gson().fromJson(json, UserDatasResponse.class);
                callback.onSucceed(datasResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestUploadHeadImage(File file, final DataCallBack callBack) {
        getRetrofit().uploadImage(file, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
//                    new JSONObject(json).getJSONObject("data").getString("markId")
                callBack.onSucceed(json);

            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestUploadPerMsg(UserMsgRequest request, final DataCallBack callBack) {
        getRetrofit().setUserMsg(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
                Log.d("onSucceed", "onSucceed: " + json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }



}
