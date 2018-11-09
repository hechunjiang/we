package com.sven.huinews.international.entity.requst;

import retrofit2.http.PUT;

/**
 * Created by Burgess on 2018/9/20 0020.
 */
public class FansAndFollowRequest extends BaseRequest {
    private String other_id;
    private int du_type;
    private int page;
    private int page_size;

    public FansAndFollowRequest(){

    }


    public FansAndFollowRequest(String other_id, int du_type) {
        this.other_id = other_id;
        this.du_type = du_type;
    }

    public FansAndFollowRequest(String other_id, int du_type, int page, int page_size) {
        this.other_id = other_id;
        this.du_type = du_type;
        this.page = page;
        this.page_size = page_size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public String getOther_id() {
        return other_id;
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

    public int getDu_type() {
        return du_type;
    }

    public void setDu_type(int du_type) {
        this.du_type = du_type;
    }
}
