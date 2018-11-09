package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sven on 2018/2/5.
 */

public class TaskRequestAdVideo extends BaseRequest {
    public static final int TASK_ID_READ_NEWS = 2;  //阅读视频
    public static final int TASK_ID_READ_RED_NEWS = 3;  //阅读红包新闻
    public static final int TASK_ID_READ_PUSH_NEWS = 1;  //阅读推送消息
    public static final int TASK_ID_READ_AD = 1004;  //阅读新闻
    public static final int TASK_ID_READ_VIDEO_AD = 1005;  //阅读奖励视频广告

    public static final String TASK_CODE_READ_NEWS = "key_code=usual_read";


    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
