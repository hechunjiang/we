package com.sven.huinews.international.main.me.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.NewsListRequst;
import com.sven.huinews.international.entity.response.MessageResponse;
import com.sven.huinews.international.main.news.contract.FirstNewsContruct;

public interface MessageContract {

    /**
     * 新闻列表
     */

    abstract class Model extends BaseModel {
        public abstract void getMessageList(DataResponseCallback<MessageResponse> callBack);
    }

    interface View extends BaseView {
        void setMessageData(MessageResponse response);
    }

    abstract class Presenter extends BasePresenter<MessageContract.View, MessageContract.Model> {

        public abstract void getMessageList();
    }
}
