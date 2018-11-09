package com.sven.huinews.international.main.web.webjs;

import android.content.Context;
import android.webkit.JavascriptInterface;


import com.google.gson.Gson;
import com.sven.huinews.international.config.AppInfo;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;

import org.greenrobot.eventbus.EventBus;

/**
 * auther: sunfuyi
 * data: 2018/5/14
 * effect: js交互
 */
public class PersonalJs {
    private Context mContext;

    //回到首页
    public static final String METHOD_TO_MAIN_PAGE = "toMainPage";
    //跳转个人中心
    public static final String METHOD_TO_MINE_PAGE = "inputIniOnSucceed";
    //分享
    public static final String METHOD_SHARE_TO_ONE = "shareToOne";
    //微信登录
    public static final String METHOD_WECHAT_LOGIN = "wechatLogin";
    //金币动画
    public static final String METHOD_GOLD_WELCOME = "goldWelcome";
    //书籍排行榜
    public static final String METHOD_BOOK = "toBookDetail";


    private boolean isRefreshUrl = true;

    public boolean isRefreshUrl() {
        return isRefreshUrl;
    }

    public void setRefreshUrl(boolean refreshUrl) {
        isRefreshUrl = refreshUrl;
    }

    public PersonalJs(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public String login() {

        JsLogin jsLogin = new JsLogin();
        jsLogin.setLoginTicket(UserSpCache.getInstance(mContext.getApplicationContext()).getStringData(UserSpCache.KEY_TICKET));
        jsLogin.setSystemName("android");
        jsLogin.setSign("121213213123");
        jsLogin.setMeid(PhoneUtils.getAndroidId(mContext));
        jsLogin.setVersion(AppInfo.getVersion(mContext));
        return new Gson().toJson(jsLogin);
    }

    @JavascriptInterface
    public void refreshUrl(boolean isRefresh) {
        LogUtil.showLog("msg---refreshUrl:" + isRefresh);
        setRefreshUrl(isRefresh);
    }

    @JavascriptInterface
    public void openLoginPage() {
        LogUtil.showLog("msg---跳转登录");
//        EventBus.getDefault().post(Constant.TO_MINE_TO_LOGIN);
    }

    /**
     * 跳转支付界面
     */
    @JavascriptInterface
    public void openPayPage(){
  //      EventBus.getDefault().post(Constant.TO_PAY_PAGE);
    }

    @JavascriptInterface
    public void inputIniOnSucceed() {
    //    EventBus.getDefault().post(Constant.TO_MINE_PAGE_EVENT);
    }

    @JavascriptInterface
    public void goldWelcome(String json) {
      //  JsGoldCome jsGoldCome = new Gson().fromJson(json, JsGoldCome.class);
       // EventBus.getDefault().post(jsGoldCome);
    }

    public void goToMainNewsPage() {
       // EventBus.getDefault().post(Constant.TO_MAIN_PAGE_EVENT);
    }


    public void goToMinePage() {
      //  EventBus.getDefault().post(Constant.TO_MINE_PAGE_EVENT);
    }

}
