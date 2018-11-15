package com.sven.huinews.international;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.ap.AirPush;
import com.ap.gdpr.AirpushGdpr;
import com.ap.gdpr.ApAgreement;
import com.duapps.ad.base.DuAdNetwork;
import com.duapps.ad.video.DuVideoAdSDK;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.sven.huinews.international.base.BaseResponse;

import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.entity.Appinfor;
import com.sven.huinews.international.tplatform.PlatformConstant;
import com.sven.huinews.international.utils.LolipopBitmapMemoryCacheSupplier;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.utils.db.ReadNewsDb;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sfy. on 2018/9/5 0005.
 */

public class AppConfig extends Application {

    public static AppConfig baseApplication;

    private static final String FLURRY_APIKEY = "2F722GJ3VR47WDZDSHNW";
    public static final String FLURRY_ADSPACE = "watch_and_earn_stream_detailed_card";
    private Handler handler;

    //airPush广告
    private String airPushApiKey = "1361616622473125425";//1361616622473125425   1533003182316462570
    private String airPushId = "29011";//29011  396134

    public static Context getAppContext() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        baseApplication = this;
        handler = new Handler(getMainLooper());
        ReadNewsDb.getInstance(getApplicationContext()).deleteDb();

        UMConfigure.init(this, "5b120ffca40fa31fbb0002c1", getChannel(), UMConfigure.DEVICE_TYPE_PHONE, "1d496044653f9ef0bc3181e02010693f");
        //facebook登录
        initFacebook();
        //Tiwtter登录
        initTiwtter();
        setFresco();
        initUDAd();
        //初始化airpush
        initAirPush();
        activityList = new ArrayList<>();
        initGlobeActivity();

        //初始化google设备测试
        //initGoogleDevice();

        //添加统计代码
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);
        /**
         * 配置信息
         */
        MyRetrofit.getInstance(getApplicationContext()).appConfig(new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                Appinfor appConfig = new Gson().fromJson(json, Appinfor.class);//SharedPreferences存储相关数据
                Log.d("appConfig", "onSucceed: " + appConfig);
                UserSpCache.getInstance(getApplicationContext()).putInt(UserSpCache.NEEDCOUNT_LOGIN, Integer.parseInt(appConfig.getData().getNEEDCOUNT()));
                UserSpCache.getInstance(getApplicationContext()).putString(UserSpCache.SHARE_TITLE, appConfig.getData().getSHARE() + "");

//                UserSpCache.getInstance(getApplicationContext()).putString(UserSpCache.SHARE_CONTENT, appConfig.getData().getSHARE().getContent());
//                UserSpCache.getInstance(getApplicationContext()).putLong(UserSpCache.SIGN_SERVICE_TIME, appConfig.getData().getTime());
//                UserSpCache.getInstance(getApplicationContext()).putLong(UserSpCache.SIGN_LOCAL_TIME, new Date().getTime());
//                UserSpCache.getInstance(getApplicationContext()).putInt(UserSpCache.V_AT_COUNT, appConfig.getData().getGOlD().getV_at_count());
//                UserSpCache.getInstance(getApplicationContext()).putInt(UserSpCache.V_AT_RED, appConfig.getData().getGOlD().getV_at_red());
            }

            @Override
            public void onFail(BaseResponse baseResponse) {

            }
        });


        initYaHooAd();
    }


    private void initYaHooAd() {
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withCaptureUncaughtExceptions(true)
                .withContinueSessionMillis(10000)
                //.withLogLevel(Log.ERROR)
                .build(this, FLURRY_APIKEY);
    }

    public static AppConfig getInstance() {
        return baseApplication;
    }

    public Handler getMainThreadHandler() {
        return handler;
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    /*
     * 百度 海外版
     * */
    private void initUDAd() {
        DuAdNetwork.init(this, getConfigJSON(getApplicationContext()));
        DuVideoAdSDK.init(this, getConfigJSON(getApplicationContext()));
    }

    /**
     * 初始化airpush广告
     */
    private void initAirPush() {
        AirPush.init(this, airPushApiKey, airPushId);
        AirPush.enableTestMode();
        AirpushGdpr.init(getApplicationContext());
        AirpushGdpr.registerAgreement(ApAgreement.getAgreement(this));
        Thread fetchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                AirpushGdpr.fetchRemoteStatuses();
            }
        });
        fetchThread.setPriority(Thread.MIN_PRIORITY);
        fetchThread.start();
    }

    /**
     * 获取渠道名称
     *
     * @return
     */
    private String getChannel() {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String channelName = appInfo.metaData.getString("CHANNEL_NAME");
            return channelName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    /**
     * 从assets中读取txt
     */
    private String getConfigJSON(Context context) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bis = new BufferedInputStream(context.getAssets().open("json.txt"));
            byte[] buffer = new byte[4096];
            int readLen = -1;
            while ((readLen = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readLen);
            }
        } catch (IOException e) {
            Log.e("", "IOException :" + e.getMessage());
        } finally {
            closeQuietly(bis);
        }

        return bos.toString();
    }


    private void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            // empty
        }
    }

    private void initTiwtter() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(PlatformConstant.TIWTTER_KEY, PlatformConstant.TIWTTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    private void setFresco() {
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio) {
                    //清空内存缓存
                    ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
                    /*
                     * 添加清楚内存缓存
                     * */
                    Fresco.getImagePipeline().clearMemoryCaches();

                }
            }
        });
        //小图片的磁盘配置,用来储存用户头像之类的小图
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(this.getCacheDir())//缓存图片基路径
                .setBaseDirectoryName(getString(R.string.app_name))//文件夹名
                .setMaxCacheSize(20 * ByteConstants.MB)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * ByteConstants.MB)//缓存的最大大小,当设备极低磁盘空间
                .build();


        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .setBitmapMemoryCacheParamsSupplier(new LolipopBitmapMemoryCacheSupplier((ActivityManager) getSystemService(ACTIVITY_SERVICE)))
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();

//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
//                .setDownsampleEnabled(true)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//                .build();
        Fresco.initialize(getApplicationContext(), config);
    }

    private List<Activity> activityList = null;
    private Activity mActivity = null;


    //判断是否是指定activity
    public Activity getMbActivity(int position) {
        if (activityList != null && activityList.size() > 0) {

            for (int i = 0; i < activityList.size(); i++) {
                if (i != position) {
                    activityList.get(i).finish();
                }
            }
            return activityList.get(position);
        } else {
            return null;
        }
    }

    //监听activity的生命周期
    private void initGlobeActivity() {

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityList.add(activity);
                mActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public Activity getCurrentActivity() {
        return mActivity;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


//    private void initGoogleDevice(){
//        Stetho.initializeWithDefaults(this);
//        new OkHttpClient.Builder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();
//    }
}
