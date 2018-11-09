package com.sven.huinews.international.entity.requst;

import com.sven.huinews.international.base.BaseResponse;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class PersonWorkRequest extends BaseRequest {
    private String du_type;
    private String other_id;
    private String page;
    private String page_size;

    public String getDu_type() {
        return du_type;
    }

    public void setDu_type(String du_type) {
        this.du_type = du_type;
    }

    public String getOther_id() {
        return other_id;
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

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
