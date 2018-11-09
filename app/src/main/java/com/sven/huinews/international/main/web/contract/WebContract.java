package com.sven.huinews.international.main.web.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.response.ShareResponse;

/**
 * Created by W.mago on 2018/9/17 0017.
 */
public interface WebContract {
    abstract class Model extends BaseModel {
        public abstract void shareVisit(ShareVisitRequest request, final DataCallBack dataCacheBack);
        public abstract void getShareInfo(final DataCallBack dataCacheBack);
    }

    interface View extends BaseView {
        void onBindWxSucceed(int count);
        void onShareInfo(ShareResponse shareResponse);
    }

    abstract class Presenter extends BasePresenter<WebContract.View, WebContract.Model> {
        public abstract void shareVisit(String response,String videoType, int type);
        public abstract void getShareInfo();
    }
}
