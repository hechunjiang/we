package com.sven.huinews.international.base;

/**
 * auther: sunfuyi
 * data: 2018/5/12
 * effect:
 */
public interface BaseView {
    void showLoading();

    void hideLoading();

    void showErrorTip(int code, String msg);

}
