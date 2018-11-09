package com.sven.huinews.international.tplatform.twitter;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.event.ShareResponseEvent;
import com.sven.huinews.international.entity.jspush.JsShareType;

import com.sven.huinews.international.entity.response.TwitterRegResponse;
import com.sven.huinews.international.tplatform.SaveImageListener;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.FrescoUtils;

import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;


/**
 * Created by sfy. on 2018/4/25 0025.
 */

public class TwitterLogin {

    private TwitterAuthClient mTwitterAuthClient;
    private TwitterShareLisenter twitterShareLisenter;

    public TwitterLogin() {
    }

    public void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShareResponseEvent(ShareResponseEvent event) {
        if (twitterShareLisenter == null) return;
        switch (event.getShareResponseType()) {
            case Common.JS_RESPONSE_CODE_SUCCEED:
                twitterShareLisenter.getShareOk(CommonUtils.getShareSuccesResponse());
                break;
            case Common.JS_RESPONSE_CODE_FAIL:
                twitterShareLisenter.getShareFail(CommonUtils.getShareFailResponse());
                break;
            case Common.JS_RESPONSE_CODE_CANCEL:
                twitterShareLisenter.getShareCancel(CommonUtils.getShareCancelResponse());
                break;
            default:
                break;
        }
    }

    public void setTwitterShareLisenter(TwitterShareLisenter twitterLoginLisenter) {
        this.twitterShareLisenter = twitterLoginLisenter;
    }

    public interface TwitterShareLisenter {

        void getShareOk(String response);

        void getShareFail(String response);

        void getShareCancel(String response);
    }

    public void loginTwitter(Activity activity, final titterLoginCallback callback) {

        if (mTwitterAuthClient == null) {
            mTwitterAuthClient = new TwitterAuthClient();
        }
        mTwitterAuthClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                String name = result.data.getUserName();
                long userId = result.data.getUserId();
                getTwitterUserInfo(userId, callback);
            }

            @Override
            public void failure(TwitterException e) {
                callback.onFailure();
            }
        });
    }

    private void getTwitterUserInfo(final long userId, final titterLoginCallback callback) {


        final TwitterSession activeSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterApiClient client = new TwitterApiClient(activeSession);
        AccountService accountService = client.getAccountService();
        Call<User> show = accountService.verifyCredentials(false, false, false);
        show.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                User data = result.data;
                String profileImageUrl = data.profileImageUrl.replace("_normal", "");
                String name = data.name;
                TwitterRegResponse twitterRegResponse = new TwitterRegResponse();
                twitterRegResponse.setHeadImg(profileImageUrl);
                twitterRegResponse.setNickName(name);
                twitterRegResponse.setTwitter_id(data.id + "");
                twitterRegResponse.setSex("1");
                callback.onSuccess(twitterRegResponse);
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                Log.e("twitter", "msg---tiwtterInfo:failure");
                callback.onFailure();
            }
        });
    }


    public void setActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTwitterAuthClient != null) {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface titterLoginCallback {
        void onSuccess(TwitterRegResponse data);

        void onFailure();
    }


    public void twitterShare(final Activity activity, final JsShareType jsShareType, int shareType) {
        if (!CommonUtils.isApplicationAvilible(activity, "com.twitter.android")) {
            ToastUtils.showLong(activity, activity.getString(R.string.you_not_installed_twitter));
            return;
        }

        if (shareType == Common.TWITTER_SHARE_IAMGE) {
            //图片地址
            final File imagePath = new File(Common.SAVE_SHARE_IMAGE_PATH);
            if (!imagePath.exists()) {
                imagePath.mkdirs();
            }


            //下载文件，存储到相对目录
            FrescoUtils.SaveImageFromDataSource(activity, jsShareType.getImgUrl(), imagePath.getAbsolutePath(), new SaveImageListener() {
                @Override
                public void saveImageOk() {

//                    LogUtils.logLocalD("msg---ID::::" + BuildConfig.APPLICATION_ID + ".fileProvider");
                    Uri imageUri = FileProvider.getUriForFile(activity,
                            BuildConfig.APPLICATION_ID + ".fileProvider",
                            new File(imagePath.getAbsolutePath() + "shareImage.jpg"));
//                    LogUtils.logLocalD("msg---shareImage:::-" + imageUri.toString());

                    shareByTweetComposer(activity, jsShareType, imageUri);
                }

                @Override
                public void saveImageFailed() {
                    LogUtil.showLog("保存失败=========");
                    shareByTweetComposer(activity, jsShareType, null);
                }
            });
        } else if (shareType == Common.TWITTER_SHARE_URL) {
            shareByKit(activity, jsShareType);
        }
    }

    /**
     * 分享图片等
     * 没有回调....
     */
    private void shareByTweetComposer(Activity activity, JsShareType jsShareType, Uri imageUri) {
        TweetComposer.Builder builder = null;
        try {
            builder = new TweetComposer.Builder(activity)
                    .text(jsShareType.getContent())
                    .image(imageUri)
                    .url(new URL(jsShareType.getUrl()));
            builder.show();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (twitterShareLisenter != null) {
            twitterShareLisenter.getShareOk(CommonUtils.getShareSuccesResponse());
        }
    }

    /**
     * 分享连接
     * 有回调
     */
    private void shareByKit(final Activity activity, final JsShareType jsShareType) {
        //获取权限信息
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        //没有权限 则调用获取权限接口
        if (session == null) {
            loginTwitter(activity, new titterLoginCallback() {
                @Override
                public void onSuccess(TwitterRegResponse data) {
                    launchComposer(activity, jsShareType);
                }

                @Override
                public void onFailure() {
                    LogUtil.showLog("登录授权失败");
                }
            });
        } else {
            launchComposer(activity, jsShareType);
        }
    }

    /**
     * kit分享
     * 带广播回调
     */
    private void launchComposer(Activity activity, JsShareType jsShareType) {
        final Intent intent = new ComposerActivity.Builder(activity)
                .session(TwitterCore.getInstance().getSessionManager().getActiveSession())
                .text(jsShareType.getContent() + jsShareType.getUrl())//内容+title
//                .hashtags("") twitter内容主题  以#开头
                .createIntent();
        activity.startActivity(intent);
    }

}
