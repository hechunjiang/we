package com.sven.huinews.international.utils.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;


import com.sven.huinews.international.R;
import com.sven.huinews.international.config.AppInfo;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.config.http.MyRetrofit;
import com.sven.huinews.international.main.follow.activity.FollowVideoPlay1Activity;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.NetWorkUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.view.dialog.UploadProgressDialog;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * auther: sunfuyi
 * data: 2018/5/30
 * effect:
 */
public class UpdateInfo {
    private Activity activity;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private int notification_id = 0;
    private boolean isPermission; // 判断是否有权限
    private boolean isClickUpdate;
    private boolean isMustUpdate;
    private String apkName = "";
    private VersionInfo mVersionInfo;
    private AppUpdateDialog dialog;
    Handler handler = new Handler();
    private UploadProgressDialog mDialog;


    public UpdateInfo(Activity activity) {
        this.activity = activity;
        notificationManager = (NotificationManager) this.activity.getSystemService(NOTIFICATION_SERVICE);
        initNotify();
    }

    public void setPermission(boolean permission) {
        isPermission = permission;
    }

    public void setClickUptate(boolean clickUptate) {
        isClickUpdate = clickUptate;
    }

    public void getVersionInfo(final boolean isHome) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final VersionInfo mVersion = VersionManger.getVersion();
                mVersionInfo = mVersion;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mVersion.getVersion_code() != null && mVersion.getVersion_code().length() > 0) {
                            if (AppInfo.getVersionCode(activity) < Integer.parseInt(mVersion.getVersion_code())) {
                                if (!isPermission) {
                                    versionDialog(mVersion);
                                    return;
                                }
                                if (isClickUpdate) {
                                    checkVersion(mVersion);
                                    return;
                                }

                                netWorkInfo(mVersion);

                            } else {
                                if (!isHome) {
                                    ToastUtils.showLong(activity, activity.getString(R.string.you_are_all_caught_up));
                                }
                            }
                        }
                    }
                });
            }
        };
        new Thread(runnable).start();
    }


    /**
     * wifi环境下才下载
     *
     * @param versionInfo
     */
    private void netWorkInfo(VersionInfo versionInfo) {
        if (NetWorkUtils.getNetWorkStates(activity) != NetWorkUtils.TYPE_WIFI) {
            return;
        }
        checkVersion(versionInfo);
    }

    /**
     * 版本更新
     *
     * @param versionInfo
     */
    public void versionDialog(final VersionInfo versionInfo) {
        dialog = new AppUpdateDialog(activity, versionInfo);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isClickUpdate) {
                    File[] files = getFiles(Constant.SAVE_APK_PATH);
                    File file = files[0];
                    //格式化文件名称
                    String fileName = file.getName();
                    //获取文件名中的版本号
                    String tempVersionCode = getInsideString(fileName, "_", ".a");
                    //格式化版本号
                    String versionCode = tempVersionCode.replace(".", "");
                    //首先判断文件中的apk包是否等于服务器版本号，如果相等则判断 文件的apk版本号是否大于app版本号，大于就提示更新。小于就重新下载最新apk包
                    if (versionCode.equals(versionInfo.getVersion_code())) {
                        if (Integer.valueOf(versionCode) > AppInfo.getVersionCode(activity)) {
                            //如果本地版本号大于app版本号提示更新
                            installApk(file);
                            return;
                        }
                    } else {
                        //删除文件，重新下载最新包
                        File file1 = new File(file.getPath());
                        if (file1.exists()) {
                            file1.delete();
                        }
                        downloadApk(versionInfo);
                    }

                } else {
                    try {
                        dialog.show();
                    } catch (Exception e) {

                    }
                }
            }
        }, 500);

        dialog.OnUpdateApkLisenter(new AppUpdateDialog.OnUpdateApkLisenter() {
            @Override
            public void update() {
                if (isPermission) {
                    File[] files = getFiles(Constant.SAVE_APK_PATH);

                    installApk(files[0]);
                } else {
                    if (checkPermissionLinsenter != null) {
                        checkPermissionLinsenter.checkPermission();
                    }
                }
            }

            @Override
            public void kill() {
                activity.finish();
            }
        });
    }


    private void downloadApk(final VersionInfo versionInfo) {

        apkName = "juNews_" + versionInfo.getVersion_name() + ".apk";
        final File apk = new File(Constant.SAVE_APK_PATH);
        if (!apk.exists()) {
            apk.mkdirs();
        }
        MyRetrofit.getInstance(activity, null).getService()
                .downloadApk(versionInfo.getApk_url())
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, File>() {
                    @Override
                    public File call(ResponseBody responseBody) {
                        File file = writeResponseBodyToDisk(apk.getPath() + "/" + apkName, responseBody);
                        return file;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//在Android主线程中展示
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(File file) {
                        if (isClickUpdate) {
                            installApk(file);
                        } else {
                            versionDialog(versionInfo);
                        }

                    }
                });
    }

    public void uploadApk() {
        isClickUpdate = true;
        downloadApk(mVersionInfo);
    }

    private void installApk(File apk) {
        if (notificationManager != null) {
            notificationManager.cancel(notification_id);
        }
        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(activity.getApplicationContext(), "com.sven.huinews.international.fileProvider", apk);
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apk);
        }
        if (uri != null) {
            intent1.setDataAndType(uri, "application/vnd.android.package-archive");
            activity.startActivity(intent1);
        } else {

        }
    }

    /**
     * 检查当前是否存在最新版本
     */
    private void checkVersion(VersionInfo versionInfo) {
        File[] files = getFiles(Constant.SAVE_APK_PATH);

        if (files == null || files.length <= 0) {
            //未找到相关文件，则进行下载
            downloadApk(versionInfo);
            return;
        }
        File file = files[0];
        //格式化文件名称
        String fileName = file.getName();
        //获取文件名中的版本号
        String tempVersionCode = getInsideString(fileName, "_", ".a");
        //格式化版本号
        String versionCode = tempVersionCode.replace(".", "");
        //首先判断文件中的apk包是否等于服务器版本号，如果相等则判断 文件的apk版本号是否大于app版本号，大于就提示更新。小于就重新下载最新apk包
        if (versionCode.equals(versionInfo.getVersion_code())) {
            if (Integer.valueOf(versionCode) > AppInfo.getVersionCode(activity)) {
                //如果本地版本号大于app版本号提示更新
                versionDialog(versionInfo);
                return;
            }
        } else {
            //删除文件，重新下载最新包
            File file1 = new File(file.getPath());
            if (file1.exists()) {
                file1.delete();
            }

            downloadApk(versionInfo);
        }

    }

    private File writeResponseBodyToDisk(String path, ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(path);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                    if (isClickUpdate) {
                        showNotify(activity.getString(R.string.downloading), (int) (fileSizeDownloaded * 1.0 / fileSize * 100));
                    }
                }
                outputStream.flush();
                return futureStudioIconFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(activity);
        mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setContentIntent(getDefalutIntent(0))
                // .setNumber(number)//显示数量
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setAutoCancel(false)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                // .setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                // requires VIBRATE permission
                .setSmallIcon(R.mipmap.news_logo);
    }

    private void showNotify(String status, int progress) {
        mBuilder.setContentTitle(activity.getResources().getString(R.string.app_name));
        mBuilder.setContentText(status + (progress + "%"));
        mBuilder.setProgress(100, progress, false);
        notificationManager.notify(notification_id, mBuilder.build());
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    private PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 1, new Intent(), flags);
        return pendingIntent;
    }

    @Subscribe
    public void updateApkEvent(FileLoadEvent event) {
        showNotify(activity.getString(R.string.downloading), (int) (event.getBytesLoaded() * 1.0 / event.getTotal() * 100));
    }

    /**
     * 获取目录下的文件
     *
     * @param path
     * @return
     */
    public File[] getFiles(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        return files;
    }

    /**
     * 指定两个字符串，获取两个字符串中间的值
     *
     * @param str      原始字符串
     * @param strStart 开始字符串
     * @param strEnd   结束字符串
     * @return
     */
    public String getInsideString(String str, String strStart, String strEnd) {
        if (str.indexOf(strStart) < 0) {
            return "";
        }
        if (str.indexOf(strEnd) < 0) {
            return "";
        }
        return str.substring(str.indexOf(strStart) + strStart.length(), str.indexOf(strEnd));
    }

    private checkPermissionLinsenter checkPermissionLinsenter;

    public void setCheckPermissionLinsenter(checkPermissionLinsenter checkPermissionLinsenter) {
        this.checkPermissionLinsenter = checkPermissionLinsenter;
    }

    public interface checkPermissionLinsenter {
        void checkPermission();
    }


    public void getVersionIsUpdate() {
        mDialog = UploadProgressDialog.initGrayDialog(activity);
        mDialog.setCancelable(false);
        mDialog.setMessage("");
        mDialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final VersionInfo mVersion = VersionManger.getVersion();
                mVersionInfo = mVersion;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mVersion.getVersion_code() != null && mVersion.getVersion_code().length() > 0) {
                            mDialog.cancel();
                            if (AppInfo.getVersionCode(activity) < Integer.parseInt(mVersion.getVersion_code())) {
//                                ToastUtils.showLong(activity, activity.getString(R.string.update_vision_log));
                                new AlertDialog.Builder(activity)
                                        .setTitle(R.string.tip)
                                        .setMessage(activity.getString(R.string.update_vision_log))
                                        .setNegativeButton(R.string.effect_str5, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).setPositiveButton(R.string.continues, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sven.huinews.international"));
                                        intent.setAction(Intent.ACTION_VIEW);
                                        activity.startActivity(intent);
                                    }
                                }).show();
                            } else {
                                ToastUtils.showLong(activity, activity.getString(R.string.you_are_all_caught_up));
                            }
                        }
                    }
                });
            }
        };
        new Thread(runnable).start();
    }
}
