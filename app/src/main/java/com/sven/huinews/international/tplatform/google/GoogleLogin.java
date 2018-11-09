package com.sven.huinews.international.tplatform.google;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.LogUtil;

/**
 * Created by sfy. on 2018/10/30 0030.
 */

public class GoogleLogin {
    public int requestCode = 10;
    private FragmentActivity activity;
    public GoogleSignInOptions gso;
    public GoogleApiClient mGoogleApiClient;
    public GoogleApiClient.OnConnectionFailedListener listener;
    private GoogleSignListener googleSignListener;


    public GoogleLogin(FragmentActivity activity, GoogleApiClient.OnConnectionFailedListener listener) {
        this.activity = activity;
        this.listener = listener;

        //初始化谷歌登录服务
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken(activity.getString(R.string.server_client_id))
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
               // .enableAutoManage(activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(listener)
                .build();
    }

    /**
     * 登录
     */
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, requestCode);
    }

    public String handleSignInResult(GoogleSignInResult result) {
        String res = "";
        if (result.isSuccess()) {
            //登录成功
            GoogleSignInAccount acct = result.getSignInAccount();
            res = "登录成功"
                    + "用户名为：" + acct.getDisplayName()
                    + "  邮箱为：" + acct.getEmail()
                    + " token为：" + acct.getIdToken()
                    + " 头像地址为：" + acct.getPhotoUrl()
                    + " Id为：" + acct.getId()
                    + " GrantedScopes为：" + acct.getGrantedScopes();

            LogUtil.showLog("Google", res);
            if (googleSignListener != null) {
                googleSignListener.googleLoginSuccess(acct);
            }
        } else {

            res = "-1";
            if (googleSignListener != null) {
                googleSignListener.googleLoginFail();
            }
        }
        return res;
    }

    public void setGoogleSignListener(GoogleSignListener googleSignListener) {
        this.googleSignListener = googleSignListener;
    }

    public interface GoogleSignListener {
        void googleLoginSuccess(GoogleSignInAccount acct);

        void googleLoginFail();




    }
}
