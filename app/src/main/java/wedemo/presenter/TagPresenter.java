package wedemo.presenter;//package com.sven.huinews.international.main.shot.presenter;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.response.TagResponse;

import java.util.List;

import wedemo.contract.TagContract;

public class TagPresenter extends TagContract.Presenter {
    @Override
    public void onTagList() {
        mModel.onTagList(new DataResponseCallback<List<TagResponse.DataBean>>() {
            @Override
            public void onSucceed(List<TagResponse.DataBean> dataBeans) {
                mView.setTagList(dataBeans);
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
