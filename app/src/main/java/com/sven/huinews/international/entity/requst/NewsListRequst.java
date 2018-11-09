package com.sven.huinews.international.entity.requst;

/**
 * Created by sfy. on 2018/9/6 0006.
 */

public class NewsListRequst extends BaseRequest {
    public int page;
    public int r_type;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getR_type() {
        return r_type;
    }

    public void setR_type(int r_type) {
        this.r_type = r_type;
    }
}
