package com.sven.huinews.international.main.advert.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.PushTokenRequest;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public interface AdvertContract {
    /**
     * 临时登录
     */
    abstract class Model extends BaseModel {
        public abstract void requestIstemp(DataCallBack dataCallBack);

        public abstract void pushToken(PushTokenRequest request, DataCallBack callBack);
    }

    interface View extends BaseView {
        void tempLoginSuccess(String s);

        PushTokenRequest onPushTokenRequest();
    }

    abstract class Presenter extends BasePresenter<AdvertContract.View, AdvertContract.Model> {
        public abstract void checkIstemp();

        public abstract void pushToken();
    }
}
