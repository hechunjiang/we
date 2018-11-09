package com.sven.huinews.international.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.TUtil;
import com.sven.huinews.international.view.EmptyLayout;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by sfy. on 2018/5/7 0007.
 */

public abstract class BaseFragment<P extends BasePresenter, M extends BaseModel> extends Fragment implements View.OnClickListener {

    public Context mContext;
    public P mPresenter;
    public M mModel;
    protected View mView;
    protected int PAGE = 1;
    protected int LIMIT = 20;
    protected boolean isRefresh = true;
    protected EmptyLayout emptyLayout;


    private boolean isViewCreated;  //Fragment的View加载完毕的标记
    private boolean isUIVisible;    //Fragment对用户可见的标记
    private Dialog mLoadingDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutResource(), container, false);
        }
        this.mContext = getActivity();
        initView(mView);
        initObject();
        initEvents();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingDialog = new Dialog(getActivity(), R.style.dialog);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }


    private void lazyLoad() {
        if (isViewCreated && isUIVisible) {
            loadData();
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    public void openActivity(Class targetClass) {
        Intent intent = new Intent(getActivity(), targetClass);
        startActivity(intent);
    }
    public void showBaseLoading(String msg) {
        mLoadingDialog.show();
    }



    public void hideBaseLoading() {
        if (mLoadingDialog == null) {
            return;
        }
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 初始化MVP
     */
    public void setMVP() {
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this.getActivity();
        }
        mPresenter.setVM(this, mModel);
    }


    /**
     * 获取布局文件
     */

    protected abstract int getLayoutResource();

    /**
     * 初始化事物
     */

    public abstract void initObject();

    /**
     * 懒加载数据
     */
    protected abstract void loadData();

    /**
     * 初始化view
     */
    protected abstract void initView(View v);

    /**
     * 事件监听
     */
    public abstract void initEvents();

    /**
     * 处理监听
     *
     * @param v
     */
    public abstract void OnClickEvents(View v);

    @Override
    public void onClick(View v) {
        OnClickEvents(v);
    }

/*********************跳转相关**********************************/
    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        isUIVisible = false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getTag());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getTag());
    }
}
