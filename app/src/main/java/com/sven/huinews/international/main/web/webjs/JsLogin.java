package com.sven.huinews.international.main.web.webjs;

import com.google.gson.annotations.SerializedName;

/**
 * auther: sunfuyi
 * data: 2018/5/14
 * effect:
 */
public class JsLogin {
    @SerializedName("ticket")
    private String loginTicket;
    @SerializedName("os")
    private String systemName;

    @SerializedName("meid")
    private String meid;
    private String sign;
    private String version;


    public void setVersion(String version) {
        this.version = version;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getLoginTicket() {
        return loginTicket;
    }

    public void setLoginTicket(String loginTicket) {
        this.loginTicket = loginTicket;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
