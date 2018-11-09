package com.sven.huinews.international.config.http;

import android.content.Context;
import android.text.TextUtils;

import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.config.Constant;
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
import com.sven.huinews.international.utils.HasUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.TimeUtils;
import com.sven.huinews.international.utils.cache.ACache;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wedemo.activity.request.WcsRequest;

/**
 * auther: sunfuyi
 * data: 2018/5/12
 * effect:
 * 举报视频
 */
public class MyRetrofit {
    private Context mContext;
    private static MyRetrofit mInstance;
    private HttpService mHttpService;

    public MyRetrofit(Context mContext) {
        this.mContext = mContext;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                //添加拦截器
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL)
//                 .baseUrl(Api.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mHttpService = retrofit.create(HttpService.class);
    }

    public MyRetrofit(Context mContext, String httpUrl) {
        this.mContext = mContext;
        String http = null;
        if (httpUrl == null) {
            // TODO: 2018/9/5   请求地址
            //  http = Api.BASE_URL;
        } else {
            http = httpUrl;
        }


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mHttpService = retrofit.create(HttpService.class);
    }

    public static MyRetrofit getInstance(Context context, String httpUrl) {
        if (mInstance == null) {
            synchronized (MyRetrofit.class) {
                if (mInstance == null) {
                    mInstance = new MyRetrofit(context, httpUrl);
                }
            }
        }
        return mInstance;
    }

    public static MyRetrofit getInstance(Context context) {
//        if (mInstance == null) {
        synchronized (MyRetrofit.class) {
//                if (mInstance == null) {
            mInstance = new MyRetrofit(context);
//                }
        }
//        }
        return mInstance;
    }

    public HttpService getService() {
        return mHttpService;
    }


    /**
     * 配置信息
     */
    public void appConfig(DataCallBack callBack) {
        mHttpService.appConfig(getHeaderMap(), 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    /**
     * 登录
     *
     * @param request
     * @param callBack
     */
    public void onLogin(LoginRequest request, DataCallBack callBack) {
        String json = SignJson.singOnLogin(request.getAccount(), request.getPassword());
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onLogin(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void newsStatistics(NewsStatisticsRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signNewsStatistics(request)));
        mHttpService.newsStatistics(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    public void videoStatistics(VideoStatisticsRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signVideoStatistics(request)));
        mHttpService.videoStatistics(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    /**
     * 分享
     *
     * @param request
     * @param callBack
     */
    public void inviteShareDatas(SharedRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), ""));
        mHttpService.videoShareUrl(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    /**
     * facebokchecklogin
     *
     * @param request
     * @param callBack
     */
    public void checkIsLogin(PlatformLogin request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        String platformId = null;
        String platform = null;
        if (request.getPlatform().equals(Api.TYPE_FACEBOOK)) {
            platformId = request.getFb_id();
            platform = Api.FACEBOOK;
            request.setFb_id(platformId);
        } else if (request.getPlatform().equals(Api.TYPE_TIWTTER)) {
            platformId = request.getTwitter_id();
            platform = Api.TIWTTER_ID;
            request.setTwitter_id(platformId);
        } else if (request.getPlatform().equals(Api.TYPE_LINKEDIN)) {
            platformId = request.getLk_id();
            platform = Api.LINKEDIN_ID;
            request.setLk_id(platformId);
        }
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.checkIsLogin(platform, platformId)));
        request.setPlatform(null);
        mHttpService.checkLogin(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    /**
     * new 三方登录
     */
    public void checkIsLogin(ThirdRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.checkIsLogin(request)));
        mHttpService.checkLogin(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 视频列表
     *
     * @param request
     * @param callBack
     */
    public void onVideoList(VideoListRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.singVideoList(request)));
        mHttpService.getVideoList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void getFollowList(FollowRequest request, DataCallBack callBack) {
        String json = SignJson.signFollowList(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.getFollowList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    /**
     * 视频评论
     *
     * @param callBack
     */
    public void getVideoCommentList(VideoCommentRequest requests, DataCallBack callBack) {
        String json = SignJson.signVideoCommentList(requests);
        requests.setTime(nowTime());
        requests.setNonce_str(nonceStr());
        requests.setSign(signStr(requests.getTime(), requests.getNonce_str(), json));
        mHttpService.getVideoCommentList(getHeaderMap(), requests)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 评论点赞
     *
     * @param request
     * @param callBack
     */

    public void videoCommentLike(VideoCommentLikeRequest request, DataCallBack callBack) {
        String json = SignJson.signVideoCommentLike(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onVideoCommentLike(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 举报
     *
     * @param request
     * @param callBack
     */
    public void videoRepost(VideoReportRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signVideoReport(request)));
        mHttpService.videoReport(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    public void onFollowList(FansAndFollowRequest request, DataCallBack callback) {
        String json = SignJson.signFollowAndFans(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onFollowList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public void onFansList(FansAndFollowRequest request, DataCallBack callback) {
        String json = SignJson.signFollowAndFans(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onFansList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }


    /**
     * 评论举报
     *
     * @param request
     * @param callBack
     */
    public void videoCommentRepost(TipOffRequest request, DataCallBack callBack) {
        String json = SignJson.signVideoReport(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onVideoCommentReport(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 活动
     *
     * @param callBack
     */
    public void getTaskPush(DataCallBack callBack) {
        String nonceStr = nonceStr();
        String time = nowTime();
        mHttpService.getTaskPush(getHeaderMap(), time, nonceStr, signStr(time, nonceStr, ""), 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 视频添加评论
     *
     * @param request
     * @param callBack
     */
    public void onVideoAdComment(AdCommentRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signVideoAdComment(request)));
        mHttpService.VIDEOCOMENT(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 分享视频
     *
     * @param request
     * @param callBack
     */
    public void VideoSharedUrl(VideoShareUrlRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signVideoShareUrl(request)));
        mHttpService.videoShareUrl(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 删除视频
     *
     * @param request
     * @param callBack
     */
    public void VideoDelete(VideoDeleteRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signVideoDelete(request)));
        mHttpService.videoDelete(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void addVideoShardNumber(ShareVisitRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signShareVisit(request)));
        mHttpService.shareVisit(getHeaderMap(), request).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 收藏视频or收藏影视
     *
     * @param request
     * @param callBack
     */
    public void onVideoCollection(VideoCollectionRequest request, DataCallBack callBack) {
        String json = SignJson.signVideoCollection(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onVideoCollection(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 取消收藏
     *
     * @param request
     * @param callBack
     */
    public void onCollectionCancel(VideoCollectionCancelRequest request, DataCallBack callBack) {
        String json = SignJson.signCollectionCancel(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onCancelCollection(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 新闻列表
     *
     * @param request
     * @param callBack
     */
    public void onNewsList(NewsListRequst request, DataCallBack callBack) {
        String json = SignJson.singNewsList(request.getR_type() + "", request.getPage() + "");
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.getNewsInfoList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 视频点赞
     *
     * @param request
     * @param callBack
     */
    public void onVideoLike(VideoLikeRequest request, DataCallBack callBack) {
        String json = SignJson.signVideoLike(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onVideoLike(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 获取阿里云视频播放数据
     */

    public void getAlPlayUrl(String VideoID, DataCallBack callBack) {
        GetAliPlayUrlRequest request = new GetAliPlayUrlRequest();
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setId(VideoID);
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signAliVideoid(VideoID)));
        mHttpService.getVideoInfo(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }

    /**
     * 开宝箱
     *
     * @param request
     * @param callBack
     */
    public void getGoldByTask(TaskRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signTaskGold(request)));
        mHttpService.getGoldByTask(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 获取开宝箱时间
     */
    public void getGoldBoxTime(GetboxtimeRequst requst, DataCallBack callBack) {
        requst.setTime(nowTime());
        requst.setNonce_str(nonceStr());
        requst.setSign(signStr(requst.getTime(), requst.getNonce_str(), SignJson.signGoldTime(requst)));
        mHttpService.getGoldboxTime(getHeaderMap(), requst)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }


    public void getRedBag(DataCallBack callBack) {
        String nonceStr = nonceStr();
        String time = nowTime();
        mHttpService.getRedBag(getHeaderMap(), time, nonceStr, signStr(time, nonceStr, ""), 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /***
     * 临时登录
     * @param request
     * @param callBack
     */
    public void onTempLogin(TempLoginRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.singOnTempLogin(request.getMobileBrand())));
        mHttpService.onTempLogin(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 注册获取验证码
     *
     * @param request
     * @param callBack
     */
    public void getSmsCode(GetCodeRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signGetSmsCode(request)));
        mHttpService.getSmsCode(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 绑定邮箱
     */
    public void bindEmail(BindEmailRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signBindEmail(request)));
        mHttpService.getBindSmsCode(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 邮箱注册
     */
    public void onEmailRegister(RegistRequst request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.singRegister(request)));
        mHttpService.onEmailRegister(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    /**
     * Twitter注册
     *
     * @param request
     * @param callBack
     */
    public void onTwitterReg(TwitterRegRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.twitterReg(request)));
        mHttpService.twitterReg(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * FaceBook注册
     *
     * @param request
     * @param callBack
     */

    public void onFacebookReg(FacebookRegRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.facebookReg(request)));
        mHttpService.facebookReg(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /***
     * LinkedIn注册
     * @param request
     * @param callBack
     */

    public void onLinkedInReg(LinkedInRegRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.linkedInReg(request)));
        mHttpService.linkedInReg(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 重新设置密码
     *
     * @param request
     * @param callBack
     */
    public void resetPassWord(ResetPassRequst request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signFindPass(request)));
        mHttpService.resetPss(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    /***
     * 用户信息
     * @param callBack
     */
    public void userInfo(DataCallBack callBack) {
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setNonce_str(nonceStr());
        baseRequest.setTime(nowTime());
        baseRequest.setSign(signStr(baseRequest.getTime(), baseRequest.getNonce_str(), ""));
        mHttpService.userInfo(getHeaderMap(), baseRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }

    /**
     * 音乐分类
     *
     * @param callBack
     */
    public void onMusicCategroy(DataCallBack callBack) {
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setNonce_str(nonceStr());
        baseRequest.setTime(nowTime());
        baseRequest.setSign(signStr(baseRequest.getTime(), baseRequest.getNonce_str(), ""));
        mHttpService.onMusicCategroy(getHeaderMap(), baseRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 上传头像文件
     *
     * @param file
     * @param callBack
     */

    public void uploadImage(File file, DataCallBack callBack) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploadFile", file.getName(), requestFile);

        mHttpService.uploadFile(getHeaderMap(), body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 上传个人信息
     *
     * @param request
     * @param callBack
     */
    public void setUserMsg(UserMsgRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signUserMsg(request)));
        mHttpService.setUserMsg(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    /**
     * 分享计数
     */
    public void shareVisit(ShareVisitRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signShareVisit(request)));
        mHttpService.shareVisit(getHeaderMap(), request).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 获取分享信息
     *
     * @param
     */
    public void getShareInfo(final DataCallBack callBack) {
        mHttpService.getShareInfo(getHeaderMap()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    //用户信息
    public void getUserDetails(UserCommentRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signUserCommentList(request)));
        mHttpService.getUserCommentList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    //个人work信息
    public void getUserPerSonWorkDetails(DataCallBack callBack) {
        mHttpService.getUserCommentList(getHeaderMap())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    public void apprenticePageData(DataCallBack callBack) {
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setNonce_str(nonceStr());
        baseRequest.setTime(nowTime());
        baseRequest.setSign(signStr(baseRequest.getTime(), baseRequest.getNonce_str(), ""));
        mHttpService.apprenticePageData(getHeaderMap(), baseRequest).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    //用户作品

    public void getUserDetailsLikesList(PersonWorkRequest request, DataCallBack callBack) {
        String json = SignJson.signUserDetailsLikesList(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.getUserDetailsLikesList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }


    //用户Likes

    public void getUserLikesList(LikesRequest request, DataCallBack callBack) {
        String json = SignJson.signUserLike(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.getLikesList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    public void onFollow(FollowsRequest request, DataCallBack callBack) {
        String json = SignJson.signFollow(request);
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), json));
        mHttpService.onFollow(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 头部信息
     *
     * @return
     */
    private HashMap<String, String> getHeaderMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Api.HEADER_OS, "android");
        map.put(Api.HEADER_MEID, PhoneUtils.getAndroidId(mContext));
        map.put(Api.HEADER_VERSION, BuildConfig.VERSION_NAME);
        map.put(Api.HEADER_TICKET, UserSpCache.getInstance(mContext).getStringData(UserSpCache.KEY_TICKET));
        map.put(Api.HEADER_RATIO, PhoneUtils.getScreenWidth(mContext) + "*" + PhoneUtils.getScreenHeight(mContext));
        map.put(Api.HEADER_BRAND, PhoneUtils.getPhoneBrand());
        map.put(Api.HEADER_BLUETOOTH, PhoneUtils.notHasBlueTooth());
        map.put(Api.HEADER_OPTICAL_SENSOR, PhoneUtils.notHasLightSensorManager(mContext));
        map.put(Api.HEADER_CPU, PhoneUtils.checkIsNotRealPhone());
        map.put(Api.LANGUAGE, PhoneUtils.getLocalLanguage());
        if (!TextUtils.isEmpty(ACache.get(mContext).getAsString(Constant.CACHE_COUNTRY))) {
            map.put(Api.LAT, ACache.get(mContext).getAsString(Constant.CACHE_LAT) + "");
            map.put(Api.LNG, ACache.get(mContext).getAsString(Constant.CACHE_LNG) + "");
            map.put(Api.COUNTRY, ACache.get(mContext).getAsString(Constant.CACHE_COUNTRY) + "");
        }
        return map;
    }

    private String nowTime() {
        TimeUtils timeUtils = new TimeUtils();
        return timeUtils.phoneTime(AppConfig.getAppContext()) + "";
    }

    private String nonceStr() {
        return HasUtils.getInstance().nonceStr();
    }

    private String signStr(String time, String nonceStr, String json) {
        try {
            String ticket = UserSpCache.getInstance(mContext).getStringData(UserSpCache.KEY_TICKET);
            return HasUtils.getInstance().sign(time, nonceStr, json, ticket);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 领取金币
     */
   /* public void onToGiveGold(DataCallBack callBack) {
        BaseRequest request = new BaseRequest();
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), ""));
        mHttpService.onToGiveGold(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }*/


    /**
     * 领取金币
     */
   /* public void onToGiveGold(DataCallBack callBack) {
        BaseRequest request = new BaseRequest();
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), ""));
        mHttpService.onToGiveGold(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }*/


    /**
     * 音乐列表
     */
    public void onMusicList(MusicListRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signMusicList(request)));
        mHttpService.onMusicList(getHeaderMap(), request).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void downloadMusic(String musicUrl, DataCallBack callBack) {
        mHttpService.downloadFile(musicUrl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void onMusicSearch(MusicSearchRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signMusicSearchList(request)));
        mHttpService.onMusicSearchList(getHeaderMap(), request).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 获取临时身份
     *
     * @param callBack
     */
    public void onWcsToken(WcsRequest publishInfo, DataCallBack callBack) {
        publishInfo.setNonce_str(nonceStr());
        publishInfo.setTime(nowTime());
        publishInfo.setSign(signStr(publishInfo.getTime(), publishInfo.getNonce_str(), SignJson.signWcsToken(publishInfo)));
        mHttpService.onWcsUploadToken(getHeaderMap(), publishInfo)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 上传视频
     *
     * @param request
     * @param callBack
     */
    public void onUploadVideoId(VideoUploadRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signVideoUploadId(request)));
        mHttpService.onUploadVideoWcs(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 标签列表
     *
     * @param callBack
     */
    public void onTagList(DataCallBack callBack) {
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setNonce_str(nonceStr());
        baseRequest.setTime(nowTime());
        baseRequest.setSign(signStr(baseRequest.getTime(), baseRequest.getNonce_str(), ""));
        mHttpService.onTagList(getHeaderMap(), baseRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 素材分类类表
     *
     * @param request
     * @param callBack
     */
    public void onNvsResCategroy(NvsResCategroyRequest request, DataCallBack callBack) {
        request.setNonce_str(nonceStr());
        request.setTime(nowTime());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signNvsResCategroy(request)));
        mHttpService.onNvsResCategroy(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 资源文件集合
     *
     * @param request
     * @param callBack
     */
    public void onNvsResList(NvsResListRequest request, DataCallBack callBack) {
        request.setNonce_str(nonceStr());
        request.setTime(nowTime());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signNvsResList(request)));
        mHttpService.onNvsResList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 消息列表
     *
     * @param callBack
     */
    public void onMessageList(DataCallBack callBack) {
        BaseRequest request = new BaseRequest();
        request.setNonce_str(nonceStr());
        request.setTime(nowTime());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), ""));
        mHttpService.onMessageList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 获取意见分类
     *
     * @param callBack
     */
    public void onFeedClassify(DataCallBack callBack) {
        BaseRequest request = new BaseRequest();
        request.setNonce_str(nonceStr());
        request.setTime(nowTime());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), ""));
        mHttpService.feedbackClassify(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 发送意见
     *
     * @param request
     * @param callBack
     */
    public void sendFeedBack(FeedBackRequest request, DataCallBack callBack) {
        request.setNonce_str(nonceStr());
        request.setTime(nowTime());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signFeedBack(request)));
        mHttpService.feedbackSend(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void onPushToken(PushTokenRequest request, DataCallBack callBack) {
        request.setNonce_str(nonceStr());
        request.setTime(nowTime());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signPushToken(request)));
        mHttpService.onPushToken(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    public void getAdVideoByTask(TaskRequestAdVideo request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signTaskGoldAdVideo(request)));
        mHttpService.getAdVideoByTask(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void getTaskList(TaskListRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), ""));
        mHttpService.getTaskList(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void getTaskListNew(TaskListRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), ""));
        mHttpService.getTaskListNew(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void taskFinish(TaskFinishRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signTaskFinish(request)));
        mHttpService.taskFinish(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 获取收徒动态
     * @param request
     * @param callBack
     */
    public void getTaskInfo(InfoRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), ""));
        mHttpService.getTaskInfo(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void thridLogin(ThridLoginRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signThridLogin(request)));
        mHttpService.getGold(getHeaderMap(), request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void addIntroduceVideoCoid(DataCallBack callBack) {
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setNonce_str(nonceStr());
        baseRequest.setTime(nowTime());
        baseRequest.setSign(signStr(baseRequest.getTime(), baseRequest.getNonce_str(), ""));
        mHttpService.addIntroduceVideoCoid(getHeaderMap(), baseRequest).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void videoPlayTime(VideoPlayTimeSize videoPlayTimeSize, DataCallBack callBack) {
        videoPlayTimeSize.setNonce_str(nonceStr());
        videoPlayTimeSize.setTime(nowTime());
        videoPlayTimeSize.setSign(signStr(videoPlayTimeSize.getTime(), videoPlayTimeSize.getNonce_str(), ""));
        mHttpService.videoPlayTime(getHeaderMap(), videoPlayTimeSize).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }
}
