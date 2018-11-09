package com.sven.huinews.international.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sfy. on 2018/9/18 0018.
 */

public class AliVideoResponse implements Parcelable {

    /**
     * code : 200
     * msg : success
     * data : {"id":"3364205eaffb46c09c2a91f8ea7ef183","title":"测试","aliyun_id":"3364205eaffb46c09c2a91f8ea7ef183","video_duration":6000,"video_cover":"http://videot.hesheng138.com/image/cover/27ECE589CFBE48EF902C44B769D92D00-6-2.png","video_height":1920,"video_width":1080,"like_count":0,"comment_count":0,"share_count":0,"play_count":0,"r_type":1,"user_avatar":"http://tg.199ho.com//static/img/default_head.png","user_nickname":"leish_19cute","du_type":1,"video_url":"http://videot.hesheng138.com/3364205eaffb46c09c2a91f8ea7ef183/79912e5e0c734e019c89f491bbc1528d-89bbbd2d8204c079e503e5f273d5f764-ld.mp4"}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public static class DataBean implements Parcelable {
        /**
         * id : 3364205eaffb46c09c2a91f8ea7ef183
         * title : 测试
         * aliyun_id : 3364205eaffb46c09c2a91f8ea7ef183
         * video_duration : 6000
         * video_cover : http://videot.hesheng138.com/image/cover/27ECE589CFBE48EF902C44B769D92D00-6-2.png
         * video_height : 1920
         * video_width : 1080
         * like_count : 0
         * comment_count : 0
         * share_count : 0
         * play_count : 0
         * r_type : 1
         * user_avatar : http://tg.199ho.com//static/img/default_head.png
         * user_nickname : leish_19cute
         * du_type : 1
         * video_url : http://videot.hesheng138.com/3364205eaffb46c09c2a91f8ea7ef183/79912e5e0c734e019c89f491bbc1528d-89bbbd2d8204c079e503e5f273d5f764-ld.mp4
         */

        private String id;
        private String title;
        private String aliyun_id;
        private String video_duration;
        private String video_cover;
        private String video_height;
        private String video_width;
        private String like_count;
        private String comment_count;
        private String share_count;
        private String play_count;
        private String r_type;
        private String user_avatar;
        private String user_nickname;
        private String du_type;
        private boolean is_liked;
        private String video_url;

        public boolean isIs_liked() {
            return is_liked;
        }

        public void setIs_liked(boolean is_liked) {
            this.is_liked = is_liked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAliyun_id() {
            return aliyun_id;
        }

        public void setAliyun_id(String aliyun_id) {
            this.aliyun_id = aliyun_id;
        }

        public String getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(String video_duration) {
            this.video_duration = video_duration;
        }

        public String getVideo_cover() {
            return video_cover;
        }

        public void setVideo_cover(String video_cover) {
            this.video_cover = video_cover;
        }

        public String getVideo_height() {
            return video_height;
        }

        public void setVideo_height(String video_height) {
            this.video_height = video_height;
        }

        public String getVideo_width() {
            return video_width;
        }

        public void setVideo_width(String video_width) {
            this.video_width = video_width;
        }

        public String getLike_count() {
            return like_count;
        }

        public void setLike_count(String like_count) {
            this.like_count = like_count;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public String getShare_count() {
            return share_count;
        }

        public void setShare_count(String share_count) {
            this.share_count = share_count;
        }

        public String getPlay_count() {
            return play_count;
        }

        public void setPlay_count(String play_count) {
            this.play_count = play_count;
        }

        public String getR_type() {
            return r_type;
        }

        public void setR_type(String r_type) {
            this.r_type = r_type;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public String getDu_type() {
            return du_type;
        }

        public void setDu_type(String du_type) {
            this.du_type = du_type;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeString(this.aliyun_id);
            dest.writeString(this.video_duration);
            dest.writeString(this.video_cover);
            dest.writeString(this.video_height);
            dest.writeString(this.video_width);
            dest.writeString(this.like_count);
            dest.writeString(this.comment_count);
            dest.writeString(this.share_count);
            dest.writeString(this.play_count);
            dest.writeString(this.r_type);
            dest.writeString(this.user_avatar);
            dest.writeString(this.user_nickname);
            dest.writeString(this.du_type);
            dest.writeString(this.video_url);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readString();
            this.title = in.readString();
            this.aliyun_id = in.readString();
            this.video_duration = in.readString();
            this.video_cover = in.readString();
            this.video_height = in.readString();
            this.video_width = in.readString();
            this.like_count = in.readString();
            this.comment_count = in.readString();
            this.share_count = in.readString();
            this.play_count = in.readString();
            this.r_type = in.readString();
            this.user_avatar = in.readString();
            this.user_nickname = in.readString();
            this.du_type = in.readString();
            this.video_url = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.msg);
        dest.writeParcelable(this.data, flags);
    }

    public AliVideoResponse() {
    }

    protected AliVideoResponse(Parcel in) {
        this.code = in.readString();
        this.msg = in.readString();
        this.data = in.readParcelable(AliVideoResponse.DataBean.class.getClassLoader());
    }

    public static final Creator<AliVideoResponse> CREATOR = new Creator<AliVideoResponse>() {
        @Override
        public AliVideoResponse createFromParcel(Parcel source) {
            return new AliVideoResponse(source);
        }

        @Override
        public AliVideoResponse[] newArray(int size) {
            return new AliVideoResponse[size];
        }
    };
}
