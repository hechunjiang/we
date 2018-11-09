package com.sven.huinews.international.main.home.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.UserCommentRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.userdetail.contract.UserDetailContract;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public interface PersonWorkContract {

    //个人作品页面
    abstract class Model extends BaseModel {

        public abstract void requestUserInfo(DataResponseCallback<UserDatasResponse> callback);

        public abstract void getUserDetailsLists(final PersonWorkRequest request,final DataResponseCallback<PerSonWorkResponse> callBack);

        public abstract void getUserLikesLists(final LikesRequest request,final DataResponseCallback<PersonLikeResponse> callBack);

        //获取阿里云播放地址
        public abstract void requestAliPlayUrl(String id, DataResponseCallback<AliVideoResponse> callBack);
    }

    interface View extends BaseView {

        void setUserInfo(UserDatasResponse userInfo);

        void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType);

        void setPersonLikesData(PersonLikeResponse response, int selectType,boolean isRefresh);

        void getAliNewData(AliVideoResponse response);

        void hideRefresh();

        void hideLoadMore(boolean isHide);
    }

    abstract class Presenter extends BasePresenter<PersonWorkContract.View, PersonWorkContract.Model> {

        //获取用户个人信息
        public abstract void getUserDetailsDataInfo(boolean fasle);

        //获得真实用户Video数据
        public abstract void getUserDetailsInfo(final PersonWorkRequest request,final Boolean isRefresh, final int selectType);

        //获取用户喜欢的视频
        public abstract void getUserDetailsLikesInfo(final LikesRequest request,final Boolean isRefresh, final int selectType);

        public abstract void getAliPlayUrl(String videoUrl);
    }
}
