package com.sven.huinews.international.main.web;

import com.sven.huinews.international.base.BaseView;
import com.sven.huinews.international.entity.response.ShareResponse;

/**
 * Created by Sven on 2018/2/1.
 */

public interface JsWebView extends BaseView {
    void onBindWxSucceed(int count);
    void onShareInfo(ShareResponse shareResponse);
    void onDisLikeVideo();
}
