package com.sven.huinews.international.main.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.event.LoginSucceedEvent;
import com.sven.huinews.international.main.login.fragment.LoginFragment;
import com.sven.huinews.international.main.login.fragment.RegisterFragment;
import com.sven.huinews.international.utils.ActivityManager;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private ImageView login_close_iv;
    private TabLayout login_tab_layout;
    private ViewPager login_view_pager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private boolean isLogout;

    private int otherLogin = 0;

    @Override

    public int getLayoutId() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isLogout = bundle.getBoolean(Constant.BUNDLE_TO_LOGIN_FROM_MAIN);
        }
        ActivityManager.getInstance().pushOneActivity(this);
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isLogout = bundle.getBoolean(Constant.BUNDLE_TO_LOGIN_FROM_MAIN);
            otherLogin = bundle.getInt(Constant.OTHER_LOGIN, 0);
        }

        login_close_iv = findViewById(R.id.login_close_iv);
        login_tab_layout = findViewById(R.id.login_tab_layout);
        login_view_pager = findViewById(R.id.login_view_pager);
        login_tab_layout.post(new Runnable() {
            @Override
            public void run() {
                CommonUtils.setIndicator(login_tab_layout, 60, 60);
            }
        });

        TabLayout.Tab tabLogin = login_tab_layout.newTab();
        tabLogin.setText(getString(R.string.log_in));
        TabLayout.Tab tabRegister = login_tab_layout.newTab();
        tabRegister.setText(getString(R.string.sign_up));
        login_tab_layout.addTab(tabLogin);
        login_tab_layout.addTab(tabRegister);
        mTabTitles.add(getString(R.string.log_in));
        mTabTitles.add(getString(R.string.sign_up));

        LoginFragment loginFragment = new LoginFragment();

        Bundle loginBundle = new Bundle();
        loginBundle.putInt(Constant.OTHER_LOGIN, otherLogin);
        loginFragment.setArguments(loginBundle);

        RegisterFragment registerFragment = new RegisterFragment();

        Bundle registerBundle = new Bundle();
        registerBundle.putInt(Constant.OTHER_LOGIN, otherLogin);
        registerFragment.setArguments(registerBundle);

        mFragments.add(loginFragment);
        mFragments.add(registerFragment);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments);
        login_view_pager.setAdapter(myPagerAdapter);
        login_tab_layout.setupWithViewPager(login_view_pager);

        setSelectDefTab(0);
    }

    @Override
    public void initEvents() {

        login_close_iv.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == login_close_iv) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                ToastUtils.showShort(mContext, getResources().getString(R.string.click_once_more_to_exit));
//                exitTime = System.currentTimeMillis();
//            }
            finish();
        }

    }

    @Override
    public void initObject() {


    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragments = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles.get(position);
        }
    }

    private void setSelectDefTab(int index) {
        login_view_pager.setCurrentItem(index);
        login_tab_layout.getTabAt(index).select();
    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isLogout) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    ToastUtils.showShort(mContext, mContext.getString(R.string.click_once_more_to_exit));
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSucceedEvent(LoginSucceedEvent event) {
        LoginActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f instanceof LoginFragment) {
                f.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
