package com.sven.huinews.international.publicclass;

import android.content.Context;

import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.utils.TaskModel;

/**
 * Created by Burgess on 2018/10/11 0011.
 */
public class DBPresenter {
    private Context mContext;
    private TaskModel mTaskModel = new TaskModel(AppConfig.getAppContext());

    public DBPresenter(Context mContext){
        this.mContext = mContext;
    }

    public boolean isCanGetCoinByReadNews(Object data) {
        mTaskModel = new TaskModel(AppConfig.getAppContext());
        return mTaskModel.readNewsCanGetCoin(data);

    }
}
