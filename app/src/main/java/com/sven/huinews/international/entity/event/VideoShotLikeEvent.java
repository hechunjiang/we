package com.sven.huinews.international.entity.event;

import com.sven.huinews.international.entity.response.FollowVideoResponse;

/**
 * Created by sfy. on 2018/9/10 0010.
 */

public class VideoShotLikeEvent {
    private FollowVideoResponse.DataBean data;

    public VideoShotLikeEvent(FollowVideoResponse.DataBean data) {
        this.data = data;
    }

    public FollowVideoResponse.DataBean getData() {
        return data;
    }

    public void setData(FollowVideoResponse.DataBean data) {
        this.data = data;
    }
}
