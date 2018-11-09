package wedemo.contract;//package com.sven.huinews.international.main.shot.contract;
//
//import com.sven.huinews.international.base.BaseModel;
//import com.sven.huinews.international.base.BasePresenter;
//import com.sven.huinews.international.base.BaseView;
//import com.sven.huinews.international.config.http.DataCallBack;
//import com.sven.huinews.international.config.http.DataResponseCallback;
//import com.sven.huinews.international.entity.requst.MusicListRequest;
//import com.sven.huinews.international.entity.requst.MusicSearchRequest;
//import com.sven.huinews.international.entity.response.FollowVideoResponse;
//import com.sven.huinews.international.entity.response.MusicListResponse;
//import com.sven.huinews.international.main.follow.contract.FollowContract;
//import com.sven.huinews.international.main.shot.bean.MusicTypeResponse;
//
//import java.util.List;
//
//public interface MusicContract {
//    abstract class Model extends BaseModel {
//        public abstract void onMusicCategroy(DataResponseCallback<List<MusicTypeResponse.DataBean>> callback);
//
//        public abstract void onMusicList(MusicListRequest request, DataResponseCallback<List<MusicListResponse.DataBean>> callback);
//
//        public abstract void onMusicSearch(MusicSearchRequest request, DataResponseCallback<List<MusicListResponse.DataBean>> callback);
//    }
//
//    interface View extends BaseView {
//        void setMusicType(List<MusicTypeResponse.DataBean> mMusicTypes);
//
//        MusicListRequest getMusicListRequest();
//
//        void setMusicList(List<MusicListResponse.DataBean> mDatas);
//
//        MusicSearchRequest getMusicSearchRequest();
//
//        void setMusicPath(String path, boolean isUse);
//    }
//
//    abstract class Presenter extends BasePresenter<MusicContract.View, MusicContract.Model> {
//        public abstract void onMusic();
//
//        public abstract void onMusicList();
//
//        public abstract void onMusicSearch();
//    }
//}
