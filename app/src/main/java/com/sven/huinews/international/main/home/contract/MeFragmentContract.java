package com.sven.huinews.international.main.home.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.UserInfoResponse;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.LinkShareUrlRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.SharedRequest;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;

/**
 * Created by sfy. on 2018/9/8 0008.
 */

public interface MeFragmentContract {
    /**
     * 我的界面数据请求
     */
    abstract class Model extends BaseModel {
        public abstract void getPersonMsg(DataResponseCallback<UserInfoResponse> callBack);


        public abstract void requestLinkUrl(SharedRequest request, DataResponseCallback<String> callback);
    }

    interface View extends BaseView {
        void getPersonMsgSuccess(UserInfoResponse user);



        void toShare(int type, JsShareType jsShareType);


    }

    abstract class Presenter extends BasePresenter<MeFragmentContract.View, MeFragmentContract.Model> {
        public abstract void getPersonMsg();


        public abstract void getLinkShareUrl(final int type);
    }
}
