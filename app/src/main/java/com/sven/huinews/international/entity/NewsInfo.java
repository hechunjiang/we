package com.sven.huinews.international.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

public class NewsInfo implements Serializable, MultiItemEntity {

    /**
     * id : 9717713
     * title : This Test Will Prove If You're An Alien, An Android, Or Actually Human
     * href : /perpetua/alien-android-or-human
     * source : buzzfeed
     * desc : This is scientific.
     * cover_img : https://img.buzzfeed.com/buzzfeed-static/static/2018-08/16/14/campaign_images/buzzfeed-prod-web-01/this-test-will-prove-if-youre-an-alien-an-android-2-6022-1534443674-0_dblbig.jpg?output-format=auto&output-quality=auto&resize=300:*;
     * author_avatar : https://img.buzzfeed.com/buzzfeed-static/static/2015-02/12/21/user_images/webdr10/perpetua-19230-1423792827-0_large.jpg?output-format=jpeg&output-quality=85&downsize=30:*
     * author_name : Matthew Perpetua
     * uni : bf8903cc48db00674205e471e163230c364e8fa5
     * create_time : 2018-08-19 17:55:37
     * channel : buzzfeed_quizzes
     * group_id : 3
     * visit_count : 33
     * is_gold : 0
     * is_redpack : 0
     * is_ad : 0
     * open_browser : 0
     * ad_otherMsg : {"imp":[],"clk":[]}
     * ad_type :
     * display_type : 1
     * news_stop_second : 15000
     */
    public static final int NEWS_BIG = 1;
    public static final int NEWS_SMELL = 0;
    public static final int AD_ONE = 2;
    private int type;
    private int id;
    private String title;
    private String href;
    private String source;
    private String desc;
    private String cover_img;
    private String author_avatar;
    private String author_name;
    private String uni;
    private String create_time;
    private String channel;
    private int group_id;
    private int visit_count;
    private int is_gold;
    private int is_redpack;
    private int is_ad;
    private int open_browser;
    private AdOtherMsgBean ad_otherMsg;
    private String ad_type;
    private int display_type;
    private int news_stop_second;
    private int adPosition = 0;

    public int getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(int adPosition) {
        this.adPosition = adPosition;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getAuthor_avatar() {
        return author_avatar;
    }

    public void setAuthor_avatar(String author_avatar) {
        this.author_avatar = author_avatar;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getUni() {
        return uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getVisit_count() {
        return visit_count;
    }

    public void setVisit_count(int visit_count) {
        this.visit_count = visit_count;
    }

    public int getIs_gold() {
        return is_gold;
    }

    public void setIs_gold(int is_gold) {
        this.is_gold = is_gold;
    }

    public int getIs_redpack() {
        return is_redpack;
    }

    public void setIs_redpack(int is_redpack) {
        this.is_redpack = is_redpack;
    }

    public int getIs_ad() {
        return is_ad;
    }

    public void setIs_ad(int is_ad) {
        this.is_ad = is_ad;
    }

    public int getOpen_browser() {
        return open_browser;
    }

    public void setOpen_browser(int open_browser) {
        this.open_browser = open_browser;
    }

    public AdOtherMsgBean getAd_otherMsg() {
        return ad_otherMsg;
    }

    public void setAd_otherMsg(AdOtherMsgBean ad_otherMsg) {
        this.ad_otherMsg = ad_otherMsg;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public int getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(int display_type) {
        this.display_type = display_type;
    }

    public int getNews_stop_second() {
        return news_stop_second;
    }

    public void setNews_stop_second(int news_stop_second) {
        this.news_stop_second = news_stop_second;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public static class AdOtherMsgBean implements Serializable {
        private List<?> imp;
        private List<?> clk;

        public List<?> getImp() {
            return imp;
        }

        public void setImp(List<?> imp) {
            this.imp = imp;
        }

        public List<?> getClk() {
            return clk;
        }

        public void setClk(List<?> clk) {
            this.clk = clk;
        }
    }
}
