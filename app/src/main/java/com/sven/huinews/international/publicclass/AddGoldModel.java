package com.sven.huinews.international.publicclass;

import android.content.Context;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.entity.requst.GetboxtimeRequst;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.VideoPlayTimeSize;
import com.sven.huinews.international.entity.response.GetGoldTimeResponse;
import com.sven.huinews.international.entity.response.TaskOpenBoxResponse;
import com.sven.huinews.international.entity.response.TaskResponse;
import com.sven.huinews.international.entity.response.TaskSingInResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：burgess by Burgess on 2018/9/24 02:09
 * 作用：watchEarn
 */
public class AddGoldModel {
    MyRetrofit mMyRetrofit;

    public AddGoldModel(Context context) {
        mMyRetrofit = new MyRetrofit(context);
    }

    public void getGoldByTask(final TaskRequest request, final DataResponseCallback cacheBack, final int coins, final String type) {
        String requestsss = new Gson().toJson(request);
        mMyRetrofit.getGoldByTask(request, new DataCallBack() {
            @Override
            public void onComplete() {
                cacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                Map<String, String> map = new HashMap<>();
                if (request.getId().equals(TaskRequest.TASK_ID_OPEN_BOX+"")){
                    TaskOpenBoxResponse taskResponse = new Gson().fromJson(json, TaskOpenBoxResponse.class);
                    map.put("Count", taskResponse.getData().getGold_tribute() + "");
                    map.put("message", taskResponse.getMsg());
                }else{
                    TaskSingInResponse taskSingInResponse = new Gson().fromJson(json,TaskSingInResponse.class);
                    map.put("Count", taskSingInResponse.getData().getGold_flag()+ "");
                    map.put("message", taskSingInResponse.getMsg());
                }
                cacheBack.onSucceed(map);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                cacheBack.onFail(baseResponse);
            }

        });
    }

    public void getGoldTime(final GetboxtimeRequst requst, final DataCallBack callBack) {

        mMyRetrofit.getGoldBoxTime(requst, new DataCallBack() {
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


    /**
     * 看新手视频加金币
     * */
    public void addIntroduceVideoCoid(final DataCallBack callBack) {
        mMyRetrofit.addIntroduceVideoCoid(new DataCallBack() {
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


    /**
     * 看新手视频加金币
     * */
    public void videoPlayTime(VideoPlayTimeSize videoPlayTimeSize, final DataCallBack callBack) {
        mMyRetrofit.videoPlayTime(videoPlayTimeSize,new DataCallBack() {
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


    public void getExcitingVideo(final DataCallBack cacheBack) {
        mMyRetrofit.getExcitingVideo(new DataCallBack() {
            @Override
            public void onComplete() {
                cacheBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                cacheBack.onSucceed(json);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                cacheBack.onFail(baseResponse);
            }

        });
    }

}
