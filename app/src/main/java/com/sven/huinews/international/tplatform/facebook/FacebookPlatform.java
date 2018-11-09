package com.sven.huinews.international.tplatform.facebook;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.response.FacebookRegResponse;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sfy. on 2018/4/26 0026.
 */

public class FacebookPlatform {
    private Activity activity;
    private CallbackManager callbackManager;
    private FacebookListener facebookListener;
    private List<String> permissions = Collections.<String>emptyList();
    private LoginManager loginManager;

    public FacebookPlatform(Activity activity) {
        this.activity = activity;

        //初始化facebook登录服务
        callbackManager = CallbackManager.Factory.create();
        getLoginManager().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // login success
                AccessToken accessToken = loginResult.getAccessToken();
                getLoginInfo(accessToken);
            }

            @Override
            public void onCancel() {
                //取消登录
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }

            }
        });

        permissions = Arrays.asList("public_profile");
    }

    /**
     * 登录
     */
    public void login() {
        if (!CommonUtils.isApplicationAvilible(activity, "com.facebook.katana")) {
            ToastUtils.showLong(activity, "You've not installed facebook App,Please re-try after installation.");
            return;
        }
        getLoginManager().logInWithReadPermissions(activity, permissions);
    }

    /**
     * 退出
     */
    public void logout() {
      /*  String logout = activity.getResources().getString(
                com.facebook.R.string.com_facebook_loginview_log_out_action);
        String cancel = activity.getResources().getString(
                com.facebook.R.string.com_facebook_loginview_cancel_action);
        String message;
        Profile profile = Profile.getCurrentProfile();
        if (profile != null && profile.getName() != null) {
            message = String.format(
                    activity.getResources().getString(
                            com.facebook.R.string.com_facebook_loginview_logged_in_as),
                    profile.getName());
        } else {
            message = activity.getResources().getString(
                    com.facebook.R.string.com_facebook_loginview_logged_in_using_facebook);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(logout, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getLoginManager().logOut();
                    }
                })
                .setNegativeButton(cancel, null);
        builder.create().show();*/
    }

    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    public void getLoginInfo(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {

                    String id = object.optString("id");   //比如:1565455221565
                    String name = object.optString("name");  //比如：Zhang San
                    String gender = object.optString("gender");  //性别：比如 male （男）  female （女）
                    String emali = object.optString("email");  //邮箱：比如：56236545@qq.com

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");


                    PlatformLogin platformLogin = new PlatformLogin();
                    platformLogin.setFb_id(id);
                    platformLogin.setPlatform(Common.TYPE_FACEBOOK);
                    FacebookRegResponse mFacebookRegResponse = new FacebookRegResponse();
                    mFacebookRegResponse.setFb_id(id);
                    mFacebookRegResponse.setHeadImg(photo);
                    mFacebookRegResponse.setNickName(name);
                    mFacebookRegResponse.setSex(gender);
                    mFacebookRegResponse.setFb_access_token(accessToken.getToken());
                    if (facebookListener != null) {
                        facebookListener.facebookLoginSuccess(platformLogin, mFacebookRegResponse);
                    }

                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * 获取loginMananger
     *
     * @return
     */
    private LoginManager getLoginManager() {
        if (loginManager == null) {
            loginManager = LoginManager.getInstance();
        }
        return loginManager;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    /**
     * 设置登录监听
     *
     * @param facebookListener
     */
    public void setFacebookListener(FacebookListener facebookListener) {
        this.facebookListener = facebookListener;
    }

    public interface FacebookListener {
        void facebookLoginSuccess(PlatformLogin platformLogin, FacebookRegResponse response);

        void facebookLoginFail();
    }

}
