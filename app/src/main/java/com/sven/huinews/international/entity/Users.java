package com.sven.huinews.international.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenshuaiwen on 18/1/25.
 */

public class Users {
    @SerializedName("c_user_id")
    private String id;
    @SerializedName("nickname")
    private String name;
    @SerializedName("sex")
    private int sex;
    @SerializedName("headimg")
    private String userIcon;
    @SerializedName("invitation_code")
    private String invitationCode;
    private String birthday;
    private double lat;
    private double lng;
    private float balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", userIcon='" + userIcon + '\'' +
                ", invitationCode='" + invitationCode + '\'' +
                ", birthday='" + birthday + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", balance=" + balance +
                '}';
    }
}
