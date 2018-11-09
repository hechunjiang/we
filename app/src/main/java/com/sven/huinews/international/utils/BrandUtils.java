package com.sven.huinews.international.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by Burgess on 2018/9/18 0018.
 */
public class BrandUtils {
    public final static String XIAO_MI_BRAND = "Xiaomi";
    private Context mContext;

    public BrandUtils(Context context) {
        mContext = context;
    }

    public void openSettingActivity() {
        Intent intent = new Intent();
        if (AppInfoUtils.getPhoneBrand(mContext.getApplicationContext()).equals(XIAO_MI_BRAND)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("extra_pkgname", mContext.getPackageName());
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName())); // 根据包名打开对应的设置界面
        }
        mContext.startActivity(intent);
    }
}
