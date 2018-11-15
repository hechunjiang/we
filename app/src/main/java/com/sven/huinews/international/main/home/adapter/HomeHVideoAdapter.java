package com.sven.huinews.international.main.home.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dueeeke.videoplayer.listener.VideoListener;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.main.video.listener.ProgressBarListener;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.video.controller.HorizontalVideoController;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Burgess on 2018/9/30 0030.
 */
public class HomeHVideoAdapter extends BaseMultiItemQuickAdapter<MyNews, BaseViewHolder> {
    private List<View> myAdViews;//当前要显示的AdView
    private ViewGroup mAdViewGroup;//广告容器
    private HashMap<Integer, MyNews> mAdIndex;//广告位置与类型

    public HomeHVideoAdapter(List<MyNews> data) {
        super(data);
        addItemType(MyNews.H_VIDEO, R.layout.h_video_list_item);
        addItemType(MyNews.V_VIDEO, R.layout.v_video_list_item);
        addItemType(MyNews.AD_VIDEO, R.layout.item_videos_ad);
        myAdViews = new ArrayList<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyNews item) {
        switch (helper.getItemViewType()) {
            case MyNews.H_VIDEO:
                helper.setText(R.id.tv_user_name, item.getUserName());
                helper.setText(R.id.tv_video_dis, item.getTitle().equals("") ? "" : item.getTitle());

                if (!TextUtils.isEmpty(item.getUserIcon())) {
                    ((SimpleDraweeView) helper.getView(R.id.user_head)).setImageURI(Uri.parse(item.getUserIcon()));
                }

                helper.setImageResource(R.id.iv_like, item.isLike() ? R.mipmap.icon_item_liekd : R.mipmap.icon_item_like);
                helper.setText(R.id.tv_like, CommonUtils.getLikeCount(item.getLikeCount()))
                        .setTextColor(R.id.tv_like, item.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.c999999))
                        .setText(R.id.tv_msg, CommonUtils.getLikeCount(item.getCommentCount()))
                        .setText(R.id.tv_share, CommonUtils.getLikeCount(item.getShareCount()));
                final IjkVideoView videoPlayer = ((IjkVideoView) helper.getView(R.id.video_player));

                PlayerConfig mPlayerConfig = new PlayerConfig.Builder().enableCache()./*setLooping().*/addToPlayerManager().build();
                final HorizontalVideoController mController = new HorizontalVideoController(mContext);
                videoPlayer.setPlayerConfig(mPlayerConfig);
                videoPlayer.setUrl(item.getVideoUrl());
                videoPlayer.setVideoController(mController);
                videoPlayer.setVideoListener(new VideoListener() {
                    @Override
                    public void onVideoStarted() {
                        //启动
                        Log.e("videoPlayer", "onVideoStarted");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onVideoStarted(videoPlayer, item);
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
                            mOnVideoPlayStatusLisenter.onPrepared(videoPlayer, item);
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
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onInfo();
                        }
                    }
                });

                mController.getThumb().setImageURI(Uri.parse(item.getCoverUrl()));
                mController.setVideoInfo(item.getDuration());

                mController.setProgressBarListener(new ProgressBarListener() {
                    @Override
                    public void onMove() {
                        Log.e("videoPlayer", "onMove");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onMove();
                        }
                    }

                    @Override
                    public void onBuffering() {
                        Log.e("videoPlayer", "onBuffering");
                    }

                    @Override
                    public void onPreparing() {
                        Log.e("videoPlayer", "onPreparing");
                    }

                    @Override
                    public void onProgressCompletion() {
                        Log.e("videoPlayer", "onProgressCompletion");
                    }

                    @Override
                    public void onProgress(int position, int duration) {
                        if (duration>0){
                            mController.setVideoInfo(duration - position);
                        }
                    }

                    @Override
                    public void onStop() {
                        Log.e("videoPlayer", "onStop1111");
                        if (mOnVideoPlayStatusLisenter != null) {
                            mOnVideoPlayStatusLisenter.onStop(item);
                        }
                    }
                });

                helper.getView(R.id.user_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onHead(item);
                        }
                    }
                });

                helper.getView(R.id.tv_user_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onHead(item);
                        }
                    }
                });

                helper.getView(R.id.btn_msg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//评论
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onComment(item, (TextView) helper.getView(R.id.tv_msg));
                        }
                    }
                });

                helper.getView(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onShare(item, helper.getPosition());//helper.getAdapterPosition()  helper.getPosition()
                    }
                });

                helper.getView(R.id.btn_comment).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            //视频点赞回调
                            mOnItemClickListener.onLike(item, (ImageView) helper.getView(R.id.iv_like), (TextView) helper.getView(R.id.tv_like), helper.getPosition(), (IjkVideoView) helper.getView(R.id.video_player));
                        }
                    }
                });
                break;
            case MyNews.V_VIDEO:

                /*HttpProxyCacheServer proxy = VideoCacheManager.getProxy(mContext);
                proxy.getProxyUrl(item.getVideoUrl());*/


                Drawable liked = mContext.getResources().getDrawable(R.mipmap.icon_item_liekd);
                Drawable like = mContext.getResources().getDrawable(R.mipmap.icon_item_like);
                liked.setBounds(0, 0, liked.getMinimumWidth(), liked.getMinimumHeight());
                like.setBounds(0, 0, like.getMinimumWidth(), like.getMinimumHeight());

                helper.setText(R.id.tv_like, CommonUtils.getLikeCount(item.getLikeCount()));
                helper.setText(R.id.tv_video_dis, item.getTitle().equals("") ? "" : item.getTitle());
                helper.setText(R.id.tv_user_name, item.getUserName());
                ((SimpleDraweeView) helper.getView(R.id.user_head)).setImageURI(Uri.parse(TextUtils.isEmpty(item.getUserIcon()) ? "" : item.getUserIcon()));
                //((SimpleDraweeView) helper.getView(R.id.video_img)).setImageURI(Uri.parse(TextUtils.isEmpty(item.getCoverUrl()) ? "" : item.getCoverUrl()));
                ImageView iv_bg = helper.getView(R.id.video_img1);
                RequestOptions options = new RequestOptions();
                options.centerCrop()
                        .placeholder(R.drawable.def_image);


                Glide.with(mContext).load(item.getCoverUrl()).apply(options).into(iv_bg);

                helper.getView(R.id.user_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onHead(item);
                        }
                    }
                });

                helper.getView(R.id.video_img1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onClick(item, helper.getPosition());
                        }
                    }
                });

                break;
            case MyNews.AD_VIDEO:
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
     * 添加google广告
     */
    public void addGoogleAdView(List<View> adViews) {
        if (mAdIndex == null) return;
        Log.d("广告", "addGoogleAdView: mAdIndex=" + mAdIndex.size());
        int i = 0;
        int x = myAdViews.size();
        for (Map.Entry<Integer, MyNews> entry : mAdIndex.entrySet()) {
            if (mData.size() - entry.getKey() < 0) continue;//防止加入数据越界
            if (entry.getValue().getAd_type().equals(Common.AD_TYPE_GOOGLE)) {
                Log.d("广告", "GoogleAd的广告view数量:=" + adViews.size() + ";i的大小" + i);
                if (i < adViews.size()) {
                    MobclickAgent.onEvent(mContext, Common.AD_TYPE_GOOGLE_NATIVE_LOOK_UNIFIED_NATIVEW_AD);
                    MobclickAgent.onEvent(mContext, Common.AD_TYPE_GOOGLE_NATIVE_LOOK_UNIFIED_NATIVEW_AD, "ad_type_google_native_look_unified_nativew_ad");
                    myAdViews.add(adViews.get(i));
                    i++;
                    Log.d("广告", "GoogleAd:x= " + x);
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
        for (Map.Entry<Integer, MyNews> entry : mAdIndex.entrySet()) {
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
     * 刷新item
     */
    public void notifyOneChange(MyNews data) {
        int index = mData.indexOf(data);
        if (index < 0) {
            return;
        }
        mData.get(index).setLike(data.isLike());
        mData.get(index).setLikeCount(data.getLikeCount());
        mData.get(index).setSaveCount(data.getSaveCount());
        mData.get(index).setCommentCount(data.getCommentCount());
        notifyItemChanged(index);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * datas 数据
     * isRefresh 是否刷新
     * by w.mago
     */
    public void addData(List<MyNews> datas, Boolean isRefresh) {
        Log.d("广告", "adapter addData: " + datas.size());
        if (datas.size() == 0) {
            return;
        }
        if (isRefresh) {
            getData().clear();
        }
        addData(datas);
    }

    private onVideoPlayStatusLisenter mOnVideoPlayStatusLisenter;

    public void setmOnVideoPlayStatusLisenter(onVideoPlayStatusLisenter mOnVideoPlayStatusLisenter) {
        this.mOnVideoPlayStatusLisenter = mOnVideoPlayStatusLisenter;
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

        void onInfo();

        void onStop(MyNews item);
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
        void onClick(MyNews data, int position);

        void onComment(MyNews news, TextView tv_msg);//评论

        void onLike(MyNews news, ImageView ivLike, TextView tvLike, int position, IjkVideoView videoPlayer);//视频点赞

        void onHead(MyNews news);

        void onShare(MyNews item, int pos);
    }

}
