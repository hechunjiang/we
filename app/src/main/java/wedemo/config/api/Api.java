package wedemo.config.api;


import com.sven.huinews.international.BuildConfig;

public class Api {

    //h5
    public static final String H5_INCOME_TYPE_1 = BuildConfig.BASE_WEB_URL + "personal/incomeDetails.html?type=1";
    public static final String H5_INCOME_TYPE_2 = BuildConfig.BASE_WEB_URL + "personal/incomeDetails.html?type=2";

    //config
    public static final String HEADER_OS = "os";
    public static final String HEADER_MEID = "meid";
    public static final String HEADER_VERSION = "version";
    public static final String HEADER_TICKET = "ticket";
    public static final String HEADER_RATIO = "ratio";
    public static final String HEADER_BRAND = "brand";
    public static final String HEADER_BLUETOOTH = "bh";//头文件蓝牙
    public static final String HEADER_OPTICAL_SENSOR = "sensor";//头文件光传感器
    public static final String HEADER_CPU = "cpu";//头文件cpu
    public static final String LANGUAGE = "language";//语言
    public static final String MAIL = "mail";
    public static final String PASS = "pass";
    public static final String PAGE = "page";
    public static final String PAGESIZE = "pagesize";
    public static final String PAGE_SIZE = "page_size";
    public static final String TOKEN = "token";
    public static final String R_TYPE = "r_type";
    public static final String TYPE = "type";
    public static final String CATEGORY = "category";
    public static final String R_ID = "rId";
    public static final String DEL_TYPE = "delType";
    public static final String VIDEO_ID = "video_id";
    public static final String CID = "cid";
    public static final String DUTYPE = "du_type";
    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String ORDER = "order";
    public static final String COMMENT_ID = "comment_id";
    public static final String F_INVIT_CODE = "f_invit_code";
    public static final String VERIFY = "verify";
    public static final String MOBILE_BRAND = "mobile_brand";
    public static final String ALVIDEO_ID = "id";
    //burgess
    public static final String ACTIVITY_TYPE = "activity_type";
    public static final String CODE = "code";
    public static final String SHARE_CHANNEL = "share_channel";
    public static final String TYPE_FACEBOOK = "facebook";
    public static final String FACEBOOK = "fb_id";
    public static final String TYPE_TIWTTER = "twitter";
    public static final String TIWTTER_ID = "twitter_id";
    public static final String TYPE_LINKEDIN = "linkedin";
    public static final String LINKEDIN_ID = "lk_id";
    public static final String KEYWORDS = "keywords";

    public static final String VIDEOID = " videoId";
    public static final String TITLE = "title";
    public static final String COVER_IMG = "coverImg";
    public static final String DURATION = "duration";
    public static final String HEIGHT = "height";
    public static final String WIDTH = "width";
    public static final String TAG = "tag";

    public static final String BASE_URL = BuildConfig.BASE_URL;
    // public static final String SHOT_URL = BuildConfig.SHOT_URL;
    public static final String BASE_H5_URL = BuildConfig.BASE_WEB_URL;

    //数据接口

    public static final String APP_CONFIG = "app/Setting/config";//手机配置信息
    public static final String LOGIN = "app/Login/index";//登录接口
    public static final String CHECK_LOGIN = "app/reg/check_login";//checklogin
    public static final String LOGIN_TEMP = "app/Reg/f_reg";//临时登录
    public static final String SMS_CODE_MAIL = "app/pub/mailSendCode";//获取注册验证码
    public static final String REGISTER_BY_EMAIL = "app/Reg/m_reg";//邮箱注册
    public static final String FIND_PASS_EMAIL = "app/reg/m_findpass";//重设密码
    public static final String USER_INFO = "app/User/index";//获取用户信息
    public static final String NEWS_INFO_LISTS = "app/news/todayNews"; //新闻列表
    public static final String VIDEO_LIKE = "app/video/like";//视频点赞
    public static final String UPLOAD_FILE = "app/pub/imageFormUpload";//上传头像
    public static final String USER_MSG = "app/user/setUserMsg";//上传个人信息
    public static final String SHARE_VISIT = "app/Datapre/shareVisit";//分享计数
    public static final String SHARE_INFO = "app/Activateshare/share";//获取分享信息

    public static final String VIDEO_LISTS = "app/video/lists";//视频列表
    public static final String VIDEO_COLLECTION = "Collection/cResource";//视频收藏   未验证
    public static final String VIDEO_CANCEL_COLLECTION = "Collection/delCollect";//取消收藏  未验证
    public static final String FOLLOWLISTS = "/user/mobile/video/getMyConernVideoList";//Follow列表
    public static final String ALIYUN_VIDEO_PALY = "user/aliyun/getVideoPlayInfo"; //阿里云视频播放地址获取
    public static final String VIDEO_COMMENT_LIST = "app/comment_video/lists";//请求视频评论列表
    public static final String VIDEO_COMMENT_LIKE = "app/comment_video/like"; //评论点赞
    public static final String VIDEO_COMMENT_REPORT = "app/Report/videocomment"; //评论举报
    public static final String VIDEO_EDIT_COMMENT = "app/comment_video/push";//视频发布评论


    public static final String MUSIC_CATEGROY = "app/music_type/getMusicTypeList";//音乐分类
    public static final String MUSIC_LIST = "app/Music/getMusicListByCid";//音乐列表
    public static final String MUSIC_SEARCH = "app/Music/searchMusic";//搜索音乐
    public static final String ALI_UOLOAD_TOKEN = "app/demand/getUserToken";//阿里云临时上传token
    public static final String UOLOAD_VIDEO = "app/demand/updateVideoId";//上传视频信息
    public static final String TAG_LIST = "app/demand/tag";//tag列表

    //美摄素材包
    public static final String SDK_CATEGORY = "app/video/sdkcategory";
    public static final String SDK_LIST = "app/Material/getMaterialByCid";

    //h5界面
    public static final String DAILI_CHALLENGE = BASE_H5_URL + "personal/task_hall.html";
    public static final String MelExchange = BASE_H5_URL + "personal/incomeDetails.html?type=1";
    public static final String HOWTOEARN = BASE_H5_URL + "personal/howToEarn.html";
    public static final String CONTACT = BASE_H5_URL + "personal/contactUs.html";
    public static final String ENTER = BASE_H5_URL + "personal/enterCode.html";
    public static final String BASIC_REWARD = BASE_H5_URL + "makeMoney/basicReward.html";
    public static final String PERMANENT_COMMISSION = BASE_H5_URL + "makeMoney/permanentCommission.html";
    public static final String SHARE = BASE_H5_URL + "makeMoney/share.html";
    public static final String APPRENTICE_DISCIPLE = BASE_H5_URL + "makeMoney/apprenticeDisciple.html?type=1";
    public static final String APPRENTICE_DISCIPLE_TYPE = BASE_H5_URL + "makeMoney/apprenticeDisciple.html?type=2";


    //facebook
    public static final String DWON_FACEBOOK = "https://www.facebook.com/WatchnEarnOfficial";


    //v2.0接口
    public static final String ALIYUNPLAYURL = "app/video/aliyunUrl";
    public static final String VIDEOCOMMENTS = "api/video/list_comment/paginate";//请求视频评论列表
    public static final String VIDEOCOMENT = "api/video/push_comment";//视频发布评论
    public static final String VIDEOCOMMENTLIKE = "api/video/like_comment"; //评论点赞
    public static final String VIDEOLIKE = "app/video/like";//视频点赞
    public static final String VIDEOCOMMENTREPORT = "api/video/report_comment"; //评论举报

}
