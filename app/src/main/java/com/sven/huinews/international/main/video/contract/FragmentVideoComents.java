package com.sven.huinews.international.main.video.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.PushTask;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.TipOffRequest;
import com.sven.huinews.international.entity.requst.VideoCommentLikeRequest;
import com.sven.huinews.international.entity.requst.VideoCommentRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlResponse;
import com.sven.huinews.international.entity.response.ComentsResponse;
import com.sven.huinews.international.entity.response.CommentReponse;
import com.sven.huinews.international.entity.response.PushTaskResponse;

public interface FragmentVideoComents {

    abstract class Model extends BaseModel {
        //请求评论列表
        public abstract void requestVideoConments(VideoCommentRequest request, DataResponseCallback<CommentReponse> callback);

        //增加评论
        public abstract void requestAddVideoComents(AdCommentRequest request, DataCallBack callBack);

        //评论点赞
        public abstract void requestVideoCommentLike(VideoCommentLikeRequest request, DataCallBack callBack);

        //举报
        public abstract void videoCommentReport(TipOffRequest request, DataCallBack callBack);

        //活动
        public abstract void getTaskPush(DataCallBack callBack);

        //分享视频
        public abstract void requestSharedVideo(VideoShareUrlRequest request, DataResponseCallback<VideoShareUrlResponse> callback);

        //分享计数
        public abstract void requestSHaredVisitNumber(ShareVisitRequest request, DataResponseCallback<String> callback);

        public abstract void requstIsLogin(ThirdRequest request, DataResponseCallback<String> callback);

        public abstract void getLogin(LoginRequest request, DataCallBack callBack);
    }

    interface View extends BaseView {

        //评论举报
        TipOffRequest getTipOffRequest();

        //评论请求
        AdCommentRequest getAdComentRequest();

        ThirdRequest getThirdRequest(String s);

        void loginSucceed();

        //点赞
        VideoCommentLikeRequest getVideoCommentLikes();

        VideoCommentRequest getVideoComentsRequest(String type, int page, String videoId, String sort);

        //获取评论
        void setVideoComent(CommentReponse videoComent);

        //增加评论
        void setAddComents(ComentsResponse comentsResponse);

        void showOtherTask(PushTaskResponse.DataBean task);

        VideoShareUrlRequest getVideSharedRequest(int type);

        void setSharedVideoUrl(VideoShareUrlResponse videoShareUrlResponse, int type);
    }

    abstract class Presenter extends BasePresenter
            <FragmentVideoComents.View, FragmentVideoComents.Model> {

        //获取添加评论
        public abstract void getVideoComents(VideoCommentRequest request);

        public abstract void addComents();

        //评论点赞
        public abstract void onVideoCommentLike(VideoCommentLikeRequest request);

        //举报
        public abstract void onVideoCommentReport(TipOffRequest tipOffRequest);

        //活动
        public abstract void getTaskPush();

        //分享
        public abstract void getVideoShareUrl(VideoShareUrlRequest request, final int type);

        public abstract String getShareJson(MyNews myNews, int type);

        public abstract void shareVisit(String response, String videoType, int type, String vdeoId, String duType);

        public abstract void checkLogin(String type);

        public abstract void getLogin(LoginRequest request);
    }

}
