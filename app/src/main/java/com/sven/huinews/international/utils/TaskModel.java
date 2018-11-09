package com.sven.huinews.international.utils;

import android.content.Context;

import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.utils.db.ReadNewsDb;

public class TaskModel {

    private Context mContext;
    private MyRetrofit mMyRetrofit;
    private ReadNewsDb mReadNewsDb;

    public TaskModel(Context context) {
        mContext = context;
        mMyRetrofit = MyRetrofit.getInstance(context);
        mReadNewsDb = ReadNewsDb.getInstance(context);
    }

    public void getTaskPush(DataCallBack callBack) {
        mMyRetrofit.getTaskPush(callBack);
    }

    public void getRedBag(DataCallBack callBack) {
        mMyRetrofit.getRedBag(callBack);
    }

    public boolean hasReadNews(String newsId) {
        return mReadNewsDb.getAllReadNewsList().contains(newsId);
    }

    public void addReadNews(String newsId) {
        mReadNewsDb.insertInReadNewsId(newsId);
    }

    public boolean readNewsCanGetCoin(Object data) {
        String id = "";
        if (data instanceof MyNews) {
            MyNews myNews = (MyNews) data;
            id = myNews.getId();
        } else if (data instanceof NewsInfo) {
            NewsInfo newsInfo = (NewsInfo) data;
            id = newsInfo.getId() + "";
        }
        //判断数据库中是否存在该视频，如果有返回false
        if (hasReadNews(id)) {
            return false;
        }
        return true;
    }

}
