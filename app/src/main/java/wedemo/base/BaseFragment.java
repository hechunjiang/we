package wedemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public Context mContext;
    protected View mView;

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

}
