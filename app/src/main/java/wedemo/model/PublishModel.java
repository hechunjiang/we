package wedemo.model;//package com.sven.huinews.international.main.shot.model;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.VideoUploadRequest;
import com.sven.huinews.international.entity.response.AliyunInfoResponse;
import com.sven.huinews.international.utils.LogUtil;


import wedemo.activity.data.PublishInfo;
import wedemo.activity.data.WcsBean;

import wedemo.activity.request.WcsRequest;
import wedemo.contract.PublishContract;

public class PublishModel extends PublishContract.Model {

    @Override
    public void getWcsToken(WcsRequest request,PublishInfo videoInfo, final DataResponseCallback<WcsBean> callback) {
        getRetrofit().onWcsToken(request,new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showJson("getAliyunToken", json);
                WcsBean response = new Gson().fromJson(json, WcsBean.class);
                callback.onSucceed(response);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void uploadVideoId(VideoUploadRequest request, final DataCallBack callBack) {
        getRetrofit().onUploadVideoId(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
                LogUtil.showJson("uploadVideoId", json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }
}
