package wedemo.activity.down;

import android.view.View;

import com.sven.huinews.international.R;

import java.util.List;

import wedemo.base.BaseActivity;

public class DownActivity extends BaseActivity<DownPresenter, DownModel> implements DownContract.View {
    private int type = 1;

    @Override
    public void setSDKCategroy(List<CategoryResponse.DataBean> mDatas) {

    }

    @Override
    public void setSdkDown(List<DownResponse.DataBean> mDatas) {

    }

    @Override
    public DownRequest getDownRequest() {
        DownRequest downRequest = new DownRequest();
        downRequest.setType(type);
        return downRequest;
    }

    @Override
    public void onDownLoadFinish(DownResponse.DataBean dataBean) {

    }

    @Override
    public int getLayoutId() {
        type = getIntent().getIntExtra("type", 1);
        return R.layout.activity_down_sdk;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void initObject() {
        setMVP();
        mPresenter.onSDKCategroy(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {

    }
}
