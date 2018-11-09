package com.sven.huinews.international.main.follow.model;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.main.follow.contract.FollowContract;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by sfy. on 2018/9/10 0010.
 */

public class FollowModel extends FollowContract.Model {


    @Override
    public void requestFollowList(FollowRequest request, final DataCallBack callback) {
        getRetrofit().getFollowList(request, new DataCallBack() {
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

    @Override
    public void requestFollowPlayUrlList(String aliyunVideoId, final DataCallBack callBack) {

        getRetrofit().getAlPlayUrl(aliyunVideoId, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
                LogUtil.showLog("aliyun", json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }
}
