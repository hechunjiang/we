package com.sven.huinews.international.main.login.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.Users;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.RegistRequst;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public interface RegistFragmentContract {

    /**
     * 注册、发送短信
     */

    abstract class Model extends BaseModel {
        public abstract void requestCode(GetCodeRequest request, DataCallBack callBack);

        public abstract void requestRegist(RegistRequst registRequst, DataCallBack callBack);

    }

    interface View extends BaseView {
        GetCodeRequest getCodeRequest();

        RegistRequst getRegistRequst();

        void RegistSuccess(Users user);

        void getCodeSuccess();

    }

    abstract class Presenter extends BasePresenter<RegistFragmentContract.View, RegistFragmentContract.Model> {
        public abstract void getRegistCode();

        public abstract void regist();
    }

}
