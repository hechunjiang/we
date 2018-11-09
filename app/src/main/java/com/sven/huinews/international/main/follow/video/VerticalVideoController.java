package com.sven.huinews.international.main.follow.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.dueeeke.videoplayer.controller.BaseVideoController;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.util.L;
import com.dueeeke.videoplayer.util.PlayerConstants;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.main.video.listener.ProgressBarListener;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ScreensUitls;
import com.sven.huinews.international.view.StatusView;

import java.util.Random;

/**
 * Created by sfy. on 2018/9/10 0010.
 */

public class VerticalVideoController extends BaseVideoController implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "VerticalVideoController";
    private SimpleDraweeView thumb;
    private SeekBar seek_bar;
    private ImageView iv_start;
    private StatusView mStatusView;
    private ProgressBar loading;
    private boolean isDragging;
    protected ProgressBarListener mProgressBarListener;
    private Context mContext;

    public VerticalVideoController(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public VerticalVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public VerticalVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_vertical_controller;
    }

    @Override
    protected void initView() {
        super.initView();
        controllerView = LayoutInflater.from(getContext()).inflate(getLayoutId(), this);
        thumb = controllerView.findViewById(R.id.iv_thumb);
        seek_bar = controllerView.findViewById(R.id.seek_bar);
        iv_start = controllerView.findViewById(R.id.iv_start);
        loading = controllerView.findViewById(R.id.loading);

        mStatusView = new StatusView(getContext());
//        setParams();
      /*  controllerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    iv_start.setVisibility(View.VISIBLE);
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                    iv_start.setVisibility(View.GONE);
                }
            }
        });*/
        iv_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                iv_start.setVisibility(View.GONE);
            }
        });
    }

    private void setParams() {
        thumb.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) thumb.getLayoutParams();
        params.height = ScreensUitls.getScreenHeight(getContext());
        params.width = ScreensUitls.getScreenWidth(getContext());
        thumb.setLayoutParams(params);
    }

    float x;
    float y;

    int count = 0;
    private long mLastDownTime = 0;
    private long mLastUpTime = 0;
    private final long MAX_TIME = 200;

    public interface OnLoveListener{
        void onLoveClick();
    }

    private OnLoveListener onLoveListener;

    public void setOnLoveListener(OnLoveListener onLoveListener) {
        this.onLoveListener = onLoveListener;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        // TODO Auto-generated method stub
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mLastDownTime = System.currentTimeMillis();
            if(mLastDownTime  - mLastUpTime <= MAX_TIME){
                count++;
                addLove(event);
                if(onLoveListener != null){
                    onLoveListener.onLoveClick();
                }
            }else {
                count = 0;
            }
        }else if(MotionEvent.ACTION_UP == event.getAction()){
            mLastUpTime = System.currentTimeMillis();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(count == 0){
                        if (mediaPlayer.isPlaying()) {
                            iv_start.setVisibility(View.VISIBLE);
                            mediaPlayer.pause();
                        } else {
                            mediaPlayer.start();
                            iv_start.setVisibility(View.GONE);
                        }
                    }
                }
            },MAX_TIME);
        }

        return true;
    }


    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);

        switch (playState) {
            case IjkVideoView.STATE_IDLE:
                LogUtil.showLog(TAG, "STATE_IDLE");
                thumb.setVisibility(VISIBLE);
                iv_start.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                if (mProgressBarListener != null) {
                    mProgressBarListener.onStop();
                }
                break;
            case IjkVideoView.STATE_PLAYING:
                LogUtil.showLog(TAG, "STATE_PLAYING");
                thumb.setVisibility(GONE);
                post(mShowProgress);
                this.removeView(mStatusView);
                loading.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_PREPARED:
                LogUtil.showLog(TAG, "STATE_PREPARED");
                iv_start.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                break;
            case IjkVideoView.STATE_ERROR:
                LogUtil.showLog(TAG, "STATE_ERROR");
                loading.setVisibility(View.GONE);
                setErrorStatus();
                break;
            case IjkVideoView.STATE_PREPARING:
                loading.setVisibility(View.VISIBLE);
                LogUtil.showLog(TAG, "STATE_PREPARING");
                if (mProgressBarListener != null) {
                    mProgressBarListener.onPreparing();
                }
                break;
            case IjkVideoView.STATE_BUFFERING:
                //缓冲
                LogUtil.showLog(TAG, "STATE_BUFFERING");
                loading.setVisibility(View.VISIBLE);
                iv_start.setVisibility(GONE);
                // thumb.setVisibility(GONE);
                if (mProgressBarListener != null) {
                    mProgressBarListener.onBuffering();
                }
                break;
            case IjkVideoView.STATE_BUFFERED:
                //缓冲完毕
                LogUtil.showLog(TAG, "STATE_BUFFERED");
                loading.setVisibility(View.GONE);
                iv_start.setVisibility(GONE);
                thumb.setVisibility(GONE);
                break;
        }
    }

    public SimpleDraweeView getThumb() {
        return thumb;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }
        long duration = mediaPlayer.getDuration();
        long newPosition = (duration * progress) / seek_bar.getMax();
        //播放器的进度条监听
        if (mProgressBarListener != null) {
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
                LogUtil.showLog("mProgressBarListener:" + pos);
                if (pos >= 90) {
                    if (mProgressBarListener != null) {
                        mProgressBarListener.onProgressCompletion();
                    }
                }
                seek_bar.setProgress(pos);
                //bottomProgress.setProgress(pos);
            } else {
                seek_bar.setEnabled(false);
            }
            int percent = mediaPlayer.getBufferPercentage();

            if (percent >= 95) { //修复第二进度不能100%问题
                seek_bar.setSecondaryProgress(seek_bar.getMax());
                //    bottomProgress.setSecondaryProgress(bottomProgress.getMax());
            } else {
                seek_bar.setSecondaryProgress(percent * 10);
                //   bottomProgress.setSecondaryProgress(percent * 10);
            }
        }

        return position;
    }

    /**
     * 播放出错，复写该setPlayState方法
     * 主要更改界面
     */
    private void setErrorStatus() {
        this.removeView(mStatusView);
        this.addView(mStatusView, 0);
    }

    @Override
    public void showStatusView() {
        super.showStatusView();
        // setNetWorkStatus();
    }

    /**
     * 移动网络  、、  播放器框架处理在 baseControll里面
     * 这里需要复写他的方法showStatusView()
     * 主要更改界面
     */
    private void setNetWorkStatus() {
        this.removeView(mStatusView);
        mStatusView.setMessage(getResources().getString(com.dueeeke.videoplayer.R.string.player_wifi_tip));
        mStatusView.setButtonTextAndAction(getResources().getString(com.dueeeke.videoplayer.R.string.player_continue_play), new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatusView();
                PlayerConstants.IS_PLAY_ON_MOBILE_NETWORK = true;
                mediaPlayer.start();
            }
        });
        this.addView(mStatusView);
    }

    /**
     *  int centerX = getWidth() / 2;
     int centerY = getHeight() / 2;

     switch (event.getAction()) {
     case MotionEvent.ACTION_DOWN:
     x = event.getX();
     y = event.getY();
     LogUtil.showLog("msg---手指按下");

     break;
     case MotionEvent.ACTION_UP:
     LogUtil.showLog("msg---手指抬起");
     if ((x > centerX / 2 && x < centerX + (centerX / 2)) &&
     (y > centerY / 2 && y < centerY + (centerY / 2))) {

     if (mediaPlayer.isPlaying()) {
     iv_start.setVisibility(View.VISIBLE);
     mediaPlayer.pause();
     } else {
     mediaPlayer.start();
     iv_start.setVisibility(View.GONE);
     }
     }
     break;
     }
     */

    /**
     * 设置播放器监听，用于外部监听播放器的各种状态
     */
    public void setProgressBarListener(ProgressBarListener listener) {
        this.mProgressBarListener = listener;
    }


    /*********点赞动画*******/

    float[] num = {-30, -20, 0, 20, 30}; // 随机心形图片的角度
    private final int DOUBLE_TAP_TIMEOUT = 200;
    private MotionEvent mCurrentDownEvent;
    private MotionEvent mPreviousUpEvent;

    private boolean hasDoble = false;

    private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown) {
        if (secondDown.getEventTime() - firstUp.getEventTime() > DOUBLE_TAP_TIMEOUT) {
            return false;
        }
        int deltaX = (int) firstUp.getX() - (int) secondDown.getX();
        int deltaY = (int) firstUp.getY() - (int) secondDown.getY();
        return deltaX * deltaX + deltaY * deltaY < 10000;
    }


    private void addLove(MotionEvent event) {
        // 首先，我们需要在触摸事件中做监听，当有触摸时，创建一个展示心形图片的 ImageView
        final ImageView imageView = new ImageView(mContext);

        // 设置图片展示的位置，需要在手指触摸的位置上方，即触摸点是心形的下方角的位置。所以我们需要将 ImageView 设置到手指的位置
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 300);
        params.leftMargin = (int) event.getX() - 150;
        params.topMargin = (int) event.getY() - 300;

        imageView.setImageDrawable(getResources().getDrawable(com.dueeeke.videoplayer.R.drawable.lov));
        imageView.setLayoutParams(params);
        addView(imageView);

        // 设置 imageView 动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scale(imageView, "scaleX", 2f, 0.9f, 100, 0)) // 缩放动画，X轴2倍缩小至0.9倍
                .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0)) // 缩放动画，Y轴2倍缩小至0.9倍
                .with(rotation(imageView, 0, 0, num[new Random().nextInt(4)])) // 旋转动画，随机旋转角度num={-30.-20，0，20，30}
                .with(alpha(imageView, 0, 1, 100, 0)) // 渐变透明度动画，透明度从0-1.
                .with(scale(imageView, "scaleX", 0.9f, 1, 50, 150)) // 缩放动画，X轴0.9倍缩小至1倍
                .with(scale(imageView, "scaleY", 0.9f, 1, 50, 150)) // 缩放动画，Y轴0.9倍缩小至1倍
                .with(translationY(imageView, 0, -600, 800, 400)) // 平移动画，Y轴从0向上移动600单位
                .with(alpha(imageView, 1, 0, 300, 400)) // 透明度动画，从1-0
                .with(scale(imageView, "scaleX", 1, 3f, 700, 400)) // 缩放动画，X轴1倍放大至3倍
                .with(scale(imageView, "scaleY", 1, 3f, 700, 400)); // 缩放动画，Y轴1倍放大至3倍
        animatorSet.start();

        // 我们不可能无限制的增加 view，在 view 消失之后，需要手动的移除改 imageView
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeViewInLayout(imageView);
            }
        });
    }

    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator rotation(View view, long time, long delayTime, float... values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        return rotation;
    }

}
