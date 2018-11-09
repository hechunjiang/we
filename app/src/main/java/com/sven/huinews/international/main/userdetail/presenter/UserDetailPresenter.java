package com.sven.huinews.international.main.userdetail.presenter;

import com.google.gson.Gson;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.FollowsRequest;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.UserCommentRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.FollowVideoResponse;
import com.sven.huinews.international.entity.response.LoginUserResponse;
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

public class UserDetailPresenter extends UserDetailContract.Presenter {

    @Override
    public void getUserDetailsDataInfo(UserCommentRequest request, final boolean isRefresh) {
        mModel.requestUserInfo(request, new DataResponseCallback<UserDatasResponse>() {
            @Override
            public void onSucceed(UserDatasResponse response) {
                mView.setUserInfo(response);
            }

            @Override
            public void onFail(BaseResponse response) {
                if (mView != null) {
                    if (isRefresh) {
                        mView.hideRefresh();
                    } else {
                        mView.hideLoadMore(false);
                    }
                }
            }

            @Override
            public void onComplete() {
                if (mView != null) {
                    if (isRefresh) {
                        mView.hideRefresh();
                    } else {
                        mView.hideLoadMore(false);
                    }
                }
            }
        });
    }

    @Override
    public void getUserDetailsInfo(final PersonWorkRequest request, final Boolean isRefresh, final int selectType) {
        mModel.getUserDetailsLists(request, new DataResponseCallback<PerSonWorkResponse>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(PerSonWorkResponse json) {
                mView.setPersonalWorksData(json, isRefresh, true, selectType);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                if (mView != null) {
                    if (isRefresh) {
                        mView.hideRefresh();
                    } else {
                        mView.hideLoadMore(false);
                    }

                }
            }
        });
    }

    @Override
    public void getUserDetailsLikesInfo(final LikesRequest request, final Boolean isRefresh, final int selectType) {
        mModel.getUserLikesLists(request, new DataResponseCallback<PersonLikeResponse>() {
            @Override
            public void onSucceed(PersonLikeResponse personLikeResponse) {
                mView.setPersonLikesData(personLikeResponse, selectType);
            }

            @Override
            public void onFail(BaseResponse response) {
                if (isRefresh) {
                    mView.hideRefresh();
                } else {
                    mView.hideLoadMore(false);

                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onFollow(FollowsRequest request) {
        mModel.onFollow(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                mView.follows(true,"");
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.follows(false,baseResponse.getMsg());
            }
        });
    }

    @Override
    public void getVideoPlayInfo(String aliyunVideoId,final PerSonWorkResponse.DataBean data) {
        mModel.requestFollowPlayUrlList(aliyunVideoId, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                AliVideoResponse response = new Gson().fromJson(json, AliVideoResponse.class);
                mView.setVideoPlayUrl(response, data);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
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
