package com.sven.huinews.international.entity.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class PerSonWorkResponse implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : [{"id":"65eeabbf7ce848a698a8e23251792537","title":"测试节省时间点解点解","aliyun_id":"65eeabbf7ce848a698a8e23251792537","video_duration":8303,"video_cover":"http://video.hesheng138.com/image/cover/08B53F93B8754EEC87826854FF91556C-6-2.png","video_height":1920,"video_width":1080,"like_count":0,"comment_count":0,"share_count":0,"play_count":0,"r_type":1,"user_id":20306,"user_nickname":"burgess","user_avatar":"http://tg.199ho.com//static/img/default_head.png","du_type":1}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 65eeabbf7ce848a698a8e23251792537
         * title : 测试节省时间点解点解
         * aliyun_id : 65eeabbf7ce848a698a8e23251792537
         * video_duration : 8303
         * video_cover : http://video.hesheng138.com/image/cover/08B53F93B8754EEC87826854FF91556C-6-2.png
         * video_height : 1920
         * video_width : 1080
         * like_count : 0
         * comment_count : 0
         * share_count : 0
         * play_count : 0
         * r_type : 1
         * user_id : 20306
         * user_nickname : burgess
         * user_avatar : http://tg.199ho.com//static/img/default_head.png
         * du_type : 1
         */

        private String id;
        private String title;
        private String aliyun_id;
        private int video_duration;
        private String video_cover;
        private int video_height;
        private int video_width;
        private int like_count;
        private int comment_count;
        private int share_count;
        private int play_count;
        private int r_type;
        private int user_id;
        private String user_nickname;
        private String user_avatar;
        private int du_type;
        private String video_url;
        private boolean is_up;

        public boolean isIs_up() {
            return is_up;
        }

        public void setIs_up(boolean is_up) {
            this.is_up = is_up;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
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

        public int getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(int video_duration) {
            this.video_duration = video_duration;
        }

        public String getVideo_cover() {
            return video_cover;
        }

        public void setVideo_cover(String video_cover) {
            this.video_cover = video_cover;
        }

        public int getVideo_height() {
            return video_height;
        }

        public void setVideo_height(int video_height) {
            this.video_height = video_height;
        }

        public int getVideo_width() {
            return video_width;
        }

        public void setVideo_width(int video_width) {
            this.video_width = video_width;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getShare_count() {
            return share_count;
        }

        public void setShare_count(int share_count) {
            this.share_count = share_count;
        }

        public int getPlay_count() {
            return play_count;
        }

        public void setPlay_count(int play_count) {
            this.play_count = play_count;
        }

        public int getR_type() {
            return r_type;
        }

        public void setR_type(int r_type) {
            this.r_type = r_type;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public int getDu_type() {
            return du_type;
        }

        public void setDu_type(int du_type) {
            this.du_type = du_type;
        }
    }
}
