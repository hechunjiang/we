package com.sven.huinews.international.config;

import android.os.Environment;

import com.sven.huinews.international.BuildConfig;

import java.io.File;

/**
 * 常量
 */
public class Constant {

    public static final String CACHE_LAT = "lat";
    public static final String CACHE_LNG = "lng";
    public static final String CACHE_COUNTRY = "country";
    public static final String FACEBOOK = "fb";
    public static final String TWITTER = "tt";
    public static final String LINKEDIN = "lk";
    public static final String GOOGLELOGIN = "gmail";
    //event bus
    // public static final String TO_MAIN_PAGE_EVENT = "toMainPageEvent";
    //登陆成功
    public static final String LOGIN_SUCCESS = "login_success";
    public static final String OTHER_LOGIN = "other_login";

    //User常量
    public static final String KEY_TICKET = "key_ticket";
    public static final String KEY_IS_USER_LOGIN = "key_is_user_login";
    public static final String KEY_PHONE = "key_phone";
    public static final String NIKE_NAME = "NIKE_NAME";
    public static final String KEY_PASS = "key_pass";
    public static final String C_USERID = "key_pass";
    public static final String OPENID = "openId";
    public static final String BUNDLE_VIDEO_LIST = "bundle_video_list";
    public static final String KEY_USER = "key_user";
    public static final String nowTime = "nowTime";
    public static final String KEY_IS_SECEND_OPEN_APP = "key_is_secend_open_app";

    public static final String COMMENT_UP = "up"; //点赞数量排序
    public static final String COMMENT_TIME = "time"; //评论时间排序
    public static final int LIKE_TYPE = 1; //视频点赞
    public static final int UN_LIKE_TYPE = 2; //取消视频点赞
    public static final int CHANNEL_TYPE_VIDEO = 1; //视频
    public static final int SELECT_USER_VIDEOS = 1;//查询用户的视频
    public static final int SELECT_USER_LIKE_VIDEOS = 2;//查询用户喜欢的视频

    //bundle常量
    public static final String BUNDLE_TO_WEB_URL = "bundle_to_web_url";
    public static final String BUNDLE_FORVIDEO_LIST = "bundle_video_list";
    public static final String BUNDLE_VIDEO_LIST_POSITION = "bundle_video_list_position";
    public static final String BUNDLE_VIDEO_LIST_TYPE = "bundle_video_list_type";
    public static final String BUNDLE_TO_LOGIN_FROM_MAIN = "bundle_to_login_from_main";
    public static String WEB_URL = "webUrl";
    public static final String NEWSINFO = "newsinfo";


    public static final int EDIT_MODE_PHOTO_AREA_DISPLAY = 2001;//图片运动-区域显示
    public static final int EDIT_MODE_PHOTO_TOTAL_DISPLAY = 2002;//图片运动-全图显示
    public static final int EDIT_MODE_CUSTOMSTICKER = 2003;//自定义贴纸

    public static final String NO_FX = "None"; // 无特效的ID

    public static final String SAVE_APK_PATH = Environment.getExternalStorageDirectory() + File.separator + "watch$earn" + File.separator + "apk" + File.separator;


    public static final String UPDATE_URL = BuildConfig.BASE_URL + "juNew_update.xml";
}
