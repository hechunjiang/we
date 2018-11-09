package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sfy. on 2018/9/14 0014.
 */

public class VideoCommentLikeRequest extends BaseRequest {

    private int cid;
    private int du_type;
    private String video_id;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getDu_type() {
        return du_type;
    }

    public void setDu_type(int du_type) {
        this.du_type = du_type;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
