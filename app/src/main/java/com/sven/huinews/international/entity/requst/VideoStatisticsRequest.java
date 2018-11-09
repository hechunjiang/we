package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class VideoStatisticsRequest extends BaseRequest {
    @SerializedName("video_id")
    private String videoId;

    @SerializedName("r_type")
    private String r_type;

    @SerializedName("du_type")
    private String du_type;

    public String getDu_type() {
        return du_type;
    }

    public void setDu_type(String du_type) {
        this.du_type = du_type;
    }

    public String getR_type() {
        return r_type;
    }

    public void setR_type(String r_type) {
        this.r_type = r_type;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
