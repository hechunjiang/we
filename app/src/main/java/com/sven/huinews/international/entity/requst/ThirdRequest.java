package com.sven.huinews.international.entity.requst;

/**
 * Created by sfy. on 2018/10/24 0024.
 */

public class ThirdRequest extends BaseRequest {
    private String mobile_brand;
    private String headimg;
    private String nickname;
    private String fb_id;
    private String fb_access_token;
    private String sex;
    private String login_source;
    private String twitter_id;
    private String lk_id;
    private String gm_id;
    private String task;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getGm_id() {
        return gm_id;
    }

    public void setGm_id(String gm_id) {
        this.gm_id = gm_id;
    }

    public String getLk_id() {
        return lk_id;
    }

    public void setLk_id(String lk_id) {
        this.lk_id = lk_id;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getMobile_brand() {
        return mobile_brand;
    }

    public void setMobile_brand(String mobile_brand) {
        this.mobile_brand = mobile_brand;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getFb_access_token() {
        return fb_access_token;
    }

    public void setFb_access_token(String fb_access_token) {
        this.fb_access_token = fb_access_token;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLogin_source() {
        return login_source;
    }

    public void setLogin_source(String login_source) {
        this.login_source = login_source;
    }
}
