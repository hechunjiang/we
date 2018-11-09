package wedemo.utils;

/**
 * Created by admin on 2018/7/5.
 */

public class RatioUtil {
    public static boolean ratioIsUnsuitable(int curTimelineRatio, int aspectRatio) {
        switch (curTimelineRatio) {
            case 1:
                return aspectRatio % 2 == 0;
            case 2:
                int retResbyFour = aspectRatio % 4;
                return retResbyFour == 0 || retResbyFour == 1;
            case 4:
                int retResbyEight = aspectRatio % 8;
                return retResbyEight >= 0 && retResbyEight <= 3;
            case 8:
                return aspectRatio < curTimelineRatio;
        }
        return false;
    }
}
