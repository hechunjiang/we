package wedemo.contract;//package com.sven.huinews.international.main.shot.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.MusicListRequest;
import com.sven.huinews.international.entity.requst.MusicSearchRequest;
import com.sven.huinews.international.entity.response.MusicListResponse;
import com.sven.huinews.international.entity.response.TagResponse;

import java.util.List;

public interface TagContract {
    abstract class Model extends BaseModel {
        public abstract void onTagList(DataResponseCallback<List<TagResponse.DataBean>> callBack);
    }

    interface View extends BaseView {
        void setTagList(List<TagResponse.DataBean> mDatas);
    }

    abstract class Presenter extends BasePresenter<TagContract.View, TagContract.Model> {
        public abstract void onTagList();
    }
}
