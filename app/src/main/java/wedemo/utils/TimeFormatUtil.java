package wedemo.utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by admin on 2018/6/15.
 */

public class TimeFormatUtil {
    private static final double TIMEBASE = 1000000D;

    /**
     *  格式化时间串00:00:00.0
     */
    public static String formatUsToString1(long us){
        double totalSecond = us / TIMEBASE;
        int hour = (int)totalSecond / 3600;
        int minute = (int)totalSecond % 3600 / 60;
        double second = totalSecond % 60;
        String timeStr = hour > 0 ? String.format("%02d:%02d:%04.1f", hour, minute, second) : String.format("%02d:%04.1f", minute, second);
        return timeStr;
    }

    /**
     *  格式化时间串00:00:00
     */
    public static String formatUsToString2(long us) {
        int second = (int) (us / 1000000.0 + 0.5);
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String timeStr;
        if (us == 0) {
            return "00:00";
        }
        if (hh > 0) {
            timeStr = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            timeStr = String.format("%02d:%02d", mm, ss);
        }
        return timeStr;
    }

    /**
     * 毫秒转换成hh:mm:ss格式
     */
    public static String formatMsToString(long ms) {
        if (ms <= 0 || ms >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = (int)ms / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
