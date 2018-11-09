

package com.sven.huinews.international.main.follow.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duapps.ad.entity.strategy.NativeAd;
import com.dueeeke.videoplayer.listener.VideoListener;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.main.follow.video.VerticalVideoController;
import com.sven.huinews.international.main.video.listener.ProgressBarListener;
import com.sven.huinews.international.publicclass.DBPresenter;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.FileUtils;
import com.sven.huinews.international.utils.FrescoUtil;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.MyVideoProgress;

import java.util.ArrayList;
import java.util.List;

import wedemo.utils.ScreenUtils;

public class VideoAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private Activity activity;
    private List<MyNews> mDatas = new ArrayList<>();
    private Drawable mLikedDrawable, mLikeDrawable;


    public VideoAdapter(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.activity = mActivity;
        mLikeDrawable = mContext.getResources().getDrawable(R.mipmap.icon_zan4);
        mLikeDrawable.setBounds(0, 0, mLikeDrawable.getMinimumWidth(), mLikeDrawable.getMinimumHeight());
        mLikedDrawable = mContext.getResources().getDrawable(R.mipmap.icon_zan4ed);
        mLikedDrawable.setBounds(0, 0, mLikedDrawable.getMinimumWidth(), mLikedDrawable.getMinimumHeight());
    }

    public void refreshItem(List<MyNews> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_video_v, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final VH vh = (VH) holder;
        final MyNews myNews = mDatas.get(position);
        if (myNews.getIsAd()==0) {
            vh.right_view.setVisibility(View.VISIBLE);
            vh.tv_collection.setVisibility(View.VISIBLE);
            vh.btn_comment.setVisibility(View.VISIBLE);
            vh.btn_share.setVisibility(View.VISIBLE);
            vh.tv_ad.setVisibility(View.INVISIBLE);
            vh.fl_video.setVisibility(View.VISIBLE);
            vh.ll_ad.setVisibility(View.GONE);
            vh.videoPlayer.setPlayerConfig(vh.mPlayerConfig);
            vh.videoPlayer.setUrl(myNews.getVideoUrl());
            vh.videoPlayer.setVideoController(vh.mVerticalVideoController);

            FrescoUtil.setControllerListener(vh.mVerticalVideoController.getThumb(), myNews.getCoverUrl(),
                    ScreenUtils.getScreenWidth(mContext));
            // FrescoUtil.loadDefImg(vh.mVerticalVideoController.getThumb(), myNews.getCoverUrl());
            vh.userHead.setImageURI(myNews.getUserIcon());
            vh.video_content.setText(myNews.getTitle().equals("") ? "" : myNews.getTitle());
            vh.btn_comment.setText(CommonUtils.getLikeCount(myNews.getCommentCount()));
            vh.btn_share.setText(CommonUtils.getLikeCount(myNews.getShareCount() / 2));
            vh.tv_collection.setText(CommonUtils.getLikeCount(myNews.getLikeCount()));
            vh.user_name.setText(myNews.getUserName());

            setIsColection(myNews, vh.tv_collection);

            //头像点击
            vh.userHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickLisenter != null) {
                        onItemClickLisenter.onHead(myNews);
                    }
                }
            });

            vh.tv_collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myNews.setLike(!myNews.isLike());
                    myNews.setLikeCount(myNews.isLike() ? myNews.getLikeCount() + 1 : myNews.getLikeCount() - 1);
                    vh.tv_collection.setCompoundDrawables(null, myNews.isLike() ? mLikedDrawable : mLikeDrawable, null, null);
                    vh.tv_collection.setText(CommonUtils.getLikeCount(myNews.getLikeCount()));
                    vh.tv_collection.setTextColor(myNews.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.bg_white));
                    if (onItemClickLisenter != null) {
                        onItemClickLisenter.onLike(myNews);
                    }
                }
            });
            vh.mVerticalVideoController.setOnLoveListener(new VerticalVideoController.OnLoveListener() {
                @Override
                public void onLoveClick() {

                    if(myNews.isLike()){
                        return;
                    }

                    myNews.setLike(!myNews.isLike());
                    myNews.setLikeCount(myNews.isLike() ? myNews.getLikeCount() + 1 : myNews.getLikeCount() - 1);
                    vh.tv_collection.setCompoundDrawables(null, myNews.isLike() ? mLikedDrawable : mLikeDrawable, null, null);
                    vh.tv_collection.setText(CommonUtils.getLikeCount(myNews.getLikeCount()));
                    vh.tv_collection.setTextColor(myNews.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.bg_white));
                    if (onItemClickLisenter != null) {
                        onItemClickLisenter.onLike(myNews);
                    }
                }
            });
            //评论点击
            vh.btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickLisenter != null) {
                        onItemClickLisenter.onComment(myNews, vh.btn_comment);
                    }
                }
            });
            //分享点击
            vh.btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickLisenter != null) {
                        onItemClickLisenter.onShare(myNews, position);
                    }
                }
            });
            if (onItemClickLisenter!=null){
                onItemClickLisenter.onDisplayHideGold(true);
            }
            setGoldInfo(vh, myNews);
        }else if (myNews.getIsAd()==1){
            vh.right_view.setVisibility(View.GONE);
            vh.tv_collection.setVisibility(View.GONE);
            vh.btn_comment.setVisibility(View.GONE);
            vh.btn_share.setVisibility(View.GONE);
            vh.tv_ad.setVisibility(View.VISIBLE);
            vh.fl_video.setVisibility(View.VISIBLE);
            vh.ll_ad.setVisibility(View.GONE);
            vh.videoPlayer.setPlayerConfig(vh.mPlayerConfig);
            vh.videoPlayer.setUrl(myNews.getVideoUrl());
            vh.videoPlayer.setVideoController(vh.mVerticalVideoController);
            NativeAd mNativeAD= myNews.getmNativeAd();
            String url = mNativeAD.getAdCoverImageUrl();//图片
            String title = mNativeAD.getAdTitle();//标题
            String iconimageurl = mNativeAD.getAdIconUrl();//图标
            String callToAction = mNativeAD.getAdCallToAction();//点击按钮文案

            FrescoUtil.setControllerListener(vh.mVerticalVideoController.getThumb(),url,
                    ScreenUtils.getScreenWidth(mContext));

            vh.userHead.setImageURI(iconimageurl);
            vh.user_name.setText(callToAction);
            vh.video_content.setText(title);
            mNativeAD.registerViewForInteraction(vh.videoPlayer);
            if (onItemClickLisenter!=null){
                onItemClickLisenter.onDisplayHideGold(false);
            }
        }else if (myNews.getIsAd()==2){
            vh.fl_video.setVisibility(View.GONE);
            vh.ll_ad.setVisibility(View.VISIBLE);
            populateUnifiedNativeAdView(myNews.getUnifiedNativeAd(), vh.unav);


        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    private void setIsColection(MyNews data, TextView tv) {
        Drawable drawable;
        if (!data.isLike()) {
            drawable = mContext.getResources().getDrawable(R.mipmap.icon_zan4);
        } else {
            drawable = mContext.getResources().getDrawable(R.mipmap.icon_zan4ed);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(null, drawable, null, null);
        tv.setTextColor(data.isLike() ? mContext.getResources().getColor(R.color.c_eb3e44) : mContext.getResources().getColor(R.color.bg_white));
        tv.setText(CommonUtils.getLikeCount(data.getLikeCount()));
    }


    private void setGoldInfo(final VH vh, final MyNews mData) {
        vh.videoPlayer.setVideoListener(new VideoListener() {
            @Override
            public void onVideoStarted() {
                final MyNews mCurrentPlayData = mData;
                if (onItemClickLisenter != null) {
                    onItemClickLisenter.onVideoStart(vh.videoPlayer);
                }
                if (mOnVideoPlayStatusLisenter != null) {
                    mOnVideoPlayStatusLisenter.onVideoPlaying(mData, vh.getAdapterPosition());
                }

            }

            @Override
            public void onVideoPaused() {
                LogUtil.showLog("onVideoPaused");
                if (mOnVideoPlayStatusLisenter != null) {
                    mOnVideoPlayStatusLisenter.onVideoPaused();
                }
            }

            @Override
            public void onComplete() {
                LogUtil.showLog("onComplete");
                if (mOnVideoPlayStatusLisenter != null) {
                    mOnVideoPlayStatusLisenter.onVideoComplete(mData, vh.getAdapterPosition());
                }
            }

            @Override
            public void onPrepared() {
                LogUtil.showLog("onPrepared");
            }

            @Override
            public void onError() {
                LogUtil.showLog("onError");
                if (mOnVideoPlayStatusLisenter != null) {
                    mOnVideoPlayStatusLisenter.onVideoError();
                }
            }

            @Override
            public void onInfo(int what, int extra) {
                LogUtil.showLog("onInfo");
            }
        });

        vh.mVerticalVideoController.setProgressBarListener(new ProgressBarListener() {
            @Override
            public void onMove() {

            }

            @Override
            public void onStop() {
                if (mOnVideoPlayStatusLisenter != null) {
                    mOnVideoPlayStatusLisenter.onVideoStop(mData);
                }
            }

            @Override
            public void onBuffering() {
                LogUtil.showLog("onBuffering");
                if (mOnVideoPlayStatusLisenter != null) {
                    mOnVideoPlayStatusLisenter.onBuffering(mData);
                }
            }

            @Override
            public void onPreparing() {
                if (mOnVideoPlayStatusLisenter != null) {
                    mOnVideoPlayStatusLisenter.onPreparing(mData);
                }
            }

            @Override
            public void onProgressCompletion() {
                if (mOnVideoPlayStatusLisenter != null) {
                    mOnVideoPlayStatusLisenter.onVideoComplete(mData, vh.getAdapterPosition());
                }
            }

            @Override
            public void onProgress(int position, int duration) {

            }
        });
    }

    class VH extends RecyclerView.ViewHolder {
        IjkVideoView videoPlayer;
        PlayerConfig mPlayerConfig;
        VerticalVideoController mVerticalVideoController;//垂直播放Vp

        SimpleDraweeView userHead;
        TextView tv_collection;
        TextView btn_share;
        TextView btn_comment;
        TextView user_name;
        TextView video_content;
        TextView tv_ad;

        RelativeLayout video_progress_layout;
        MyVideoProgress mVideoProgress;
        LinearLayout right_view;
        UnifiedNativeAdView unav;

        FrameLayout fl_video;
        LinearLayout ll_ad;
        public VH(View v) {
            super(v);
            videoPlayer = v.findViewById(R.id.videoPlayer);
            mVerticalVideoController = new VerticalVideoController(mContext);
            mPlayerConfig = new PlayerConfig.Builder().enableCache().setLooping().addToPlayerManager().build();
            userHead = v.findViewById(R.id.user_head);
            tv_collection = v.findViewById(R.id.tv_collection);
            btn_share = v.findViewById(R.id.btn_share);
            btn_comment = v.findViewById(R.id.btn_comment);
            user_name = v.findViewById(R.id.user_name);
            video_content = v.findViewById(R.id.video_content);
            video_progress_layout = v.findViewById(R.id.video_progress_layout);
            mVideoProgress = v.findViewById(R.id.video_progress);
            tv_ad = v.findViewById(R.id.tv_ad);
            right_view = v.findViewById(R.id.right_view);
            fl_video = v.findViewById(R.id.fl_video);
            ll_ad = v.findViewById(R.id.ll_ad);
            unav = v.findViewById(R.id.unav);
        }
    }


    public interface OnItemClickLisenter {
        void onClick(MyNews data, int position);

        void onComment(MyNews news, TextView tv_msg);//评论

        void onLike(MyNews news);//视频点赞

        void onHead(MyNews news);

        void onShare(MyNews item, int pos);

        void onGetGold(MyNews myNews);

        void getVideoProgress(MyVideoProgress mVideoProgress);

        void onVideoStart(IjkVideoView v);

        void onDisplayHideGold(boolean isDisplay);
    }

    private OnItemClickLisenter onItemClickLisenter;

    public void setOnItemClickLisenter(OnItemClickLisenter l) {
        this.onItemClickLisenter = l;
    }


    /**
     * 视频播放监听暴露接口
     */
    public interface onVideoPlayStatusLisenter {
        void onVideoComplete(MyNews mData, int position);

        void onVideoPlaying(MyNews mData, int position);

        void onVideoPaused();

        void onVideoError();

        void onVideoStop(MyNews mData);

        void onBuffering(MyNews mData);

        void onPreparing(MyNews mData);
    }

    private onVideoPlayStatusLisenter mOnVideoPlayStatusLisenter;

    public void setOnVideoPlayStatusLisenter(onVideoPlayStatusLisenter mOnVideoPlayStatusLisenter) {
        this.mOnVideoPlayStatusLisenter = mOnVideoPlayStatusLisenter;
    }


    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);


        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {/*
            videoStatus.setText(String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    vc.getAspectRatio()));*/

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
//                    refresh.setEnabled(true);
//                    videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
//            videoStatus.setText("Video status: Ad does not contain a video asset.");
//            refresh.setEnabled(true);
        }
    }
}

