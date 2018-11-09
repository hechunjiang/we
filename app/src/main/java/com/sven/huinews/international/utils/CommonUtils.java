package com.sven.huinews.international.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.sven.huinews.international.entity.jspush.JsShareResponse;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sven on 2018/2/5.
 */

public class CommonUtils {
    private static final int PAGE_SIZE = 20;
    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isNoMoreData(int listSize) {
        if (listSize < PAGE_SIZE) {
            return true;
        }
        return false;
    }

    public static boolean checkPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }
        if (phoneNum.length() != 11) {
            return false;
        }
        String regExp = "^(1)\\d{10}$";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }

    public static boolean checkIsEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPassWord(String passWord) {
        if (TextUtils.isEmpty(passWord)) {
            return false;
        }
        if (passWord.length() < 6) {
            return false;
        }
        if (passWord.length() > 22) {
            return false;
        }
        return true;
    }

    public static boolean checkPhoneCode(String phoneCode) {
        if (phoneCode.length() != 4) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(phoneCode).matches();
    }

    public static boolean checkComment(String commentContent) {
        if (TextUtils.isEmpty(commentContent)) {
            return false;
        }
        return true;
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static String readRawTxt(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String getLikeCount(int likeCount) {
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        double doubleMoney;
        if (likeCount < 1000) {
            return likeCount + "";
        } else if (likeCount < 999999 && likeCount > 1000) {
            doubleMoney = likeCount * 0.001;
            return decimalFormat.format(doubleMoney) + "K";
        } else if (likeCount < 999999999 && likeCount > 1000000) {
            doubleMoney = likeCount * 0.000001;
            return decimalFormat.format(doubleMoney) + "M";
        } else {
            doubleMoney = likeCount * 0.000000001;
            return decimalFormat.format(doubleMoney) + "B";
        }
    }

    public static String getPlayCount(int playCount) {
        if (playCount < 10000) {
            return playCount + "views";
        }
        double doubleMoney = playCount * 0.0001;
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        return decimalFormat.format(doubleMoney) + "M views";
    }

    public static String getDuration(long duration) {
        duration = duration / 1000;
        return /*format(duration / 3600) + ":" + */format(duration / 60 % 60) + ":" + format(duration % 60);

    }

    public static String getDurationInt(long duration) {
        duration = duration / 1000;
        return format(duration / 3600) + ":" + format(duration / 60 % 60) + ":" + format(duration % 60);

    }

    private static String format(long i) {
        return i < 10 ? "0" + i : String.valueOf(i);
    }

    public static String getDuration(int duration) {

        return /*format(duration / 3600) + ":" + */format(duration / 60 % 60) + ":" + format(duration % 60);
    }


    /**
     * 时间戳获取日期时间
     *
     * @param time
     * @return
     */
    public static String getDataTime(long time) {
        time = time * 1000;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(time);
        return date;
    }

    public static String getLongTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(time);
        return date;
    }

    public static String getDataTimeDay(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(time);
        return date;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String body(String str, Type mListType) {
        String o = null;
        try {
            List<String> mlist = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(str);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                //获取key值
                String key = keys.next().toString();
                //获取value值
                String value = null;
                o = jsonObject.getString(key);
                //   mlist.add(value);
            }


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return o;
    }

    /**
     * 毫秒转化为秒
     *
     * @param mss
     * @return
     */
    public static String formatMillisecondsToSeconds(long mss) {
        Date date = new Date(mss);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String seconds = sdf.format(date);
        return seconds;
    }

    /**
     * 判断手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 组装出以某个字符串间隔的字符串
     *
     * @param list
     * @param strInterval 间隔字符串
     * @return
     */
    public static String getStringByStringList(List<String> list, String strInterval) {
        String str = "";

        if (list == null)
            return str;

        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i) + strInterval;
        }

        if (str.lastIndexOf(strInterval) != -1)
            str = str.substring(0, str.lastIndexOf(strInterval));

        return str;
    }


    /**
     * 分享成功response
     * 返回类型 {"code":200,"msg":"分享成功"}
     */
    public static String getShareSuccesResponse() {
        JsShareResponse jsShareResponse = new JsShareResponse();
        jsShareResponse.setCode(Common.JS_RESPONSE_CODE_SUCCEED);
        jsShareResponse.setMsg("分享成功");
        return new Gson().toJson(jsShareResponse);
    }

    /**
     * 分享失败response
     * 返回类型 {"code":200,"msg":"分享失败"}
     */
    public static String getShareFailResponse() {
        JsShareResponse jsShareResponse = new JsShareResponse();
        jsShareResponse.setCode(Common.JS_RESPONSE_CODE_FAIL);
        jsShareResponse.setMsg("分享失败");
        return new Gson().toJson(jsShareResponse);
    }

    /**
     * 分享取消response
     * 返回类型 {"code":200,"msg":"分享取消"}
     */
    public static String getShareCancelResponse() {
        JsShareResponse jsShareResponse = new JsShareResponse();
        jsShareResponse.setCode(Common.JS_RESPONSE_CODE_CANCEL);
        jsShareResponse.setMsg("分享取消");
        return new Gson().toJson(jsShareResponse);
    }


    public Date parse(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(strDate);
    }

    //由出生日期获得年龄
    public int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

}
