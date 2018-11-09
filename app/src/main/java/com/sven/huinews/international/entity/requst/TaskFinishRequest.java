package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sven on 2018/2/5.
 */

public class TaskFinishRequest extends BaseRequest {
    private String id;
    private String debug;

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}