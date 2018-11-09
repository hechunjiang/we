package com.sven.huinews.international.entity.requst;

import com.google.gson.Gson;

/**
 * Created by sfy. on 2018/9/10 0010.
 */

public class FollowRequest extends BaseRequest {
    private int page;
    private int pagesize;


    public FollowRequest(int page, int pagesize) {

        this.page = page;
        this.pagesize = pagesize;
    }

    public FollowRequest() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String requestJson() {
        return new Gson().toJson(FollowRequest.class);
    }
}
