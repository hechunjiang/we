package com.sven.huinews.international.main.forgetpass.model;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.ResetPassRequst;
import com.sven.huinews.international.main.forgetpass.contract.FindPassContract;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class FindPassModel extends FindPassContract.Model {
    @Override
    public void requestFindPassCode(GetCodeRequest request, final DataCallBack callBack) {
        getRetrofit().getSmsCode(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showLog(json);
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }

        });
    }

    @Override
    public void requestResetPassReset(ResetPassRequst requst, final DataCallBack callBack) {
        getRetrofit().resetPassWord(requst, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }

        });
    }
}
