package com.sven.huinews.international.entity.requst;

/**
 * Created by sfy. on 2018/9/27 0027.
 */

public class SmsCodeRequest extends BaseRequest {
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
