package com.sven.huinews.international.entity.response;

import java.io.Serializable;

/**
 * Created by sfy. on 2018/4/27 0027.
 */

public class LinkedInResponse implements Serializable {
    private String firstName;
    private String id;
    private String lastName;
    private String pictureUrl;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
