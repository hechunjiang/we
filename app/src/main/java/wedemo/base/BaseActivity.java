package wedemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import wedemo.utils.AppManager;
import wedemo.utils.TUtil;

public abstract class BaseActivity<P extends BasePresenter, M extends BaseModel> extends AppCompatActivity implements View.OnClickListener {
    protected Context mContext;
    public P mPresenter;
    public M mModel;
    protected boolean isRefresh;
    protected int LIMIT = 20;
    protected int PAGE = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
        setContentView(getLayoutId());
        //把当前初始化的activity加入栈中
        AppManager.getInstance().addActivity(this);
        mContext = this;
        initView();
        initObject();
        initEvents();

    }
    /**
     * 初始化MVP
     */
    @SuppressWarnings("unchecked")
    public void setMVP() {
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        mPresenter.setVM(this, mModel);
    }
    /**
     * 获取布局文件
     */
    public abstract int getLayoutId();


    /**
     * 初始化View
     */
    public abstract void initView();

    /**
     * 设置监听
     */
    public abstract void initEvents();

    /**
     * 处理监听事件
     */
    public abstract void onClickEvent(View v);

    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }
    /**
     * 初始化事物
     */

    public abstract void initObject();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        //AppManager.getInstance().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        Activity activity = AppManager.getInstance().currentActivity();

        super.onBackPressed();
    }
}
