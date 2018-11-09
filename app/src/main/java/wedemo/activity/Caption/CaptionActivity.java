package wedemo.activity.Caption;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import wedemo.MessageEvent;
import wedemo.activity.data.BackupData;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.activity.view.InputDialog;
import wedemo.base.BaseActivity;
import wedemo.fragment.VideoFragment;
import wedemo.utils.AppManager;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.ScreenUtils;
import wedemo.utils.TimeFormatUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.Util;
import wedemo.utils.dataInfo.CaptionInfo;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.view.timelineEditor.NvsTimelineEditor;
import wedemo.view.timelineEditor.NvsTimelineTimeSpan;

public class CaptionActivity extends BaseActivity {
    private static final String TAG = "CaptionActivity";
    private static final int VIDEOPLAYTOEOF = 105;
    private static final int REQUESTCAPTIONSTYLE = 103;
    private CustomTitleBar mTitleBar;
    private TextView mPlayCurTime;
    private RelativeLayout mZoomInBtn;
    private RelativeLayout mZoomOutBtn;
    private Button mCaptionStyleButton;
    private NvsTimelineEditor mTimelineEditor;
    private Button mPlayBtn;
    private Button mAddCaptionBtn;
    private Button mOkBtn;
    private VideoFragment mVideoFragment;
    private RelativeLayout mBottomRelativeLayout;
    private RelativeLayout mPlayBtnLayout;
    private NvsMultiThumbnailSequenceView mMultiSequenceView;

    private boolean mIsSeekTimeline = true;
    private NvsTimelineCaption mCurCaption;
    private NvsStreamingContext mStreamingContext;
    private List<CaptionTimeSpanInfo> mTimeSpanInfoList = new ArrayList<>();
    private CaptionActivity.CaptionHandler m_handler = new CaptionActivity.CaptionHandler(this);
    private ArrayList<CaptionInfo> mCaptionDataListClone;
    private boolean mIsInnerDrawRect = false;

    private CustomTimeLine mCustomTimeline;
    private NvsTimeline mTimeline;
    private ImageButton ib_clear;
    private ImageButton ib_ok;
    private EditText et_input;

    static class CaptionHandler extends Handler {
        WeakReference<CaptionActivity> mWeakReference;

        public CaptionHandler(CaptionActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CaptionActivity activity = mWeakReference.get();
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

    private void resetView() {
        updatePlaytimeText(0);
        mVideoFragment.seekTimeline(0, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
        mMultiSequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
    }

    private void selectCaptionAndTimeSpan() {
        selectCaption();
        mVideoFragment.setCurCaption(mCurCaption);
        mVideoFragment.updateCaptionCoordinate(mCurCaption);
        mVideoFragment.changeCaptionRectVisible();
        if (mCurCaption != null) {
            int alignVal = mCurCaption.getTextAlignment();
            mVideoFragment.setAlignIndex(alignVal);
        }
        if (mCurCaption != null) {
            selectTimeSpan();
        } else {
            mTimelineEditor.unSelectAllTimeSpan();
        }
    }

    @Override
    public void initEvents() {

        ib_clear.setOnClickListener(this);
        ib_ok.setOnClickListener(this);
        mZoomInBtn.setOnClickListener(this);
        mZoomOutBtn.setOnClickListener(this);
        mCaptionStyleButton.setOnClickListener(this);
        mAddCaptionBtn.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);

        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
                AppManager.getInstance().finishActivity();
                finish();
            }

            @Override
            public void OnNextTextClick() {
                mStreamingContext.stop();
                removeTimeline();
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setCaptionData(mCaptionDataListClone);
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
                EventBus.getDefault().post(new MessageEvent("caption"));
                AppManager.getInstance().finishActivity();
            }
        });

        mTimelineEditor.setOnScrollListener(new NvsTimelineEditor.OnScrollChangeListener() {
            @Override
            public void onScrollX(long timeStamp) {
                if (!mIsSeekTimeline)
                    return;
                updatePlaytimeText(timeStamp);
                selectCaptionAndTimeSpan();
                mVideoFragment.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
            }
        });

        mMultiSequenceView.setOnTouchListener(new View.OnTouchListener() {
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
                selectCaptionAndTimeSpan();
            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                updatePlaytimeText(stamp);
                mVideoFragment.setDrawRectVisible(View.GONE);
                mTimelineEditor.unSelectAllTimeSpan();
                if (mMultiSequenceView != null) {
                    int x = Math.round((stamp / (float) mTimeline.getDuration() * mTimelineEditor.getSequenceWidth()));
                    mMultiSequenceView.smoothScrollTo(x, 0);
                }
            }

            @Override
            public void streamingEngineStateChanged(int state) {
                if (NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK == state) {
                    mIsSeekTimeline = false;
                    mPlayBtn.setBackgroundResource(R.mipmap.icon_pause);
                } else {
                    mPlayBtn.setBackgroundResource(R.mipmap.icon_start);
                    mIsSeekTimeline = true;
                }
            }
        });
        mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
            @Override
            public void onAssetDelete() {
                deleteCurCaptionTimeSpan();
                int zVal = (int) mCurCaption.getZValue();
                int index = getCaptionIndex(zVal);
                if (index >= 0) {
                    mCaptionDataListClone.remove(index);
                }
                mTimeline.removeCaption(mCurCaption);
                mCurCaption = null;
                selectCaptionAndTimeSpan();
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
            }

            @Override
            public void onAssetSelected(PointF curPoint) {
                //判断若没有选中当前字幕框则选中，选中则不处理
                mIsInnerDrawRect = mVideoFragment.curPointIsInnerDrawRect((int) curPoint.x, (int) curPoint.y);
                if (!mIsInnerDrawRect) {
                    mVideoFragment.selectCaptionByHandClick(curPoint);
                    mCurCaption = mVideoFragment.getCurCaption();
                    selectTimeSpan();
                    if (mCurCaption != null) {
                        int alignVal = mCurCaption.getTextAlignment();
                        mVideoFragment.setAlignIndex(alignVal);
                    }
                }
            }

            @Override
            public void onAssetTranstion() {
                if (mCurCaption == null)
                    return;
                PointF pointF = mCurCaption.getCaptionTranslation();
                //Log.e(TAG,"pointF.x = " + pointF.x + "pointF.y =" + pointF.y);
                int zVal = (int) mCurCaption.getZValue();
                int index = getCaptionIndex(zVal);


                if (index >= 0) {
                    mCaptionDataListClone.get(index).setTranslation(pointF);
                }
            }

            @Override
            public void onAssetScale() {
                if (mCurCaption == null)
                    return;
                int zVal = (int) mCurCaption.getZValue();
                int index = getCaptionIndex(zVal);
                if (index >= 0) {
                    mCaptionDataListClone.get(index).setScaleFactorX(mCurCaption.getScaleX());
                    mCaptionDataListClone.get(index).setScaleFactorY(mCurCaption.getScaleY());
                    mCaptionDataListClone.get(index).setAnchor(mCurCaption.getAnchorPoint());
                    mCaptionDataListClone.get(index).setRotation(mCurCaption.getRotationZ());
                    mCaptionDataListClone.get(index).setCaptionSize(mCurCaption.getFontSize());
                    PointF pointF = mCurCaption.getCaptionTranslation();
                    mCaptionDataListClone.get(index).setTranslation(pointF);

                }
            }

            @Override
            public void onAssetAlign(int alignVal) {
                int zVal = (int) mCurCaption.getZValue();
                int index = getCaptionIndex(zVal);
                if (index >= 0) {
                    mCaptionDataListClone.get(index).setAlignVal(alignVal);
                }
            }

            @Override
            public void onAssetHorizFlip(boolean isHorizFlip) {

            }
        });
        mVideoFragment.setCaptionTextEditListener(new VideoFragment.VideoCaptionTextEditListener() {
            @Override
            public void onCaptionTextEdit() {
                if (!mIsInnerDrawRect)
                    return;
                new InputDialog(CaptionActivity.this, R.style.dialog, new InputDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean ok) {
                        if (ok) {
                            InputDialog d = (InputDialog) dialog;
                            String userInputText = d.getUserInputText();
                            mCurCaption.setText(userInputText);
                            mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                            mVideoFragment.updateCaptionCoordinate(mCurCaption);
                            mVideoFragment.changeCaptionRectVisible();
                            int zVal = (int) mCurCaption.getZValue();
                            int index = getCaptionIndex(zVal);
                            if (index >= 0) {
                                mCaptionDataListClone.get(index).setText(userInputText);
                            }

                        } else {
                            dialog.dismiss();
                        }
                    }
                }).show();
                mIsInnerDrawRect = false;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_caption;
    }

    @Override
    public void initView() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mPlayCurTime = (TextView) findViewById(R.id.play_cur_time);
        mZoomInBtn = (RelativeLayout) findViewById(R.id.zoom_in_btn);
        mZoomOutBtn = (RelativeLayout) findViewById(R.id.zoom_out_btn);
        mCaptionStyleButton = (Button) findViewById(R.id.captionStyleButton);
        mTimelineEditor = (NvsTimelineEditor) findViewById(R.id.caption_timeline_editor);
        mPlayBtn = (Button) findViewById(R.id.play_btn);
        mAddCaptionBtn = (Button) findViewById(R.id.add_caption_btn);
        mOkBtn = (Button) findViewById(R.id.ok_btn);
        mBottomRelativeLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mPlayBtnLayout = (RelativeLayout) findViewById(R.id.play_btn_layout);
        mMultiSequenceView = mTimelineEditor.getMultiThumbnailSequenceView();
        ib_clear = (ImageButton) findViewById(R.id.ib_clear);
        et_input = (EditText) findViewById(R.id.et_input);
        ib_ok = (ImageButton) findViewById(R.id.ib_ok);
        mTitleBar.setRightText(getString(R.string.save));
    }

    @Override
    public void onClickEvent(View v) {
        int i = v.getId();
        if (i == R.id.zoom_in_btn) {
            mIsSeekTimeline = false;
            mTimelineEditor.ZoomInSequence();

        } else if (i == R.id.zoom_out_btn) {
            mIsSeekTimeline = false;
            mTimelineEditor.ZoomOutSequence();

        } else if (i == R.id.captionStyleButton) {
            mCaptionStyleButton.setClickable(false);
            BackupData.instance().setCaptionData(mCaptionDataListClone);
            BackupData.instance().setCaptionZVal((int) mCurCaption.getZValue());
            BackupData.instance().setCurSeekTimelinePos(mStreamingContext.getTimelineCurrentPosition(mTimeline));
            AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), CaptionStyleActivity.class, null, REQUESTCAPTIONSTYLE);

        } else if (i == R.id.add_caption_btn) {
            new InputDialog(this, R.style.dialog, new InputDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean ok) {
                    if (ok) {
                        InputDialog d = (InputDialog) dialog;
                        String userInputText = d.getUserInputText();
                        addCaption(userInputText);
                    } else {
                        dialog.dismiss();
                    }
                }
            }).show();
        } else if (i == R.id.ok_btn) {
            mStreamingContext.stop();
            removeTimeline();
            TimelineManager.getInstance().getCurrenTimeline().getTimeData().setCaptionData(mCaptionDataListClone);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            AppManager.getInstance().finishActivity();

        } else if (i == R.id.play_btn) {
            playVideo();

        } else if (i == R.id.ib_clear) {
            et_input.setText("");
        } else if (i == R.id.ib_ok) {
            if (TextUtils.isEmpty(et_input.getText())) {
                ToastUtils.showShort(this, getString(R.string.input_text_tip));
                return;
            }
            String userInputText = et_input.getText().toString();
            addCaption(userInputText);
            et_input.setText("");
        }
    }

    @Override
    public void initObject() {
        mStreamingContext = NvsStreamingContext.getInstance();

        mCustomTimeline = CustomTimelineUtil.createcCopyTimeline(TimelineManager.getInstance().getCurrenTimeline());
        if (mCustomTimeline == null)
            return;

        mTimeline = mCustomTimeline.getTimeline();
        mCaptionDataListClone = mCustomTimeline.getTimeData().cloneCaptionData();

        initVideoFragment();
        updatePlaytimeText(0);
        initMultiSequence();
        addAllTimeSpan();
        selectCaption();
        selectTimeSpan();
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case REQUESTCAPTIONSTYLE:
                mCaptionDataListClone = BackupData.instance().getCaptionData();
                mCurCaption = null;
                CustomTimelineUtil.setAllCaption(mCustomTimeline);
                mTimelineEditor.deleteAllTimeSpan();
                mTimeSpanInfoList.clear();
                addAllTimeSpan();
                long curSeekPos = BackupData.instance().getCurSeekTimelinePos();
                mVideoFragment.seekTimeline(curSeekPos, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                boolean isSelectCurCaption = data.getBooleanExtra("isSelectCurCaption", true);
                if (!isSelectCurCaption) {
                    selectCaptionAndTimeSpan();
                } else {
                    int curZVal = BackupData.instance().getCaptionZVal();
                    selectCatpionByZVal(curZVal);
                    mVideoFragment.setCurCaption(mCurCaption);
                    mVideoFragment.updateCaptionCoordinate(mCurCaption);
                    mVideoFragment.changeCaptionRectVisible();
                    if (mCurCaption != null) {
                        int alignVal = mCurCaption.getTextAlignment();
                        mVideoFragment.setAlignIndex(alignVal);
                    }
                    selectTimeSpan();
                }
                break;
            default:
                break;
        }
    }

    private void selectCatpionByZVal(int curZVal) {
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineCaption> captionList = mTimeline.getCaptionsByTimelinePosition(curPos);
        int captionCount = captionList.size();
        if (captionCount > 0) {
            mCaptionStyleButton.setVisibility(View.VISIBLE);
            for (int i = 0; i < captionCount; i++) {
                int zVal = (int) captionList.get(i).getZValue();
                if (curZVal == zVal) {
                    mCurCaption = captionList.get(i);
                    break;
                }
            }
        } else {
            mCurCaption = null;
            mCaptionStyleButton.setVisibility(View.GONE);
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

    private void updatePlaytimeText(long playTime) {
        long totalDuaration = mTimeline.getDuration();
        String totalStr = TimeFormatUtil.formatUsToString1(totalDuaration);
        String playTimeStr = TimeFormatUtil.formatUsToString1(playTime);
        String tmpStr = playTimeStr + "/" + totalStr;
        mPlayCurTime.setText(tmpStr);
    }

    private float getCurCaptionZVal() {
        float zVal = 0.0f;
        NvsTimelineCaption caption = mTimeline.getFirstCaption();
        while (caption != null) {
            float tmpZVal = caption.getZValue();
            if (tmpZVal > zVal)
                zVal = tmpZVal;
            caption = mTimeline.getNextCaption(caption);
        }
        zVal += 1.0;
        return zVal;
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.setCurCaption(mCurCaption);
                mOkBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoFragment.updateCaptionCoordinate(mCurCaption);
                        mVideoFragment.changeCaptionRectVisible();
                        mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                    }
                }, 100);
            }
        });
        //设置字幕模式
        mVideoFragment.setEditMode(Constants.EDIT_MODE_CAPTION);
        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomRelativeLayout.getLayoutParams().height);
        bundle.putInt("ratio", mCustomTimeline.getTimeData().getMakeRatio());
        bundle.putBoolean("playBarVisible", false);
        mVideoFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.video_layout, mVideoFragment)
                .commit();
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
        int halfScreenWidth = ScreenUtils.getScreenWidth(this) / 2;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPlayBtnLayout.getLayoutParams();
        int playBtnTotalWidth = layoutParams.width + layoutParams.leftMargin + layoutParams.rightMargin;
        int sequenceLeftPadding = halfScreenWidth - playBtnTotalWidth;
        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
        //需要减去margin
        mTimelineEditor.setSequencRightPadding(halfScreenWidth - ScreenUtils.dip2px(this, 14));
        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray, duration);
    }

    //添加字幕
    private void addCaption(String caption) {
        long inPoint = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        long captionDuration = 4 * Constants.NS_TIME_BASE;
        long outPoint = inPoint + captionDuration;
        long duration = mTimeline.getDuration();

        if (outPoint > duration) {
            captionDuration = duration - inPoint;
            if (captionDuration <= Constants.NS_TIME_BASE) {
                captionDuration = Constants.NS_TIME_BASE;
                inPoint = duration - captionDuration;
                if (duration <= Constants.NS_TIME_BASE) {
                    captionDuration = duration;
                    inPoint = 0;
                }
            }
            outPoint = duration;
        }

        mCurCaption = mTimeline.addCaption(caption, inPoint, captionDuration, null);
        if (mCurCaption == null) {
            Log.e(TAG, "addCaption: " + " 添加字幕失败！");
            return;
        }
        float zVal = getCurCaptionZVal();
        mCurCaption.setZValue(zVal);
        NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint, outPoint);
        if (timeSpan == null) {
            Log.e(TAG, "addCaption: " + " 添加TimeSpan失败!");
            return;
        }
        mTimeSpanInfoList.add(new CaptionTimeSpanInfo(mCurCaption, timeSpan));

        //TODO:是否开放字体样式
        // mCaptionStyleButton.setVisibility(View.VISIBLE);

        mVideoFragment.setCurCaption(mCurCaption);
        mVideoFragment.updateCaptionCoordinate(mCurCaption);
        int alignVal = mCurCaption.getTextAlignment();
        mVideoFragment.setAlignIndex(alignVal);
        mVideoFragment.changeCaptionRectVisible();
        mVideoFragment.seekTimeline(inPoint, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
        selectTimeSpan();//

        CaptionInfo captionInfo = Util.saveCaptionData(mCurCaption, TimelineManager.getInstance().getCurrentPreviousAllTime(), TimelineManager.getInstance().getBranch());
        if (captionInfo != null) {
            mCaptionDataListClone.add(captionInfo);
        }
    }

    private NvsTimelineTimeSpan addTimeSpan(long inPoint, long outPoint) {
        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpan");
        NvsTimelineTimeSpan timelineTimeSpan = mTimelineEditor.addTimeSpan(inPoint, outPoint);
        if (timelineTimeSpan == null) {
            return null;
        }
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimInChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                mVideoFragment.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                mVideoFragment.changeCaptionRectVisible();
                if (isDragEnd) {
                    if (mCurCaption == null)
                        return;
                    mCurCaption.changeInPoint(timeStamp);
                    seekMultiThumbnailSequenceView();
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        mCaptionDataListClone.get(index).setInPoint(timeStamp);
                        mCaptionDataListClone.get(index).setMasterInPoint(timeStamp + TimelineManager.getInstance().getCurrentPreviousAllTime());
                    }
                }
            }
        });
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimOutChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                //outPoint是开区间，seekTimeline时，需要往前平移一帧即0.04秒，转换成微秒即40000微秒
                mVideoFragment.seekTimeline(timeStamp - 40000, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                mVideoFragment.changeCaptionRectVisible();
                if (isDragEnd) {
                    if (mCurCaption == null)
                        return;
                    mCurCaption.changeOutPoint(timeStamp);
                    seekMultiThumbnailSequenceView();
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        long inPoint = mCaptionDataListClone.get(index).getInPoint();
                        mCaptionDataListClone.get(index).setDuration(timeStamp - inPoint);
                    }
                }
            }
        });

        return timelineTimeSpan;
    }

    private void seekMultiThumbnailSequenceView() {
        if (mMultiSequenceView != null) {
            long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long duration = mTimeline.getDuration();
            mMultiSequenceView.scrollTo(Math.round(((float) curPos) / (float) duration * mTimelineEditor.getSequenceWidth()), 0);
        }
    }

    private void addAllTimeSpan() {
        NvsTimelineCaption caption = mTimeline.getFirstCaption();
        while (caption != null) {
            long inPoint = caption.getInPoint();
            long outPoint = caption.getOutPoint();
            NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint, outPoint);
            if (timeSpan != null) {
                CaptionTimeSpanInfo timeSpanInfo = new CaptionTimeSpanInfo(caption, timeSpan);
                mTimeSpanInfoList.add(timeSpanInfo);
            }
            caption = mTimeline.getNextCaption(caption);
        }
    }

    private void selectCaption() {
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineCaption> captionList = mTimeline.getCaptionsByTimelinePosition(curPos);
        int captionCount = captionList.size();
        if (captionCount > 0) {
            //TODO:是否开放字体样式
            //mCaptionStyleButton.setVisibility(View.VISIBLE);
            float zVal = captionList.get(0).getZValue();
            int index = 0;
            for (int i = 0; i < captionCount; i++) {
                float tmpZVal = captionList.get(i).getZValue();
                if (tmpZVal > zVal) {
                    zVal = tmpZVal;
                    index = i;
                }
            }
            mCurCaption = captionList.get(index);
        } else {
            mCurCaption = null;
            mCaptionStyleButton.setVisibility(View.GONE);
        }
    }

    private void selectTimeSpan() {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mCurCaption != null &&
                    mTimeSpanInfoList.get(i).mCaption == mCurCaption) {
                mTimelineEditor.selectTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                break;
            }
        }
    }

    private void deleteCurCaptionTimeSpan() {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mTimeSpanInfoList.get(i).mCaption == mCurCaption) {
                mTimelineEditor.deleteSelectedTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                mTimeSpanInfoList.remove(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline() {
        CustomTimelineUtil.removeTimeline(mCustomTimeline);
        mTimeline = null;
    }

    private class CaptionTimeSpanInfo {
        public NvsTimelineCaption mCaption;
        public NvsTimelineTimeSpan mTimeSpan;

        public CaptionTimeSpanInfo(NvsTimelineCaption caption, NvsTimelineTimeSpan timeSpan) {
            this.mCaption = caption;
            this.mTimeSpan = timeSpan;
        }
    }

    private int getCaptionIndex(int curZValue) {
        int index = -1;
        int count = mCaptionDataListClone.size();
        for (int i = 0; i < count; ++i) {
            int zVal = mCaptionDataListClone.get(i).getCaptionZVal();
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
        mCaptionStyleButton.setClickable(true);

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("CaptionActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("CaptionActivity");
            MobclickAgent.onPause(this);
        }
    }
}
