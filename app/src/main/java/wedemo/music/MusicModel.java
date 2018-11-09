package wedemo.music;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import wedemo.base.BaseResponse;
import wedemo.config.http.DataCallBack;
import wedemo.config.http.DataResponseCallback;
import wedemo.utils.LogUtil;

public class MusicModel extends MusicContract.Model {

    @Override
    public void onMusicCategroy(Context context,final DataResponseCallback<List<MusicTypeResponse.DataBean>> callback) {
        getRetrofit(context).onMusicCategroy(new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showJson("onMusicCategroy", json);
                MusicTypeResponse response = new Gson().fromJson(json, MusicTypeResponse.class);
                callback.onSucceed(response.getData());

            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void onMusicList(Context context, final MusicListRequest request, final DataResponseCallback<List<MusicListResponse.DataBean>> callback) {
        getRetrofit(context).onMusicList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                MusicListResponse response = new Gson().fromJson(json, MusicListResponse.class);
                callback.onSucceed(response.getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void onMusicSearch(Context context, MusicSearchRequest request, final DataResponseCallback<List<MusicListResponse.DataBean>> callback) {
        getRetrofit(context).onMusicSearch(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showJson("onMusicSearch", json);
                MusicListResponse response = new Gson().fromJson(json, MusicListResponse.class);
                callback.onSucceed(response.getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }


}
