package wedemo.config.http;

import android.content.Context;

import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.utils.cache.UserSpCache;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wedemo.activity.down.DownRequest;
import wedemo.base.BaseRequest;
import wedemo.config.api.Api;
import wedemo.music.MusicListRequest;
import wedemo.music.MusicSearchRequest;
import wedemo.utils.HasUtils;
import wedemo.utils.PhoneUtils;
import wedemo.utils.TimeUtils;

/**
 * auther: sunfuyi
 * data: 2018/5/12
 * effect:
 */
public class MyRetrofit {
    private Context mContext;
    private static MyRetrofit mInstance;
    private HttpService mHttpService;

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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
        map.put(Api.LANGUAGE, PhoneUtils.getLocalLanguage(mContext));
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
            return HasUtils.getInstance().sign(time, nonceStr, json, UserSpCache.getInstance(mContext).getStringData(UserSpCache.KEY_TICKET));
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
     * 素材分类
     *
     * @param request
     * @param callBack
     */
    public void onSDKCategroy(DownRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signSDKCategroy(request)));
        mHttpService.onSDKCategory(getHeaderMap(), request).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 素材下载列表
     *
     * @param request
     * @param callBack
     */
    public void onSDKDown(DownRequest request, DataCallBack callBack) {
        request.setTime(nowTime());
        request.setNonce_str(nonceStr());
        String s = signStr(request.getTime(), request.getNonce_str(), SignJson.signSDKDownload(request));
        request.setSign(signStr(request.getTime(), request.getNonce_str(), SignJson.signSDKDownload(request)));
        mHttpService.onSDKDown(getHeaderMap(), request).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

}
