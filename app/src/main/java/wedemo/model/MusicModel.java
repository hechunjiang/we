package wedemo.model;//package com.sven.huinews.international.main.shot.model;
//
//import com.google.gson.Gson;
//import com.sven.huinews.international.base.BaseResponse;
//import com.sven.huinews.international.config.http.DataCallBack;
//import com.sven.huinews.international.config.http.DataResponseCallback;
//import com.sven.huinews.international.entity.requst.MusicListRequest;
//import com.sven.huinews.international.entity.requst.MusicSearchRequest;
//import com.sven.huinews.international.entity.response.MusicListResponse;
//import com.sven.huinews.international.main.shot.bean.MusicTypeResponse;
//import com.sven.huinews.international.main.shot.contract.MusicContract;
//import com.sven.huinews.international.utils.LogUtil;
//
//import java.util.List;
//
//public class MusicModel extends MusicContract.Model {
//
//    @Override
//    public void onMusicCategroy(final DataResponseCallback<List<MusicTypeResponse.DataBean>> callback) {
//        getRetrofit().onMusicCategroy(new DataCallBack() {
//            @Override
//            public void onComplete() {
//                callback.onComplete();
//            }
//
//            @Override
//            public void onSucceed(String json) {
//                LogUtil.showJson("onMusicCategroy", json);
//                MusicTypeResponse response = new Gson().fromJson(json, MusicTypeResponse.class);
//                callback.onSucceed(response.getData());
//
//            }
//
//            @Override
//            public void onFail(BaseResponse baseResponse) {
//                callback.onFail(baseResponse);
//            }
//        });
//    }
//
//    @Override
//    public void onMusicList(final MusicListRequest request, final DataResponseCallback<List<MusicListResponse.DataBean>> callback) {
//        getRetrofit().onMusicList(request, new DataCallBack() {
//            @Override
//            public void onComplete() {
//                callback.onComplete();
//            }
//
//            @Override
//            public void onSucceed(String json) {
//                MusicListResponse response = new Gson().fromJson(json, MusicListResponse.class);
//                callback.onSucceed(response.getData());
//            }
//
//            @Override
//            public void onFail(BaseResponse baseResponse) {
//                callback.onFail(baseResponse);
//            }
//        });
//    }
//
//    @Override
//    public void onMusicSearch(MusicSearchRequest request, final DataResponseCallback<List<MusicListResponse.DataBean>> callback) {
//        getRetrofit().onMusicSearch(request, new DataCallBack() {
//            @Override
//            public void onComplete() {
//                callback.onComplete();
//            }
//
//            @Override
//            public void onSucceed(String json) {
//                LogUtil.showJson("onMusicSearch", json);
//                MusicListResponse response = new Gson().fromJson(json, MusicListResponse.class);
//                callback.onSucceed(response.getData());
//            }
//
//            @Override
//            public void onFail(BaseResponse baseResponse) {
//                callback.onFail(baseResponse);
//            }
//        });
//    }
//
//
//}
