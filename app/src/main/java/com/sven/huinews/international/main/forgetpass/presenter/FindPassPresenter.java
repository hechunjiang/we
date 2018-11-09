package com.sven.huinews.international.main.forgetpass.presenter;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.BaseRequest;
import com.sven.huinews.international.main.forgetpass.contract.FindPassContract;
import com.sven.huinews.international.utils.ToastUtils;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class FindPassPresenter extends FindPassContract.Presenter {

    @Override
    public void getFindPassCode() {
        mModel.requestFindPassCode(mView.getFindPassCodeRequest(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.the_verifacation_code_has_been_send_to_your_email));
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.sendCodeerror));
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void resetNotPass() {
        mModel.requestResetPassReset(mView.getResetPassRequset(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                mView.resetPassSuccess(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }

        });
    }
}
