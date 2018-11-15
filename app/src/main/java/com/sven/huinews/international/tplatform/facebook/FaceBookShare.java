package com.sven.huinews.international.tplatform.facebook;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMedia;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.tplatform.SaveImageListener;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.FrescoUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;

import java.io.File;

/**
 * Created by sfy. on 2018/4/27 0027.
 */

public class FaceBookShare {
    private Activity mActivity;
    private ShareDialog shareDialog;
    private CallbackManager callBackManager;
    public static final int SHARE_REQUEST_CODE = 10010;
    private ShareLinkContent.Builder shareLinkContentBuilder;

    public FaceBookShare(Activity activity,FacebookCallback facebookCallback) {
        this.mActivity = activity;
        callBackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(mActivity);
        //注册分享状态监听回调接口
        shareDialog.registerCallback(callBackManager, facebookCallback, SHARE_REQUEST_CODE);
        shareLinkContentBuilder = new ShareLinkContent.Builder();
    }

    /**
     * 分享
     */
    public void share(JsShareType mJsShareType) {
        if (!CommonUtils.isApplicationAvilible(mActivity,"com.facebook.katana")){
            ToastUtils.showLong(mActivity, "You've not installed facebook App,Please re-try after installation.\n");
            return;
        }
        Log.d("msg---ImgUrl",Uri.parse(mJsShareType.getImgUrl())+"");
        Log.d("msg---ImgUrl",Uri.parse(mJsShareType.getUrl())+"");
        shareLinkContentBuilder
                .setContentTitle(mJsShareType.getTitle())
                .setImageUrl(Uri.parse(mJsShareType.getImgUrl()))
                .setContentDescription(mJsShareType.getContent())
                .setContentUrl(Uri.parse(mJsShareType.getUrl()));
        ShareLinkContent shareLinkContent = shareLinkContentBuilder.build();
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(shareLinkContent);
        }
    }

    /**
     * 分享
     */
    public void sharePhoto(final Activity activity,final JsShareType mJsShareType) {
        if (!CommonUtils.isApplicationAvilible(mActivity,"com.facebook.katana")){
            ToastUtils.showLong(mActivity, "You've not installed facebook App,Please re-try after installation.\n");
            return;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.share_bg);
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .setContentUrl(Uri.parse(mJsShareType.getUrl()))
                .build();

        shareDialog.show(content);

        Log.d("msg---ImgUrl",Uri.parse(mJsShareType.getImgUrl())+"");
        Log.d("msg---ImgUrl",Uri.parse(mJsShareType.getUrl())+"");

//        final File imagePath = new File(Common.SAVE_SHARE_IMAGE_PATH);
//        if (!imagePath.exists()) {
//            imagePath.mkdirs();
//        }
//
//        //下载文件，存储到相对目录
//        FrescoUtils.SaveImageFromDataSource(activity, mJsShareType.getImgUrl(), imagePath.getAbsolutePath(), new SaveImageListener() {
//            @Override
//            public void saveImageOk() {
//                Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath() + "shareImage.jpg");
//                SharePhoto sharePhoto = new SharePhoto.Builder()
//                        .setBitmap(bitmap)
//                        .build();
//
//                SharePhotoContent content = new SharePhotoContent.Builder()
//                        .addPhoto(sharePhoto)
//                        .setContentUrl(Uri.parse(mJsShareType.getUrl()))
//                        .build();
//
//                shareDialog.show(content);
//            }
//
//            @Override
//            public void saveImageFailed() {
//                LogUtil.showLog("保存失败=========");
//
//                share(mJsShareType);
//            }
//        });


    }



    public void shareVideo(JsShareType mJsShareType){
        if (!CommonUtils.isApplicationAvilible(mActivity,"com.facebook.katana")){
            ToastUtils.showLong(mActivity, "You've not installed facebook App,Please re-try after installation.\n");
            return;
        }

        Bitmap bm = BitmapFactory.decodeFile(mJsShareType.getImagePath());
        SharePhoto sharePhoto1 = new SharePhoto.Builder()
                .setBitmap(bm)
                .build();

        Uri videoUri = FileProvider.getUriForFile(mActivity,
                BuildConfig.APPLICATION_ID + ".fileProvider",
                new File(TextUtils.isEmpty(mJsShareType.getWaterVideoPath())?mJsShareType.getVideoPath():mJsShareType.getWaterVideoPath()));
        ShareVideo shareVideo1 = new ShareVideo.Builder()
                .setLocalUrl(videoUri)
                .build();


        ShareVideoContent shareVideoContent = new ShareVideoContent.Builder()
                .setVideo(shareVideo1)
                .build();

        shareDialog.show(shareVideoContent);

    }

    public CallbackManager getCallbackManager() {
        return callBackManager;
    }
    private FacebookCallback facebookCallback = new FacebookCallback() {
        @Override
        public void onSuccess(Object o) {
            LogUtil.showLog("msg---Facebook分享成功:");
        }

        @Override
        public void onCancel() {
            LogUtil.showLog("msg---Facebook取消分享:");
        }

        @Override
        public void onError(FacebookException error) {
            LogUtil.showLog("msg---Facebook分享出错:");
        }
    };
}
