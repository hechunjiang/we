package com.sven.huinews.international.entity.requst;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class UserCommentRequest extends BaseRequest {
    private String du_type;
    private String other_id;

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
}
