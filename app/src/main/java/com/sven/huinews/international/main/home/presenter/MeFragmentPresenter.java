package com.sven.huinews.international.main.home.presenter;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.UserInfoResponse;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.LinkShareUrlRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.requst.SharedRequest;
import com.sven.huinews.international.entity.response.LoginUserResponse;
import com.sven.huinews.international.main.home.contract.MeFragmentContract;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sfy. on 2018/9/8 0008.
 */

public class MeFragmentPresenter extends MeFragmentContract.Presenter {
    private JsShareType jsShareType;

    @Override
    public void getPersonMsg() {
        mModel.getPersonMsg(new DataResponseCallback<UserInfoResponse>() {
            @Override
            public void onSucceed(UserInfoResponse userInfoResponse) {
                mView.getPersonMsgSuccess(userInfoResponse);
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




    @Override
    public void getLinkShareUrl(final int type) {
        String s_type = "";
        SharedRequest request = new SharedRequest();
        if (type == Common.SHARE_TYPE_TWITTER) {
            s_type = "twitter";
        } else if (type == Common.SHARE_TYPE_FACEBOOK) {
            s_type = "facebook";
        } else if (type == Common.SHARE_TYPE_INS) {
            s_type = "instgram";
        } else if (type == Common.SHARE_TYPE_WHATS) {
            s_type = "whatsapp";
        }
        request.setType(s_type);

        mModel.requestLinkUrl(request, new DataResponseCallback<String>() {
            @Override
            public void onSucceed(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    jsonObject = jsonObject.getJSONObject("data");
                    jsonObject = jsonObject.getJSONObject("default");
                    jsShareType = new Gson().fromJson(jsonObject.toString(), JsShareType.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mView != null) mView.toShare(type, jsShareType);
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
