package com.sven.huinews.international.config.http;

import android.text.TextUtils;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.entity.requst.AdCommentRequest;
import com.sven.huinews.international.entity.requst.BindEmailRequest;
import com.sven.huinews.international.entity.requst.DisLikeVideoRequest;
import com.sven.huinews.international.entity.requst.FacebookRegRequest;
import com.sven.huinews.international.entity.requst.FansAndFollowRequest;
import com.sven.huinews.international.entity.requst.FeedBackRequest;
import com.sven.huinews.international.entity.requst.FollowListRequest;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.entity.requst.FollowsRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.GetboxtimeRequst;
import com.sven.huinews.international.entity.requst.LinkedInRegRequest;
import com.sven.huinews.international.entity.requst.MusicListRequest;
import com.sven.huinews.international.entity.requst.MusicSearchRequest;
import com.sven.huinews.international.entity.requst.NewsListRequst;

import com.sven.huinews.international.entity.requst.NewsStatisticsRequest;
import com.sven.huinews.international.entity.requst.NvsResCategroyRequest;
import com.sven.huinews.international.entity.requst.NvsResListRequest;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.PushTokenRequest;
import com.sven.huinews.international.entity.requst.ShareVisitRequest;
import com.sven.huinews.international.entity.requst.TaskFinishRequest;
import com.sven.huinews.international.entity.requst.TaskListRequest;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.requst.TaskRequestAdVideo;
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
import com.sven.huinews.international.entity.requst.VideoPlayUrlRequest;
import com.sven.huinews.international.entity.requst.VideoStatisticsRequest;
import com.sven.huinews.international.entity.requst.VideoStayRequest;
import com.sven.huinews.international.entity.requst.VideoUploadRequest;

import com.sven.huinews.international.entity.requst.VideoReportRequest;
import com.sven.huinews.international.entity.requst.VideoShareUrlRequest;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.entity.requst.RegistRequst;
import com.sven.huinews.international.entity.requst.ResetPassRequst;

import org.w3c.dom.Text;

import wedemo.activity.data.PublishInfo;
import wedemo.activity.request.WcsRequest;


public class SignJson {

    /**
     * 临时登录
     *
     * @param mobile_brand
     * @return
     */
    public static String singOnTempLogin(String mobile_brand) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(mobile_brand)) {
            parmsUtils.getPostBody(Api.MOBILE_BRAND, mobile_brand);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 登录
     *
     * @param phone
     * @param pass
     * @return
     */
    public static String singOnLogin(String phone, String pass) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(phone)) {
            parmsUtils.getPostBody(Api.MAIL, phone);
        }
        if (!TextUtils.isEmpty(pass)) {
            parmsUtils.getPostBody(Api.PASS, pass);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);

        return json;
    }


    /**
     * 新闻点击统计
     *
     * @param request
     * @return
     */
    public static String signNewsStatistics(NewsStatisticsRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getNewsId())) {
            parmsUtils.getPostBody(Api.NEWSID, request.getNewsId());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 视频播放统计
     *
     * @param request
     * @return
     */
    public static String signVideoStatistics(VideoStatisticsRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getVideoId())) {
            parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideoId());
        }
        if (!TextUtils.isEmpty(request.getR_type())) {
            parmsUtils.getPostBody(Api.R_TYPE, request.getR_type());
        }
        if (!TextUtils.isEmpty(request.getDu_type())) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     *
     */

    public static String signAliVideoid(String VideoId) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(VideoId)) {
            parmsUtils.getPostBody(Api.ALVIDEO_ID, VideoId);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    public static String checkIsLogin(String platformType, String platformId) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(platformType)) {
            parmsUtils.getPostBody(platformType, platformId);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    public static String checkIsLogin(ThirdRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getMobile_brand())) {
            parmsUtils.getPostBody(Api.MOBILE_BRAND, request.getMobile_brand());
        }
        if (!TextUtils.isEmpty(request.getHeadimg())) {
            parmsUtils.getPostBody(Api.HEAD_IMG, request.getHeadimg());
        }
        if (!TextUtils.isEmpty(request.getNickname())) {
            parmsUtils.getPostBody(Api.NICK_NAME, request.getNickname());
        }
        if (!TextUtils.isEmpty(request.getSex())) {
            parmsUtils.getPostBody(Api.SEX, request.getSex());
        }
        if (!TextUtils.isEmpty(request.getLogin_source())) {
            parmsUtils.getPostBody(Api.LOGIN_SOURCE, request.getLogin_source());
        }
        if (!TextUtils.isEmpty(request.getTask())) {
            parmsUtils.getPostBody(Api.TASK, request.getTask());
        }

        if (request.getLogin_source().equals(Constant.FACEBOOK)) {

            if (!TextUtils.isEmpty(request.getFb_id())) {
                parmsUtils.getPostBody(Api.FACEBOOK, request.getFb_id());
            }
            if (!TextUtils.isEmpty(request.getFb_access_token())) {
                parmsUtils.getPostBody(Api.FACEBOOK_TOKEN, request.getFb_access_token());
            }

        } else if (request.getLogin_source().equals(Constant.TWITTER)) {

            if (!TextUtils.isEmpty(request.getTwitter_id())) {
                parmsUtils.getPostBody(Api.TIWTTER_ID, request.getTwitter_id());
            }


        } else if (request.getLogin_source().equals(Constant.LINKEDIN)) {
            if (!TextUtils.isEmpty(request.getLk_id())) {
                parmsUtils.getPostBody(Api.LINKEDIN_ID, request.getLk_id());
            }

        } else if (request.getLogin_source().equals(Constant.GOOGLELOGIN)) {
            if (!TextUtils.isEmpty(request.getGm_id())) {
                parmsUtils.getPostBody(Api.GOOGLE_ID, request.getGm_id());
            }
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 视频列表
     *
     * @return
     */
    public static String singVideoList(VideoListRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getPage() + "")) {
            parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        }
        if (!TextUtils.isEmpty(request.getR_type() + "")) {
            parmsUtils.getPostBody(Api.R_TYPE, request.getR_type() + "");
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 新闻列表
     *
     * @return
     */
    public static String singNewsList(String r_type, String page) {
        ParmsUtils parmsUtils = new ParmsUtils();

        if (!TextUtils.isEmpty(page)) {
            parmsUtils.getPostBody(Api.PAGE, page);
        }
        if (!TextUtils.isEmpty(r_type)) {
            parmsUtils.getPostBody(Api.R_TYPE, r_type);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;

    }

    /**
     * 视频列表
     *
     * @param limit
     * @param page
     * @return
     */
    public static String singVideoList(String limit, String r_type, String page) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(limit)) {
            parmsUtils.getPostBody(Common.LIMIT, limit + "");
        }
        if (!TextUtils.isEmpty(Api.PAGE)) {
            parmsUtils.getPostBody(Api.PAGE, page);
        }
        if (TextUtils.isEmpty(Api.R_TYPE)) {
            parmsUtils.getPostBody(Api.R_TYPE, r_type);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 视频分享
     *
     * @param request
     * @return
     */
    public static String signVideoShareUrl(VideoShareUrlRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getVideoId())) {
            parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideoId());
            parmsUtils.getPostBody(Api.TO_PLATFROM, request.getType());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 视频删除
     *
     * @param request
     * @return
     */
    public static String signVideoDelete(VideoDeleteRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getVideoId())) {
            parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideoId());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 视频收藏or小视频收藏
     *
     * @param request
     * @return
     */
    public static String signVideoCollection(VideoCollectionRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");
        parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideo_id() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 取消收藏
     *
     * @param request
     * @return
     */
    public static String signCollectionCancel(VideoCollectionCancelRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.TYPE, request.getType() + "");
        parmsUtils.getPostBody(Api.R_ID, request.getrId() + "");
        parmsUtils.getPostBody(Api.DEL_TYPE, request.getDelType() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 金币任务
     *
     * @param request
     * @return
     */
    public static String signTaskGold(TaskRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getId())) {
            parmsUtils.getPostBody(Api.ID, request.getId() + "");
        }
        if (!TextUtils.isEmpty(request.getF_code())) {
            parmsUtils.getPostBody(Api.F_CODE, request.getF_code());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * @param request
     * @return
     */
    public static String signGoldTime(GetboxtimeRequst request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getKey_code())) {
            parmsUtils.getPostBody(Api.key_code, request.getKey_code() + "");
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 视频点赞
     *
     * @param request
     * @return
     */
    public static String signVideoLike(VideoLikeRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getDu_type())) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");
        }
        if (!TextUtils.isEmpty(request.getVideo_id())) {
            parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideo_id() + "");
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * Follow视频列表
     */


    public static String signFollowList(FollowRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getPage() + "")) {
            parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        }
        if (!TextUtils.isEmpty(request.getPagesize() + "")) {
            parmsUtils.getPostBody(Api.PAGESIZE, request.getPagesize() + "");
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(request);
        return json;
    }


    public static String signFollowPlayUrlList(String aliyunVideoId) {
        VideoPlayUrlRequest request = new VideoPlayUrlRequest(aliyunVideoId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(request);
        return json;
    }

    /**
     * 视频评论列表
     *
     * @param request
     * @return
     */
    public static String signVideoCommentList(VideoCommentRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();

        if (!TextUtils.isEmpty(request.getPage() + "")) {
            parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        }
        if (!TextUtils.isEmpty(request.getId())) {
            parmsUtils.getPostBody(Api.ID, request.getId());
        }
        if (!TextUtils.isEmpty(request.getOrder())) {
            parmsUtils.getPostBody(Api.ORDER, request.getOrder());
        }

        if (!TextUtils.isEmpty(request.getSize())) {
            parmsUtils.getPostBody(Api.SIZE, request.getSize());
        }
        if (!TextUtils.isEmpty(request.getDu_type())) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type());
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 评论点赞
     *
     * @param request
     * @return
     */
    public static String signVideoCommentLike(VideoCommentLikeRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getCid() + "")) {
            parmsUtils.getPostBody(Api.CID, request.getCid() + "");
        }
        if (!TextUtils.isEmpty(request.getDu_type() + "")) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");
        }
        if (!TextUtils.isEmpty(request.getVideo_id() + "")) {
            parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideo_id() + "");
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 关注列表 粉丝列表
     *
     * @param request
     * @return
     */
    public static String signFollowAndFans(FansAndFollowRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getOther_id() + "")) {
            parmsUtils.getPostBody(Api.OTHER_ID, request.getOther_id() + "");
        }
        if (!TextUtils.isEmpty(request.getDu_type() + "")) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");
        }
        if (!TextUtils.isEmpty(request.getPage() + "")) {
            parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        }
        if (!TextUtils.isEmpty(request.getPage_size() + "")) {
            parmsUtils.getPostBody(Api.PAGE_SIZE, request.getPage_size() + "");
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 添加评论
     *
     * @param request
     * @return
     */
    public static String signVideoAdComment(AdCommentRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getContent())) {
            parmsUtils.getPostBody(Api.CONTENT, request.getContent());
        }
        if (!TextUtils.isEmpty(request.getV_id())) {
            parmsUtils.getPostBody(Api.V_TD, request.getV_id() + "");
        }
        if (!TextUtils.isEmpty(request.getDu_type())) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type());
        }
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 举报
     *
     * @param request
     * @return
     */
    public static String signVideoReport(TipOffRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getVideo_id())) {
            parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideo_id());
        }
        if (!TextUtils.isEmpty(request.getCid())) {
            parmsUtils.getPostBody(Api.CID, request.getCid());
        }
        if (!TextUtils.isEmpty(request.getDu_type())) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 举报
     *
     * @param request
     * @return
     */
    public static String signVideoReport(VideoReportRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getVideoId())) {
            parmsUtils.getPostBody(Api.VIDEOID, request.getVideoId());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 短信验证码
     *
     * @param request
     * @return
     */
    public static String signGetSmsCode(GetCodeRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getMail())) {
            parmsUtils.getPostBody(Api.MAIL, request.getMail());
        }
        if (!TextUtils.isEmpty(request.getType())) {
            parmsUtils.getPostBody(Api.TYPE, request.getType());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 绑定邮箱
     *
     * @param request
     * @return
     */
    public static String signBindEmail(BindEmailRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getMail())) {
            parmsUtils.getPostBody(Api.MAIL, request.getMail());
        }
        if (!TextUtils.isEmpty(request.getVerify())) {
            parmsUtils.getPostBody(Api.VERIFY, request.getVerify());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 注册
     *
     * @param request
     * @return
     */
    public static String singRegister(RegistRequst request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getMail())) {
            parmsUtils.getPostBody(Api.MAIL, request.getMail());
        }
        if (!TextUtils.isEmpty(request.getPass())) {
            parmsUtils.getPostBody(Api.PASS, request.getPass());
        }
        if (!TextUtils.isEmpty(request.getInvitCode())) {
            parmsUtils.getPostBody(Api.F_INVIT_CODE, request.getInvitCode());
        }
        if (!TextUtils.isEmpty(request.getVerify())) {
            parmsUtils.getPostBody(Api.VERIFY, request.getVerify());
        }
        if (!TextUtils.isEmpty(request.getMobile_brand())) {
            parmsUtils.getPostBody(Api.MOBILE_BRAND, request.getMobile_brand());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * tiwtter 注册
     *
     * @param request
     * @return
     */
    public static String twitterReg(TwitterRegRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getMail())) {
            parmsUtils.getPostBody(Api.MAIL, request.getMail());
        }
        if (!TextUtils.isEmpty(request.getPass())) {
            parmsUtils.getPostBody(Api.PASS, request.getPass());
        }
        if (!TextUtils.isEmpty(request.getHeadIcon())) {
            parmsUtils.getPostBody(Api.HEAD_IMG, request.getHeadIcon());
        }
        //if (!TextUtils.isEmpty(request.getInvitCode())) {
        parmsUtils.getPostBody(Api.F_INVIT_CODE, request.getInvitCode());
        //}
        if (!TextUtils.isEmpty(request.getRegisterCode())) {
            parmsUtils.getPostBody(Api.VERIFY, request.getRegisterCode());
        }
        if (!TextUtils.isEmpty(request.getMobileBrand())) {
            parmsUtils.getPostBody(Api.MOBILE_BRAND, request.getMobileBrand());
        }
        if (!TextUtils.isEmpty(request.getTwitterId())) {
            parmsUtils.getPostBody(Api.TIWTTER_ID, request.getTwitterId());
        }
        if (!TextUtils.isEmpty(request.getNickName())) {
            parmsUtils.getPostBody(Api.NICK_NAME, request.getNickName());
        }
        if (!TextUtils.isEmpty(request.getSex())) {
            parmsUtils.getPostBody(Api.SEX, request.getSex());
        }


        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * facebook reg
     *
     * @param request
     * @return
     */
    public static String facebookReg(FacebookRegRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getMail())) {
            parmsUtils.getPostBody(Api.MAIL, request.getMail());
        }
        if (!TextUtils.isEmpty(request.getPass())) {
            parmsUtils.getPostBody(Api.PASS, request.getPass());
        }
        if (!TextUtils.isEmpty(request.getHeadIcon())) {
            parmsUtils.getPostBody(Api.HEAD_IMG, request.getHeadIcon());
        }
        // if (!TextUtils.isEmpty(request.getInvitCode())) {
        parmsUtils.getPostBody(Api.F_INVIT_CODE, request.getInvitCode());
        // }
        if (!TextUtils.isEmpty(request.getRegisterCode())) {
            parmsUtils.getPostBody(Api.VERIFY, request.getRegisterCode());
        }
        if (!TextUtils.isEmpty(request.getMobileBrand())) {
            parmsUtils.getPostBody(Api.MOBILE_BRAND, request.getMobileBrand());
        }
        if (!TextUtils.isEmpty(request.getFbId())) {
            parmsUtils.getPostBody(Api.FACEBOOK, request.getFbId());
        }
        if (!TextUtils.isEmpty(request.getFb_access_token())) {
            parmsUtils.getPostBody(Api.FACEBOOK_TOKEN, request.getFb_access_token());
        }
        if (!TextUtils.isEmpty(request.getNickName())) {
            parmsUtils.getPostBody(Api.NICK_NAME, request.getNickName());
        }
        if (!TextUtils.isEmpty(request.getSex())) {
            parmsUtils.getPostBody(Api.SEX, request.getSex());
        }


        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();//给url去除转义
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * LinkedIn
     *
     * @param request
     * @return
     */

    public static String linkedInReg(LinkedInRegRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getMail())) {
            parmsUtils.getPostBody(Api.MAIL, request.getMail());
        }
        if (!TextUtils.isEmpty(request.getPass())) {
            parmsUtils.getPostBody(Api.PASS, request.getPass());
        }
        if (!TextUtils.isEmpty(request.getHeadIcon())) {
            parmsUtils.getPostBody(Api.HEAD_IMG, request.getHeadIcon());
        } else {
            parmsUtils.getPostBody(Api.HEAD_IMG, Api.DEFAULT_IMG);
        }
        //     if (!TextUtils.isEmpty(request.getInvitCode())) {
        parmsUtils.getPostBody(Api.F_INVIT_CODE, request.getInvitCode());
        //  }
        if (!TextUtils.isEmpty(request.getRegisterCode())) {
            parmsUtils.getPostBody(Api.VERIFY, request.getRegisterCode());
        }
        if (!TextUtils.isEmpty(request.getMobileBrand())) {
            parmsUtils.getPostBody(Api.MOBILE_BRAND, request.getMobileBrand());
        }
        if (!TextUtils.isEmpty(request.getLk_id())) {
            parmsUtils.getPostBody(Api.LINKEDIN_ID, request.getLk_id());
        }
        if (!TextUtils.isEmpty(request.getNickName())) {
            parmsUtils.getPostBody(Api.NICK_NAME, request.getNickName());
        }
        if (!TextUtils.isEmpty(request.getSex())) {
            parmsUtils.getPostBody(Api.SEX, request.getSex());
        }


        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 找回密码
     *
     * @param request
     * @return
     */
    public static String signFindPass(ResetPassRequst request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getPhone())) {
            parmsUtils.getPostBody(Api.MAIL, request.getPhone());
        }
        if (!TextUtils.isEmpty(request.getPass())) {
            parmsUtils.getPostBody(Api.PASS, request.getPass());
        }
        if (!TextUtils.isEmpty(request.getCode())) {
            parmsUtils.getPostBody(Api.VERIFY, request.getCode());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 上传个人信息
     *
     * @param request
     * @return
     */

    public static String signUserMsg(UserMsgRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();

        if (!TextUtils.isEmpty(request.getBirthday())) {
            parmsUtils.getPostBody(Api.BIRTHDAY, request.getMarkId());
        }
        if (!TextUtils.isEmpty(request.getHeadImg())) {
            parmsUtils.getPostBody(Api.HEADIMAGE, request.getHeadImg());
        }
        if (!TextUtils.isEmpty(request.getMarkId())) {
            parmsUtils.getPostBody(Api.MARKID, request.getMarkId());
        }
        if (!TextUtils.isEmpty(request.getNickname())) {
            parmsUtils.getPostBody(Api.NICKNAME, request.getNickname());
        }
        if (!TextUtils.isEmpty(request.getSex())) {
            parmsUtils.getPostBody(Api.SEX, request.getSex());
        }
        if (!TextUtils.isEmpty(request.getSignature())) {
            parmsUtils.getPostBody(Api.SIGNATURE, request.getSignature());
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 分享计数签名
     *
     * @param request
     * @return
     */
    public static String signShareVisit(ShareVisitRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getActivityType())) {
            parmsUtils.getPostBody(Api.ACTIVITY_TYPE, request.getActivityType());
        }
        if (!TextUtils.isEmpty(request.getCode())) {
            parmsUtils.getPostBody(Api.CODE, request.getCode());
        }
        if (!TextUtils.isEmpty(request.getShareChannel())) {
            parmsUtils.getPostBody(Api.SHARE_CHANNEL, request.getShareChannel());
        }
        if (!TextUtils.isEmpty(request.getVideoId())) {
            parmsUtils.getPostBody(Api.VID, request.getVideoId());
        }
        if (!TextUtils.isEmpty(request.getDuType())) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDuType());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 音乐列表
     *
     * @param request
     * @return
     */
    public static String signMusicList(MusicListRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.ID, request.getId() + "");
        parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        parmsUtils.getPostBody(Api.PAGE_SIZE, request.getPage_size() + "");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 音乐搜索
     *
     * @param request
     * @return
     */
    public static String signMusicSearchList(MusicSearchRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getKeyword())) {
            parmsUtils.getPostBody(Api.KEYWORDS, request.getKeyword() + "");
        }
        if (!TextUtils.isEmpty(request.getPage() + "")) {
            parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        }
        if (TextUtils.isEmpty(request.getPage_size() + "")) {
            parmsUtils.getPostBody(Api.PAGESIZE, request.getPage_size() + "");
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    public static String signUserCommentList(UserCommentRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();

        if (!TextUtils.isEmpty(request.getOther_id() + "")) {
            parmsUtils.getPostBody(Api.OTHER_ID, request.getOther_id() + "");
        }
        if (!TextUtils.isEmpty(request.getDu_type() + "")) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 上传视频
     *
     * @param request
     * @return
     */
    public static String signVideoUploadId(VideoUploadRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.WCSID, request.getWcsid() + "");
//        if (!TextUtils.isEmpty(request.getCountry())) {
//            parmsUtils.getPostBody(Api.COUNTRY, request.getCountry());
//        }
//        parmsUtils.getPostBody(Api.LAT, request.getLat() + "");
//        parmsUtils.getPostBody(Api.LNG, request.getLng() + "");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    public static String signWcsToken(WcsRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();

        parmsUtils.getPostBody(Api.TITLE, request.getTitle() + "");
        parmsUtils.getPostBody(Api.DURATION, request.getDuration() + "");
        parmsUtils.getPostBody(Api.WIDTH, request.getWidth() + "");
        parmsUtils.getPostBody(Api.HEIGHT, request.getHeight() + "");
        parmsUtils.getPostBody(Api.TAG, request.getTag() + "");
        parmsUtils.getPostBody(Api.MUSIC_ID, request.getMusic_id()+"");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    public static String signUserDetailsLikesList(PersonWorkRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();

        if (!TextUtils.isEmpty(request.getDu_type() + "")) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");
        }
        if (!TextUtils.isEmpty(request.getOther_id() + "")) {
            parmsUtils.getPostBody(Api.OTHER_ID, request.getDu_type() + "");
        }
        if (!TextUtils.isEmpty(request.getPage() + "")) {
            parmsUtils.getPostBody(Api.PAGE, request.getPage());
        }
        if (!TextUtils.isEmpty(request.getPage_size() + "")) {
            parmsUtils.getPostBody(Api.PAGESIZE, request.getPage_size());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 资源文件
     *
     * @param request
     * @return
     */
    public static String signNvsResCategroy(NvsResCategroyRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.TYPE, request.getType() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    public static String signUserLike(LikesRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();

        if (!TextUtils.isEmpty(request.getDu_type() + "")) {
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");
        }
        if (!TextUtils.isEmpty(request.getOther_id() + "")) {
            parmsUtils.getPostBody(Api.OTHER_ID, request.getOther_id() + "");
        }
        if (!TextUtils.isEmpty(request.getPage() + "")) {
            parmsUtils.getPostBody(Api.PAGE, request.getPage());
        }
        if (!TextUtils.isEmpty(request.getPage_size() + "")) {
            parmsUtils.getPostBody(Api.PAGE_SIZE, request.getPage_size());
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 资源集合
     *
     * @param request
     * @return
     */
    public static String signNvsResList(NvsResListRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.TYPE, request.getType() + "");
        parmsUtils.getPostBody(Api.CATEGROY, request.getCategroy() + "");
        parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        parmsUtils.getPostBody(Api.PAGESIZE, request.getPage_size() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    public static String signFollow(FollowsRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();

        if (!TextUtils.isEmpty(request.getDu_type() + "")) {
            parmsUtils.getPostBody("du_type", request.getDu_type() + "");
        }
        if (!TextUtils.isEmpty(request.getFollower_id() + "")) {
            parmsUtils.getPostBody("follower_id", request.getFollower_id() + "");
        }
        parmsUtils.getPostBody("video_id", request.getVideo_id() + "");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 推送token
     *
     * @param request
     * @return
     */
    public static String signPushToken(PushTokenRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody("token", request.getToken() + "");
        parmsUtils.getPostBody("topic", request.getTopic() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 推送消息
     *
     * @param request
     * @return
     */
    public static String signFeedBack(FeedBackRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.TYPE, request.getType() + "");
        parmsUtils.getPostBody(Api.CONTENT, request.getContent() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 金币任务
     *
     * @param request
     * @return
     */
    public static String signTaskGoldAdVideo(TaskRequestAdVideo request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.ID, request.getId() + "");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 任务大厅
     *
     * @param request
     * @return
     */
    public static String signTaskFinish(TaskFinishRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.ID, request.getId() + "");

        if (!TextUtils.isEmpty(request.getDebug())) {
            parmsUtils.getPostBody(Api.DEBUG, request.getDebug());
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 三方登陆成功
     *
     * @param request
     * @return
     */
    public static String signThridLogin(ThridLoginRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.LOGIN_SOURCE, request.getLogin_source() + "");

        parmsUtils.getPostBody(Api.TASK_THRID, request.getUser_id());

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     *
     *
     * @param request
     * @return
     */
    public static String signTaskDisLikeVideo(DisLikeVideoRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getVideo_id())) {
            parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideo_id() + "");
        }

        parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     *
     *
     * @param request
     * @return
     */
    public static String signVideoStay(VideoStayRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        if (!TextUtils.isEmpty(request.getVideo_id())) {
            parmsUtils.getPostBody(Api.VIDEO_ID, request.getVideo_id() + "");
        }

        if (!TextUtils.isEmpty(request.getDu_type())){
            parmsUtils.getPostBody(Api.DU_TYPE, request.getDu_type() + "");
        }

        if (!TextUtils.isEmpty(request.getStay_time())){
            parmsUtils.getPostBody(Api.STAY_TIME, request.getStay_time() + "");
        }

        if (!TextUtils.isEmpty(request.getR_type())){
            parmsUtils.getPostBody(Api.R_TYPE,request.getR_type());
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }
}
