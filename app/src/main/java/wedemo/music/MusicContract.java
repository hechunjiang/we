package wedemo.music;

import android.content.Context;

import java.util.List;

import wedemo.base.BaseModel;
import wedemo.base.BasePresenter;
import wedemo.base.BaseView;
import wedemo.config.http.DataResponseCallback;

public interface MusicContract {
    abstract class Model extends BaseModel {
        public abstract void onMusicCategroy(Context context, DataResponseCallback<List<MusicTypeResponse.DataBean>> callback);

        public abstract void onMusicList(Context context, MusicListRequest request, DataResponseCallback<List<MusicListResponse.DataBean>> callback);

        public abstract void onMusicSearch(Context context, MusicSearchRequest request, DataResponseCallback<List<MusicListResponse.DataBean>> callback);
    }

    interface View extends BaseView {
        void setMusicType(List<MusicTypeResponse.DataBean> mMusicTypes);

        MusicListRequest getMusicListRequest();

        void setMusicList(List<MusicListResponse.DataBean> mDatas);

        MusicSearchRequest getMusicSearchRequest();

        void setMusicPath(String path, boolean isUse);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void onMusic(Context context);

        public abstract void onMusicList(Context context);

        public abstract void onMusicSearch(Context context);
    }
}
