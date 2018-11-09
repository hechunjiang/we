package com.sven.huinews.international.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.duapps.ad.entity.strategy.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.view.View;

/**
 * Created by chenshuaiwen on 18/1/24.
 */

public class MyNews implements MultiItemEntity, Parcelable, Serializable {
    public static final int H_VIDEO = 0;// funny 0  stories 2  life 1
    public static final int V_VIDEO = 2;
    public static final int VIDEO_DOWN = 3;
    public static final int VIDEO_G = 1;
    public static final int AD_VIDEO = 4;
    public static final int V_PLAY_VIDEO = 5;

    private AdOtherMsg ad_otherMsg;
    private String title;
    @SerializedName("r_type")
    private int type;

    private String ad_type; // 广告类型

    private String id;
    //视频播放地址
    @SerializedName("video_url")
    private String videoUrl;
    //封面地址
    @SerializedName("video_cover")
    private String coverUrl;
    //用户名称
    @SerializedName("user_nickname")
    private String userName;

    @SerializedName("video_id")
    private String video_id;

    private String imagePath;
    private String videoPath;
    private String waterVideoPath;

    private NativeAd mNativeAd;

    public NativeAd getmNativeAd() {
        return mNativeAd;
    }

    public void setmNativeAd(NativeAd mNativeAd) {
        this.mNativeAd = mNativeAd;
    }


    private UnifiedNativeAd unifiedNativeAd;

    public UnifiedNativeAd getUnifiedNativeAd() {
        return unifiedNativeAd;
    }

    public void setUnifiedNativeAd(UnifiedNativeAd unifiedNativeAd) {
        this.unifiedNativeAd = unifiedNativeAd;
    }

    public String getWaterVideoPath() {
        return waterVideoPath;
    }

    public void setWaterVideoPath(String waterVideoPath) {
        this.waterVideoPath = waterVideoPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getDu_type() {
        return du_type;
    }

    public void setDu_type(String du_type) {
        this.du_type = du_type;
    }

    private String du_type;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    @SerializedName("play_count")
    private int playCount;

    @SerializedName("user_avatar")
    private String userIcon;

    @SerializedName("comment_count")
    private int commentCount;

    @SerializedName("like_count")
    private int likeCount;
    //    @SerializedName("is_up")
//    private boolean isLike;
    @SerializedName("is_collected")
    private boolean isSave;

    private boolean isSaveRedVideo;

    private boolean isStatistics;

    @SerializedName("is_up")
    private boolean isLike;

    @SerializedName("user_id")
    private String otherId;

    @SerializedName("du_id")
    private String du_id;

    public String getDu_id() {
        return du_id;
    }

    public void setDu_id(String du_id) {
        this.du_id = du_id;
    }

    @SerializedName("is_ad")
    private int isAd;

    public int getIsAd() {
        return isAd;
    }

    public void setIsAd(int isAd) {
        this.isAd = isAd;
    }

    public boolean isSave() {
        return isSave;
    }

    @SerializedName("share_count")
    private int shareCount;

    @SerializedName("collect_count")
    private int saveCount;
    private int goldCount;
    private String channel;
    private int videoType;
    private Boolean isGetUrl = true;//是否已经解析youtobe的链接
    private int adPosition = 0;
    @SerializedName("video_watch_second")
    private int addGoldTime;

    public int getAddGoldTime() {
        return addGoldTime;
    }

    public void setAddGoldTime(int addGoldTime) {
        this.addGoldTime = addGoldTime;
    }

    public int getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(int adPosition) {
        this.adPosition = adPosition;
    }

    public Boolean getGetUrl() {
        return isGetUrl;
    }

    public void setGetUrl(Boolean getUrl) {
        isGetUrl = getUrl;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    private boolean isShowGoldAnim; //是否显示金币动画

    @SerializedName("open_browser")
    private int openBrowser;

    public int getOpenBrowser() {
        return openBrowser;
    }

    public void setOpenBrowser(int openBrowser) {
        this.openBrowser = openBrowser;
    }

    public boolean isShowGoldAnim() {
        return isShowGoldAnim;
    }

    public void setShowGoldAnim(boolean showGoldAnim) {
        isShowGoldAnim = showGoldAnim;
    }

    @SerializedName("top_comments")
    private ArrayList<Comment> comments;

    public int getGoldCount() {
        return goldCount;
    }

    public void setGoldCount(int goldCount) {
        this.goldCount = goldCount;
    }

    private List<Recommend> recommend;

    public List<Recommend> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<Recommend> recommend) {
        this.recommend = recommend;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }


    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }


    public boolean isStatistics() {
        return isStatistics;
    }

    public void setStatistics(boolean statistics) {
        isStatistics = statistics;
    }

    public boolean isSaveRedVideo() {
        return isSaveRedVideo;
    }

    public void setSaveRedVideo(boolean saveRedVideo) {
        isSaveRedVideo = saveRedVideo;
    }

    public int getSaveCount() {
        return saveCount;
    }

    public void setSaveCount(int saveCount) {
        this.saveCount = saveCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    @SerializedName("video_height")
    private int videoHeight;
    @SerializedName("video_width")
    private int videoWidth;

    @SerializedName("video_duration")
    private int duration;

    @SerializedName("is_redpack")
    private int isRedpack;


    @SerializedName("is_gold")
    private int isGold;

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public int getIsGold() {
        return isGold;
    }

    public void setIsGold(int isGold) {
        this.isGold = isGold;
    }

    public int getIsRedpack() {
        return isRedpack;
    }

    public void setIsRedpack(int isRedpack) {
        this.isRedpack = isRedpack;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AdOtherMsg getAd_otherMsg() {
        return ad_otherMsg;
    }

    public void setAd_otherMsg(AdOtherMsg ad_otherMsg) {
        this.ad_otherMsg = ad_otherMsg;
    }

    @Override
    public int getItemType() {
        return type;
    }


    public static class AdOtherMsg implements Parcelable {
        private String[] imp;
        private String[] clk;

        public String[] getImp() {
            return imp;
        }

        public void setImp(String[] imp) {
            this.imp = imp;
        }

        public String[] getClk() {
            return clk;
        }

        public void setClk(String[] clk) {
            this.clk = clk;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(this.imp);
            dest.writeStringArray(this.clk);
        }

        public AdOtherMsg() {
        }

        protected AdOtherMsg(Parcel in) {
            this.imp = in.createStringArray();
            this.clk = in.createStringArray();
        }

        public static final Creator<AdOtherMsg> CREATOR = new Creator<AdOtherMsg>() {
            @Override
            public AdOtherMsg createFromParcel(Parcel source) {
                return new AdOtherMsg(source);
            }

            @Override
            public AdOtherMsg[] newArray(int size) {
                return new AdOtherMsg[size];
            }
        };
    }

    public MyNews() {
    }

    @Override
    public boolean equals(Object obj) {
        MyNews myNews = null;
        if (obj instanceof MyNews) {
            myNews = (MyNews) obj;
            return this.id.equals(myNews.getId());
        }
        return false;
    }


    @Override
    public String toString() {
        return "MyNews{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", id='" + id + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", playCount=" + playCount +
                ", userIcon='" + userIcon + '\'' +
                ", commentCount=" + commentCount +
                ", likeCount=" + likeCount +
                ", isLike=" + isLike +
                ", isSave=" + isSave +
                ", isSaveRedVideo=" + isSaveRedVideo +
                ", isStatistics=" + isStatistics +
                ", isAd=" + isAd +
                ", shareCount=" + shareCount +
                ", saveCount=" + saveCount +
                ", comments=" + comments +
                ", recommend=" + recommend +
                ", videoHeight=" + videoHeight +
                ", videoWidth=" + videoWidth +
                ", duration=" + duration +
                ", isRedpack=" + isRedpack +
                ", isGold=" + isGold +
                ", otherId=" + otherId +
                ", du_id=" + du_id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ad_otherMsg, flags);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeString(this.ad_type);
        dest.writeString(this.id);
        dest.writeString(this.videoUrl);
        dest.writeString(this.coverUrl);
        dest.writeString(this.userName);
        dest.writeString(this.video_id);
        dest.writeString(this.du_type);
        dest.writeInt(this.playCount);
        dest.writeString(this.userIcon);
        dest.writeInt(this.commentCount);
        dest.writeInt(this.likeCount);
        dest.writeByte(this.isSave ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSaveRedVideo ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isStatistics ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLike ? (byte) 1 : (byte) 0);
        dest.writeString(this.otherId);
        dest.writeString(this.du_id);
        dest.writeInt(this.isAd);
        dest.writeInt(this.shareCount);
        dest.writeInt(this.saveCount);
        dest.writeInt(this.goldCount);
        dest.writeString(this.channel);
        dest.writeInt(this.videoType);
        dest.writeValue(this.isGetUrl);
        dest.writeInt(this.adPosition);
        dest.writeInt(this.addGoldTime);
        dest.writeByte(this.isShowGoldAnim ? (byte) 1 : (byte) 0);
        dest.writeInt(this.openBrowser);
        dest.writeTypedList(this.comments);
        dest.writeTypedList(this.recommend);
        dest.writeInt(this.videoHeight);
        dest.writeInt(this.videoWidth);
        dest.writeInt(this.duration);
        dest.writeInt(this.isRedpack);
        dest.writeInt(this.isGold);
    }

    protected MyNews(Parcel in) {
        this.ad_otherMsg = in.readParcelable(AdOtherMsg.class.getClassLoader());
        this.title = in.readString();
        this.type = in.readInt();
        this.ad_type = in.readString();
        this.id = in.readString();
        this.videoUrl = in.readString();
        this.coverUrl = in.readString();
        this.userName = in.readString();
        this.video_id = in.readString();
        this.du_type = in.readString();
        this.playCount = in.readInt();
        this.userIcon = in.readString();
        this.commentCount = in.readInt();
        this.likeCount = in.readInt();
        this.isSave = in.readByte() != 0;
        this.isSaveRedVideo = in.readByte() != 0;
        this.isStatistics = in.readByte() != 0;
        this.isLike = in.readByte() != 0;
        this.otherId = in.readString();
        this.du_id = in.readString();
        this.isAd = in.readInt();
        this.shareCount = in.readInt();
        this.saveCount = in.readInt();
        this.goldCount = in.readInt();
        this.channel = in.readString();
        this.videoType = in.readInt();
        this.isGetUrl = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.adPosition = in.readInt();
        this.addGoldTime = in.readInt();
        this.isShowGoldAnim = in.readByte() != 0;
        this.openBrowser = in.readInt();
        this.comments = in.createTypedArrayList(Comment.CREATOR);
        this.recommend = in.createTypedArrayList(Recommend.CREATOR);
        this.videoHeight = in.readInt();
        this.videoWidth = in.readInt();
        this.duration = in.readInt();
        this.isRedpack = in.readInt();
        this.isGold = in.readInt();
    }

    public static final Parcelable.Creator<MyNews> CREATOR = new Parcelable.Creator<MyNews>() {
        @Override
        public MyNews createFromParcel(Parcel source) {
            return new MyNews(source);
        }

        @Override
        public MyNews[] newArray(int size) {
            return new MyNews[size];
        }
    };
}
