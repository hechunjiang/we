package com.sven.huinews.international.main.me.Model;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.response.MessageResponse;
import com.sven.huinews.international.main.me.contract.MessageContract;

public class MessageModel extends MessageContract.Model {


    @Override
    public void getMessageList(final DataResponseCallback<MessageResponse> callBack) {
        getRetrofit().onMessageList(new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                MessageResponse response = new Gson().fromJson(json, MessageResponse.class);
                callBack.onSucceed(response);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }
}
