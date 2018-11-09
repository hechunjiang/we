package com.sven.huinews.international.main.bindemail.persenter;

import com.google.gson.Gson;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.BindEmailRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.response.BindEamilResponse;
import com.sven.huinews.international.main.bindemail.contract.BindEmailContract;
import com.sven.huinews.international.utils.ToastUtils;

/**
 * Created by sfy. on 2018/11/1 0001.
 */

public class BindEmailPresenter extends BindEmailContract.Presenter {

    @Override
    public void getSmsCode(GetCodeRequest request) {
        mModel.getSmsCode(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {

                mView.getsmsCodeSuccess();
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });
    }

    @Override
    public void bindEmail() {
        mModel.requestBindEmail(mView.getBindEmailRequest(), new DataResponseCallback<String>() {
            @Override
            public void onSucceed(String s) {
                BindEamilResponse response = new Gson().fromJson(s, BindEamilResponse.class);

                    mView.bindEmailSuccess(response.getData().getGold() + "");

            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(), response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
