package com.sven.huinews.international.main.home.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.NewsListRequst;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.SharedRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.VideoLikeRequest;
import com.sven.huinews.international.entity.requst.VideoListRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlResponse;
import com.sven.huinews.international.entity.requst.VideoStayRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.TimeLengthRespone;

import java.util.List;
import java.util.Map;

public interface HomeVideoContract {

    abstract class Model extends BaseModel {
        //视频列表
        public abstract void getVideoList(VideoListRequest request, DataResponseCallback<List<MyNews>> callback);

        //新闻列表
        public abstract void getNewsList(NewsListRequst requst, DataResponseCallback<List<NewsInfo>> callback);

        //点赞
        public abstract void onVideoLike(VideoLikeRequest request, DataCallBack callBack);

        //获取阿里云播放地址
        public abstract void requestAliPlayUrl(String id, DataResponseCallback<AliVideoResponse> callBack);

        public abstract void getGoldByTask(final TaskRequest request, final DataResponseCallback<Map<String, String>> cacheBack, final int coins, final String type);

        //分享视频
        public abstract void requestSharedVideo(VideoShareUrlRequest request, DataResponseCallback<VideoShareUrlResponse> callback);

        //分享计数
        public abstract void requestSHaredVisitNumber(ShareVisitRequest request, DataResponseCallback<String> callback);

        //时间到达加金币
        public abstract void getOnlineTimeLength(TaskRequest taskRequest, DataResponseCallback<Map<String, String>> callback, final int get_type);

        //30分钟加金币

        public abstract void requestThirtyGold(TaskRequest taskRequest, DataResponseCallback<TimeLengthRespone> callback);

        public abstract void videoStay(VideoStayRequest request,DataCallBack callBack);
    }

    interface View extends BaseView {
        VideoListRequest getVideoListRequest();

        NewsListRequst getNewsListRequst();

        //视频分享参数
        VideoShareUrlRequest getVideSharedRequest(int type);


        void setVideoList(List<MyNews> mDatas);

        void setNewsList(List<NewsInfo> mDatas, boolean isRefresh);

        void getAliNewData(AliVideoResponse response);

        void showGoldCome(int count, int type, String masgess);

        void setSharedVideoUrl(String url, int type);

        void getCodeView();

        void showNetWorkError();

        void showGoldComes(int count, int type, String masgess);
    }

    abstract class Presenter extends BasePresenter<HomeVideoContract.View, HomeVideoContract.Model> {
        public abstract void onVideoList(boolean isRefresh);

        public abstract void onNewsList(boolean isRefresh);

        public abstract void onVideoLike(VideoLikeRequest request);

        public abstract void getAliPlayUrl(String videoUrl);

        public abstract void readAnyNewsGetGold(final int type, String type_id);

        //视频分享
        public abstract void getVideoShareUrl(VideoShareUrlRequest request, int type);

        public abstract String getShareJson(MyNews myNews, int type);


        public abstract void shareVisit(String response, String videoType, int type, String vdeoId, String duType);

        //观看时间到达30分钟加金币
        public abstract void OnlineTimeLength(final int type, String f_code, int get_type);

        //new观看30分钟视频
        public abstract void getThirtyGold(final String id, String f_code);

        public abstract void videoStay(VideoStayRequest request);

    }
}
