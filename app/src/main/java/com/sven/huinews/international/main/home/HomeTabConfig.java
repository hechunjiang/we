package com.sven.huinews.international.main.home;

import android.content.Context;

import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.response.HomeTab;

import java.util.ArrayList;
import java.util.List;

public class HomeTabConfig {

    public static List<HomeTab> getHomeTabs(Context mContext) {
        String[] names = {mContext.getResources().getString(R.string.videos), mContext.getResources().getString(R.string.stories), mContext.getResources().getString(R.string.news)};
        int[] type = {2, 1, 3};
        List<HomeTab> mTomeTabs = new ArrayList<>();
        mTomeTabs.add(new HomeTab(names[0], type[0]));
        mTomeTabs.add(new HomeTab(names[1], type[1]));
        mTomeTabs.add(new HomeTab(names[2], type[2]));
        return mTomeTabs;
    }

    public static List<HomeTab> getUserVideoTabs(Context mContext) {
        String[] names = {mContext.getResources().getString(R.string.videos), mContext.getResources().getString(R.string.likes)};
        int[] type = {2, 1};
        List<HomeTab> mTomeTabs = new ArrayList<>();
        mTomeTabs.add(new HomeTab(names[0], type[0]));
        mTomeTabs.add(new HomeTab(names[1], type[1]));
        return mTomeTabs;
    }

    public static List<HomeTab> getMyVideoTabs(Context mContext) {
        String[] names = {mContext.getResources().getString(R.string.videos), mContext.getResources().getString(R.string.likes),mContext.getResources().getString(R.string.draft)};
        int[] type = {2, 1,3};
        List<HomeTab> mTomeTabs = new ArrayList<>();
        mTomeTabs.add(new HomeTab(names[0], type[0]));
        mTomeTabs.add(new HomeTab(names[1], type[1]));
        mTomeTabs.add(new HomeTab(names[2], type[2]));
        return mTomeTabs;
    }
}
