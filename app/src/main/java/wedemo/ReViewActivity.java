package wedemo;

import android.view.View;
import android.widget.ImageButton;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.sven.huinews.international.R;

import wedemo.base.BaseActivity;
import wedemo.utils.LogUtil;
import wedemo.utils.TimelineManager;

public class ReViewActivity extends BaseActivity {
    private ImageButton btn_back;
    private NvsLiveWindow liveWindow;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;

    @Override
    public int getLayoutId() {
        return R.layout.activity_review;
    }

    @Override
    public void initView() {
        btn_back = findViewById(R.id.btn_back);
        liveWindow = findViewById(R.id.liveWindow);
    }

    @Override
    public void initEvents() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == btn_back) {
            finish();
        }
    }

    @Override
    public void initObject() {
        initLiveWindow();
    }

    private void initLiveWindow() {
        mStreamingContext = NvsStreamingContext.getInstance();
        mTimeline = TimelineManager.getInstance().getMasterTimeline().getTimeline();
        if (null == mTimeline) {
            LogUtil.showLog("mTimeline is null!");
            return;
        }
        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, liveWindow);
        playVideo(mStreamingContext.getTimelineCurrentPosition(mTimeline), mTimeline.getDuration());

        mStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
            @Override
            public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {

            }

            @Override
            public void onPlaybackStopped(NvsTimeline nvsTimeline) {

            }

            @Override
            public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                playVideo(0, mTimeline.getDuration());
            }
        });
    }

    public void playVideo(long startTime, long endTime) {
        // 播放视频
        mStreamingContext.playbackTimeline(mTimeline, startTime, endTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);

    }
}
