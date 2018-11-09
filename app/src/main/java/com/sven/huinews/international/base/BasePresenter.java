package com.sven.huinews.international.base;

import android.content.Context;

/**
 * auther: sunfuyi
 * data: 2018/5/12
 * effect:
 */
public class BasePresenter<V, M> {
    public Context mContext;
    public M mModel;
    public V mView;

    public void setVM(V v, M m) {
        this.mView = v;
        this.mModel = m;
    }
}
