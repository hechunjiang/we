package com.sven.huinews.international.publicclass;

import android.content.Context;

import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.entity.requst.NewsStatisticsRequest;
import com.sven.huinews.international.entity.requst.VideoStatisticsRequest;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by Burgess on 2018/9/29 0029.
 */
public class VideoAndNewsPresenter {
    private Context mContext;
    VideoAndNewsModel mVideoAndNewsModel;

    public VideoAndNewsPresenter(Context mContext) {
        this.mContext = mContext;
        this.mVideoAndNewsModel = new VideoAndNewsModel(mContext) ;
    }


    /**
     * 视频统计
     * */
    public void videoStatistics(String video_id,String r_type,String du_type) {
        VideoStatisticsRequest request = new VideoStatisticsRequest();
        request.setVideoId(video_id);
        request.setR_type(r_type);
        request.setDu_type(du_type);
        mVideoAndNewsModel.setVideoStatistics(request, new DataCallBack() {
            @Override
            public void onSucceed(String s) {
                LogUtil.showLog("统计成功"+s);
            }

            @Override
            public void onFail(BaseResponse response) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 新闻统计
     * */
    public void newsStatistics(String news_id) {
        NewsStatisticsRequest request = new NewsStatisticsRequest();
        request.setNewsId(news_id);
        mVideoAndNewsModel.setNewsStatistics(request, new DataCallBack() {
            @Override
            public void onSucceed(String s) {
                LogUtil.showLog("统计成功"+s);
            }

            @Override
            public void onFail(BaseResponse response) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
