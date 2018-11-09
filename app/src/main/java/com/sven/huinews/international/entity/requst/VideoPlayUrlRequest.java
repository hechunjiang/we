package com.sven.huinews.international.entity.requst;

/**
 * Created by sfy. on 2018/9/17 0017.
 */

public class VideoPlayUrlRequest {
    private String aliyunVideoId;

    public VideoPlayUrlRequest(String aliyunVideoId) {
        this.aliyunVideoId = aliyunVideoId;
    }

    public String getAliyunVideoId() {
        return aliyunVideoId;
    }

    public void setAliyunVideoId(String aliyunVideoId) {
        this.aliyunVideoId = aliyunVideoId;
    }
}
