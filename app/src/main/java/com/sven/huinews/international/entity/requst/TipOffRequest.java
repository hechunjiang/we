package com.sven.huinews.international.entity.requst;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sfy. on 2018/9/19 0019.
 */

//举报
public class TipOffRequest  extends BaseRequest{
    private String video_id;
    private String du_type;
    private String cid;

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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
