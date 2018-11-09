package com.sven.huinews.international.main.home.adapter;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by W.mago on 2018/5/19.
 */


public class NewsAdapter extends BaseMultiItemQuickAdapter<NewsInfo, BaseViewHolder> {
    private ViewGroup mAdViewGroup;//广告容器
    private List<View> myAdViews;//当前要显示的AdView
    private HashMap<Integer, NewsInfo> mAdIndex;//广告位置与类型

    public NewsAdapter(List<NewsInfo> data) {
        super(data);
        addItemType(NewsInfo.NEWS_BIG, R.layout.item_news_big);
        addItemType(NewsInfo.NEWS_SMELL, R.layout.item_news_smell);
        addItemType(NewsInfo.AD_ONE, R.layout.item_videos_ad);
        myAdViews = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, final NewsInfo item) {
        switch (helper.getItemViewType()) {
            case NewsInfo.NEWS_BIG:
            case NewsInfo.NEWS_SMELL:
                helper.setText(R.id.tv_item_news_title, item.getTitle())
                        .setText(R.id.tv_item_news_smite, item.getSource())
                        .setText(R.id.tv_item_news_look, CommonUtils.getLikeCount(item.getOpen_browser()));
//                (helper.getView(R.id.ll_newsItem)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mOnItemClickListener.onItem(item);
//                    }
//                });

                if (item.getCover_img().equals("")) {
                    helper.getView(R.id.iv_item_news_image1).setVisibility(View.GONE);
                } else {
                    ((SimpleDraweeView) helper.getView(R.id.iv_item_news_image1))
                            .setImageURI(item.getCover_img());
                }
                break;
            case NewsInfo.AD_ONE:
                //找到广告布局容器  将广告布局容器清空 添加广告view
                mAdViewGroup = helper.getView(R.id.my_ad_view_constraint);
                mAdViewGroup.removeAllViews();
                if (myAdViews.get(item.getAdPosition()).getParent() == null) {
                    mAdViewGroup.addView(myAdViews.get(item.getAdPosition()));
                }
                break;
        }
    }


    /**
     * 添加数据
     * datas 数据
     * isRefresh 是否刷新
     * by w.mago
     */
    public void addData(List<NewsInfo> datas, Boolean isRefresh) {
        Log.d("广告", "adapter addData: " + datas.size());
        if (datas.size() == 0) {
            return;
        }
        if (isRefresh) {
            getData().clear();
        }
        addData(datas);
    }


    /**
     * 放置广告
     */
    public void setAdPostionAndAdType(HashMap<Integer, NewsInfo> datas) {
        Log.d("广告", "setAdPostionAndAdType: datas=" + datas.size());
        mAdIndex = datas;
    }

    /**
     * 添加yahoo广告
     */
    public void addYHAdView(List<View> adViews) {
        if (mAdIndex == null) return;
        Log.d("广告", "addYHAdView: mAdIndex=" + mAdIndex.size());
        int i = 0;
        int x = myAdViews.size();
        for (Map.Entry<Integer, NewsInfo> entry : mAdIndex.entrySet()) {
            if (mData.size() - entry.getKey() < 0) continue;//防止加入数据越界
            if (entry.getValue().getAd_type().equals(Common.AD_TYPE_YAHOO)) {
                Log.d("广告", "yahoo的广告view数量:=" + adViews.size() + ";i的大小" + i);
                if (i < adViews.size()) {
                    myAdViews.add(adViews.get(i));
                    i++;
                    Log.d("广告", "yahoo:x= " + x);
                    entry.getValue().setAdPosition(x);
                    x++;
                    addData((mData.size() - entry.getKey()), entry.getValue());
                }
            }
        }
    }


    /**
     * 添加百度海外版广告
     */
    public void addUDAdView(List<View> adViews) {
        if (mAdIndex == null) return;
        Log.d("广告", "addUDAdView: mAdIndex=" + mAdIndex.size());
        int i = 0;
        int x = myAdViews.size();
        for (Map.Entry<Integer, NewsInfo> entry : mAdIndex.entrySet()) {
            if (mData.size() - entry.getKey() < 0) continue;//防止加入数据越界
            if (entry.getValue().getAd_type().equals(Common.AD_TYPE_UD)) {
                Log.d("广告", "UD的广告view数量:=" + adViews.size() + ";i的大小" + i);
                if (i < adViews.size()) {
                    myAdViews.add(adViews.get(i));
                    i++;
                    Log.d("广告", "UD: x=" + x);
                    entry.getValue().setAdPosition(x);
                    x++;
                    addData((mData.size() - entry.getKey()), entry.getValue());
                }
            }
        }
    }


    /**
     * 移除广告
     */
    public void removeAds() {
        if (myAdViews == null || mData == null) return;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getIs_ad() == 1) {
                remove(i);
                i--;
            }
        }

        for (View view : myAdViews) {
            view.destroyDrawingCache();
        }
        myAdViews.clear();
    }

    /**
     * 清除所有数据
     */
    public void clearDatas() {
        removeAds();
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
        if (mAdIndex != null) mAdIndex.clear();
    }

    /*****
     * 点击事件处理item itemchiled
     * @param
     */
    private NewsViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(NewsViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface NewsViewItemClickListener {
        void onItem(NewsInfo item);
    }

}
