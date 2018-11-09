package com.sven.huinews.international.main.permsg.contact;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.UserInfoResponse;
import com.sven.huinews.international.entity.requst.NewsListRequst;
import com.sven.huinews.international.entity.requst.UserMsgRequest;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.news.contract.FirstNewsContruct;

import java.io.File;

/**
 * Created by sfy. on 2018/9/12 0012.
 */

public interface PersonMsgContract {
    abstract class Model extends BaseModel {

        public abstract void requestUserInfo(DataResponseCallback<UserDatasResponse> callback);

        public abstract void requestUploadHeadImage(File file, DataCallBack callBack);

        public abstract void requestUploadPerMsg(UserMsgRequest request, DataCallBack callBack);


    }

    interface View extends BaseView {
        UserMsgRequest getUserMsgRequest();

        void setUserInfo(UserDatasResponse userInfo);

        void uploadImageSuccess(String markId);



        void uploadPerMsgSuccess();

    }

    abstract class Presenter extends BasePresenter<PersonMsgContract.View, PersonMsgContract.Model> {
        //获取用户个人信息
        public abstract void getUserDetailsDataInfo(boolean fasle);

        public abstract void uploadeHeadImage(File file);

        public abstract void upLoadPerMsg();


    }
}
