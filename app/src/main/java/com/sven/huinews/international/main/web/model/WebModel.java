package com.sven.huinews.international.main.web.model;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.main.web.contract.WebContract;

/**
 * Created by Burgess on 2018/9/17 0017.
 */
public class WebModel extends WebContract.Model {
    @Override
    public void shareVisit(ShareVisitRequest request,final DataCallBack dataCacheBack) {
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
    public void getShareInfo(final DataCallBack dataCacheBack) {
        getRetrofit().getShareInfo(new DataCallBack() {
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
}
