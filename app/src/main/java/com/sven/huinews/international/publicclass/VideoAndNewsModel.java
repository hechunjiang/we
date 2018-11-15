package com.sven.huinews.international.publicclass;

import android.content.Context;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.entity.requst.BaseRequest;
import com.sven.huinews.international.entity.requst.NewsStatisticsRequest;
import com.sven.huinews.international.entity.requst.VideoStatisticsRequest;

/**
 * Created by Burgess on 2018/9/29 0029.
 */
public class VideoAndNewsModel {
    MyRetrofit mMyRetrofit;
    public VideoAndNewsModel(Context context) {
        mMyRetrofit = new MyRetrofit(context);
    }

    public void setVideoStatistics(VideoStatisticsRequest request, final DataCallBack dataCacheBack) {
        mMyRetrofit.videoStatistics(request, new DataCallBack() {
            @Override
            public void onComplete() {
                dataCacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                dataCacheBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                dataCacheBack.onFail(baseResponse);
            }
        });
    }




    public void videoLookTask(BaseRequest request, final DataCallBack dataCacheBack) {
        mMyRetrofit.videoLookTask(request, new DataCallBack() {
            @Override
            public void onComplete() {
                dataCacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                dataCacheBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                dataCacheBack.onFail(baseResponse);
            }
        });
    }


    public void setNewsStatistics(NewsStatisticsRequest request, final DataCallBack dataCacheBack) {
        mMyRetrofit.newsStatistics(request, new DataCallBack() {
            @Override
            public void onComplete() {
                dataCacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                dataCacheBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                dataCacheBack.onFail(baseResponse);
            }
        });
    }

}
