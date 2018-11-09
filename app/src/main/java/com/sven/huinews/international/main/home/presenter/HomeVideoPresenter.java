package com.sven.huinews.international.main.home.presenter;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.entity.jspush.JsShareResponse;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.VideoLikeRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlResponse;
import com.sven.huinews.international.entity.requst.VideoStatisticsRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.TimeLengthRespone;
import com.sven.huinews.international.main.home.contract.HomeVideoContract;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.TaskModel;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.util.List;
import java.util.Map;

public class HomeVideoPresenter extends HomeVideoContract.Presenter {
    private TaskModel mTaskModel = new TaskModel(AppConfig.getAppContext());

    @Override
    public void onVideoList(boolean isRefresh) {
        mModel.getVideoList(mView.getVideoListRequest(), new DataResponseCallback<List<MyNews>>() {

            @Override
            public void onSucceed(List<MyNews> myNews) {
                mView.setVideoList(myNews);
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(), response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onNewsList(final boolean isRefresh) {
        mModel.getNewsList(mView.getNewsListRequst(), new DataResponseCallback<List<NewsInfo>>() {

            @Override
            public void onSucceed(List<NewsInfo> newsInfos) {
                mView.setNewsList(newsInfos, isRefresh);
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(), response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onVideoLike(VideoLikeRequest request) {
        mModel.onVideoLike(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
//                ToastUtils.showShort(mContext, mContext.getString(R.string.successfull));
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mView.showErrorTip(baseResponse.getCode(), baseResponse.getMsg());
            }
        });
    }

    @Override
    public void getAliPlayUrl(String videoUrl) {
        mModel.requestAliPlayUrl(videoUrl, new DataResponseCallback<AliVideoResponse>() {
            @Override
            public void onSucceed(AliVideoResponse response) {
                mView.getAliNewData(response);
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(), response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void readAnyNewsGetGold(final int type, String type_id) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setId(type + "");
        if (type == TaskRequest.TASK_ID_READ_AD) {//新闻
            taskRequest.setType_name("news");
        } else if (type == TaskRequest.TASK_ID_READ_NEWS) { //视频
            taskRequest.setType_name("video");
        }
        taskRequest.setType_id(type_id);//页面id
        mModel.getGoldByTask(taskRequest, new DataResponseCallback<Map<String, String>>() {
            @Override
            public void onSucceed(Map<String, String> map) {
                mView.showGoldComes(Integer.parseInt(map.get("Count")), type, map.get("message"));
            }

            @Override
            public void onFail(BaseResponse response) {
                if (response.getCode() == Api.API_CODE_GOLD_FAILD) {
                    mView.showGoldComes(0, type, "");
                    //Toast提示用户无法获取更多金币
                    ToastUtils.showLong(mContext, response.getMsg());
                } else if (response.getCode() == -1000001) {
//                        mVideoView.showNetWorkError();
                    ToastUtils.showLong(mContext, mContext.getString(R.string.network_unavailable_try_again_later));
                }
            }

            @Override
            public void onComplete() {

            }
        }, 0, null);
    }

    @Override
    public void getVideoShareUrl(VideoShareUrlRequest request, final int type) {
        mModel.requestSharedVideo(request, new DataResponseCallback<VideoShareUrlResponse>() {
            @Override
            public void onSucceed(VideoShareUrlResponse videoShareUrlResponse) {
                mView.setSharedVideoUrl(videoShareUrlResponse.getData().getUrl(), type);
            }

            @Override
            public void onFail(BaseResponse response) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    //将分享数据转化为具体的类
    @Override
    public String getShareJson(MyNews myNews, int type) {
        JsShareType jsShareType = new JsShareType();
        jsShareType.setType(type);
        jsShareType.setTitle(myNews.getTitle());
        jsShareType.setUrl(myNews.getVideoUrl());
        jsShareType.setContent(myNews.getTitle());
        jsShareType.setImgUrl(myNews.getCoverUrl());
        return new Gson().toJson(jsShareType);

    }

    //分享计数
    @Override
    public void shareVisit(String response, String videoType, int type, String videoId, String duType) {
        JsShareResponse mJsShareResponse = new Gson().fromJson(response, JsShareResponse.class);
        ShareVisitRequest request = new ShareVisitRequest();
        request.setActivityType(videoType);
        request.setCode(mJsShareResponse.getCode() + "");
        request.setShareChannel(type + "");
        request.setVideoId(videoId);
        request.setDuType(duType);
        mModel.requestSHaredVisitNumber(request, new DataResponseCallback<String>() {
            @Override
            public void onSucceed(String s) {
                LogUtil.showLog("twtter 接口分享成功");
                LogUtil.showLog("succeed");
            }

            @Override
            public void onFail(BaseResponse response) {
                LogUtil.showLog("twtter 接口分享失败");
                LogUtil.showLog("onFail");
            }

            @Override
            public void onComplete() {
                LogUtil.showLog("onComplete");
            }
        });

    }

    @Override
    public void OnlineTimeLength(final int type, String f_code, final int get_type) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setId(type + "");
        taskRequest.setF_code(f_code);//后台返回参数  key值
        mModel.getOnlineTimeLength(taskRequest, new DataResponseCallback<Map<String, String>>() {
            @Override
            public void onSucceed(Map<String, String> map) {
                if (map.get("netWorkType").equals("gold")) {
                    mView.showGoldCome(Integer.parseInt(map.get("Count")), get_type, map.get("message"));
                } else if (map.get("netWorkType").equals("code")) {
                    mView.getCodeView();
                }
            }

            @Override
            public void onFail(BaseResponse response) {
                if (response.getCode() == Api.API_CODE_GOLD_FAILD) {
                    mView.showGoldCome(0, type, "");
                } else if (response.getCode() == -1000001) {
                    mView.showNetWorkError();
                }
            }


            @Override
            public void onComplete() {

            }
        }, get_type);

    }

    @Override
    public void getThirtyGold(String id, String f_code) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setId(id);
        taskRequest.setF_code(f_code);
        mModel.requestThirtyGold(taskRequest, new DataResponseCallback<TimeLengthRespone>() {
            @Override
            public void onSucceed(TimeLengthRespone respone) {
                mView.showGoldCome(respone.getData().getGold_flag(), 30, respone.getMsg());
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(), response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });

    }


    public void addReadNews(Object data) {
        String id = "";
        if (data instanceof MyNews) {
            MyNews myNews = (MyNews) data;
            id = myNews.getId();
        } else if (data instanceof NewsInfo) {
            NewsInfo newsInfo = (NewsInfo) data;
            id = newsInfo.getId() + "";
        }
        mTaskModel.addReadNews(id);

    }

    public void saveGoldOpenCount() {
        int count = UserSpCache.getInstance(mContext).getInt(UserSpCache.OPEN_GOLD_COUNT);
        count++;
        LogUtil.showLog("msg--- count：" + count);
        UserSpCache.getInstance(mContext).putInt(UserSpCache.OPEN_GOLD_COUNT, count);
    }

    public boolean isCanGetCoinByReadNews(Object data) {
        mTaskModel = new TaskModel(AppConfig.getAppContext());
        return mTaskModel.readNewsCanGetCoin(data);

    }



}
