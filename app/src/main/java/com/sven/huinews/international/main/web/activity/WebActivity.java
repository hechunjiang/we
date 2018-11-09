package com.sven.huinews.international.main.web.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.config.Constant;

public class WebActivity extends BaseActivity {
    private BridgeWebView mWebView;




    public static void toThis(Context mContext, String url) {
        Intent intent = new Intent(mContext, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BUNDLE_TO_WEB_URL, url);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        mWebView = findViewById(R.id.webview);
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void initObject() {

    }
}
