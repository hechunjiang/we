package com.sven.huinews.international.entity.event;

import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.response.FollowVideoResponse;

/**
 * Created by sfy. on 2018/9/10 0010.
 */

public class VideoLikeEvent {
    private MyNews mData;

    private boolean isAliYun;

    public boolean isAliYun() {
        return isAliYun;
    }

    public void setAliYun(boolean aliYun) {
        isAliYun = aliYun;
    }

    public VideoLikeEvent(MyNews mData) {
        this.mData = mData;
    }

    public VideoLikeEvent() {
    }

    public MyNews getmData() {
        return mData;
    }

    public void setmData(MyNews mData) {
        this.mData = mData;
    }
}
