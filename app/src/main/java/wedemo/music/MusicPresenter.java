package wedemo.music;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import wedemo.base.BaseResponse;
import wedemo.config.Constant;
import wedemo.config.http.DataResponseCallback;
import wedemo.utils.Constants;
import wedemo.utils.DownloadUtils;
import wedemo.utils.PathUtils;

public class MusicPresenter extends MusicContract.Presenter {
    @Override
    public void onMusic(Context context) {
        mModel.onMusicCategroy(context, new DataResponseCallback<List<MusicTypeResponse.DataBean>>() {
            @Override
            public void onSucceed(List<MusicTypeResponse.DataBean> dataBeans) {
                mView.setMusicType(dataBeans);
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.setMusicType(new ArrayList<MusicTypeResponse.DataBean>());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onMusicList(Context context) {
        mModel.onMusicList(context, mView.getMusicListRequest(), new DataResponseCallback<List<MusicListResponse.DataBean>>() {
            @Override
            public void onSucceed(List<MusicListResponse.DataBean> dataBeans) {
                mView.setMusicList(dataBeans);
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
    public void onMusicSearch(Context context) {
        mModel.onMusicSearch(context, mView.getMusicSearchRequest(), new DataResponseCallback<List<MusicListResponse.DataBean>>() {
            @Override
            public void onSucceed(List<MusicListResponse.DataBean> dataBeans) {
                mView.setMusicList(dataBeans);
            }

            @Override
            public void onFail(BaseResponse response) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private String path;
    public void getMusic(MusicListResponse.DataBean data, final boolean isUse) {

        if(data.getId() == Constant.MUSIC_STORE){
            path = data.getMusic_path();
        }else{
            path = PathUtils.getMusicPath(data.getTitle());
        }


        boolean tmp = PathUtils.fileIsExists(path);
        if (tmp) {
            mView.setMusicPath(path, isUse);
        } else {
            DownloadUtils.get().download(data.getMusic_url(), path, data.getTitle(), new DownloadUtils.OnDownloadListener() {
                @Override
                public void onDownloadSuccess() {
                    mView.setMusicPath(path, isUse);
                }

                @Override
                public void onDownloading(int progress) {

                }

                @Override
                public void onDownloadFailed() {

                }
            });
        }
    }
}
