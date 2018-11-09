package wedemo.contract;//package com.sven.huinews.international.main.shot.contract;
//
//import com.sven.huinews.international.base.BaseModel;
//import com.sven.huinews.international.base.BasePresenter;
//import com.sven.huinews.international.base.BaseView;
//import com.sven.huinews.international.config.http.DataCallBack;
//import com.sven.huinews.international.config.http.DataResponseCallback;
//import com.sven.huinews.international.entity.requst.NvsResCategroyRequest;
//import com.sven.huinews.international.entity.requst.NvsResListRequest;
//import com.sven.huinews.international.entity.requst.VideoUploadRequest;
//import com.sven.huinews.international.entity.response.AliyunInfoResponse;
//import com.sven.huinews.international.main.shot.bean.PublishInfo;
//
//public interface ShotContract {
//    abstract class Model extends BaseModel {
//        public abstract void onNvsResCategroy(NvsResCategroyRequest resCategroyRequest, DataCallBack callBack);
//
//        public abstract void onNbsResList(NvsResListRequest request, DataCallBack callBack);
//    }
//
//    interface View extends BaseView {
//        NvsResCategroyRequest getNvsResCategroyRequest();
//
//        NvsResListRequest getResListRequest();
//    }
//
//    abstract class Presenter extends BasePresenter<ShotContract.View, ShotContract.Model> {
//        public abstract void onNvsResCategroy();
//
//        public abstract void onNvsResList();
//
//
//    }
//}
