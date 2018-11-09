package com.sven.huinews.international.main.follow.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.FollowVideoPlayResponse;
import com.sven.huinews.international.entity.response.FollowVideoResponse;
import com.sven.huinews.international.main.follow.contract.FollowContract;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by sfy. on 2018/9/10 0010.
 */


public class FollowPresenter extends FollowContract.Presenter {


    @Override
    public void getFollowList(final boolean isRefresh) {
        mModel.requestFollowList(mView.getFollowRequest(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showLog("msg----json:" + json);
                FollowVideoResponse response = new Gson().fromJson(json, FollowVideoResponse.class);
                mView.setFollowData(response.getData(), isRefresh, true);
            }

            @Override
            public void onFail(BaseResponse response) {
                if (mView != null) {
                    if (isRefresh) {
                        mView.hideRefresh();
                    } else {
                        mView.hideLoadMore(false);
                    }
                    if (response.getMsg() != null)
                        mView.showErrorTip(response.getCode(), response.getMsg());
                }
            }
        });
    }

    @Override
    public void getVideoPlayInfo(String aliyunVideoId, final FollowVideoResponse.DataBean dataBean) {
        mModel.requestFollowPlayUrlList(aliyunVideoId, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                AliVideoResponse response = new Gson().fromJson(json, AliVideoResponse.class);
                mView.setVideoPlayUrl(response, dataBean);

            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }


}
