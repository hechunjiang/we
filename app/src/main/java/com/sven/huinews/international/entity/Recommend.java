package com.sven.huinews.international.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sfy. on 2018/3/13 0013.
 */

public class Recommend implements Parcelable{

    /**
     * id : 91126606471
     * title : 刚入学的学妹太疯狂了，学校外的小摊老板见了就想逃，太逗了
     * category : 搞笑视频
     * video_url : http://i.snssdk.com/neihan/video/playback/?video_id=551f8ecaa49e48a096c9ee864c406c99&quality=origin&line=0&is_gif=0
     * video_duration : 16099
     * video_cover : http://p3.pstatp.com/large/64a500145db2c617fb32
     * video_height : 854
     * video_width : 480
     * like_count : 111974
     * dislike_count : 1006
     * comment_count : 908
     * share_count : 2
     * play_count : 1090368
     * group_id : 91126606471
     * user_id : 5427476443
     * user_nickname : 留赞可否
     * user_avatar : http://p3.pstatp.com/thumb/658a0023af598107d2d2
     * uri : 551f8ecaa49e48a096c9ee864c406c99
     * is_handler_comment : 1
     * create_time : 1518876674
     * collect_count : 0
     * channel : neihan
     * visit_count : 0
     * status : 1
     * dis_time : 0
     * order_time : 1518876674
     */

    private String id;
    private String title;
    private String category;
    private String video_url;
    private int video_duration;
    private String video_cover;
    private int video_height;
    private int video_width;
    private int like_count;
    private int dislike_count;
    private int comment_count;
    private int share_count;
    private int play_count;
    private long group_id;
    private long user_id;
    private String user_nickname;
    private String user_avatar;
    private String uri;
    private int is_handler_comment;
    private int create_time;
    private int collect_count;
    private String channel;
    private int visit_count;
    private int status;
    private int dis_time;
    private int order_time;
    private int is_ad;
    private int is_gold;

    protected Recommend(Parcel in) {
        id = in.readString();
        title = in.readString();
        category = in.readString();
        video_url = in.readString();
        video_duration = in.readInt();
        video_cover = in.readString();
        video_height = in.readInt();
        video_width = in.readInt();
        like_count = in.readInt();
        dislike_count = in.readInt();
        comment_count = in.readInt();
        share_count = in.readInt();
        play_count = in.readInt();
        group_id = in.readLong();
        user_id = in.readLong();
        user_nickname = in.readString();
        user_avatar = in.readString();
        uri = in.readString();
        is_handler_comment = in.readInt();
        create_time = in.readInt();
        collect_count = in.readInt();
        channel = in.readString();
        visit_count = in.readInt();
        status = in.readInt();
        dis_time = in.readInt();
        order_time = in.readInt();
        is_ad = in.readInt();
        is_gold = in.readInt();
    }

    public static final Creator<Recommend> CREATOR = new Creator<Recommend>() {
        @Override
        public Recommend createFromParcel(Parcel in) {
            return new Recommend(in);
        }

        @Override
        public Recommend[] newArray(int size) {
            return new Recommend[size];
        }
    };

    public int getIs_gold() {
        return is_gold;
    }

    public void setIs_gold(int is_gold) {
        this.is_gold = is_gold;
    }

    public int getIs_ad() {
        return is_ad;
    }

    public void setIs_ad(int is_ad) {
        this.is_ad = is_ad;
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

    public int getDislike_count() {
        return dislike_count;
    }

    public void setDislike_count(int dislike_count) {
        this.dislike_count = dislike_count;
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

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getIs_handler_comment() {
        return is_handler_comment;
    }

    public void setIs_handler_comment(int is_handler_comment) {
        this.is_handler_comment = is_handler_comment;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getVisit_count() {
        return visit_count;
    }

    public void setVisit_count(int visit_count) {
        this.visit_count = visit_count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDis_time() {
        return dis_time;
    }

    public void setDis_time(int dis_time) {
        this.dis_time = dis_time;
    }

    public int getOrder_time() {
        return order_time;
    }

    public void setOrder_time(int order_time) {
        this.order_time = order_time;
    }

    @Override
    public String toString() {
        return "Recommend{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", video_url='" + video_url + '\'' +
                ", video_duration=" + video_duration +
                ", video_cover='" + video_cover + '\'' +
                ", video_height=" + video_height +
                ", video_width=" + video_width +
                ", like_count=" + like_count +
                ", dislike_count=" + dislike_count +
                ", comment_count=" + comment_count +
                ", share_count=" + share_count +
                ", play_count=" + play_count +
                ", group_id=" + group_id +
                ", user_id=" + user_id +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_avatar='" + user_avatar + '\'' +
                ", uri='" + uri + '\'' +
                ", is_handler_comment=" + is_handler_comment +
                ", create_time=" + create_time +
                ", collect_count=" + collect_count +
                ", channel='" + channel + '\'' +
                ", visit_count=" + visit_count +
                ", status=" + status +
                ", dis_time=" + dis_time +
                ", order_time=" + order_time +
                ", is_ad=" + is_ad +
                ", is_gold=" + is_gold +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(category);
        parcel.writeString(video_url);
        parcel.writeInt(video_duration);
        parcel.writeString(video_cover);
        parcel.writeInt(video_height);
        parcel.writeInt(video_width);
        parcel.writeInt(like_count);
        parcel.writeInt(dislike_count);
        parcel.writeInt(comment_count);
        parcel.writeInt(share_count);
        parcel.writeInt(play_count);
        parcel.writeLong(group_id);
        parcel.writeLong(user_id);
        parcel.writeString(user_nickname);
        parcel.writeString(user_avatar);
        parcel.writeString(uri);
        parcel.writeInt(is_handler_comment);
        parcel.writeInt(create_time);
        parcel.writeInt(collect_count);
        parcel.writeString(channel);
        parcel.writeInt(visit_count);
        parcel.writeInt(status);
        parcel.writeInt(dis_time);
        parcel.writeInt(order_time);
        parcel.writeInt(is_gold);
    }
}
