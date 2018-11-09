package com.sven.huinews.international.main.home.presenter;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.FollowVideoPlayResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.home.contract.PersonWorkContract;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class PersonWorkPresenter extends PersonWorkContract.Presenter {
    @Override
    public void getUserDetailsDataInfo(final boolean isRefresh) {
        mModel.requestUserInfo(new DataResponseCallback<UserDatasResponse>() {
            @Override
            public void onSucceed(UserDatasResponse response) {
                mView.setUserInfo(response);
                if (mView != null) {
                    if (isRefresh) {
                        mView.hideRefresh();
                    } else {
                        mView.hideLoadMore(false);
                    }
                }
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

            }
        });

    }

    @Override
    public void getUserDetailsInfo(final PersonWorkRequest request, final Boolean isRefresh, final int selectType) {
        mModel.getUserDetailsLists(request,new DataResponseCallback<PerSonWorkResponse>() {
            @Override
            public void onSucceed(PerSonWorkResponse response) {
                mView.setPersonalWorksData(response, isRefresh, true, selectType);
                if (mView != null) {
                    if (isRefresh) {
                        mView.hideRefresh();
                    } else {
                        mView.hideLoadMore(false);
                    }
                }
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

            }
        });

    }

    @Override
    public void getUserDetailsLikesInfo(final LikesRequest request, final Boolean isRefresh, final int selectType) {
        mModel.getUserLikesLists(request,new DataResponseCallback<PersonLikeResponse>() {
            @Override
            public void onSucceed(PersonLikeResponse personLikeResponse) {
                mView.setPersonLikesData(personLikeResponse, selectType,isRefresh);
                if (mView != null) {
                    if (isRefresh) {
                        mView.hideRefresh();
                    } else {
                        mView.hideLoadMore(false);
                    }
                }
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

            }
        });
    }

    @Override
    public void getAliPlayUrl(String videoUrl) {

        mModel.requestAliPlayUrl(videoUrl, new DataResponseCallback<AliVideoResponse>() {
            @Override
            public void onSucceed(AliVideoResponse response) {
                mView.getAliNewData(response);
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

}
