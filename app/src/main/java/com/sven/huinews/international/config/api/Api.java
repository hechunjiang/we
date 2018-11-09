package com.sven.huinews.international.config.api;

import com.sven.huinews.international.BuildConfig;

public class Api {

    //h5
    public static final String H5_INCOME_TYPE_1 = BuildConfig.BASE_WEB_URL + "personal/incomeDetails.html?type=1";
    public static final String H5_INCOME_TYPE_2 = BuildConfig.BASE_WEB_URL + "personal/incomeDetails.html?type=2";

    public static final int API_CODE_GOLD_FAILD = 10002;
    public static final int API_CODE_SUCCEED = 200;
    //config
    public static final String HEADER_OS = "os";
    public static final String SEX = "sex";
    public static final String DEBUG = "debug";
    public static final String LOGIN_SOURCE = "login_source";
    public static final String F_CODE = "f_code";
    public static final String TASK = "task";

    private static final String USER_ID = "user_id";

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
    public static final String HEAD_IMG = "headimg";
    public static final String NICK_NAME = "nickname";
    public static final String FACEBOOK_TOKEN = "fb_access_token";
    public static final String PHONE = "phone";
    public static final String PASS = "pass";
    public static final String PAGE = "page";
    public static final String PAGESIZE = "pagesize";
    public static final String PAGE_SIZE = "page_size";
    public static final String TOKEN = "token";
    public static final String R_TYPE = "r_type";
    public static final String TYPE = "type";
    public static final String R_ID = "rId";
    public static final String DEL_TYPE = "delType";
    public static final String VIDEO_ID = "video_id";
    public static final String VID = "vid";
    public static final String TO_PLATFROM = "to_platfrom";
    public static final String CID = "cid";

    public static final String ID = "id";
    public static final String key_code = "key_code";
    public static final String NEWS_ID = "newsId";
    public static final String NEWSID = "news_Id";
    public static final String CONTENT = "content";
    public static final String ORDER = "order";
    public static final String SIZE = "size";
    public static final String COMMENT_ID = "comment_id";
    public static final String F_INVIT_CODE = "f_invit_code";
    public static final String HEADIMAGE = "headImg";
    public static final String BIRTHDAY = "birthday";
    public static final String MARKID = "markId";
    public static final String NICKNAME = "nickname";
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
    public static final String GOOGLE_ID = "gg_id";
    public static final String KEYWORDS = "keywords";

    public static final String VIDEO_URL = " videoUrl";
    public static final String VIDEOID = " videoId";
    public static final String TITLE = "title";
    public static final String COVER_IMG = "coverImg";
    public static final String DURATION = "duration";
    public static final String HEIGHT = "height";
    public static final String WIDTH = "width";
    public static final String TAG = "tag";
    public static final String COUNTRY = "country";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String CATEGROY = "categroy";
    public static final String OTHER_ID = "other_id";
    public static final String DU_TYPE = "du_type";
    public static final String V_TD = "v_id";
    public static final String SIGNATURE = "signature";//tag列表
    public static final String DEFAULT_IMG = BuildConfig.BASE_WEB_URL + "/static/img/default_head.png"; //默认头像地址  适用于twitter
    public static final String WCSID = "wcsid";

    public static final String FOLLOW_ID = "follower_id";
    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String BASE_H5_URL = BuildConfig.BASE_WEB_URL;
    public static final String WCS_UPDATELOAD_URL = "http://hesheng138.up19.v1.wcsapi.com";

    //数据接口
    public static final String APP_CONFIG = "app/Setting/config";//手机配置信息
    public static final String LOGIN = "app/Login/index";//登录接口
    public static final String CHECK_LOGIN = "app/reg/check_login";//checklogin
    public static final String CHECK_THIRDLOGIN = "app/Login/thirdPartyLogin";
    public static final String LOGIN_TEMP = "app/Reg/f_reg";//临时登录
    public static final String SMS_CODE_MAIL = "app/pub/mailSendCode";//获取注册验证码
    public static final String REGISTER_BY_EMAIL = "app/Reg/m_reg";//邮箱注册
    public static final String FIND_PASS_EMAIL = "app/reg/m_findpass";//重设密码
    public static final String NEWS_INFO_LISTS = "app/news/todayNews"; //新闻列表
    public static final String VIDEO_LIKE = "app/video/like";//视频点赞
    public static final String BINDEMAIL = "app/Task/bindMail";

    public static final String SHARE_VISIT = "app/Datapre/shareVisit";//分享计数
    public static final String SHARE_INFO = "app/Activateshare/share";//获取分享信息

    public static final String VIDEO_LISTS = "app/video/lists";//视频列表
    public static final String VIDEO_COLLECTION = "Collection/cResource";//视频收藏   未验证
    public static final String VIDEO_CANCEL_COLLECTION = "Collection/delCollect";//取消收藏  未验证
    public static final String FOLLOWLISTS = "app/follow/followVideo";//Follow列表
    public static final String ALIYUN_VIDEO_PALY = "user/aliyun/getVideoPlayInfo"; //阿里云视频播放地址获取


    public static final String MUSIC_CATEGROY = "app/music_type/getMusicTypeList";//音乐分类
    public static final String MUSIC_LIST = "app/Music/getMusicListByCid";//音乐列表
    public static final String MUSIC_SEARCH = "app/Music/searchMusic";//搜索音乐
    public static final String ALI_UOLOAD_TOKEN = "app/demand/getUserToken";//阿里云临时上传token
    public static final String UOLOAD_VIDEO = "app/demand/updateVideoId";//上传视频信息
    public static final String TAG_LIST = "app/demand/tag";//tag列表SIGNATURE
    public static final String WCS_UOLOAD_TOKEN = "app/demand/getWsToken";//网宿上传token
    public static final String UOLOAD_VIDEO_WCS = "app/demand/updateWsVideo";//网宿上传视频信息


    //h5界面
    public static final String DAILI_CHALLENGE = BASE_H5_URL + "personal/task_hall.html";
    public static final String MelExchange = BASE_H5_URL + "personal/incomeDetails.html?type=1";
    public static final String MelRankingList = BASE_H5_URL + "personal/billboard.html";//排行榜H5页面
    public static final String HOWTOEARN = BASE_H5_URL + "personal/howToEarn.html";
    public static final String CONTACT = BASE_H5_URL + "personal/contactUs.html";
    public static final String ENTER = BASE_H5_URL + "personal/enterCode.html";
    public static final String BASIC_REWARD = BASE_H5_URL + "makeMoney/basicReward.html";
    public static final String PERMANENT_COMMISSION = BASE_H5_URL + "makeMoney/permanentCommission.html";
    public static final String SHARE = BASE_H5_URL + "makeMoney/share.html";
    public static final String APPRENTICE_DISCIPLE = BASE_H5_URL + "makeMoney/apprenticeDisciple.html?type=1";
    public static final String APPRENTICE_DISCIPLE_TYPE = BASE_H5_URL + "makeMoney/apprenticeDisciple.html?type=2";
    public static final String APPRENTICE_COINS = BASE_H5_URL + "personal/incomeDetails.html?type=1";
    public static final String APPRENTICE_BALANCE = BASE_H5_URL + "personal/incomeDetails.html?type=2";


    //facebook
    public static final String DWON_FACEBOOK = "https://www.facebook.com/WatchnEarnOfficial";


    //v2.0接口
    public static final String ALIYUNPLAYURL = "app/video/aliyunUrl";//阿里云地址
    public static final String VIDEOCOMMENTS = "app/video_comment/getList";//请求视频评论列表
    public static final String VIDEOCOMENT = "app/video_comment/push";//视频发布评论
    public static final String VIDEOCOMMENTLIKE = "app/video_comment/CommentLike"; //评论点赞
    public static final String VIDEOLIKE = "app/video/like";//视频点赞
    public static final String VIDEOCOMMENTREPORT = "api/video/report_comment"; //评论举报
    public static final String TASK_PUSH = "app/Activate/push"; //活动

    public static final String FOLLOW_LIST = "app/work/followList"; //关注
    public static final String FANS_LIST = "app/work/fansList"; //关注


    public static final String OTHER_USER_DATA = "app/user/workInfo";//用户资料
    public static final String OTHER_WORK_LIST = "app/work/workList";//用户Videos
    public static final String User_Likes_List = "app/User/getMyTagsVideo";//用户likes
    public static final String USER_FOLLOW = "app/Follow/followUser";//关注
    public static final String USERINFO = "app/User/index";//获取我的用户信息
    public static final String UPLOAD_FILE = "app/pub/imageFormUpload";//上传头像
    public static final String USER_MSG = "app/user/setUserMsg";//上传个人信息
    public static final String INVITE_SHARE_DATAS = "app/Activateshare/shareWithInvitationCode";//分享
    public static final String VIDEO_SHARE_URL = "app/Activateshare/videoShare";//分享视频
    public static final String VIDEO_DELETE = "app/Work/delVideo";//删除视频
    public static final String APPRENTICEPAGEDATA = "app/Apprentice/apprenticePageData";//获取任务大厅信息

    public static final String READ_NEWS_GET_GOLD = "app/mission_new/handler";//金币
    public static final String GET_GOLDBOX_TIME = "app/Task/treasureStatus";//获取金币时间
    public static final String VIDEO_REPORT = "app/Report/video";//举报视频
    public static final String NEWS_STATISTICS = "app/datapre/newsVisit";
    public static final String VIDEO_STATISTICS = "app/datapre/videoVisit";
    public static final String RED_BAG = "app/Redbag/getOneRed";
    public static final String TASK_LIST = "app/task/index"; //任务大厅
    public static final String TASK_LIST_NEW = "app/task/lists"; //任务大厅
    public static final String TASK_FINISH = "app/mission_new/handler"; //任务结束，领取金币

    public static final String TASK_GET_INFO = "app/pub/withdrawlist"; //收益列表

    public static final String TASK_THRID = "app/Login/thirdPartyTask"; // 三方登陆成功

    public static final String TWITTER_REG = "app/reg/twi_reg";
    public static final String FACEBOOK_REG = "app/reg/fb_reg";
    public static final String LINKEDIN_REG = "app/reg/lk_reg";
    //素材
    public static final String NVS_RES_CATRGROY = "app/video/sdkcategory"; //素材分类
    public static final String NVS_RES_LIST = "app/video/sdklist";//素材列表
    public static final String MESSAGE_LIST = "app/Announce/getAnnounceList"; //公告
    public static final String BASE_WEB_URL = BuildConfig.BASE_WEB_URL;
    public static final String PUSH_TOKEN = "app/user/setFirebase";//firebase 推送注册


    public static final String WEB_HOW_TO_EARN = BuildConfig.BASE_WEB_URL + "personal/howToEarn.html";//如何赚钱（规则）

    public static final String ADD_INTRODUCE_VIDEO_COID = "app/mission_new/handler?id=13";

    //意见反馈
    public static final String FEEDBACK_CLASSIFY = "app/Feedback/getFbTypeList";
    public static final String FEEDBACK_SEND = "app/Feedback/send";

    public static final String ADD_VIDEO_VIDEOPLAY = "app/video/videoPlay";
}
