package wedemo.activity.music;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.sven.huinews.international.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import wedemo.MessageEvent;
import wedemo.MusicActivity;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.base.BaseActivity;
import wedemo.fragment.VideoFragment;
import wedemo.utils.AppManager;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.MusicInfo;

public class AddMusicActivity extends BaseActivity {
    private final String TAG = "MusicActivity";
    private VideoFragment m_videoFragment;
    private CustomTitleBar m_titleBar;
    private RelativeLayout m_bottomLayout;
    private NvsStreamingContext m_streamingContext;
    private NvsTimeline m_timeLine;
    private ImageButton m_musicSingleBtn, m_musicMultiBtn;
    private MusicInfo m_musicInfo;
    private CustomTimeLine currenTimeline;

    @Override
    public int getLayoutId() {
        m_streamingContext = NvsStreamingContext.getInstance();
        EventBus.getDefault().register(this);
        return R.layout.activity_add_music;
    }

    @Override
    public void initView() {
        m_titleBar = (CustomTitleBar) findViewById(R.id.title);
        m_bottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        m_musicSingleBtn = (ImageButton) findViewById(R.id.music_single_btn);
        m_musicMultiBtn = (ImageButton) findViewById(R.id.music_multi_btn);
    }


    @Override
    public void initObject() {
        initVideoFragment();
    }

    @Override
    public void initEvents() {
        m_titleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
            }

            @Override
            public void OnNextTextClick() {
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setMusicData(currenTimeline.getTimeData().getMusicData());
                removeTimeline();
                EventBus.getDefault().post(new MessageEvent("music"));
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
                //  AppManager.getInstance().finishActivity();
                finish();
            }


        });

        m_musicSingleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMusicActivity.this, MusicActivity.class);
                intent.putExtra("intype", 0);
                startActivity(intent);
            }
        });

        m_musicMultiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Util.showDialog(MusicActivity.this, "提示", "敬请期待！", "可移步官网联系商务人员");
            }
        });
    }

    @Override
    public void onClickEvent(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("select_music_add")) {
            m_musicInfo = (MusicInfo) messageEvent.getData();
            if (m_timeLine == null) {
                return;
            }
            currenTimeline.getTimeData().setMusicData(m_musicInfo);
            CustomTimelineUtil.buildTimelineMusic(m_timeLine, m_musicInfo);
        }
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        AppManager.getInstance().finishActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ACTIVITY_START_CODE_MUSIC) {

                m_musicInfo = (MusicInfo) data.getSerializableExtra("select_music");
                Log.e("weiwei", "音频时间===========" + m_musicInfo.getFilePath());
                if (m_timeLine == null) {
                    return;
                }
                currenTimeline.getTimeData().setMusicData(m_musicInfo);
                CustomTimelineUtil.buildTimelineMusic(m_timeLine, m_musicInfo);
                // 保存数据
                // TimelineData.instance().setMusicData(m_musicInfo);
            }
        }
    }

    private void removeTimeline() {
        CustomTimelineUtil.removeTimeline(currenTimeline);
        m_timeLine = null;
    }

    private void initVideoFragment() {
        currenTimeline = CustomTimelineUtil.createcCopyTimeline(TimelineManager.getInstance().getCurrenTimeline());

        m_timeLine = currenTimeline.getTimeline();
        if (m_timeLine == null)
            return;
        m_videoFragment = new VideoFragment();
        m_videoFragment.setAutoPlay(true);
        m_videoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                m_videoFragment.seekTimeline(m_streamingContext.getTimelineCurrentPosition(m_timeLine), 0);
            }
        });
        m_videoFragment.setTimeline(m_timeLine);
        Bundle m_bundle = new Bundle();
        m_bundle.putInt("titleHeight", m_titleBar.getLayoutParams().height);
        m_bundle.putInt("bottomHeight", m_bottomLayout.getLayoutParams().height);
        m_bundle.putBoolean("playBarVisible", true);
        m_bundle.putInt("ratio", currenTimeline.getTimeData().getMakeRatio());
        m_videoFragment.setArguments(m_bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.video_layout, m_videoFragment)
                .commit();
    }
}
