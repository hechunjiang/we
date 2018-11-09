package wedemo.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.sven.huinews.international.R;

import wedemo.TransitionActivity;

public class PreviewFragment extends Fragment implements NvsStreamingContext.PlaybackCallback,
        NvsStreamingContext.PlaybackCallback2, NvsStreamingContext.StreamingEngineCallback, View.OnClickListener {
    protected String TAG = getClass().getSimpleName();
    private View rootView;
    private NvsLiveWindow previewWindow;
    private TextView tv_current_time, tv_start_end;
    private ImageView playImage;
    private SeekBar play_seekBar;
    private LinearLayout playBarLayout;

    private long maxTime;
    private long currentTime;


    protected <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    public void t(@NonNull String msg) {
        Toast.makeText(getContext(), "" + msg, Toast.LENGTH_LONG).show();
    }

    public void i(@NonNull String msg) {
        Log.i(TAG, ">>>>>>>>" + msg);
    }

    public void e(@NonNull String msg) {
        Log.e(TAG, ">>>>>>>>" + msg);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_preview, container, false);
        initVew();
        initData();
        initListener();
        seekTimeline(0, 0);
        return rootView;
    }

    /**
     * 初始化控件
     */
    private void initVew() {
        previewWindow = findViewById(R.id.previewWindow);
        tv_current_time = findViewById(R.id.tv_current_time);
        tv_start_end = findViewById(R.id.tv_start_end);
        playImage = findViewById(R.id.playImage);
        play_seekBar = findViewById(R.id.play_seekBar);
        playBarLayout = findViewById(R.id.playBarLayout);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initTimeLine();
    }

    /**
     * 初始化时间线
     */
    private void initTimeLine() {
        if (TransitionActivity.mTimeline == null) return;
        initSeekBar();
    }

    /**
     * 初始化进度条
     */
    private void initSeekBar() {
        if (TransitionActivity.mTimeline == null) return;
        maxTime = TransitionActivity.mTimeline.getDuration();
        play_seekBar.setMax((int) maxTime);
        currentTime = 0;
        upEndTime(maxTime);
        upCurrentTime(currentTime);
        upProgress(currentTime);
        seekTimeline(currentTime, 0);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        playImage.setOnClickListener(this);
        previewWindow.setOnClickListener(this);

        play_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) seekTimeline(progress, 0);
                currentTime = progress;
                upCurrentTime(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        connectTimelineWithLiveWindow();
    }

    /**
     * 绑定时间线与预览窗口
     */
    public void connectTimelineWithLiveWindow() {
        if (TransitionActivity.mStreamingContext != null) {
            TransitionActivity.mStreamingContext.setPlaybackCallback(this);
            TransitionActivity.mStreamingContext.setPlaybackCallback2(this);
            TransitionActivity.mStreamingContext.setStreamingEngineCallback(this);
            if (TransitionActivity.mTimeline != null && previewWindow != null) {
                TransitionActivity.mStreamingContext.connectTimelineWithLiveWindow(TransitionActivity.mTimeline, previewWindow);
            }
        }
    }

    /**
     * 节点预览
     *
     * @param timestamp
     * @param seekShowMode
     */
    public void seekTimeline(long timestamp, int seekShowMode) {
        TransitionActivity.mStreamingContext.seekTimeline(TransitionActivity.mTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, seekShowMode);
    }

    /**
     * 开始播放
     *
     * @param startTime
     * @param endTime
     */
    public void playVideo(long startTime, long endTime) {
        TransitionActivity.mStreamingContext.playbackTimeline(TransitionActivity.mTimeline, startTime, endTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }

    /**
     * 停止播放
     */
    public void stopEngine() {
        if (TransitionActivity.mStreamingContext != null)
            TransitionActivity.mStreamingContext.stop();
    }

    /**
     * 更新当前显示时间
     */
    public void upCurrentTime(long time) {
        tv_current_time.setText(formatTime(time));
    }

    /**
     * 更新结束显示时间
     */
    public void upEndTime(long time) {
        tv_start_end.setText(formatTime(time));
    }

    /**
     * 更新进度
     *
     * @param time
     */
    public void upProgress(long time) {
        play_seekBar.setProgress((int) time);
    }


    /**
     * 格式化时间显示
     *
     * @param time
     * @return
     */
    private String formatTime(long time) {
        int second = (int) (time / 1000000.0 + 0.5);
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String timeStr;
        if (time == 0) {
            timeStr = "00:00";
        } else if (hh > 0) {
            timeStr = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            timeStr = String.format("%02d:%02d", mm, ss);
        }
        return timeStr;
    }

    /**
     * 预加载完成时回调
     *
     * @param nvsTimeline
     */
    @Override
    public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {
        playImage.setImageResource(R.mipmap.icon_pause);
    }

    /**
     * 播放暂停 结束时回调的接口
     *
     * @param nvsTimeline
     */
    @Override
    public void onPlaybackStopped(NvsTimeline nvsTimeline) {
        playImage.setImageResource(R.mipmap.icon_start);
    }

    /**
     * 播放完回调接口
     *
     * @param nvsTimeline
     */
    @Override
    public void onPlaybackEOF(NvsTimeline nvsTimeline) {
        initSeekBar();
    }

    /**
     * 进度跟新时回调接口 播放中
     *
     * @param nvsTimeline
     * @param l
     */
    @Override
    public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long l) {
        play_seekBar.setProgress((int) l);
    }

    /**
     * 播放状态发生改变时回调
     *
     * @param i
     */
    @Override
    public void onStreamingEngineStateChanged(int i) {
    }


    /**
     * 播放第一个视频时回调
     *
     * @param nvsTimeline
     */
    @Override
    public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.playImage) {
            playOrPause();

        } else if (i == R.id.previewWindow) {
            playOrPause();

        }
    }


    /**
     * 播放或暂停
     */
    private void playOrPause() {
        if (TransitionActivity.mStreamingContext.getStreamingEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            stopEngine();
        } else {
            playVideo(currentTime, maxTime);
        }
    }

    public interface OnPlayListener {
        void onPlayVideo(long start, long end);
    }


    private OnPlayListener listener = new OnPlayListener() {
        @Override
        public void onPlayVideo(long start, long end) {
            playVideo(start, end);
        }
    };

    public OnPlayListener getPlayListener() {
        return listener;
    }

    private void setLiveWindowRatio(int ratio) {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        ViewGroup.LayoutParams layoutParams = previewWindow.getLayoutParams();

        int screenWidth = layoutParams.width;
        int screenHeight = layoutParams.height;

        switch (ratio) {
            case 1: // 16:9
                layoutParams.width = screenWidth;
                layoutParams.height = (int) (screenWidth * 9.0 / 16);
                break;
            case 2: //1:1
                layoutParams.width = screenWidth;
                layoutParams.height = screenWidth;
                break;
            case 4: //9:16
                int newHeight = screenHeight - 0;
                layoutParams.width = (int) (newHeight * 9.0 / 16);
                layoutParams.height = newHeight;
                break;
            case 8: // 3:4
                int newHeight1 = screenHeight - 0;
                layoutParams.width = (int) (newHeight1 * 3.0 / 4);
                layoutParams.height = newHeight1;
                break;
            default: // 7:10
                int newHeight2 = screenHeight - 0;
                layoutParams.width = (int) (newHeight2 * 7.0 / 10);
                layoutParams.height = newHeight2;
                break;
        }

        previewWindow.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTFIT);
    }

}
