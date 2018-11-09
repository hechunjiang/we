package com.sven.huinews.international.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class PersonLikeResponse implements Parcelable {


    /**
     * code : 200
     * msg : Successful operation
     * data : [{"id":"3364205eaffb46c09c2a91f8ea7ef183","title":"测试","aliyun_id":"3364205eaffb46c09c2a91f8ea7ef183","video_duration":6000,"video_cover":"http://videot.hesh0-6-2.png","video_height":1920,"video_width":1080,"like_count":4,"comment_count":0,"share_count":0,"play_count":0,"r_type":1,"user_id":70785,"user_nickname":"leish_19cute","user_avatar":"http://tg.199ho.com//static/img/default_head.png","du_type":1},{"id":559040,"title":"??? #twerk #танцы #танец #лучшее #dance #dancer","category":"musical.ly","video_url":"http://mpak-odec1.aka91310848_uqSFczcEzl_o.mp4","video_uni":"b1090a17ccc3ef2949a318f71d748f3957fed8c7","video_duration":14266,"video_cover":"http://mpak-odec1.464d-a01d-d9db72a902d7_IzcsKhEwOn.jpg","video_height":960,"video_width":540,"like_count":3980,"dislike_count":0,"comment_count":191,"share_count":36,"play_count":5,"group_id":0,"user_id":11,"user_nickname":"Polina  Dubkova | DANCER","user_avatar":"http://mpaw-sun-252210588595163137-225.jpg","uri":"280889082191310848","is_handler_comment":0,"create_time":1528366157,"collect_count":0,"channel":"musical.ly","visit_count":5,"status":1,"dis_time":0,"order_time":1528366157,"r_type":1,"insert_key":"1","du_id":11,"du_type":2},{"id":560312,"title":"CAN WE BE FRIENDe to get back on it!! ","category":"musical.ly","video_url":"http://mpak-suse1265557219307520_BPMsQLUUDV_o.mp4","video_uni":"00e2d5be5a62df9b8aded7779f7d24b49888f418","video_duration":15015,"video_cover":"http://mpak-su-006ac4fa6322_JILqBjHMzA.jpg","video_height":960,"video_width":540,"like_count":69175,"dislike_count":0,"comment_count":313,"share_count":22,"play_count":7,"group_id":0,"user_id":12,"user_nickname":"BluPrString","user_avatar":"http://mpak-suse1.ak707-PlekXls1cL.jpg","uri":"269265557219307520","is_handler_comment":0,"create_time":1528207529,"collect_count":0,"channel":"musical.ly","visit_count":7,"status":1,"dis_time":0,"order_time":1528207529,"r_type":1,"insert_key":"1","du_id":12,"du_type":2}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "PersonLikeResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable{
        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", aliyun_id='" + aliyun_id + '\'' +
                    ", video_duration='" + video_duration + '\'' +
                    ", video_cover='" + video_cover + '\'' +
                    ", video_height='" + video_height + '\'' +
                    ", video_width='" + video_width + '\'' +
                    ", like_count='" + like_count + '\'' +
                    ", comment_count='" + comment_count + '\'' +
                    ", share_count='" + share_count + '\'' +
                    ", play_count='" + play_count + '\'' +
                    ", r_type='" + r_type + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", user_nickname='" + user_nickname + '\'' +
                    ", user_avatar='" + user_avatar + '\'' +
                    ", du_type='" + du_type + '\'' +
                    ", category='" + category + '\'' +
                    ", video_url='" + video_url + '\'' +
                    ", video_uni='" + video_uni + '\'' +
                    ", dislike_count='" + dislike_count + '\'' +
                    ", group_id='" + group_id + '\'' +
                    ", uri='" + uri + '\'' +
                    ", is_handler_comment='" + is_handler_comment + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", collect_count='" + collect_count + '\'' +
                    ", channel='" + channel + '\'' +
                    ", visit_count='" + visit_count + '\'' +
                    ", status='" + status + '\'' +
                    ", dis_time='" + dis_time + '\'' +
                    ", order_time='" + order_time + '\'' +
                    ", insert_key='" + insert_key + '\'' +
                    ", du_id='" + du_id + '\'' +
                    '}';
        }

        /**
         * id : 3364205eaffb46c09c2a91f8ea7ef183
         * title : 测试
         * aliyun_id : 3364205eaffb46c09c2a91f8ea7ef183
         * video_duration : 6000
         * video_cover : http://videot.hesh0-6-2.png
         * video_height : 1920
         * video_width : 1080
         * like_count : 4
         * comment_count : 0
         * share_count : 0
         * play_count : 0
         * r_type : 1
         * user_id : 70785
         * user_nickname : leish_19cute
         * user_avatar : http://tg.199ho.com//static/img/default_head.png
         * du_type : 1
         * category : musical.ly
         * video_url : http://mpak-odec1.aka91310848_uqSFczcEzl_o.mp4
         * video_uni : b1090a17ccc3ef2949a318f71d748f3957fed8c7
         * dislike_count : 0
         * group_id : 0
         * uri : 280889082191310848
         * is_handler_comment : 0
         * create_time : 1528366157
         * collect_count : 0
         * channel : musical.ly
         * visit_count : 5
         * status : 1
         * dis_time : 0
         * order_time : 1528366157
         * insert_key : 1
         * du_id : 11
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
        private String user_id;
        private String user_nickname;
        private String user_avatar;
        private String du_type;
        private String category;
        private String video_url;
        private String video_uni;
        private String dislike_count;
        private String group_id;
        private String uri;
        private String is_handler_comment;
        private String create_time;
        private String collect_count;
        private String channel;
        private String visit_count;
        private String status;
        private String dis_time;
        private String order_time;
        private String insert_key;
        private String du_id;
        private boolean is_up;

        public boolean isIs_up() {
            return is_up;
        }

        public void setIs_up(boolean is_up) {
            this.is_up = is_up;
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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
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

        public String getDu_type() {
            return du_type;
        }

        public void setDu_type(String du_type) {
            this.du_type = du_type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getVideo_uni() {
            return video_uni;
        }

        public void setVideo_uni(String video_uni) {
            this.video_uni = video_uni;
        }

        public String getDislike_count() {
            return dislike_count;
        }

        public void setDislike_count(String dislike_count) {
            this.dislike_count = dislike_count;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getIs_handler_comment() {
            return is_handler_comment;
        }

        public void setIs_handler_comment(String is_handler_comment) {
            this.is_handler_comment = is_handler_comment;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCollect_count() {
            return collect_count;
        }

        public void setCollect_count(String collect_count) {
            this.collect_count = collect_count;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getVisit_count() {
            return visit_count;
        }

        public void setVisit_count(String visit_count) {
            this.visit_count = visit_count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDis_time() {
            return dis_time;
        }

        public void setDis_time(String dis_time) {
            this.dis_time = dis_time;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getInsert_key() {
            return insert_key;
        }

        public void setInsert_key(String insert_key) {
            this.insert_key = insert_key;
        }

        public String getDu_id() {
            return du_id;
        }

        public void setDu_id(String du_id) {
            this.du_id = du_id;
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
            dest.writeString(this.user_id);
            dest.writeString(this.user_nickname);
            dest.writeString(this.user_avatar);
            dest.writeString(this.du_type);
            dest.writeString(this.category);
            dest.writeString(this.video_url);
            dest.writeString(this.video_uni);
            dest.writeString(this.dislike_count);
            dest.writeString(this.group_id);
            dest.writeString(this.uri);
            dest.writeString(this.is_handler_comment);
            dest.writeString(this.create_time);
            dest.writeString(this.collect_count);
            dest.writeString(this.channel);
            dest.writeString(this.visit_count);
            dest.writeString(this.status);
            dest.writeString(this.dis_time);
            dest.writeString(this.order_time);
            dest.writeString(this.insert_key);
            dest.writeString(this.du_id);
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
            this.user_id = in.readString();
            this.user_nickname = in.readString();
            this.user_avatar = in.readString();
            this.du_type = in.readString();
            this.category = in.readString();
            this.video_url = in.readString();
            this.video_uni = in.readString();
            this.dislike_count = in.readString();
            this.group_id = in.readString();
            this.uri = in.readString();
            this.is_handler_comment = in.readString();
            this.create_time = in.readString();
            this.collect_count = in.readString();
            this.channel = in.readString();
            this.visit_count = in.readString();
            this.status = in.readString();
            this.dis_time = in.readString();
            this.order_time = in.readString();
            this.insert_key = in.readString();
            this.du_id = in.readString();
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
        dest.writeTypedList(this.data);
    }

    public PersonLikeResponse() {
    }

    protected PersonLikeResponse(Parcel in) {
        this.code = in.readString();
        this.msg = in.readString();
        this.data = in.createTypedArrayList(PersonLikeResponse.DataBean.CREATOR);
    }

    public static final Creator<PersonLikeResponse> CREATOR = new Creator<PersonLikeResponse>() {
        @Override
        public PersonLikeResponse createFromParcel(Parcel source) {
            return new PersonLikeResponse(source);
        }

        @Override
        public PersonLikeResponse[] newArray(int size) {
            return new PersonLikeResponse[size];
        }
    };
}
