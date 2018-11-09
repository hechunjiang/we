package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

public class VideoShareUrlRequest extends BaseRequest {
    @SerializedName("video_id")
    private String videoId;
    @SerializedName("to_platfrom")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
