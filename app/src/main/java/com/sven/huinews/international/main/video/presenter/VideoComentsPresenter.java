package com.sven.huinews.international.main.video.presenter;

import com.google.gson.Gson;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.event.ActiveIconEvent;
import com.sven.huinews.international.entity.jspush.JsShareResponse;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.TipOffRequest;
import com.sven.huinews.international.entity.requst.VideoCommentLikeRequest;
import com.sven.huinews.international.entity.requst.VideoCommentRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlResponse;
import com.sven.huinews.international.entity.response.ComentsResponse;
import com.sven.huinews.international.entity.response.CommentReponse;
import com.sven.huinews.international.entity.response.LoginUserResponse;
import com.sven.huinews.international.entity.response.PushTaskResponse;
import com.sven.huinews.international.main.video.contract.FragmentVideoComents;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

import org.greenrobot.eventbus.EventBus;

public class VideoComentsPresenter extends FragmentVideoComents.Presenter {
    @Override
    public void getVideoComents(VideoCommentRequest request) {
        mModel.requestVideoConments(request, new DataResponseCallback<CommentReponse>() {
            @Override
            public void onSucceed(CommentReponse commentReponse) {

                mView.setVideoComent(commentReponse);
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
    public void addComents() {
        mModel.requestAddVideoComents(mView.getAdComentRequest(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                ComentsResponse comentsResponse = new Gson().fromJson(json, ComentsResponse.class);
                mView.setAddComents(comentsResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void onVideoCommentLike(VideoCommentLikeRequest request) {
        mModel.requestVideoCommentLike(request, new DataCallBack() {
            @Override
            public void onComplete() {
                LogUtil.showLog("test");
                ToastUtils.showShort(mContext, mContext.getString(R.string.Successful));
            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showLog(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void onVideoCommentReport(TipOffRequest tipOffRequest) {


        mModel.videoCommentReport(tipOffRequest, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                ToastUtils.showLong(mContext, mContext.getResources().getString(R.string.thanks));
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void getTaskPush() {
        mModel.getTaskPush(new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                PushTaskResponse pushTaskResponse = new Gson().fromJson(json, PushTaskResponse.class);
                if (pushTaskResponse.getData() != null) {
                    if (mView != null) {
                        mView.showOtherTask(pushTaskResponse.getData());
                    }
                }
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                ToastUtils.showLong(mContext, mContext.getString(R.string.network_unavailable_try_again_later));
            }
        });
    }

    @Override
    public void getVideoShareUrl(VideoShareUrlRequest request,final int type) {
        mModel.requestSharedVideo(request, new DataResponseCallback<VideoShareUrlResponse>() {
            @Override
            public void onSucceed(VideoShareUrlResponse videoShareUrlResponse) {
                mView.setSharedVideoUrl(videoShareUrlResponse, type);
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
    public String getShareJson(MyNews myNews, int type) {
        JsShareType jsShareType = new JsShareType();
        jsShareType.setType(type);
        jsShareType.setTitle(myNews.getTitle());
        jsShareType.setUrl(myNews.getVideoUrl());
        jsShareType.setContent(myNews.getTitle());
        jsShareType.setImgUrl(myNews.getCoverUrl());
        jsShareType.setImagePath(myNews.getImagePath());
        jsShareType.setVideoPath(myNews.getVideoPath());
        jsShareType.setWaterVideoPath(myNews.getWaterVideoPath());
        return new Gson().toJson(jsShareType);
    }

    @Override
    public void shareVisit(String response, String videoType, int type, String vdeoId, String duType) {
        JsShareResponse mJsShareResponse = new Gson().fromJson(response, JsShareResponse.class);
        ShareVisitRequest request = new ShareVisitRequest();
        request.setActivityType(videoType);
        request.setCode(mJsShareResponse.getCode() + "");
        request.setShareChannel(type + "");
        request.setVideoId(vdeoId);
        request.setDuType(duType);
        mModel.requestSHaredVisitNumber(request, new DataResponseCallback<String>() {
            @Override
            public void onSucceed(String s) {
                LogUtil.showLog("succeed");
            }

            @Override
            public void onFail(BaseResponse response) {
                LogUtil.showLog("onFail");
            }

            @Override
            public void onComplete() {
                LogUtil.showLog("onComplete");
            }
        });
    }


    public boolean isNotCurrentDataTime() {
        String currentDay = CommonUtils.getDataTimeDay(System.currentTimeMillis());
        String cacheDay = UserSpCache.getInstance(mContext).getStringData(UserSpCache.KEY_CURRENT_DATA_TIME);
        if (currentDay.equals(cacheDay)) {
            return false;
        }
        UserSpCache.getInstance(mContext).putString(UserSpCache.KEY_CURRENT_DATA_TIME, currentDay);
        return true;
    }

    @Override
    public void checkLogin(String type) {
        mModel.requstIsLogin(mView.getThirdRequest(type), new DataResponseCallback<String>() {
            @Override
            public void onSucceed(String s) {
                BaseResponse baseResponse = new Gson().fromJson(s, BaseResponse.class);
                if (baseResponse.getCode() == 200) {
                    LoginUserResponse response = new Gson().fromJson(s, LoginUserResponse.class);
                    UserSpCache mUserSpCache = UserSpCache.getInstance(mContext);
                    mUserSpCache.putString(UserSpCache.KEY_TICKET, response.getData().getTicket());
                    mUserSpCache.putBoolean(UserSpCache.KEY_IS_USER_LOGIN, response.getData().isLogin_flag());
                    mUserSpCache.putString(UserSpCache.KEY_USER, new Gson().toJson(response.getData().getUser_info()));
                    mUserSpCache.putString(UserSpCache.KEY_PHONE, response.getData().getUser_info().getNickname());
                    ACache.get(mContext).put(UserSpCache.KEY_PHONE, response.getData().getUser_info().getNickname(), ACache.STORAGE_TIME);
                    mUserSpCache.putString(UserSpCache.KEY_PASS, response.getData().getUser_info().getNickname());
                    mUserSpCache.putString(UserSpCache.KEY_IS_SECEND_OPEN_APP, "isSecend");
                    mView.loginSucceed();
                }

            }

            @Override
            public void onFail(BaseResponse response) {
                LogUtil.showLog("msg-----response:" + response.toString());
                mView.hideLoading();
                mView.showErrorTip(response.getCode(), response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }
    @Override
    public void getLogin(LoginRequest request) {
        mModel.getLogin(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showLog(json);
                mView.loginSucceed();
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }
}
