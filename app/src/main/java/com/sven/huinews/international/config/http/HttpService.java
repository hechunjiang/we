package com.sven.huinews.international.config.http;


import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.BaseRequest;
import com.sven.huinews.international.entity.requst.BindEmailRequest;
import com.sven.huinews.international.entity.requst.FacebookRegRequest;
import com.sven.huinews.international.entity.requst.FansAndFollowRequest;
import com.sven.huinews.international.entity.requst.FeedBackRequest;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.entity.requst.FollowsRequest;
import com.sven.huinews.international.entity.requst.GetAliPlayUrlRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.GetboxtimeRequst;
import com.sven.huinews.international.entity.requst.InfoRequest;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.LinkedInRegRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.MusicListRequest;
import com.sven.huinews.international.entity.requst.MusicSearchRequest;
import com.sven.huinews.international.entity.requst.NewsListRequst;
import com.sven.huinews.international.entity.requst.NewsStatisticsRequest;
import com.sven.huinews.international.entity.requst.NvsResCategroyRequest;
import com.sven.huinews.international.entity.requst.NvsResListRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.requst.PushTokenRequest;
import com.sven.huinews.international.entity.requst.RegistRequst;
import com.sven.huinews.international.entity.requst.ResetPassRequst;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.SharedRequest;
import com.sven.huinews.international.entity.requst.TaskFinishRequest;
import com.sven.huinews.international.entity.requst.TaskListRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.TaskRequestAdVideo;
import com.sven.huinews.international.entity.requst.TempLoginRequest;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.ThridLoginRequest;
import com.sven.huinews.international.entity.requst.TipOffRequest;
import com.sven.huinews.international.entity.requst.TwitterRegRequest;
import com.sven.huinews.international.entity.requst.UserCommentRequest;
import com.sven.huinews.international.entity.requst.UserMsgRequest;
import com.sven.huinews.international.entity.requst.VideoCollectionCancelRequest;
import com.sven.huinews.international.entity.requst.VideoCollectionRequest;
import com.sven.huinews.international.entity.requst.VideoCommentLikeRequest;
import com.sven.huinews.international.entity.requst.VideoCommentRequest;
import com.sven.huinews.international.entity.requst.VideoDeleteRequest;
import com.sven.huinews.international.entity.requst.VideoLikeRequest;
import com.sven.huinews.international.entity.requst.VideoListRequest;
import com.sven.huinews.international.entity.requst.VideoPlayTimeSize;
import com.sven.huinews.international.entity.requst.VideoReportRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.entity.requst.VideoStatisticsRequest;
import com.sven.huinews.international.entity.requst.VideoUploadRequest;
import com.sven.huinews.international.utils.Common;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;
import wedemo.activity.data.PublishInfo;
import wedemo.activity.request.WcsRequest;

/**
 * http请求接口在此写
 */
public interface HttpService {


    //分享
    @POST(Api.INVITE_SHARE_DATAS)
    Observable<String> videoShareUrl(@HeaderMap HashMap<String, String> map, @Body SharedRequest request);

    //分享视频
    @POST(Api.VIDEO_SHARE_URL)
    Observable<String> videoShareUrl(@HeaderMap HashMap<String, String> map, @Body VideoShareUrlRequest request);

    //分享视频
    @POST(Api.VIDEO_DELETE)
    Observable<String> videoDelete(@HeaderMap HashMap<String, String> map, @Body VideoDeleteRequest request);

    /**
     * 临时登录
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.LOGIN_TEMP)
    Observable<String> onTempLogin(@HeaderMap HashMap<String, String> map, @Body TempLoginRequest request);


    @GET(Api.APP_CONFIG)
    Observable<String> appConfig(@HeaderMap HashMap<String, String> map, @Query("new_auth") int new_auth);

    /**
     * 登录
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.LOGIN)
    Observable<String> onLogin(@HeaderMap HashMap<String, String> map, @Body LoginRequest request);

    @POST(Api.CHECK_LOGIN)
    Observable<String> checkLogin(@HeaderMap HashMap<String, String> map, @Body PlatformLogin request);

    @POST(Api.CHECK_THIRDLOGIN)
    Observable<String> checkLogin(@HeaderMap HashMap<String, String> map, @Body ThirdRequest request);

    /**
     * 新闻统计
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.NEWS_STATISTICS)
    Observable<String> newsStatistics(@HeaderMap HashMap<String, String> map, @Body NewsStatisticsRequest request);


    @POST(Api.VIDEO_STATISTICS)
    Observable<String> videoStatistics(@HeaderMap HashMap<String, String> map, @Body VideoStatisticsRequest request);

    /**
     * 注册
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.SMS_CODE_MAIL)
    Observable<String> getSmsCode(@HeaderMap HashMap<String, String> map, @Body GetCodeRequest request);

    @POST(Api.BINDEMAIL)
    Observable<String> getBindSmsCode(@HeaderMap HashMap<String, String> map, @Body BindEmailRequest request);

    /**
     * 获取用户信息
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.USERINFO)
    Observable<String> userInfo(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST(Api.UPLOAD_FILE)
    Observable<String> uploadFile(@HeaderMap HashMap<String, String> map, @Part MultipartBody.Part file);

    /**
     * 上传个人信息
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.USER_MSG)
    Observable<String> setUserMsg(@HeaderMap HashMap<String, String> map, @Body UserMsgRequest request);

    @POST(Api.SHARE_VISIT)
    Observable<String> shareVisit(@HeaderMap HashMap<String, String> map, @Body ShareVisitRequest request);

    @POST(Api.SHARE_INFO)
    Observable<String> getShareInfo(@HeaderMap HashMap<String, String> map);


    //用户个人资料
    @POST(Api.OTHER_USER_DATA)
    Observable<String> getUserCommentList(@HeaderMap HashMap<String, String> map, @Body UserCommentRequest request);

    //用户个人资料
    @POST(Api.OTHER_USER_DATA)
    Observable<String> getUserCommentList(@HeaderMap HashMap<String, String> map);

    @POST(Api.APPRENTICEPAGEDATA)
    Observable<String> apprenticePageData(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    //用户作品
    @POST(Api.OTHER_WORK_LIST)
    Observable<String> getUserDetailsLikesList(@HeaderMap HashMap<String, String> map, @Body PersonWorkRequest request);

    //用户作品
    @POST(Api.OTHER_WORK_LIST)
    Observable<String> getUserDetailsLikesList(@HeaderMap HashMap<String, String> map);

    /**
     * 邮箱注册
     *
     * @param map
     * @param registerRequest
     * @return
     */

    @POST(Api.REGISTER_BY_EMAIL)
    Observable<String> onEmailRegister(@HeaderMap HashMap<String, String> map, @Body RegistRequst registerRequest);


    /**
     * Twitter FaceBook LinkedIn 注册
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.TWITTER_REG)
    Observable<String> twitterReg(@HeaderMap HashMap<String, String> map, @Body TwitterRegRequest request);

    @POST(Api.FACEBOOK_REG)
    Observable<String> facebookReg(@HeaderMap HashMap<String, String> map, @Body FacebookRegRequest request);

    @POST(Api.LINKEDIN_REG)
    Observable<String> linkedInReg(@HeaderMap HashMap<String, String> map, @Body LinkedInRegRequest request);

    @POST(Api.FIND_PASS_EMAIL)
    Observable<String> resetPss(@HeaderMap HashMap<String, String> map, @Body ResetPassRequst request);

    /**
     * 新闻列表
     */
    @POST(Api.NEWS_INFO_LISTS)
    Observable<String> getNewsInfoList(@HeaderMap HashMap<String, String> map, @Body NewsListRequst newsListRequst);


    /**
     * 视频点赞
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.VIDEOLIKE)
    Observable<String> onVideoLike(@HeaderMap HashMap<String, String> map, @Body VideoLikeRequest request);

    @POST(Api.ALIYUNPLAYURL)
    Observable<String> getVideoInfo(@HeaderMap HashMap<String, String> map, @Body GetAliPlayUrlRequest request);

    /**
     * 视频列表
     */

    @POST(Api.VIDEO_LISTS)
    Observable<String> getVideoList(@HeaderMap HashMap<String, String> map, @Body VideoListRequest request);

    @GET(Api.VIDEO_LISTS)
    Observable<String> getVideoList(@HeaderMap HashMap<String, String> map, @Query(Common.KEY_WORDS) String keyWords, @Query(Api.DEBUG) String debug, @Query(Api.PAGE) String page, @Query(Common.R_TYPE) String r_type, @Query("time") String time, @Query("nonce_str") String nonce_str, @Query("sign") String sign, @Query("new_auth") int new_auth);

    //垂直视频点赞
    @POST(Api.VIDEOLIKE)
    Observable<String> onVideoCollection(@HeaderMap HashMap<String, String> map, @Body VideoCollectionRequest request);

    //取消收藏
    @POST(Api.VIDEO_CANCEL_COLLECTION)
    Observable<String> onCancelCollection(@HeaderMap HashMap<String, String> map, @Body VideoCollectionCancelRequest request);

    //获取Follow列表
    @POST(Api.FOLLOWLISTS)
    Observable<String> getFollowList(@HeaderMap HashMap<String, String> map, @Body FollowRequest request);


    /**
     * 阿里云视频播放地址
     *
     * @param json
     * @return
     */

    @Headers("Content-Type:application/json")
    @POST(Api.ALIYUN_VIDEO_PALY)
    Observable<String> getVideoPlayInfo(@Body String json);

    /**
     * 获取视频评论
     *
     * @return
     */

    @POST(Api.VIDEOCOMMENTS)
    Observable<String> getVideoCommentList(@HeaderMap HashMap<String, String> map, @Body VideoCommentRequest requests);

    /**
     * 评论点赞
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.VIDEOCOMMENTLIKE)
    Observable<String> onVideoCommentLike(@HeaderMap HashMap<String, String> map, @Body VideoCommentLikeRequest request);

    /**
     * 关注
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.FOLLOW_LIST)
    Observable<String> onFollowList(@HeaderMap HashMap<String, String> map, @Body FansAndFollowRequest request);

    /**
     * 举报
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.VIDEO_REPORT)
    Observable<String> videoReport(@HeaderMap HashMap<String, String> map, @Body VideoReportRequest request);


    /**
     * 粉丝列表
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.FANS_LIST)
    Observable<String> onFansList(@HeaderMap HashMap<String, String> map, @Body FansAndFollowRequest request);

    /**
     * 举报
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.VIDEOCOMMENTREPORT)
    Observable<String> onVideoCommentReport(@HeaderMap HashMap<String, String> map, @Body TipOffRequest request);


    /**
     * 活动
     *
     * @param map
     * @return
     */
    @GET(Api.TASK_PUSH)
    Observable<String> getTaskPush(@HeaderMap HashMap<String, String> map, @Query("time") String time, @Query("nonce_str") String nonce_str, @Query("sign") String sign, @Query("new_auth") int new_auth);

    /**
     * 增加视频评论
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.VIDEOCOMENT)
    Observable<String> VIDEOCOMENT(@HeaderMap HashMap<String, String> map, @Body AdCommentRequest request);

    /**
     * 音乐分类
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.MUSIC_CATEGROY)
    Observable<String> onMusicCategroy(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    /**
     * 音乐列表
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.MUSIC_LIST)
    Observable<String> onMusicList(@HeaderMap HashMap<String, String> map, @Body MusicListRequest request);

    /**
     * 音乐列表
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.MUSIC_SEARCH)
    Observable<String> onMusicSearchList(@HeaderMap HashMap<String, String> map, @Body MusicSearchRequest request);

    /**
     * 阿里云临时上传token
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.ALI_UOLOAD_TOKEN)
    Observable<String> onAliyunUploadToken(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    @POST(Api.WCS_UOLOAD_TOKEN)
    Observable<String> onWcsUploadToken(@HeaderMap HashMap<String, String> map, @Body WcsRequest request);

    /**
     * 上传视频
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.UOLOAD_VIDEO)
    Observable<String> onUploadVideo(@HeaderMap HashMap<String, String> map, @Body VideoUploadRequest request);

    @POST(Api.UOLOAD_VIDEO_WCS)
    Observable<String> onUploadVideoWcs(@HeaderMap HashMap<String, String> map, @Body VideoUploadRequest request);

    /**
     * tag列表
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.TAG_LIST)
    Observable<String> onTagList(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    @Streaming
    @POST
    Observable<String> downloadFile(@Url String fileUrl);

    @POST(Api.NVS_RES_CATRGROY)
    Observable<String> onNvsResCategroy(@HeaderMap HashMap<String, String> map, @Body NvsResCategroyRequest request);

    @POST(Api.NVS_RES_LIST)
    Observable<String> onNvsResList(@HeaderMap HashMap<String, String> map, @Body NvsResListRequest request);

    //用户喜欢的
    @POST(Api.User_Likes_List)
    Observable<String> getLikesList(@HeaderMap HashMap<String, String> map, @Body LikesRequest request);

    //个人喜欢的
    @POST(Api.User_Likes_List)
    Observable<String> getLikesList(@HeaderMap HashMap<String, String> map);

    //关注
    @POST(Api.USER_FOLLOW)
    Observable<String> onFollow(@HeaderMap HashMap<String, String> map, @Body FollowsRequest request);

    //金币视频
    @POST(Api.READ_NEWS_GET_GOLD)
    Observable<String> getGoldByTask(@HeaderMap HashMap<String, String> map, @Body TaskRequest request);

    //金币时间
    @POST(Api.GET_GOLDBOX_TIME)
    Observable<String> getGoldboxTime(@HeaderMap HashMap<String, String> map, @Body GetboxtimeRequst requst);

    @GET(Api.RED_BAG)
    Observable<String> getRedBag(@HeaderMap HashMap<String, String> map, @Query("time") String time, @Query("nonce_str") String nonce_str, @Query("sign") String sign, @Query("new_auth") int new_auth);

    @POST(Api.MESSAGE_LIST)
    Observable<String> onMessageList(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    @POST(Api.PUSH_TOKEN)
    Observable<String> onPushToken(@HeaderMap HashMap<String, String> map, @Body PushTokenRequest request);

    @POST(Api.READ_NEWS_GET_GOLD)
    Observable<String> getAdVideoByTask(@HeaderMap HashMap<String, String> map, @Body TaskRequestAdVideo request);

    @Streaming
    @GET
    Observable<ResponseBody> downloadApk(@Url String url);

    @POST(Api.ADD_INTRODUCE_VIDEO_COID)
    Observable<String> addIntroduceVideoCoid(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    @POST(Api.FEEDBACK_CLASSIFY)
    Observable<String> feedbackClassify(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    @POST(Api.FEEDBACK_SEND)
    Observable<String> feedbackSend(@HeaderMap HashMap<String, String> map, @Body FeedBackRequest request);

    @POST(Api.ADD_VIDEO_VIDEOPLAY)
    Observable<String> videoPlayTime(@HeaderMap HashMap<String, String> map, @Body VideoPlayTimeSize request);

    @POST(Api.TASK_LIST)
    Observable<String> getTaskList(@HeaderMap HashMap<String, String> map, @Body TaskListRequest request);

    @POST(Api.TASK_LIST_NEW)
    Observable<String> getTaskListNew(@HeaderMap HashMap<String, String> map, @Body TaskListRequest request);

    @POST(Api.TASK_FINISH)
    Observable<String> taskFinish(@HeaderMap HashMap<String, String> map, @Body TaskFinishRequest request);


    @POST(Api.TASK_GET_INFO)
    Observable<String> getTaskInfo(@HeaderMap HashMap<String, String> map, @Body InfoRequest request);

    @POST(Api.TASK_THRID)
    Observable<String> getGold(@HeaderMap HashMap<String, String> map, @Body ThridLoginRequest request);
}
