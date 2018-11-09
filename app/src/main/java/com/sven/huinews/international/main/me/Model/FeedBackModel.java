package com.sven.huinews.international.main.me.Model;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.FeedBackRequest;
import com.sven.huinews.international.entity.response.FeedBackClassifyResponse;
import com.sven.huinews.international.entity.response.FeedBackResponse;
import com.sven.huinews.international.main.me.contract.FeedBackContract;

public class FeedBackModel extends FeedBackContract.Model {

    @Override
    public void getFeedClassify(final DataResponseCallback<FeedBackClassifyResponse> callBack) {
        getRetrofit().onFeedClassify(new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                FeedBackClassifyResponse response = new Gson().fromJson(json, FeedBackClassifyResponse.class);
                callBack.onSucceed(response);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void sendFeed(FeedBackRequest request,final DataResponseCallback<FeedBackResponse> callBack) {
        getRetrofit().sendFeedBack(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                FeedBackResponse response = new Gson().fromJson(json, FeedBackResponse.class);
                callBack.onSucceed(response);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }
}
