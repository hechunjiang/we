package wedemo.utils;

/**
 * Created by admin on 2018-5-29.
 */

public class Constants {

    public static final boolean SHARE_TEST = false;
    public static final boolean UPLOAD_TEST = false;
    public static final boolean GUIDE_TEST = false;
    public static final boolean DRAFT_VIDEO = false;

    //是否开启友盟统计
    public static final boolean UMENG_TEST = true;

    public static final int IMAGE_WIDTH = 720;
    public static final int IMAGE_HEIGHT = 1280;

    public static final int SCREEN_TYPE = 2;
    public static final int RESOLUTION_TYPE = 4;

    public static final int TYPE_CAPTURESCENE = 8;
    public static final int TYPE_ANIMATEDSTICKER = 4;

    public static final int LIMIT_TIME = 31 * 1000 * 1000;

    public static final String REFRESH_DRATS = "refresh_drafts";


    /**
     * sp中用到的key
     */
    public static final String KEY_PARAMTER = "paramter";

    public static final int EDIT_MODE_CAPTION = 0;
    public static final int EDIT_MODE_STICKER = 1;
    public static final long NS_TIME_BASE = 1000000;

    public static final int MEDIA_TYPE_AUDIO = 1;

    public static final int ACTIVITY_START_CODE_MUSIC = 100;

    public static final String START_ACTIVITY_FROM_CAPTURE = "start_activity_from_capture";
    public static final String CAN_USE_ARFACE_FROM_MAIN = "can_use_arface_from_main";

    public static final boolean canUseARFace = false;

    public static final int FROMMAINACTIVITYTOVISIT = 1001;//从主页面进入视频选择页面
    public static final int FROMCLIPEDITACTIVITYTOVISIT = 1002;//从片段编辑页面进入视频选择页面
    public static final int FROMCUSTOMSTICKERACTIVITYTOVISIT = 1003;//从自定义贴纸页面进入视频选择页面

    public static final int EDIT_MODE_PHOTO_AREA_DISPLAY = 2001;//图片运动-区域显示
    public static final int EDIT_MODE_PHOTO_TOTAL_DISPLAY = 2002;//图片运动-全图显示
    public static final int EDIT_MODE_CUSTOMSTICKER = 2003;//自定义贴纸

    public static final String NO_FX = "None"; // 无特效的ID

    public static final int RECORD_TYPE_NULL = 3000;
    public static final int RECORD_TYPE_PICTURE = 3001;
    public static final int RECORD_TYPE_VIDEO = 3002;

    //视音频音量值
    public static final float VIDEOVOLUME_DEFAULTVALUE = 1.0f;
    public static final float VIDEOVOLUME_MAXVOLUMEVALUE = 2.0f;
    public static final int VIDEOVOLUME_MAXSEEKBAR_VALUE = 100;

    public static final String SDK_LIC = "assets:/B89976D0-B001-789D-B65C-H7C7023B573.lic";
}
