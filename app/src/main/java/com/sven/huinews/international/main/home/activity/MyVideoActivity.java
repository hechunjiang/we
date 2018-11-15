package com.sven.huinews.international.main.home.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.main.home.fragment.MyVideoFragment;
import com.sven.huinews.international.utils.StatusBarUtils;
import com.sven.huinews.international.utils.statusbar.Eyes;

public class MyVideoActivity extends BaseActivity {
    private FrameLayout frame_video;
    private ImageView back_iv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_myvideo;
    }

    @Override
    public void initView() {
        frame_video = findViewById(R.id.frame_video);
        back_iv = findViewById(R.id.back_iv);
        initFragment();
    }

    @Override
    public void initEvents() {
        back_iv.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if(v == back_iv){
            finish();
        }
    }

    @Override
    public void initObject() {
        StatusBarUtils.setColor(MyVideoActivity.this, Color.parseColor("#FFFFFF"));
    }

    private void initFragment(){
        MyVideoFragment fragment = new MyVideoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_video,fragment)
                .commitAllowingStateLoss();
    }
}
