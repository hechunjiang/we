package com.sven.huinews.international.utils;

import android.content.Context;

import com.sven.huinews.international.utils.cache.UserSpCache;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sfy. on 2018/3/22 0022.
 */

public class TimeUtils {

    public static TimeUtils instance;

    public TimeUtils() {

    }

    public long phoneTime(Context mContext) {
        //服务器时间
        long serviceTime = UserSpCache.getInstance(mContext).getLong(UserSpCache.SIGN_SERVICE_TIME) * 1000;
        //服务器本地时间
        long localTime = UserSpCache.getInstance(mContext).getLong(UserSpCache.SIGN_LOCAL_TIME);
        //当前时间
        long phoneTime = new Date().getTime();
        Date w_ret = new Date();
        w_ret.setTime(phoneTime + (serviceTime - localTime));
        return w_ret.getTime();
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }


    /**
     * 时间计算  加
     *
     * @param d
     * @param second
     * @return
     */
    public static String getTime(String d, int second) {
        Calendar now = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
        try {
            now.setTime(df.parse(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + second);
        return df.format(now.getTime());
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date(System.currentTimeMillis()));
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 将年月日转化为时间戳
     *
     * @param dateString
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}