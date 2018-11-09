package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

public class SharedRequest extends BaseRequest {

    @SerializedName("to_platfrom")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
