package wedemo.activity.animatesticker;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.R;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import wedemo.MessageEvent;
import wedemo.activity.data.BackupData;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.base.BaseActivity;
import wedemo.fragment.VideoFragment;
import wedemo.utils.AppManager;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.ScreenUtils;
import wedemo.utils.TimeFormatUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.StickerInfo;
import wedemo.view.timelineEditor.NvsTimelineEditor;
import wedemo.view.timelineEditor.NvsTimelineTimeSpan;

public class AnimateStickerActivity extends BaseActivity {
    private static final String TAG = "AnimateStickerActivity";
    private static final int VIDEOPLAYTOEOF = 102;
    private static final int REQUESTANIMATESTICKERID = 104;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private RelativeLayout mZoomOutButton;
    private RelativeLayout mZoomInButton;
    private TextView mCurrentPlaytime;
    private ImageView mVideoPlay;
    private NvsTimelineEditor mTimelineEditor;
    private TextView mAddAnimateSticker;
    private ImageView mStickerFinish;

    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private VideoFragment mVideoFragment;
    private NvsMultiThumbnailSequenceView mMultiThumbnailSequenceView;
    private boolean mIsSeekTimeline = true;
    private AnimateStickerActivity.AnimateStickerHandler m_handler = new AnimateStickerActivity.AnimateStickerHandler(this);
    private List<AnimateStickerTimeSpanInfo> mTimeSpanInfoList = new ArrayList<AnimateStickerTimeSpanInfo>();
    private NvsTimelineAnimatedSticker mCurAnimatedSticker;
    ArrayList<StickerInfo> mStickerDataListClone;
    private CustomTimeLine currenTimeline;

    static class AnimateStickerHandler extends Handler {
        WeakReference<AnimateStickerActivity> mWeakReference;

        public AnimateStickerHandler(AnimateStickerActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final AnimateStickerActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case VIDEOPLAYTOEOF:
                        activity.resetView();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //贴纸与timeSpan类，存储添加的贴纸跟TimeSpan
    private class AnimateStickerTimeSpanInfo {
        public NvsTimelineAnimatedSticker mAnimateSticker;
        public NvsTimelineTimeSpan mTimeSpan;

        public AnimateStickerTimeSpanInfo(NvsTimelineAnimatedSticker sticker, NvsTimelineTimeSpan timeSpan) {
            this.mAnimateSticker = sticker;
            this.mTimeSpan = timeSpan;
        }
    }

    @Override
    public int getLayoutId() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_animate_sticker;
    }

    @Override
    public void initView() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mZoomOutButton = (RelativeLayout) findViewById(R.id.zoomOut);
        mZoomInButton = (RelativeLayout) findViewById(R.id.zoomIn);
        mCurrentPlaytime = (TextView) findViewById(R.id.currentPlaytime);
        mVideoPlay = (ImageView) findViewById(R.id.videoPlay);
        mTimelineEditor = (NvsTimelineEditor) findViewById(R.id.timelineEditor);
        mAddAnimateSticker = (TextView) findViewById(R.id.addAnimateSticker);
        mStickerFinish = (ImageView) findViewById(R.id.stickerFinish);
        mMultiThumbnailSequenceView = mTimelineEditor.getMultiThumbnailSequenceView();

        mTitleBar.setRightText(getString(R.string.save));
    }


    @Override
    public void initObject() {
        currenTimeline = CustomTimelineUtil.createcCopyTimeline(TimelineManager.getInstance().getCurrenTimeline());
        mTimeline = currenTimeline.getTimeline();
        if (mTimeline == null)
            return;
        mStickerDataListClone = currenTimeline.getTimeData().cloneStickerData();
        initVideoFragment();
        setPlaytimeText(0);
        initMultiSequence();
        //添加贴纸
        addAllTimeSpan();
        selectAnimateSticker();
        selectTimeSpan();
    }

    @Override
    public void initEvents() {
        mZoomOutButton.setOnClickListener(this);
        mZoomInButton.setOnClickListener(this);
        mVideoPlay.setOnClickListener(this);
        mAddAnimateSticker.setOnClickListener(this);
        mStickerFinish.setOnClickListener(this);
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                finish();
            }

            @Override
            public void OnNextTextClick() {
                mStreamingContext.stop();
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setStickerData(mStickerDataListClone);
                removeTimeline();
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
                EventBus.getDefault().post(new MessageEvent("sticker"));
                AppManager.getInstance().finishActivity();
            }
        });

        mTimelineEditor.setOnScrollListener(new NvsTimelineEditor.OnScrollChangeListener() {
            @Override
            public void onScrollX(long timeStamp) {
                if (!mIsSeekTimeline)
                    return;
                setPlaytimeText(timeStamp);
                selectAnimateStickerAndTimeSpan();
                mVideoFragment.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
            }
        });
        mMultiThumbnailSequenceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mIsSeekTimeline = true;
                return false;
            }
        });

        mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                m_handler.sendEmptyMessage(VIDEOPLAYTOEOF);
            }

            @Override
            public void playStopped(NvsTimeline timeline) {
                selectAnimateStickerAndTimeSpan();
            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                setPlaytimeText(stamp);
                mVideoFragment.setDrawRectVisible(View.GONE);
                mTimelineEditor.unSelectAllTimeSpan();
                if (mMultiThumbnailSequenceView != null) {
                    int x = Math.round((stamp / (float) mTimeline.getDuration() * mTimelineEditor.getSequenceWidth()));
                    mMultiThumbnailSequenceView.smoothScrollTo(x, 0);
                }
            }

            @Override
            public void streamingEngineStateChanged(int state) {
                if (state == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    mVideoPlay.setBackgroundResource(R.mipmap.icon_pause);
                    mIsSeekTimeline = false;
                } else {
                    mVideoPlay.setBackgroundResource(R.mipmap.icon_start);
                    mIsSeekTimeline = true;
                }
            }
        });
        mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
            @Override
            public void onAssetDelete() {
                int zVal = (int) mCurAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.remove(index);
                }
                deleteAnimateSticker();
            }

            @Override
            public void onAssetSelected(PointF curPoint) {
                mVideoFragment.selectAnimateStickerByHandClick(curPoint);
                mCurAnimatedSticker = mVideoFragment.getCurAnimateSticker();
                mVideoFragment.updateAnimateStickerCoordinate(mCurAnimatedSticker);
                updateStickerMuteVisible();
                mVideoFragment.changeStickerRectVisible();
                selectTimeSpan();
            }

            @Override
            public void onAssetTranstion() {
                if (mCurAnimatedSticker == null)
                    return;
                int zVal = (int) mCurAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0)
                    mStickerDataListClone.get(index).setTranslation(mCurAnimatedSticker.getTranslation());
            }

            @Override
            public void onAssetScale() {
                if (mCurAnimatedSticker == null)
                    return;
                int zVal = (int) mCurAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setTranslation(mCurAnimatedSticker.getTranslation());
                    mStickerDataListClone.get(index).setScaleFactor(mCurAnimatedSticker.getScale());
                    mStickerDataListClone.get(index).setRotation(mCurAnimatedSticker.getRotationZ());
                }
            }

            @Override
            public void onAssetAlign(int alignVal) {

            }

            @Override
            public void onAssetHorizFlip(boolean isHorizFlip) {
                if (mCurAnimatedSticker == null)
                    return;
                int zVal = (int) mCurAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setHorizFlip(mCurAnimatedSticker.getHorizontalFlip());
                }
            }
        });
        mVideoFragment.setStickerMuteListener(new VideoFragment.OnStickerMuteListener() {
            @Override
            public void onStickerMute() {
                if (mCurAnimatedSticker == null)
                    return;
                float volumeGain = mCurAnimatedSticker.getVolumeGain().leftVolume;
                int zVal = (int) mCurAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setVolumeGain(volumeGain);
                }
            }
        });

    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        if (id == R.id.zoomIn) {
            mIsSeekTimeline = false;
            mTimelineEditor.ZoomInSequence();

        } else if (id == R.id.zoomOut) {
            mIsSeekTimeline = false;
            mTimelineEditor.ZoomOutSequence();

        } else if (id == R.id.videoPlay) {
            playVideo();

        } else if (id == R.id.addAnimateSticker) {
            long inPoint = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long stickerDuration = 4 * Constants.NS_TIME_BASE;
            long duration = mTimeline.getDuration();
            long outPoint = inPoint + stickerDuration;
            if (outPoint > duration) {
                stickerDuration = duration - inPoint;
                if (stickerDuration <= Constants.NS_TIME_BASE) {
                    stickerDuration = Constants.NS_TIME_BASE;
                    inPoint = duration - stickerDuration;
                    if (duration <= Constants.NS_TIME_BASE) {
                        stickerDuration = duration;
                        inPoint = 0;
                    }
                }
            }
            BackupData.instance().setCurSeekTimelinePos(inPoint);
            BackupData.instance().setStickerDuration(stickerDuration);
            BackupData.instance().setStickerData(mStickerDataListClone);
            mAddAnimateSticker.setClickable(false);
            AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AnimateStickerAssetActivity.class, null, REQUESTANIMATESTICKERID);

        } else if (id == R.id.stickerFinish) {
            mStreamingContext.stop();
            TimelineManager.getInstance().getCurrenTimeline().getTimeData().setStickerData(mStickerDataListClone);
            removeTimeline();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            AppManager.getInstance().finishActivity();

        } else {
        }
    }

    private void playVideo() {
        if (mVideoFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            long startTime = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long endTime = mTimeline.getDuration();
            mVideoFragment.playVideo(startTime, endTime);
        } else {
            mVideoFragment.stopEngine();
        }
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline() {
        CustomTimelineUtil.removeTimeline(currenTimeline);
        mTimeline = null;
        m_handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case REQUESTANIMATESTICKERID:
                mStickerDataListClone = BackupData.instance().getStickerData();

                currenTimeline.getTimeData().setStickerData(mStickerDataListClone);

                mStickerDataListClone = currenTimeline.getTimeData().cloneStickerData();
                CustomTimelineUtil.setAllSticker(currenTimeline);
                mCurAnimatedSticker = null;
                mTimelineEditor.deleteAllTimeSpan();
                mTimeSpanInfoList.clear();
                addAllTimeSpan();
                selectAnimateStickerAndTimeSpan();
                mVideoFragment.seekTimeline(BackupData.instance().getCurSeekTimelinePos(), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);

                break;
            default:
                break;
        }
    }

    private void selectAnimateStickerAndTimeSpan() {
        selectAnimateSticker();
        mVideoFragment.setCurAnimateSticker(mCurAnimatedSticker);
        mVideoFragment.updateAnimateStickerCoordinate(mCurAnimatedSticker);
        updateStickerMuteVisible();
        mVideoFragment.changeStickerRectVisible();
        if (mCurAnimatedSticker != null) {
            selectTimeSpan();
        } else {
            mTimelineEditor.unSelectAllTimeSpan();
        }
    }

    private void deleteAnimateSticker() {
        deleteCurStickerTimeSpan();
        mTimeline.removeAnimatedSticker(mCurAnimatedSticker);
        mCurAnimatedSticker = null;
        selectAnimateStickerAndTimeSpan();
        mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
    }

    private void deleteCurStickerTimeSpan() {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mTimeSpanInfoList.get(i).mAnimateSticker == mCurAnimatedSticker) {
                mTimelineEditor.deleteSelectedTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                mTimeSpanInfoList.remove(i);
                break;
            }
        }
    }

    private NvsTimelineTimeSpan addTimeSpan(long inPoint, long outPoint) {
        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpan");
        final NvsTimelineTimeSpan timelineTimeSpan = mTimelineEditor.addTimeSpan(inPoint, outPoint);
        if (timelineTimeSpan == null) {
            Log.e(TAG, "addTimeSpan: " + " 添加TimeSpan失败!");
            return null;
        }
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimInChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                mVideoFragment.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
                mVideoFragment.changeStickerRectVisible();
                if (isDragEnd) {
                    if (mCurAnimatedSticker == null)
                        return;
                    mCurAnimatedSticker.changeInPoint(timeStamp);
                    seekMultiThumbnailSequenceView();
                    int zVal = (int) mCurAnimatedSticker.getZValue();
                    int index = getAnimateStickerIndex(zVal);
                    if (index >= 0) {
                        mStickerDataListClone.get(index).setInPoint(mCurAnimatedSticker.getInPoint());
                        mStickerDataListClone.get(index).setMasterInPoint(mCurAnimatedSticker.getInPoint() + TimelineManager.getInstance().getCurrentPreviousAllTime());
                    }
                    mVideoFragment.changeStickerRectVisible();
                }
            }
        });
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimOutChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                //outPoint是开区间，seekTimeline时，需要往前平移一帧即0.04秒，转换成微秒即40000微秒
                mVideoFragment.seekTimeline(timeStamp - 40000, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
                mVideoFragment.changeStickerRectVisible();
                if (isDragEnd) {
                    if (mCurAnimatedSticker == null)
                        return;
                    mCurAnimatedSticker.changeOutPoint(timeStamp);
                    seekMultiThumbnailSequenceView();
                    int zVal = (int) mCurAnimatedSticker.getZValue();
                    int index = getAnimateStickerIndex(zVal);
                    if (index >= 0) {
                        long duaration = timeStamp - mCurAnimatedSticker.getInPoint();
                        mStickerDataListClone.get(index).setDuration(duaration);
                    }
                }
            }
        });

        return timelineTimeSpan;
    }

    private void seekMultiThumbnailSequenceView() {
        if (mMultiThumbnailSequenceView != null) {
            long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long duration = mTimeline.getDuration();
            mMultiThumbnailSequenceView.scrollTo(Math.round(((float) curPos) / (float) duration * mTimelineEditor.getSequenceWidth()), 0);
        }
    }

    private void addAllTimeSpan() {
        //设置贴纸模式
        mVideoFragment.setEditMode(Constants.EDIT_MODE_STICKER);
        NvsTimelineAnimatedSticker animatedSticker = mTimeline.getFirstAnimatedSticker();
        while (animatedSticker != null) {
            long inPoint = animatedSticker.getInPoint();
            long outPoint = animatedSticker.getOutPoint();
            NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint, outPoint);
            if (timeSpan != null) {
                AnimateStickerActivity.AnimateStickerTimeSpanInfo timeSpanInfo = new AnimateStickerActivity.AnimateStickerTimeSpanInfo(animatedSticker, timeSpan);
                mTimeSpanInfoList.add(timeSpanInfo);
            }
            animatedSticker = mTimeline.getNextAnimatedSticker(animatedSticker);
        }
    }

    private void selectAnimateSticker() {
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineAnimatedSticker> animateStickerList = mTimeline.getAnimatedStickersByTimelinePosition(curPos);
        int stickerCount = animateStickerList.size();
        if (stickerCount > 0) {
            float zVal = animateStickerList.get(0).getZValue();
            int index = 0;
            for (int i = 0; i < animateStickerList.size(); i++) {
                float tmpZVal = animateStickerList.get(i).getZValue();
                if (tmpZVal > zVal) {
                    zVal = tmpZVal;
                    index = i;
                }
            }
            mCurAnimatedSticker = animateStickerList.get(index);
        } else {
            mCurAnimatedSticker = null;
        }
    }

    private void selectTimeSpan() {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mTimeSpanInfoList.get(i).mAnimateSticker == mCurAnimatedSticker) {
                mTimelineEditor.selectTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                break;
            }
        }
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.setCurAnimateSticker(mCurAnimatedSticker);
                mStickerFinish.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoFragment.updateAnimateStickerCoordinate(mCurAnimatedSticker);
                        mVideoFragment.changeStickerRectVisible();
                        updateStickerMuteVisible();
                        mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
                    }
                }, 100);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineManager.getInstance().getCurrenTimeline().getTimeData().getMakeRatio());
        bundle.putBoolean("playBarVisible", false);
        mVideoFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.spaceLayout, mVideoFragment)
                .commit();
    }

    private void updateStickerMuteVisible() {
        if (mCurAnimatedSticker != null) {
            boolean hasAudio = mCurAnimatedSticker.hasAudio();
            mVideoFragment.setMuteVisible(hasAudio);
            if (hasAudio) {
                float leftVolume = (int) mCurAnimatedSticker.getVolumeGain().leftVolume;
                mVideoFragment.setStickerMuteIndex(leftVolume > 0 ? 0 : 1);
            }
        }
    }

    private void initMultiSequence() {
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if (videoTrack == null)
            return;
        int clipCount = videoTrack.getClipCount();
        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> sequenceDescsArray = new ArrayList<>();
        for (int index = 0; index < clipCount; ++index) {
            NvsVideoClip videoClip = videoTrack.getClipByIndex(index);
            if (videoClip == null)
                continue;

            NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc sequenceDescs = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
            sequenceDescs.mediaFilePath = videoClip.getFilePath();
            sequenceDescs.trimIn = videoClip.getTrimIn();
            sequenceDescs.trimOut = videoClip.getTrimOut();
            sequenceDescs.inPoint = videoClip.getInPoint();
            sequenceDescs.outPoint = videoClip.getOutPoint();
            sequenceDescs.stillImageHint = false;
            sequenceDescsArray.add(sequenceDescs);
        }
        long duration = mTimeline.getDuration();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mVideoPlay.getLayoutParams();
        int playBtnTotalWidth = layoutParams.width + layoutParams.leftMargin + layoutParams.rightMargin;
        int halfScreenWidth = ScreenUtils.getScreenWidth(this) / 2;
        int sequenceLeftPadding = halfScreenWidth - playBtnTotalWidth;
        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
        mTimelineEditor.setSequencRightPadding(halfScreenWidth - ScreenUtils.dip2px(this, 14));
        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray, duration);
    }

    private void setPlaytimeText(long playTime) {
        long totalDuaration = mTimeline.getDuration();
        String totalStr = TimeFormatUtil.formatUsToString1(totalDuaration);
        String playTimeStr = TimeFormatUtil.formatUsToString1(playTime);
        String tmpStr = playTimeStr + "/" + totalStr;
        mCurrentPlaytime.setText(tmpStr);
    }

    private void resetView() {
        setPlaytimeText(0);
        mVideoPlay.setBackgroundResource(R.mipmap.icon_start);
        mVideoFragment.seekTimeline(0, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
        mMultiThumbnailSequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
    }

    private int getAnimateStickerIndex(int curZValue) {
        int index = -1;
        int count = mStickerDataListClone.size();
        for (int i = 0; i < count; ++i) {
            int zVal = mStickerDataListClone.get(i).getAnimateStickerZVal();
            if (curZValue == zVal) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddAnimateSticker.setClickable(true);

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("AnimateStickerActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("AnimateStickerActivity");
            MobclickAgent.onPause(this);
        }
    }
}
