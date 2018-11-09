package com.sven.huinews.international.entity.requst;

public class VideoListRequest extends BaseRequest {
    private String page;
    private String r_type;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getR_type() {
        return r_type;
    }

    public void setR_type(String r_type) {
        this.r_type = r_type;
    }



}
