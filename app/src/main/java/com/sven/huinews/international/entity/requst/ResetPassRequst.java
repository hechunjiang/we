package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class ResetPassRequst extends BaseRequest {
    @SerializedName("mail")
    private String phone;
    private String pass;
    @SerializedName("verify")
    private String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
