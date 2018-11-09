package com.sven.huinews.international.main.bindemail.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.BindEmailRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;

/**
 * Created by sfy. on 2018/11/1 0001.
 */

public interface BindEmailContract {

    abstract class Model extends BaseModel {

        public abstract void getSmsCode(GetCodeRequest request, DataCallBack callBack);

        public abstract void requestBindEmail(BindEmailRequest request, DataResponseCallback<String> callback);
    }

    interface View extends BaseView {

        GetCodeRequest getSmsRequest();

        BindEmailRequest getBindEmailRequest();

        void getsmsCodeSuccess();

        void bindEmailSuccess(String s);

    }

    abstract class Presenter extends BasePresenter<BindEmailContract.View, BindEmailContract.Model> {
        public abstract void getSmsCode(GetCodeRequest request);

        public abstract void bindEmail();

    }
}
