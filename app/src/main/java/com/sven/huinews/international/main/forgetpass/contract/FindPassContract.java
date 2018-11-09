package com.sven.huinews.international.main.forgetpass.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.ResetPassRequst;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public interface FindPassContract {
    /***
     * 找回密码
     */
    abstract class Model extends BaseModel {

        public abstract void requestFindPassCode(GetCodeRequest request, DataCallBack callBack);

        public abstract void requestResetPassReset(ResetPassRequst requst, DataCallBack callBack);
    }

    interface View extends BaseView {
        GetCodeRequest getFindPassCodeRequest();

        ResetPassRequst getResetPassRequset();

        void resetPassSuccess(String s);
    }

    abstract class Presenter extends BasePresenter<FindPassContract.View, FindPassContract.Model> {
        public abstract void getFindPassCode();

        public abstract void resetNotPass();
    }

}
