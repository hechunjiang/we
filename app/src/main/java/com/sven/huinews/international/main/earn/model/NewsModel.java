package com.sven.huinews.international.main.earn.model;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.SharedRequest;
import com.sven.huinews.international.main.earn.contract.EarnActivityContract;

/**
 * Created by Burgess on 2018/9/17 0017.
 */
public class NewsModel extends EarnActivityContract.Model {

    //分享计数
    @Override
    public void shareVisit(ShareVisitRequest request, final DataCallBack dataCacheBack) {
        getRetrofit().shareVisit(request, new DataCallBack() {
            @Override
            public void onComplete() {
                dataCacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                dataCacheBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                dataCacheBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void inviteShareDatas(SharedRequest request, final DataCallBack callBack) {
        getRetrofit().inviteShareDatas(request, new DataCallBack() {
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

    //分享
}
