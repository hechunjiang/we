package com.sven.huinews.international.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Toast;

import com.chinanetcenter.wcs.android.ClientConfig;
import com.chinanetcenter.wcs.android.api.FileUploader;
import com.chinanetcenter.wcs.android.api.ParamsConf;
import com.chinanetcenter.wcs.android.entity.OperationMessage;
import com.chinanetcenter.wcs.android.internal.UploadFileRequest;
import com.chinanetcenter.wcs.android.listener.FileUploaderListener;
import com.chinanetcenter.wcs.android.listener.SliceUploaderBase64Listener;
import com.google.gson.Gson;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.dialog.GoldComeDialog;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.event.OpenDialogEvent;
import com.sven.huinews.international.entity.event.RefreshTaskEvent;
import com.sven.huinews.international.entity.requst.VideoUploadRequest;
import com.sven.huinews.international.entity.response.AliyunInfoResponse;
import com.sven.huinews.international.entity.response.ComentsResponse;
import com.sven.huinews.international.main.follow.activity.FollowVideoPlay1Activity;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.utils.ActivityManager;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.view.dialog.CustomShareDialog;
import com.sven.huinews.international.view.dialog.UploadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import wedemo.MessageEvent;
import wedemo.activity.data.PublishInfo;
import wedemo.activity.data.WcsBean;
import wedemo.utils.Constants;

public class UploadService extends Service {

    private UploadProgressDialog mDialog;
    private PublishInfo mVideoInfo;
    private WcsBean mTokenInfo;
    private Handler mHandler = new Handler();
    private MyNews myNews = new MyNews();
    private GoldComeDialog mGoldComeDialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Activity activity = ActivityManager.getInstance().getMainActivity();
        mDialog = UploadProgressDialog.initDialog(activity);
        mDialog.setCancelable(false);
        mDialog.getWindow().setDimAmount(0);
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            mVideoInfo = (PublishInfo) bundle.getSerializable("videoInfo");
            mTokenInfo = (WcsBean) bundle.getSerializable("tokenInfo");
        }

        try {
            if (mDialog != null) {
                mDialog.show();
            }//你自己定义的显示PopupWindow的方法
        } catch (Exception e) {
            e.printStackTrace();
        }


        //uploadAliyun(mTokenInfo, mVideoInfo);
        uploadWCS(mTokenInfo, mVideoInfo);
        return super.onStartCommand(intent, flags, startId);

    }

    //网宿上传
    public void uploadWCS(final WcsBean wcsBean, final PublishInfo mInfo) {

        myNews.setTitle(mInfo.getTitle());
        myNews.setCoverUrl(wcsBean.getData().getCover_url());
        myNews.setId(wcsBean.getData().getWcsid());
        myNews.setDu_type("1");
        myNews.setImagePath(mInfo.getImagePath());
        myNews.setVideoPath(mInfo.getVideoPath());
        myNews.setWaterVideoPath(mInfo.getWarterVideoPath());

        if (wcsBean == null || wcsBean.getData() == null || wcsBean.getData().getVideo() == null) {
            ToastUtils.showShort(this, getString(R.string.sendCodeerror));
            mDialog.dismiss();
            stopSelf();
            return;
        }

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setConnectionTimeout(120 * 1000);//连接超时
        clientConfig.setSocketTimeout(120 * 1000);//socket超时
        clientConfig.setMaxErrorRetry(0);//重试次数

        ParamsConf conf = new ParamsConf();
        conf.mimeType = "video/mp4";
        conf.keyName = wcsBean.getData().getVideo().getFileName();   //key上传到云端的文件名


        //上传参数
        FileUploader.setClientConfig(clientConfig);
        FileUploader.setUploadUrl(wcsBean.getData().getUrl()); //url
        FileUploader.setParams(conf);

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date curDate = new Date(System.currentTimeMillis());
        if (Constants.UPLOAD_TEST) {

            EventBus.getDefault().post(new MessageEvent("test", "、\n\n视频上传\n\n token=" + wcsBean.getData().getVideo().getToken()
                    + "\n\nimagename=" + wcsBean.getData().getVideo().getFileName() + "\n\n开始时间:" + df.format(curDate)));

        }

        //上传
        File file = new File(mInfo.getVideoPath());
        FileUploader.upload(this, wcsBean.getData().getVideo().getToken(), file, null, new FileUploaderListener() {
            @Override
            public void onSuccess(int status, JSONObject responseJson) {

                String videoUrl = wcsBean.getData().getUrl() + wcsBean.getData().getVideo().getFileName();

                if (Constants.UPLOAD_TEST) {
                    String hash = null;
                    try {
                        hash = responseJson.getString("hash");
                    } catch (JSONException e) {

                    }

                    Date success = new Date(System.currentTimeMillis());
                    EventBus.getDefault().post(new MessageEvent("test", "responseJson=" + responseJson.toString() +
                            "\nhash===" + hash + "\n\n上传成功:url = " + videoUrl + "\n成功结束时间:" + df.format(success) + "\n耗时:" + (success.getTime() - curDate.getTime()) + " ms"));
                    LogUtil.showLog("视频hash ====== " + videoUrl);
                }

                VideoUploadRequest request = new VideoUploadRequest();
                request.setWcsid(wcsBean.getData().getWcsid());
                uploadVideoId(request);
            }

            @Override
            public void onFailure(OperationMessage operationMessage) {
                if (Constants.UPLOAD_TEST) {

                    Date fail = new Date(System.currentTimeMillis());

                    EventBus.getDefault().post(new MessageEvent("test", "\n\n视频上传失败:result=" + operationMessage.getMessage()
                            + "status:" + operationMessage.getStatus() + "\n失败时间:" + df.format(fail) + "\n耗时:" + (fail.getTime() - curDate.getTime()) + " ms"));
                    LogUtil.showLog("错误-------->" + operationMessage.getMessage());
                }
                //Toast.makeText(UploadService.this, R.string.upload_error, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadService.this, R.string.upload_error, Toast.LENGTH_SHORT).show();
                    }
                });
                stopSelf();
            }

            @Override
            public void onProgress(UploadFileRequest request, final long currentSize, final long totalSize) {
                if (Constants.UPLOAD_TEST) {

                    EventBus.getDefault().post(new MessageEvent("test", "\n视频上传进度: progress:"
                            + currentSize + ",totalSize：" + totalSize));
                    LogUtil.showLog("uploaded---------->" + currentSize + ",total--------->" + totalSize);
                }
                if (currentSize == totalSize) {
                    Date progress = new Date(System.currentTimeMillis());
                    EventBus.getDefault().post(new MessageEvent("test", "\n上传进度完成时间:"
                            + df.format(progress) + "\n耗时:" + (progress.getTime() - curDate.getTime()) + " ms\n"));
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.setMessage(currentSize * 100 / totalSize + "%");
                    }
                });
            }
        });

    }


//    public void uploadAliyun(AliyunInfoResponse.DataBean dataBean, final PublishInfo mInfo) {
//        //1.初始化短视频上传对象
//        VODSVideoUploadClient vodsVideoUploadClient = new VODSVideoUploadClientImpl(AppConfig.getAppContext());
//        vodsVideoUploadClient.init();
//
//        // 文件路径保证存在之外因为Android 6.0之后需要动态获取权限，请开发者自行实现获取"文件读写权限".
//        VodHttpClientConfig vodHttpClientConfig = new VodHttpClientConfig.Builder()
//                .setMaxRetryCount(2)//重试次数
//                .setConnectionTimeout(15 * 1000)//连接超时
//                .setSocketTimeout(15 * 1000)//socket超时
//                .build();
//
//        //构建短视频VideoInfo,常见的描述，标题，详情都可以设置
//        SvideoInfo svideoInfo = new SvideoInfo();
//        svideoInfo.setTitle(new File(mInfo.getVideoPath()).getName());
//        svideoInfo.setDesc("");
//        svideoInfo.setCateId(1);
//
//        //构建点播上传参数(重要)
//        VodSessionCreateInfo vodSessionCreateInfo = new VodSessionCreateInfo.Builder()
//                .setImagePath(mInfo.getImagePath())//图片地址
//                .setVideoPath(mInfo.getVideoPath())//视频地址
//                .setAccessKeyId(dataBean.getAccessKeyId())//临时accessKeyId
//                .setAccessKeySecret(dataBean.getAccessKeySecret())//临时accessKeySecret
//                .setSecurityToken(dataBean.getSecurityToken())//securityToken
//                .setExpriedTime(dataBean.getExpiration())//STStoken过期时间
//                .setRequestID(dataBean.getRequestId())//requestID，开发者可以传将获取STS返回的requestID设置也可以不设.
//                .setIsTranscode(true)//是否转码.如开启转码请AppSever务必监听服务端转码成功的通知
//                .setSvideoInfo(svideoInfo)//短视频视频信息
//                .setVodHttpClientConfig(vodHttpClientConfig)//网络参数
//                .build();
//
//        vodsVideoUploadClient.uploadWithVideoAndImg(vodSessionCreateInfo, new VODSVideoUploadCallback() {
//            @Override
//            public void onUploadSucceed(String s, String s1) {
//                VideoUploadRequest request = new VideoUploadRequest();
//                request.setVideoId(s);
//                request.setCoverImg(s1);
//                request.setTitle(mInfo.getTitle());
//                request.setWidth(mInfo.getWidth());
//                request.setHeight(mInfo.getHeight());
//                request.setDuration(mInfo.getDuration());
//                request.setTag(mInfo.getTag());
//                request.setLat(mInfo.getLat());
//                request.setLng(mInfo.getLng());
//                request.setCountry(mInfo.getCountry());
//                uploadVideoId(request);
//            }
//
//            @Override
//            public void onUploadFailed(String s, String s1) {
//                LogUtil.showLog("msg--onUploadFailed:" + s + "-------s1:" + s1);
//            }
//
//            @Override
//            public void onUploadProgress(final long l, final long l1) {
//                LogUtil.showLog("msg--onUploadProgress:" + l + "------l1:" + l1);
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDialog.setMessage(l * 100 / l1 + "%");
//                    }
//                });
//            }
//
//            @Override
//            public void onSTSTokenExpried() {
//                LogUtil.showLog("msg--onSTSTokenExpried");
//            }
//
//            @Override
//            public void onUploadRetry(String s, String s1) {
//                LogUtil.showLog("msg--onUploadRetry:" + s + "------s1:" + s1);
//            }
//
//            @Override
//            public void onUploadRetryResume() {
//                LogUtil.showLog("msg--onUploadRetryResume");
//            }
//        });
//    }


    public void uploadVideoId(final VideoUploadRequest request) {
        MyRetrofit.getInstance(AppConfig.getAppContext()).onUploadVideoId(request, new DataCallBack() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSucceed(String json) {
                mDialog.dismiss();
                Toast.makeText(UploadService.this, R.string.push_ok, Toast.LENGTH_SHORT).show();
                ComentsResponse comentsResponse = new Gson().fromJson(json, ComentsResponse.class);
                if (comentsResponse.getData().getGold() > 0) {
                    ToastUtils.showGoldCoinToast(UploadService.this, getString(R.string.award_of_work)
                            , "+" + comentsResponse.getData().getGold());
                    EventBus.getDefault().post(new RefreshTaskEvent());
                }

                EventBus.getDefault().post(Common.REFRESH_VIDEO);
                EventBus.getDefault().post(new MessageEvent(Common.REFRESH_VIDEO, myNews));
                stopSelf();
            }

            @Override
            public void onFail(BaseResponse baseResponse) {
                mDialog.dismiss();
                Toast.makeText(UploadService.this, baseResponse.getMsg(), Toast.LENGTH_SHORT).show();
                stopSelf();
            }
        });
    }
}
