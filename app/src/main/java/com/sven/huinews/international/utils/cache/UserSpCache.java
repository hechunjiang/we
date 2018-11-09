package com.sven.huinews.international.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sven.huinews.international.entity.User;
import com.sven.huinews.international.entity.Users;
import com.sven.huinews.international.entity.response.PushTaskResponse;


/**
 * Created by chenshuaiwen on 18/1/25.
 */

public class UserSpCache {
    public static final String KEY_CURRENT_DATA_TIME = "key_current_data_time";
    public static final String KEY_DEVICE_TOKEN = "key_device_token";

    public static final String KEY_IS_FIRST_OPEN_APP = "key_is_first_open_app";
    public static final String KEY_IS_SECEND_OPEN_APP = "key_is_secend_open_app";
    public static final String KEY_TOKEN_IS_EXPIRE = "key_token_is_expire";
    public static final String KEY_IS_SHOW_INTRODUCE = "key_is_show_introduce";
    public static final String LIST_BEAN = "listBean";
    public static final String VIDEO_ID = "videoid";

    public static final String KEY_TICKET = "key_ticket";
    public static final String KEY_IS_USER_LOGIN = "key_is_user_login";
    public static final String KEY_USER = "key_user";
    public static final String KEY_PHONE = "key_phone";
    public static final String NIKE_NAME = "NIKE_NAME";
    public static final String KEY_PASS = "key_pass";
    public static final String OPENID = "openId";
    public static final String KEY_USER_ID = "key_user_id";
    public static final String TIPS_FLAG = "tips_flag";
    public static final String TIPS_LOCAL_FLAG = "tips_local_flag";

    public static final String OPEN_COUNT = "open_count"; //记录打开的次数
    public static final String NEEDCOUNT_LOGIN = "need_count_login"; //记录服务器打开次数
    public static final String SIGN_SERVICE_TIME = "sign_service_time"; //同步服务器时间之服务器时间
    public static final String SIGN_LOCAL_TIME = "sign_localtime"; //同步服务器时间之获取服务器时间时的手机时间


    public static final String V_AT_COUNT = "v_at_count";//服务端配置的金币次数
    public static final String V_AT_RED = "v_at_red";//服务的配置的红包次数
    public static final String OPEN_GOLD_COUNT = "open_gold_count"; //当前金币获取次数
    public static final String OPEN_RED_COUNT = "open_red_count"; //当前红包获取次数
    public static final String SHARE_TITLE = "share_title";
    public static final String SHARE_CONTENT = "share_content";
    public static final String PUSHTASK = "TASKDATABEAN";
    public static final String WX_UNIONID = "unionid";
    public static final String GOLD_TIME = "gold_time";
//    public static final String GOLD_NUMBER = "gold_number";
    public static final String GOLD_NUMBERS = "gold_numbers";

    public static final String KEY_REFERRER = "referrer";//记录google渠道

    private SharedPreferences mSharedPreferences;
    private final static String CACHE_NAME = "USER_CACHE";
    private static UserSpCache mInstance;


    public static final String REQUEST_CODE = "request_code";//key
    public static final String REQUEST_TIME = "request_time";//时间  下次请求时间
    public static final String REQUEST_LONG = "request_long";//时长  持续时间
    public static final String REQUEST_ALLLONG = "request_all_long";//时长  持续时间

    public static final String ME_PAGE_AD_TYPE = "me_page_ad_type";//me页面banner广告类型
    public static final String VIDEO_PAGE_AD_TYPE = "video_page_ad_type";//视频播放页面banner广告类型
    public static final String VIDEO_PAGE_VIDEO_AD_TYPE = "video_page_video_ad_type";//视频播放页面video广告类型
    public static final String H5_PAGE_AD_PROJECTILE_FRAME_TYPE = "h5_page_ad_projectile_frame_type";//视频播放页面banner广告类型

    private UserSpCache(Context context) {
        mSharedPreferences = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
    }

    public static UserSpCache getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UserSpCache.class) {
                if (mInstance == null) {
                    mInstance = new UserSpCache(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 存储String类型的键值对
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key) {
        return mSharedPreferences.getString(key, "");
    }

    /**
     * 存储long
     *
     * @param key
     * @param value
     */
    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, -1);
    }

    /**
     * 存储int
     *
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, -1);
    }


    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public void deleteData(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearCache() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void putUser(User user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_USER, new Gson().toJson(user));
        editor.putString(KEY_USER_ID, user.getData().getUser_info().getId());
        editor.commit();
    }

    public void putUser(Users user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_USER, new Gson().toJson(user));
        editor.putString(KEY_USER_ID, user.getId());
        editor.commit();
    }

    public void putUserInfo(User.DataBean.UserInfoBean userInfo){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_USER, new Gson().toJson(userInfo));
        editor.putString(KEY_USER_ID, userInfo.getId());
        editor.commit();

    }


    public User getUser() {
        String userJson = mSharedPreferences.getString(KEY_USER, "");
        if (TextUtils.isEmpty(userJson)) {
            return new User();
        }
        return new Gson().fromJson(userJson, User.class);
    }


    public void putPageListBean(PushTaskResponse.DataBean.PageListBean listBean) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LIST_BEAN, new Gson().toJson(listBean));
        editor.commit();
    }

    public PushTaskResponse.DataBean.PageListBean getPageListBean() {
        String listBean = mSharedPreferences.getString(LIST_BEAN, "");
        return new Gson().fromJson(listBean, PushTaskResponse.DataBean.PageListBean.class);
    }
}
