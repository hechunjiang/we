package com.sven.huinews.international.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sven on 2018/2/28.
 */

public class PushTask {
    private String id;
    private String title;
    @SerializedName("body")
    private String content;
    @SerializedName("redirect")
    private String url;
    @SerializedName("picUrl")
    private String imgUrl;
    private int type;//1新手红包

    public int getType() {
        return type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
