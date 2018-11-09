package com.sven.huinews.international.main.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.sven.huinews.international.entity.event.ClosePageEvent;
import com.sven.huinews.international.entity.event.IniSucceedEvent;
import com.sven.huinews.international.entity.event.JsSmsContent;
import com.sven.huinews.international.entity.event.OpenLoginPageEvent;
import com.sven.huinews.international.entity.event.OpenNewPageEvent;
import com.sven.huinews.international.entity.event.ToMainPageEvent;
import com.sven.huinews.international.entity.jspush.JsGoToRarn;
import com.sven.huinews.international.entity.jspush.JsGoldCome;
import com.sven.huinews.international.entity.jspush.JsHideAd;
import com.sven.huinews.international.entity.jspush.JsLogin;
import com.sven.huinews.international.entity.jspush.JsOpenGoogleAd;
import com.sven.huinews.international.entity.jspush.JsOpenTheTreasureBox;
import com.sven.huinews.international.entity.jspush.JsPageLoadCompletion;
import com.sven.huinews.international.entity.jspush.VideosTimeLast;
import com.sven.huinews.international.utils.AppInfoUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by sfy. on 2018/9/14 0014.
 */

public class PersonalJs {
    //登录
    public static final String METHOD_LOGIN = "login";
    //打开新页面
    public static final String METHOD_OPEN_NEW_WEB_PAGE = "openNewWebPage";
    //关闭当前页面
    public static final String METHOD_CLOSE_NEW_WEB_PAGE = "closeNewWebPage";
    //回到首页
    public static final String METHOD_TO_MAIN_PAGE = "toMainPage";
    //微信登录
    public static final String METHOD_WECHAT_LOGIN = "wechatLogin";
    //分享
    public static final String METHOD_SHARE = "share";

    public static final String METHOD_SHARE_TO_ONE = "shareToOne";

    public static final String METHOD_OPEN_SMS = "openSms";

    public static final String METHOD_GOLD_WELCOME = "goldWelcome";

    public static final String OPEN_INTRODUCE_VIDEO = "openIntroduceVideo";

    public static final String METHOD_TO_ME_PAGE = "toMePage";

    public static final String METHOD_SCAN_COLLECT_VIDEO = "scanCollectVideo";
    public static final String METHOD_INI_ON_SUCCEED = "inputIniOnSucceed"; //邀请码输入成功

    private boolean isRefreshUrl = true;

    public boolean isRefreshUrl() {
        return isRefreshUrl;
    }

    public void setRefreshUrl(boolean refreshUrl) {
        isRefreshUrl = refreshUrl;
    }

    private Context mContext;

    public PersonalJs(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public String login() {
        LogUtil.showLog("js ==  login");
        JsLogin jsLogin = new JsLogin();
        jsLogin.setLoginTicket(UserSpCache.getInstance(mContext.getApplicationContext()).getStringData
                (UserSpCache.KEY_TICKET));
        jsLogin.setSystemName("android");
        jsLogin.setSign("121213213123");
        jsLogin.setMeid(PhoneUtils.getAndroidId(mContext));
        jsLogin.setVersion(AppInfoUtils.getVersion(mContext));
        jsLogin.setLanguage(PhoneUtils.getLocalLanguage());
        return new Gson().toJson(jsLogin);
    }

    @JavascriptInterface
    public String videostimelast() {
        LogUtil.showLog("Js调用登录方法");
        VideosTimeLast vtl = new VideosTimeLast();
        vtl.setLastTime(UserSpCache.getInstance(mContext).getLong(UserSpCache.REQUEST_ALLLONG));
        vtl.setLookTime(UserSpCache.getInstance(mContext).getLong(UserSpCache.REQUEST_LONG));
        LogUtil.showLog("videostimelast():" + new Gson().toJson(vtl));
        return new Gson().toJson(vtl);
    }


    @JavascriptInterface
    public void refreshUrl(boolean isRefresh) {
        LogUtil.showLog("msg---isRefresh:" + isRefresh);
        setRefreshUrl(isRefresh);
    }


    @JavascriptInterface
    public void openLoginPage() {
        EventBus.getDefault().post(new OpenLoginPageEvent());
    }

    /*@JavascriptInterface
    public void inputIniOnSucceed() {
        //输入邀请码 后关闭webview 并刷新界面
        EventBus.getDefault().post(new IniSucceedEvent());
    }*/

    public void openNewWebPage(String url) {
        LogUtil.showLog("JS调起打开新页面" + url);
        //在新的任务栈打开网页
        EventBus.getDefault().post(new OpenNewPageEvent(url));
    }

    public void closeNewWebPage() {
        LogUtil.showLog("JS关闭当前任务栈");
        //关闭新的任务栈
        EventBus.getDefault().post(new ClosePageEvent());
    }

    public void goToMainNewsPage() {
        //回到首页
        LogUtil.showLog("JS请求回到首页");
        EventBus.getDefault().post(new ToMainPageEvent());
    }


    public void sendSms(String smsJson) {
        JsSmsContent jsSmsContent = new Gson().fromJson(smsJson, JsSmsContent.class);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", jsSmsContent.getContent());
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void goldWelcome(String json) {
        LogUtil.showLog("显示金币" + json);
        JsGoldCome jsGoldCome = new Gson().fromJson(json, JsGoldCome.class);
        LogUtil.showLog(new Gson().toJson(jsGoldCome));
        EventBus.getDefault().post(jsGoldCome);
    }

    @JavascriptInterface
    public void hideAd() {
        EventBus.getDefault().post(new JsHideAd());
    }

    @JavascriptInterface
    public void goldOpenTheTreasureBox(String json) {
        LogUtil.showLog("打开宝箱" + json);
        JsOpenTheTreasureBox jsOpenTheTreasureBox = new Gson().fromJson(json, JsOpenTheTreasureBox.class);
        EventBus.getDefault().post(jsOpenTheTreasureBox);
    }

    @JavascriptInterface
    public void openGoogleAd(String json) {
        LogUtil.showLog("打开google插页广告" + json);
        JsOpenGoogleAd jsOpenGoogleAd = new Gson().fromJson(json, JsOpenGoogleAd.class);
        EventBus.getDefault().post(jsOpenGoogleAd);
    }


    @JavascriptInterface
    public void goEarnPage() {
        ((Activity) mContext).finish();
        EventBus.getDefault().post(new JsGoToRarn());
    }

    /**
     * 页面加载完成
     *
     * @param title
     */
    @JavascriptInterface
    public void pageLoadCompletion(String title) {
//        ToastUtils.showLong(mContext,title);
        EventBus.getDefault().post(new JsPageLoadCompletion(title));
    }

}
