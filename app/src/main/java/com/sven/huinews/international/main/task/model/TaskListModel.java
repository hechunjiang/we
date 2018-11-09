package com.sven.huinews.international.main.task.model;

import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.GetboxtimeRequst;
import com.sven.huinews.international.entity.requst.InfoRequest;
import com.sven.huinews.international.entity.requst.TaskFinishRequest;
import com.sven.huinews.international.entity.requst.TaskListRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.response.InfoResponse;
import com.sven.huinews.international.entity.response.TaskFinishResponse;
import com.sven.huinews.international.entity.response.TaskListNewResponse;
import com.sven.huinews.international.entity.response.TaskListResponse;
import com.sven.huinews.international.entity.response.TaskOpenBoxResponse;
import com.sven.huinews.international.entity.response.TaskSingInResponse;
import com.sven.huinews.international.main.task.contract.TaskContract;

import java.util.HashMap;
import java.util.Map;

public class TaskListModel extends TaskContract.Model {
    @Override
    public void getTaskList(TaskListRequest request, final DataResponseCallback<TaskListResponse> callBack) {
        getRetrofit().getTaskList(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                TaskListResponse newsResponse = new Gson().fromJson(json, TaskListResponse.class);
                callBack.onSucceed(newsResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getTaskListNew(TaskListRequest request,final DataResponseCallback<TaskListNewResponse> callBack) {
        getRetrofit().getTaskListNew(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                TaskListNewResponse newsResponse = new Gson().fromJson(json, TaskListNewResponse.class);
                callBack.onSucceed(newsResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void taskFinish(TaskFinishRequest request, final DataResponseCallback<TaskFinishResponse> callBack) {
        getRetrofit().taskFinish(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                TaskFinishResponse newsResponse = new Gson().fromJson(json, TaskFinishResponse.class);
                callBack.onSucceed(newsResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getInfo(InfoRequest request,final DataResponseCallback<InfoResponse> callBack) {
        getRetrofit().getTaskInfo(request, new DataCallBack() {
            @Override
            public void onComplete() {
                callBack.onComplete();
            }

            @Override
            public void onSucceed(String json) {
                InfoResponse newsResponse = new Gson().fromJson(json, InfoResponse.class);
                callBack.onSucceed(newsResponse);
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                callBack.onFail(baseResponse);
            }
        });
    }

    @Override
    public void getOpenTreasureBox(final TaskRequest request,final DataResponseCallback<Map<String, String>> cacheBack) {
        getRetrofit().getGoldByTask(request, new DataCallBack() {
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

    @Override
    public void getGoldTime(GetboxtimeRequst requst,final DataCallBack callBack) {
        getRetrofit().getGoldBoxTime(requst, new DataCallBack() {
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
