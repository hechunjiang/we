package com.sven.huinews.international.main.me.presenter;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.FeedBackRequest;
import com.sven.huinews.international.entity.response.FeedBackClassifyResponse;
import com.sven.huinews.international.entity.response.FeedBackResponse;
import com.sven.huinews.international.main.me.contract.FeedBackContract;

public class FeedBackPresenter extends FeedBackContract.Presenter {


    @Override
    public void getFeedClassifyList() {
        mModel.getFeedClassify(new DataResponseCallback<FeedBackClassifyResponse>() {
            @Override
            public void onSucceed(FeedBackClassifyResponse feedBackClassifyResponse) {
                mView.getFeedClassify(feedBackClassifyResponse);
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(),response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void sendFeed(final FeedBackRequest request) {
        mModel.sendFeed(request,new DataResponseCallback<FeedBackResponse>() {
            @Override
            public void onSucceed(FeedBackResponse response) {
                mView.showErrorTip(response.getCode(),response.getMsg());
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(),response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
