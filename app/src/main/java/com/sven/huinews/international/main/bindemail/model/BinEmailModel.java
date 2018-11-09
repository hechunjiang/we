package com.sven.huinews.international.main.bindemail.model;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.BindEmailRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.main.bindemail.contract.BindEmailContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sfy. on 2018/11/1 0001.
 */

public class BinEmailModel extends BindEmailContract.Model {
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
    public void requestBindEmail(BindEmailRequest request, final DataResponseCallback<String> callback) {

        getRetrofit().bindEmail(request, new DataCallBack() {
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
