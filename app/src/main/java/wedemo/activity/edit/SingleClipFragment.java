package wedemo.activity.edit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.sven.huinews.international.R;

import wedemo.utils.Util;

/**
 * Created by czl on 2018/5/29 0029.
 * 单片段编辑SingleClipFragment，封装liveWindow,供多个页面使用，避免代码重复
 */

public class SingleClipFragment extends Fragment {
    private final String TAG = "SingleClipFragment";
    private View mView;
    private static final int RESETPLATBACKSTATE = 100;
    private NvsLiveWindow mLiveWindow;
    private RelativeLayout mPlayButton;
    private ImageView mPlayImage;
    private TextView mCurrentPlayTime;
    private SeekBar mPlaySeekBar;
    private TextView mTotalDuration;
    private RelativeLayout mVoiceButton;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private OnFragmentLoadFinisedListener mFragmentLoadFinisedListener;
    private VideoFragmentListener mVideoFragmentCallBack;
    private boolean mIsTrimActivity = false;
    private long mTrimInPoint = 0, mTrimOutPoint = 0;

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

    private Handler m_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case RESETPLATBACKSTATE:
                    updateSeekBarProgress(0);
                    updateCurPlayTime(0);
                    seekTimeline(0, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                    break;
            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_single_clip, container, false);
        mLiveWindow = (NvsLiveWindow) mView.findViewById(R.id.liveWindow);
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
        if (mFragmentLoadFinisedListener != null) {
            mFragmentLoadFinisedListener.onLoadFinished();
        }
    }

    public void setFragmentLoadFinisedListener(SingleClipFragment.OnFragmentLoadFinisedListener fragmentLoadFinisedListener) {
        this.mFragmentLoadFinisedListener = fragmentLoadFinisedListener;
    }

    public void setVideoFragmentCallBack(VideoFragmentListener videoFragmentCallBack) {
        this.mVideoFragmentCallBack = videoFragmentCallBack;
    }

    public void setIsTrimActivity(boolean mIsTrimActivity) {
        this.mIsTrimActivity = mIsTrimActivity;
    }

    private void initData() {
        updateLivewindow();
        updateTotalDuration();
    }

    private void updateLivewindow() {
        Bundle bundle = getArguments();
        int ratio = 0;
        int titleHeight = 0;
        int bottomHeight = 0;
        if (bundle != null) {
            ratio = bundle.getInt("ratio", 1);
            titleHeight = bundle.getInt("titleHeight");
            bottomHeight = bundle.getInt("bottomHeight");

        }
        mStreamingContext = NvsStreamingContext.getInstance();
        if (null == mTimeline) {
            Log.e(TAG, "mTimeline is null!");
            return;
        }

        setLiveWindowRatio(ratio, titleHeight, bottomHeight);
        connectTimelineWithLiveWindow();
    }

    //设置Timeline
    public void setTimeline(NvsTimeline timeline) {
        this.mTimeline = timeline;
    }

    public void playVideo(long startTime, long endTime) {
        mTrimInPoint = startTime;
        mTrimOutPoint = endTime;

        // 播放视频
        mStreamingContext.playbackTimeline(mTimeline, startTime, endTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }

    //连接Timeline跟liveWindow
    public void connectTimelineWithLiveWindow() {
        if (mStreamingContext == null || mTimeline == null || mLiveWindow == null)
            return;
        mStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
            @Override
            public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {

            }

            @Override
            public void onPlaybackStopped(NvsTimeline nvsTimeline) {
            }

            @Override
            public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                if (!mIsTrimActivity) {
                    m_handler.sendEmptyMessage(RESETPLATBACKSTATE);
                }
                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.playBackEOF(nvsTimeline);
                }
            }
        });

        mStreamingContext.setPlaybackCallback2(new NvsStreamingContext.PlaybackCallback2() {
            @Override
            public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long l) {
                updateCurPlayTime(l);
                updateSeekBarProgress((int) l);
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
            }

            @Override
            public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {

            }
        });
        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, mLiveWindow);
        mPlaySeekBar.setMax((int) mTimeline.getDuration());
        updateCurPlayTime(0);
        updateSeekBarProgress(0);
    }

    // 获取当前引擎状态
    public int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }

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

    public void updateCurPlayTime(long time) {
        mCurrentPlayTime.setText(formatTimeStrWithUs(time));
    }

    public void updateSeekBarProgress(int progress) {
        mPlaySeekBar.setProgress(progress);
    }

    public void updateTotalDuration() {
        mTotalDuration.setText(formatTimeStrWithUs(mTimeline.getDuration()));
        mPlaySeekBar.setMax((int) mTimeline.getDuration());
    }

    //停止引擎
    public void stopEngine() {
        if (mStreamingContext != null) {
            mStreamingContext.stop();//停止播放
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
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

        m_handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: " + hidden);
    }

    private void setLiveWindowRatio(int ratio, int titleHeight, int bottomHeight) {

        ViewGroup.LayoutParams layoutParams = mLiveWindow.getLayoutParams();
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
        mLiveWindow.setLayoutParams(layoutParams);
        mLiveWindow.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTFIT);
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
                //判断当前引擎状态是否是播放状态
                if (getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    // 播放视频
                    if (mIsTrimActivity && mTrimInPoint < mTrimOutPoint) {
                        playVideo(mTrimInPoint, mTrimOutPoint);
                    } else {
                        long startTime = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                        long endTime = mTimeline.getDuration();
                        playVideo(startTime, endTime);
                    }
                } else {
                    stopEngine();
                }
            }
        });

        mPlaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekTimeline(progress, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
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
            }
        });
    }
}
