package com.sven.huinews.international.main.fansandfollow.presenter;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.FansAndFollow;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.requst.FansAndFollowRequest;
import com.sven.huinews.international.main.earn.contract.EarnActivityContract;
import com.sven.huinews.international.main.fansandfollow.contract.FansAndFollowContract;
import com.sven.huinews.international.utils.LogUtil;

import java.util.List;

/**
 * Created by Burgess on 2018/9/20 0020.
 */
public class FansAndFollowPresenter extends FansAndFollowContract.Presenter {
    @Override
    public void onFollowList(FansAndFollowRequest request, final boolean isRefresh) {
        LogUtil.showLog(request.getDu_type()+"");
        mModel.onFollowList(request, new DataResponseCallback<List<FansAndFollow.DataBean>>() {
            @Override
            public void onSucceed(List<FansAndFollow.DataBean> dataBeans) {
                mView.setFansAndFollowList(dataBeans,isRefresh);
            }

            @Override
            public void onFail(BaseResponse response) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onFansList(FansAndFollowRequest request, final boolean isRefresh) {
        mModel.onFansList(request, new DataResponseCallback<List<FansAndFollow.DataBean>>() {
            @Override
            public void onSucceed(List<FansAndFollow.DataBean> dataBeans) {
                mView.setFansAndFollowList(dataBeans,isRefresh);
            }

            @Override
            public void onFail(BaseResponse response) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
