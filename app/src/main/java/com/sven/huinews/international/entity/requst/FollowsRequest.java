package com.sven.huinews.international.entity.requst;

/**
 * Created by Burgess on 2018/9/20 0020.
 */
public class FollowsRequest extends BaseRequest {
    private int du_type;
    private int follower_id;
    private int video_id;

    public FollowsRequest() {

    }

    public FollowsRequest(int du_type, int follower_id, int video_id) {
        this.du_type = du_type;
        this.follower_id = follower_id;
        this.video_id = video_id;
    }

    public FollowsRequest(int du_type, int follower_id) {
        this.du_type = du_type;
        this.follower_id = follower_id;
    }

    public int getDu_type() {
        return du_type;
    }

    public void setDu_type(int du_type) {
        this.du_type = du_type;
    }

    public int getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(int follower_id) {
        this.follower_id = follower_id;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }
}
