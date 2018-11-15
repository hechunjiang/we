package com.sven.huinews.international.main.home.model;

import android.content.Context;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.entity.requst.NewsListRequst;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.VideoLikeRequest;
import com.sven.huinews.international.entity.requst.VideoListRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlResponse;
import com.sven.huinews.international.entity.requst.VideoStayRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.NewsInfoResponse;
import com.sven.huinews.international.entity.response.NewsResponse;
import com.sven.huinews.international.entity.response.TaskResponse;
import com.sven.huinews.international.entity.response.TimeLengthRespone;
import com.sven.huinews.international.main.home.contract.HomeVideoContract;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.TimeUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.IDN;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeVideoModel extends HomeVideoContract.Model {
    private Context mContext = AppConfig.getAppContext();

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
    public void getNewsList(NewsListRequst requst, final DataResponseCallback<List<NewsInfo>> callback) {
        getRetrofit().onNewsList(requst, new DataCallBack() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                NewsInfoResponse response = new Gson().fromJson(json, NewsInfoResponse.class);
                callback.onSucceed(response.getData().getData());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callback.onFail(baseResponse);
            }
        });
    }

    @Override
    public void onVideoLike(VideoLikeRequest request, final DataCallBack callBack) {
        getRetrofit().onVideoLike(request, new DataCallBack() {
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
                            taskResponse.getData().getmCount() + " " + mContext.getString(R.string.coin_news_left);
                } else if (request.getId().equals("1005")) {
                    message = taskResponse.getData().getmCount() == 0 ?
                            mContext.getString(R.string.earn_more_in_ads_videos) :
                            taskResponse.getData().getmCount() + " " + mContext.getString(R.string.coin_news_left);
                    if (type != null) {
                        message = "+" + coins + type;
                    }
                } else {
                    message = taskResponse.getData().getmCount() == 0 ?
                            mContext.getString(R.string.earn_more_in_ads_videos) :
                            taskResponse.getData().getmCount() + " " + mContext.getString(R.string.circles_left);
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

    //接口计数不需要返回数据
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

    //获取是否可以30分钟加金币的数据
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
                        UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, (taskResponse.getData().getLast()));
                        UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_ALLLONG, (taskResponse.getData().getLast()));
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
                    callback.onSucceed(taskResponse);
                } else { //无排队码 获取排队码 并保存
                    UserSpCache.getInstance(mContext).putString(UserSpCache.REQUEST_CODE, taskResponse.getData().getF_code());
                    UserSpCache.getInstance(mContext).putString(UserSpCache.REQUEST_TIME, TimeUtils.getTime(TimeUtils.getNowDate(), taskResponse.getData().getReset()));
                    UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_LONG, (taskResponse.getData().getLast()));
                    UserSpCache.getInstance(mContext).putLong(UserSpCache.REQUEST_ALLLONG, (taskResponse.getData().getLast()));
                }
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


}
