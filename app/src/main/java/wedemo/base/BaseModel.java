package wedemo.base;


import android.content.Context;

import wedemo.config.http.MyRetrofit;


/**
 * auther: sunfuyi
 * data: 2018/5/12
 * effect:
 */
public abstract class BaseModel {

    protected MyRetrofit myRetrofit;

    protected MyRetrofit getRetrofit(Context context) {
        if (myRetrofit == null) {
            myRetrofit = new MyRetrofit(context, null);
        }
        return myRetrofit;
    }

    protected MyRetrofit getRetrofit(Context context, String https) {
        return new MyRetrofit(context, https);
    }
}
