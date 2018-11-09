package com.sven.huinews.international.entity.requst;

/**
 * 作者：burgess by Burgess on 2018/10/25 19:49
 * 作用：watchEarn
 */
public class VideoPlayTimeSize extends BaseRequest {
    private int use_time;
    private String video_id;

    public int getUse_time() {
        return use_time;
    }

    public void setUse_time(int use_time) {
        this.use_time = use_time;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
