package com.sven.huinews.international.main.fansandfollow.model;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.FansAndFollow;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.requst.FansAndFollowRequest;
import com.sven.huinews.international.main.fansandfollow.contract.FansAndFollowContract;
import com.sven.huinews.international.utils.LogUtil;

import java.util.List;

/**
 * Created by Burgess on 2018/9/20 0020.
 */
public class FansAndFollowModel extends FansAndFollowContract.Model {
    @Override
    public void onFollowList(FansAndFollowRequest request,final DataResponseCallback<List<FansAndFollow.DataBean>> callback) {
        getRetrofit().onFollowList(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                FansAndFollow fansAndFollow = new Gson().fromJson(json, FansAndFollow.class);
                callback.onSucceed(fansAndFollow.getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });
    }

    @Override
    public void onFansList(FansAndFollowRequest request,final DataResponseCallback<List<FansAndFollow.DataBean>> callback) {
        getRetrofit().onFansList(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                FansAndFollow fansAndFollow = new Gson().fromJson(json, FansAndFollow.class);
                callback.onSucceed(fansAndFollow.getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });
    }
}
