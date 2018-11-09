package wedemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.google.gson.Gson;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.ActivityManager;
import com.sven.huinews.international.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wedemo.activity.Caption.CaptionActivity;
import wedemo.activity.PublishActivity;
import wedemo.activity.animatesticker.AnimateStickerActivity;
import wedemo.activity.data.BackupData;
import wedemo.activity.edit.VideoCuttingActivity;
import wedemo.activity.effect.EffectActivity;
import wedemo.activity.music.AudioPlayer;
import wedemo.activity.transition.TransitionActivity;
import wedemo.adapter.EditMenuAdapter;
import wedemo.adapter.FiltersAdapter;
import wedemo.adapter.VideoEditAdapter;
import wedemo.base.BaseActivity;
import wedemo.config.Constant;
import wedemo.db.entity.TimeLineEntity;
import wedemo.fragment.VideoFragment;
import wedemo.shot.bean.FilterItem;
import wedemo.shot.bean.ToolBean;
import wedemo.utils.AppManager;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.LogUtil;
import wedemo.utils.MediaPlayerUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.Util;
import wedemo.utils.asset.NvAsset;
import wedemo.utils.asset.NvAssetManager;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.MusicInfo;
import wedemo.utils.dataInfo.TimeDataCache;
import wedemo.utils.dataInfo.TimelineData;
import wedemo.utils.dataInfo.TransitionInfo;
import wedemo.utils.dataInfo.VideoClipFxInfo;
import wedemo.view.LinearLayoutManagerWithSmoothScroller;
import wedemo.view.MusicHorizontalScrollView;
import wedemo.view.MusicWaveView;

public class DraftsVideoEditActivity extends BaseActivity {

    public static final int REQUESTRESULT_THEME = 1001;
    public static final int REQUESTRESULT_EDIT = 1002;
    public static final int REQUESTRESULT_FILTER = 1003;
    public static final int REQUESTRESULT_STICKER = 1004;
    public static final int REQUESTRESULT_CAPTION = 1005;
    public static final int REQUESTRESULT_TRANSITION = 1006;
    public static final int REQUESTRESULT_MUSIC = 1007;
    public static final int REQUESTRESULT_RECORD = 1008;

    public static final int ADDVIDEO_REQUESTCODE = 1009;//添加视频
    public static final int REQUESTRESULT_EFFECT = 1010;

    private List<ToolBean> mToolDatas;

    private RecyclerView rv_videos;  //视频列表
    private ImageView iv_add_video;
    private VideoEditAdapter editAdapter;

    private NvsStreamingContext mStreamingContext;

    private CustomTimeLine currentTimeline;

    private NvsVideoTrack mVideoTrack;
    private NvsAudioTrack mMusicTrack;
    private VideoFragment mVideoFragment;

    private boolean m_waitFlag = false;
    private ArrayList<ClipInfo> mClipInfoArray = new ArrayList<>();
    //记录视频添加位置
    private int mAddVideoPostion = 0;


    private ProgressBar mCompileProgressBar;
    private TextView mCompileProgressVal;
    private ImageView mCompileCancel;
    private RelativeLayout mCompilePage;
    private LinearLayout ll_volume;
    private ImageButton btn_volume_x;
    private SeekBar seek_sound, seek_music;
    private Switch volume_switch;
    private int lastPos = 0;
    private LinearLayout ll_cut_music;
    private ImageButton btn_cut_music_x;
    private MusicWaveView musicWave;
    private MusicHorizontalScrollView scrollBar;
    private int mScrollX;
    private SimpleDraweeView select_music_image;
    private TextView select_music_name;
    private TextView tv_music_time_start, tv_music_time_end;
    private MusicInfo m_musicInfo;
    private TextView tv_sound;
    private TextView tv_music;
    private RecyclerView rv_menu;
    private EditMenuAdapter adapter;
    private ImageView iv_play_all;
    private LinearLayout ll_seeklayout;
    private LinearLayout ll_right_menu;
    private ImageButton btn_back;
    private TextView btn_next;

    private RecyclerView fliter_rv;
    private TextView tv_scenery;
    private TextView tv_beauty;

    private boolean isSelectMaster = false; // 是否是选中的主线

    private NvAssetManager mAssetManager;

    private ArrayList<FilterItem> mFilterItemArrayList = new ArrayList<>();
    private ArrayList<FilterItem> mFilterItemBeautyArrayList = new ArrayList<>();
    private FiltersAdapter mFiltersAdapter;

    private int filterSelect = 1; //当前选中滤镜分类
    private VideoClipFxInfo videoClipFx;
    private RelativeLayout filter_layout;

    private ImageButton iv_close;

    private int flterPosition = 0;
    private int flterBeautyPosition = 0;
    private TextView tv_flters; //当前滤镜名称
    private TextView tv_flters_type; //当前滤镜类型

    private AlphaAnimation mFitersAnimation;

    private ImageButton ib_top;
    private ImageButton ib_bottom;
    private PagerGridLayoutManager layoutManager;

    @Override
    public int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mStreamingContext = NvsStreamingContext.getInstance();
        EventBus.getDefault().register(this);
        ActivityManager.getInstance().pushOneActivity(this);
        //Eyes.translucentStatusBar(this, true);
        return R.layout.activity_video_edit;
    }


    @Override
    public void initView() {
        rv_videos = findViewById(R.id.rv_videos);
        iv_add_video = findViewById(R.id.iv_add_video);
        mCompileProgressBar = (ProgressBar) findViewById(R.id.compileProgressBar);
        mCompileProgressVal = (TextView) findViewById(R.id.compileProgressVal);
        mCompileCancel = (ImageView) findViewById(R.id.compileCancel);
        mCompilePage = (RelativeLayout) findViewById(R.id.compilePage);
        ll_volume = findViewById(R.id.ll_volume);
        seek_sound = findViewById(R.id.seek_sound);
        seek_music = findViewById(R.id.seek_music);
        btn_volume_x = findViewById(R.id.btn_volume_x);
        volume_switch = findViewById(R.id.volume_switch);
        volume_switch.setChecked(true);
        ll_cut_music = findViewById(R.id.ll_cut_music);
        musicWave = findViewById(R.id.music_wave_view);
        scrollBar = findViewById(R.id.music_scroll_bar);
        btn_cut_music_x = findViewById(R.id.btn_cut_music_x);
        select_music_image = findViewById(R.id.iv_music_image);
        select_music_name = findViewById(R.id.tv_music_name);
        tv_music_time_start = findViewById(R.id.tv_music_time_start);
        tv_music_time_end = findViewById(R.id.tv_music_time_end);

        tv_sound = findViewById(R.id.tv_sound);
        tv_music = findViewById(R.id.tv_music);

        rv_menu = findViewById(R.id.rv_menu);

        iv_play_all = findViewById(R.id.iv_play_all);

        ll_seeklayout = findViewById(R.id.ll_seeklayout);
        ll_right_menu = findViewById(R.id.ll_right_menu);

        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);

        tv_scenery = findViewById(R.id.tv_scenery);
        tv_beauty = findViewById(R.id.tv_beauty);

        fliter_rv = findViewById(R.id.fliter_rv);
        filter_layout = findViewById(R.id.filter_layout);

        iv_close = findViewById(R.id.iv_close);

        tv_flters = findViewById(R.id.tv_flters);
        tv_flters_type = findViewById(R.id.tv_flters_type);

        mFitersAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFitersAnimation.setDuration(1000);
        mFitersAnimation.setFillAfter(true);

        ib_top = findViewById(R.id.ib_top);
        ib_bottom = findViewById(R.id.ib_bottom);

    }


    @Override
    public void onBackPressed() {

        if (isSelectMaster) {
            //如果当前选中master，就是退出预览
            ll_seeklayout.setVisibility(View.VISIBLE);
            ll_right_menu.setVisibility(View.VISIBLE);

            currentTimeline = TimelineManager.getInstance().getCurrenTimeline();
            initVideoFragment();
            isSelectMaster = false;
        } else {
            TimelineManager.getInstance().clear();  //清除timeline
            finish();
        }
    }

    @Override
    public void initEvents() {
        iv_add_video.setOnClickListener(this);
        mCompileCancel.setOnClickListener(this);
        btn_volume_x.setOnClickListener(this);
        ll_volume.setOnClickListener(this);
        btn_cut_music_x.setOnClickListener(this);
        iv_play_all.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        tv_scenery.setOnClickListener(this);
        tv_beauty.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        ib_top.setOnClickListener(this);
        ib_bottom.setOnClickListener(this);


        editAdapter.setOnItemClickListener(new VideoEditAdapter.OnItemClickListener() {
            @Override
            public void onVideoClick(View view, int position) {

                if (lastPos == position) {
                    return;
                } else {
                    lastPos = position;
                }

                TimelineManager.getInstance().setBranch(position);
                currentTimeline = TimelineManager.getInstance().getCurrenTimeline();
                initVideoFragment();

                //切换时间线，也是要黑屏
                //mVideoFragment.updateLivewindow(currentTimeline.getTimeline());
            }

            @Override
            public void onAddClick(View view, int position) {  //点击添加
                selectVideo(position + 1);
            }

            @Override
            public void onTopAddClick(View view, int position) {
                selectVideo(0);
            }
        });

        mStreamingContext.setCompileCallback(new NvsStreamingContext.CompileCallback() {
            @Override
            public void onCompileProgress(NvsTimeline nvsTimeline, int i) {
                mCompileProgressBar.setProgress(i);
                String progress = String.valueOf(i) + "%";
                mCompileProgressVal.setText(progress);
            }

            @Override
            public void onCompileFinished(NvsTimeline nvsTimeline) {
                mCompilePage.setVisibility(View.GONE);
                mStreamingContext.setCompileConfigurations(null);
            }

            @Override
            public void onCompileFailed(NvsTimeline nvsTimeline) {
                mCompilePage.setVisibility(View.GONE);
            }
        });
        mStreamingContext.setCompileCallback2(new NvsStreamingContext.CompileCallback2() {
            @Override
            public void onCompileCompleted(NvsTimeline nvsTimeline, boolean isCanceled) {
                if (!isCanceled) {
                    mCompilePage.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        if (v == iv_add_video) {
            selectVideo(mClipInfoArray.size());
        } else if (v == mCompileCancel) {
            if (mStreamingContext != null) {
                mStreamingContext.stop();
                mCompilePage.setVisibility(View.GONE);
            }
        } else if (v == btn_volume_x) {
            ll_volume.setVisibility(View.GONE);

            mVideoFragment.playVideoButtonCilck();
        } else if (v == btn_cut_music_x) {
            ll_cut_music.setVisibility(View.GONE);

            stopMusic();
            TimelineData.instance().setMasterMusic(m_musicInfo);
            //添加音乐线
            CustomTimelineUtil.createSingleMusic();
            CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线

            mVideoFragment.playVideoButtonCilck();
        } else if (v == iv_play_all) {
            if (TimelineManager.getInstance().getMasterTimeline().getTimeline().getDuration() <= 3 * 1000 * 1000) {
                ToastUtils.showShort(mContext, R.string.tip_short);
                return;
            }

            CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线
            currentTimeline = TimelineManager.getInstance().getMasterTimeline();
            ll_seeklayout.setVisibility(View.GONE);
            ll_right_menu.setVisibility(View.GONE);
            initVideoFragment();
            isSelectMaster = true;
        } else if (v == btn_back) {

            onBackPressed();
        } else if (v == btn_next) {
            if (TimelineManager.getInstance().getMasterTimeline().getTimeline().getDuration() <= 3 * 1000 * 1000) {
                ToastUtils.showShort(mContext, R.string.tip_short);
                return;
            }

            CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线
            startActivity(new Intent(mContext, PublishActivity.class));
        } else if (v == tv_scenery) {
            filterSelect = 0;
            flterPosition = 0;
            mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
            mFiltersAdapter.setSelectPos(-1);

            mFiltersAdapter.notifyDataSetChanged();
            tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
            tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));
        } else if (v == tv_beauty) {
            filterSelect = 1;
            flterBeautyPosition = 0;
            mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
            mFiltersAdapter.setSelectPos(-1);

            mFiltersAdapter.notifyDataSetChanged();
            tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
            tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));
        } else if (v == iv_close) {
            filter_layout.setVisibility(View.GONE);
        } else if (v == ib_top) {
            if (currentIndex == 0) {
                layoutManager.smoothScrollToPage(2);
            } else {
                layoutManager.smoothPrePage();
            }
        } else if (v == ib_bottom) {
            if (currentIndex == 2) {
                layoutManager.smoothScrollToPage(0);
            } else {
                layoutManager.smoothNextPage();
            }
        }
    }


    @Override
    public void initObject() {
        initAssets();
        initData();

        initVideoList();
        initToolRv();
        initTimeLines();

        initBeautyFilterList();
        initFilterList();
        initFilterView();

        initVideoFragment();
        initVolume();

        filterSelect = 1;
        mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
        mFiltersAdapter.setSelectPos(0);
        flterBeautyPosition = 0;

        mFiltersAdapter.notifyDataSetChanged();
        tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
        tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));
        // initCutMusic();

    }

    /**
     * 初始化草稿素材
     */
    private void initData() {
        try {
            TimeLineEntity timeline =(TimeLineEntity)getIntent().getSerializableExtra("timeline");
            Gson gson = new Gson();
            TimeDataCache timeDataCache = gson.fromJson(timeline.getJson(), TimeDataCache.class);
            if(timeDataCache == null){
                ToastUtils.showShort(this,R.string.tip_drafts_error);
                finish();
                return;
            }

            TimelineManager.getInstance().createDraftsTimeLine(timeDataCache,this,timeline.getId());
            mClipInfoArray = TimelineData.instance().getClipInfoData();
        }catch (Exception e){
            e.printStackTrace();
            ToastUtils.showShort(this,R.string.tip_drafts_error);
            finish();
        }

    }

    /**
     * 初始化素材资源
     */
    private void initAssets(){
        mAssetManager = NvAssetManager.sharedInstance(this);
        mAssetManager.searchLocalAssets(NvAsset.ASSET_FILTER);

        String bundlePath = "filter";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_FILTER, bundlePath);

        bundlePath = "filterbeauty";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_FILTER_BEAUTY, bundlePath);
    }

    /**
     * 初始化美颜
     */
    private void initBeautyFilterList() {
        mFilterItemBeautyArrayList.clear();
        FilterItem filterItem = new FilterItem();
        filterItem.setFilterName(getString(R.string.no_info));
        filterItem.setEnglishName(getString(R.string.no_info_eng));
        filterItem.setImageId(R.mipmap.meiyan);
        mFilterItemBeautyArrayList.add(filterItem);

        ArrayList<NvAsset> filterList = getLocalData(NvAsset.ASSET_FILTER_BEAUTY);
        String bundlePath = "filterbeauty/infobeauty.txt";
        Util.getBundleFilterInfo(this, filterList, bundlePath);

        for (NvAsset asset : filterList) {
            FilterItem newFilterItem = new FilterItem();
            if (asset.isReserved()) {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUNDLE);
            } else {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
            }
            newFilterItem.setFilterName(asset.name);
            newFilterItem.setPackageId(asset.uuid);
            newFilterItem.setImageUrl(asset.coverUrl);
            newFilterItem.setEnglishName(asset.englishName);
            mFilterItemBeautyArrayList.add(newFilterItem);
        }
    }

    /**
     * 初始化风景滤镜
     */
    private void initFilterList() {
        FilterItem filterItem = new FilterItem();
        filterItem.setFilterName(getString(R.string.no_info));
        filterItem.setEnglishName(getString(R.string.no_info_eng));
        filterItem.setImageId(R.mipmap.fengjing);

        mFilterItemArrayList.clear();
        mFilterItemArrayList.add(filterItem);


        ArrayList<NvAsset> filterList = getLocalData(NvAsset.ASSET_FILTER);
        String bundlePath = "filter/info.txt";
        Util.getBundleFilterInfo(this, filterList, bundlePath);

        for (NvAsset asset : filterList) {
            FilterItem newFilterItem = new FilterItem();
            if (asset.isReserved()) {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUNDLE);
            } else {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
            }
            newFilterItem.setFilterName(asset.name);
            newFilterItem.setPackageId(asset.uuid);
            newFilterItem.setImageUrl(asset.coverUrl);
            newFilterItem.setEnglishName(asset.englishName);

            mFilterItemArrayList.add(newFilterItem);
        }
    }

    /**
     * 获取滤镜数据
     *
     * @param assetType
     * @return
     */
    private ArrayList<NvAsset> getLocalData(int assetType) {
        return mAssetManager.getUsableAssets(assetType, NvAsset.AspectRatio_All, 0);
    }


    private void initFilterView() {
        mFiltersAdapter = new FiltersAdapter(mContext);
        mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // fliter_rv.setLayoutManager(linearLayoutManager);
        fliter_rv.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this, LinearLayoutManager.HORIZONTAL, false));
        fliter_rv.setAdapter(mFiltersAdapter);
        mFiltersAdapter.setOnItemClickListener(new FiltersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FilterItem filterItem = null;
                videoClipFx.setSelectType(filterSelect);
                videoClipFx.setSelectNum(position);
                if (filterSelect == 0) {
                    if (position < mFilterItemArrayList.size()) {

                        flterPosition = position;

                        if (position == 0) {
                            videoClipFx.setFxId(null);
                            videoClipFx.setFxMode(FilterItem.FILTERMODE_PACKAGE);
                            CustomTimelineUtil.buildTimelineSingleFilter(currentTimeline.getTimeline(), videoClipFx);
                            TimelineManager.getInstance().getCurrenTimeline().getTimeData().setVideoClipFxData(videoClipFx);
                            //CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线
                            // mVideoFragment.playVideoButtonCilck();
                            return;
                        }

                        filterItem = mFilterItemArrayList.get(position);
                        int filterMode = filterItem.getFilterMode();
                        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                            String filterName = filterItem.getFilterName();
                            videoClipFx.setFxId(filterName);
                            videoClipFx.setFxMode(FilterItem.FILTERMODE_BUILTIN);
                        } else {
                            String filterPackageId = filterItem.getPackageId();
                            if (!TextUtils.isEmpty(filterPackageId)) {
                                videoClipFx.setFxId(filterPackageId);
                                videoClipFx.setFxMode(FilterItem.FILTERMODE_PACKAGE);
                            }
                        }
                    }
                } else {

                    if (position < mFilterItemBeautyArrayList.size()) {

                        flterBeautyPosition = position;

                        if (position == 0) {
                            videoClipFx.setFxId(null);
                            videoClipFx.setFxMode(FilterItem.FILTERMODE_PACKAGE);
                            CustomTimelineUtil.buildTimelineSingleFilter(currentTimeline.getTimeline(), videoClipFx);
                            TimelineManager.getInstance().getCurrenTimeline().getTimeData().setVideoClipFxData(videoClipFx);
                            //CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线
                            // mVideoFragment.playVideoButtonCilck();
                            return;
                        }

                        filterItem = mFilterItemBeautyArrayList.get(position);
                        int filterMode = filterItem.getFilterMode();
                        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                            String filterName = filterItem.getFilterName();
                            if (!TextUtils.isEmpty(filterName)) {
                                videoClipFx.setFxId(filterName);
                                videoClipFx.setFxMode(FilterItem.FILTERMODE_BUILTIN);
                            }
                        } else {
                            String filterPackageId = filterItem.getPackageId();
                            if (!TextUtils.isEmpty(filterPackageId)) {
                                videoClipFx.setFxId(filterPackageId);
                                videoClipFx.setFxMode(FilterItem.FILTERMODE_PACKAGE);
                            }
                        }
                    }
                }

                int exposure = 0;
                double mStrengthValue = 0;
                double mWhiteningValue = 0;
                double mReddeningValue = 0;
                if (filterItem != null) {

                    if (filterSelect == 1) {
                        if ("霓虹".equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 0.5;
                            mWhiteningValue = 0.3;
                            mReddeningValue = 0.3;
                        } else if (getString(R.string.no_info).equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 0.5;
                            mWhiteningValue = 0.15;
                            mReddeningValue = 0.4;
                        } else if ("粉嫩".equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 0.6;
                            mWhiteningValue = 0.3;
                            mReddeningValue = 0.3;
                        } else if ("暖茶".equals(filterItem.getFilterName())) {
                            exposure = 2;
                            mStrengthValue = 0.5;
                            mWhiteningValue = 0.4;
                            mReddeningValue = 0.1;
                        } else if ("流星".equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 0.5;
                            mWhiteningValue = 0.35;
                            mReddeningValue = 0.1;
                        } else if ("情书".equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 0.5;
                            mWhiteningValue = 0.3;
                            mReddeningValue = 0;
                        } else if ("美颜白皙".equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 0.4;
                            mWhiteningValue = 0.2;
                            mReddeningValue = 0;
                        } else if ("取景器".equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 1;
                            mWhiteningValue = 0.5;
                            mReddeningValue = 0.5;
                        }
                    } else {
                        if ("甜美".equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 0;
                            mWhiteningValue = 0;
                            mReddeningValue = 0;
                        } else if ("秋日".equals(filterItem.getFilterName())) {
                            exposure = 1;
                            mStrengthValue = 0;
                            mWhiteningValue = 0;
                            mReddeningValue = 0;
                        } else if ("经典".equals(filterItem.getFilterName())) {
                            exposure = -1;
                            mStrengthValue = 0;
                            mWhiteningValue = 0;
                            mReddeningValue = 0;
                        } else if ("蓝调".equals(filterItem.getFilterName())) {
                            exposure = 1;
                            mStrengthValue = 0;
                            mWhiteningValue = 0;
                            mReddeningValue = 0;
                        } else if ("甜美".equals(filterItem.getFilterName())) {
                            exposure = -8;
                            mStrengthValue = 0.5;
                            mWhiteningValue = 0.3;
                            mReddeningValue = 0.7;
                        } else if ("晨曦".equals(filterItem.getFilterName())) {
                            exposure = 0;
                            mStrengthValue = 0.33;
                            mWhiteningValue = 0;
                            mReddeningValue = 0.12;
                        } else if ("筑地 LUT".equals(filterItem.getFilterName())) {
                            exposure = 4;
                            mStrengthValue = 0;
                            mWhiteningValue = 0;
                            mReddeningValue = 0;
                        } else if ("盛夏光年".equals(filterItem.getFilterName())) {
                            exposure = 12;
                            mStrengthValue = 0;
                            mWhiteningValue = 0;
                            mReddeningValue = 0;
                        }
                    }
                }

                videoClipFx.setExposure(exposure);
                videoClipFx.setmWhiteningValue(mWhiteningValue);
                videoClipFx.setmStrengthValue(mStrengthValue);
                videoClipFx.setmReddeningValue(mReddeningValue);

                CustomTimelineUtil.buildTimelineSingleFilter(currentTimeline.getTimeline(), videoClipFx);

                //CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setVideoClipFxData(videoClipFx);
                //mVideoFragment.playVideoButtonCilck();
            }

            @Override
            public void onSameItemClick() {

            }
        });
    }

    /**
     * 选择视频
     */
    private void selectVideo(int pos) {
        mAddVideoPostion = pos;
        BackupData.instance().clearAddClipInfoList();
        Bundle editBundle = new Bundle();
        editBundle.putInt("visitMethod", Constants.FROMCLIPEDITACTIVITYTOVISIT);
        Intent intent = new Intent(DraftsVideoEditActivity.this, ImportVideoActivity.class);
        intent.putExtras(editBundle);
        startActivityForResult(intent, ADDVIDEO_REQUESTCODE);
    }

    /**
     * 初始化视频选择列表
     */
    private void initVideoList() {
        editAdapter = new VideoEditAdapter(mClipInfoArray, TimelineManager.getInstance().getBranch());

        rv_videos.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this, LinearLayoutManager.HORIZONTAL, false));
        rv_videos.setAdapter(editAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        m_waitFlag = false;

        if (!isSelectMaster) {
            tv_flters.startAnimation(mFitersAnimation);
            tv_flters_type.startAnimation(mFitersAnimation);
        }

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("DraftsVideoEditActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("DraftsVideoEditActivity");
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AudioPlayer.getInstance(this).destroyPlayer();
        stopMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMusic();
        ll_cut_music.setVisibility(View.GONE);
    }

    private void initTimeLines() {

        //默认第一个视频线
        TimelineManager.getInstance().setBranch(0);
        currentTimeline = TimelineManager.getInstance().getCurrenTimeline();
        mVideoTrack = currentTimeline.getVideoTrackByIndex(0);
        mMusicTrack = currentTimeline.getAudioTrackByIndex(0);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

            }
        }
    }


    private void toolClickLisenter(int position) {
        if (m_waitFlag)
            return;
        switch (position) {
            case 0:  //filter
//                m_waitFlag = true;
//                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), FilterActivity.class, null, VideoEditActivity.REQUESTRESULT_FILTER);
                if (filter_layout.getVisibility() == View.VISIBLE) {
                    filter_layout.setVisibility(View.GONE);
                    ll_volume.setVisibility(View.GONE);
                    ll_cut_music.setVisibility(View.GONE);
                } else {
                    filter_layout.setVisibility(View.VISIBLE);
                    ll_volume.setVisibility(View.GONE);
                    ll_cut_music.setVisibility(View.GONE);
                }
                break;
            case 1:  // special effect
                m_waitFlag = true;
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), EffectActivity.class, null, DraftsVideoEditActivity.REQUESTRESULT_EFFECT);
                break;
            case 2:  // music
                m_waitFlag = true;

                Intent intent = new Intent(DraftsVideoEditActivity.this, MusicActivity.class);
                intent.putExtra("intype", 0);
                startActivity(intent);
                break;
            case 3: //transition
                if (TimelineManager.getInstance().getSingleTimelineList() != null && TimelineManager.getInstance().getSingleTimelineList().size() > 1) {


                    CustomTimelineUtil.removeTimeFx();  //移除时间特效
                    CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线
                    LogUtil.showLog("重置");
                    m_waitFlag = true;
                    AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), TransitionActivity.class, null, DraftsVideoEditActivity.REQUESTRESULT_TRANSITION);

                } else {
                    new AlertDialog.Builder(DraftsVideoEditActivity.this)
                            .setTitle(R.string.tip)
                            .setMessage(getString(R.string.transition_tip))
                            .setNegativeButton(R.string.edit_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
                break;
            case 4:  //text
                m_waitFlag = true;
                BackupData.instance().setClipIndex(TimelineManager.getInstance().getBranch());
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), CaptionActivity.class, null, DraftsVideoEditActivity.REQUESTRESULT_CAPTION);
                break;
            case 5:  //sticker
                m_waitFlag = true;
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AnimateStickerActivity.class, null, DraftsVideoEditActivity.REQUESTRESULT_STICKER);
                break;
            case 6:
                ll_cut_music.setVisibility(View.GONE);
                filter_layout.setVisibility(View.GONE);

                if (ll_volume.getVisibility() == View.VISIBLE) {
                    ll_volume.setVisibility(View.GONE);
                    mVideoFragment.playVideoButtonCilck();
                } else {
                    ll_volume.setVisibility(View.VISIBLE);
                }

                break;
            case 7:  //cut

                new AlertDialog.Builder(this)
                        .setTitle(R.string.tip)
                        .setMessage(getString(R.string.edit_tip))
                        .setNegativeButton(R.string.cancel_china, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).setPositiveButton(R.string.continues, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        TimelineManager.getInstance().getCurrenTimeline().getTimeData().clear();
                        currentTimeline.getTimeData().clear();

                        CustomTimelineUtil.reSingleBuildVideoTrack();

                        CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线

                        m_waitFlag = true;
                        AppManager.getInstance().jumpActivity(AppManager.getInstance().currentActivity(), VideoCuttingActivity.class, null);
                    }
                }).show();

                break;
            case 8:
                ll_volume.setVisibility(View.GONE);
                filter_layout.setVisibility(View.GONE);

                m_musicInfo = TimelineData.instance().getMasterMusic();
                if (m_musicInfo != null && !TextUtils.isEmpty(m_musicInfo.getFilePath())) {

                    if (ll_cut_music.getVisibility() == View.VISIBLE) {
                        ll_cut_music.setVisibility(View.GONE);
                        mVideoFragment.playVideoButtonCilck();
                    } else {
                        ll_cut_music.setVisibility(View.VISIBLE);
                        //裁剪时移除所有单线音乐
                        CustomTimelineUtil.removeSingleMusic();

                        select_music_name.setText(m_musicInfo.getTitle());
                        tv_music_time_end.setText("/00:" + Math.round(TimelineManager.getInstance().getMasterTimeline().getTimeline().getDuration() / 1000 / 1000));

                        if(m_musicInfo.getId() == Constant.MUSIC_STORE){
                            select_music_image.setImageResource(R.mipmap.default_music);
                        }else {
                            select_music_image.setImageURI(Uri.parse(m_musicInfo.getImagePath()));
                        }

                        initMusic(m_musicInfo.getFilePath());
                        playMusic();
                        initCutMusic(TimelineManager.getInstance().getMasterTimeline().getTimeline().getDuration() / 1000, m_musicInfo.getDuration() / 1000);
                    }

                } else {
                    ToastUtils.showShort(mContext, getString(R.string.cut_tip));
                }

                break;
            case 9:  //copy

                long currentDuration = TimelineManager.getInstance().getCurrenTimeline().getTimeline().getDuration();
                long masterDuration = TimelineManager.getInstance().getMasterTimeline().getTimeline().getDuration();

                if (currentDuration + masterDuration > Constants.LIMIT_TIME) {
                    ToastUtils.showShort(this, getString(R.string.tip_copy_video));
                    return;
                }

                int selectPos = editAdapter.getSelectPos();

                mAddVideoPostion = selectPos;

                ClipInfo clipInfo = mClipInfoArray.get(mAddVideoPostion);
                ArrayList<ClipInfo> addCipInfoList = new ArrayList<>();
                addCipInfoList.add(clipInfo);

                TimelineManager.getInstance().addBranchTimeline(addCipInfoList, mAddVideoPostion);
                mClipInfoArray.addAll(mAddVideoPostion, addCipInfoList);
                BackupData.instance().setClipInfoData(mClipInfoArray);

                TimelineData.instance().setClipInfoData(mClipInfoArray);

                TimelineManager.getInstance().setBranch(TimelineManager.getInstance().getBranch());
                editAdapter.setData(mClipInfoArray, TimelineManager.getInstance().getBranch());

                currentTimeline = TimelineManager.getInstance().getCurrenTimeline();
                mVideoTrack = currentTimeline.getVideoTrackByIndex(0);
                mMusicTrack = currentTimeline.getAudioTrackByIndex(0);
                initVideoFragment();

                CustomTimelineUtil.buildMasterTranistion(DraftsVideoEditActivity.this);
                CustomTimelineUtil.reMasterBuildVideoTrack();
                break;
            case 10:  //delete

                if (mClipInfoArray.size() <= 1) {
                    Toast.makeText(DraftsVideoEditActivity.this, R.string.del_tip, Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectPosDel = editAdapter.getSelectPos();
                if (TimelineManager.getInstance().removeBranchTimeline(selectPosDel)) {
                    mClipInfoArray.remove(selectPosDel);
                    BackupData.instance().setClipInfoData(mClipInfoArray);
                    TimelineData.instance().setClipInfoData(mClipInfoArray);
                    CustomTimelineUtil.buildMasterTranistion(DraftsVideoEditActivity.this);

                    if (TimelineManager.getInstance().getBranch() != 0) {
                        TimelineManager.getInstance().setBranch(TimelineManager.getInstance().getBranch() - 1);
                    }

                    editAdapter.setData(mClipInfoArray, TimelineManager.getInstance().getBranch());

                    currentTimeline = TimelineManager.getInstance().getCurrenTimeline();
                    mVideoTrack = currentTimeline.getVideoTrackByIndex(0);
                    mMusicTrack = currentTimeline.getAudioTrackByIndex(0);
                    initVideoFragment();

                    CustomTimelineUtil.reMasterBuildVideoTrack();
                } else {
                    Toast.makeText(DraftsVideoEditActivity.this, R.string.del_error, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 初始化videofragment
     */
    private void initVideoFragment() {

            videoClipFx = currentTimeline.getTimeData().getVideoClipFxData();
        if (videoClipFx == null) {
            videoClipFx = new VideoClipFxInfo();
        }

        filterSelect = videoClipFx.getSelectType();

        if (filterSelect == 0) {
            mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
            mFiltersAdapter.setSelectPos(videoClipFx.getSelectNum());
            if (videoClipFx.getSelectNum() >= mFilterItemArrayList.size()) {
                flterPosition = 0;
            } else {
                flterPosition = videoClipFx.getSelectNum();
            }

            mFiltersAdapter.notifyDataSetChanged();
            tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
            tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));
            fliter_rv.smoothScrollToPosition(videoClipFx.getSelectNum());
        } else if (filterSelect == 1) {
            mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
            mFiltersAdapter.setSelectPos(videoClipFx.getSelectNum());
            if (videoClipFx.getSelectNum() >= mFilterItemBeautyArrayList.size()) {
                flterBeautyPosition = 0;
            } else {
                flterBeautyPosition = videoClipFx.getSelectNum();
            }

            mFiltersAdapter.notifyDataSetChanged();
            tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
            tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));
            fliter_rv.smoothScrollToPosition(videoClipFx.getSelectNum());
        }

        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(currentTimeline.getTimeline()), 0);
            }
        });
        mVideoFragment.setTimeline(currentTimeline.getTimeline());
        mVideoFragment.setAutoPlay(true);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", 0);
        bundle.putInt("bottomHeight", 0);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("playBarVisible", false);
        bundle.putBoolean("setwindow", false);
        mVideoFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);

        fragmentTransaction.replace(R.id.video_layout, mVideoFragment)
                .commit();

        mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                mVideoFragment.updateSeekBarProgress(0);
                mVideoFragment.updateCurPlayTime(0);
                mVideoFragment.seekTimeline(0, 0);
                mVideoFragment.playVideo(0, currentTimeline.getTimeline().getDuration());
            }

            @Override
            public void playStopped(NvsTimeline timeline) {

            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {

            }

            @Override
            public void streamingEngineStateChanged(int state) {

            }
        });

        mVideoFragment.setVideoScrollListener(new VideoFragment.VideoScrollListener() {
            @Override
            public boolean onLeft(int pos) {

                if (!isSelectMaster) {
                    int selectPos = editAdapter.getSelectPos();

                    if (filterSelect == 0) {
                        if (flterPosition == mFilterItemArrayList.size() - 1) {
                            flterPosition = 0;
                            filterSelect = 1;
                            flterBeautyPosition = 0;

                            mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
                            mFiltersAdapter.setSelectPos(flterBeautyPosition);

                            mFiltersAdapter.notifyDataSetChanged();
                            tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
                            tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));

                            tv_flters.setText(mFilterItemBeautyArrayList.get(flterBeautyPosition).getEnglishName());
                            tv_flters_type.setText(getString(R.string.beauty_ch));

                            tv_flters.startAnimation(mFitersAnimation);
                            tv_flters_type.startAnimation(mFitersAnimation);

                            mFiltersAdapter.callItemClick(flterBeautyPosition);
                            //smoothMoveToPosition(fliters_rv,flterBeautyPosition);
                            fliter_rv.smoothScrollToPosition(flterBeautyPosition);
                            return true;
                        } else {
                            flterPosition++;
                        }

                        if (getString(R.string.no_info).equals(mFilterItemArrayList.get(flterPosition).getFilterName())) {
                            tv_flters.setText(getString(R.string.no_info_eng));
                        } else {
                            tv_flters.setText(mFilterItemArrayList.get(flterPosition).getEnglishName());
                        }
                        tv_flters_type.setText(getString(R.string.scenery));

                        tv_flters.startAnimation(mFitersAnimation);
                        tv_flters_type.startAnimation(mFitersAnimation);

                        mFiltersAdapter.callItemClick(flterPosition);
                        //smoothMoveToPosition(fliters_rv,flterPosition);
                        fliter_rv.smoothScrollToPosition(flterPosition);
                    } else {
                        if (flterBeautyPosition == mFilterItemBeautyArrayList.size() - 1) {
                            flterBeautyPosition = 0;
                            filterSelect = 0;
                            flterPosition = 1;

                            mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
                            mFiltersAdapter.setSelectPos(flterPosition);

                            mFiltersAdapter.notifyDataSetChanged();
                            tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
                            tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));


                            if (getString(R.string.no_info).equals(mFilterItemArrayList.get(flterPosition).getFilterName())) {
                                tv_flters.setText(getString(R.string.no_info_eng));
                            } else {
                                tv_flters.setText(mFilterItemArrayList.get(flterPosition).getEnglishName());
                            }
                            tv_flters_type.setText(getString(R.string.scenery));

                            tv_flters.startAnimation(mFitersAnimation);
                            tv_flters_type.startAnimation(mFitersAnimation);

                            mFiltersAdapter.callItemClick(flterPosition);
                            //smoothMoveToPosition(fliters_rv,flterPosition);
                            fliter_rv.smoothScrollToPosition(flterPosition);
                            return true;
                        } else {
                            flterBeautyPosition++;
                        }
                        tv_flters.setText(mFilterItemBeautyArrayList.get(flterBeautyPosition).getEnglishName());
                        tv_flters_type.setText(getString(R.string.beauty_ch));

                        tv_flters.startAnimation(mFitersAnimation);
                        tv_flters_type.startAnimation(mFitersAnimation);

                        mFiltersAdapter.callItemClick(flterBeautyPosition);
                        // smoothMoveToPosition(fliters_rv,flterBeautyPosition);
                        fliter_rv.smoothScrollToPosition(flterBeautyPosition);
                    }
                }
                return true;
            }

            @Override
            public boolean onRight(int pos) {
                if (!isSelectMaster) {
                    int selectPos = editAdapter.getSelectPos();

                    if (filterSelect == 0) {
                        if (flterPosition == 1 || flterPosition == 0) {
                            flterPosition = mFilterItemArrayList.size();
                            filterSelect = 1;
                            flterBeautyPosition = mFilterItemBeautyArrayList.size() - 1;

                            mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
                            mFiltersAdapter.setSelectPos(flterBeautyPosition);

                            mFiltersAdapter.notifyDataSetChanged();
                            tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
                            tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));

                            tv_flters.setText(mFilterItemBeautyArrayList.get(flterBeautyPosition).getEnglishName());
                            tv_flters_type.setText(getString(R.string.beauty_ch));

                            tv_flters.startAnimation(mFitersAnimation);
                            tv_flters_type.startAnimation(mFitersAnimation);

                            mFiltersAdapter.callItemClick(flterBeautyPosition);
                            //smoothMoveToPosition(fliters_rv, flterBeautyPosition);
                            fliter_rv.smoothScrollToPosition(flterBeautyPosition);
                            return true;
                        } else {
                            flterPosition--;
                        }

                        if (getString(R.string.no_info).equals(mFilterItemArrayList.get(flterPosition).getFilterName())) {
                            tv_flters.setText(getString(R.string.no_info_eng));
                        } else {
                            tv_flters.setText(mFilterItemArrayList.get(flterPosition).getEnglishName());
                        }
                        tv_flters_type.setText(getString(R.string.scenery));

                        tv_flters.startAnimation(mFitersAnimation);
                        tv_flters_type.startAnimation(mFitersAnimation);

                        mFiltersAdapter.callItemClick(flterPosition);
                        // smoothMoveToPosition(fliters_rv,flterPosition);
                        fliter_rv.smoothScrollToPosition(flterPosition);
                    } else {
                        if (flterBeautyPosition == 0) {
                            flterBeautyPosition = mFilterItemBeautyArrayList.size();
                            filterSelect = 0;
                            flterPosition = mFilterItemArrayList.size() - 1;

                            mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
                            mFiltersAdapter.setSelectPos(flterPosition);

                            mFiltersAdapter.notifyDataSetChanged();
                            tv_beauty.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.a4a4));
                            tv_scenery.setTextColor(ContextCompat.getColor(DraftsVideoEditActivity.this, R.color.bg_white));

                            if (getString(R.string.no_info).equals(mFilterItemArrayList.get(flterPosition).getFilterName())) {
                                tv_flters.setText(getString(R.string.no_info_eng));
                            } else {
                                tv_flters.setText(mFilterItemArrayList.get(flterPosition).getEnglishName());
                            }
                            tv_flters_type.setText(getString(R.string.scenery));

                            tv_flters.startAnimation(mFitersAnimation);
                            tv_flters_type.startAnimation(mFitersAnimation);

                            mFiltersAdapter.callItemClick(flterPosition);
                            //smoothMoveToPosition(fliters_rv,flterPosition);
                            fliter_rv.smoothScrollToPosition(flterPosition);
                            return true;
                        } else {
                            flterBeautyPosition--;
                        }
                        tv_flters.setText(mFilterItemBeautyArrayList.get(flterBeautyPosition).getEnglishName());
                        tv_flters_type.setText(getString(R.string.beauty_ch));

                        tv_flters.startAnimation(mFitersAnimation);
                        tv_flters_type.startAnimation(mFitersAnimation);

                        mFiltersAdapter.callItemClick(flterBeautyPosition);
                        // smoothMoveToPosition(fliters_rv,flterBeautyPosition);
                        fliter_rv.smoothScrollToPosition(flterBeautyPosition);
                    }
                }
                return true;
            }
        });
    }

    /**
     * 初始化工具集合
     */
    private int currentIndex = 0;

    private void initToolRv() {
        int[] mResId = {R.mipmap.icon_edit_filter, R.mipmap.icon_edit_special, R.mipmap.icon_edit_music, R.mipmap.icon_edit_tranisn,
                R.mipmap.icon_edit_text, R.mipmap.icon_shot_dj, R.mipmap.icon_edit_volume,
                R.mipmap.icon_video_edit, R.mipmap.icon_edit_cutmusec, R.mipmap.icon_edit_add, R.mipmap.del_video};
        String[] mStrId = getResources().getStringArray(R.array.edit_video_labels);

        mToolDatas = new ArrayList<>();
        for (int i = 0; i < mStrId.length; i++) {
            mToolDatas.add(new ToolBean(mResId[i], mStrId[i]));
        }

        // 1.水平分页布局管理器
        layoutManager = new PagerGridLayoutManager(
                4, 1, PagerGridLayoutManager.VERTICAL);
        layoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                currentIndex = pageIndex;
            }
        });

        rv_menu.setLayoutManager(layoutManager);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(rv_menu);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rv_menu.setLayoutManager(linearLayoutManager);
        rv_menu.setHasFixedSize(true);
//        rv_menu.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                if (parent.getChildPosition(view) != 0)
//                    outRect.top = ScreenUtils.dip2px(VideoEditActivity.this,17);
//            }
//        });
        adapter = new EditMenuAdapter(mToolDatas);
        rv_menu.setAdapter(adapter);
        adapter.setOnItemClick(new EditMenuAdapter.OnItemClick() {
            @Override
            public void onClick(View view, int pos) {
                toolClickLisenter(pos);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        Log.e("weiwei", "event");
        long cur_time = mStreamingContext.getTimelineCurrentPosition(currentTimeline.getTimeline());
        if (messageEvent.getMessage().equals("music")) {
            m_musicInfo = (MusicInfo) messageEvent.getData();

            //TimelineManager.getInstance().getCurrenTimeline().getTimeData().setMusicData(m_musicInfo);

            select_music_name.setText(m_musicInfo.getTitle());
            tv_music_time_end.setText("/00:" + Math.round(TimelineManager.getInstance().getMasterTimeline().getTimeline().getDuration() / 1000 / 1000));

            if(m_musicInfo.getId() == Constant.MUSIC_STORE){
                select_music_image.setImageResource(R.mipmap.default_music);
            }else {
                select_music_image.setImageURI(Uri.parse(m_musicInfo.getImagePath()));
            }

            m_musicInfo.setTrimIn(0);
            m_musicInfo.setTrimOut(m_musicInfo.getDuration());
            TimelineData.instance().setMasterMusic(m_musicInfo);
            CustomTimelineUtil.reSingleBuildVideoTrack();

            //添加音乐线
            CustomTimelineUtil.createSingleMusic();
            mVideoFragment.playVideoButtonCilck();

        } else if (messageEvent.getMessage().equals("sticker")) {
            CustomTimelineUtil.setAllSticker(currentTimeline);

            mVideoFragment.seekTimeline(cur_time, 0);
            mVideoFragment.updateTotalDuarationText();
            mVideoFragment.updateSeekBarMaxValue();
        } else if (messageEvent.getMessage().equals("edit")) {
            mClipInfoArray = BackupData.instance().getClipInfoData();
            BackupData.instance().setClipInfoData(mClipInfoArray);
            BackupData.instance().clearAddClipInfoList();
            TimelineData.instance().setClipInfoData(mClipInfoArray);

            CustomTimelineUtil.reSingleBuildVideoTrack();
            CustomTimelineUtil.createSingleMusic();

            editAdapter.setData(mClipInfoArray, TimelineManager.getInstance().getBranch());

            currentTimeline = TimelineManager.getInstance().getCurrenTimeline();
            mVideoTrack = currentTimeline.getVideoTrackByIndex(0);
            mMusicTrack = currentTimeline.getAudioTrackByIndex(0);
            //initVideoFragment();

            mVideoFragment.seekTimeline(cur_time, 0);
            mVideoFragment.updateTotalDuarationText();
            mVideoFragment.updateSeekBarMaxValue();
        } else if (messageEvent.getMessage().equals("caption")) {
            CustomTimelineUtil.setAllCaption(currentTimeline);

            mVideoFragment.seekTimeline(cur_time, 0);
            mVideoFragment.updateTotalDuarationText();
            mVideoFragment.updateSeekBarMaxValue();
        } else if (messageEvent.getMessage().equals("transition")) {
            TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
            TimelineManager.getInstance().getMasterTimeline().getTimeData().setTransitionData(transitionInfo);
        } else if (messageEvent.getMessage().equals("effect")) {

//            currentTimeline.getTimeData().setTimelineFx(
//                    TimelineManager.getInstance().getCurrenTimeline().getTimeData().getTimelineFx());
//            CustomTimelineUtil.setTimelineData(currentTimeline);
            CustomTimelineUtil.reSingleBuildVideoTrack();
        } else if (messageEvent.getMessage().equals("filter")) {
            CustomTimelineUtil.reSingleBuildVideoTrack();
        }

        CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        long cur_time = mStreamingContext.getTimelineCurrentPosition(currentTimeline.getTimeline());
        switch (requestCode) {
            case REQUESTRESULT_THEME:

                break;
            case REQUESTRESULT_EDIT:
                mClipInfoArray = BackupData.instance().getClipInfoData();
                BackupData.instance().setClipInfoData(mClipInfoArray);
                BackupData.instance().clearAddClipInfoList();
                TimelineData.instance().setClipInfoData(mClipInfoArray);

                CustomTimelineUtil.reSingleBuildVideoTrack();

                editAdapter.setData(mClipInfoArray, TimelineManager.getInstance().getBranch());

                currentTimeline = TimelineManager.getInstance().getCurrenTimeline();
                mVideoTrack = currentTimeline.getVideoTrackByIndex(0);
                mMusicTrack = currentTimeline.getAudioTrackByIndex(0);
                initVideoFragment();
                break;
            case REQUESTRESULT_FILTER:

                break;
            case REQUESTRESULT_STICKER:
                CustomTimelineUtil.setAllSticker(currentTimeline);

                mVideoFragment.seekTimeline(cur_time, 0);
                mVideoFragment.updateTotalDuarationText();
                mVideoFragment.updateSeekBarMaxValue();
                break;
            case REQUESTRESULT_CAPTION:
                CustomTimelineUtil.setAllCaption(currentTimeline);

                mVideoFragment.seekTimeline(cur_time, 0);
                mVideoFragment.updateTotalDuarationText();
                mVideoFragment.updateSeekBarMaxValue();
                break;
            case REQUESTRESULT_TRANSITION:
                TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
                TimelineManager.getInstance().getMasterTimeline().getTimeData().setTransitionData(transitionInfo);
                //CustomTimelineUtil.setTransition(TimelineManager.getInstance().getMasterTimeline(), transitionInfo);
                break;
            case REQUESTRESULT_MUSIC:   //音乐添加
                // musicInfo = (MusicListResponse.DataBean) data.getSerializableExtra("select_music");
                // TimelineUtil.buildTimelineMusic1(currentTimeline, musicInfo);

                CustomTimelineUtil.reSingleBuildVideoTrack();
                //  playMusic(musicInfo.getMusic_path());
//                playMusic(musicInfo);
//                select_music_image.setImageURI(Uri.parse(musicInfo.getMusic_cover()));
//                select_music_name.setText(musicInfo.getTitle());
//                initCutMusic(currentTimeline.getTimeline().getDuration() / 1000, musicInfo.getMusic_duration() * 1000);

                break;
            case REQUESTRESULT_EFFECT:  //特效
                CustomTimelineUtil.reSingleBuildVideoTrack();
                break;
            case REQUESTRESULT_RECORD:

                break;
            case ADDVIDEO_REQUESTCODE: //视频添加
                ArrayList<ClipInfo> addCipInfoList = BackupData.instance().getAddClipInfoList();
                int size = addCipInfoList.size();
                if (addCipInfoList.size() > 0) {
                    //创建新的时间线
                    TimelineManager.getInstance().addBranchTimeline(addCipInfoList, mAddVideoPostion);
                    mClipInfoArray.addAll(mAddVideoPostion, addCipInfoList);
                    BackupData.instance().setClipInfoData(mClipInfoArray);
                    BackupData.instance().clearAddClipInfoList();

                }

                TimelineData.instance().setClipInfoData(mClipInfoArray);

                CustomTimelineUtil.buildMasterTranistion(DraftsVideoEditActivity.this);
                TimelineManager.getInstance().setBranch(TimelineManager.getInstance().getBranch());
                editAdapter.setData(mClipInfoArray, TimelineManager.getInstance().getBranch());

                currentTimeline = TimelineManager.getInstance().getCurrenTimeline();
                mVideoTrack = currentTimeline.getVideoTrackByIndex(0);
                mMusicTrack = currentTimeline.getAudioTrackByIndex(0);
                initVideoFragment();
                break;
            default:
                break;
        }

        CustomTimelineUtil.reMasterBuildVideoTrack();  //重置总线时间线
    }


    private void initVolume() {

        mVideoFragment.setVideoVolumeListener(new VideoFragment.VideoVolumeListener() {
            @Override
            public void onVideoVolume() {
                ll_volume.setVisibility(View.VISIBLE);
                ll_cut_music.setVisibility(View.GONE);
            }
        });

        seek_sound.setMax(100);
        seek_music.setMax(100);

        int volumeVal = (int) Math.floor(mVideoTrack.getVolumeGain().leftVolume / Constants.VIDEOVOLUME_MAXVOLUMEVALUE * Constants.VIDEOVOLUME_MAXSEEKBAR_VALUE + 0.5D);

        updateVideoVoiceSeekBar(volumeVal);
        updateMusicVoiceSeekBar(volumeVal);

        seek_sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                LogUtil.showLog("改变");
                if (b) {
                    updateVideoVoiceSeekBar(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                CustomTimelineUtil.buildSound(currentTimeline);
            }
        });

        seek_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    updateMusicVoiceSeekBar(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                CustomTimelineUtil.buildSound(currentTimeline);
            }
        });

        volume_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seek_sound.setEnabled(true);
                    seek_music.setEnabled(true);
                    setVideoVoice(videoVoice);
                    setMusicVoice(musicVoice);
                } else {
                    seek_sound.setProgress(0);
                    seek_music.setProgress(0);

                    tv_sound.setText("0");
                    tv_music.setText("0");

                    seek_sound.setEnabled(false);
                    seek_music.setEnabled(false);

                    mVideoTrack.setVolumeGain(0, 0);
                    mMusicTrack.setVolumeGain(0, 0);
                    TimelineData.instance().setOriginVideoVolume(0);
                    TimelineData.instance().setMusicVolume(0);
                }

                //CustomTimelineUtil.reSingleBuildVideoTrack();
                //CustomTimelineUtil.reMasterBuildVideoTrack();
                CustomTimelineUtil.buildSound(currentTimeline);
            }
        });
    }

    private void updateMusicVoiceSeekBar(int volumeVal) {
        seek_music.setProgress(volumeVal);
        tv_music.setText(volumeVal + "");
        setMusicVoice(volumeVal);
    }

    private void updateVideoVoiceSeekBar(int volumeVal) {
        seek_sound.setProgress(volumeVal);
        tv_sound.setText(volumeVal + "");
        setVideoVoice(volumeVal);
    }

    int videoVoice;
    int musicVoice;

    private void setVideoVoice(int voiceVal) {
        seek_sound.setProgress(voiceVal);
        tv_sound.setText(voiceVal + "");
        // mVideoVoiceSeekBarValue.setText(String.valueOf(voiceVal));
        float volumeVal = voiceVal * Constants.VIDEOVOLUME_MAXVOLUMEVALUE / Constants.VIDEOVOLUME_MAXSEEKBAR_VALUE;
        videoVoice = voiceVal;

        LogUtil.showLog("msg---videoVocie:" + videoVoice);
        if (mVideoTrack == null)
            return;

        mVideoTrack.setVolumeGain(volumeVal, volumeVal);
        TimelineData.instance().setOriginVideoVolume(volumeVal);

    }

    private void setMusicVoice(int voiceVal) {
        seek_music.setProgress(voiceVal);
        tv_music.setText(voiceVal + "");
        float volumeVal = voiceVal * Constants.VIDEOVOLUME_MAXVOLUMEVALUE / Constants.VIDEOVOLUME_MAXSEEKBAR_VALUE;
        musicVoice = voiceVal;

        if (mMusicTrack == null)
            return;

        mMusicTrack.setVolumeGain(volumeVal, volumeVal);
        TimelineData.instance().setMusicVolume(volumeVal);
    }


    private void initCutMusic(final long displayTime, final long totalTime) {
        LogUtil.showLog("msg----displayTime:" + displayTime + "----totalTime:" + totalTime);
        musicWave.setDisplayTime((int) displayTime);
        musicWave.setTotalTime((int) totalTime);
        musicWave.layout();


        scrollBar.setScrollViewListener(new MusicHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(HorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                mScrollX = x;
                setDurationTxt((int) displayTime, x, (int) totalTime);

            }

            @Override
            public void onScrollStop() {
                int leftTime = (int) ((float) mScrollX / musicWave.getMusicLayoutWidth() * totalTime);
                //LogUtil.showLog("msg----mScrollX:" + mScrollX +"=================:"+leftTime);

                seekMusic(leftTime);
                m_musicInfo.setTrimIn(leftTime * 1000);
                m_musicInfo.setTrimOut(m_musicInfo.getDuration());
            }
        });
    }


    private void setDurationTxt(int displayTime, int x, int duration) {
        int leftTime = (int) ((float) x / musicWave.getMusicLayoutWidth() * duration);
        int rightTime = leftTime + displayTime;
        int time = leftTime / 1000;
        int min = time / 60;
        int sec = time % 60;
        tv_music_time_start.setText(String.format("%1$02d:%2$02d", min, sec));
        time = rightTime / 1000;
        min = time / 60;
        sec = time % 60;
        //  holder.musicEndTxt.setText(String.format("%1$02d:%2$02d", min, sec));
        tv_music_time_end.setText("/" + String.format("%1$02d:%2$02d", min, sec));

    }


    private MediaPlayerUtil player;

    private void initMusic(String path) {
        if (player != null) {
            player.stop();
        }

        player = new MediaPlayerUtil();
        player.setUrl(path);
    }

    private void playMusic() {
        if (player != null) {
            player.play();
            player.setRePlay(true);
        }
    }

    private void puseMusic() {
        if (player != null) {
            player.pause();
        }
    }

    private void seekMusic(int pos) {
        if (player != null) {
            player.seekTo(pos);
        }
    }

    /**
     * 停止音乐
     */
    private void stopMusic() {
        //AudioPlayer.getInstance(this).destroyPlayer();
        if (player != null) {
            player.stop();
        }
    }
}