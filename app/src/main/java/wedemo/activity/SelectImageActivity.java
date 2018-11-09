package wedemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.ImageUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;

import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.base.BaseActivity;
import wedemo.fragment.VideoFragment;
import wedemo.utils.AppManager;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.ScreenUtils;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.view.timelineEditor.NvsTimelineEditor;
import wedemo.view.timelineEditor.NvsTimelineTimeSpan;

public class SelectImageActivity extends BaseActivity {
    private CustomTitleBar mTitleBar;
    private NvsTimelineEditor mTimelineEditor;
    private VideoFragment mVideoFragment;
    private RelativeLayout mBottomRelativeLayout;
    private RelativeLayout mPlayBtnLayout;
    private NvsMultiThumbnailSequenceView mMultiSequenceView;

    private boolean mIsSeekTimeline = true;
    private NvsStreamingContext mStreamingContext;

    private CustomTimeLine mCustomTimeline;
    private NvsTimeline mTimeline;
    private ProgressDialog mProgressDialog;
    private Thread threadImage;

    private void resetView() {
        mVideoFragment.seekTimeline(0, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
        mMultiSequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            mStreamingContext.stop();
            removeTimeline();

            Intent intent = new Intent();
            intent.putExtra("imagePath", (String) msg.obj);
            setResult(RESULT_OK, intent);

            mProgressDialog.dismiss();
            finish();
        }
    };

    @Override
    public void initEvents() {

        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
//                removeTimeline();
//                //AppManager.getInstance().finishActivity();
//                finish();

                onBackPressed();
            }

            @Override
            public void OnNextTextClick() {

                mProgressDialog.show();

                if (threadImage == null) {
                    threadImage = new Thread() {
                        @Override
                        public void run() {
                            Bitmap bitmap = mStreamingContext.grabImageFromTimeline(mTimeline, mStreamingContext.getTimelineCurrentPosition(mTimeline), new NvsRational(16, 9));
                            File file = ImageUtils.saveBitmapToSdCard(bitmap);
                            String imagePath = file.getAbsolutePath();
                            Message message = Message.obtain();
                            message.obj = imagePath;
                            handler.sendMessage(message);
                        }
                    };
                }
                threadImage.start();
            }
        });

        mTimelineEditor.setOnScrollListener(new NvsTimelineEditor.OnScrollChangeListener() {
            @Override
            public void onScrollX(long timeStamp) {
                if (!mIsSeekTimeline)
                    return;
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
            }

            @Override
            public void playStopped(NvsTimeline timeline) {
            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                mTimelineEditor.unSelectAllTimeSpan();
                if (mMultiSequenceView != null) {
                    int x = Math.round((stamp / (float) mTimeline.getDuration() * mTimelineEditor.getSequenceWidth()));
                    mMultiSequenceView.smoothScrollTo(x, 0);
                }
            }

            @Override
            public void streamingEngineStateChanged(int state) {

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_image;
    }

    @Override
    public void initView() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mTimelineEditor = (NvsTimelineEditor) findViewById(R.id.select_timeline_editor);
        mBottomRelativeLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mPlayBtnLayout = (RelativeLayout) findViewById(R.id.play_btn_layout);
        mMultiSequenceView = mTimelineEditor.getMultiThumbnailSequenceView();

        mTitleBar.setRightText(getString(R.string.save));
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.updateApk));
        mProgressDialog.setCancelable(false);
    }


    @Override
    public void onClickEvent(View v) {
        int i = v.getId();
//        if (i == R.id.zoom_in_btn) {
////            mIsSeekTimeline = false;
////            mTimelineEditor.ZoomInSequence();
////
////        } else if (i == R.id.zoom_out_btn) {
////            mIsSeekTimeline = false;
////            mTimelineEditor.ZoomOutSequence();
////
////        }

    }

    @Override
    public void initObject() {
        mStreamingContext = NvsStreamingContext.getInstance();

        mCustomTimeline = CustomTimelineUtil.createcCopyMasterTimeline();
        if (mCustomTimeline == null)
            return;

        mTimeline = mCustomTimeline.getTimeline();

        initVideoFragment();
        initMultiSequence();
    }


    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setTimeline(mTimeline);
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), 0);
            }
        });
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
        //int playBtnTotalWidth = layoutParams.width + layoutParams.leftMargin + layoutParams.rightMargin;
        int sequenceLeftPadding = halfScreenWidth - ScreenUtils.dip2px(this, 63);
        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
        //需要减去margin
        mTimelineEditor.setSequencRightPadding(halfScreenWidth - ScreenUtils.dip2px(this, 63));
        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray, duration);

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
                    seekMultiThumbnailSequenceView();
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
                    seekMultiThumbnailSequenceView();
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


    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        mProgressDialog.dismiss();
        super.onBackPressed();
    }

    private void removeTimeline() {
        CustomTimelineUtil.removeTimeline(mCustomTimeline);
        mTimeline = null;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("SelectImageActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("SelectImageActivity");
            MobclickAgent.onPause(this);
        }
    }
}
