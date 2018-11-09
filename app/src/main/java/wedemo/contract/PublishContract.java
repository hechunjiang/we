package wedemo.contract;//package com.sven.huinews.international.main.shot.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.VideoUploadRequest;
import com.sven.huinews.international.entity.response.AliyunInfoResponse;

import wedemo.activity.data.PublishInfo;
import wedemo.activity.data.WcsBean;
import wedemo.activity.request.WcsRequest;

public interface PublishContract {
    abstract class Model extends BaseModel {
        public abstract void getWcsToken(WcsRequest request,PublishInfo videoInfo, DataResponseCallback<WcsBean> callback);

        public abstract void uploadVideoId(VideoUploadRequest request, DataCallBack callBack);
    }

    interface View extends BaseView {
        void setService(WcsBean dataBean, PublishInfo videoInfo);
    }

    abstract class Presenter extends BasePresenter<PublishContract.View, PublishContract.Model> {
        public abstract void onWcsToken(WcsRequest request,PublishInfo videoInfo);

        public abstract void onUploadVideoId(VideoUploadRequest request);
    }
}
