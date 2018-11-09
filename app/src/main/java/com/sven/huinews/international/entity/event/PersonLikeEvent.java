package com.sven.huinews.international.entity.event;

import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;

/**
 * Created by sfy. on 2018/9/25 0025.
 */

public class PersonLikeEvent {
    private PersonLikeResponse mData;

    public PersonLikeResponse getmData() {
        return mData;
    }

    public void setmData(PersonLikeResponse mData) {
        this.mData = mData;
    }
}
