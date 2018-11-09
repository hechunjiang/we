package wedemo.model;//package com.sven.huinews.international.main.shot.model;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.response.TagResponse;

import java.util.List;

import wedemo.contract.TagContract;

public class TagModel extends TagContract.Model {


    @Override
    public void onTagList(final DataResponseCallback<List<TagResponse.DataBean>> callBack) {
        getRetrofit().onTagList(new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                TagResponse response = new Gson().fromJson(json, TagResponse.class);
                callBack.onSucceed(response.getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }
}
