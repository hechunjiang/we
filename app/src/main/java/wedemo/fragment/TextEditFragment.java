package wedemo.fragment;//package com.mobile.wedemo.fragment;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.PointF;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.HorizontalScrollView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.meicam.sdk.NvsMultiThumbnailSequenceView;
//import com.meicam.sdk.NvsStreamingContext;
//import com.meicam.sdk.NvsTimeline;
//import com.meicam.sdk.NvsTimelineCaption;
//import com.meicam.sdk.NvsVideoClip;
//import com.meicam.sdk.NvsVideoTrack;
//import com.mobile.wedemo.R;
//import com.mobile.wedemo.VideoEditActivity;
//import com.mobile.wedemo.base.BaseFragment;
//import com.mobile.wedemo.listeners.OnEditFinishListener;
//import com.mobile.wedemo.utils.Constants;
//import com.mobile.wedemo.utils.LogUtil;
//import com.mobile.wedemo.utils.ScreenUtils;
//import com.mobile.wedemo.utils.TimeFormatUtil;
//import com.mobile.wedemo.utils.Util;
//import com.mobile.wedemo.utils.dataInfo.CaptionInfo;
//import com.mobile.wedemo.utils.dataInfo.TimelineData;
//import com.mobile.wedemo.view.timelineEditor.NvsTimelineEditor;
//import com.mobile.wedemo.view.timelineEditor.NvsTimelineTimeSpan;
//
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.List;
//
//@SuppressLint("ValidFragment")
//public class TextEditFragment extends BaseFragment {
//
//    //播放完毕
//    private static final int VIDEOPLAYTOEOF = 105;
//
//    private TextView tv_back;
//    private TextView tv_clear;
//    private TextView tv_right;
//    private EditText et_input;
//    private TextView tv_ok;
//
//    private VideoEditActivity activity;
//    private NvsTimeline mTimeline;
//    private NvsStreamingContext mStreamingContext;
//    private NvsTimelineEditor mTimelineEditor;
//    private NvsMultiThumbnailSequenceView mMultiSequenceView;
//
//    private ArrayList<CaptionInfo> mCaptionDataListClone;
//    private List<CaptionTimeSpanInfo> mTimeSpanInfoList = new ArrayList<>();
//
//    private boolean mIsSeekTimeline = true;
//    private boolean mIsInnerDrawRect = false; //手指是否在字幕框
//
//    private OnEditFinishListener onEditFinishListener;
//
//    /**
//     * 当前添加字幕
//     */
//    private NvsTimelineCaption mCurCaption;
//
//    private CaptionHandler m_handler = new CaptionHandler(this);
//    static class CaptionHandler extends Handler
//    {
//        WeakReference<TextEditFragment> mWeakReference;
//        public CaptionHandler(TextEditFragment activity)
//        {
//            mWeakReference= new WeakReference<>(activity);
//        }
//        @Override
//        public void handleMessage(Message msg)
//        {
//            final TextEditFragment fragment = mWeakReference.get();
//            if(fragment!=null)
//            {
//                switch (msg.what) {
//                    case VIDEOPLAYTOEOF:
//                        fragment.resetView();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//    }
//
//    public void setOnEditFinishListener(OnEditFinishListener onEditFinishListener){
//        this.onEditFinishListener = onEditFinishListener;
//    }
//
//    private void resetView(){
//        updatePlaytimeText(0);
//        activity.seekTimeline(0,NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//        mMultiSequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
//    }
//
//    public TextEditFragment(VideoEditActivity activity,NvsTimeline mTimeline){
//        this.activity = activity;
//        this.mTimeline = mTimeline;
//        mStreamingContext = NvsStreamingContext.getInstance();
//    }
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.fragment_text_edit;
//    }
//
//    @Override
//    public void initObject() {
//
//        initAvtivity();
//        updatePlaytimeText(0);
//        initMultiSequence();
//        addAllTimeSpan();
//        selectCaption();
//    }
//
//    private void initAvtivity() {
//        activity.setCurCaption(mCurCaption);
//        tv_right.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                activity.updateCaptionCoordinate(mCurCaption);
//                activity.changeCaptionRectVisible();
//                activity.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline),NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//            }
//        }, 100);
//
//        //设置字幕模式
//        activity.setEditMode(Constants.EDIT_MODE_CAPTION);
//
//    }
//
//    //格式化当前时间
//    private void updatePlaytimeText(long playTime){
//        long totalDuaration = mTimeline.getDuration();
//        String totalStr = TimeFormatUtil.formatUsToString1(totalDuaration);
//        String playTimeStr = TimeFormatUtil.formatUsToString1(playTime);
//        String tmpStr = playTimeStr + "/" + totalStr;
//        Log.e("weiwei","current time:   "+tmpStr);
//    }
//
//    private void initMultiSequence(){
//        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
//        if(videoTrack == null)
//            return;
//        int clipCount = videoTrack.getClipCount();
//        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> sequenceDescsArray = new ArrayList<>();
//        for (int index = 0;index < clipCount;++index){
//            NvsVideoClip videoClip = videoTrack.getClipByIndex(index);
//            if(videoClip == null)
//                continue;
//
//            NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc sequenceDescs = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
//            sequenceDescs.mediaFilePath = videoClip.getFilePath();
//            sequenceDescs.trimIn = videoClip.getTrimIn();
//            sequenceDescs.trimOut = videoClip.getTrimOut();
//            sequenceDescs.inPoint = videoClip.getInPoint();
//            sequenceDescs.outPoint = videoClip.getOutPoint();
//            sequenceDescs.stillImageHint = false;
//            sequenceDescsArray.add(sequenceDescs);
//        }
//
//        long duration = mTimeline.getDuration();
//        int halfScreenWidth = ScreenUtils.getScreenWidth(getContext()) / 2;
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)activity.getPlayBtn().getLayoutParams();
//        int playBtnTotalWidth = layoutParams.width + layoutParams.leftMargin + layoutParams.rightMargin;
//        int sequenceLeftPadding = halfScreenWidth - playBtnTotalWidth;
//        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
//        mTimelineEditor.setSequencRightPadding(halfScreenWidth);
//        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
//        mTimelineEditor.initTimelineEditor(sequenceDescsArray,duration);
//    }
//
//
//    private void addAllTimeSpan(){
//        NvsTimelineCaption caption = mTimeline.getFirstCaption();
//        while (caption != null){
//            long inPoint = caption.getInPoint();
//            long outPoint = caption.getOutPoint();
//            NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint,outPoint);
//            if(timeSpan != null) {
//                CaptionTimeSpanInfo timeSpanInfo = new CaptionTimeSpanInfo(caption, timeSpan);
//                mTimeSpanInfoList.add(timeSpanInfo);
//            }
//            caption = mTimeline.getNextCaption(caption);
//        }
//    }
//
//    /**
//     * 获取当前位置字幕
//     */
//    private void selectCaption(){
//        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
//        List<NvsTimelineCaption> captionList = mTimeline.getCaptionsByTimelinePosition(curPos);
//        int captionCount = captionList.size();
//        if(captionCount > 0){
//            //mCaptionStyleButton.setVisibility(View.VISIBLE);
//            float zVal = captionList.get(0).getZValue();
//            int index = 0;
//            for(int i = 0; i < captionCount; i++){
//                float tmpZVal = captionList.get(i).getZValue();
//                if(tmpZVal > zVal){
//                    zVal = tmpZVal;
//                    index = i;
//                }
//            }
//            mCurCaption = captionList.get(index);
//        }else {
//            mCurCaption = null;
//            //mCaptionStyleButton.setVisibility(View.GONE);
//        }
//    }
//
//    private void selectTimeSpan(){
//        for(int i = 0; i < mTimeSpanInfoList.size(); i++){
//            if(mCurCaption != null &&
//                    mTimeSpanInfoList.get(i).mCaption == mCurCaption){
//                mTimelineEditor.selectTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
//                break;
//            }
//        }
//    }
//
//    @Override
//    protected void loadData() {
//
//    }
//
//    @Override
//    protected void initView(View v) {
//
//        tv_back = v.findViewById(R.id.tv_back);
//        tv_clear = v.findViewById(R.id.tv_clear);
//        tv_right = v.findViewById(R.id.tv_right);
//        et_input = v.findViewById(R.id.et_input);
//        tv_ok = v.findViewById(R.id.tv_ok);
//
//        activity.setFragmentView(VideoEditActivity.EDIT_TEXT);
//        mTimelineEditor = activity.getTimelineEditor();
//        mMultiSequenceView = activity.getMultiSequenceView();
//        mCaptionDataListClone = TimelineData.instance().cloneCaptionData();
//
//    }
//
//    @Override
//    public void initEvents() {
//        tv_clear.setOnClickListener(this);
//        tv_back.setOnClickListener(this);
//        tv_right.setOnClickListener(this);
//        tv_ok.setOnClickListener(this);
//
//        mTimelineEditor.setOnScrollListener(new NvsTimelineEditor.OnScrollChangeListener() {
//            @Override
//            public void onScrollX(long timeStamp) {
//                if(!mIsSeekTimeline)
//                    return;
//                updatePlaytimeText(timeStamp);
//                selectCaptionAndTimeSpan();
//                activity.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//            }
//        });
//
//        mMultiSequenceView.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event){
//                mIsSeekTimeline = true;
//                return false;
//            }
//        });
//
//        activity.setVideoFragmentCallBack(new VideoEditActivity.VideoFragmentListener() {
//            @Override
//            public void playBackEOF(NvsTimeline timeline) {
//                m_handler.sendEmptyMessage(VIDEOPLAYTOEOF);
//            }
//
//            @Override
//            public void playStopped(NvsTimeline timeline) {
//                selectCaptionAndTimeSpan();
//            }
//
//            @Override
//            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
//                updatePlaytimeText(stamp);  //更新当前时间
//                activity.setDrawRectVisible(View.GONE);
//                mTimelineEditor.unSelectAllTimeSpan();
//                if(mMultiSequenceView != null){
//                    int x = Math.round((stamp / (float) mTimeline.getDuration() * mTimelineEditor.getSequenceWidth()));
//                    mMultiSequenceView.smoothScrollTo(x, 0);
//                }
//            }
//
//            @Override
//            public void streamingEngineStateChanged(int state) {
//                if(NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK == state){
//                    mIsSeekTimeline = false;
//                }else{
//                    mIsSeekTimeline = true;
//                }
//                activity.setPlayStatusBackground(state);
//            }
//        });
//
//        activity.setAssetEditListener(new VideoEditActivity.AssetEditListener() {
//            @Override
//            public void onAssetDelete() { //删除字幕
//                deleteCurCaptionTimeSpan();
//                int zVal = (int) mCurCaption.getZValue();
//                int index = getCaptionIndex(zVal);
//                if(index >= 0){
//                    mCaptionDataListClone.remove(index);
//                }
//                mTimeline.removeCaption(mCurCaption);
//                mCurCaption = null;
//                selectCaptionAndTimeSpan();
//                activity.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//            }
//
//            @Override
//            public void onAssetSelected(PointF curPoint) {
//                //判断若没有选中当前字幕框则选中，选中则不处理
//                mIsInnerDrawRect = activity.curPointIsInnerDrawRect((int)curPoint.x,(int)curPoint.y);
//                if(!mIsInnerDrawRect) {
//                    activity.selectCaptionByHandClick(curPoint);
//                    mCurCaption = activity.getCurCaption();
//                    selectTimeSpan();
//                    if (mCurCaption != null) {
//                        int alignVal = mCurCaption.getTextAlignment();
//                        activity.setAlignIndex(alignVal);
//                    }
//                }
//            }
//
//            @Override
//            public void onAssetTranstion() {
//                //拖动字幕
//                if(mCurCaption == null)
//                    return;
//                PointF pointF = mCurCaption.getCaptionTranslation();
//                //Log.e(TAG,"pointF.x = " + pointF.x + "pointF.y =" + pointF.y);
//                int zVal = (int) mCurCaption.getZValue();
//                int index = getCaptionIndex(zVal);
//                if(index >= 0)
//                    mCaptionDataListClone.get(index).setTranslation(pointF);
//            }
//
//            @Override
//            public void onAssetScale() {
//                //缩放字幕
//                if(mCurCaption == null)
//                    return;
//                int zVal = (int) mCurCaption.getZValue();
//                int index = getCaptionIndex(zVal);
//                if(index >= 0){
//                    mCaptionDataListClone.get(index).setScaleFactorX(mCurCaption.getScaleX());
//                    mCaptionDataListClone.get(index).setScaleFactorY(mCurCaption.getScaleY());
//                    mCaptionDataListClone.get(index).setAnchor(mCurCaption.getAnchorPoint());
//                    mCaptionDataListClone.get(index).setRotation(mCurCaption.getRotationZ());
//                    mCaptionDataListClone.get(index).setCaptionSize(mCurCaption.getFontSize());
//                    PointF pointF = mCurCaption.getCaptionTranslation();
//                    //Log.e(TAG,"pointF.x = " + pointF.x + "pointF.y =" + pointF.y);
//                    mCaptionDataListClone.get(index).setTranslation(pointF);
//                }
//            }
//
//            @Override
//            public void onAssetAlign(int alignVal) {
//                //字幕对齐方式
//                int zVal = (int) mCurCaption.getZValue();
//                int index = getCaptionIndex(zVal);
//                if(index >= 0)
//                    mCaptionDataListClone.get(index).setAlignVal(alignVal);
//            }
//
//            @Override
//            public void onAssetHorizFlip(boolean isHorizFlip) {
//
//            }
//        });
//
//        activity.setCaptionTextEditListener(new VideoEditActivity.VideoCaptionTextEditListener() {
//            @Override
//            public void onCaptionTextEdit() {
//                if (!mIsInnerDrawRect)
//                    return;
//                if (!TextUtils.isEmpty( et_input.getText())) {
//                    String userInputText = et_input.getText().toString();
//                    mCurCaption.setText(userInputText);
//                    activity.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline),NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//                    activity.updateCaptionCoordinate(mCurCaption);
//                    activity.changeCaptionRectVisible();
//                    int zVal = (int) mCurCaption.getZValue();
//                    int index = getCaptionIndex(zVal);
//                    if (index >= 0) {
//                        mCaptionDataListClone.get(index).setText(userInputText);
//                    }
//                }
//                mIsInnerDrawRect = false;
//            }
//        });
//    }
//
//    @Override
//    public void OnClickEvents(View v) {
//        if(v == tv_clear){
//            et_input.setText("");
//        }else if(v == tv_back){
//            TimelineData.instance().setCaptionData(mCaptionDataListClone);
//            activity.showEditItem();
//            activity.setFragmentView(VideoEditActivity.EDIT_SEEK);
//            if(onEditFinishListener != null){
//                onEditFinishListener.onFinish();
//            }
//        }else if(v == tv_right){
//            if (!TextUtils.isEmpty( et_input.getText())) {
//                String userInputText = et_input.getText().toString();
//                addCaption(userInputText);
//            }
//        }else if(v == tv_ok){
//            mStreamingContext.stop();
//            TimelineData.instance().setCaptionData(mCaptionDataListClone);
//            activity.showEditItem();
//            activity.setFragmentView(VideoEditActivity.EDIT_SEEK);
//            if(onEditFinishListener != null){
//                onEditFinishListener.onFinish();
//            }
//        }
//    }
//
//    private void deleteCurCaptionTimeSpan(){
//        for (int i = 0; i < mTimeSpanInfoList.size(); i++){
//            if(mTimeSpanInfoList.get(i).mCaption == mCurCaption){
//                mTimelineEditor.deleteSelectedTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
//                mTimeSpanInfoList.remove(i);
//                break;
//            }
//        }
//    }
//
//    //添加字幕
//    private void addCaption(String caption){
//        long inPoint = mStreamingContext.getTimelineCurrentPosition(mTimeline);
//        long captionDuration = 4 * Constants.NS_TIME_BASE;
//        long outPoint = inPoint + captionDuration;
//        long duration = mTimeline.getDuration();
//
//        if(outPoint > duration){
//            captionDuration = duration - inPoint;
//            if(captionDuration <= Constants.NS_TIME_BASE){
//                captionDuration = Constants.NS_TIME_BASE;
//                inPoint = duration - captionDuration;
//                if(duration <= Constants.NS_TIME_BASE){
//                    captionDuration = duration;
//                    inPoint = 0;
//                }
//            }
//            outPoint = duration;
//        }
//
//        mCurCaption = mTimeline.addCaption(caption, inPoint, captionDuration, null);
//        if(mCurCaption == null){
//            LogUtil.showLog("addCaption: " + " 添加字幕失败！" );
//            return;
//        }
//        float zVal = getCurCaptionZVal();
//        mCurCaption.setZValue(zVal);
//        NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint, outPoint);
//        if(timeSpan == null){
//            LogUtil.showLog("addCaption: " + " 添加TimeSpan失败!" );
//            return;
//        }
//        mTimeSpanInfoList.add(new CaptionTimeSpanInfo(mCurCaption, timeSpan));
//        activity.setCurCaption(mCurCaption);
//        activity.updateCaptionCoordinate(mCurCaption);
//        int alignVal = mCurCaption.getTextAlignment();
//        activity.setAlignIndex(alignVal);
//        activity.changeCaptionRectVisible();
//        activity.seekTimeline(inPoint, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//        selectTimeSpan();//选择timeSpan
//        CaptionInfo captionInfo = Util.saveCaptionData(mCurCaption);
//        if(captionInfo != null)
//            mCaptionDataListClone.add(captionInfo);
//    }
//
//    private float getCurCaptionZVal(){
//        float zVal = 0.0f;
//        //获得时间线上的第一个字幕
//        NvsTimelineCaption caption = mTimeline.getFirstCaption();
//        while (caption != null){
//            //获取字幕Z值
//            float tmpZVal = caption.getZValue();
//            if(tmpZVal > zVal)
//                zVal = tmpZVal;
//            caption = mTimeline.getNextCaption(caption);
//        }
//        zVal += 1.0;
//        return zVal;
//    }
//
//    private NvsTimelineTimeSpan addTimeSpan(long inPoint, long outPoint){
//        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
//        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpan");
//        NvsTimelineTimeSpan timelineTimeSpan = mTimelineEditor.addTimeSpan(inPoint,outPoint);
//        if(timelineTimeSpan == null){
//            LogUtil.showLog("addTimeSpan: " + " 添加TimeSpan失败!" );
//            return null;
//        }
//        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimInChangeListener() {
//            @Override
//            public void onChange(long timeStamp, boolean isDragEnd) {
//                activity.seekTimeline(timeStamp,NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//                activity.changeCaptionRectVisible();
//                if(isDragEnd){
//                    if(mCurCaption == null )
//                        return;
//                    mCurCaption.changeInPoint(timeStamp);
//                    activity.seekMultiThumbnailSequenceView();
//                    int zVal = (int) mCurCaption.getZValue();
//                    int index = getCaptionIndex(zVal);
//                    if(index >= 0)
//                        mCaptionDataListClone.get(index).setInPoint(timeStamp);
//                }
//            }
//        });
//        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimOutChangeListener() {
//            @Override
//            public void onChange(long timeStamp, boolean isDragEnd) {
//                //outPoint是开区间，seekTimeline时，需要往前平移一帧即0.04秒，转换成微秒即40000微秒
//                activity.seekTimeline(timeStamp - 40000,NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
//                activity.changeCaptionRectVisible();
//                if(isDragEnd){
//                    if(mCurCaption == null )
//                        return;
//                    mCurCaption.changeOutPoint(timeStamp);
//                    activity.seekMultiThumbnailSequenceView();
//                    int zVal = (int) mCurCaption.getZValue();
//                    int index = getCaptionIndex(zVal);
//                    if(index >= 0){
//                        long inPoint = mCaptionDataListClone.get(index).getInPoint();
//                        mCaptionDataListClone.get(index).setDuration(timeStamp - inPoint);
//                    }
//                }
//            }
//        });
//
//        return timelineTimeSpan;
//    }
//
//    private int getCaptionIndex(int curZValue){
//        int index = -1;
//        int count = mCaptionDataListClone.size();
//        for (int i = 0;i < count;++i){
//            int zVal = mCaptionDataListClone.get(i).getCaptionZVal();
//            if(curZValue == zVal){
//                index = i;
//                break;
//            }
//        }
//        return index;
//    }
//
//
//    /**
//     * 获取当前时间段的字幕
//     */
//    private void selectCaptionAndTimeSpan(){
//        selectCaption();
//        activity.setCurCaption(mCurCaption);
//        activity.updateCaptionCoordinate(mCurCaption);
//        activity.changeCaptionRectVisible();
//        if(mCurCaption != null){
//            int alignVal = mCurCaption.getTextAlignment();
//            activity.setAlignIndex(alignVal);
//        }
//        if(mCurCaption != null){
//            selectTimeSpan();
//        }else {
//            mTimelineEditor.unSelectAllTimeSpan();
//        }
//    }
//
//
//    private class CaptionTimeSpanInfo {
//        public NvsTimelineCaption mCaption;
//        public NvsTimelineTimeSpan mTimeSpan;
//        public CaptionTimeSpanInfo(NvsTimelineCaption caption, NvsTimelineTimeSpan timeSpan){
//            this.mCaption = caption;
//            this.mTimeSpan = timeSpan;
//        }
//    }
//
//
//}
