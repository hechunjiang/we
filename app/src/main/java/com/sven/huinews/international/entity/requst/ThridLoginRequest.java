package com.sven.huinews.international.entity.requst;

public class ThridLoginRequest extends BaseRequest {
    private String login_source;
    private String user_id;

    public String getLogin_source() {
        return login_source;
    }

    public void setLogin_source(String login_source) {
        this.login_source = login_source;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
