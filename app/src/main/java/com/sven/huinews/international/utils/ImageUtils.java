package com.sven.huinews.international.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sven.huinews.international.R;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by chenshuaiwen on 18/1/24.
 */

public class ImageUtils {

    public static void loadAvterUrl(Context context, final String url, final ImageView imageView) {
        if (imageView.getTag() != null) {
            if (imageView.getTag().equals(url)) {
                return;
            }
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        imageView.setTag(url);
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {

            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (imageView.getTag().equals(url)) {
                    imageView.setImageDrawable(resource);
                }
            }
        };

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.def_avatar);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(simpleTarget);
    }

    public static void loadImgUrl(Context context, final String url, final ImageView imageView) {

        if (imageView.getTag() != null) {
            if (imageView.getTag().equals(url)) {
                return;
            }
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        imageView.setTag(url);
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {

            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (imageView.getTag().equals(url)) {
                    imageView.setImageDrawable(resource);
                }
            }
        };
        Glide.with(context)
                .load(url)
                .into(simpleTarget);
    }

    public static void loadImgUrlAndSetBitMap(final Context context, final String url, final ImageView imageView, final ImageView bg) {
        if (imageView.getTag() != null) {
            if (imageView.getTag().equals(url)) {
                return;
            }
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        imageView.setTag(url);
        bg.setTag(url);
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (imageView.getTag().equals(url)) {
                    imageView.setBackgroundDrawable(resource);
                }
                if (bg.getTag().equals(url)) {
                    bg.setBackgroundDrawable(resource);
                }
            }
        };
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.optionalTransform(new BlurTransformation(25, 3));
        requestOptions.skipMemoryCache(true);
        Glide.with(context).load(url)
                .apply(requestOptions)
                .into(simpleTarget);
    }

    public static void loadImgGetResult(Context context, String url, SimpleTarget<Drawable> simpleTarget) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(context).load(url)
                .into(simpleTarget);
    }

    private static Drawable rsBlur(Context context, Drawable source, int radius) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) source;
        Bitmap inputBmp = bitmapDrawable.getBitmap();
        RenderScript renderScript = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(renderScript, inputBmp);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(inputBmp);
        renderScript.destroy();
        return new BitmapDrawable(inputBmp);
    }

    public static Bitmap uri2Bitmap(Context mContext, Uri uri) {
        InputStream in = null;
        try {
            in = mContext.getContentResolver().openInputStream(uri);
            //从输入流中获取到图片
            Bitmap bm = BitmapFactory.decodeStream(in);
            in.close();
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param
     * @description 保存图片到手机SD卡, 并返回图片对应的文件i
     * @author ldm
     * @time 2016/7/11 9:55
     */
    public static File saveBitmapToSdCard(Bitmap bm) {
        //自定义图片名称
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
        //定义图片存放的位置
        File tempFile = new File("/sdcard/Image/");
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        String fileName = "/sdcard/Image/" + name;

        NativeUtil.compressBitmap(bm,fileName);

        File pic = new File(fileName);
        return pic;
//
//        try {
//
//            FileOutputStream os = new FileOutputStream(pic);
//            //对图片进行压缩
//            bm.compress(Bitmap.CompressFormat.PNG, 100, os);
//            os.flush();
//            os.close();
//            return pic;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }

}
