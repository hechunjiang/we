package com.sven.huinews.international.main.task.contract;

import com.sven.huinews.international.base.BaseModel;
import com.sven.huinews.international.base.BasePresenter;
import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.DataResponseCallback;
import com.sven.huinews.international.entity.requst.GetboxtimeRequst;
import com.sven.huinews.international.entity.requst.InfoRequest;
import com.sven.huinews.international.entity.requst.TaskFinishRequest;
import com.sven.huinews.international.entity.requst.TaskListRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.response.InfoResponse;
import com.sven.huinews.international.entity.response.MessageResponse;
import com.sven.huinews.international.entity.response.TaskFinishResponse;
import com.sven.huinews.international.entity.response.TaskListNewResponse;
import com.sven.huinews.international.entity.response.TaskListResponse;

import java.util.Map;

public interface TaskContract {

    /**
     * 新闻列表
     */

    abstract class Model extends BaseModel {
        public abstract void getTaskList(TaskListRequest request, DataResponseCallback<TaskListResponse> callBack);

        public abstract void getTaskListNew(TaskListRequest request, DataResponseCallback<TaskListNewResponse> callBack);

        public abstract void taskFinish(TaskFinishRequest request, DataResponseCallback<TaskFinishResponse> callBack);

        public abstract void getInfo(InfoRequest request, DataResponseCallback<InfoResponse> callBack);


        public abstract void getOpenTreasureBox(TaskRequest taskRequest, DataResponseCallback<Map<String, String>> callback);

        public abstract void getGoldTime(GetboxtimeRequst requst, DataCallBack callBack);
    }

    interface View extends BaseView {
        void setTask(TaskListResponse response);

        void setTask(TaskListNewResponse response);

        void showGoldCome(int count, int type, String masgess);

        void showGoldTime(int time);

        void taskFinish(TaskFinishResponse response);

        void getInfo(InfoResponse response);
    }

    abstract class Presenter extends BasePresenter<TaskContract.View, TaskContract.Model> {

        public abstract void getTaskList(TaskListRequest request);

        public abstract void getTaskListNew(TaskListRequest request);

        public abstract void getOpenTreasureBox(int id);//开宝箱

        public abstract void getGoldTime();//获取时间

        public abstract void taskFinish(TaskFinishRequest request);

        public abstract void getInfo(InfoRequest request); //获取滚动数据

    }
}
