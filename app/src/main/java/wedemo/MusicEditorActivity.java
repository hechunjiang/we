package wedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.R;

import wedemo.fragment.MusicEditorFragment;
import wedemo.fragment.PreviewFragment;
import wedemo.utils.TimelineUtil;

public class MusicEditorActivity extends AppCompatActivity {
    public static NvsStreamingContext mStreamingContext = NvsStreamingContext.getInstance();
    public static NvsTimeline mTimeline = TimelineUtil.createTimeline();
    public static NvsVideoTrack mVideoTrack;

    private PreviewFragment previewFragment;
    private MusicEditorFragment musicEditorFragment;
    private ImageView iv_back;
    private TextView tv_right_menu;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_right_menu = findViewById(R.id.tv_right_menu);
        tv_right_menu.setText("" + rightMenuText());

        previewFragment = new PreviewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_preview, previewFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_editor, getContentFragment()).commit();
        getContentFragment().setPlayListener(previewFragment.getPlayListener());
    }


    private void initData() {
        if (mTimeline != null) mVideoTrack = mTimeline.getVideoTrackByIndex(0);
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_right_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightMenuClick();
            }
        });
    }

    public String rightMenuText() {
        return getString(R.string.right_menu_text_save);
    }

    public MusicEditorFragment getContentFragment() {
        if (musicEditorFragment == null) {
            musicEditorFragment = new MusicEditorFragment();
        }
        return musicEditorFragment;
    }

    public void rightMenuClick() {
    }
}
