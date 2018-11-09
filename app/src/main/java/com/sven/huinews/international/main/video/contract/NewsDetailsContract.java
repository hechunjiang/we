package com.sven.huinews.international.main.video.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.NewsStatisticsRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;

import java.util.Map;


public interface NewsDetailsContract {
    abstract class Model extends BaseModel {

        //新闻统计
        public abstract void setNewsStatistics(NewsStatisticsRequest request, final DataResponseCallback<String> dataCacheBack);

        public abstract void getGoldByTask(TaskRequest request, final DataResponseCallback<Map<String, String>> cacheBack, final int coins, final String type);
    }

    interface View extends BaseView {
        void showGoldCome(int count, int type, String masgess);

        void showNetWorkError();
    }

    abstract class Presenter extends BasePresenter<NewsDetailsContract.View, NewsDetailsContract.Model> {


        //新闻统计
        public abstract void newsStatistics(String news_id);

        //是否已经获取到金币
        public abstract boolean isCanGetCoinByReadNews(Object data);

        //获取金币
        public abstract void readAnyNewsGetGold(final int type, String type_id);

        //数据库添加金币
        public abstract void addReadNews(Object data);

        //保存金币打开次数
        public abstract void saveGoldOpenCount();

    }
}
