package com.sven.huinews.international.main.permsg.persenter;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.UserInfoResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.permsg.contact.PersonMsgContract;

import java.io.File;

/**
 * Created by sfy. on 2018/9/13 0013.
 */

public class PersonMsgPresenter extends PersonMsgContract.Presenter {
    @Override
    public void getUserDetailsDataInfo(boolean fasle) {
        mModel.requestUserInfo(new DataResponseCallback<UserDatasResponse>() {
            @Override
            public void onSucceed(UserDatasResponse response) {
                mView.setUserInfo(response);
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
    public void uploadeHeadImage(File file) {
        mModel.requestUploadHeadImage(file, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                mView.uploadImageSuccess(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void upLoadPerMsg() {
        mModel.requestUploadPerMsg(mView.getUserMsgRequest(), new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                mView.uploadPerMsgSuccess();
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

//    @Override
//    public void getPersonMsg() {
//        mModel.getPersonMsg(new DataResponseCallback<UserInfoResponse>() {
//            @Override
//            public void onSucceed(UserInfoResponse userInfoResponse) {
//                mView.getPersonMsgSuccess(userInfoResponse);
//            }
//
//            @Override
//            public void onFail(BaseResponse response) {
//                mView.showErrorTip(response.getCode(), response.getMsg());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//
//    }
}
