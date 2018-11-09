package wedemo.activity.effect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsClip;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsThumbnailSequenceView;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meicam.sdk.NvsVolume;
import com.sven.huinews.international.R;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import wedemo.MessageEvent;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.customview.FxSelect;
import wedemo.customview.RangeProgressBar;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.LogUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.Util;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.TimeLineFxInfo;
import wedemo.utils.dataInfo.TimelineData;

/**
 * Created by zd on 2017/6/26.
 */

public class EffectActivity extends Activity implements NvsStreamingContext.StreamingEngineCallback,
        NvsStreamingContext.PlaybackCallback, NvsStreamingContext.PlaybackCallback2 {

    private static final int SOUL_COLOR = Color.parseColor("#7fF55454");
    private static final int IMAGE_COLOR = Color.parseColor("#7fEFCD2D");
    private static final int SHAKE_COLOR = Color.parseColor("#7f45EDEF");
    private static final int WAVE_COLOR = Color.parseColor("#7f44ECCE");
    private static final int BLACK_MAGIC_COLOR = Color.parseColor("#7f000000");
    private static final int CLASSIC_COLOR = Color.parseColor("#7fEA4E90");
    private static final int VIEWFINDER_COLOR = Color.parseColor("#7fEB8429");
    private static final int MIDSUMMER_COLOR = Color.parseColor("#7f214ACD");
    private static final int ZIXIA_COLOR = Color.parseColor("#7fD075FF");
    private static final int CHUNCHUN_COLOR = Color.parseColor("#7fFFFA7A");
    private static final int HALLUCINATION_COLOR = Color.parseColor("#7fB354F5");

    private static final float TIMEBASE = 1000000f;
    private static final int noneTimeMode = 0;
    private static final int reverseMode = 1;
    private static final int relapseMode = 2;
    private static final int slowMode = 3;
    private static final int soul_mode = 0;
    private static final int image_mode = 1;
    private static final int shake_mode = 2;
    private static final int wave_mode = 3;
    private static final int black_magic = 4;
    private static final int calssic_mode = 5;
    private static final int viewfinder_mode = 6;
    private static final int midsummer_mode = 7;
    private static final int zixia_mode = 8;
    private static final int chunchun_mode = 9;
    private static final int xsngal_mode = 10;
    private static final int hallucination_mode = 11;
    private NvsStreamingContext m_streamingContext;
    private String TAG = "douyin_editVideo";
    private NvsLiveWindow m_liveWindow;
    private Button m_backButton;
    private NvsTimeline m_TimeLine;
    private NvsVideoClip m_clip;
    private Button m_filter_fx;
    private Button m_timeline_fx;
    private Button m_delete_fx;
    private boolean timelineFxMode = true;
    private LinearLayout m_time_fx;
    private Timer m_timer;
    private TimerTask m_timerTask;
    private LinearLayout m_range_bar;
    private TextView m_in_time;
    private TextView m_out_time;
    private ImageView m_play_image;
    private float inTime;
    private float outTime;
    private float slowInTime;
    private float slowOutTime;
    private float soulInTime;
    private float m_in_time_point;
    private float shakeInTime;
    private int m_timeFxMode = noneTimeMode;
    private FxSelect progressbarView;
    private FxSelect slowSeekView;
    private FxSelect filterView;

    private CustomTitleBar title;

    private int filter_mode = 0;

    private LinearLayout m_package_fx;
    private float m_progress;
    private LinearLayout m_sequence_view_linear_layout;
    private boolean m_is_applay = true;

    private ArrayList<ClipInfo> clipInfoData = new ArrayList<>();
    private StringBuilder m_filter_fx_ids[] = {
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder(),
            new StringBuilder()
    };

    private String m_filter_fx_package_path[] = {
            "assets:/C6273A8F-C899-4765-8BFC-E683EE37AA84.2.videofx", // 灵魂出窍
            "assets:/6B7BE12C-9FA1-4ED0-8E81-E107632FFBC8.2.videofx", // 镜像
            "assets:/A8A4344D-45DA-460F-A18F-C0E2355FE864.3.videofx", // 抖动
            "assets:/1CEE3777-A813-4378-AD52-7B264BD0CC4D.3.videofx", // 波浪
            "assets:/C02204D0-F3C3-495E-B65C-9F2C79E68573.4.videofx", // 黑色魔术
            "assets:/707EB4BC-2FD0-46FA-B607-ABA3F6CE7250.1.videofx", // 经典
            "assets:/0785C0D9-12E7-4A3D-9496-6199F9FDDD2C.4.videofx", // 取景器
            "assets:/327D2618-74B5-4F44-B76B-5E2E62A5870C.1.videofx", // 盛夏
            "assets:/34897DAA-8F41-4862-84CD-5573F8D6787B.3.videofx", // 紫霞
            "assets:/0D572D28-AB90-4707-98FF-C9BA84BB8422.2.videofx", // 纯纯
            "assets:/9AC28816-639F-4A9B-B4BA-4060ABD229A2.9.videofx" // x-singal

            // append here 3

    };

    private String m_filter_fx_lic_path[] = {
            "assets:/C6273A8F-C899-4765-8BFC-E683EE37AA84.lic", // 灵魂出窍
            "assets:/6B7BE12C-9FA1-4ED0-8E81-E107632FFBC8.lic", // 镜像
            "assets:/A8A4344D-45DA-460F-A18F-C0E2355FE864.lic", // 抖动
            "assets:/1CEE3777-A813-4378-AD52-7B264BD0CC4D.lic", // 波浪
            "assets:/C02204D0-F3C3-495E-B65C-9F2C79E68573.lic", // 黑色魔术
            "assets:/707EB4BC-2FD0-46FA-B607-ABA3F6CE7250.lic", // 经典
            "assets:/0785C0D9-12E7-4A3D-9496-6199F9FDDD2C.lic", // 取景器
            "assets:/327D2618-74B5-4F44-B76B-5E2E62A5870C.lic", // 盛夏
            "assets:/34897DAA-8F41-4862-84CD-5573F8D6787B.lic", // 紫霞
            "assets:/0D572D28-AB90-4707-98FF-C9BA84BB8422.lic", // 纯纯
            "assets:/9AC28816-639F-4A9B-B4BA-4060ABD229A2.lic" // x-singal

            // append here 3

    };

    private boolean m_is_down = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (m_timeFxMode == relapseMode)
                        progressbarView.setFirstValue(m_progress, true);
                    if (m_timeFxMode == slowMode)
                        slowSeekView.setFirstValue(m_progress, true);
                    if (m_timeFxMode == noneTimeMode)
                        progressbarView.setFirstValue(m_progress, true);

                    if (m_timeFxMode == reverseMode) {
                        progressbarView.setFirstValue(m_progress, true);
                    }

                    if (!timelineFxMode) {
                        filterView.setFirstValue(m_progress, true);
                    }

                    Log.d(TAG, "handleMessage: m_progress: " + m_progress);
                    break;
                default:
                    break;
            }
        }
    };
    private long timeLineDurtion;
    private ImageView m_replay_image;
    private NvsTimelineVideoFx m_timeLineShakeFx;
    private NvsTimelineVideoFx m_timeLineSoulFx;
    private Map<Integer, NvsTimelineVideoFx> m_time_line_videofx_map = new HashMap<Integer, NvsTimelineVideoFx>();
    private ArrayList<TimeLineFxInfo> timelineFx;
    private CustomTimeLine customTimeLine;
    private RelativeLayout rl_play;
    private LinearLayout bottom_layout;
    private ClipInfo clipInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_timeline);
        m_streamingContext = NvsStreamingContext.getInstance();

        initUI();
        initTimeline();

        getDataFromIntent();
        installAssetPackage();
        initData();

        initProgress();
        initSequenceView(clipInfoData);
        buttonClickLIstener();
        m_filter_fx.callOnClick();
    }


    private void setLiveWindowRatio(int ratio, int titleHeight, int bottomHeight) {

        ViewGroup.LayoutParams layoutParams = rl_play.getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        //状态栏高度
        int statusHeight = Util.getStatusBarHeight(this);
        // screen width
        int screenWidth = metric.widthPixels;
        int screenHeight = metric.heightPixels;
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
        rl_play.setLayoutParams(layoutParams);
        m_liveWindow.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTFIT);
    }

    private void initData() {
        timelineFx = customTimeLine.getTimeData().cloneTimelineFxInfo();
        ArrayList<ClipInfo> clipInfoD = customTimeLine.getTimeData().getClipInfoData();
        if(clipInfoD != null && clipInfoD.size() > 0) {
            clipInfo = clipInfoD.get(0);
            if(clipInfo == null){
                clipInfo = new ClipInfo();
            }
        }
    }


    private int installAssetPackage() {

        int error = 0;
        for (int i = 0; i < m_filter_fx_package_path.length; i++) {
            error = m_streamingContext.getAssetPackageManager().installAssetPackage(m_filter_fx_package_path[i], m_filter_fx_lic_path[i], NvsAssetPackageManager.ASSET_PACKAGE_TYPE_VIDEOFX, true, m_filter_fx_ids[i]);
            if (error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR
                    && error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
                Log.e(TAG, "Failed to install soul package!");
            }
        }

        return error;
    }

    private void initProgress() {

        outTime = (float) m_TimeLine.getDuration() / 100 * 70;
        slowOutTime = (float) m_TimeLine.getDuration() / 100 * 70;

        m_out_time.setText(formatTimeStrWithUs((int) outTime));

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        progressbarView = new FxSelect(this);
        m_range_bar.addView(progressbarView, param);
        progressbarView.setVisibility(View.GONE);

        filterView = new FxSelect(this);
        filterView.exchangeProgressBar(false);
        m_range_bar.addView(filterView, param);
        filterView.setVisibility(View.GONE);

        slowSeekView = new FxSelect(this);
        slowSeekView.setSecondControlColor(Color.RED);
        m_range_bar.addView(slowSeekView, param);
        slowSeekView.setVisibility(View.GONE);

        m_in_time.setText(formatTimeStrWithUs(0));
        m_out_time.setText(formatTimeStrWithUs((int) m_TimeLine.getDuration()));

        filterView.setOndataChanged(new RangeProgressBar.OnDataChanged() {
            @Override
            public void onFirstDataChange(float var) {
                m_replay_image.setVisibility(View.GONE);
                if (m_replay_image.getVisibility() == View.VISIBLE) {
                    m_play_image.setVisibility(View.GONE);
                } else
                    m_play_image.setVisibility(View.VISIBLE);

                inTime = (float) m_TimeLine.getDuration() / 100 * var;
                m_in_time.setText(formatTimeStrWithUs((int) inTime));
                stopTimer();
                m_streamingContext.seekTimeline(m_TimeLine, (long) inTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
            }

            @Override
            public void onSecondDataChange(float var) {

            }
        });

        progressbarView.setOndataChanged(new RangeProgressBar.OnDataChanged() {
            @Override
            public void onFirstDataChange(float var) {
                m_replay_image.setVisibility(View.GONE);

                if (m_replay_image.getVisibility() == View.VISIBLE) {
                    m_play_image.setVisibility(View.GONE);
                } else
                    m_play_image.setVisibility(View.VISIBLE);

                inTime = (float) var / 100 * m_TimeLine.getDuration();
                m_in_time.setText(formatTimeStrWithUs((int) inTime));
                stopTimer();
                m_streamingContext.seekTimeline(m_TimeLine, (long) inTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
            }

            @Override
            public void onSecondDataChange(float var) {
                if (m_replay_image.getVisibility() == View.VISIBLE) {
                    m_play_image.setVisibility(View.GONE);
                } else
                    m_play_image.setVisibility(View.VISIBLE);
                outTime = (float) timeLineDurtion / 100 * var;
                stopTimer();
                m_streamingContext.seekTimeline(m_TimeLine, m_streamingContext.getTimelineCurrentPosition(m_TimeLine), NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);

            }
        });

        slowSeekView.setOndataChanged(new RangeProgressBar.OnDataChanged() {
            @Override
            public void onFirstDataChange(float var) {
                m_replay_image.setVisibility(View.GONE);
                if (m_replay_image.getVisibility() == View.VISIBLE) {
                    m_play_image.setVisibility(View.GONE);
                } else
                    m_play_image.setVisibility(View.VISIBLE);
                slowInTime = (float) m_TimeLine.getDuration() / 100 * var;
                m_in_time.setText(formatTimeStrWithUs((int) slowInTime));
                stopTimer();
                m_streamingContext.seekTimeline(m_TimeLine, (long) slowInTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);

            }

            @Override
            public void onSecondDataChange(float var) {
                if (m_replay_image.getVisibility() == View.VISIBLE) {
                    m_play_image.setVisibility(View.GONE);
                } else
                    m_play_image.setVisibility(View.VISIBLE);
                slowOutTime = (float) timeLineDurtion / 100 * var;
                stopTimer();
                m_streamingContext.seekTimeline(m_TimeLine, m_streamingContext.getTimelineCurrentPosition(m_TimeLine), NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
            }
        });
    }

    private void buttonClickLIstener() {
        title.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                onBackPressed();
            }

            @Override
            public void OnNextTextClick() {
                m_streamingContext.stop();
                removeTimeline();
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setTimeMode(m_timeFxMode);
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setSlowTime(slowOutTime);
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setResverTime(outTime);
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setTimelineFx(timelineFx);
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
                EventBus.getDefault().post(new MessageEvent("effect"));
                finish();
            }
        });

        m_backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        m_replay_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                filterView.setActionMode(1);
                filterView.setAutoAdjust(false);
                m_replay_image.setVisibility(View.GONE);
                startTimer();
                //m_streamingContext.seekTimeline(m_TimeLine,0, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
                m_streamingContext.playbackTimeline(m_TimeLine, 0, -1, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
            }
        });

        m_play_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_replay_image.setVisibility(View.GONE);
                if (getCurrentEngineState() != m_streamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {

                    filterView.setActionMode(1);
                    filterView.setAutoAdjust(false);
                    //filterView.setSoulAndShakeColor(Color.GRAY);
                    long startTime = m_streamingContext.getTimelineCurrentPosition(m_TimeLine);

                    if (startTime == m_TimeLine.getDuration())
                        startTime = 0;

                    if (m_timeFxMode == relapseMode) {

                        unsetVideoTrack();
                        relapseVideo();
                    }
                    if (m_timeFxMode == slowMode) {
                        unsetVideoTrack();
                        slowVideo();
                    }

                    m_play_image.setVisibility(View.GONE);
                    startTimer();
                    m_streamingContext.playbackTimeline(m_TimeLine, startTime, -1, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
                } else {
                    stopTimer();
                    m_play_image.setVisibility(View.VISIBLE);
                    m_streamingContext.stop();//停止播放
                }
            }
        });

        m_filter_fx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (timelineFxMode) {
                    m_play_image.setVisibility(View.VISIBLE);
                    m_replay_image.setVisibility(View.GONE);
                    m_package_fx.setVisibility(View.VISIBLE);
                    m_time_fx.setVisibility(View.GONE);
                    filterView.setVisibility(View.VISIBLE);
                    progressbarView.setVisibility(View.GONE);
                    slowSeekView.setVisibility(View.GONE);
                    stopTimer();
                    unsetVideoTrack();
                    filterView.setFirstValue(0, true);
                    m_streamingContext.seekTimeline(m_TimeLine, 0, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
                    timelineFxMode = false;

                    m_timeline_fx.setTextColor(ContextCompat.getColor(EffectActivity.this, R.color.a4a4));
                    m_filter_fx.setTextColor(ContextCompat.getColor(EffectActivity.this, R.color.bg_white));
                }
            }
        });


        m_timeline_fx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!timelineFxMode) {
                    progressbarView.setFirstValue(0, true);
                    slowSeekView.setFirstValue(0, true);
                    m_play_image.setVisibility(View.VISIBLE);
                    m_replay_image.setVisibility(View.GONE);

                    m_time_fx.setVisibility(View.VISIBLE);
                    m_package_fx.setVisibility(View.GONE);
                    seekTimeline();
                    filterView.setVisibility(View.GONE);
                    if (m_timeFxMode == relapseMode)
                        progressbarView.setVisibility(View.VISIBLE);
                    else if (m_timeFxMode == noneTimeMode) {
                        progressbarView.setVisibility(View.VISIBLE);
                    } else if (m_timeFxMode == slowMode)
                        slowSeekView.setVisibility(View.VISIBLE);
                    else if (m_timeFxMode == reverseMode) {
                        progressbarView.setVisibility(View.VISIBLE);
                    }
                    timelineFxMode = true;
                    m_timeline_fx.setTextColor(ContextCompat.getColor(EffectActivity.this, R.color.bg_white));
                    m_filter_fx.setTextColor(ContextCompat.getColor(EffectActivity.this, R.color.a4a4));
                }
            }
        });

        m_delete_fx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timelineFx.size() == 0) {
                    filterView.removeRect();
                    return;
                }

                Log.d(TAG, "onClick: " + "delete fx");
                m_streamingContext.stop();

                timelineFx.remove(timelineFx.size() - 1);

                NvsTimelineVideoFx nextFx = m_TimeLine.removeTimelineVideoFx(m_TimeLine.getFirstTimelineVideoFx());
                while (nextFx != null) {
                    nextFx = m_TimeLine.removeTimelineVideoFx(nextFx);
                }

                for (int j = 0; j < timelineFx.size(); j++) {
                    String name = timelineFx.get(j).getName();

                    if ("Video Echo".equals(timelineFx.get(j).getName())) {
                        m_TimeLine.addBuiltinTimelineVideoFx(timelineFx.get(j).getInPoint(), timelineFx.get(j).getOutPoint() - timelineFx.get(j).getInPoint(), timelineFx.get(j).getName());
                    } else {
                        m_TimeLine.addPackagedTimelineVideoFx(timelineFx.get(j).getInPoint(), timelineFx.get(j).getOutPoint() - timelineFx.get(j).getInPoint(), timelineFx.get(j).getName());
                    }

                }

                filterView.removeRect();

                m_streamingContext.seekTimeline(m_TimeLine, m_streamingContext.getTimelineCurrentPosition(m_TimeLine), NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER | NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
            }
        });

    }


    private void unsetVideoTrack() {
        NvsVideoTrack m_videoTrack = m_TimeLine.getVideoTrackByIndex(0);
        m_videoTrack.removeAllClips();


        for (int i = 0; i < clipInfoData.size(); i++) {
            m_clip = m_videoTrack.appendClip(clipInfoData.get(i).getFilePath());  //添加视频片段
            m_clip.changeSpeed(clipInfo.getSpeed());
            m_clip.setPanAndScan(0, 1);
//            m_clip.setVolumeGain(0f, 0f);
            if (m_timeFxMode == reverseMode) {
                if (!m_clip.getPlayInReverse())
                    m_clip.setPlayInReverse(true);
            }
        }

    }

    private void updateFxList(long inPoint, long outPoint) {

        int count = timelineFx.size();
        int inIndex = 0;
        int outIndex = 9999;
        boolean hasInSelect = false;
        boolean hasOutSelect = false;
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                if (timelineFx.get(i).getInPoint() <= inPoint && timelineFx.get(i).getOutPoint() >= inPoint) {
                    hasInSelect = true;
                    inIndex = i;
                }
            }
        }
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                if (timelineFx.get(i).getInPoint() <= outPoint && timelineFx.get(i).getOutPoint() >= outPoint) {
                    outIndex = i;
                    hasOutSelect = true;
                }
            }
        }
        if (count != 0) {

            if (inIndex == outIndex) {
                TimeLineFxInfo tempfx = new TimeLineFxInfo(timelineFx.get(outIndex).getName(), timelineFx.get(outIndex).getInPoint(), timelineFx.get(outIndex).getOutPoint());
                tempfx.setChangeTrimIn(timelineFx.get(outIndex).getInPoint());
                tempfx.setChangeTrimOut(timelineFx.get(outIndex).getOutPoint());
                timelineFx.get(outIndex).setOutPoint(inPoint);
                timelineFx.add(outIndex + 1, tempfx);
                timelineFx.get(outIndex + 1).setInPoint(outPoint);
                timelineFx.get(outIndex + 1).setOutPoint(tempfx.getOutPoint());

            } else if (hasInSelect && hasOutSelect) {
                timelineFx.get(inIndex).setOutPoint(inPoint);
                timelineFx.get(outIndex).setInPoint(outPoint);
            }
        }

        if (inIndex + 1 < outIndex && outIndex != 9999 && hasInSelect)
            timelineFx.subList(inIndex + 1, outIndex - 1).clear();

        for (int i = 0; i < timelineFx.size(); i++) {
            if (inPoint <= timelineFx.get(i).getInPoint() && outPoint >= timelineFx.get(i).getOutPoint()) {
                timelineFx.remove(i);
                i = i - 1;
            }
        }
        for (int j = 0; j < timelineFx.size(); j++) {
            String name = timelineFx.get(j).getName();

            if ("Video Echo".equals(timelineFx.get(j).getName())) {
                m_TimeLine.addBuiltinTimelineVideoFx(timelineFx.get(j).getInPoint(), timelineFx.get(j).getOutPoint() - timelineFx.get(j).getInPoint(), timelineFx.get(j).getName());
            } else {
                m_TimeLine.addPackagedTimelineVideoFx(timelineFx.get(j).getInPoint(), timelineFx.get(j).getOutPoint() - timelineFx.get(j).getInPoint(), timelineFx.get(j).getName());
            }

        }
    }

    private void slowVideo() {
        int newSlowOutTime = (int) slowOutTime + 1000000;
        NvsVideoTrack m_videoTrack = m_TimeLine.getVideoTrackByIndex(0);
        int index = m_videoTrack.getClipByTimelinePosition((int) slowOutTime).getIndex();

        if (!m_videoTrack.splitClip(m_videoTrack.getClipByTimelinePosition((int) slowOutTime).getIndex(), (long) slowOutTime)) {
            Log.d(TAG, "spilt clip is false!");
            return;
        }

        //NvsVideoClip clip = m_videoTrack.getClipByIndex(index+1);
        if (newSlowOutTime > m_TimeLine.getDuration())
            newSlowOutTime = (int) m_TimeLine.getDuration() - 100;

        if (!m_videoTrack.splitClip(m_videoTrack.getClipByTimelinePosition(newSlowOutTime).getIndex(), (long) newSlowOutTime)) {
            Log.d(TAG, "%spilt clip is false!");
            return;
        }

        int newIndex = m_videoTrack.getClipByTimelinePosition(newSlowOutTime).getIndex();

        NvsVideoClip clip = m_videoTrack.getClipByIndex(newIndex - 1);
        long trimIn = clip.getTrimIn();
        long trimOut = clip.getTrimOut();

        for (int i = 0; i < m_videoTrack.getClipCount(); i++) {
            NvsVideoClip videoClip = m_videoTrack.getClipByIndex(i);
            videoClip.setPanAndScan(0, 1);
        }
//        m_videoTrack.insertClip(clip.getFilePath(),trimIn,trimOut,newIndex);
//        for (int i = 0; i < m_videoTrack.getClipCount();i++) {
//            m_videoTrack.setBuiltinTransition(i,null);
//        }
        //addMusic();
    }

    private void relapseVideo() {
        int index = 0;
        int newIndex = 0;
        int newTime = (int) outTime + 1000000;
        NvsVideoTrack m_videoTrack = m_TimeLine.getVideoTrackByIndex(0);

        index = m_videoTrack.getClipByTimelinePosition((int) outTime).getIndex();

        if (!m_videoTrack.splitClip(m_videoTrack.getClipByTimelinePosition((int) outTime).getIndex(), (long) outTime)) {
            Log.d(TAG, "spilt clip is false!");
            return;
        }


        if (newTime > m_TimeLine.getDuration())
            newTime = (int) m_TimeLine.getDuration() - 100;

        if (!m_videoTrack.splitClip(m_videoTrack.getClipByTimelinePosition((int) newTime).getIndex(), (long) newTime)) {
            Log.d(TAG, "%spilt clip is false!");
            return;
        }
        m_streamingContext.seekTimeline(m_TimeLine, m_streamingContext.getTimelineCurrentPosition(m_TimeLine), NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
        newIndex = m_videoTrack.getClipByTimelinePosition((int) newTime).getIndex();

        NvsVideoClip newClip = m_videoTrack.getClipByIndex(newIndex - 1);

        //newClip.changeSpeed(m_speed.get(m_speed.size()-index-1));
        for (int i = 0; i < 2; i++) {
            NvsVideoClip insertClip = m_videoTrack.insertClip(newClip.getFilePath(), newClip.getTrimIn(), newClip.getTrimOut(), newIndex);
            insertClip.changeSpeed(clipInfo.getSpeed());
//            insertClip.setVolumeGain(0f, 0f);
        }

        for (int i = 0; i < m_videoTrack.getClipCount(); i++) {
            m_videoTrack.setBuiltinTransition(i, null);
            NvsVideoClip videoClip = m_videoTrack.getClipByIndex(i);
            videoClip.setPanAndScan(0, 1);
        }
        //addMusic();
        m_streamingContext.seekTimeline(m_TimeLine, m_streamingContext.getTimelineCurrentPosition(m_TimeLine), NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);


    }


    private void seekTimeline() {
        // 判定当前引擎状态是否是播放状态
        if (getCurrentEngineState() == m_streamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            stopTimer();
        }
        // 定位预览视频图像
        // 此处选择NvsStreamingEngineSeekFlag_ShowCaptionPoster|NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER模式，可以看到字幕和动画贴纸的实时效果
        m_streamingContext.seekTimeline(m_TimeLine, 0, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER | NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);

    }

    private void startTimer() {
        stopTimer();
        if (m_timer == null) {
            m_timer = new Timer();
        }

        if (m_timerTask == null) {
            m_timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (m_streamingContext == null) {
                        stopTimer();
                        return;
                    }
                    if (m_TimeLine == null) {
                        stopTimer();
                        return;
                    }
                    m_progress = (int) (100 * m_streamingContext.getTimelineCurrentPosition(m_TimeLine) / m_TimeLine.getDuration());
                    Log.d(TAG, "run: cur: " + m_streamingContext.getTimelineCurrentPosition(m_TimeLine) + " dur: " + m_TimeLine.getDuration());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {//耗时操作
                            mHandler.sendEmptyMessage(0);
                        }
                    }).start();

                }
            };
        }

        if (m_timer != null && m_timerTask != null) {
            m_timer.schedule(m_timerTask, 0, 100);
        }

    }

    private void stopTimer() {
        if (m_timer != null) {
            m_timer.cancel();
            m_timer = null;
        }
        if (m_timerTask != null) {
            m_timerTask.cancel();
            m_timerTask = null;
        }
    }

    private int getCurrentEngineState() {
        return m_streamingContext.getStreamingEngineState();
    }

    private void getDataFromIntent() {
        ArrayList<ClipInfo> clipInfoDataAll = TimelineData.instance().getClipInfoData();

        clipInfoData.add(clipInfoDataAll.get(TimelineManager.getInstance().getBranch()));
    }

    private void initUI() {
        m_liveWindow = (NvsLiveWindow) findViewById(R.id.live_window);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        rl_play = (RelativeLayout) findViewById(R.id.rl_play);
        m_backButton = (Button) findViewById(R.id.back_button);
        m_filter_fx = (Button) findViewById(R.id.filter_fx);
        m_timeline_fx = (Button) findViewById(R.id.timeline_fx);
        m_time_fx = (LinearLayout) findViewById(R.id.time_fx);
        m_in_time = (TextView) findViewById(R.id.in_time);
        m_out_time = (TextView) findViewById(R.id.out_time);
        m_range_bar = (LinearLayout) findViewById(R.id.range_bar);
        m_replay_image = (ImageView) findViewById(R.id.replay_image);
        m_package_fx = (LinearLayout) findViewById(R.id.package_fx);
        m_play_image = (ImageView) findViewById(R.id.play_image);
        m_delete_fx = (Button) findViewById(R.id.delete_fx);
        title = findViewById(R.id.title);

        title.setRightText(getString(R.string.save));

        m_play_image.setVisibility(View.VISIBLE);
        m_sequence_view_linear_layout = (LinearLayout) findViewById(R.id.sequence_view_linear_layout);

        initTimeLineFxRecyclerView();
        initPackageFxRecyclerView();


    }

    private void removeTimeline() {
        CustomTimelineUtil.removeTimeline(customTimeLine);
        m_TimeLine = null;
    }

    /*格式化时间(us)*/
    private String formatTimeStrWithUs(int us) {
        int second = us / 1000000;
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

    private void initTimeline() {

        customTimeLine = CustomTimelineUtil.createcCopyTimeline(TimelineManager.getInstance().getCurrenTimeline());
        m_TimeLine = customTimeLine.getTimeline();

        NvsVideoTrack videoTrackByIndex = m_TimeLine.getVideoTrackByIndex(0);
        NvsVolume volumeGain = videoTrackByIndex.getVolumeGain();
        LogUtil.showLog("左声道："+volumeGain.leftVolume);
        LogUtil.showLog("左声道："+volumeGain.rightVolume);

        setLiveWindowRatio(customTimeLine.getTimeData().getMakeRatio(), title.getLayoutParams().height, bottom_layout.getLayoutParams().height);

        m_streamingContext.setStreamingEngineCallback(this);
        m_streamingContext.setPlaybackCallback(this);
        m_streamingContext.setPlaybackCallback2(this);

        m_streamingContext.connectTimelineWithLiveWindow(m_TimeLine, m_liveWindow);

        //addMusic();
        timeLineDurtion = m_TimeLine.getDuration();
        //m_streamingContext.seekTimeline(m_TimeLine, 0, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);

        // m_streamingContext.playbackTimeline(m_TimeLine, 0, timeLineDurtion, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);

    }

    private void initSequenceView(ArrayList<ClipInfo> clipInfoData) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int screenWidth = metric.widthPixels;

        for (int i = 0; i < clipInfoData.size(); i++) {
            NvsVideoTrack m_videoTrack = m_TimeLine.getVideoTrackByIndex(0);
            NvsClip clip = m_videoTrack.getClipByIndex(i);
            NvsThumbnailSequenceView view = new NvsThumbnailSequenceView(this);
            view.setMediaFilePath(clipInfoData.get(i).getFilePath());
            view.setStartTime(clip.getInPoint());
            view.setDuration(clip.getOutPoint() - clip.getInPoint());
            int sequenceViewWidth = (int) screenWidth / clipInfoData.size();
            LinearLayout.LayoutParams paramsSequence = new LinearLayout.LayoutParams(sequenceViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            m_sequence_view_linear_layout.addView(view, paramsSequence);
        }

    }

    public void onBackPressed() {
        stopTimer();
        m_play_image.setVisibility(View.VISIBLE);
        m_streamingContext.stop();//停止播放
        removeTimeline();
        finish();
        super.onBackPressed();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long l) {
        m_in_time.setText(formatTimeStrWithUs((int) l));
    }

    @Override
    public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {

    }

    @Override
    public void onPlaybackStopped(NvsTimeline nvsTimeline) {

    }

    @Override
    public void onPlaybackEOF(NvsTimeline nvsTimeline) {
        m_play_image.setVisibility(View.GONE);
        m_replay_image.setVisibility(View.VISIBLE);

        m_progress = 100;
        mHandler.sendEmptyMessage(0);
        if (!timelineFxMode) {
            filterView.setFirstValue(100, true);
        } else {
            progressbarView.setFirstValue(100, true);
            slowSeekView.setFirstValue(100, true);
        }
        m_streamingContext.seekTimeline(m_TimeLine, m_TimeLine.getDuration(), NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
        stopTimer();
    }

    @Override
    public void onStreamingEngineStateChanged(int i) {

    }

    @Override
    public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {

    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void startFilterFx(StringBuilder filterFxId, int fxColor, int pos) {
        m_is_applay = filterView.isApplay();
        if (!m_is_applay) {
            return;
        }
        Log.d(TAG, "startFilterFx: pos: " + pos);
        filterView.setActionMode(0);
        filterView.setLeftValue(filterView.getFirstValue());
        filterView.setPreviousColor(fxColor);
        filterView.setSoulAndShakeColor(fxColor);
        filterView.setcolorMode(1);
        filterView.setAutoAdjust(true);

        m_play_image.setVisibility(View.GONE);

        for (int i = 0; i < timelineFx.size(); i++) {
            Log.d("timelineFx: ", "start name: " + timelineFx.get(i).getName());
        }

        NvsTimelineVideoFx nextFx = m_TimeLine.removeTimelineVideoFx(m_TimeLine.getFirstTimelineVideoFx());
        while (nextFx != null) {
            nextFx = m_TimeLine.removeTimelineVideoFx(nextFx);
        }

        if (pos == hallucination_mode) {
            m_in_time_point = m_streamingContext.getTimelineCurrentPosition(m_TimeLine);
            NvsTimelineVideoFx fx = m_TimeLine.addBuiltinTimelineVideoFx((long) m_in_time_point, m_TimeLine.getDuration(), "Video Echo");
            m_time_line_videofx_map.put(pos, fx);
        } else {
            NvsTimelineVideoFx fx = m_TimeLine.addPackagedTimelineVideoFx(m_streamingContext.getTimelineCurrentPosition(m_TimeLine), m_TimeLine.getDuration(), filterFxId.toString());
            m_in_time_point = m_streamingContext.getTimelineCurrentPosition(m_TimeLine);
            fx.changeInPoint((long) m_in_time_point);
            m_time_line_videofx_map.put(pos, fx);
        }
        startTimer();
        m_streamingContext.playbackTimeline(m_TimeLine, m_streamingContext.getTimelineCurrentPosition(m_TimeLine), -1, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
        m_is_down = true;
        filterView.downEvent(filterView.getFirstValue(), fxColor);
    }

    private void endFilterFx(StringBuilder filterFxId, int pos) {
        if (!m_is_applay) {
            return;
        }
        if (!m_is_down) {
            return;
        }
        filterView.setActionMode(1);
//        filterView.setcolorMode(0);
        if (m_replay_image.getVisibility() == View.VISIBLE) {
            m_play_image.setVisibility(View.GONE);
        } else
            m_play_image.setVisibility(View.VISIBLE);
        filterView.setRightValue(filterView.getFirstValue());
        stopTimer();
        NvsTimelineVideoFx fx = m_time_line_videofx_map.get(pos);
        if (fx == null) {
            return;
        }

        long outPoint = 0;

        if (!filterView.isApplay()) { // 进度条走到头了
            outPoint = m_TimeLine.getDuration();
        } else {
            outPoint = m_streamingContext.getTimelineCurrentPosition(m_TimeLine);
        }
        fx.changeOutPoint(outPoint);
        updateFxList((long) m_in_time_point, outPoint);

        TimeLineFxInfo fxInfo = new TimeLineFxInfo(filterFxId.toString(), fx.getInPoint(), fx.getOutPoint());
        fxInfo.setChangeTrimIn(fx.getInPoint());
        fxInfo.setChangeTrimOut(fx.getOutPoint());
        timelineFx.add(fxInfo);

        for (int i = 0; i < timelineFx.size(); i++) {
            Log.d("timelineFx: ", "end name: " + timelineFx.get(i).getName());
        }

        m_streamingContext.seekTimeline(m_TimeLine, m_streamingContext.getTimelineCurrentPosition(m_TimeLine), NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
        m_is_down = false;
        filterView.upEvent(filterView.getFirstValue());
    }

    private void initPackageFxRecyclerView() {
        RecyclerView package_fx_recyclerview = (RecyclerView) findViewById(R.id.package_fx_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(package_fx_recyclerview.getContext(), LinearLayoutManager.HORIZONTAL, false);
        package_fx_recyclerview.setLayoutManager(layoutManager);
        PackageFxRecyclerViewAdapter package_fx_apapter = new PackageFxRecyclerViewAdapter(getFilterFxData());
        package_fx_recyclerview.setAdapter(package_fx_apapter);
        package_fx_recyclerview.addItemDecoration(new SpaceItemDecoration(dip2px(getApplicationContext(), 13)));

        package_fx_apapter.setOnItemLongPressListener(new PackageFxRecyclerViewAdapter.OnItemLongPressListener() {
            @Override
            public void onItemLongPress(View view, int pos) {

                switch (pos) {
                    case soul_mode:
                        startFilterFx(m_filter_fx_ids[pos], SOUL_COLOR, pos);
                        break;
                    case image_mode:
                        startFilterFx(m_filter_fx_ids[pos], IMAGE_COLOR, pos);
                        break;
                    case shake_mode:
                        startFilterFx(m_filter_fx_ids[pos], SHAKE_COLOR, pos);
                        break;
                    case wave_mode:
                        startFilterFx(m_filter_fx_ids[pos], WAVE_COLOR, pos);
                        break;
                    case black_magic:
                        startFilterFx(m_filter_fx_ids[pos], BLACK_MAGIC_COLOR, pos);
                        break;
                    case calssic_mode:
                        startFilterFx(m_filter_fx_ids[pos], CLASSIC_COLOR, pos);
                        break;
                    case viewfinder_mode:
                        startFilterFx(m_filter_fx_ids[pos], VIEWFINDER_COLOR, pos);
                        break;
                    case midsummer_mode:
                        startFilterFx(m_filter_fx_ids[pos], MIDSUMMER_COLOR, pos);
                        break;
                    case zixia_mode:
                        startFilterFx(m_filter_fx_ids[pos], ZIXIA_COLOR, pos);
                        break;
                    case chunchun_mode:
                        startFilterFx(m_filter_fx_ids[pos], CHUNCHUN_COLOR, pos);
                        break;
                    case xsngal_mode:
                        startFilterFx(m_filter_fx_ids[pos], HALLUCINATION_COLOR, pos);
                        break;

                }
                Log.d("aaaa", "onItemPress: " + "test");
            }
        });

        package_fx_recyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {


                View itemView = rv.findChildViewUnder(e.getX(), e.getY());
                if (itemView == null) {
                    return false;
                }
                int pos = (int) itemView.getTag();
                // 使用滤镜特效
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.d("aaaa", "pos: " + pos + "down");
                        break;
                    }
                    case MotionEvent.ACTION_UP: {

                        if(pos < 0){
                            return false;
                        }

                        if (pos == hallucination_mode) {
                            endFilterFx(new StringBuilder("Video Echo"), pos);
                        } else {
                            endFilterFx(m_filter_fx_ids[pos], pos);
                        }
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL:
                        if (pos == hallucination_mode) {
                            endFilterFx(new StringBuilder("Video Echo"), pos);
                        } else {
                            endFilterFx(m_filter_fx_ids[pos], pos);
                        }

                        break;

                    default:

                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void initTimeLineFxRecyclerView() {
        RecyclerView time_line_fx_recyclerview = (RecyclerView) findViewById(R.id.time_fx_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(time_line_fx_recyclerview.getContext(), LinearLayoutManager.HORIZONTAL, false);
        time_line_fx_recyclerview.setLayoutManager(layoutManager);
        RecyclerViewAdapter time_line_fx_apapter = new RecyclerViewAdapter(getTimeLineFxData());
        time_line_fx_recyclerview.setAdapter(time_line_fx_apapter);
        time_line_fx_recyclerview.addItemDecoration(new SpaceItemDecoration(dip2px(getApplicationContext(), 20)));
        time_line_fx_apapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (pos == m_timeFxMode) {
                    return;
                }

                // 使用时间线特效
                switch (pos) {
                    case noneTimeMode:

                        m_timeFxMode = noneTimeMode;
                        m_play_image.setVisibility(View.GONE);
                        m_replay_image.setVisibility(View.GONE);
                        progressbarView.setVisibility(View.VISIBLE);
                        slowSeekView.setVisibility(View.GONE);
                        progressbarView.exchangeProgressBar(false);
                        seekTimeline();
                        unsetVideoTrack();
                        startTimer();
                        m_streamingContext.playbackTimeline(m_TimeLine, 0, -1, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
                        break;

                    case reverseMode:

                        m_timeFxMode = reverseMode;
                        m_play_image.setVisibility(View.GONE);
                        m_replay_image.setVisibility(View.GONE);
                        progressbarView.setVisibility(View.VISIBLE);
                        slowSeekView.setVisibility(View.GONE);
                        progressbarView.exchangeProgressBar(false);
                        m_progress = (int) (100 * (m_streamingContext.getTimelineCurrentPosition(m_TimeLine)) / m_TimeLine.getDuration());
                        unsetVideoTrack();
                        stopTimer();
                        startTimer();
                        m_streamingContext.playbackTimeline(m_TimeLine, 0, -1, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
                        break;

                    case relapseMode:
                        m_timeFxMode = relapseMode;
                        m_play_image.setVisibility(View.GONE);
                        m_replay_image.setVisibility(View.GONE);
                        progressbarView.setVisibility(View.VISIBLE);
                        slowSeekView.setVisibility(View.GONE);
                        progressbarView.exchangeProgressBar(true);
                        m_progress = (int) (100 * m_streamingContext.getTimelineCurrentPosition(m_TimeLine) / m_TimeLine.getDuration());
                        seekTimeline();
                        unsetVideoTrack();
                        relapseVideo();
                        startTimer();
                        m_streamingContext.playbackTimeline(m_TimeLine, 0, -1, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
                        break;

                    case slowMode:

                        m_timeFxMode = slowMode;
                        progressbarView.setVisibility(View.GONE);
                        m_replay_image.setVisibility(View.GONE);
                        slowSeekView.setVisibility(View.VISIBLE);
                        m_play_image.setVisibility(View.GONE);
                        m_progress = (int) (100 * m_streamingContext.getTimelineCurrentPosition(m_TimeLine) / m_TimeLine.getDuration());
                        seekTimeline();
                        unsetVideoTrack();
                        slowVideo();
                        startTimer();
                        m_streamingContext.playbackTimeline(m_TimeLine, 0, -1, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
                        break;
                }
            }
        });
    }

    private List<ImageResourceObj> getFilterFxData() {
        List<ImageResourceObj> list = new ArrayList<ImageResourceObj>();

        ImageResourceObj soul = new ImageResourceObj();
        soul.normalImage = R.mipmap.e1;
        soul.imageName = getString(R.string.effect_filter1);
        list.add(soul);

        ImageResourceObj image = new ImageResourceObj();
        image.normalImage = R.mipmap.e2;
        image.imageName = getString(R.string.effect_filter2);
        list.add(image);

        ImageResourceObj shake = new ImageResourceObj();
        shake.normalImage = R.mipmap.e3;
        shake.imageName = getString(R.string.effect_filter3);
        list.add(shake);

        ImageResourceObj wave = new ImageResourceObj();
        wave.normalImage = R.mipmap.e4;
        wave.imageName = getString(R.string.effect_filter4);
        list.add(wave);

        ImageResourceObj blackmagic = new ImageResourceObj();
        blackmagic.normalImage = R.mipmap.e5;
        blackmagic.imageName = getString(R.string.effect_filter5);
        list.add(blackmagic);

        ImageResourceObj classic = new ImageResourceObj();
        classic.normalImage = R.mipmap.e6;
        classic.imageName = getString(R.string.effect_filter6);
        list.add(classic);

        ImageResourceObj viewfinder = new ImageResourceObj();
        viewfinder.normalImage = R.mipmap.e7;
        viewfinder.imageName = getString(R.string.effect_filter7);
        list.add(viewfinder);

        ImageResourceObj midsummer = new ImageResourceObj();
        midsummer.normalImage = R.mipmap.e8;
        midsummer.imageName = getString(R.string.effect_filter8);
        list.add(midsummer);


        ImageResourceObj zixia = new ImageResourceObj();
        zixia.normalImage = R.mipmap.e9;
        zixia.imageName = getString(R.string.effect_filter9);
        list.add(zixia);

        ImageResourceObj chunchun = new ImageResourceObj();
        chunchun.normalImage = R.mipmap.e10;
        chunchun.imageName = getString(R.string.effect_filter10);
        list.add(chunchun);


//        ImageResourceObj hallucination = new ImageResourceObj();
//        hallucination.normalImage = R.mipmap.hallucination;
//        hallucination.imageName = "幻觉";
//        list.add(hallucination);

        ImageResourceObj x_singal = new ImageResourceObj();
        x_singal.normalImage = R.mipmap.e11;
        x_singal.imageName = "X-Signal";
        list.add(x_singal);

        return list;
    }


    private List<ImageResourceObj> getTimeLineFxData() {
        List<ImageResourceObj> list = new ArrayList<ImageResourceObj>();

        ImageResourceObj none = new ImageResourceObj();
        none.normalImage = R.mipmap.none_off;
        none.selectedImage = R.mipmap.none_on;
        none.imageName = getString(R.string.no_info);
        list.add(none);

        ImageResourceObj back_in_time = new ImageResourceObj();
        back_in_time.normalImage = R.mipmap.back_in_time_off;
        back_in_time.selectedImage = R.mipmap.back_in_time_on;
        back_in_time.imageName = getString(R.string.time_down);
        list.add(back_in_time);

        ImageResourceObj repeate = new ImageResourceObj();
        repeate.normalImage = R.mipmap.repeate_off;
        repeate.selectedImage = R.mipmap.repeate_on;
        repeate.imageName = getString(R.string.time_rept);
        list.add(repeate);

        ImageResourceObj slow_motion = new ImageResourceObj();
        slow_motion.normalImage = R.mipmap.slow_motion_off;
        slow_motion.selectedImage = R.mipmap.slow_motion_on;
        slow_motion.imageName = getString(R.string.time_slow);
        list.add(slow_motion);

        return list;
    }

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
        public int m_selected_position = 0;
        private List<ImageResourceObj> m_data_list;
        private OnItemClickListener m_on_item_click_listener = null;

        public RecyclerViewAdapter(List<ImageResourceObj> list) {
            m_data_list = list;
        }

        public void resetState() {
            m_selected_position = 0;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_line_fx_list_item, parent, false);
            v.setOnClickListener(this);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(m_data_list.get(position).imageName);
            holder.itemView.setTag(position);
            if (m_selected_position == position) {
                holder.mImageView.setImageResource(m_data_list.get(position).selectedImage);

                switch (position) {
                    case 0:
                        holder.mTextView.setTextColor(Color.parseColor("#FF9C7C3E"));
                        break;
                    case 1:
                        holder.mTextView.setTextColor(Color.parseColor("#FFFA7A"));
                        break;
                    case 2:
                        holder.mTextView.setTextColor(Color.parseColor("#45EDEF"));
                        break;
                    case 3:
                        holder.mTextView.setTextColor(Color.parseColor("#FFD075FF"));
                        break;
                }
            } else {
                holder.mImageView.setImageResource(m_data_list.get(position).normalImage);
                holder.mTextView.setTextColor(Color.parseColor("#ffffff"));
            }

        }

        @Override
        public int getItemCount() {
            return m_data_list == null ? 0 : m_data_list.size();
        }

        @Override
        public void onClick(View view) {

            if (m_on_item_click_listener != null) {
                m_on_item_click_listener.onItemClick(view, (int) view.getTag());

            }
            notifyItemChanged(m_selected_position);
            m_selected_position = (int) view.getTag();
            notifyItemChanged(m_selected_position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.m_on_item_click_listener = listener;
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int pos);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            ImageView mImageView;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.image_view);
                mTextView = (TextView) itemView.findViewById(R.id.text_view);
            }
        }
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int mSpace;

        public SpaceItemDecoration(int space) {
            mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.left = mSpace;
            outRect.right = mSpace;
        }

    }

    private class ImageResourceObj {
        public int normalImage;
        public int selectedImage;
        public String imageName;
    }

    private static class PackageFxRecyclerViewAdapter extends RecyclerView.Adapter<PackageFxRecyclerViewAdapter.ViewHolder> implements View.OnLongClickListener {
        private List<ImageResourceObj> m_data_list;

        private OnItemLongPressListener m_listener;

        public void setOnItemLongPressListener(OnItemLongPressListener listener) {
            m_listener = listener;
        }

        public PackageFxRecyclerViewAdapter(List<ImageResourceObj> list) {
            m_data_list = list;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_fx_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            v.setOnLongClickListener(this);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.mImageView.setImageResource(m_data_list.get(position).normalImage);
            holder.mTextView.setText(m_data_list.get(position).imageName);
            holder.itemView.setTag(position);

//            switch (position){
//                case 0:
//                    holder.mTextView.setTextColor(Color.parseColor("#FF9C7C3E"));
//                    break;
//                case 1:
//                    holder.mTextView.setTextColor(Color.parseColor("#FFFA7A"));
//                    break;
//                case 2:
//                    holder.mTextView.setTextColor(Color.parseColor("#45EDEF"));
//                    break;
//                case 3:
//                    holder.mTextView.setTextColor(Color.parseColor("#FFD075FF"));
//                    break;
//            }

            /*switch (position) {
                case 0:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style4);
                    //holder.mTextView.setTextColor(Color.parseColor("#3db5fe"));
                    break;
                case 1:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style5);
                    //holder.mTextView.setTextColor(Color.parseColor("#436cb2"));
                    break;

                case 2:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style6);
                    //holder.mTextView.setTextColor(Color.parseColor("#bf54ed"));
                    break;
                case 3:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style7);
                    //holder.mTextView.setTextColor(Color.parseColor("#d1527e"));
                    break;
                case 4:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style8);
                    //holder.mTextView.setTextColor(Color.parseColor("#000000"));
                    break;

                case 5:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style10);
                    //holder.mTextView.setTextColor(Color.parseColor("#FF0000")); // 红
                    break;

                case 6:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style11);
                    //holder.mTextView.setTextColor(Color.parseColor("#EEB422")); // 橘黄
                    break;

                case 7:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style12);
                    //holder.mTextView.setTextColor(Color.parseColor("#EE00EE"));  // 紫色
                    break;

                case 8:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style13);
                    //holder.mTextView.setTextColor(Color.parseColor("#0000FF")); // 蓝色
                    break;

                case 9:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style14);
                    // holder.mTextView.setTextColor(Color.parseColor("#B3EE3A"));  // 绿
                    break;

                case 11:
                    holder.mBackgroundView.setBackgroundResource(R.drawable.button_style9);
                    //holder.mTextView.setTextColor(Color.parseColor("#913d88"));
                    break;

                // append here 2

            }

*/
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return m_data_list == null ? 0 : m_data_list.size();
        }

        @Override
        public boolean onLongClick(View view) {

            if (m_listener != null) {
                m_listener.onItemLongPress(view, (int) view.getTag());
            }

            return false;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            ImageView mImageView;
            View mView;
            View mBackgroundView;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.image_view);
                mTextView = (TextView) itemView.findViewById(R.id.text_view);
                mView = itemView.findViewById(R.id.layer);
                mBackgroundView = itemView.findViewById(R.id.background_view);
            }
        }

        public interface OnItemLongPressListener {
            void onItemLongPress(View view, int pos);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("EffectActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("EffectActivity");
            MobclickAgent.onPause(this);
        }
    }
}