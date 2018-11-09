package wedemo.presenter;//package com.sven.huinews.international.main.shot.presenter;
//
//import com.sven.huinews.international.base.BaseResponse;
//import com.sven.huinews.international.config.http.DataCallBack;
//import com.sven.huinews.international.config.http.DataResponseCallback;
//import com.sven.huinews.international.entity.requst.MusicListRequest;
//import com.sven.huinews.international.entity.response.MusicListResponse;
//import com.sven.huinews.international.main.shot.bean.MusicTypeResponse;
//import com.sven.huinews.international.main.shot.contract.MusicContract;
//import com.sven.huinews.international.utils.DownloadUtils;
//import com.sven.huinews.international.utils.LogUtil;
//import com.sven.huinews.international.utils.shot.utils.PathUtils;
//
//import java.io.File;
//import java.nio.file.Path;
//import java.util.List;
//
//public class MusicPresenter extends MusicContract.Presenter {
//    @Override
//    public void onMusic() {
//        mModel.onMusicCategroy(new DataResponseCallback<List<MusicTypeResponse.DataBean>>() {
//            @Override
//            public void onSucceed(List<MusicTypeResponse.DataBean> dataBeans) {
//                mView.setMusicType(dataBeans);
//            }
//
//            @Override
//            public void onFail(BaseResponse response) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }
//
//    @Override
//    public void onMusicList() {
//        mModel.onMusicList(mView.getMusicListRequest(), new DataResponseCallback<List<MusicListResponse.DataBean>>() {
//            @Override
//            public void onSucceed(List<MusicListResponse.DataBean> dataBeans) {
//                mView.setMusicList(dataBeans);
//            }
//
//            @Override
//            public void onFail(BaseResponse response) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }
//
//    @Override
//    public void onMusicSearch() {
//        mModel.onMusicSearch(mView.getMusicSearchRequest(), new DataResponseCallback<List<MusicListResponse.DataBean>>() {
//            @Override
//            public void onSucceed(List<MusicListResponse.DataBean> dataBeans) {
//                mView.setMusicList(dataBeans);
//            }
//
//            @Override
//            public void onFail(BaseResponse response) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }
//
//    public void getMusic(MusicListResponse.DataBean data, final boolean isUse) {
//        final String path = PathUtils.getMusicPath(data.getTitle());
//
//        boolean tmp = PathUtils.fileIsExists(path);
//        if (tmp) {
//            mView.setMusicPath(path, isUse);
//        } else {
//
//            DownloadUtils.get().download(data.getMusic_url(), path, data.getTitle(), new DownloadUtils.OnDownloadListener() {
//                @Override
//                public void onDownloadSuccess() {
//                    mView.setMusicPath(path, isUse);
//                }
//
//                @Override
//                public void onDownloading(int progress) {
//
//                }
//
//                @Override
//                public void onDownloadFailed() {
//
//                }
//            });
//        }
//
//    }
//
//
//}
