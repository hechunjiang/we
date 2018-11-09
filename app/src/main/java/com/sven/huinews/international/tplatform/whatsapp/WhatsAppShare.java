package com.sven.huinews.international.tplatform.whatsapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.ToastUtils;

import java.io.File;

/**
 * Created by sfy. on 2018/10/31 0031.
 */

public class WhatsAppShare {
    public Context mContext;

    public WhatsAppShare(Context mContext) {
        this.mContext = mContext;
    }

    public void shareLink(String linkUrl) {
        if (!CommonUtils.isApplicationAvilible(mContext, "com.whatsapp")) {
            ToastUtils.showLong(mContext, "You've not installed Whats App,Please re-try after installation.");
            return;
        }
        String type = "text/*";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_TEXT, linkUrl);
        intent.setPackage("com.whatsapp");
        mContext.startActivity(Intent.createChooser(intent, "Share to"));
    }

    public void shareImg(String pathname,String title) {
        if (!CommonUtils.isApplicationAvilible(mContext, "com.whatsapp")) {
            ToastUtils.showLong(mContext, "You've not installed Whats App,Please re-try after installation.");
            return;
        }
        String type = "image/*";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_TEXT, title);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pathname));
        intent.setPackage("com.whatsapp");
        mContext.startActivity(Intent.createChooser(intent, "Share to"));
    }

    public void shareVideo() {
        if (!CommonUtils.isApplicationAvilible(mContext, "com.whatsapp")) {
            ToastUtils.showLong(mContext, "You've not installed Whats App,Please re-try after installation.");
            return;
        }
        String type = "video/*";
        String mediaPath = "/手机存储/DCIM/Camera/video_20180930_211220.mp4";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_TEXT, "分享说明");
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setPackage("com.whatsapp");
        mContext.startActivity(Intent.createChooser(intent, "Share to"));
    }
}
