package wedemo.fragment;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wedemo.utils.Constants;
import wedemo.utils.MediaPlayerUtil;
import wedemo.utils.Util;
import wedemo.view.DrawRect;

/**
 * Created by yyj on 2018/5/29 0029.
 * VideoFragment，封装liveWindow,供多个页面使用，避免代码重复
 */

public class VideoFragment extends Fragment {
    private final String TAG = "VideoFragment";
    private View mView;
    private static final int RESETPLATBACKSTATE = 100;
    private RelativeLayout mPlayerLayout;
    private NvsLiveWindow mLiveWindow;
    private DrawRect mDrawRect;
    private LinearLayout mPlayBarLayout;
    private RelativeLayout mPlayButton;
    private ImageView mPlayImage;
    private TextView mCurrentPlayTime;
    private SeekBar mPlaySeekBar;
    private TextView mTotalDuration;
    private RelativeLayout mVoiceButton;

    private NvsStreamingContext mStreamingContext = NvsStreamingContext.getInstance();
    private NvsTimeline mTimeline;
    private boolean mPlayBarVisibleState = true, mVoiceButtonVisibleState = false, mAutoPlay = false, mRecording = false;
    private OnFragmentLoadFinisedListener mFragmentLoadFinisedListener;
    private VideoFragmentListener mVideoFragmentCallBack;
    private AssetEditListener mAssetEditListener;
    private VideoVolumeListener mVideoVolumeListener;
    private OnLiveWindowClickListener mLiveWindowClickListener;
    private OnStickerMuteListener mStickerMuteListener;
    private VideoCaptionTextEditListener mCaptionTextEditListener;
    private NvsTimelineCaption mCurCaption;
    private int mEditMode = 0;
    private NvsTimelineAnimatedSticker mCurAnimateSticker;
    private int mStickerMuteIndex = 0;
    // 播放开始标识
    private long mPlayStartFlag = -1;
    private VideoScrollListener videoScrollListener;

    public void setVideoScrollListener(VideoScrollListener videoScrollListener) {
        this.videoScrollListener = videoScrollListener;
    }

    //Fragment加载完成回调
    public interface OnFragmentLoadFinisedListener {
        void onLoadFinished();
    }

    //视频播放相关回调
    public interface VideoFragmentListener {
        //video play
        void playBackEOF(NvsTimeline timeline);

        void playStopped(NvsTimeline timeline);

        void playbackTimelinePosition(NvsTimeline timeline, long stamp);

        void streamingEngineStateChanged(int state);
    }

    //视频滑动监听
    public interface VideoScrollListener {
        boolean onLeft(int pos);

        boolean onRight(int pos);
    }

    //贴纸和字幕编辑对应的回调，其他素材不用
    public interface AssetEditListener {
        void onAssetDelete();

        void onAssetSelected(PointF curPoint);

        void onAssetTranstion();

        void onAssetScale();

        void onAssetAlign(int alignVal);//字幕使用

        void onAssetHorizFlip(boolean isHorizFlip);//贴纸使用
    }

    //音量回调
    public interface VideoVolumeListener {
        void onVideoVolume();
    }

    //字幕文本修改回调
    public interface VideoCaptionTextEditListener {
        void onCaptionTextEdit();
    }

    //LiveWindowd点击回调
    public interface OnLiveWindowClickListener {
        void onLiveWindowClick();
    }

    //LiveWindowd点击回调
    public interface OnStickerMuteListener {
        void onStickerMute();
    }

    public void setLiveWindowClickListener(OnLiveWindowClickListener liveWindowClickListener) {
        this.mLiveWindowClickListener = liveWindowClickListener;
    }

    public void setCaptionTextEditListener(VideoCaptionTextEditListener captionTextEditListener) {
        this.mCaptionTextEditListener = captionTextEditListener;
    }

    public void setFragmentLoadFinisedListener(OnFragmentLoadFinisedListener fragmentLoadFinisedListener) {
        this.mFragmentLoadFinisedListener = fragmentLoadFinisedListener;
    }

    public void setVideoFragmentCallBack(VideoFragmentListener videoFragmentCallBack) {
        this.mVideoFragmentCallBack = videoFragmentCallBack;
    }

    public void setAssetEditListener(AssetEditListener assetEditListener) {
        this.mAssetEditListener = assetEditListener;
    }

    public void setVideoVolumeListener(VideoVolumeListener videoVolumeListener) {
        this.mVideoVolumeListener = videoVolumeListener;
    }

    public void setStickerMuteListener(OnStickerMuteListener stickerMuteListener) {
        this.mStickerMuteListener = stickerMuteListener;
    }

    private Handler m_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case RESETPLATBACKSTATE:
                    updateSeekBarProgress(0);
                    updateCurPlayTime(0);
                    seekTimeline(0, 0);

                    // 播放进度条显示
                    if (mPlayBarVisibleState) {
                        mPlayStartFlag = -1;
                        mPlayBarLayout.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video, container, false);
        mPlayerLayout = (RelativeLayout) mView.findViewById(R.id.player_layout);
        mLiveWindow = (NvsLiveWindow) mView.findViewById(R.id.liveWindow);
        mDrawRect = (DrawRect) mView.findViewById(R.id.draw_rect);
        mPlayBarLayout = (LinearLayout) mView.findViewById(R.id.playBarLayout);
        mPlayButton = (RelativeLayout) mView.findViewById(R.id.playLayout);
        mPlayImage = (ImageView) mView.findViewById(R.id.playImage);
        mCurrentPlayTime = (TextView) mView.findViewById(R.id.currentPlaytime);
        mPlaySeekBar = (SeekBar) mView.findViewById(R.id.play_seekBar);
        mTotalDuration = (TextView) mView.findViewById(R.id.totalDuration);
        mVoiceButton = (RelativeLayout) mView.findViewById(R.id.voiceLayout);
        controllerOperation();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated");
        initData();
        //
        mPlayBarLayout.setVisibility(mPlayBarVisibleState ? View.VISIBLE : View.GONE);
        mVoiceButton.setVisibility(mVoiceButtonVisibleState ? View.VISIBLE : View.GONE);
        if (mFragmentLoadFinisedListener != null) {
            mFragmentLoadFinisedListener.onLoadFinished();
        }
    }

    private void initData() {
        updateLivewindow();
        updateTotalDuarationText();
        initDrawRectListener();
    }

    public void updateLivewindow(NvsTimeline timeline) {
        this.mTimeline = timeline;
        connectTimelineWithLiveWindow();
        seekTimeline(0, 0);
    }

    private void updateLivewindow() {
        Bundle bundle = getArguments();
        int ratio = 0, titleHeight = 0, bottomHeight = 0;
        boolean isneed = true;
        if (bundle != null) {
            //ratio = bundle.getInt("ratio");
            titleHeight = bundle.getInt("titleHeight");
            bottomHeight = bundle.getInt("bottomHeight");
            isneed = bundle.getBoolean("setwindow", true);
            mPlayBarVisibleState = bundle.getBoolean("playBarVisible", true);
            mVoiceButtonVisibleState = bundle.getBoolean("voiceButtonVisible", false);

        }

        if (null == mTimeline) {
            Log.e(TAG, "mTimeline is null!");
            return;
        }

        if (isneed) {
            setLiveWindowRatio(ratio, titleHeight, bottomHeight);
        } else {
            mLiveWindow.setFillMode(NvsLiveWindow.FILLMODE_STRETCH);
        }

        connectTimelineWithLiveWindow();
    }

    public void updateCurPlayTime(long time) {
        mCurrentPlayTime.setText(formatTimeStrWithUs(time));
    }

    public void updateTotalDuarationText() {
        mTotalDuration.setText(formatTimeStrWithUs(mTimeline.getDuration()));
    }

    public void updateSeekBarProgress(int progress) {
        mPlaySeekBar.setProgress(progress);
    }

    public void updateSeekBarMaxValue() {
        mPlaySeekBar.setMax((int) mTimeline.getDuration());
    }

    public void setTimeline(NvsTimeline timeline) {
        mTimeline = timeline;
    }

    private void initDrawRectListener() {
        mDrawRect.setOnTouchListener(new DrawRect.OnTouchListener() {
            @Override
            public void onDrag(PointF prePointF, PointF nowPointF) {
                /* 坐标转换
                *
                * SDK接口所使用的坐标均是Canonical坐标系内的坐标，而我们在程序中所用是的
                * 一般是Android View 坐标系里面的坐标，所以在使用接口的时候需要使用SDK所
                * 提供的mapViewToCanonical函数将View坐标转换为Canonical坐标，相反的，
                * 如果想要将Canonical坐标转换为View坐标，则可以使用mapCanonicalToView
                * 函数进行转换。
                * */
                PointF pre = mLiveWindow.mapViewToCanonical(prePointF);
                PointF p = mLiveWindow.mapViewToCanonical(nowPointF);
                PointF timeLinePointF = new PointF(p.x - pre.x, p.y - pre.y);
                if (mEditMode == Constants.EDIT_MODE_CAPTION) {
                    // 移动字幕
                    if (mCurCaption != null) {
                        mCurCaption.translateCaption(timeLinePointF);
                        updateCaptionCoordinate(mCurCaption);
                        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                    }
                } else if (mEditMode == Constants.EDIT_MODE_STICKER) { // 贴纸编辑
                    // 移动贴纸
                    if (mCurAnimateSticker != null) {
                        mCurAnimateSticker.translateAnimatedSticker(timeLinePointF);
                        updateAnimateStickerCoordinate(mCurAnimateSticker);
                        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
                    }
                }
                if (mAssetEditListener != null) {
                    mAssetEditListener.onAssetTranstion();
                }
            }

            @Override
            public void onScaleAndRotate(float scaleFactor, PointF anchor, float angle) {
                /* 坐标转换
                *
                * SDK接口所使用的坐标均是Canonical坐标系内的坐标，而我们在程序中所用是的
                * 一般是Android View 坐标系里面的坐标，所以在使用接口的时候需要使用SDK所
                * 提供的mapViewToCanonical函数将View坐标转换为Canonical坐标，相反的，
                *如果想要将Canonical坐标转换为View坐标，则可以使用mapCanonicalToView
                * 函数进行转换。
                * */
                PointF assetAnchor = mLiveWindow.mapViewToCanonical(anchor);
                if (mEditMode == Constants.EDIT_MODE_CAPTION) {
                    if (mCurCaption != null) {
                        // 放缩字幕
                        mCurCaption.scaleCaption(scaleFactor, assetAnchor);
                        // 旋转字幕
                        mCurCaption.rotateCaption(angle);
                        updateCaptionCoordinate(mCurCaption);
                        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                    }
                } else if (mEditMode == Constants.EDIT_MODE_STICKER) { // 贴纸编辑
                    // 放缩贴纸
                    if (mCurAnimateSticker != null) {
                        //缩放贴纸
                        mCurAnimateSticker.scaleAnimatedSticker(scaleFactor, assetAnchor);
                        //旋转贴纸
                        mCurAnimateSticker.rotateAnimatedSticker(angle);
                        updateAnimateStickerCoordinate(mCurAnimateSticker);
                        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
                    }
                }
                if (mAssetEditListener != null) {
                    mAssetEditListener.onAssetScale();
                }
            }

            @Override
            public void onDel() {
                if (mAssetEditListener != null) {
                    mAssetEditListener.onAssetDelete();
                }
            }

            @Override
            public void onTouchDown(PointF curPoint) {
                if (mAssetEditListener != null) {
                    mAssetEditListener.onAssetSelected(curPoint);
                }
            }

            @Override
            public void onAlignClick() {
                if (mEditMode == Constants.EDIT_MODE_CAPTION
                        && mCurCaption != null) {
                    switch (mCurCaption.getTextAlignment()) {
                        case NvsTimelineCaption.TEXT_ALIGNMENT_LEFT:
                            mCurCaption.setTextAlignment(NvsTimelineCaption.TEXT_ALIGNMENT_CENTER);  //居中对齐
                            setAlignIndex(1);
                            break;
                        case NvsTimelineCaption.TEXT_ALIGNMENT_CENTER:
                            mCurCaption.setTextAlignment(NvsTimelineCaption.TEXT_ALIGNMENT_RIGHT);  //居右对齐
                            setAlignIndex(2);
                            break;

                        case NvsTimelineCaption.TEXT_ALIGNMENT_RIGHT:
                            mCurCaption.setTextAlignment(NvsTimelineCaption.TEXT_ALIGNMENT_LEFT);  //左对齐
                            setAlignIndex(0);
                            break;
                    }
                    seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                    if (mAssetEditListener != null) {
                        mAssetEditListener.onAssetAlign(mCurCaption.getTextAlignment());
                    }
                }
            }

            @Override
            public void onHorizFlipClick() {
                if (mEditMode == Constants.EDIT_MODE_STICKER) {
                    if (mCurAnimateSticker == null)
                        return;
                    // 贴纸水平翻转
                    boolean isHorizFlip = !mCurAnimateSticker.getHorizontalFlip();
                    mCurAnimateSticker.setHorizontalFlip(isHorizFlip);
                    updateAnimateStickerCoordinate(mCurAnimateSticker);
                    seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
                    if (mAssetEditListener != null) {
                        mAssetEditListener.onAssetHorizFlip(isHorizFlip);
                    }
                }
            }

            @Override
            public void onBeyondDrawRectClick() {
                mPlayButton.callOnClick();
            }
        });

        mDrawRect.setDrawRectClickListener(new DrawRect.onDrawRectClickListener() {
            @Override
            public void onDrawRectClick() {
                if (mCaptionTextEditListener != null)
                    mCaptionTextEditListener.onCaptionTextEdit();
            }
        });

        mDrawRect.setStickerMuteListenser(new DrawRect.onStickerMuteListenser() {
            @Override
            public void onStickerMute() {
                if (mCurAnimateSticker == null)
                    return;
                mStickerMuteIndex = mStickerMuteIndex == 0 ? 1 : 0;
                float volumeGain = mStickerMuteIndex == 0 ? 1.0f : 0.0f;
                mCurAnimateSticker.setVolumeGain(volumeGain, volumeGain);
                setStickerMuteIndex(mStickerMuteIndex);
                if (mStickerMuteListener != null)
                    mStickerMuteListener.onStickerMute();
            }
        });
    }

    public void setEditMode(int mode) {
        mEditMode = mode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setAlignIndex(int index) {
        mDrawRect.setAlignIndex(index);
    }

    //字幕API
    public void setCurCaption(NvsTimelineCaption caption) {
        mCurCaption = caption;
    }

    public NvsTimelineCaption getCurCaption() {
        return mCurCaption;
    }

    // 更新字幕在视图上的坐标
    public void updateCaptionCoordinate(NvsTimelineCaption caption) {
        if (caption != null) {
            // 获取字幕的原始包围矩形框变换后的顶点位置
            List<PointF> list = caption.getBoundingRectangleVertices();
            if (list == null || list.size() < 4)
                return;

            List<PointF> newList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                PointF pointF = mLiveWindow.mapCanonicalToView(list.get(i));
                newList.add(pointF);
            }
            mDrawRect.setDrawRect(newList, Constants.EDIT_MODE_CAPTION);
        }
    }

    public void setDrawRectVisible(int visibility) {
        mDrawRect.setVisibility(visibility);
    }

    public void changeCaptionRectVisible() {
        if (mEditMode == Constants.EDIT_MODE_CAPTION) {
            setDrawRectVisible(isSelectedCaption() ? View.VISIBLE : View.GONE);
        }
    }

    //在liveWindow上手动选择字幕
    public void selectCaptionByHandClick(PointF curPoint) {
        List<NvsTimelineCaption> captionList = mTimeline.getCaptionsByTimelinePosition(mStreamingContext.getTimelineCurrentPosition(mTimeline));
        if (captionList.size() <= 1)
            return;

        for (int j = 0; j < captionList.size(); j++) {
            NvsTimelineCaption caption = captionList.get(j);
            List<PointF> list = caption.getBoundingRectangleVertices();
            List<PointF> newList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                PointF pointF = mLiveWindow.mapCanonicalToView(list.get(i));
                newList.add(pointF);
            }
            //判断是否点击在字幕框中
            RectF r = new RectF();
            Path path = new Path();
            path.moveTo(newList.get(0).x, newList.get(0).y);
            path.lineTo(newList.get(1).x, newList.get(1).y);
            path.lineTo(newList.get(2).x, newList.get(2).y);
            path.lineTo(newList.get(3).x, newList.get(3).y);
            path.close();
            path.computeBounds(r, true);
            Region region = new Region();
            region.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
            boolean isInnerRect = region.contains((int) curPoint.x, (int) curPoint.y);
            if (isInnerRect) {
                mDrawRect.setDrawRect(newList, Constants.EDIT_MODE_CAPTION);
                mCurCaption = caption;
                break;
            }
        }
    }

    public boolean curPointIsInnerDrawRect(int xPos, int yPos) {
        return mDrawRect.curPointIsInnerDrawRect(xPos, yPos);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setAutoPlay(boolean flag) {
        mAutoPlay = flag;
    }

    public void setRecording(boolean record_state) {
        mRecording = record_state;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //贴纸API
    public void setCurAnimateSticker(NvsTimelineAnimatedSticker animateSticker) {
        mCurAnimateSticker = animateSticker;
    }

    public void setStickerMuteIndex(int index) {
        mStickerMuteIndex = index;
        mDrawRect.setStickerMuteIndex(index);
    }

    public NvsTimelineAnimatedSticker getCurAnimateSticker() {
        return mCurAnimateSticker;
    }

    // 更新贴纸在视图上的坐标
    public void updateAnimateStickerCoordinate(NvsTimelineAnimatedSticker animateSticker) {
        if (animateSticker != null) {
            // 获取贴纸的原始包围矩形框变换后的顶点位置
            List<PointF> list = animateSticker.getBoundingRectangleVertices();
            if (list == null || list.size() < 4)
                return;
            boolean isHorizonFlip = animateSticker.getHorizontalFlip();//如果已水平翻转，需要对顶点数据进行处理
            if (isHorizonFlip) {
                Collections.swap(list, 0, 3);
                Collections.swap(list, 1, 2);
            }
            List<PointF> newList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                PointF pointF = mLiveWindow.mapCanonicalToView(list.get(i));
                newList.add(pointF);
            }
            mDrawRect.setDrawRect(newList, Constants.EDIT_MODE_STICKER);
        }
    }

    //设置贴纸选择框显隐
    public void changeStickerRectVisible() {
        if (mEditMode == Constants.EDIT_MODE_STICKER) {
            setDrawRectVisible(isSelectedAnimateSticker() ? View.VISIBLE : View.GONE);
        }
    }

    //在liveWindow上手动选择贴纸
    public void selectAnimateStickerByHandClick(PointF curPoint) {
        List<NvsTimelineAnimatedSticker> stickerList = mTimeline.getAnimatedStickersByTimelinePosition(mStreamingContext.getTimelineCurrentPosition(mTimeline));
        if (stickerList == null) {
            return;
        }
        if (stickerList.size() <= 1)
            return;

        for (int j = 0; j < stickerList.size(); j++) {
            NvsTimelineAnimatedSticker sticker = stickerList.get(j);
            List<PointF> list = sticker.getBoundingRectangleVertices();
            List<PointF> newList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                PointF pointF = mLiveWindow.mapCanonicalToView(list.get(i));
                newList.add(pointF);
            }

            // 判断手指是否在贴纸框中框内
            RectF r = new RectF();
            Path path = new Path();
            path.moveTo(newList.get(0).x, newList.get(0).y);
            path.lineTo(newList.get(1).x, newList.get(1).y);
            path.lineTo(newList.get(2).x, newList.get(2).y);
            path.lineTo(newList.get(3).x, newList.get(3).y);
            path.close();
            path.computeBounds(r, true);
            Region region = new Region();
            region.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
            boolean isSelected = region.contains((int) curPoint.x, (int) curPoint.y);
            if (isSelected) {
                mDrawRect.setDrawRect(newList, Constants.EDIT_MODE_STICKER);
                mCurAnimateSticker = sticker;
                break;
            }
        }
    }

    public void setMuteVisible(boolean hasAudio) {
        mDrawRect.setMuteVisible(hasAudio);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //连接时间线跟liveWindow
    public void connectTimelineWithLiveWindow() {
        if (mStreamingContext == null || mTimeline == null || mLiveWindow == null)
            return;
        mStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
            @Override
            public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {

            }

            @Override
            public void onPlaybackStopped(NvsTimeline nvsTimeline) {
                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.playStopped(nvsTimeline);
                }
            }

            @Override
            public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                if (mPlayBarVisibleState) {
                    m_handler.sendEmptyMessage(RESETPLATBACKSTATE);
                }
                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.playBackEOF(nvsTimeline);
                }
            }
        });

        mStreamingContext.setPlaybackCallback2(new NvsStreamingContext.PlaybackCallback2() {
            @Override
            public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long cur_position) {
                if (mPlayBarVisibleState) {
                    updateCurPlayTime(cur_position);
                    updateSeekBarProgress((int) cur_position);
                }
                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.playbackTimelinePosition(nvsTimeline, cur_position);
                }
                // 播放进度条消失
                if (mPlayBarVisibleState) {
                    if (mPlayStartFlag != -1) {
                        if (cur_position - mPlayStartFlag >= 3000000)
                            mPlayBarLayout.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        mStreamingContext.setStreamingEngineCallback(new NvsStreamingContext.StreamingEngineCallback() {
            @Override
            public void onStreamingEngineStateChanged(int i) {
                if (i == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    mPlayImage.setBackgroundResource(R.mipmap.icon_pause);
                } else {
                    mPlayImage.setBackgroundResource(R.mipmap.icon_start);
                }

                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.streamingEngineStateChanged(i);
                }
            }

            @Override
            public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {

            }
        });

        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, mLiveWindow);
        updateSeekBarMaxValue();
        updateCurPlayTime(0);
        updateSeekBarProgress(0);
    }

    private boolean isSelectedCaption() {
        long curPosition = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        if (mCurCaption != null
                && curPosition >= mCurCaption.getInPoint()
                && curPosition <= mCurCaption.getOutPoint()) {
            return true;
        }
        return false;
    }

    private boolean isSelectedAnimateSticker() {
        long curPosition = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        if (mCurAnimateSticker != null
                && curPosition >= mCurAnimateSticker.getInPoint()
                && curPosition <= mCurAnimateSticker.getOutPoint()) {
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopEngine();
    }

    @Override
    public void onResume() {
        super.onResume();
        connectTimelineWithLiveWindow();
        long stamp = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        updateCurPlayTime(stamp);
        updateSeekBarProgress((int) stamp);
        Log.e(TAG, "onResume");

        if (mAutoPlay && mPlayImage != null) {
            playVideoButtonCilck();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        mVideoFragmentCallBack = null;
        m_handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: " + hidden);
    }


    public void playVideo(long startTime, long endTime) {
        // 播放视频
        mStreamingContext.playbackTimeline(mTimeline, startTime, endTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }

    private MediaPlayerUtil player;


    //预览
    public void seekTimeline(long timestamp, int seekShowMode) {
        /* seekTimeline
         * param1: 当前时间线
         * param2: 时间戳 取值范围为  [0, timeLine.getDuration()) (左闭右开区间)
         * param3: 图像预览模式
         * param4: 引擎定位的特殊标志
         * */
        mStreamingContext.seekTimeline(mTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, seekShowMode);
    }

    // 获取当前引擎状态
    public int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }

    //停止引擎
    public void stopEngine() {
        if (mStreamingContext != null) {
            mStreamingContext.stop();//停止播放
        }
    }

    public void playVideoButtonCilck() {
        long endTime = mTimeline.getDuration();
        playVideoButtonCilck(0, endTime);
    }

    public void playVideoButtonCilck(long inPoint, long outPoint) {
        playVideo(inPoint, outPoint);
        // 更新播放进度条显示标识
        if (mPlayBarVisibleState) {
            mPlayStartFlag = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            mPlayBarLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setLiveWindowRatio(int ratio, int titleHeight, int bottomHeight) {

        ViewGroup.LayoutParams layoutParams = mPlayerLayout.getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        //状态栏高度
        int statusHeight = Util.getStatusBarHeight(getActivity());
        // screen width
        int screenWidth = metric.widthPixels;
        int screenHeight = metric.heightPixels;
        switch (ratio) {
            case 1: // 16:9
                layoutParams.width = screenWidth;
                layoutParams.height = (int) (screenWidth * 9.0 / 16);
                break;
            case 2: //1:1
                Log.e("weiwei", "titleHeight = " + titleHeight + ",bottomHeight = " + bottomHeight + ",ratio = " + ratio + ",screenWidth" + screenWidth);
                layoutParams.width = screenWidth;
                layoutParams.height = screenWidth;
                break;
            case 4: //9:16
                int newHeight = screenHeight - titleHeight - bottomHeight - statusHeight;
                layoutParams.width = (int) (newHeight * 9.0 / 16);
                layoutParams.height = newHeight;
                break;
            case 8: // 3:4
                int newHeight1 = screenHeight - titleHeight - bottomHeight - statusHeight;
                layoutParams.width = (int) (newHeight1 * 3.0 / 4);
                layoutParams.height = newHeight1;
                break;
            default: // 16:9
                layoutParams.width = screenWidth;
                layoutParams.height = (int) (screenWidth * 9.0 / 16);
                break;
        }
        mPlayerLayout.setLayoutParams(layoutParams);
        mLiveWindow.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTFIT);
//        mLiveWindow.setFillMode(NvsLiveWindow.FILLMODE_STRETCH);
//        mLiveWindow.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTCROP);
    }

    //formate time
    private String formatTimeStrWithUs(long us) {
        int second = (int) (us / 1000000.0 + 0.5);
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String timeStr;
        if (us == 0) {
            timeStr = "00:00";
        }
        if (hh > 0) {
            timeStr = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            timeStr = String.format("%02d:%02d", mm, ss);
        }
        return timeStr;
    }

    private void controllerOperation() {
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    stopEngine();
                    // 更新播放进度条显示标识
                    if (mPlayBarVisibleState) {
                        mPlayStartFlag = -1;
                    }
                } else {
                    long startTime = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                    long endTime = mTimeline.getDuration();
                    playVideo(startTime, endTime);
                    // 更新播放进度条显示标识
                    if (mPlayBarVisibleState) {
                        mPlayStartFlag = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                    }
                }
            }
        });

        mPlaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekTimeline(progress, 0);
                    updateCurPlayTime(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoVolumeListener != null) {
                    mVideoVolumeListener.onVideoVolume();
                }
            }
        });

        mLiveWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLiveWindowClickListener != null) {
                    mLiveWindowClickListener.onLiveWindowClick();
                }
                // 如果正在录音，禁止操作
                if (mRecording) {
                    return;
                }
                // 播放进度条显示
                if (mPlayBarVisibleState) {
                    if (mPlayBarLayout.getVisibility() == View.INVISIBLE) {
                        mPlayStartFlag = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                        mPlayBarLayout.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                mPlayButton.callOnClick();
            }
        });

        mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
            float downX;
            float moveX;
            float upX;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    downX = event.getX();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    moveX = event.getX();
                } else if (action == MotionEvent.ACTION_UP) {
                    upX = event.getX();
                    if (Math.abs(upX - downX) > 50 || Math.abs(downX - upX) > 50) {

                        if (moveX < downX) {//向左滑
                            if (videoScrollListener != null) {
                                return videoScrollListener.onLeft(0);
                            }
                        } else {//向右滑
                            if (videoScrollListener != null) {
                                return videoScrollListener.onRight(0);
                            }
                        }
                    }
                }
                return true;
            }
        });
    }
}
