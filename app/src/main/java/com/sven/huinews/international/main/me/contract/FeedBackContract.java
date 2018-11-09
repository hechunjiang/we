package com.sven.huinews.international.main.me.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.FeedBackRequest;
import com.sven.huinews.international.entity.response.FeedBackClassifyResponse;
import com.sven.huinews.international.entity.response.FeedBackResponse;

public interface FeedBackContract {

    /**
     * 意见反馈
     */

    abstract class Model extends BaseModel {
        public abstract void getFeedClassify(DataResponseCallback<FeedBackClassifyResponse> callBack);

        public abstract void sendFeed(FeedBackRequest request,DataResponseCallback<FeedBackResponse> callBack);
    }

    interface View extends BaseView {
        void getFeedClassify(FeedBackClassifyResponse response);

        void sendFeed(FeedBackResponse response);
    }

    abstract class Presenter extends BasePresenter<FeedBackContract.View, FeedBackContract.Model> {

        public abstract void getFeedClassifyList();

        public abstract void sendFeed(FeedBackRequest request);

    }
}
