package com.sven.huinews.international.main.userdetail.module;

import android.util.Log;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.requst.FollowsRequest;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.UserCommentRequest;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.userdetail.contract.UserDetailContract;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class UserDetailModulel extends UserDetailContract.Model {

    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());

    @Override
    public void requestUserInfo(UserCommentRequest request, final DataResponseCallback<UserDatasResponse> callback) {
        getRetrofit().getUserDetails(request, new DataCallBack() {
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

    @Override
    public void getUserDetailsLists(PersonWorkRequest request, final DataResponseCallback<PerSonWorkResponse> callBack) {
        getRetrofit().getUserDetailsLikesList(request, new DataCallBack() {
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
    public void getUserLikesLists(LikesRequest request, final DataResponseCallback<PersonLikeResponse> callBack) {
        getRetrofit().getUserLikesList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
//                Log.d("getUserDetailsLists", "onSucceed: " + json);
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
    public void onFollow(FollowsRequest request, final DataCallBack callBack) {
        getRetrofit().onFollow(request, new DataCallBack() {
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

    @Override
    public void requestFollowPlayUrlList(String aliyunVideoId, final DataCallBack callBackShot) {
        getRetrofit().getAlPlayUrl(aliyunVideoId, new DataCallBack() {
            @Override
            public void onComplete() {
                callBackShot.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBackShot.onSucceed(json);
                LogUtil.showLog("aliyun", json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBackShot.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requstIsLogin(ThirdRequest request, final DataResponseCallback<String> callback) {
        getRetrofit().checkIsLogin(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callback.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getLogin(final LoginRequest request, final DataCallBack callBack) {
        getRetrofit().onLogin(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                Log.d("json", "onSucceed: " + json);
                //存储user信息
                User user = new Gson().fromJson(json, User.class);
                mUserSpCache.putString(Constant.KEY_TICKET, user.getData().getTicket());
                mUserSpCache.putBoolean(Constant.KEY_IS_USER_LOGIN, user.getData().isLogin_flag());
                mUserSpCache.putString(Constant.KEY_PHONE, request.getAccount());
                mUserSpCache.putString(Constant.NIKE_NAME, user.getData().getUser_info().getNickname());
                mUserSpCache.putString(Constant.KEY_PASS, request.getPassword());
                mUserSpCache.putString(Constant.C_USERID, user.getData().getUser_info().getC_user_id());
                mUserSpCache.putString(Constant.OPENID, user.getData().getUser_info().getOpenId());
                mUserSpCache.putString(Constant.KEY_USER, new Gson().toJson(user.getData().getUser_info()));
                mUserSpCache.putString(Constant.KEY_IS_SECEND_OPEN_APP, "isSecend");
                mUserSpCache.putUser(user);
                ACache.get(AppConfig.getAppContext()).put(UserSpCache.KEY_PHONE, request.getAccount(), ACache.STORAGE_TIME);
                callBack.onSucceed(json);

            }

            @Override
            public void onFail(BaseResponse baseResponse) {

                callBack.onFail(baseResponse);

            }
        });
    }


}
