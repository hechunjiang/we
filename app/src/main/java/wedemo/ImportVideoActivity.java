package wedemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsVideoResolution;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.ActivityManager;
import com.sven.huinews.international.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wedemo.activity.data.BackupData;
import wedemo.adapter.ImportVideoAdapter;
import wedemo.base.BaseActivity;
import wedemo.shot.bean.MediaData;
import wedemo.utils.AlbumItemDecoration;
import wedemo.utils.Constants;
import wedemo.utils.MediaUtils;
import wedemo.utils.TimelineManager;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.TimelineData;

public class ImportVideoActivity extends BaseActivity {
    private RecyclerView vidoes_rv;
    private ImportVideoAdapter mImportVideoAdapter;
    private TextView btn_next;
    private RelativeLayout mSelectCreateRatio;
    private Button button16v9, button1v1, button9v16, button3v4;
    private ImageButton btn_back;
    private int type = Constants.FROMMAINACTIVITYTOVISIT; // 进入方式,默认主页进入

    @Override
    public int getLayoutId() {
        ActivityManager.getInstance().pushOneActivity(this);
        return R.layout.activity_media;
    }

    @Override
    public void initView() {
        vidoes_rv = findViewById(R.id.vidoes_rv);
        btn_next = findViewById(R.id.btn_next);
        mSelectCreateRatio = findViewById(R.id.selectCreateRatio);
        button16v9 = findViewById(R.id.button16v9);
        button1v1 = findViewById(R.id.button1v1);
        button9v16 = findViewById(R.id.button9v16);
        button3v4 = findViewById(R.id.button3v4);
        btn_back = findViewById(R.id.btn_back);
    }

    @Override
    public void initEvents() {
        btn_next.setOnClickListener(this);
        button16v9.setOnClickListener(this);
        button1v1.setOnClickListener(this);
        button9v16.setOnClickListener(this);
        button3v4.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        mSelectCreateRatio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSelectCreateRatio.setVisibility(View.GONE);
                return true;
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        if (v == btn_next) {

            if (getClipInfoList() == null || getClipInfoList().size() == 0) {
                ToastUtils.showShort(this, getString(R.string.tip_select));
                return;
            }

            Log.e("weiwei", "getSelectVideoDuration() = " + getSelectVideoDuration());
            long totle = Constants.LIMIT_TIME;
            if (getSelectVideoDuration() > totle) {
                ToastUtils.showShort(this, getString(R.string.tip_select_video));
                return;
            }

            if (getRecordDuration() > totle) {
                ToastUtils.showShort(this, getString(R.string.tip_select_video));
                return;
            }

            CustomTimeLine masterTimeline = TimelineManager.getInstance().getMasterTimeline();
            if (masterTimeline != null && masterTimeline.getTimeline() != null) {
                long duration = masterTimeline.getTimeline().getDuration();

                if (duration + getSelectVideoDuration() > totle) {
                    ToastUtils.showShort(this, getString(R.string.tip_select_video));
                    return;
                }
            }

            long durationSelRec = getSelectVideoDuration() + getRecordDuration();
            Log.e("weiwei", "durationSelRec" + durationSelRec);
            if (durationSelRec > totle) {
                ToastUtils.showShort(this, getString(R.string.tip_select_video));
                return;
            }

            getClipInfoList();

            if (type == Constants.FROMMAINACTIVITYTOVISIT) {
                //mSelectCreateRatio.setVisibility(View.VISIBLE);
                selectCreateRatio(Constants.RESOLUTION_TYPE);
            } else if (type == Constants.FROMCLIPEDITACTIVITYTOVISIT) {
                ArrayList<ClipInfo> clipInfos = getClipInfoList();
                BackupData.instance().setAddClipInfoList(clipInfos);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

        } else if (v == button16v9) {
            selectCreateRatio(1);//16:9
        } else if (v == button1v1) {
            selectCreateRatio(2);//1:1
        } else if (v == button9v16) {
            selectCreateRatio(4);//9:16
        } else if (v == button3v4) {
            selectCreateRatio(8);//3:4
        } else if (v == btn_back) {
            finish();
        }
    }

    @Override
    public void initObject() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getInt("visitMethod");
        }

        showLocalMedia();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mImportVideoAdapter = new ImportVideoAdapter(mContext);
        vidoes_rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        vidoes_rv.addItemDecoration(new AlbumItemDecoration());
        vidoes_rv.setAdapter(mImportVideoAdapter);
    }

    private void showLocalMedia() {
        MediaUtils.getAllVideoInfos(this, new MediaUtils.LocalMediaCallback() {
            @Override
            public void onLocalMediaCallback(List<MediaData> mDatas) {
                mImportVideoAdapter.setData(mDatas);
            }
        });
    }

    /**
     * 选择比例跳转
     *
     * @param index
     */
    private void selectCreateRatio(int index) {
        mSelectCreateRatio.setVisibility(View.GONE);

        ArrayList<ClipInfo> clipInfoData = TimelineData.instance().getClipInfoData();

        if (clipInfoData == null) {
            clipInfoData = new ArrayList<>();
        }

        ArrayList<ClipInfo> pathList = getClipInfoList();
        clipInfoData.addAll(pathList);

        TimelineData.instance().setVideoResolution(getVideoEditResolution(index));
        TimelineData.instance().setClipInfoData(clipInfoData);
        TimelineData.instance().setMakeRatio(Constants.SCREEN_TYPE);
        startActivity(new Intent(mContext, VideoEditActivity.class));
        finish();
    }

    /**
     * 获取选中的视频
     *
     * @return
     */
    private ArrayList<ClipInfo> getClipInfoList() {
        ArrayList<ClipInfo> pathList = new ArrayList<>();
        for (MediaData mediaData : mImportVideoAdapter.getSelectList()) {
            ClipInfo clipInfo = new ClipInfo();
            clipInfo.setFilePath(mediaData.getPath());
            pathList.add(clipInfo);
        }
        return pathList;
    }

    private long getSelectVideoDuration() {
        long duration = 0;
        for (MediaData mediaData : mImportVideoAdapter.getSelectList()) {
            duration += mediaData.getDuration();
        }
        return duration * 1000;
    }

    private long getRecordDuration() {
        long duration = 0;

        ArrayList<ClipInfo> clipInfoData = TimelineData.instance().getClipInfoData();
        if (clipInfoData == null || clipInfoData.size() == 0) {
            return duration;
        }
        for (ClipInfo clipInfo : clipInfoData) {
            MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(clipInfo.getFilePath());  //recordingFilePath（）为音频文件的路径
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            long current = player.getDuration();//获取音频的时间
            duration += current;
            player.release();//记得释放资源
        }
        Log.d("ACETEST", "### duration: " + duration);
        return duration * 1000;

    }

    /**
     * 设置编辑时的比例对于的分辨率
     *
     * @param index
     * @return
     */
    private NvsVideoResolution getVideoEditResolution(int index) {
        NvsVideoResolution videoEditRes = new NvsVideoResolution();
        switch (index) {
            case 1: // 16:9
                videoEditRes.imageWidth = 1280;
                videoEditRes.imageHeight = 720;
                break;
            case 2: //1:1
                videoEditRes.imageWidth = 720;
                videoEditRes.imageHeight = 720;
                break;
            case 4: //9:16
                videoEditRes.imageWidth = Constants.IMAGE_WIDTH;
                videoEditRes.imageHeight = Constants.IMAGE_HEIGHT;
                break;
            case 8: // 3:4
                videoEditRes.imageWidth = 720;
                videoEditRes.imageHeight = 960;
                break;
            default: // 16:9
                videoEditRes.imageWidth = 1280;
                videoEditRes.imageHeight = 720;
                break;
        }
        return videoEditRes;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("ImportVideoActivity");
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("ImportVideoActivity");
            MobclickAgent.onResume(this);
        }
    }
}
