package com.sven.huinews.international.base;


import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.config.http.MyRetrofit;

/**
 * auther: sunfuyi
 * data: 2018/5/12
 * effect:
 */
public abstract class BaseModel {

    protected MyRetrofit myRetrofit;

    protected MyRetrofit getRetrofit() {
        if (myRetrofit == null) {
            myRetrofit = new MyRetrofit(AppConfig.getAppContext(), null);
        }
        return myRetrofit;
    }

    protected MyRetrofit getRetrofit(String https) {
        return new MyRetrofit(AppConfig.getAppContext(), https);
    }
}
