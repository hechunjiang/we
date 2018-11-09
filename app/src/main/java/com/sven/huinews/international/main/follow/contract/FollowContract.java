package com.sven.huinews.international.main.follow.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.FollowVideoPlayResponse;
import com.sven.huinews.international.entity.response.FollowVideoResponse;

import java.util.List;


public interface FollowContract {
    abstract class Model extends BaseModel {
        public abstract void requestFollowList(FollowRequest request, DataCallBack callback);

        public abstract void requestFollowPlayUrlList(String aliyunVideoId, DataCallBack callBackShot);
    }

    interface View extends BaseView {
        FollowRequest getFollowRequest();

        void setFollowData(List<FollowVideoResponse.DataBean> datas, boolean isRefresh, boolean isLoadMore);

        void hideRefresh();

        void hideLoadMore(Boolean isHide);

        void setVideoPlayUrl(AliVideoResponse data, FollowVideoResponse.DataBean dataBean);
    }

    abstract class Presenter extends BasePresenter<FollowContract.View, FollowContract.Model> {
        public abstract void getFollowList(boolean isRefresh);

        public abstract void getVideoPlayInfo(String aliyunVideoId,FollowVideoResponse.DataBean data);
    }
}
