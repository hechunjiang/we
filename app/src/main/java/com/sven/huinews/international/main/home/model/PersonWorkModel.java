package com.sven.huinews.international.main.home.model;

import android.util.Log;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.home.contract.PersonWorkContract;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class PersonWorkModel extends PersonWorkContract.Model {
    @Override
    public void requestUserInfo(final DataResponseCallback<UserDatasResponse> callback) {
        getRetrofit().getUserPerSonWorkDetails(new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                UserDatasResponse datasResponse = new Gson().fromJson(json, UserDatasResponse.class);
                callback.onSucceed(datasResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    /*@Override
    public void getUserDetailsLists(final DataResponseCallback<PerSonWorkResponse> callBack) {
        getRetrofit().getPerSonWorkDetailsList(new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                Log.d("getUserDetailsLists", "onSucceed: " + json);
                PerSonWorkResponse workResponse = new Gson().fromJson(json, PerSonWorkResponse.class);
                callBack.onSucceed(workResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getUserLikesLists(final DataResponseCallback<PersonLikeResponse> callBack) {
        getRetrofit().getPersonUserLikesList(new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                Log.d("getUserDetailsLists", "onSucceed: " + json);
                PersonLikeResponse likeResponse = new Gson().fromJson(json, PersonLikeResponse.class);
                callBack.onSucceed(likeResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }*/

    @Override
    public void getUserDetailsLists(PersonWorkRequest request, final DataResponseCallback<PerSonWorkResponse> callBack) {
        getRetrofit().getUserDetailsLikesList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                PerSonWorkResponse workResponse = new Gson().fromJson(json, PerSonWorkResponse.class);
                callBack.onSucceed(workResponse);
            }
            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });

    }

    @Override
    public void getUserLikesLists(LikesRequest request, final DataResponseCallback<PersonLikeResponse> callBack) {
        getRetrofit().getUserLikesList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                PersonLikeResponse likeResponse = new Gson().fromJson(json, PersonLikeResponse.class);
                callBack.onSucceed(likeResponse);

            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });

    }


    @Override
    public void requestAliPlayUrl(String id, final DataResponseCallback<AliVideoResponse> callBack) {
        getRetrofit().getAlPlayUrl(id, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                AliVideoResponse aliVideoResponse = new Gson().fromJson(json, AliVideoResponse.class);
                callBack.onSucceed(aliVideoResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }


}
