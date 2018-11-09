package com.sven.huinews.international.video.controller;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dueeeke.videoplayer.controller.GestureVideoController;
import com.dueeeke.videoplayer.listener.VideoListener;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.util.WindowUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.main.video.listener.ProgressBarListener;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;

public class HorizontalVideoController extends GestureVideoController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = HorizontalVideoController.class.getSimpleName();
    private LinearLayout bottom_container;
    private ImageView mStart_play;
    private ProgressBar loading;
    private TextView totalTime;
    private TextView currTime;
    private SeekBar seek_bar;
    private SimpleDraweeView thumb;
    private ProgressBar bottomProgress;
    private LinearLayout ll_count;
    private TextView tv_duration, tv_play;
    private boolean isDragging;
    private Animation showAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_player_alpha_in);
    private Animation hideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_player_alpha_out);
    protected ProgressBarListener mProgressBarListener;
    private Animation rotateAnimation;

    public HorizontalVideoController(@NonNull Context context) {
        super(context);
    }

    public HorizontalVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.horizontal_video_controller;
    }

    @Override
    protected void initView() {
        super.initView();
        mStart_play = controllerView.findViewById(R.id.start_play);
        loading = controllerView.findViewById(R.id.loading);
        totalTime = controllerView.findViewById(R.id.total_time);
        currTime = controllerView.findViewById(R.id.curr_time);
        seek_bar = controllerView.findViewById(R.id.seek_bar);
        thumb = controllerView.findViewById(R.id.thumb);
        bottomProgress = controllerView.findViewById(R.id.bottom_progress);
        bottom_container = controllerView.findViewById(R.id.bottom_container);
        tv_duration = controllerView.findViewById(R.id.tv_duration);
//        rotateAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.video_loading);
//        loading.startAnimation(rotateAnimation);
        initEvents();
    }

    private void initEvents() {
        seek_bar.setOnSeekBarChangeListener(this);
        mStart_play.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.start_play) {
            doPauseResume();
        }
    }

    @Override
    protected void doPauseResume() {
        super.doPauseResume();
        if (mediaPlayer.isPlaying()) {
            mStart_play.setImageResource(R.mipmap.icon_stop);
        } else {
            mStart_play.setImageResource(R.mipmap.icon_start);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }
        long duration = mediaPlayer.getDuration();
        long newPosition = (duration * progress) / seek_bar.getMax();
        if (currTime != null)
            currTime.setText(stringForTime((int) newPosition));
        //播放器的进度条监听
        if (mProgressBarListener!=null){
            mProgressBarListener.onMove();
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        removeCallbacks(mShowProgress);
        removeCallbacks(mFadeOut);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long duration = mediaPlayer.getDuration();
        long newPosition = (duration * seekBar.getProgress()) / seek_bar.getMax();
        mediaPlayer.seekTo((int) newPosition);
        isDragging = false;
        post(mShowProgress);
        show();
    }


    @Override
    public void setPlayState(int playState) {
        // super.setPlayState(playState);
        switch (playState) {
            case IjkVideoView.STATE_IDLE:
                LogUtil.showLog(TAG, "STATE_IDLE");
                hide();
                seek_bar.setProgress(0);
                seek_bar.setSecondaryProgress(0);
                loading.setVisibility(View.GONE);
                mStart_play.setVisibility(VISIBLE);
                thumb.setVisibility(VISIBLE);
                mStart_play.setImageResource(R.mipmap.icon_start);
                bottomProgress.setProgress(0);
                bottomProgress.setSecondaryProgress(0);
                bottomProgress.setVisibility(GONE);
                if (mProgressBarListener!=null){
                    mProgressBarListener.onStop();
                }
                break;
            case IjkVideoView.STATE_PLAYING:
                //开始播放
                LogUtil.showLog(TAG, "STATE_PLAYING");
                post(mShowProgress);
                loading.setVisibility(View.GONE);
                thumb.setVisibility(GONE);
                mStart_play.setImageResource(R.mipmap.icon_stop);

                break;
            case IjkVideoView.STATE_PAUSED:
                //暂停
                LogUtil.showLog(TAG, "STATE_PAUSED");
                mStart_play.setImageResource(R.mipmap.icon_start);
                break;
            case IjkVideoView.STATE_PREPARING:
                //开始准备
                LogUtil.showLog(TAG, "STATE_PREPARING");
                loading.setVisibility(View.VISIBLE);
                thumb.setVisibility(View.VISIBLE);
                mStart_play.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_PREPARED:
                //准备完毕
                LogUtil.showLog(TAG, "STATE_PREPARED");
                mStart_play.setVisibility(GONE);
                bottomProgress.setVisibility(VISIBLE);
                break;
            case IjkVideoView.STATE_ERROR:
                //错误
                LogUtil.showLog(TAG, "STATE_ERROR");
                loading.setVisibility(View.GONE);
                mStart_play.setVisibility(GONE);
                thumb.setVisibility(GONE);
                bottom_container.setVisibility(View.GONE);
                bottomProgress.setVisibility(GONE);
                break;
            case IjkVideoView.STATE_BUFFERING:
                //缓冲
                LogUtil.showLog(TAG, "STATE_BUFFERING");
                loading.setVisibility(View.VISIBLE);
                mStart_play.setVisibility(GONE);
                thumb.setVisibility(GONE);
                break;
            case IjkVideoView.STATE_BUFFERED:
                //缓冲完毕
                LogUtil.showLog(TAG, "STATE_BUFFERED");
                loading.setVisibility(View.GONE);
                mStart_play.setVisibility(GONE);
                thumb.setVisibility(GONE);
                break;
            case IjkVideoView.STATE_PLAYBACK_COMPLETED:
                //
                LogUtil.showLog(TAG, "STATE_PLAYBACK_COMPLETED");
                hide();
                removeCallbacks(mShowProgress);
                loading.setVisibility(View.GONE);
                mStart_play.setVisibility(VISIBLE);
                thumb.setVisibility(VISIBLE);
                mStart_play.setImageResource(R.mipmap.icon_start);
                bottomProgress.setProgress(0);
                bottomProgress.setSecondaryProgress(0);
                break;
        }
    }

    @Override
    protected int setProgress() {
        if (mediaPlayer == null || isDragging) {
            return 0;
        }

        int position = (int) mediaPlayer.getCurrentPosition();
        int duration = (int) mediaPlayer.getDuration();
        if (seek_bar != null) {
            if (duration > 0) {
                seek_bar.setEnabled(true);
                int pos = (int) (position * 1.0 / duration * seek_bar.getMax());
                seek_bar.setProgress(pos);
                bottomProgress.setProgress(pos);
            } else {
                seek_bar.setEnabled(false);
            }
            int percent = mediaPlayer.getBufferPercentage();
            if (percent >= 95) { //修复第二进度不能100%问题
                seek_bar.setSecondaryProgress(seek_bar.getMax());
                bottomProgress.setSecondaryProgress(bottomProgress.getMax());
            } else {
                seek_bar.setSecondaryProgress(percent * 10);
                bottomProgress.setSecondaryProgress(percent * 10);
            }
        }

        if (totalTime != null)
            totalTime.setText(stringForTime(duration));
        if (currTime != null)
            currTime.setText(stringForTime(position));
        if (mProgressBarListener!=null){
            mProgressBarListener.onProgress(position,duration);
        }

        return position;
    }

    @Override
    public void show() {
        if (!mShowing) {
            if (!mediaPlayer.isFullScreen()) {
                bottom_container.setVisibility(View.VISIBLE);
                bottom_container.startAnimation(showAnim);
                mStart_play.setVisibility(View.VISIBLE);
                mStart_play.startAnimation(showAnim);
                tv_duration.setVisibility(View.GONE);
            }
            bottomProgress.setVisibility(GONE);
            bottomProgress.startAnimation(hideAnim);
            mShowing = true;

        }

        removeCallbacks(mFadeOut);
        if (sDefaultTimeout != 0) {
            postDelayed(mFadeOut, sDefaultTimeout);
        }
    }

    @Override
    public void hide() {
        if (mShowing) {
            if (!mediaPlayer.isFullScreen()) {
                bottom_container.setVisibility(View.GONE);
                bottom_container.startAnimation(hideAnim);
                mStart_play.setVisibility(View.GONE);
                mStart_play.startAnimation(hideAnim);
                tv_duration.setVisibility(View.VISIBLE);
            }
            bottomProgress.setVisibility(VISIBLE);
            bottomProgress.startAnimation(showAnim);
            mShowing = false;
        }
    }

    @Override
    protected void slideToChangePosition(float deltaX) {
        super.slideToChangePosition(deltaX);
    }


    /**
     * 设置播放器监听，用于外部监听播放器的各种状态
     */
    public void setProgressBarListener(ProgressBarListener listener) {
        this.mProgressBarListener = listener;
    }

    @Override
    public boolean onBackPressed() {
        if (mediaPlayer.isFullScreen()) {
            WindowUtil.scanForActivity(getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mediaPlayer.stopFullScreen();
            return true;
        }
        return super.onBackPressed();
    }

    public SimpleDraweeView getThumb() {
        return thumb;
    }

    public void setVideoInfo(int duration) {
        tv_duration.setText(CommonUtils.getDuration((long) duration));
    }
}
