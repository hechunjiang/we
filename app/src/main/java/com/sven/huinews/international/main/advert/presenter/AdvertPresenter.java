package com.sven.huinews.international.main.advert.presenter;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.main.advert.contract.AdvertContract;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class AdvertPresenter extends AdvertContract.Presenter {
    @Override
    public void checkIstemp() {
        mModel.requestIstemp(new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                mView.tempLoginSuccess(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }

        });


    }

    @Override
    public void pushToken() {
        mModel.pushToken(mView.onPushTokenRequest(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showLog("msg----json:" + json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });
    }
}
