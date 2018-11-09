package com.sven.huinews.international.entity.requst;

public class PushTokenRequest extends BaseRequest {
    private String token;
    private String topic;  //1/total,login_no ,2/total,login_in

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
