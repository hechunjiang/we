package com.sven.huinews.international.entity.requst;

/**
 * Created by sfy. on 2018/9/15 0015.
 */

public class VideoLikeRequest extends BaseRequest {
    private String video_id;
    private String du_type;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getDu_type() {
        return du_type;
    }

    public void setDu_type(String du_type) {
        this.du_type = du_type;
    }
}
