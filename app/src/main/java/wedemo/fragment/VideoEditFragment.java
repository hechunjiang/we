package wedemo.fragment;//package com.mobile.wedemo.fragment;
//
//import android.annotation.SuppressLint;
//import android.graphics.Bitmap;
//import android.os.Handler;
//import android.test.mock.MockContext;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.meicam.sdk.NvsMultiThumbnailSequenceView;
//import com.meicam.sdk.NvsStreamingContext;
//import com.meicam.sdk.NvsTimeline;
//import com.meicam.sdk.NvsVideoClip;
//import com.meicam.sdk.NvsVideoTrack;
//import com.mobile.wedemo.R;
//import com.mobile.wedemo.VideoEditActivity;
//import com.mobile.wedemo.activity.data.BackupData;
//import com.mobile.wedemo.base.BaseFragment;
//import com.mobile.wedemo.utils.LogUtil;
//import com.mobile.wedemo.utils.ScreenUtils;
//import com.mobile.wedemo.utils.SingleClipTimelineUtil;
//import com.mobile.wedemo.utils.TimeFormatUtil;
//import com.mobile.wedemo.utils.Util;
//import com.mobile.wedemo.utils.dataInfo.ClipInfo;
//import com.mobile.wedemo.view.timelineEditor.NvsTimelineEditor;
//import com.mobile.wedemo.view.timelineEditor.NvsTimelineTimeSpanExt;
//
//import java.util.ArrayList;
//
//public class VideoEditFragment extends BaseFragment {
//    private ImageButton tv_right;
//    private NvsTimelineEditor mTimelineEditor;
//    private NvsTimeline mTimeline;
//    private TextView trimDurationVal;
//    private ArrayList<ClipInfo> mClipArrayList;
//    private NvsTimelineTimeSpanExt mTimlineTimeSpanExt;
//    private NvsStreamingContext mStreamingContext;
//    private int mCurClipIndex = 0;
//    private long mTrimInPoint = 0;
//    private long mTrimOutPoint = 0;
//    private VideoEditActivity mVideoEditActivity;
//
//    @Override
//    protected int getLayoutResource() {
//        mStreamingContext = NvsStreamingContext.getInstance();
//        return R.layout.fragment_video_edit;
//    }
//
//    @SuppressLint("ValidFragment")
//    public VideoEditFragment(NvsTimeline mTimeline){
//        this.mTimeline = mTimeline;
//    }
//
//    @Override
//    public void initObject() {
//        mClipArrayList = BackupData.instance().cloneClipInfoData();
//        mCurClipIndex = BackupData.instance().getClipIndex();
//        if (mCurClipIndex < 0 || mCurClipIndex >= mClipArrayList.size())
//            return;
////        mTimeline = SingleClipTimelineUtil.createTimeline(mClipArrayList.get(mCurClipIndex), true);
////        if (mTimeline == null)
////            return;
//
//        mVideoEditActivity = (VideoEditActivity) getActivity();
//        updateClipInfo();
//        initMultiSequence();
//    }
//
//    @Override
//    protected void loadData() {
//
//    }
//
//    @Override
//    protected void initView(View v) {
//        mTimelineEditor = v.findViewById(R.id.timelineEditor);
//        trimDurationVal = v.findViewById(R.id.trimDurationVal);
//        tv_right = v.findViewById(R.id.tv_right);
//    }
//
//    @Override
//    public void initEvents() {
//        if (mTimlineTimeSpanExt != null) {
//            mTimlineTimeSpanExt.setOnChangeListener(new NvsTimelineTimeSpanExt.OnTrimInChangeListener() {
//                @Override
//                public void onChange(long timeStamp, boolean isDragEnd) {
//                    mTrimInPoint = timeStamp;
//                    long totalDuration = mTrimOutPoint - mTrimInPoint;
//                    setTrimDurationText(totalDuration);
//                    float speed = mClipArrayList.get(mCurClipIndex).getSpeed();
//                    long newTrimIn = (long) (timeStamp * speed);
//                    mClipArrayList.get(mCurClipIndex).changeTrimIn(newTrimIn);
//                    if (isDragEnd) {
//                        mVideoEditActivity.playVideo(mTrimInPoint, mTrimOutPoint);
//                    } else {
//                        mVideoEditActivity.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//                    }
//
//                }
//            });
//            mTimlineTimeSpanExt.setOnChangeListener(new NvsTimelineTimeSpanExt.OnTrimOutChangeListener() {
//                @Override
//                public void onChange(long timeStamp, boolean isDragEnd) {
//                    mTrimOutPoint = timeStamp;
//                    long totalDuration = mTrimOutPoint - mTrimInPoint;
//                    setTrimDurationText(totalDuration);
//                    float speed = mClipArrayList.get(mCurClipIndex).getSpeed();
//                    long newTrimOut = (long) (timeStamp * speed);
//                    mClipArrayList.get(mCurClipIndex).changeTrimOut(newTrimOut);
//                    if (isDragEnd) {
//                        mVideoEditActivity.playVideo(mTrimInPoint, mTrimOutPoint);
//                    } else {
//                        mVideoEditActivity.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//                    }
//                }
//            });
//        }
//
//
//        tv_right.setOnClickListener(this);
//    }
//
//    @Override
//    public void OnClickEvents(View v) {
//        if (v == tv_right) {
//            Bitmap bitmap = Util.getBitmapFromClipInfo(mContext, mClipArrayList.get(mCurClipIndex));
//            BitmapData.instance().replaceBitmap(mCurClipIndex, bitmap);
//            BackupData.instance().setClipInfoData(mClipArrayList);
//
//            mVideoEditActivity.showEditItem();
//            mVideoEditActivity.setFragmentView(VideoEditActivity.EDIT_SEEK);
//            mVideoEditActivity.refreshData();
//        }
//    }
//
//    private void updateClipInfo() {
//        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
//        if (videoTrack == null)
//            return;
//        NvsVideoClip videoClip = videoTrack.getClipByIndex(0);
//        if (videoClip == null)
//            return;
//        long trimIn = mClipArrayList.get(mCurClipIndex).getTrimIn();
//        if (trimIn < 0)
//            mClipArrayList.get(mCurClipIndex).changeTrimIn(videoClip.getTrimIn());
//        long trimOut = mClipArrayList.get(mCurClipIndex).getTrimOut();
//        if (trimOut < 0)
//            mClipArrayList.get(mCurClipIndex).changeTrimOut(videoClip.getTrimOut());
//    }
//
//
//    private void removeTimeline() {
//        if (mStreamingContext != null) {
//            mStreamingContext.stop();
//        }
//    }
//
//    private void initMultiSequence() {
//        long duration = mTimeline.getDuration();
//        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> sequenceDescsArray = new ArrayList<>();
//        NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc sequenceDescs = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
//        sequenceDescs.mediaFilePath = mClipArrayList.get(mCurClipIndex).getFilePath();
//        sequenceDescs.trimIn = 0;
//        sequenceDescs.trimOut = duration;
//        sequenceDescs.inPoint = 0;
//        sequenceDescs.outPoint = duration;
//        sequenceDescs.stillImageHint = false;
//        sequenceDescsArray.add(sequenceDescs);
//        double pixelPerMicrosecond = getPixelMicrosecond(duration);
//        mTimelineEditor.setPixelPerMicrosecond(pixelPerMicrosecond);
//        int sequenceLeftPadding = ScreenUtils.dip2px(mContext, 13);
//        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
//        mTimelineEditor.setSequencRightPadding(sequenceLeftPadding);
//        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
//        mTimelineEditor.initTimelineEditor(sequenceDescsArray, duration);
//        mTimelineEditor.getMultiThumbnailSequenceView().getLayoutParams().height = ScreenUtils.dip2px(mContext, 64);
//        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
//        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpanExt");
//        float speed = mClipArrayList.get(mCurClipIndex).getSpeed();
//        mTrimInPoint = (long) (mClipArrayList.get(mCurClipIndex).getTrimIn() / speed);
//        mTrimOutPoint = (long) (mClipArrayList.get(mCurClipIndex).getTrimOut() / speed);
//        mTimlineTimeSpanExt = mTimelineEditor.addTimeSpanExt(mTrimInPoint, mTrimOutPoint);
//        setTrimDurationText(mTrimOutPoint - mTrimInPoint);
//    }
//
//    private void setTrimDurationText(long duration) {
//        String totalStr = getResources().getString(R.string.trimTime) + TimeFormatUtil.formatUsToString1(duration);
//        trimDurationVal.setText(totalStr);
//        LogUtil.showLog("msg----totalStr:" + totalStr);
//    }
//
//    private double getPixelMicrosecond(long duration) {
//        int width = ScreenUtils.getScreenWidth(mContext);
//        int leftPadding = ScreenUtils.dip2px(mContext, 13);
//        int sequenceWidth = width - 2 * leftPadding;
//        double pixelMicrosecond = sequenceWidth / (double) duration;
//        return pixelMicrosecond;
//    }
//}
