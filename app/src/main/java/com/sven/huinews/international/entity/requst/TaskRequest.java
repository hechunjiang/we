package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sven on 2018/2/5.
 */

public class TaskRequest extends BaseRequest {
    public static final int TASK_ID_READ_NEWS = 2;  //阅读视频
    public static final int TASK_ID_OPEN_BOX = 23;  //开宝箱
    public static final int TASK_ID_READ_RED_NEWS = 3;  //阅读红包新闻
    public static final int TASK_ID_READ_PUSH_NEWS = 1;  //阅读推送消息
    public static final int TASK_ID_READ_AD = 1004;  //阅读新闻
    public static final int TASK_ID_READ_VIDEO_AD = 1005;  //阅读奖励视频广告
    public static final int TASK_ID_READ_SHARE_AD = 8;  //分享twer广告
    public static final int TASK_ID_READ_TIME_LONG = 1007;  //阅读奖励视频广告
    public static final int GET_CODE = 9;  //获取code
    public static final int GET_CONIS = 10;  //获取金币
    public static final int TASK_ID_SIGN_IN = 22;//签到

    public static final String TASK_CODE_READ_NEWS = "key_code=usual_read";


    @SerializedName("id")
    private String id;

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    @SerializedName("type_name")
    private String type_name;

    @SerializedName("type_id")
    private String type_id;

    @SerializedName("f_code")
    private String f_code;

    public String getF_code() {
        return f_code;
    }

    public void setF_code(String f_code) {
        this.f_code = f_code;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
