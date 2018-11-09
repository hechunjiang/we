package wedemo.activity;//package com.mobile.wedemo.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.entity.response.TagResponse;
import com.sven.huinews.international.utils.statusbar.Eyes;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import wedemo.contract.TagContract;
import wedemo.model.TagModel;
import wedemo.presenter.TagPresenter;
import wedemo.utils.Constants;
import wedemo.utils.LogUtil;

public class TagActivity extends BaseActivity<TagPresenter, TagModel> implements TagContract.View {
    private ImageButton btn_back;
    private TagFlowLayout flowLayout;
    private TextView btn_next;
    private List<TagResponse.DataBean> selectedTags = new ArrayList<>();
    private List<TagResponse.DataBean> tags;

    @Override
    public int getLayoutId() {
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.c262626));
        return R.layout.activity_tag;
    }

    @Override
    public void initView() {
        btn_back = findViewById(R.id.btn_back);
        flowLayout = findViewById(R.id.taglayout);
        btn_next = findViewById(R.id.btn_next);
    }

    @Override
    public void initEvents() {
        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == btn_back) {
            finish();
        } else if (v == btn_next) {
            Intent intent = new Intent();
            intent.putExtra("Tags", (Serializable) getSelectedTags()); //将计算的值回传回去
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void initObject() {
        setMVP();
        mPresenter.onTagList();
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

    @Override
    public void setTagList(final List<TagResponse.DataBean> mDatas) {
        tags = mDatas;
        flowLayout.setAdapter(new TagAdapter(mDatas) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_list_item, flowLayout, false);
                tv.setText(mDatas.get(position).getTag_name());
                return tv;
            }
        });
    }

    public List<TagResponse.DataBean> getSelectedTags() {
        selectedTags.clear();
        Set<Integer> set = flowLayout.getSelectedList();
        for (Integer i : set) {
            selectedTags.add(tags.get(i));
        }

        LogUtil.showLog("msg---selected:" + selectedTags.toString());
        return selectedTags;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("TagActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("TagActivity");
            MobclickAgent.onPause(this);
        }
    }
}
