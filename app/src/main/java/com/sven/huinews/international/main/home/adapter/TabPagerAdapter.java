package com.sven.huinews.international.main.home.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.response.HomeTab;
import com.sven.huinews.international.main.video.fragment.FirstVideoFragment;

import java.util.List;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fm;
    private List<HomeTab> mHomeTabs;

    public TabPagerAdapter(List<HomeTab> mHomeTabs, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.mHomeTabs = mHomeTabs;
    }

    @Override
    public int getCount() {
        return mHomeTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        return FirstVideoFragment.getInstance(mHomeTabs.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mHomeTabs.get(position).getTabName();
    }


    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        Fragment fragment = (Fragment) object;
        fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }
}
