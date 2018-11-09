package com.sven.huinews.international.entity.response;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class UserDatasResponse implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"nickname":"Facts Verse","userAvatar":"http://p0.ipstatp.com/thumb/005ab47c8bb842a4e7e8","birthday":"","sex":"http://p0.ipstatp.com/thumb/005ab47c8bb842a4e7e8","followNum":12,"fansNum":0,"likeCount":12,"getLike":55921,"signature":"","isFollow":false,"du_type":2,"user_id":"17"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * nickname : Facts Verse
         * userAvatar : http://p0.ipstatp.com/thumb/005ab47c8bb842a4e7e8
         * birthday :
         * sex : http://p0.ipstatp.com/thumb/005ab47c8bb842a4e7e8
         * followNum : 12
         * fansNum : 0
         * likeCount : 12
         * getLike : 55921
         * signature :
         * isFollow : false
         * du_type : 2
         * user_id : 17
         */

        private String nickname;
        private String userAvatar;
        private String birthday;
        private int sex;
        private int followNum;
        private int fansNum;
        private int likeCount;
        private int getLike;
        private String signature;
        private boolean isFollow;
        private int du_type;
        private String user_id;
        private int loadType;

        public int getLoadType() {
            return loadType;
        }
        public void setLoadType(int loadType) {
            this.loadType = loadType;
        }
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getFollowNum() {
            return followNum;
        }

        public void setFollowNum(int followNum) {
            this.followNum = followNum;
        }

        public int getFansNum() {
            return fansNum;
        }

        public void setFansNum(int fansNum) {
            this.fansNum = fansNum;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getGetLike() {
            return getLike;
        }

        public void setGetLike(int getLike) {
            this.getLike = getLike;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public boolean isIsFollow() {
            return isFollow;
        }

        public void setIsFollow(boolean isFollow) {
            this.isFollow = isFollow;
        }

        public int getDu_type() {
            return du_type;
        }

        public void setDu_type(int du_type) {
            this.du_type = du_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
