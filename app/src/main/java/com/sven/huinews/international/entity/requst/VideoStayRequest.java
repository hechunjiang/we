package com.sven.huinews.international.entity.requst;

public class VideoStayRequest extends BaseRequest {
    private String stay_time;
    private String video_id;
    private String du_type;
    private String r_type;

    public VideoStayRequest(String stay_time, String video_id, String du_type) {
        this.stay_time = stay_time;
        this.video_id = video_id;
        this.du_type = du_type;
    }

    public VideoStayRequest(String stay_time, String video_id, String du_type, String r_type) {
        this.stay_time = stay_time;
        this.video_id = video_id;
        this.du_type = du_type;
        this.r_type = r_type;
    }

    public String getR_type() {
        return r_type;
    }

    public void setR_type(String r_type) {
        this.r_type = r_type;
    }

    public String getStay_time() {
        return stay_time;
    }

    public void setStay_time(String stay_time) {
        this.stay_time = stay_time;
    }

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
