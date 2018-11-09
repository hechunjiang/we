package wedemo.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;


public class AppInfo {
    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "2.0.0";
        }
    }

    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //android 获取当前手机型号
    public static String getPhoneModel(Context context) {
        Build bd = new Build();
        return bd.MODEL;
    }

    //android 获取当前手机品牌
    public static String getPhoneBrand(Context context) {
        Build bd = new Build();
        return bd.BRAND;
    }

    public static String getSDKVersion() {
        return "Android" + Build.VERSION.RELEASE;
    }


}
