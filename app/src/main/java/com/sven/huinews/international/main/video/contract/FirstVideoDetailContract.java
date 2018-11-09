package com.sven.huinews.international.main.video.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.TipOffRequest;
import com.sven.huinews.international.entity.requst.VideoCollectionCancelRequest;
import com.sven.huinews.international.entity.requst.VideoCollectionRequest;
import com.sven.huinews.international.entity.requst.VideoCommentLikeRequest;
import com.sven.huinews.international.entity.requst.VideoCommentRequest;
import com.sven.huinews.international.entity.requst.VideoDeleteRequest;
import com.sven.huinews.international.entity.requst.VideoListRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlResponse;
import com.sven.huinews.international.entity.requst.VideoStatisticsRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.ComentsResponse;
import com.sven.huinews.international.entity.response.CommentReponse;
import com.sven.huinews.international.entity.response.FollowVideoResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.TimeLengthRespone;

import java.util.List;
import java.util.Map;

/**
 * Created by sfy. on 2018/9/12 0012.
 */

public interface FirstVideoDetailContract {
    abstract class Model extends BaseModel {

        public abstract void requestDetailVideoList(int type, int page, final Boolean isRefresh, DataResponseCallback<List<MyNews>> callback);

        public abstract void requestVideoComment(VideoCommentRequest request, DataResponseCallback<CommentReponse> callback);

        //增加评论
        public abstract void requestAddVideoComents(AdCommentRequest request, DataCallBack callBack);

        public abstract void setCollection(VideoCollectionRequest request, final DataCallBack callBack);

        public abstract void setCancelCollection(VideoCollectionCancelRequest request, DataCallBack callBack);

        public abstract void getGoldByTask(final TaskRequest request, final DataResponseCallback<Map<String, String>> cacheBack, final int coins, final String type);

        public abstract void requestSharedVideo(VideoShareUrlRequest request, DataResponseCallback<VideoShareUrlResponse> callback);

        public abstract void requestVideoDelete(VideoDeleteRequest request, DataCallBack callback);

        //分享计数
        public abstract void requestSHaredVisitNumber(ShareVisitRequest request, DataResponseCallback<String> callback);

        //获取阿里云播放地址
        public abstract void requestAliPlayUrl(String id, DataResponseCallback<AliVideoResponse> callBack);

        //评论点赞
        public abstract void requestVideoCommentLike(VideoCommentLikeRequest request, DataCallBack callBack);

        //举报
        public abstract void videoCommentReport(TipOffRequest request, DataCallBack callBack);

        //时间到达加金币
        public abstract void getOnlineTimeLength(TaskRequest taskRequest, DataResponseCallback<Map<String, String>> callback, final int get_type);

        //视频列表
        public abstract void getVideoList(VideoListRequest request, DataResponseCallback<List<MyNews>> callback);

        public abstract void requestFollowList(FollowRequest request, DataCallBack callback);


        public abstract void getUserDetailsLists(final PersonWorkRequest request, final DataResponseCallback<PerSonWorkResponse> callBack);

        public abstract void getUserLikesLists(final LikesRequest request, final DataResponseCallback<PersonLikeResponse> callBack);

        //视频统计
        public abstract void setVideoStatistics(VideoStatisticsRequest request, DataCallBack callBack);

        //30分钟加金币

        public abstract void requestThirtyGold(TaskRequest taskRequest, DataResponseCallback<TimeLengthRespone> callback);
    }

    interface View extends BaseView {
        VideoCommentRequest getVideoCommentRequest(String du_type, int page, String videoId, String sort);

        void getVideoDetailList(List<MyNews> myNews);

        void setComment(CommentReponse videoComent);

        AdCommentRequest getAdComentRequest();

        void setAddComents(ComentsResponse comentsResponse);

        VideoCollectionRequest getVideoCollectionRequest(MyNews myNews);

        VideoCollectionCancelRequest getVideoCollectionCancelRequest(String videoId);

        void showGoldCome(int count, int type, String masgess);

        //视频分享参数
        VideoShareUrlRequest getVideSharedRequest(int type);

        void setSharedVideoUrl(String url, int type);

        void getAliNewData(AliVideoResponse response);

        //评论举报
        TipOffRequest getTipOffRequest();

        //点赞
        VideoCommentLikeRequest getVideoCommentLikes(String videoId);

        void getCodeView();

        void showNetWorkError();

        void shareSucceed();

        VideoListRequest getVideoListRequest();

        void setVideoList(List<MyNews> mDatas);

        FollowRequest getFollowRequest();

        void setFollowData(List<FollowVideoResponse.DataBean> datas, boolean isRefresh, boolean isLoadMore);

        void hideRefresh();

        void hideLoadMore(Boolean isHide);

        void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType);

        void setPersonLikesData(PersonLikeResponse response, int selectType, boolean isRefresh);

        void videoDeleteSuccess(VideoDeleteRequest request);

        void videoDeleteError(String msg);

    }

    abstract class Presenter extends BasePresenter<FirstVideoDetailContract.View, FirstVideoDetailContract.Model> {
        public abstract void getDetailVideoList(int type, int page, final Boolean isRefresh);

        public abstract void onVideoComment(VideoCommentRequest request);

        public abstract void addComents();


        //收藏
        public abstract void onCollection(VideoCollectionRequest request);

        //取消收藏
        public abstract void onCancelCollection(VideoCollectionCancelRequest request);

        public abstract void readAnyNewsGetGold(final int type, String type_id);


        //视频分享
        public abstract void getVideoShareUrl(VideoShareUrlRequest request, int type);

        public abstract String getShareJson(MyNews myNews, int type);


        public abstract void shareVisit(String response, String videoType, int type, String videoId, String duType);

        public abstract void getAliPlayUrl(String videoUrl);

        //评论点赞
        public abstract void onVideoCommentLike(VideoCommentLikeRequest request);

        //举报
        public abstract void onVideoCommentReport(TipOffRequest tipOffRequest);


        //观看时间到达30分钟加金币
        public abstract void OnlineTimeLength(final int type, String f_code, int get_type);

        public abstract void onVideoList(boolean isRefresh);

        public abstract void getFollowList(boolean isRefresh);


        //获得真实用户Video数据
        public abstract void getUserDetailsInfo(final PersonWorkRequest request, final Boolean isRefresh, final int selectType);

        //获取用户喜欢的视频
        public abstract void getUserDetailsLikesInfo(final LikesRequest request, final Boolean isRefresh, final int selectType);

        //视频统计
        public abstract void videoStatistics(String video_id,String r_type,String du_type);

        //删除视频
        public abstract void getVideoDelete(final VideoDeleteRequest request);

        //new观看30分钟视频
        public abstract void getThirtyGold(final String id, String f_code);

    }

}
