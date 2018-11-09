package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;


/**
 * Created by sfy. on 2018/9/7 0007.
 */

public class TempLoginRequest extends BaseRequest {
    @SerializedName("mobile_brand")
    private String mobileBrand;


    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    @Override
    public String toString() {
        return "TempLoginRequest{" +
                "mobileBrand='" + mobileBrand + '\'' +
                '}' + "BaseRequest{" +
                "time='" + getTime() + '\'' +
                ", nonce_str='" + getNonce_str() + '\'' +
                ", sign='" + getSign() + '\'' +
                '}'
                ;
    }
}
