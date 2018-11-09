package com.sven.huinews.international.main.login.model;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.RegistRequst;
import com.sven.huinews.international.main.login.contract.RegistFragmentContract;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class RegistModel extends RegistFragmentContract.Model {
    @Override
    public void requestCode(GetCodeRequest request, final DataCallBack callBack) {

        getRetrofit().getSmsCode(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
                LogUtil.showLog(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }

        });

    }

    @Override
    public void requestRegist(RegistRequst registRequst, final DataCallBack callBack) {
        getRetrofit().onEmailRegister(registRequst, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
                LogUtil.showLog(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }

        });
    }
}
