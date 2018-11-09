package com.sven.huinews.international.main.video.model;

import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.NewsStatisticsRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.response.TaskResponse;
import com.sven.huinews.international.main.video.contract.NewsDetailsContract;

import java.util.HashMap;
import java.util.Map;

public class NewsDetailsModel extends NewsDetailsContract.Model {

    @Override
    public void setNewsStatistics(NewsStatisticsRequest request, final DataResponseCallback<String> dataCacheBack) {
        getRetrofit().newsStatistics(request, new DataCallBack() {
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
                            AppConfig.getAppContext().getString(R.string.earn_more_in_ads_videos) :
                            taskResponse.getData().getmCount() + AppConfig.getAppContext().getString(R.string.coin_news_left);
                } else if (request.getId().equals("1005")) {
                    message = taskResponse.getData().getmCount() == 0 ?
                            AppConfig.getAppContext().getString(R.string.earn_more_in_ads_videos) :
                            taskResponse.getData().getmCount() + AppConfig.getAppContext().getString(R.string.ads_video_left);
                    if (type != null) {
                        message = "+" + coins + type;
                    }
                } else {
                    message = taskResponse.getData().getmCount() == 0 ?
                            AppConfig.getAppContext().getString(R.string.earn_more_in_ads_videos) :
                            taskResponse.getData().getmCount() + AppConfig.getAppContext().getString(R.string.coin_news_left);
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
}
