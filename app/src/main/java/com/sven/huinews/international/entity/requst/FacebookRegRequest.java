package com.sven.huinews.international.entity.requst;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sfy. on 2018/9/27 0027.
 */

public class FacebookRegRequest extends BaseRequest implements Serializable {
    private String mail;
    @SerializedName("verify")
    private String registerCode;
    private String pass;
    @SerializedName("mobile_brand")
    private String mobileBrand;
    @SerializedName("nickname")
    private String nickName;
    @SerializedName("headimg")
    private String headIcon;
    @SerializedName("fb_id")
    private String fbId; //facebook_id
    private String fb_access_token;
    private String sex;
    @SerializedName("f_invit_code")
    private String invitCode;


    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFb_access_token() {
        return fb_access_token;
    }

    public void setFb_access_token(String fb_access_token) {
        this.fb_access_token = fb_access_token;
    }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInvitCode() {
        return invitCode;
    }

    public void setInvitCode(String invitCode) {
        this.invitCode = invitCode;
    }
}
