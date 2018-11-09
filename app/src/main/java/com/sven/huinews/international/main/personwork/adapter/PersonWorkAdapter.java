package com.sven.huinews.international.main.personwork.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by sfy. on 2018/9/18 0018.
 */

public class PersonWorkAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    private FragmentManager fm;

    public PersonWorkAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
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
}
