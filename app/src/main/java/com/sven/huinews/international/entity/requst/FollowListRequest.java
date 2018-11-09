package com.sven.huinews.international.entity.requst;

/**
 * Created by sfy. on 2018/9/19 0019.
 */

public class FollowListRequest extends BaseRequest {
    private String page;
    private String page_size;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }
}
