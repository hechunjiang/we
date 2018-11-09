package com.sven.huinews.international.main.news.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.NewsListRequst;

/**
 * Created by sfy. on 2018/9/6 0006.
 */

public interface FirstNewsContruct {

    /**
     * 新闻列表
     */

    abstract class Model extends BaseModel {
        public abstract void getNewsList(NewsListRequst requst, DataCallBack callBack);
    }

    interface View extends BaseView {
        NewsListRequst newsListRequest();

        void setNewsList(String s);
    }

    abstract class Presenter extends BasePresenter<FirstNewsContruct.View, FirstNewsContruct.Model> {

        public abstract void getNewsDatas();
    }
}
