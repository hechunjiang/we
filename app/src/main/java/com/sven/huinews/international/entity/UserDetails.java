package com.sven.huinews.international.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;

import java.io.Serializable;

/**
 * Created by Burgess on 2018/9/28 0028.
 */
public class UserDetails implements Serializable, MultiItemEntity {

    private UserDatasResponse userDatasResponse;

    private PerSonWorkResponse perSonWorkResponse;

    private PersonLikeResponse personLikeResponse;

    private int type;

    public static final int USER_DATA = 1;
    public static final int USER_VIDEO = 0;
    public static final int USER_LIKE = 2;

    public UserDatasResponse getUserDatasResponse() {
        return userDatasResponse;
    }

    public void setUserDatasResponse(UserDatasResponse userDatasResponse) {
        this.userDatasResponse = userDatasResponse;
    }

    public PerSonWorkResponse getPerSonWorkResponse() {
        return perSonWorkResponse;
    }

    public void setPerSonWorkResponse(PerSonWorkResponse perSonWorkResponse) {
        this.perSonWorkResponse = perSonWorkResponse;
    }

    public PersonLikeResponse getPersonLikeResponse() {
        return personLikeResponse;
    }

    public void setPersonLikeResponse(PersonLikeResponse personLikeResponse) {
        this.personLikeResponse = personLikeResponse;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
