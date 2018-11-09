package com.sven.huinews.international.main.home.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dueeeke.videoplayer.listener.VideoListener;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.main.video.listener.ProgressBarListener;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.video.controller.HorizontalVideoController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeVVideoAdapter extends RecyclerView.Adapter {
    public static final int TYPE_H_VIDEO = 0; //横屏
    public static final int TYPE_V_VIDEO = 1; //竖屏
    public static final int TYPE_AD = 2;//ad
    private List<MyNews> mDatas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int viewType;
    private HashMap<Integer, MyNews> mAdIndex;//广告位置与类型
    private List<View> myAdViews;//当前要显示的AdView
    private ViewGroup mAdViewGroup;//广告容器

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public HomeVVideoAdapter(Context mContext, int layout) {
        this.mContext = mContext;
        this.viewType = layout;
        mInflater = LayoutInflater.from(mContext);
        myAdViews = new ArrayList<>();
    }

    public void setList(List<MyNews> list, boolean isRefresh) {
        if (list == null && list.size() == 0) {
            return;
        }
        if (isRefresh) {
            mDatas.clear();
        }
        mDatas.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        View view;
        switch (viewType) {
            case TYPE_H_VIDEO:
                view = mInflater.inflate(R.layout.h_video_list_item, parent, false);
                vh = new H_VH(view);
                break;
            case TYPE_V_VIDEO:
                view = mInflater.inflate(R.layout.v_video_list_item, parent, false);
                vh = new V_VH(view);
                break;
            case TYPE_AD:
                view = mInflater.inflate(R.layout.item_videos_ad,parent,false);
                vh = new ad_VH(view);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyNews item = mDatas.get(position);
        switch (viewType) {
            case TYPE_H_VIDEO:
                final H_VH vh = (H_VH) holder;
//                final Drawable liked = mContext.getResources().getDrawable(R.mipmap.icon_item_liekd);
//                final Drawable like = mContext.getResources().getDrawable(R.mipmap.icon_item_like);
                vh.userName.setText(item.getUserName());
                vh.videoDis.setText(item.getTitle().equals("") ? "" : item.getTitle());
                vh.userHead.setImageURI(Uri.parse(item.getUserIcon()));
                vh.ivLike.setImageResource(item.isLike() ? R.mipmap.icon_item_liekd : R.mipmap.icon_item_like);
                vh.tvLike.setText(CommonUtils.getLikeCount(item.getLikeCount()));
                vh.tvLike.setTextColor(item.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.c999999));
                vh.tv_msg.setText(CommonUtils.getLikeCount(item.getCommentCount()));
                vh.tv_share.setText(CommonUtils.getLikeCount(item.getShareCount()));

                vh.videoPlayer.setPlayerConfig(vh.mPlayerConfig);
                vh.videoPlayer.setUrl(item.getVideoUrl());
                vh.videoPlayer.setVideoController(vh.mController);

                vh.videoPlayer.setVideoListener(new VideoListener() {
                    @Override
                    public void onVideoStarted() {
                        //启动
                        Log.e("videoPlayer", "onVideoStarted");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onVideoStarted(vh.videoPlayer, item);
                        }
                    }

                    @Override
                    public void onVideoPaused() {
                        //暂停的
                        Log.e("videoPlayer", "onVideoPaused");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onVideoPaused();
                        }
                    }

                    @Override
                    public void onComplete() {
                        //完成
                        Log.e("videoPlayer", "onComplete");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onComplete(item);
                        }
                    }

                    @Override
                    public void onPrepared() {
                        //准备
                        Log.e("videoPlayer", "onPrepared");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onPrepared(vh.videoPlayer, item);
                        }
                    }

                    @Override
                    public void onError() {
                        //错误
                        Log.e("videoPlayer", "onError");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onError();
                        }
                    }

                    @Override
                    public void onInfo(int what, int extra) {
                        Log.e("videoPlayer", "onInfo");
                    }
                });

                vh.mController.getThumb().setImageURI(Uri.parse(item.getCoverUrl()));
                vh.mController.setVideoInfo(item.getDuration());

                vh.mController.setProgressBarListener(new ProgressBarListener() {
                    @Override
                    public void onMove() {
                        Log.e("videoPlayer", "onMove");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onMove();
                        }
                    }
                    @Override
                    public void onBuffering() {
                        LogUtil.showLog("onStop");
                    }

                    @Override
                    public void onPreparing() {

                    }

                    @Override
                    public void onProgressCompletion() {

                    }

                    @Override
                    public void onProgress(int position, int duration) {

                    }

                    @Override
                    public void onStop() {

                    }
                });


                vh.userHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onHead(item);
                        }
                    }
                });


                vh.btn_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//评论
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onComment(item, vh.tv_msg);
                        }
                    }
                });


                vh.btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onShare(item, position);
                    }
                });

                vh.btn_comment.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            //视频点赞回调
                            mOnItemClickListener.onLike(item, vh.ivLike, vh.tvLike, position);
                        }
                    }
                });


                break;
            case TYPE_V_VIDEO:
                V_VH vh1 = (V_VH) holder;
                Drawable liked = mContext.getResources().getDrawable(R.mipmap.icon_item_liekd);
                Drawable like = mContext.getResources().getDrawable(R.mipmap.icon_item_like);
                liked.setBounds(0, 0, liked.getMinimumWidth(), liked.getMinimumHeight());
                like.setBounds(0, 0, like.getMinimumWidth(), like.getMinimumHeight());


                vh1.tv_like.setText(CommonUtils.getLikeCount(item.getLikeCount()));
//                vh1.tv_like.setCompoundDrawables(item.isLike() ? liked : like, null, null, null);
//                vh1.tv_like.setTextColor(item.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.color_line));

                vh1.user_head.setImageURI(Uri.parse(item.getUserIcon()));
                vh1.video_img.setImageURI(Uri.parse(item.getCoverUrl()));
                vh1.tv_video_dis.setText(item.getTitle().equals("") ? "" : item.getTitle());
                vh1.tv_user_name.setText(item.getUserName());
                vh1.user_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onHead(item);
                        }
                    }
                });

                vh1.video_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onClick(item);
                        }
                    }
                });
                break;
            case TYPE_AD:
                ad_VH ad_vh = (ad_VH)holder;
                //找到广告布局容器  将广告布局容器清空 添加广告view
                mAdViewGroup = ad_vh.my_ad_view_constraint;
                mAdViewGroup.removeAllViews();
                if (myAdViews.get(item.getAdPosition()).getParent() == null) {
                    mAdViewGroup.addView(myAdViews.get(item.getAdPosition()));
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (viewType == TYPE_V_VIDEO) {
            type = TYPE_V_VIDEO;
        } else {
            type = TYPE_H_VIDEO;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class V_VH extends RecyclerView.ViewHolder {
        SimpleDraweeView video_img;
        TextView tv_like;
        TextView tv_video_dis;
        SimpleDraweeView user_head;
        TextView tv_user_name;

        public V_VH(View v) {
            super(v);
            video_img = v.findViewById(R.id.video_img);
            tv_like = v.findViewById(R.id.tv_like);
            tv_video_dis = v.findViewById(R.id.tv_video_dis);
            user_head = v.findViewById(R.id.user_head);
            tv_user_name = v.findViewById(R.id.tv_user_name);

        }
    }

    class H_VH extends RecyclerView.ViewHolder {
        TextView userName;
        TextView videoDis;
        SimpleDraweeView userHead;
        ImageView ivLike;
        TextView tvLike;
        TextView tv_msg;
        TextView tv_share;
        LinearLayout btn_msg, btn_comment, btn_share;
        IjkVideoView videoPlayer;
        PlayerConfig mPlayerConfig;
        private HorizontalVideoController mController;

        public H_VH(View v) {
            super(v);
            userName = v.findViewById(R.id.tv_user_name);
            videoDis = v.findViewById(R.id.tv_video_dis);
            userHead = v.findViewById(R.id.user_head);
            ivLike = v.findViewById(R.id.iv_like);
            tvLike = v.findViewById(R.id.tv_like);
            tv_msg = v.findViewById(R.id.tv_msg);
            tv_share = v.findViewById(R.id.tv_share);
            videoPlayer = v.findViewById(R.id.video_player);
            btn_msg = v.findViewById(R.id.btn_msg);
            btn_comment = v.findViewById(R.id.btn_comment);
            btn_share = v.findViewById(R.id.btn_share);
            mController = new HorizontalVideoController(mContext);
            mPlayerConfig = new PlayerConfig.Builder().enableCache()./*setLooping().*/addToPlayerManager().build();
        }

    }

    class ad_VH extends RecyclerView.ViewHolder{
        FrameLayout my_ad_view_constraint;
        public ad_VH(View itemView) {
            super(itemView);
            my_ad_view_constraint = itemView.findViewById(R.id.my_ad_view_constraint);
        }
    }


    /**
     * 放置广告
     */
    public void setAdPostionAndAdType(HashMap<Integer, MyNews> datas) {
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
        for (Map.Entry<Integer, MyNews> entry : mAdIndex.entrySet()) {
            if (mDatas.size() - entry.getKey() < 0) continue;//防止加入数据越界
            if (entry.getValue().getAd_type().equals(Common.AD_TYPE_YAHOO)) {
                Log.d("广告", "yahoo的广告view数量:=" + adViews.size() + ";i的大小" + i);
                if (i < adViews.size()) {
                    myAdViews.add(adViews.get(i));
                    i++;
                    Log.d("广告", "yahoo:x= " + x);
                    entry.getValue().setAdPosition(x);
                    x++;
                    mDatas.add((mDatas.size() - entry.getKey()), entry.getValue());
                    notifyDataSetChanged();
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
        for (Map.Entry<Integer, MyNews> entry : mAdIndex.entrySet()) {
            if (mDatas.size() - entry.getKey() < 0) continue;//防止加入数据越界
            if (entry.getValue().getAd_type().equals(Common.AD_TYPE_UD)) {
                Log.d("广告", "UD的广告view数量:=" + adViews.size() + ";i的大小" + i);
                if (i < adViews.size()) {
                    myAdViews.add(adViews.get(i));
                    i++;
                    Log.d("广告", "UD: x=" + x);
                    entry.getValue().setAdPosition(x);
                    x++;
                    mDatas.add((mDatas.size() - entry.getKey()), entry.getValue());
                    notifyDataSetChanged();
                }
            }
        }
    }



    /*****
     * 点击事件处理item itemchiled
     * @param
     */
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(MyNews data);

        void onComment(MyNews news, TextView tv_msg);//评论

        void onLike(MyNews news, ImageView ivLike, TextView tvLike, int position);//视频点赞

        void onHead(MyNews news);

        void onShare(MyNews item, int pos);
    }


    public interface onVideoPlayStatusLisenter {
        void onVideoStarted(IjkVideoView videoPlayer, MyNews item);

        void onVideoPaused();

        //播放完成
        void onComplete(MyNews item);

        //准备完成
        void onPrepared(IjkVideoView videoPlayer, MyNews item);

        void onError();

        void onMove();
    }


    private onVideoPlayStatusLisenter mOnVideoPlayStatusLisenter;

    public void setmOnVideoPlayStatusLisenter(onVideoPlayStatusLisenter mOnVideoPlayStatusLisenter) {
        this.mOnVideoPlayStatusLisenter = mOnVideoPlayStatusLisenter;
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 刷新item
     *
     * @param mData
     */
    public void notifyOneChange(MyNews mData) {
        int index = mDatas.indexOf(mData);
        if (index < 0) {
            return;
        }
        mDatas.get(index).setLike(mData.isLike());
        mDatas.get(index).setLikeCount(mData.getLikeCount());
        mDatas.get(index).setSaveCount(mData.getSaveCount());
        mDatas.get(index).setCommentCount(mData.getCommentCount());
        notifyItemChanged(index);
    }
}
