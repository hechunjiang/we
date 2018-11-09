package com.sven.huinews.international.main.earn.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.SharedRequest;
import com.sven.huinews.international.entity.response.ApprenticePageDataResponse;

/**
 * Created by Burgess on 2018/9/17 0017.
 */
public interface EarnActivityContract {

    abstract class Model extends BaseModel {
        //分享计数
        public abstract void shareVisit(ShareVisitRequest request, final DataCallBack dataCacheBack);

        //获取分享信息
        public abstract void inviteShareDatas(SharedRequest request, DataCallBack callBack);
    }

    interface View extends BaseView {
        void setViewData(ApprenticePageDataResponse apprenticePageDataResponse);

        void setViewupdate(String context, int height);

        void toShare(int type, JsShareType jsShareType);
    }

    abstract class Presenter extends BasePresenter<EarnActivityContract.View, EarnActivityContract.Model> {
        public abstract void getDatas();

        public abstract void getShareDatas(final int type);

        public abstract void shareVisit(String response, String videoType, int type);

        public abstract void vw_toplayout(int friendsize);
    }
}
