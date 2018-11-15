package com.sven.huinews.international.main.video.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.DisLikeVideoRequest;
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
import com.sven.huinews.international.entity.requst.VideoStayRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.CommentReponse;
import com.sven.huinews.international.entity.response.NewsResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.TaskResponse;
import com.sven.huinews.international.entity.response.TimeLengthRespone;
import com.sven.huinews.international.main.video.contract.FirstVideoDetailContract;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.TimeUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sfy. on 2018/9/12 0012.
 */

public class FirstVideoDetailModel extends FirstVideoDetailContract.Model {
    private Context mContext = AppConfig.getAppContext();

    @Override
    public void requestDetailVideoList(int type, int page, Boolean isRefresh, final DataResponseCallback<List<MyNews>> callback) {
        VideoListRequest request = new VideoListRequest();
        request.setR_type(type+"");
        request.setPage(page+"");
        getRetrofit().onVideoList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                NewsResponse newsResponse = new Gson().fromJson(json, NewsResponse.class);
                callback.onSucceed(newsResponse.getData().getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestVideoComment(VideoCommentRequest request, final DataResponseCallback<CommentReponse> callback) {
        getRetrofit().getVideoCommentList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                CommentReponse commentResponse = new Gson().fromJson(json, CommentReponse.class);
                callback.onSucceed(commentResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestAddVideoComents(AdCommentRequest request, final DataCallBack callBack) {
        getRetrofit().onVideoAdComment(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onCompleted();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }


    @Override
    public void setCollection(VideoCollectionRequest request, final DataCallBack callBack) {
        getRetrofit().onVideoCollection(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void setCancelCollection(VideoCollectionCancelRequest request, final DataCallBack callBack) {
        getRetrofit().onCollectionCancel(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getGoldByTask(final TaskRequest request, final DataResponseCallback<Map<String, String>> cacheBack, final int coins, final String type) {
        getRetrofit().getGoldByTask(request, new DataCallBack() {
            @Override
            public void onComplete() {
                cacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                TaskResponse taskResponse = new Gson().fromJson(json, TaskResponse.class);
                String message = "";
                if (request.getId().equals("1004")) {
                    message = taskResponse.getData().getmCount() == 0 ?
                            mContext.getString(R.string.earn_more_in_ads_videos) :
                            taskResponse.getData().getmCount() + " "+mContext.getString(R.string.coin_news_left);
                } else if (request.getId().equals("1005")) {
                    message = taskResponse.getData().getmCount() == 0 ?
                            mContext.getString(R.string.ads_video_left) :
                            taskResponse.getData().getmCount() + " "+mContext.getString(R.string.coin_news_left);
                    if (type != null) {
                        message = "+" + coins + type;
                    }
                } else {
                    message = taskResponse.getData().getmCount() == 0 ?
                            mContext.getString(R.string.earn_more_in_ads_videos) :
                            taskResponse.getData().getmCount() + " "+mContext.getString(R.string.coin_news_left);
                }
                Map<String, String> map = new HashMap<>();
                map.put("Count", taskResponse.getData().getCount() + "");
                map.put("message", message);
                cacheBack.onSucceed(map);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                cacheBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestSharedVideo(VideoShareUrlRequest request, final DataResponseCallback<VideoShareUrlResponse> callback) {
        getRetrofit().VideoSharedUrl(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                VideoShareUrlResponse response = new Gson().fromJson(json, VideoShareUrlResponse.class);
                callback.onSucceed(response);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestVideoDelete(VideoDeleteRequest request,final DataCallBack callback) {
        getRetrofit().VideoDelete(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callback.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }


    @Override
    public void requestSHaredVisitNumber(ShareVisitRequest request, final DataResponseCallback<String> callback) {

        getRetrofit().addVideoShardNumber(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callback.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestAliPlayUrl(String id, final DataResponseCallback<AliVideoResponse> callBack) {
        getRetrofit().getAlPlayUrl(id, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                AliVideoResponse aliVideoResponse = new Gson().fromJson(json, AliVideoResponse.class);
                callBack.onSucceed(aliVideoResponse);

            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestVideoCommentLike(VideoCommentLikeRequest request, final DataCallBack callBack) {
        getRetrofit().videoCommentLike(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                Log.d("commment", "onSucceed: " + json);
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void videoCommentReport(TipOffRequest request, final DataCallBack callBack) {
        getRetrofit().videoCommentRepost(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }


    //    视频看完加金币
    @Override
    public void getOnlineTimeLength(final TaskRequest request, final DataResponseCallback<Map<String, String>> callback, final int get_type) {

        getRetrofit().getGoldByTask(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                mContext = AppConfig.getAppContext();
                TimeLengthRespone taskResponse = new Gson().fromJson(json, TimeLengthRespone.class);
                Map<String, String> map = new HashMap<>();
                if (request.getId().equals("1007")) {
                    if (get_type == TaskRequest.GET_CODE) { //获取code
                        UserSpCache.getInstance(mContext).putString(UserSpCache.REQUEST_CODE, taskResponse.getData().getF_code());
                        UserSpCache.getInstance(mContext).putString(UserSpCache.REQUEST_TIME, TimeUtils.getTime(TimeUtils.getNowDate(), taskResponse.getData().getReset()));
                        UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, (taskResponse.getData().getLast() * 1000));
                        UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_ALLLONG, (taskResponse.getData().getLast() * 1000));
                        map.put("netWorkType", "code");
                    } else if (get_type == TaskRequest.GET_CONIS) { //获取金币
                        UserSpCache.getInstance(mContext).putString(UserSpCache.REQUEST_CODE, "");
                        UserSpCache.getInstance(mContext).putString(UserSpCache.REQUEST_TIME, "");
                        UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, -1);
                        UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_ALLLONG, -1);
                        map.put("netWorkType", "gold");
                        map.put("Count", taskResponse.getData().getGold_flag() + "");
                        map.put("message", taskResponse.getMsg());
                    }
                    callback.onSucceed(map);
                }
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });


    }

    @Override
    public void requestThirtyGold(final TaskRequest taskRequest, final DataResponseCallback<TimeLengthRespone> callback) {
        getRetrofit().getGoldByTask(taskRequest, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                TimeLengthRespone taskResponse = new Gson().fromJson(json, TimeLengthRespone.class);
                if (taskRequest.getF_code() != null) { //请求参数无排队码
                    UserSpCache.getInstance(mContext).putString(UserSpCache.REQUEST_CODE, taskResponse.getData().getF_code());
                    UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, taskResponse.getData().getLast());
                } else { //无排队码 获取排队码 并保存
                    callback.onSucceed(taskResponse);
                }
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void disLikeVideo(DisLikeVideoRequest request,final DataCallBack callback) {
        getRetrofit().getDisLikeVideo(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callback.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void videoStay(VideoStayRequest request,final DataCallBack callBack) {
        getRetrofit().getVideoStay(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getVideoList(VideoListRequest request, final DataResponseCallback<List<MyNews>> callback) {
        getRetrofit().onVideoList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                NewsResponse newsResponse = new Gson().fromJson(json, NewsResponse.class);
                callback.onSucceed(newsResponse.getData().getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void requestFollowList(FollowRequest request, final DataCallBack callback) {
        getRetrofit().getFollowList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                callback.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getUserDetailsLists(PersonWorkRequest request, final DataResponseCallback<PerSonWorkResponse> callBack) {
        getRetrofit().getUserDetailsLikesList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                PerSonWorkResponse workResponse = new Gson().fromJson(json, PerSonWorkResponse.class);
                callBack.onSucceed(workResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getUserLikesLists(LikesRequest request, final DataResponseCallback<PersonLikeResponse> callBack) {
        getRetrofit().getUserLikesList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                PersonLikeResponse likeResponse = new Gson().fromJson(json, PersonLikeResponse.class);
                callBack.onSucceed(likeResponse);

            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    //视频统计
    public void setVideoStatistics(VideoStatisticsRequest request, final DataCallBack dataCacheBack) {
        getRetrofit().videoStatistics(request, new DataCallBack() {
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
