package com.sven.huinews.international.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sven.huinews.international.tplatform.SaveImageListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sfy. on 2018/3/20 0020.
 */

public class FrescoUtils {
    /**
     * 以高斯模糊显示。
     *
     * @param draweeView View。
     * @param url        url.
     * @param iterations 迭代次数，越大越魔化。
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
     */
    public static void showUrlBlur(Context context, SimpleDraweeView draweeView, String url, int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();

            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
            //    draweeView.setHierarchy(hierarchy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载网络图片
     *
     * @param draweeView
     * @param url
     */
    public static void loadUrl(Context context, SimpleDraweeView draweeView, String url, int res) {
      /*  GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        *//**
         * 设置淡入淡出效果的显示时间
         *//*
        GenericDraweeHierarchy hierarchy = builder.setFadeDuration(500).build();
        hierarchy.setPlaceholderImage(res);
      //  hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
        DraweeController placeHolderDraweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url)) //为图片设置url
                .setTapToRetryEnabled(true)   //设置在加载失败后,能否重试
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(placeHolderDraweeController);
        draweeView.setHierarchy(hierarchy);
        draweeView.setAspectRatio(4/3);
        draweeView.setImageURI(Uri.parse(url));*/
        draweeView.setImageURI(Uri.parse(url));


    }

    public static void loadImgUrlAndSetBitMap(final Context context, final String url, final SimpleDraweeView imageView, final SimpleDraweeView bg) {
        if (imageView.getTag() != null) {
            if (imageView.getTag().equals(url)) {
                return;
            }
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }

        imageView.setTag(url);
        // bg.setTag(url);
        if (imageView.getTag().equals(url)) {
            showUrlBlur(context, imageView, url, 3, 25);
            // showUrlBlur(context,bg,url,3,25);
        }


    }

    public static void SaveImageFromDataSource(Activity activity, String url, final String localSavePath,final SaveImageListener l) {
        final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, activity);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    Boolean savesucess = saveBitmap(bitmap, localSavePath + "shareImage.jpg");
                    if (savesucess) {
                        //保存成功处理
//                        LogUtils.logLocalD("msg---imagepath:" + localSavePath);
                       if (l!=null){
                           l.saveImageOk();
                       }
                    } else {
                        //保存失败处理
                    }

                } else {
                    //保存失败处理
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                //保存失败处理
            }
        }, CallerThreadExecutor.getInstance());
    }



    public static Boolean saveBitmap(Bitmap bitmap, String localSavePath) {
        if (TextUtils.isEmpty(localSavePath)) {
            throw new NullPointerException("path is null");
        }
        File f = new File(localSavePath);
        if (f.exists()) {// 如果本来存在的话，删除
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }

        return true;

    }
}
