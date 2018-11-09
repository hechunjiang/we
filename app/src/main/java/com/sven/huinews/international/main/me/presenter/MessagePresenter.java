package com.sven.huinews.international.main.me.presenter;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.response.MessageResponse;
import com.sven.huinews.international.main.me.contract.MessageContract;

public class MessagePresenter extends MessageContract.Presenter {
    @Override
    public void getMessageList() {
        mModel.getMessageList(new DataResponseCallback<MessageResponse>() {
            @Override
            public void onSucceed(MessageResponse messageResponse) {
                mView.setMessageData(messageResponse);
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
