package com.sven.huinews.international.main.web.presenter;


import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.jspush.JsShareResponse;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.response.ShareResponse;
import com.sven.huinews.international.main.web.JsWebView;
import com.sven.huinews.international.publicclass.ShareModel;

/**
 * Created by Sven on 2018/2/1.
 */

public class WebPresenter extends BasePresenter {
    private JsWebView mJsWebView;
    private Context mContext;
    private ShareModel mShareModel;
    private Handler mHandler = new Handler();
    public WebPresenter(JsWebView jsWebView, Context context) {
        mJsWebView = jsWebView;
        mContext = context;
        mShareModel = new ShareModel(context);
    }

    public WebPresenter(Context context) {
        mContext = context;
        mShareModel = new ShareModel(context);
    }

    /**
     * 分享计数
     *
     * @param response
     */
    public void shareVisit(String response,String videoType, int type) {
        JsShareResponse mJsShareResponse = new Gson().fromJson(response, JsShareResponse.class);
        ShareVisitRequest request = new ShareVisitRequest();
        request.setActivityType(videoType);
        request.setCode(mJsShareResponse.getCode() + "");
        request.setShareChannel(type + "");
        mShareModel.shareVisit(request, new DataCallBack() {
            @Override
            public void onSucceed(String s) {

            }

            @Override
            public void onFail(BaseResponse response) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 获取分享信息
     */
    public void getShareInfo() {
        mShareModel.getShareInfo(new DataCallBack() {
            @Override
            public void onSucceed(String s) {
//                JsShareType jsShareType = new Gson().fromJson(s, JsShareType.class);
                ShareResponse shareResponse = new Gson().fromJson(s,ShareResponse.class);
                mJsWebView.onShareInfo(shareResponse);
            }

            @Override
            public void onFail(BaseResponse response) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

}
