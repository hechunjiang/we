package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class GetCodeRequest extends BaseRequest {
    @SerializedName("type")
    private String type;   //reg , findpwd
    private String mail;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
