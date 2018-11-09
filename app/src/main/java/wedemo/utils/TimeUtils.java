package wedemo.utils;

import android.content.Context;

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
        long serviceTime = 1000 * 1000;
        //获取服务器时间时的本地时间
        long localTime = 1000 * 1000;
        //当前时间
        long phoneTime = new Date().getTime();
        long nowTime = phoneTime + (serviceTime - localTime);
        Date w_ret = new Date();
        w_ret.setTime(phoneTime + (serviceTime - localTime));
        return w_ret.getTime();
    }

}