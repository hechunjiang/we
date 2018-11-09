package wedemo.activity.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.R;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import wedemo.MessageEvent;
import wedemo.activity.data.BackupData;
import wedemo.activity.data.BitmapData;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.base.BaseActivity;
import wedemo.utils.AppManager;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.ScreenUtils;
import wedemo.utils.TimeFormatUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.Util;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.TimelineData;
import wedemo.view.timelineEditor.NvsTimelineEditor;
import wedemo.view.timelineEditor.NvsTimelineTimeSpanExt;

public class VideoCuttingActivity extends BaseActivity {
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private TextView mTrimDurationVal;
    private NvsTimelineEditor mTimelineEditor;
    private ImageView mTrimFinish;
    private long mTrimInPoint = 0;
    private long mTrimOutPoint = 0;
    private SingleClipFragment mClipFragment;
    NvsTimelineTimeSpanExt mTimlineTimeSpanExt;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    ArrayList<ClipInfo> mClipArrayList;
    private int mCurClipIndex = 0;
    private CustomTimeLine customTimeLine;

    @Override
    public int getLayoutId() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_cutting;
    }

    @Override
    public void initView() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        mTrimDurationVal = (TextView) findViewById(R.id.trimDurationVal);
        mTimelineEditor = (NvsTimelineEditor) findViewById(R.id.timelineEditor);
        mTrimFinish = (ImageView) findViewById(R.id.trimFinish);
        mTitleBar.setRightText(getString(R.string.save));
    }

    @Override
    public void initObject() {
        mClipArrayList = TimelineData.instance().cloneClipInfoData();
        mCurClipIndex = TimelineManager.getInstance().getBranch();

        BackupData.instance().setClipInfoData(mClipArrayList);

        customTimeLine = CustomTimelineUtil.createTimeline(TimelineManager.getInstance().getCurrenTimeline(), mClipArrayList.get(mCurClipIndex), true);

        mTimeline = customTimeLine.getTimeline();
        if (mTimeline == null)
            return;
        updateClipInfo();
        initVideoFragment();
        initMultiSequence();
    }

    @Override
    public void initEvents() {
        mTrimFinish.setOnClickListener(this);

        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
                //AppManager.getInstance().finishActivity();
                finish();
            }

            @Override
            public void OnNextTextClick() {
                customTimeLine = CustomTimelineUtil.cut(customTimeLine, mTrimInPoint, mTrimOutPoint);
                Bitmap bitmap = Util.getBitmapFromClipInfo(VideoCuttingActivity.this, mClipArrayList.get(mCurClipIndex));
                BitmapData.instance().replaceBitmap(mCurClipIndex, bitmap);

                BackupData.instance().setClipInfoData(mClipArrayList);
                TimelineManager.getInstance().getCurrenTimeline().setTimeData(customTimeLine.getTimeData());
                removeTimeline();
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
                Log.e("weiwei", "edit set result");
                EventBus.getDefault().post(new MessageEvent("edit"));
                finish();
            }
        });

        if (mTimlineTimeSpanExt != null) {
            mTimlineTimeSpanExt.setOnChangeListener(new NvsTimelineTimeSpanExt.OnTrimInChangeListener() {
                @Override
                public void onChange(long timeStamp, boolean isDragEnd) {
                    mTrimInPoint = timeStamp;
                    long totalDuration = mTrimOutPoint - mTrimInPoint;
                    //setTrimDurationText(totalDuration);
                    setTrimDurationText(mTrimInPoint, mTrimOutPoint);
                    float speed = mClipArrayList.get(mCurClipIndex).getSpeed();
                    long newTrimIn = (long) (timeStamp * speed);
                    mClipArrayList.get(mCurClipIndex).changeTrimIn(newTrimIn);

                    if (isDragEnd) {
                        mClipFragment.playVideo(mTrimInPoint, mTrimOutPoint);
                    } else {
                        mClipFragment.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                    }

                }
            });
            mTimlineTimeSpanExt.setOnChangeListener(new NvsTimelineTimeSpanExt.OnTrimOutChangeListener() {
                @Override
                public void onChange(long timeStamp, boolean isDragEnd) {
                    mTrimOutPoint = timeStamp;
                    long totalDuration = mTrimOutPoint - mTrimInPoint;

                    //setTrimDurationText(totalDuration);
                    setTrimDurationText(mTrimInPoint, mTrimOutPoint);
                    float speed = mClipArrayList.get(mCurClipIndex).getSpeed();
                    long newTrimOut = (long) (timeStamp * speed);
                    mClipArrayList.get(mCurClipIndex).changeTrimOut(newTrimOut);

                    if (isDragEnd) {
                        mClipFragment.playVideo(mTrimInPoint, mTrimOutPoint);
                    } else {
                        mClipFragment.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                    }
                }
            });
        }
//        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
//            @Override
//            public void OnBackImageClick() {
//                removeTimeline();
//            }
//
//            @Override
//            public void OnNextTextClick() {
//                customTimeLine = CustomTimelineUtil.cut(customTimeLine,mTrimInPoint, mTrimOutPoint);
//
//                Bitmap bitmap = Util.getBitmapFromClipInfo(VideoCuttingActivity.this, mClipArrayList.get(mCurClipIndex));
//                BitmapData.instance().replaceBitmap(mCurClipIndex, bitmap);
//                BackupData.instance().setClipInfoData(mClipArrayList);
//
//                TimelineManager.getInstance().getCurrenTimeline().setTimeData(customTimeLine.getTimeData());
//
//                removeTimeline();
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                AppManager.getInstance().finishActivity();
//            }
//
//
//        });

        mClipFragment.setVideoFragmentCallBack(new SingleClipFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mClipFragment.playVideo(mTrimInPoint, mTrimOutPoint);
                    }
                });
            }

            @Override
            public void playStopped(NvsTimeline timeline) {

            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {

            }

            @Override
            public void streamingEngineStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        if (id == R.id.trimFinish) {//customTimeLine = CustomTimelineUtil.cut(customTimeLine,mTrimInPoint, mTrimOutPoint);

            Bitmap bitmap = Util.getBitmapFromClipInfo(this, mClipArrayList.get(mCurClipIndex));
            BitmapData.instance().replaceBitmap(mCurClipIndex, bitmap);
            BackupData.instance().setClipInfoData(mClipArrayList);

            TimelineManager.getInstance().getCurrenTimeline().setTimeData(customTimeLine);
            removeTimeline();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            AppManager.getInstance().finishActivity();

            TimelineData.instance().setClipInfoData(mClipArrayList);


        } else {
        }
    }


    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline() {
        mClipFragment.stopEngine();
        CustomTimelineUtil.removeTimeline(customTimeLine);
        mTimeline = null;
    }

    private void updateClipInfo() {
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if (videoTrack == null)
            return;
        NvsVideoClip videoClip = videoTrack.getClipByIndex(0);
        if (videoClip == null)
            return;
        long trimIn = mClipArrayList.get(mCurClipIndex).getTrimIn();
        if (trimIn < 0)
            mClipArrayList.get(mCurClipIndex).changeTrimIn(videoClip.getTrimIn());
        long trimOut = mClipArrayList.get(mCurClipIndex).getTrimOut();
        if (trimOut < 0)
            mClipArrayList.get(mCurClipIndex).changeTrimOut(videoClip.getTrimOut());
    }

    private void initMultiSequence() {
        long duration = mTimeline.getDuration();
        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> sequenceDescsArray = new ArrayList<>();
        NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc sequenceDescs = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
        sequenceDescs.mediaFilePath = mClipArrayList.get(mCurClipIndex).getFilePath();
        sequenceDescs.trimIn = 0;
        sequenceDescs.trimOut = duration;
        sequenceDescs.inPoint = 0;
        sequenceDescs.outPoint = duration;
        sequenceDescs.stillImageHint = false;
        sequenceDescsArray.add(sequenceDescs);
        double pixelPerMicrosecond = getPixelMicrosecond(duration);
        mTimelineEditor.setPixelPerMicrosecond(pixelPerMicrosecond);
        int half = ScreenUtils.dip2px(this, 13);

        mTimelineEditor.setSequencLeftPadding(half);
        mTimelineEditor.setSequencRightPadding(half);
        mTimelineEditor.setTimeSpanLeftPadding(half);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray, duration);
        mTimelineEditor.getMultiThumbnailSequenceView().getLayoutParams().height = ScreenUtils.dip2px(this, 64);
        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpanExt");
        float speed = mClipArrayList.get(mCurClipIndex).getSpeed();
        mTrimInPoint = (long) (mClipArrayList.get(mCurClipIndex).getTrimIn() / speed);
        mTrimOutPoint = (long) (mClipArrayList.get(mCurClipIndex).getTrimOut() / speed);
        mTimlineTimeSpanExt = mTimelineEditor.addTimeSpanExt(mTrimInPoint, mTrimOutPoint);
        setTrimDurationText(mTrimInPoint, mTrimOutPoint);
        //setTrimDurationText(mTrimOutPoint - mTrimInPoint);
    }

    private void setTrimDurationText(long duration) {
        String totalStr = "裁剪后总时长为" + TimeFormatUtil.formatUsToString1(duration);
        mTrimDurationVal.setText(totalStr);
    }

    private void setTrimDurationText(long inTime, long outTime) {
        String startTime = TimeFormatUtil.formatUsToString1(inTime);
        String endTime = TimeFormatUtil.formatUsToString1(outTime);
        mTrimDurationVal.setText(startTime + " / " + endTime);
    }

    private double getPixelMicrosecond(long duration) {
        int width = ScreenUtils.getScreenWidth(VideoCuttingActivity.this);
        int leftPadding = ScreenUtils.dip2px(this, 13);
        int sequenceWidth = width - 2 * leftPadding;
        double pixelMicrosecond = sequenceWidth / (double) duration;
        return pixelMicrosecond;
    }

    private void initVideoFragment() {
        mClipFragment = new SingleClipFragment();
        mClipFragment.setFragmentLoadFinisedListener(new SingleClipFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mClipFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
            }
        });
        mClipFragment.setTimeline(mTimeline);
        mClipFragment.setIsTrimActivity(true);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        mClipFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.spaceLayout, mClipFragment)
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("VideoCuttingActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("VideoCuttingActivity");
            MobclickAgent.onPause(this);
        }
    }
}
