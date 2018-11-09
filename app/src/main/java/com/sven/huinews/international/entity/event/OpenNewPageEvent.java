package com.sven.huinews.international.entity.event;

/**
 * Created by Sven on 2018/1/27.
 */

public class OpenNewPageEvent {
    private String url;
    private int type=-1;

    public OpenNewPageEvent(String url) {
        this.url = url;
    }

    public OpenNewPageEvent(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
