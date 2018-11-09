package com.sven.huinews.international.main.fansandfollow.activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.entity.FansAndFollow;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.requst.FansAndFollowRequest;
import com.sven.huinews.international.main.advert.model.AdvertModel;
import com.sven.huinews.international.main.advert.presenter.AdvertPresenter;
import com.sven.huinews.international.main.fansandfollow.adapter.FansAndFollowAdapter;
import com.sven.huinews.international.main.fansandfollow.contract.FansAndFollowContract;
import com.sven.huinews.international.main.fansandfollow.model.FansAndFollowModel;
import com.sven.huinews.international.main.fansandfollow.presenter.FansAndFollowPresenter;
import com.sven.huinews.international.main.userdetail.activity.UserDetailedDataActivity;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.StatusBarUtils;
import com.sven.huinews.international.utils.statusbar.Eyes;
import com.sven.huinews.international.view.MyRefreshLayout;

import java.util.List;

public class FansAndFollowActivity extends BaseActivity<FansAndFollowPresenter, FansAndFollowModel> implements FansAndFollowContract.View, View.OnClickListener {
    private ImageView img_output;
    private TextView tv_title;
    private MyRefreshLayout refresh_layout;
    private RecyclerView rv_fans_follow;
    private RelativeLayout no_follow_data_layout;
    private String du_type;
    private String other_id;
    private String load_type;
    private FansAndFollowAdapter adapter;
    private int page = 1;
    private int pagesize = 20;
    private boolean isRefresh = true;
    private List<FansAndFollow.DataBean> mDatas;
    private TextView tv_no_data;


    @Override
    public int getLayoutId() {
        du_type = getIntent().getStringExtra("du_type");
        other_id = getIntent().getStringExtra("other_id");
        load_type = getIntent().getStringExtra("load_type");
        return R.layout.activity_fans_and_follow;
    }

    @Override
    public void initView() {
        img_output = findViewById(R.id.img_output);
        tv_title = findViewById(R.id.tv_title);
        refresh_layout = findViewById(R.id.refresh_layout);
        refresh_layout.setEnableLoadmoreWhenContentNotFull(false);
        refresh_layout.setEnableNestedScroll(true);
        rv_fans_follow = findViewById(R.id.rv_fans_follow);
        no_follow_data_layout = findViewById(R.id.no_follow_data_layout);
        tv_no_data = findViewById(R.id.tv_no_data);

        adapter = new FansAndFollowAdapter(R.layout.item_fans_and_follow);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_fans_follow.setLayoutManager(linearLayoutManager);
        rv_fans_follow.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_item) {
                    //跳转
                    UserDetailedDataActivity.toThis(mContext, mDatas.get(position).getDu_type(), mDatas.get(position).getUser_id() + "");
                }
            }
        });
    }

    @Override
    public void initEvents() {
        img_output.setOnClickListener(this);
        no_follow_data_layout.setOnClickListener(this);

        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                isRefresh = true;
                netWork(page);
            }
        });

        refresh_layout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                isRefresh = false;
                netWork(page);
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        if (v.getId() == R.id.img_output) {
            finish();
        }else if (v.getId() == R.id.no_follow_data_layout){
            refresh_layout.autoRefresh();
        }
    }

    @Override
    public void initObject() {
        setMVP();
        refresh_layout.autoRefresh();
        StatusBarUtils.setColor(FansAndFollowActivity.this, Color.parseColor("#FFFFFF"));
        netWork(0);
        if (load_type.equals("follow")) {
            tv_title.setText(getString(R.string.follow));
        } else if (load_type.equals("fans")) {
            tv_title.setText(getString(R.string.fans));
        }
    }

    private void netWork(int pages) {
        if (load_type.equals("follow")) {
            mPresenter.onFollowList(new FansAndFollowRequest(other_id, Integer.parseInt(du_type), pages, pagesize), isRefresh);
        } else if (load_type.equals("fans")) {
            mPresenter.onFansList(new FansAndFollowRequest(other_id, Integer.parseInt(du_type), pages, pagesize), isRefresh);
        }
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
    public void setFansAndFollowList(List<FansAndFollow.DataBean> mDatas, boolean isRefresh) {
        if (refresh_layout != null) {
            refresh_layout.finishRefresh();
            refresh_layout.finishLoadmore();
        }

        if (isRefresh) {
            adapter.clearDatas();
            this.mDatas = mDatas;
        } else {
            this.mDatas.addAll(mDatas);
        }

        if (mDatas != null && mDatas.size() > 0) {
            adapter.addData(mDatas);
            no_follow_data_layout.setVisibility(View.GONE);
            refresh_layout.setVisibility(View.VISIBLE);
        } else {
            if (isRefresh) {

                if (load_type.equals("follow")) {
                    tv_no_data.setText("");
                }

                no_follow_data_layout.setVisibility(View.VISIBLE);
                refresh_layout.setVisibility(View.GONE);
            }

        }


    }
}
