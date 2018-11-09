package com.sven.huinews.international.entity.jspush;

import com.google.gson.annotations.SerializedName;

public class VideosTimeLast {
    @SerializedName("lastTime")
    private Long lastTime;

    @SerializedName("lookTime")
    private Long lookTime;

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public Long getLookTime() {
        return lookTime;
    }

    public void setLookTime(Long lookTime) {
        this.lookTime = lookTime;
    }
}
