package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sfy. on 2018/9/6 0006.
 */

public class LoginRequest extends BaseRequest {

    @SerializedName("mail")
    private String account;

    @SerializedName("pass")
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
