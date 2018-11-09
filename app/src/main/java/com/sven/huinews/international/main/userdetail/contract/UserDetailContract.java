package com.sven.huinews.international.main.userdetail.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.FollowsRequest;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.UserCommentRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.FollowVideoResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public interface UserDetailContract {
    abstract class Model extends BaseModel {

        public abstract void requestUserInfo(UserCommentRequest request, DataResponseCallback<UserDatasResponse> callback);

        public abstract void getUserDetailsLists(PersonWorkRequest request, final DataResponseCallback<PerSonWorkResponse> callBack);

        public abstract void getUserLikesLists(LikesRequest request, final DataResponseCallback<PersonLikeResponse> callBack);

        //关注
        public abstract void onFollow(FollowsRequest request, final DataCallBack callBack);

        public abstract void requestFollowPlayUrlList(String aliyunVideoId, DataCallBack callBackShot);

        public abstract void requstIsLogin(ThirdRequest request, DataResponseCallback<String> callback);

        public abstract void getLogin(LoginRequest request, DataCallBack callBack);
    }

    interface View extends BaseView {

        //用户信息
        UserCommentRequest getUserCommentRequest();

        PersonWorkRequest getPersonWorkRequest();

        LikesRequest getLikeRequest();

        ThirdRequest getThirdRequest(String s);

        void loginSucceed();

        void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType);

        void setPersonLikesData(PersonLikeResponse response, int selectType);

        void setUserInfo(UserDatasResponse userInfo);

        void hideRefresh();

        void hideLoadMore(boolean isHide);

        void follows(boolean yesOrNo, String msg);

        void setVideoPlayUrl(AliVideoResponse data, PerSonWorkResponse.DataBean dataBean);
    }

    abstract class Presenter extends BasePresenter<UserDetailContract.View, UserDetailContract.Model> {

        //获取用户个人信息
        public abstract void getUserDetailsDataInfo(UserCommentRequest request, boolean fasle);

        //获得真实用户Video数据
        public abstract void getUserDetailsInfo(PersonWorkRequest request, final Boolean isRefresh, final int selectType);

        //获取用户粉丝列表
        public abstract void getUserDetailsLikesInfo(LikesRequest request, final Boolean isRefresh, final int selectType);

        //关注
        public abstract void onFollow(FollowsRequest request);

        //获取阿里云播放地址
        public abstract void getVideoPlayInfo(String aliyunVideoId, PerSonWorkResponse.DataBean data);

        public abstract void checkLogin(String type);

        public abstract void getLogin(LoginRequest request);
    }
}
