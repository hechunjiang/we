package wedemo.model;//package com.sven.huinews.international.main.shot.model;
//
//import com.sven.huinews.international.base.BaseResponse;
//import com.sven.huinews.international.config.http.DataCallBack;
//import com.sven.huinews.international.entity.requst.NvsResCategroyRequest;
//import com.sven.huinews.international.entity.requst.NvsResListRequest;
//import com.sven.huinews.international.main.shot.contract.ShotContract;
//import com.sven.huinews.international.utils.LogUtil;
//
//public class ShotModel extends ShotContract.Model {
//    @Override
//    public void onNvsResCategroy(NvsResCategroyRequest request, final DataCallBack callBack) {
//        getRetrofit().onNvsResCategroy(request, new DataCallBack() {
//            @Override
//            public void onComplete() {
//                callBack.onComplete();
//            }
//
//            @Override
//            public void onSucceed(String json) {
//                LogUtil.showJson("onNvsResCategroy", json);
//                callBack.onSucceed(json);
//            }
//
//            @Override
//            public void onFail(BaseResponse baseResponse) {
//                callBack.onFail(baseResponse);
//            }
//        });
//    }
//
//    @Override
//    public void onNbsResList(NvsResListRequest request, final DataCallBack callBack) {
//        getRetrofit().onNvsResList(request, new DataCallBack() {
//            @Override
//            public void onComplete() {
//                callBack.onComplete();
//            }
//
//            @Override
//            public void onSucceed(String json) {
//                LogUtil.showJson("onNbsResList",json);
//            }
//
//            @Override
//            public void onFail(BaseResponse baseResponse) {
//
//
//            }
//        });
//    }
//}
