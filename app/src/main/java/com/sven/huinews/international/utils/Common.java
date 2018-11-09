package com.sven.huinews.international.utils;

import android.os.Environment;

import com.sven.huinews.international.BuildConfig;

import java.io.File;

/**
 * Created by sfy. on 2018/9/6 0006.
 */

public class Common {
    public static final int V_VIDEO = 1;
    public static final int H_VIDEO = 2;
    public static final int H_NEWS = 3;
    public static final int LIKE = 1;
    public static final int VIDEO = 2;
    public static final String KEY_WORDS = "keywords";
    public static final String LIMIT = "limit";

    public static final String SELECT_TASK = "select_task";
    public static final String SELECT_FRAGMENT = "select_fragment";
    public static final String SELECT_SHOT = "select_shot";
    public static final String FRAGMENT_CHANGE = "fragment_change";

    public static final String HOME_LAYOUT_TYPE = "homeLayoutType";
    public static final String HOME_UD_TYPE = "du_type";
    public static final String HOME_OTHER_ID = "other";
    public static final String R_TYPE = "r_type";
    public static final String MY_ADVERTISEMENT = "my_advertisement"; //平台广告

    public static final String REFRESH_USERINFO = "refresh_userinfo";

    /**
     * burgess
     */
    //定义一些bundle 传输名称
    public static final String BUNDLE_TO_WEB_URL = "bundle_to_web_url";
    public static final String BUNDLE_TO_WEB_TYPE = "bundle_to_web_type";
    public static final String BUNDLE_TO_ADVERTISEMENT = "bundle_to_advertisement";
    public static final String BUNDLE_TO_LOGIN_FROM_MAIN = "bundle_to_login_from_main";

    public static final String BUNDLE_TO_BIND_PHONE = "bundle_to_bind_phone";
    public static final String BUNDLE_TO_BIND_PHONE_TYPE = "bundle_to_bind_phone_type";

    public static final int SHARE_TYPE_FACEBOOK = 0; // facebook
    public static final int SHARE_TYPE_TWITTER = 1; //twitter
    public static final int SHARE_TYPE_WHATS = 2; //whatsapp
    public static final int SHARE_TYPE_LINKEDIN = 3; //LinkedIn
    public static final int SHARE_TYPE_GMAIL = 5; //gmail
    public static final int SHARE_TYPE_INS = 7; //gmail
    public static final int SHARE_TYPE_REPORT = 4; //举报
    public static final int SHARE_TYPE_DELETE = 5; //删除

    public static final int JS_RESPONSE_CODE_SUCCEED = 200;
    public static final int JS_RESPONSE_CODE_FAIL = -1;
    public static final int JS_RESPONSE_CODE_CANCEL = -2;


    public static final String TYPE_FACEBOOK = "facebook";

    public static final int TWITTER_SHARE_IAMGE = 1;
    public static final int TWITTER_SHARE_URL = 0;

    //分享图片地址
    public static final String SAVE_SHARE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "juNews" + File.separator + "shareImage" + File.separator;


    public static final String AD_TYPE_UD = "101"; //百度
    public static final String AD_TYPE_YAHOO = "102"; //yahoo  102
    public static final String AD_TYPE_FACEBOOK = "103"; //facebook  103
    public static final String AD_TYPE_GOOGLE = "105";//google

    //临时常量
    public static final String LLURL = "weburl";


    public static final String REFRESH_TIPS = "refresh_item";  //刷新
    public static final String REFRESH_VIDEO = "refresh_video";  //刷新个人作品


    //广告统计自定义事件
    public static final String AD_TYPE_YAHOO_CLICK = "ad_yahoo_click"; //yahoo ad点击
    public static final String AD_TYPE_YAHOO_LOOK = "ad_yahoo_look"; //yahoo ad 曝光
    public static final String AD_TYPE_GOOGLE_NEWS_CLICK = "ad_google_news_click"; //google ad点击 news
    public static final String AD_TYPE_GOOGLE_NEWS_LOOK = "ad_google_news_look"; //google ad曝光 news
    public static final String AD_TYPE_GOOGLE_VIDEO_CLICK = "ad_google_video_click"; //google ad点击 video
    public static final String AD_TYPE_GOOGLE_VIDEO_LOOK = "ad_google_video_look"; //google ad曝光 video
    public static final String AD_TYPE_GOOGLE_ME_CLICK = "ad_google_me_click"; //google ad点击 me
    public static final String AD_TYPE_GOOGLE_ME_LOOK = "ad_google_me_look"; //google ad曝光 me
    public static final String AD_TYPE_GOOGLE_VIDEO_ACTIVITY_CLICK = "ad_google_video_activity_click"; //google ad点击video activity
    public static final String AD_TYPE_GOOGLE_VIDEO_ACTIVITY_LOOK = "ad_google_video_activity_look"; //google ad曝光 video activity
    public static final String AD_TYPE_GOOGLE_REWARDED_LOOK = "ad_google_rewarded_look"; //google 奖励视频曝光
    public static final String AD_TYPE_GOOGLE_WEB_CLICK = "ad_google_web_click"; //google ad点击 web
    public static final String AD_TYPE_GOOGLE_WEB_LOOK = "ad_google_web_look"; //google ad曝光 web
    public static final String AD_UD_LOOK = "ad_ud_look";//百度海外版 ad曝光
    public static final String AD_UD_CLICK = "ad_ud_click";//百度海外版 ad点击事件
    public static final String AD_TYPE_CHARTBOOST_VIDEO_CLICK = "ad_chartboost_video_click"; //Chartboost广告点击
    public static final String AD_TYPE_CHARTBOOST_VIDEO_LOOK = "ad_chartboost_video_look"; //Chartboost广告曝光
    public static final String AD_TYPE_VUNGLE_VIDEO_LOOK = "ad_vungle_video_look";//vungle广告曝光
    public static final String AD_TYPE_VUNGLE_VIDEO_COMPLETED = "ad_vungle_video_completed";//vungle广告完成
    public static final String AD_TYPE_VUNGLE_VIDEO_CLICK = "ad_vungle_video_click";//vungle广告点击
    public static final String AD_TYPE_UNITY_VIDEO_LOOK = "ad_unity_video_look";//vungle广告曝光
    public static final String AD_TYPE_UNITY_VIDEO_COMPLETED = "ad_unity_video_completed";//vungle广告完成
    public static final String AD_TYPE_DU_VIDEO_LOOK = "ad_du_video_look";//百度广告曝光
    public static final String AD_TYPE_DU_VIDEO_COMPLETED = "ad_du_video_completed";//百度广告完成
    public static final String AD_TYPE_DU_VIDEO_CLICK = "ad_du_video_cilck";//百度广告点击
    public static final String AD_TYPE_LIST_DU_LOOK = "ad_type_list_du_look";//百度原生广告曝光
    public static final String AD_TYPE_NES_LIST_DU_LOOK = "ad_type_new_list_du_look";//新闻列表百度原生广告曝光
    public static final String AD_TYPE_VIDEO_AIRPUSH_LOOK = "ad_type_video_airpush_look";//竖向视频页面airpush广告banner曝光
    public static final String AD_TYPE_VIDEO_AIRPUSH_CLICK = "ad_type_video_airpush_click";//竖向视频页面airpush广告banner点击
    public static final String AD_TYPE_NES_LIST_FACEBOOK_LOOK = "ad_type_new_list_facebook_look";//视频列表facebook曝光
    public static final String AD_TYPE_DU_BANNER_LOOK = "baidu_ad_banner_look";//百度banner广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK = "google_interstitial_ad_look";//google广告插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK = "google_interstitial_ad_click";//google广告插页广告点击


    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_BASIC = "google_interstitial_ad_look_basic";//google广告BASIC插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_BASIC = "google_interstitial_ad_click_basic";//google广告BASIC插页广告点击
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_ME = "google_interstitial_ad_look_me";//google广告Me插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_ME = "google_interstitial_ad_click_me";//google广告Me插页广告点击
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_WEB_OUTPUT = "google_interstitial_ad_look_web_output";//google广告web返回插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_WEB_OUTPUT = "google_interstitial_ad_click_web_output";//google广告web返回插页广告点击
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_PERMANENT = "google_interstitial_ad_look_permanent";//google广告PERMANENT插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_PERMANENT = "google_interstitial_ad_click_permanent";//google广告PERMANENT插页广告点击
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_DAILY = "google_interstitial_ad_look_daily";//google广告DAILY插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_DAILY = "google_interstitial_ad_click_daily";//google广告DAILY插页广告点击
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_WITHDRAW = "google_interstitial_ad_look_withdeaw";//google广告WITHDRAW插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_WITHDRAW = "google_interstitial_ad_click_withdeaw";//google广告WITHDRAW插页广告点击
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_ENTER = "google_interstitial_ad_look_enter";//google广告ENTER插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_ENTER = "google_interstitial_ad_click_enter";//google广告ENTER插页广告点击
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_INVITE = "google_interstitial_ad_look_invite";//google广告INVITE插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_INVITE = "google_interstitial_ad_click_invite";//google广告INVITE插页广告点击
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_LOOK_TRANSACTIONS = "google_interstitial_ad_look_transactions";//google广告TRANSACTIONS插页广告曝光
    public static final String AD_TYPE_GOOGLE_INTERSTITIAL_CLICK_TRANSACTIONS = "google_interstitial_ad_click_transactions";//google广告TRANSACTIONS插页广告点击


    public static final int STORIES_PAGE = 0;//第一个foryou stories页面
    public static final int FOLLOW_PAGE = 1;//follow页面
    public static final int VIDEO_VIDEOS_PAGE = 2;//video页面videos数据
    public static final int VIDEO_LIKES_PAGE = 3;//video页面likes数据
    public static final int OTHER_VIDEOS_PAGE = 4;//other页面videos数据
    public static final int OTHER_LIKES_PAGE = 5;//other页面likes数据

    public static final String PAGE_VIDEOS_CLICK = "page_videos_click";//videos页点击
    public static final String PAGE_VLOG_CLICK = "page_vlog_click";//vlog页点击
    public static final String PAGE_NEWS_CLICK = "page_news_click";//news页点击


    public static final String AD_TYPE_GOOGLE_NATIVE_LOOK = "ad_type_google_native_look";//google原生广告曝光
    public static final String AD_TYPE_GOOGLE_NATIVE_CLICK = "ad_type_google_native_click";//google原生广告点击
}
