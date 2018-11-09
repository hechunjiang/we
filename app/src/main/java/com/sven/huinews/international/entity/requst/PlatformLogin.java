package com.sven.huinews.international.entity.requst;

/**
 * Created by sfy. on 2018/9/17 0017.
 */

public class PlatformLogin extends BaseRequest {
    private String twitter_id;
    private String fb_id;
    private String lk_id;
    private String platform;

    public String getLk_id() {
        return lk_id;
    }

    public void setLk_id(String lk_id) {
        this.lk_id = lk_id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }
}
