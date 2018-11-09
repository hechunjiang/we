package wedemo.activity.down;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import wedemo.base.BaseResponse;
import wedemo.config.http.DataCallBack;
import wedemo.config.http.DataResponseCallback;
import wedemo.utils.LogUtil;

public class DownModel extends DownContract.Model {

//    @Override
//    public void onMusicCategroy(Context context,final DataResponseCallback<List<MusicTypeResponse.DataBean>> callback) {
//        getRetrofit(context).onMusicCategroy(new DataCallBack() {
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


    @Override
    public void onSDKCategroy(Context context, DownRequest request, final DataResponseCallback<List<CategoryResponse.DataBean>> callback) {
        getRetrofit(context).onSDKCategroy(request, new DataCallBack() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onSucceed(String json) {
                LogUtil.showLog(json);
                CategoryResponse response = new Gson().fromJson(json, CategoryResponse.class);
                callback.onSucceed(response.getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                LogUtil.showLog("错误");
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void onDownload(Context context, DownRequest request, final DataResponseCallback<List<DownResponse.DataBean>> callback) {
        getRetrofit(context).onSDKDown(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                DownResponse response = new Gson().fromJson(json, DownResponse.class);
                callback.onSucceed(response.getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }
}
