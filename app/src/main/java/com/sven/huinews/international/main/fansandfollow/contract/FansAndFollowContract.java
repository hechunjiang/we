package com.sven.huinews.international.main.fansandfollow.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.FansAndFollow;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.requst.FansAndFollowRequest;
import com.sven.huinews.international.entity.requst.VideoLikeRequest;
import com.sven.huinews.international.main.earn.contract.EarnActivityContract;

import java.util.List;

/**
 * Created by Burgess on 2018/9/20 0020.
 */
public interface FansAndFollowContract {
    abstract class Model extends BaseModel {
        //关注列表
        public abstract void onFollowList(FansAndFollowRequest request, DataResponseCallback<List<FansAndFollow.DataBean>> callback);
        //粉丝列表
        public abstract void onFansList(FansAndFollowRequest request, DataResponseCallback<List<FansAndFollow.DataBean>> callback);
    }

    interface View extends BaseView {
        void setFansAndFollowList(List<FansAndFollow.DataBean> mDatas,boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<FansAndFollowContract.View, FansAndFollowContract.Model> {
        public abstract void onFollowList(FansAndFollowRequest request,boolean isRefresh);

        public abstract void onFansList(FansAndFollowRequest request,boolean isRefresh);
    }
}
