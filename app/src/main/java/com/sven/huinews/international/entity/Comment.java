package com.sven.huinews.international.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class Comment implements Parcelable {


    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", video_id='" + video_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                ", like_count='" + like_count + '\'' +
                ", create_time='" + create_time + '\'' +
                ", is_up=" + is_up +
                ", is_sure=" + is_sure +
                ", pub_datetime='" + pub_datetime + '\'' +
                ", is_add_gold='" + is_add_gold + '\'' +
                '}';
    }

    /**
     * id : 4986
     * video_id : 616f368e252c4efa87ae4c4382ce0929
     * user_id : 28792
     * avatar : http://www.hesheng138.com/uploads/headImage/281c81f4a190c96698683f0dacb48bb0.png
     * nickname : hc葫芦
     * content : 龙
     * like_count : null
     * create_time : 1537340537
     * is_up : false
     * is_sure : true
     * pub_datetime : 1537340537
     * is_add_gold : 1
     */

    private String id;
    private String video_id;
    private String user_id;
    private String avatar;
    private String nickname;
    private String content;
    private int like_count;
    private String create_time;
    private boolean is_up;
    private boolean is_sure;
    private String pub_datetime;
    private String is_add_gold;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public boolean isIs_up() {
        return is_up;
    }

    public void setIs_up(boolean is_up) {
        this.is_up = is_up;
    }

    public boolean isIs_sure() {
        return is_sure;
    }

    public void setIs_sure(boolean is_sure) {
        this.is_sure = is_sure;
    }

    public String getPub_datetime() {
        return pub_datetime;
    }

    public void setPub_datetime(String pub_datetime) {
        this.pub_datetime = pub_datetime;
    }

    public String getIs_add_gold() {
        return is_add_gold;
    }

    public void setIs_add_gold(String is_add_gold) {
        this.is_add_gold = is_add_gold;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.video_id);
        dest.writeString(this.user_id);
        dest.writeString(this.avatar);
        dest.writeString(this.nickname);
        dest.writeString(this.content);
        dest.writeInt(this.like_count);
        dest.writeString(this.create_time);
        dest.writeByte(this.is_up ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_sure ? (byte) 1 : (byte) 0);
        dest.writeString(this.pub_datetime);
        dest.writeString(this.is_add_gold);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.id = in.readString();
        this.video_id = in.readString();
        this.user_id = in.readString();
        this.avatar = in.readString();
        this.nickname = in.readString();
        this.content = in.readString();
        this.like_count = in.readInt();
        this.create_time = in.readString();
        this.is_up = in.readByte() != 0;
        this.is_sure = in.readByte() != 0;
        this.pub_datetime = in.readString();
        this.is_add_gold = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
