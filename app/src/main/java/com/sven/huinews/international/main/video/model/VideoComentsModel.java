package com.sven.huinews.international.main.video.model;

import android.util.Log;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.TipOffRequest;
import com.sven.huinews.international.entity.requst.VideoCommentLikeRequest;
import com.sven.huinews.international.entity.requst.VideoCommentRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlResponse;
import com.sven.huinews.international.entity.response.CommentReponse;
import com.sven.huinews.international.main.video.contract.FragmentVideoComents;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

public class VideoComentsModel extends FragmentVideoComents.Model {
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
    @Override
    public void requestVideoConments(VideoCommentRequest request, final DataResponseCallback<CommentReponse> callback) {
        getRetrofit().getVideoCommentList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                CommentReponse reponse = new Gson().fromJson(json, CommentReponse.class);
                callback.onSucceed(reponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestAddVideoComents(AdCommentRequest request, final DataCallBack callBack) {
        getRetrofit().onVideoAdComment(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onCompleted();
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
    public void requestVideoCommentLike(VideoCommentLikeRequest request, final DataCallBack callBack) {
        getRetrofit().videoCommentLike(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                Log.d("commment", "onSucceed: " + json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void videoCommentReport(TipOffRequest request, final DataCallBack callBack) {
        getRetrofit().videoCommentRepost(request, new DataCallBack() {
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
    public void getTaskPush(final DataCallBack callBack) {
        getRetrofit().getTaskPush(new DataCallBack() {
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
    public void requestSharedVideo(VideoShareUrlRequest request,final DataResponseCallback<VideoShareUrlResponse> callback) {
        getRetrofit().VideoSharedUrl(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                VideoShareUrlResponse response = new Gson().fromJson(json, VideoShareUrlResponse.class);
                callback.onSucceed(response);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestSHaredVisitNumber(ShareVisitRequest request,final DataResponseCallback<String> callback) {
        getRetrofit().addVideoShardNumber(request, new DataCallBack() {
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
