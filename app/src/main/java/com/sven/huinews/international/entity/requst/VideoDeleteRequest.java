package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：burgess by Burgess on 2018/10/25 10:56
 * 作用：watchEarn
 */
public class VideoDeleteRequest extends BaseRequest {
    @SerializedName("video_id")
    private String videoId;
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
