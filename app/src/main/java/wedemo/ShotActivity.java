package wedemo;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.facebook.drawee.view.SimpleDraweeView;
import com.meicam.sdk.NvsAREffect;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsCaptureVideoFx;
import com.meicam.sdk.NvsFxDescription;
import com.meicam.sdk.NvsLiveWindowExt;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsVideoResolution;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.ActivityManager;
import com.sven.huinews.international.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wedemo.activity.down.CategoryResponse;
import wedemo.activity.down.DownContract;
import wedemo.activity.down.DownModel;
import wedemo.activity.down.DownPresenter;
import wedemo.activity.down.DownRequest;
import wedemo.activity.down.DownResponse;
import wedemo.activity.music.AudioPlayer;
import wedemo.adapter.FiltersAdapter;
import wedemo.adapter.StickerAdapter;
import wedemo.adapter.VideoShowAdapter;
import wedemo.base.BaseActivity;
import wedemo.config.Constant;
import wedemo.shot.bean.FilterItem;
import wedemo.utils.Constants;
import wedemo.utils.LogUtil;
import wedemo.utils.MediaPlayerUtil;
import wedemo.utils.MediaScannerUtil;
import wedemo.utils.ParameterSettingValues;
import wedemo.utils.PathUtils;
import wedemo.utils.TimelineManager;
import wedemo.utils.Util;
import wedemo.utils.VideoEffectUtil;
import wedemo.utils.asset.NvAsset;
import wedemo.utils.asset.NvAssetManager;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.Filter;
import wedemo.utils.dataInfo.MusicInfo;
import wedemo.utils.dataInfo.TimelineData;
import wedemo.view.CircleView;
import wedemo.view.MusicHorizontalScrollView;
import wedemo.view.MusicWaveView;
import wedemo.view.ShotBlockDialog;



/**
 * enum AssetPackageError    {
 * NoError,
 * NameError,
 * AlreadyInstalledError,
 * WorkingInProgress,
 * NotInstalledError,
 * ImproperStatusError,
 * DecompressionError,
 * InvalidPackageError,
 * AssetTypeError,
 * PermissionError,
 * MetaContentError,
 * SdkVersionError,
 * UpgradeVersionError,
 * IOError,
 * ResourceError    };
 * <p>
 * 那个15是横纵比，AspectRatio_16v9 = 1,
 * AspectRatio_1v1 = 2,
 * AspectRatio_9v16 = 4,
 * AspectRatio_3v4 = 8,这几个比率按位或起来正好是15.
 * 拍摄
 */
public class ShotActivity extends BaseActivity<DownPresenter, DownModel> implements
        NvsStreamingContext.CaptureDeviceCallback,
        NvsStreamingContext.CaptureRecordingDurationCallback,
        NvsStreamingContext.CaptureRecordingStartedCallback,
        DownContract.View {
    public static final int REQUES_MUSIC = 101;

    private NvsLiveWindowExt mLiveWindow;
    private NvsStreamingContext mStreamingContext;
    NvsStreamingContext.CaptureDeviceCapability mCapability = null;
    private boolean mPermissionGranted;
    private int mCurrentDeviceIndex;
    /*已拍摄的列表*/
    private RecyclerView rv_videos;
    /*滤镜*/
    private LinearLayout ll_fliter;
    private RecyclerView fliters_rv;
    private ImageView btn_fliters;
    private ImageView btn_ratios;
    private ImageView btn_stickers;
    private ImageView btn_sets;
    private ImageButton btn_back;
    private FiltersAdapter mFiltersAdapter;
    private ArrayList<FilterItem> mFilterItemArrayList = new ArrayList<>();
    private ArrayList<FilterItem> mFilterItemBeautyArrayList = new ArrayList<>();
    private NvsCaptureVideoFx mCurCaptureVideoFx;


    //对焦
    private ImageView mImageAutoFocusRect;
    private AlphaAnimation mFocusAnimation, mFitersAnimation;
    //拍摄速率
    private RadioGroup rg_video;
    private float speed = 1;  //默认速率
    private int mSpeedMode = 3;
    private int moreSlowSpeedMode = 1;
    private int slowSpeedMode = 2;
    private int normalSpeedMode = 3;
    private int fastSpeedMode = 4;
    private int moreFastSpeedMode = 5;
    //美颜
    private SeekBar strength; //磨皮
    private SeekBar mWhitening; //美白
    private SeekBar mReddening; //红润
    private double mStrengthValue;
    private double mWhiteningValue;
    private double mReddeningValue;
    private boolean isDown = false; //是否处于倒计时状态


    private long inTime = 0;
    private String m_currentFxName = "None";
    private List<Filter> filterList;
    private boolean m_filterMode = false;

    //切换摄像头
    private boolean mIsSwitchingCamera = false;
    private ToggleButton btn_swich_camera;
    private NvAssetManager mAssetManager;
    //闪光灯
    private ToggleButton btn_flash;

    //滤镜布局
    private LinearLayout ll_beauty;

    //拍摄
    private TextView btn_shot;
    //拍摄布局
    private LinearLayout function_ll;
    private LinearLayout btn_ratio;
    private LinearLayout btn_filter;
    private LinearLayout btn_sticker;
    private LinearLayout btn_set;
    //当前拍摄视频路径
    private String mCurRecordVideoPath;
    private long mEachRecodingVideoTime = 0, mEachRecodingImageTime = 4000000;
    private long mAllRecordingTime = 0;

    //删除视频
    private TextView btn_del_video;

    private final int MIN_RECORD_DURATION = 1000000;

    //拍摄视频路径集合
    private ArrayList<String> mRecordFileList = new ArrayList<String>();
    //录制的时间集合
    private ArrayList<Long> mRecordTimeList = new ArrayList<Long>();
    //录制视频的速率集合
    //private ArrayList<Float> Tran_speed = new ArrayList<>();

    //录制视频列表
    private VideoShowAdapter videoShowAdapter;

    //底部
    private LinearLayout bottom_view;
    //顶部
    private RelativeLayout rl_top;
    //右边
    private LinearLayout ll_right;

    //贴图
    private RecyclerView stickers_rv;
    private LinearLayout ll_sticker;
    private StickerAdapter stickerAdapter;
    //贴图列表
    private ArrayList m_lstCaptureScene;

    //拍摄最大时长
    private long time = 30 * 1000 * 1000;
    private long copy_time = 30 * 1000 * 1000;
    private TextView tv_time;
    private TextView btn_next;
    private TextView btn_import;
    private TextView tv_count_down;//倒计时时间
    private TextView btn_count_down;//倒计时按钮
    private TextView tv_shot_type;
    private LinearLayout ll_shot_type;
    private TextView tv_shot_dis;
    private Handler mHandler;
    private int shotType;
    private ShotBlockDialog shotBlockDialog;
    private TextView btn_music;
    private MusicInfo musicInfo;
    private MediaPlayerUtil player;
    private LinearLayout ll_speed;

    private SeekBar intensitySeekBar;
    private TextView tv_expot;
    private TextView btn_cut_music;

    private TextView tv_strength;
    private TextView tv_whitening;
    private TextView tv_red;

    private int select_cappos = 0;

    private TextView tv_scenery;
    private TextView tv_beauty;


    private int filterSelect = 1; //当前选中滤镜分类
    //脸贴
    private List<DownResponse.DataBean> mDatas = new ArrayList<>();
    private LinearLayout ll_cut_music;
    private ImageButton btn_cut_music_x;
    private SimpleDraweeView iv_music_image;
    private TextView tv_music_name;
    private TextView tv_music_time_start;
    private TextView tv_music_time_end;
    private MusicHorizontalScrollView music_scroll_bar;
    private MusicWaveView music_wave_view;

    private CircleView cv_shot;
    private ProgressBar pb_sticker;
    private TextView tv_refresh_sticker;

    public static void toThis(Context mContext, int type) {
        Intent intent = new Intent(mContext, ShotActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("shotType", type);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    //视频片段的拍摄参数
    private ArrayList<Float> Tran_speed = new ArrayList<>();
    private ArrayList<Boolean> beauty;
    private ArrayList<Double> strengthValue;
    private ArrayList<Double> whiteningValue;
    private ArrayList<String> filterName;
    private ArrayList<Long> musicTime;

    private TextView tv_video_num;

    private boolean isMove; //是否滑动屏幕
    private int flterPosition = 0;
    private int flterBeautyPosition = 0;
    private TextView tv_flters; //当前滤镜名称
    private TextView tv_flters_type; //当前滤镜类型


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        VideoEffectUtil.init(this.getApplicationContext());
    }


    @Override
    public int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            shotType = bundle.getInt("shotType");
        }

        initNvs();
        EventBus.getDefault().register(this);
        ActivityManager.getInstance().pushOneActivity(this);
        return R.layout.activity_ms_shot_res;
    }

    private void initNvs() {
        mStreamingContext = NvsStreamingContext.getInstance();
    }

    @Override
    public void initView() {
        mLiveWindow = findViewById(R.id.liveWindow);
        /*滤镜dialog*/
        btn_fliters = findViewById(R.id.btn_fliters);
        ll_fliter = findViewById(R.id.ll_fliter);
        fliters_rv = findViewById(R.id.fliters_rv);
        mImageAutoFocusRect = findViewById(R.id.imageAutoFocusRect);
        btn_swich_camera = findViewById(R.id.btn_swich_camera);
        rg_video = findViewById(R.id.rg_video);
        strength = findViewById(R.id.strength);
        mWhitening = findViewById(R.id.mWhitening);
        mReddening = findViewById(R.id.mReddening);
        btn_flash = findViewById(R.id.btn_flash);
        btn_shot = findViewById(R.id.btn_shot);
        cv_shot = findViewById(R.id.cv_shot);
        rv_videos = findViewById(R.id.rv_videos);
        btn_del_video = findViewById(R.id.btn_del_video);
        tv_time = findViewById(R.id.tv_time);
        ll_beauty = findViewById(R.id.ll_beauty);
        function_ll = findViewById(R.id.function_ll);
        btn_cut_music = findViewById(R.id.btn_cut_music);
        tv_flters = findViewById(R.id.tv_flters);
        tv_flters_type = findViewById(R.id.tv_flters_type);

        intensitySeekBar = findViewById(R.id.intensitySeekBar);
        intensitySeekBar.setMax(24);
        intensitySeekBar.setProgress(12);
        tv_expot = findViewById(R.id.tv_expot);

        btn_ratio = findViewById(R.id.btn_ratio);
        btn_filter = findViewById(R.id.btn_filter);
        btn_sticker = findViewById(R.id.btn_sticker);
        btn_set = findViewById(R.id.btn_set);

        btn_ratios = findViewById(R.id.btn_ratios);
        btn_stickers = findViewById(R.id.btn_stickers);
        btn_sets = findViewById(R.id.btn_sets);

        bottom_view = findViewById(R.id.bottom_view);

        rl_top = findViewById(R.id.rl_top);

        ll_right = findViewById(R.id.ll_right);

        //贴图
        ll_sticker = findViewById(R.id.ll_sticker);
        stickers_rv = findViewById(R.id.stickers_rv);
        btn_next = findViewById(R.id.btn_next);
        btn_import = findViewById(R.id.btn_import);
        tv_count_down = findViewById(R.id.tv_count_down);
        btn_count_down = findViewById(R.id.btn_count_down);
        tv_shot_type = findViewById(R.id.tv_shot_type);
        tv_shot_dis = findViewById(R.id.tv_shot_dis);
        ll_shot_type = findViewById(R.id.ll_shot_type);
        btn_music = findViewById(R.id.btn_music);

        ll_speed = findViewById(R.id.ll_speed);
        btn_back = findViewById(R.id.btn_back);


        tv_scenery = findViewById(R.id.tv_scenery);
        tv_beauty = findViewById(R.id.tv_beauty);

        tv_strength = findViewById(R.id.tv_strength);
        tv_whitening = findViewById(R.id.tv_whitening);
        tv_red = findViewById(R.id.tv_red);
        //timer.schedule(task,time,1000);

        //音乐剪切
        ll_cut_music = (LinearLayout) findViewById(R.id.ll_cut_music);
        btn_cut_music_x = (ImageButton) findViewById(R.id.btn_cut_music_x);
        iv_music_image = (SimpleDraweeView) findViewById(R.id.iv_music_image);
        tv_music_name = (TextView) findViewById(R.id.tv_music_name);
        tv_music_time_start = (TextView) findViewById(R.id.tv_music_time_start);
        tv_music_time_end = (TextView) findViewById(R.id.tv_music_time_end);
        music_scroll_bar = (MusicHorizontalScrollView) findViewById(R.id.music_scroll_bar);
        music_wave_view = (MusicWaveView) findViewById(R.id.music_wave_view);

        tv_video_num = findViewById(R.id.tv_video_num);

        pb_sticker = findViewById(R.id.pb_sticker);

        tv_refresh_sticker = findViewById(R.id.tv_refresh_sticker);

        //初始化引导页
        initGuide();

    }

    private void initSelectMusic() {

        musicInfo = TimelineData.instance().getMasterMusic();

        if(musicInfo == null)return;

        tv_music_name.setText(musicInfo.getTitle());
        tv_music_time_end.setText("/00:30");

        if(musicInfo.getId() == Constant.MUSIC_STORE){
            iv_music_image.setImageResource(R.mipmap.default_music);
        }else {
            iv_music_image.setImageURI(Uri.parse(musicInfo.getImagePath()));
        }

        initMusic(musicInfo.getFilePath());
        //puseMusic();

        btn_cut_music.setVisibility(View.VISIBLE);

        musicInfo.setTrimIn(leftTime * 1000);
        musicInfo.setTrimOut(musicInfo.getDuration());
    }

    private void initGuide() {

        TextView tv_guide = findViewById(R.id.tv_guide);

        final GuidePage page = GuidePage
                .newInstance()
                .setLayoutRes(R.layout.info_shot)
                .addHighLight(tv_guide)
                .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                    @Override
                    public void onLayoutInflated(View view, final Controller controller) {
                        view.findViewById(R.id.tv_know).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(controller != null){
                                    controller.remove();
                                }
                            }
                        });
                    }
                });

        NewbieGuide.with(this)
                .setLabel("guideshot")
                .alwaysShow(Constants.GUIDE_TEST)//总是显示，调试时可以打开
                .addGuidePage(page)
                .show();

    }


    @Override
    public void initEvents() {
        btn_filter.setOnClickListener(this);
        btn_ratio.setOnClickListener(this);
        btn_sticker.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        tv_scenery.setOnClickListener(this);
        tv_beauty.setOnClickListener(this);
        btn_cut_music.setOnClickListener(this);

        btn_shot.setOnClickListener(this);
        btn_del_video.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_import.setOnClickListener(this);
        btn_count_down.setOnClickListener(this);
        ll_shot_type.setOnClickListener(this);
        btn_music.setOnClickListener(this);
        tv_refresh_sticker.setOnClickListener(this);

        btn_cut_music_x.setOnClickListener(this);

        cv_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv_shot.startAnim();
            }
        });

        intensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                mStreamingContext.setExposureCompensation(i - 12);//设置曝光补偿
                tv_expot.setText((i - 12) + "");
//                if(b) {
//                    if(mCurCaptureVideoFx != null) {
////                        float value = (float) i / 100;
////                        mCurCaptureVideoFx.setFilterIntensity(value);
//
//
//                    }
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_swich_camera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mIsSwitchingCamera) {
                    return;
                }
                if (isChecked) {
                    mCurrentDeviceIndex = 1;
                    btn_flash.setEnabled(false);
                    btn_flash.setVisibility(View.INVISIBLE);
                } else {
                    mCurrentDeviceIndex = 0;
                    btn_flash.setEnabled(true);
                    btn_flash.setVisibility(View.VISIBLE);
                }
                mIsSwitchingCamera = true;
                startCapturePreview(true);
            }
        });

        btn_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mStreamingContext.toggleFlash(b);
            }
        });

        rg_video.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_video_1) {
                    if (mSpeedMode != moreSlowSpeedMode) {
                        mSpeedMode = moreSlowSpeedMode;
                        speed = 0.5f;
                    }

                } else if (i == R.id.rb_video_2) {
                    if (mSpeedMode != slowSpeedMode) {
                        mSpeedMode = slowSpeedMode;
                        speed = 0.75f;
                    }

                } else if (i == R.id.rb_video_3) {
                    if (mSpeedMode != normalSpeedMode) {
                        mSpeedMode = normalSpeedMode;
                        speed = 1f;
                    }

                } else if (i == R.id.rb_video_4) {
                    if (mSpeedMode != fastSpeedMode) {
                        mSpeedMode = fastSpeedMode;
                        speed = 1.5f;
                    }

                } else if (i == R.id.rb_video_5) {
                    if (mSpeedMode != moreFastSpeedMode) {
                        mSpeedMode = moreFastSpeedMode;
                        speed = 2.0f;
                    }

                }
                LogUtil.showLog("设置speed===="+speed);
            }
        });

        mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
            float downX;
            float moveX;
            float upX;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    downX = event.getX();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    moveX = event.getX();
                } else if (action == MotionEvent.ACTION_UP) {
                    upX = event.getX();
                    if (Math.abs(upX - downX) > 50 || Math.abs(downX - upX) > 50) {
                        //如果录制中，禁止滤镜切换
                        if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
                            return true;
                        }
                        LogUtil.showLog("ssflterBeautyPosition = "+flterBeautyPosition+",flterPosition = "+flterPosition+",filterSelect = "+filterSelect);
                        //按下和松开的值差大于20标识为滑动
                        if(filterSelect == 0){
                            if (moveX < downX) {//向左滑
                                //左滑的条件是否满足
                                if (flterPosition == mFilterItemArrayList.size() - 1) {
                                    flterPosition = 0;
                                    filterSelect = 1;
                                    flterBeautyPosition = 0;
                                    LogUtil.showLog("flterBeautyPosition = "+flterBeautyPosition+",flterPosition = "+flterPosition);
                                    mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
                                    mFiltersAdapter.setSelectPos(flterBeautyPosition);

                                    mFiltersAdapter.notifyDataSetChanged();
                                    tv_scenery.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.a4a4));
                                    tv_beauty.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.bg_white));

                                    tv_flters.setText(mFilterItemBeautyArrayList.get(flterBeautyPosition).getEnglishName());
                                    tv_flters_type.setText(getString(R.string.beauty_ch));

                                    tv_flters.startAnimation(mFitersAnimation);
                                    tv_flters_type.startAnimation(mFitersAnimation);

                                    mFiltersAdapter.callItemClick(flterBeautyPosition);
                                    smoothMoveToPosition(fliters_rv,flterBeautyPosition);
                                    return true;
                                }else{
                                    flterPosition++;
                                }

                            } else {//向右滑
                                if (flterPosition == 1 || flterPosition == 0) {
                                    flterPosition = mFilterItemArrayList.size();
                                    filterSelect = 1;
                                    flterBeautyPosition = mFilterItemBeautyArrayList.size() - 1;

                                    mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
                                    mFiltersAdapter.setSelectPos(flterBeautyPosition);

                                    mFiltersAdapter.notifyDataSetChanged();
                                    tv_scenery.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.a4a4));
                                    tv_beauty.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.bg_white));

                                    tv_flters.setText(mFilterItemBeautyArrayList.get(flterBeautyPosition).getEnglishName());
                                    tv_flters_type.setText(getString(R.string.beauty_ch));

                                    tv_flters.startAnimation(mFitersAnimation);
                                    tv_flters_type.startAnimation(mFitersAnimation);

                                    mFiltersAdapter.callItemClick(flterBeautyPosition);
                                    smoothMoveToPosition(fliters_rv,flterBeautyPosition);
                                    return true;
                                }else {
                                    flterPosition--;
                                }
                            }
                            if (mFilterItemArrayList == null) {
                                return true;
                            }



                            if(getString(R.string.no_info).equals(mFilterItemArrayList.get(flterPosition).getFilterName())){
                                tv_flters.setText(getString(R.string.no_info_eng));
                            }else{
                                tv_flters.setText(mFilterItemArrayList.get(flterPosition).getEnglishName());
                            }
                            tv_flters_type.setText(getString(R.string.scenery));

                            tv_flters.startAnimation(mFitersAnimation);
                            tv_flters_type.startAnimation(mFitersAnimation);

                            mFiltersAdapter.callItemClick(flterPosition);
                            smoothMoveToPosition(fliters_rv,flterPosition);
                        }else{
                            if (moveX < downX) {//向左滑
                                //左滑的条件是否满足
                                if (flterBeautyPosition == mFilterItemBeautyArrayList.size() - 1) {
                                    flterBeautyPosition = 0;
                                    filterSelect = 0;
                                    flterPosition = 1;

                                    mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
                                    mFiltersAdapter.setSelectPos(flterPosition);

                                    mFiltersAdapter.notifyDataSetChanged();
                                    tv_beauty.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.a4a4));
                                    tv_scenery.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.bg_white));


                                    if(getString(R.string.no_info).equals(mFilterItemArrayList.get(flterPosition).getFilterName())){
                                        tv_flters.setText(getString(R.string.no_info_eng));
                                    }else{
                                        tv_flters.setText(mFilterItemArrayList.get(flterPosition).getEnglishName());
                                    }
                                    tv_flters_type.setText(getString(R.string.scenery));

                                    tv_flters.startAnimation(mFitersAnimation);
                                    tv_flters_type.startAnimation(mFitersAnimation);

                                    mFiltersAdapter.callItemClick(flterPosition);
                                    smoothMoveToPosition(fliters_rv,flterPosition);
                                    return true;
                                }else {
                                    flterBeautyPosition++;
                                }

                            } else {//向右滑
                                if (flterBeautyPosition == 0) {
                                    flterBeautyPosition = mFilterItemBeautyArrayList.size();
                                    filterSelect = 0;
                                    flterPosition = mFilterItemArrayList.size() -1;
                                    LogUtil.showLog("优化flterBeautyPosition = "+flterBeautyPosition+",flterPosition = "+flterPosition+",filterSelect = "+filterSelect);
                                    mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
                                    mFiltersAdapter.setSelectPos(flterPosition);

                                    mFiltersAdapter.notifyDataSetChanged();
                                    tv_beauty.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.a4a4));
                                    tv_scenery.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.bg_white));

                                    if(getString(R.string.no_info).equals(mFilterItemArrayList.get(flterPosition).getFilterName())){
                                        tv_flters.setText(getString(R.string.no_info_eng));
                                    }else{
                                        tv_flters.setText(mFilterItemArrayList.get(flterPosition).getEnglishName());
                                    }
                                    tv_flters_type.setText(getString(R.string.scenery));

                                    tv_flters.startAnimation(mFitersAnimation);
                                    tv_flters_type.startAnimation(mFitersAnimation);

                                    mFiltersAdapter.callItemClick(flterPosition);
                                    smoothMoveToPosition(fliters_rv,flterPosition);
                                    return true;
                                }else {
                                    flterBeautyPosition--;
                                }
                            }
                            if (mFilterItemBeautyArrayList == null) {
                                return true;
                            }

                            tv_flters.setText(mFilterItemBeautyArrayList.get(flterBeautyPosition).getEnglishName());
                            tv_flters_type.setText(getString(R.string.beauty_ch));

                            tv_flters.startAnimation(mFitersAnimation);
                            tv_flters_type.startAnimation(mFitersAnimation);

                            mFiltersAdapter.callItemClick(flterBeautyPosition);
                            smoothMoveToPosition(fliters_rv,flterBeautyPosition);
                        }

//                        removeAllFilterFx();
//                        LogUtil.showLog("msg---滤镜下标：" + flterPosition);
//                        String filterPackageId = mFilterItemArrayList.get(flterPosition).getPackageId();
//                        tv_flters.setText(mFilterItemArrayList.get(flterPosition).getFilterName());
//                        mStreamingContext.appendPackagedCaptureVideoFx(filterPackageId);
//
//                        tv_flters.startAnimation(mFocusAnimation);
//                        LogUtil.showLog("msg---ACTION_UP：move");


                    } else {
                        LogUtil.showLog("msg---ACTION_UP：down");
                        float rectHalfWidth = mImageAutoFocusRect.getWidth() / 2;
                        if (event.getX() - rectHalfWidth >= 0 && event.getX() + rectHalfWidth <= mLiveWindow.getWidth()
                                && event.getY() - rectHalfWidth >= 0 && event.getY() + rectHalfWidth <= mLiveWindow.getHeight()) {
                            mImageAutoFocusRect.setX(event.getX() - rectHalfWidth);
                            mImageAutoFocusRect.setY(event.getY() - rectHalfWidth);
                            RectF rectFrame = new RectF();
                            rectFrame.set(mImageAutoFocusRect.getX(), mImageAutoFocusRect.getY(),
                                    mImageAutoFocusRect.getX() + mImageAutoFocusRect.getWidth(),
                                    mImageAutoFocusRect.getY() + mImageAutoFocusRect.getHeight());
                            //启动自动聚焦
                            mImageAutoFocusRect.startAnimation(mFocusAnimation);
                            mStreamingContext.startAutoFocus(new RectF(rectFrame));
                        }

                        if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING || isDown) {
                            //如果正在录制
                            return false;
                        }
                        viewHide();
                    }
                }
                return true;
            }
        });
    }



    @Override
    public void onClickEvent(View v) {
        if (v == btn_filter) { //滤镜

            if (ll_fliter.getVisibility() == View.VISIBLE) {
                viewHide();
                return;
            }

            setRecordStateBackground(RECORD_FILTER);
            ll_fliter.setVisibility(View.VISIBLE);
            ll_beauty.setVisibility(View.GONE);
            ll_sticker.setVisibility(View.GONE);
            ll_speed.setVisibility(View.GONE);
            closeMusicLayout(0);
        } else if (v == btn_ratio) { //美颜

            if (ll_beauty.getVisibility() == View.VISIBLE) {
                viewHide();
                return;
            }

            setRecordStateBackground(RECORD_FILTER);
            ll_fliter.setVisibility(View.GONE);
            ll_beauty.setVisibility(View.VISIBLE);
            ll_sticker.setVisibility(View.GONE);
            ll_speed.setVisibility(View.GONE);
            closeMusicLayout(0);

        } else if (v == btn_sticker) { //贴图

            if (ll_sticker.getVisibility() == View.VISIBLE) {
                viewHide();
                return;
            }

            setRecordStateBackground(RECORD_FILTER);
            ll_fliter.setVisibility(View.GONE);
            ll_beauty.setVisibility(View.GONE);
            ll_sticker.setVisibility(View.VISIBLE);
            ll_speed.setVisibility(View.GONE);
            closeMusicLayout(0);

        } else if (v == btn_set) {  //速度

            if (ll_speed.getVisibility() == View.VISIBLE) {
                viewHide();
                return;
            }

            setRecordStateBackground(RECORD_FILTER);
            ll_fliter.setVisibility(View.GONE);
            ll_beauty.setVisibility(View.GONE);
            ll_sticker.setVisibility(View.GONE);
            ll_speed.setVisibility(View.VISIBLE);
            closeMusicLayout(0);

        } else if (v == btn_shot) {
            startRecord();
        } else if (v == btn_del_video) {
            if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
                //ToastUtils.showShort(ShotActivity.this, getString(R.string.video_delet_tip));
                Toast.makeText(ShotActivity.this, getString(R.string.video_delet_tip), Toast.LENGTH_SHORT).show();
                return;
            }

            if (mRecordTimeList.size() != 0 && mRecordFileList.size() != 0) {

                new AlertDialog.Builder(this)
                        .setTitle(R.string.tip)
                        .setMessage(getString(R.string.delet_last))
                        .setNegativeButton(R.string.cancel_china, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).setPositiveButton(R.string.del, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAllRecordingTime -= mRecordTimeList.get(mRecordTimeList.size() - 1);
                        mRecordTimeList.remove(mRecordTimeList.size() - 1);
                        PathUtils.deleteFile(mRecordFileList.get(mRecordFileList.size() - 1));
                        Tran_speed.remove(mRecordFileList.size() - 1);
                        mRecordFileList.remove(mRecordFileList.size() - 1);
                        videoShowAdapter.setData(mRecordFileList);

                        if (shotType == 1) {
                            time = copy_time - mAllRecordingTime;
                            tv_time.setText(formatTimeStrWithUs(time));
                            tv_video_num.setVisibility(View.INVISIBLE);
                        } else {
                            //分段视频删除
                            time = blockTime;
                            tv_time.setText(formatTimeStrWithUs(time));

                            tv_video_num.setVisibility(View.VISIBLE);
                            String temp = getResources().getString(R.string.video_num);
                            String timeTip = String.format(temp, blockTime * mRecordFileList.size() / (float) 1000 / 1000, mRecordFileList.size());
                            tv_video_num.setText(timeTip);
                        }

                    }
                }).show();
            }

        } else if (v == btn_next) {
            //   toEditView();

            if (mRecordFileList == null || mRecordFileList.size() == 0) {
                Toast.makeText(ShotActivity.this, R.string.tip_next_video, Toast.LENGTH_SHORT).show();
                return;
            }

            VideoEffectUtil.instance().setBeautyBooleanData(beauty);
            VideoEffectUtil.instance().setStrengthDoubleData(strengthValue);
            VideoEffectUtil.instance().setwhiteningDoubleData(whiteningValue);
            VideoEffectUtil.instance().setfilterNameStringData(filterName);
            VideoEffectUtil.instance().setmusicTimeFloatData(musicTime);
            VideoEffectUtil.instance().setspeedFloatData(Tran_speed);

            mStreamingContext.removeAllCaptureVideoFx(); //移除所有采集视频特效



            /*将拍摄的视频传到下一个页面mRecordFileList*/
            ArrayList<ClipInfo> pathList = new ArrayList<>();
            for (int i = 0; i < mRecordFileList.size(); i++) {
                ClipInfo clipInfo = new ClipInfo();
                clipInfo.setFilePath(mRecordFileList.get(i));
                clipInfo.setSpeed(Tran_speed.get(i));
                pathList.add(clipInfo);
            }
            TimelineData.instance().clear();
            NvsVideoResolution videoEditRes = new NvsVideoResolution();
            videoEditRes.imageWidth = Constants.IMAGE_WIDTH;      /*视频分辨率的宽*/
            videoEditRes.imageHeight = Constants.IMAGE_HEIGHT;
            TimelineData.instance().setVideoResolution(videoEditRes);
            TimelineData.instance().setClipInfoData(pathList);
            TimelineData.instance().setMakeRatio(Constants.SCREEN_TYPE);  //设置实用默认比例

//            Bundle bundle = new Bundle();
//            bundle.putBoolean(Constants.START_ACTIVITY_FROM_CAPTURE, true);
            Intent intent = new Intent(mContext, VideoEditActivity.class);
            intent.putStringArrayListExtra("clipsPath", mRecordFileList);
            startActivity(intent);

        } else if (v == btn_import) {

            //存储录制的视频
            ArrayList<ClipInfo> pathList = new ArrayList<>();
            for (int i = 0; i < mRecordFileList.size(); i++) {
                ClipInfo clipInfo = new ClipInfo();
                clipInfo.setFilePath(mRecordFileList.get(i));
                clipInfo.setSpeed(Tran_speed.get(i));
                pathList.add(clipInfo);
            }
            TimelineData.instance().clear();
            NvsVideoResolution videoEditRes = new NvsVideoResolution();
            videoEditRes.imageWidth = Constants.IMAGE_WIDTH;      /*视频分辨率的宽*/
            videoEditRes.imageHeight = Constants.IMAGE_HEIGHT;
            TimelineData.instance().setVideoResolution(videoEditRes);
            TimelineData.instance().setClipInfoData(pathList);
            TimelineData.instance().setMakeRatio(Constants.SCREEN_TYPE);  //设置实用默认比例

            Intent intent = new Intent(mContext, ImportVideoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("visitMethod",Constants.FROMMAINACTIVITYTOVISIT);
            intent.putExtras(bundle);
            startActivity(intent);

        } else if (v == btn_count_down) {

            if (shotType == 1 && time <= 0) {
                Toast.makeText(ShotActivity.this, R.string.tip_cut_videos, Toast.LENGTH_SHORT).show();
                return;
            }

            tv_count_down.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.GONE);
            setRecordStateBackground(RECORD_PLATING);
            mCountDownTimer.start();
        } else if (v == ll_shot_type) {
            if (shotType == 2) {
                if (mRecordFileList != null && mRecordFileList.size() == 0) {
                    shotBlockDialog.show();
                } else {
                    Toast.makeText(ShotActivity.this, R.string.tip_cut_video, Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == btn_music) {
            if (mRecordFileList == null || mRecordFileList.size() == 0) {
                Intent intent = new Intent(mContext, MusicActivity.class);
                intent.putExtra("intype", 1);
                startActivity(intent);
            } else {
                ToastUtils.showShort(mContext, getString(R.string.add_music_tip));
            }

        } else if (v == btn_back) {
            finish();
        } else if (v == tv_scenery) {
            filterSelect = 0;
            flterPosition = 0;
            mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
            mFiltersAdapter.setSelectPos(-1);
            removeAllFilterFx();

            mFiltersAdapter.notifyDataSetChanged();
            tv_beauty.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.a4a4));
            tv_scenery.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.bg_white));
        } else if (v == tv_beauty) {
            filterSelect = 1;
            flterBeautyPosition = 0;
            mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
            mFiltersAdapter.setSelectPos(-1);
            removeAllFilterFx();

            mFiltersAdapter.notifyDataSetChanged();
            tv_scenery.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.a4a4));
            tv_beauty.setTextColor(ContextCompat.getColor(ShotActivity.this, R.color.bg_white));
        } else if (v == btn_cut_music) {
            if (mRecordFileList == null || mRecordFileList.size() == 0) {
                if (musicInfo != null) {
                    ll_cut_music.setVisibility(View.VISIBLE);

                    music_scroll_bar.scrollTo(0, 0);
                    initMusic(musicInfo.getFilePath());
                    playMusic();
                    initCutMusic(musicInfo.getDuration() / 1000);
                }
            } else {
                ToastUtils.showShort(mContext, getString(R.string.edit_music_tip));
            }
        } else if (v == btn_cut_music_x) {
            closeMusicLayout(1);
            musicInfo.setTrimIn(leftTime * 1000);

            musicInfo.setTrimOut(musicInfo.getDuration());
            TimelineData.instance().setMasterMusic(musicInfo);
        }else if(v == tv_refresh_sticker){
            pb_sticker.setVisibility(View.VISIBLE);
            tv_refresh_sticker.setVisibility(View.GONE);
            mPresenter.onDownload(this);
        }
    }

    /**
     * 关闭音乐剪切页面
     * 0 not  1 seek
     */
    private void closeMusicLayout(int type) {
        ll_cut_music.setVisibility(View.GONE);
        if (player != null) {
            if (type == 1) {
                player.seekTo(leftTime);
            }
            player.pause();
        }
    }

    private int mScrollX;
    private int leftTime = 0;

    private void initCutMusic(final long totalTime) {
        final long displayTime = time / 1000;
        music_wave_view.setDisplayTime((int) displayTime);
        music_wave_view.setTotalTime((int) totalTime);
        music_wave_view.layout();


        music_scroll_bar.setScrollViewListener(new MusicHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(HorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                mScrollX = x;
                setDurationTxt((int) displayTime, x, (int) totalTime);

            }

            @Override
            public void onScrollStop() {
                leftTime = (int) ((float) mScrollX / music_wave_view.getMusicLayoutWidth() * totalTime);

                if (player != null) {
                    player.seekTo(leftTime);
                }

            }
        });
    }

    private void setDurationTxt(int displayTime, int x, int duration) {
        int leftTime = (int) ((float) x / music_wave_view.getMusicLayoutWidth() * duration);
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

    @Override
    public void initObject() {
        setMVP();
        shotBlockDialog = new ShotBlockDialog(mContext);
        setShotType();
        initCapture();
        mAssetManager = NvAssetManager.sharedInstance(this);
        mAssetManager.searchLocalAssets(NvAsset.ASSET_FILTER);

        String bundlePath = "filter";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_FILTER, bundlePath);

        bundlePath = "filterbeauty";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_FILTER_BEAUTY, bundlePath);

//        bundlePath = "arface";
//        mAssetManager.searchReservedAssets(NvAsset.ASSET_FACE_BUNDLE_STICKER, bundlePath);

//        mAssetManager.searchLocalAssets(NvAsset.ASSET_FACE1_STICKER);

        mFocusAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFocusAnimation.setDuration(1000);
        mFocusAnimation.setFillAfter(true);
        mFitersAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFitersAnimation.setDuration(1000);
        mFitersAnimation.setFillAfter(true);
        initVideosView();

        initBeautyFilterList();
        initFilterList();

        initFilterView();

        initBeautyData();
        initStickerData();

        beautyListener();

        initTimer();

        //初始化前置摄像
        btn_swich_camera.setChecked(true);
        mCurrentDeviceIndex = 1;
        btn_flash.setEnabled(false);
        btn_flash.setVisibility(View.INVISIBLE);
        mIsSwitchingCamera = true;
        startCapturePreview(true);

        initSelectMusic();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("select_music")) {
            musicInfo = (MusicInfo) messageEvent.getData();

            tv_music_name.setText(musicInfo.getTitle());
            tv_music_time_end.setText("/00:30");

            if(musicInfo.getId() == Constant.MUSIC_STORE){
                iv_music_image.setImageResource(R.mipmap.default_music);
            }else {
                iv_music_image.setImageURI(Uri.parse(musicInfo.getImagePath()));
            }

            initMusic(musicInfo.getFilePath());
            //puseMusic();

            btn_cut_music.setVisibility(View.VISIBLE);

            musicInfo.setTrimIn(leftTime * 1000);
            musicInfo.setTrimOut(musicInfo.getDuration());
            TimelineData.instance().setMasterMusic(musicInfo);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != RESULT_OK)
//            return;
//        if (data == null)
//            return;
//
//        if (requestCode == REQUES_MUSIC) {
//            musicInfo = (MusicInfo)data.getSerializableExtra("select_music");
//            playMusic(musicInfo.getFilePath());
//        }
//
//    }

    /**
     * 播放音乐
     *
     * @param path
     */
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
        }
    }

    private void puseMusic() {
        if (player != null) {
            player.pause();
        }
    }

    /**
     * 停止音乐
     */
    private void stopMusic() {
        AudioPlayer.getInstance(this).destroyPlayer();
        if (player != null) {
            player.stop();
        }
    }

    /**
     * 初始化贴图
     */
    @SuppressWarnings("unchecked")
    private void initStickerData() {
        // 初始化AR Effect，全局只需一次
        NvsAREffect.Init("assets:/model/facedetectmodel.xml", "assets:/model/clnf.mod");

        m_lstCaptureScene = new ArrayList();
        m_lstCaptureScene.add("None");
        //安装素材包
//        installCaptureScene(m_lstCaptureScene);
//
//        //默认选中第一个特效
//        if(m_lstCaptureScene != null && m_lstCaptureScene.size() > 1) {
//            mStreamingContext.applyCaptureScene(m_lstCaptureScene.get(1).toString());
//        }
//
        GridLayoutManager layoutManager = new GridLayoutManager(this,
                2, LinearLayoutManager.HORIZONTAL, false);
        stickers_rv.setLayoutManager(layoutManager);
        stickerAdapter = new StickerAdapter(this);
        stickers_rv.setAdapter(stickerAdapter);

        pb_sticker.setVisibility(View.VISIBLE);
        tv_refresh_sticker.setVisibility(View.GONE);
        mPresenter.onDownload(this);

        stickerAdapter.setOnItemClickListener(new StickerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                select_cappos = position;

                DownResponse.DataBean dataBean = mDatas.get(select_cappos);
                if ("None".equals(dataBean.getName())) {
                    mStreamingContext.removeCurrentCaptureScene();
                    return;
                }

                if (dataBean.isInstall()) {
                    mStreamingContext.applyCaptureScene(dataBean.getThemeId());
                } else {
                    dataBean.setOnLoad(true);
                    stickerAdapter.notifyDataSetChanged();
                    mPresenter.downSdk(dataBean, NvAsset.ASSET_CAPTURE_SCENE);
                }

            }
        });
    }


    private String installSingleCaptureScene(String path, String licpath) {

        StringBuilder themeId = new StringBuilder();
        int error = mStreamingContext.getAssetPackageManager().installAssetPackage(path, licpath,
                NvsAssetPackageManager.ASSET_PACKAGE_TYPE_CAPTURESCENE, true, themeId);
        if (error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR &&
                error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            Log.e("weiwei", "Failed to install package!" + path);
            return null;
        }

        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            error = mStreamingContext.getAssetPackageManager().upgradeAssetPackage(path, licpath,
                    NvsAssetPackageManager.ASSET_PACKAGE_TYPE_CAPTURESCENE, true, themeId);
            if (error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR) {
                Log.e("weiwei", "Failed to upgrade package!" + path);
            }
        }

        return themeId.toString();
    }


    private void initVideosView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_videos.setLayoutManager(linearLayoutManager);
        videoShowAdapter = new VideoShowAdapter(mRecordFileList);
        videoShowAdapter.setHasStableIds(true);
        rv_videos.setAdapter(videoShowAdapter);

        videoShowAdapter.setOnItemClickListener(new VideoShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
                    //ToastUtils.showShort(ShotActivity.this, getString(R.string.video_delet_tip));
                    Toast.makeText(ShotActivity.this, getString(R.string.video_delet_tip), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mRecordTimeList.size() != 0 && mRecordFileList.size() != 0) {

                    new AlertDialog.Builder(ShotActivity.this)
                            .setTitle(R.string.tip)
                            .setMessage(getString(R.string.delet_current))
                            .setNegativeButton(R.string.cancel_china, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).setPositiveButton(R.string.del, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAllRecordingTime -= mRecordTimeList.get(position);
                            mRecordTimeList.remove(position);

                            PathUtils.deleteFile(mRecordFileList.get(position));
                            mRecordFileList.remove(position);
                            videoShowAdapter.setData(mRecordFileList);

                            Tran_speed.remove(position);

                            if (shotType == 1) {
                                time = copy_time - mAllRecordingTime;
                                tv_time.setText(formatTimeStrWithUs(time));
                            } else {
                                time = blockTime;
                                tv_time.setText(formatTimeStrWithUs(time));

                                tv_video_num.setVisibility(View.VISIBLE);
                                String temp = getResources().getString(R.string.video_num);
                                String timeTip = String.format(temp, blockTime * mRecordFileList.size() / (float) 1000 / 1000, mRecordFileList.size());
                                tv_video_num.setText(timeTip);
                            }
                        }
                    }).show();

                }
            }
        });


    }


    private void setData() throws IOException {

        //startTimer();
        Tran_speed = new ArrayList<Float>();
        Log.e("weiwei", "chushihua");

        beauty = new ArrayList<Boolean>();
        strengthValue = new ArrayList<Double>();
        whiteningValue = new ArrayList<Double>();
        filterName = new ArrayList<String>();
        musicTime = new ArrayList<Long>();
    }


    /**
     * 开始录制
     */
    private void startRecord() {
        // 当前在录制状态，可停止视频录制

        if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {

            //如果分段拍摄，则不能手动停止
            if (shotType == 2) {
                return;
            }

            stopRecording();


        } else {
            if (shotType == 2 && mRecordFileList != null && mRecordFileList.size() == videoCount) {
                Toast.makeText(ShotActivity.this, R.string.tip_over_fradom, Toast.LENGTH_SHORT).show();
                return;
            }


            //TODO:录制时间达到30s
            if (shotType == 1 && time <= 0) {

                Toast.makeText(ShotActivity.this, R.string.tip_cut_videos, Toast.LENGTH_SHORT).show();
                return;
            }

            mCurRecordVideoPath = PathUtils.getRecordVideoPath();
            LogUtil.showLog("msg----mCurRecordVideoPath:" + mCurRecordVideoPath);
            if (mCapability == null) {
                return;
            }
            btn_shot.setEnabled(false);
            btn_shot.setBackgroundResource(R.mipmap.icon_shot_stop);

            mEachRecodingVideoTime = 0;
            //当前未在视频录制状态，则启动视频录制。此处使用带特效的录制方式
            if (musicInfo != null) { //如果播放音乐，启用无声录制
                if (!mStreamingContext.startRecording(mCurRecordVideoPath, NvsStreamingContext.STREAMING_ENGINE_RECORDING_FLAG_ONLY_RECORD_VIDEO)) {
                    return;
                }

            } else {
                if (!mStreamingContext.startRecording(mCurRecordVideoPath))
                    return;
            }
            // isInRecording(false);
            mRecordFileList.add(mCurRecordVideoPath);


            boolean isBeautyOn = true;
            Tran_speed.add(speed);
            LogUtil.showLog("add speed===="+speed);
            beauty.add(isBeautyOn);
            if (isBeautyOn) {
                strengthValue.add(mStrengthValue);
                whiteningValue.add(mWhiteningValue);
            } else {
                strengthValue.add((double) 0);
                whiteningValue.add((double) 0);
            }
            filterName.add(m_currentFxName);
            musicTime.add(inTime);

            ArrayList<ClipInfo> pathList = new ArrayList<>();
            for (int i = 0; i < mRecordFileList.size(); i++) {
                ClipInfo clipInfo = new ClipInfo();
                clipInfo.setFilePath(mRecordFileList.get(i));
                clipInfo.setSpeed(Tran_speed.get(i));
                pathList.add(clipInfo);
            }
            TimelineData.instance().setClipInfoData(pathList);

        }
    }


    /**
     * 结束录制
     */
    private void stopRecording() {
        mStreamingContext.stopRecording();
        btn_shot.setBackgroundResource(R.mipmap.icon_shot);

        // 拍视频

        mAllRecordingTime += mEachRecodingVideoTime;
        mRecordTimeList.add(mEachRecodingVideoTime);

        videoShowAdapter.setData(mRecordFileList);

        //TODO:处理录制时间刚好达到30s的情况
        if (mAllRecordingTime >= copy_time) {
            Log.e("weiwei", "结束");
            time = 0;
        } else {
            if (shotType == 1) {
                time = copy_time - mAllRecordingTime;
            } else {
                time = blockTime;
            }

        }
        //  mStartText.setText(mRecordTimeList.size() + "");
        // isInRecording(true);
    }

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
     * 初始化滤镜数据
     */
    private void initFilterList() {
//        int[] resImags = {
//                R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo,
//                R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo,
//                R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo
//        };

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

//            if ("粉嫩".equals(newFilterItem.getFilterName())) {
//                mFilterItemBeautyArrayList.add(newFilterItem);
//            } else if ("情书".equals(newFilterItem.getFilterName())) {
//                mFilterItemBeautyArrayList.add(newFilterItem);
//            } else if ("美颜白皙".equals(newFilterItem.getFilterName())) {
//                mFilterItemBeautyArrayList.add(newFilterItem);
//            } else if ("冰淇淋".equals(newFilterItem.getFilterName())) {
//                mFilterItemBeautyArrayList.add(newFilterItem);
//                mFilterItemArrayList.add(newFilterItem);
//            } else if ("暖茶".equals(newFilterItem.getFilterName())) {
//                mFilterItemBeautyArrayList.add(newFilterItem);
//                mFilterItemArrayList.add(newFilterItem);
//            } else {
//                mFilterItemArrayList.add(newFilterItem);
//            }
        }

//        List<String> builtinFilterList = mStreamingContext.getAllBuiltinCaptureVideoFxNames();
//        for (int i = 0; i < builtinFilterList.size(); i++) {
//            String filterName = builtinFilterList.get(i);
//            FilterItem newFilterItem = new FilterItem();
//            newFilterItem.setFilterName(filterName);
//            if (i < resImags.length) {
//                int imageId = resImags[i];
//                newFilterItem.setImageId(imageId);
//            }
//            newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
//            mFilterItemArrayList.add(newFilterItem);
//        }
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

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }


    /**
     * 初始化滤镜
     */
    private void initFilterView() {
        mFiltersAdapter = new FiltersAdapter(mContext);
        mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        fliters_rv.setLayoutManager(linearLayoutManager);
        fliters_rv.setAdapter(mFiltersAdapter);
        fliters_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll&& RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(fliters_rv, mToPosition);
                }
            }
        });


        mFiltersAdapter.setOnItemClickListener(new FiltersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FilterItem filterItem = null;
                if (filterSelect == 0) {
                    if (position < mFilterItemArrayList.size()) {
                        removeAllFilterFx();
                        flterPosition = position;
                        if (position == 0) {
                            return;
                        }

                        filterItem = mFilterItemArrayList.get(position);
                        int filterMode = filterItem.getFilterMode();
                        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                            String filterName = filterItem.getFilterName();
                            if (!TextUtils.isEmpty(filterName)) {
                                mCurCaptureVideoFx = mStreamingContext.appendBuiltinCaptureVideoFx(filterName);
                            }
                        } else {
                            String filterPackageId = filterItem.getPackageId();
                            if (!TextUtils.isEmpty(filterPackageId)) {
                                mCurCaptureVideoFx = mStreamingContext.appendPackagedCaptureVideoFx(filterPackageId);
                            }
                        }
                    }
                } else {
                    if (position < mFilterItemBeautyArrayList.size()) {
                        removeAllFilterFx();
                        flterBeautyPosition = position;
                        if (position == 0) {
                            return;
                        }

                        filterItem = mFilterItemBeautyArrayList.get(position);
                        int filterMode = filterItem.getFilterMode();
                        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                            String filterName = filterItem.getFilterName();
                            if (!TextUtils.isEmpty(filterName)) {
                                mCurCaptureVideoFx = mStreamingContext.appendBuiltinCaptureVideoFx(filterName);
                            }
                        } else {
                            String filterPackageId = filterItem.getPackageId();
                            if (!TextUtils.isEmpty(filterPackageId)) {
                                mCurCaptureVideoFx = mStreamingContext.appendPackagedCaptureVideoFx(filterPackageId);
                            }
                        }
                    }
                }


                int exposure = 0;
                if (getString(R.string.no_info).equals(filterItem.getFilterName())) {
                    exposure = 0;
                    mStrengthValue = 0;
                    mWhiteningValue = 0;
                    mReddeningValue = 0;
                } else if ("晨曦".equals(filterItem.getFilterName())) {
                    if (filterSelect == 0) {
                        exposure = -5;
                        mStrengthValue = 80;
                        mWhiteningValue = 0;
                        mReddeningValue = 0;
                    } else {
                        exposure = 0;
                        mStrengthValue = 80;
                        mWhiteningValue = 0;
                        mReddeningValue = 0;
                    }
                } else if ("冰淇淋".equals(filterItem.getFilterName())) {
                    if (filterSelect == 0) {
                        exposure = 0;
                        mStrengthValue = 0.8;
                        mWhiteningValue = 0;
                        mReddeningValue = 0;
                    } else {
                        exposure = -4;
                        mStrengthValue = 0.32;
                        mWhiteningValue = 0.16;
                        mReddeningValue = 0.11;
                    }
                } else if ("暖茶".equals(filterItem.getFilterName())) {
                    if (filterSelect == 0) {
                        exposure = 0;
                        mStrengthValue = 0.55;
                        mWhiteningValue = 0.1;
                        mReddeningValue = 0.35;
                    } else {
                        exposure = 2;
                        mStrengthValue = 0.5;
                        mWhiteningValue = 0.4;
                        mReddeningValue = 0.1;
                    }
                } else if ("盛夏光年".equals(filterItem.getFilterName())) {
                    exposure = 0;
                    mStrengthValue = 0;
                    mWhiteningValue = 0;
                    mReddeningValue = 0;
                } else if ("紫霞".equals(filterItem.getFilterName())) {
                    exposure = 0;
                    mStrengthValue = 0.5;
                    mWhiteningValue = 0.28;
                    mReddeningValue = 0.26;
                } else if ("霓虹".equals(filterItem.getFilterName())) {
                    exposure = 0;
                    mStrengthValue = 0;
                    mWhiteningValue = 0.2;
                    mReddeningValue = 0;
                } else if ("甜美".equals(filterItem.getFilterName())) {
                    exposure = -6;
                    mStrengthValue = 1;
                    mWhiteningValue = 1;
                    mReddeningValue = 1;
                } else if ("花儿与少年".equals(filterItem.getFilterName())) {
                    exposure = -6;
                    mStrengthValue = 1;
                    mWhiteningValue = 1;
                    mReddeningValue = 1;
                } else if ("蓝调".equals(filterItem.getFilterName())) {
                    exposure = -1;
                    mStrengthValue = 0.5;
                    mWhiteningValue = 0.2;
                    mReddeningValue = 0.2;
                } else if ("粉嫩".equals(filterItem.getFilterName())) {
                    exposure = -6;
                    mStrengthValue = 0.65;
                    mWhiteningValue = 0.38;
                    mReddeningValue = 0;
                } else if ("情书".equals(filterItem.getFilterName())) {
                    exposure = 0;
                    mStrengthValue = 0.5;
                    mWhiteningValue = 0.5;
                    mReddeningValue = 0.3;
                } else if ("美颜白皙".equals(filterItem.getFilterName())) {
                    exposure = -6;
                    mStrengthValue = 0.35;
                    mWhiteningValue = 0;
                    mReddeningValue = 0;
                } else if ("取景器".equals(filterItem.getFilterName())) {
                    exposure = -1;
                    mStrengthValue = 1;
                    mWhiteningValue = 0.5;
                    mReddeningValue = 0.5;
                } else if ("纯纯".equals(filterItem.getFilterName())) {
                    exposure = 0;
                    mStrengthValue = 0.58;
                    mWhiteningValue = 0.46;
                    mReddeningValue = 0.38;
                } else if ("筑地 LUT".equals(filterItem.getFilterName())) {
                    exposure = 1;
                    mStrengthValue = 0;
                    mWhiteningValue = 0;
                    mReddeningValue = 0;
                } else if ("时尚".equals(filterItem.getFilterName())) {
                    exposure = 2;
                    mStrengthValue = 0;
                    mWhiteningValue = 0;
                    mReddeningValue = 0;
                } else if ("经典".equals(filterItem.getFilterName())) {
                    exposure = -2;
                    mStrengthValue = 0;
                    mWhiteningValue = 0;
                    mReddeningValue = 0;
                } else if ("青涩".equals(filterItem.getFilterName())) {
                    exposure = 3;
                    mStrengthValue = 0;
                    mWhiteningValue = 0;
                    mReddeningValue = 0;
                } else if ("秋日".equals(filterItem.getFilterName())) {
                    exposure = -2;
                    mStrengthValue = 0;
                    mWhiteningValue = 0;
                    mReddeningValue = 0;
                }

                for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
                    NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
                    String name = fx.getBuiltinCaptureVideoFxName();
                    if (name.equals("Beauty")) {
                        //设置美颜强度值
                        fx.setFloatVal("Strength", mStrengthValue);
                        fx.setFloatVal("Whitening", mWhiteningValue);
                        fx.setFloatVal("Reddening", mReddeningValue);
                        continue;
                    }
                }

                //设置曝光度
                mStreamingContext.setExposureCompensation(exposure);

                strength.setProgress((int) (mStrengthValue * 100));
                mWhitening.setProgress((int) (mWhiteningValue * 100));
                mReddening.setProgress((int) (mReddeningValue * 100));

                tv_strength.setText((int) (mStrengthValue * 100) + "");
                tv_whitening.setText((int) (mWhiteningValue * 100) + "");
                tv_red.setText((int) (mReddeningValue * 100) + "");

                mCurCaptureVideoFx.setFilterIntensity(1.0f);
                intensitySeekBar.setProgress(exposure + 12);
                tv_expot.setText(exposure + "");

            }

            @Override
            public void onSameItemClick() {

            }
        });
    }

    private void initBeautyData() {
        mStrengthValue = 0;
        mWhiteningValue = 0;
        mReddeningValue = 0;

        //默认开启美颜
        NvsCaptureVideoFx fx = mStreamingContext.appendBeautyCaptureVideoFx();   //添加美颜采集特效
        fx.setFloatVal("Strength", mStrengthValue);//设置美颜强度值
        fx.setFloatVal("Whitening", mWhiteningValue);
        fx.setFloatVal("Reddening", mReddeningValue);

        //设置美颜数据
        NvsFxDescription fxDescription = mStreamingContext.getVideoFxDescription("Beauty");
        List<NvsFxDescription.ParamInfoObject> paramInfo = fxDescription.getAllParamsInfo();
        for (NvsFxDescription.ParamInfoObject param : paramInfo) {
            String paramName = param.getString("paramName");
            if (paramName.equals("Strength")) {
                double maxValue = param.getFloat("floatMaxVal");
                mStrengthValue = param.getFloat("floatDefVal");
                Log.e("mStrengthValue=", mStrengthValue + "");
                //  mStrength.setMax((int) (maxValue * 100));
                strength.setProgress((int) (mStrengthValue * 100));
                tv_strength.setText((int) (mStrengthValue * 100) + "");

                //  mStrength_text.setText(String.format(Locale.getDefault(), "%.2f", mStrengthValue));
            } else if (paramName.equals("Whitening")) {
                double maxValue = param.getFloat("floatMaxVal");
                mWhiteningValue = param.getFloat("floatDefVal");

              /*  mWhitening.setMax((int) (maxValue * 100));
                mWhitening.setProgress((int) (mWhiteningValue * 100));
                mWhitening_text.setText(String.format(Locale.getDefault(), "%.2f", mWhiteningValue));*/
                mWhitening.setProgress((int) (mWhiteningValue * 100));
                tv_whitening.setText((int) (mWhiteningValue * 100) + "");

            } else if (paramName.equals("Reddening")) {
                double maxValue = param.getFloat("floatMaxVal");
                mReddeningValue = param.getFloat("floatDefVal");
                Log.e("mReddeningValue=", mReddeningValue + "");
              /*  mReddening.setMax((int) (maxValue * 100));
                mReddening.setProgress((int) (mReddeningValue * 100));
                mReddening_text.setText(String.format(Locale.getDefault(), "%.2f", mReddeningValue));*/
                mReddening.setProgress((int) (mReddeningValue * 100));
                tv_red.setText((int) (mReddeningValue * 100) + "");
            }
        }
    }

    /**
     * 美颜动作监听
     */
    private void beautyListener() {

        //磨皮
        strength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_strength.setText(progress + "");

                mStrengthValue = progress * 0.01;
                for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
                    NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
                    String name = fx.getBuiltinCaptureVideoFxName();
                    if (name.equals("Beauty")) {
                        //设置美颜强度值
                        fx.setFloatVal("Strength", mStrengthValue);
                        break;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //美白
        mWhitening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                tv_whitening.setText(progress + "");

                mWhiteningValue = progress * 0.01;

                for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
                    NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
                    String name = fx.getBuiltinCaptureVideoFxName();
                    if (name.equals("Beauty")) {
                        //设置美颜强度值
                        fx.setFloatVal("Whitening", mWhiteningValue);
                        break;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        /*红润*/
        mReddening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tv_red.setText(progress + "");

                mReddeningValue = progress * 0.01;
                for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
                    NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
                    String name = fx.getBuiltinCaptureVideoFxName();
                    if (name.equals("Beauty")) {
                        //设置美颜强度值
                        fx.setFloatVal("Reddening", mReddeningValue);
                        break;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化预览
     */
    private void initCapture() {
        if (null == mStreamingContext) {
            return;
        }
        //给Streaming Context设置采集设备回调接口
        mStreamingContext.setCaptureDeviceCallback(this);
        mStreamingContext.setCaptureRecordingDurationCallback(this);
        mStreamingContext.setCaptureRecordingStartedCallback(this);
        if (mStreamingContext.getCaptureDeviceCount() == 0) {
            return;
        }

        // 将采集预览输出连接到LiveWindow控件
        if (!mStreamingContext.connectCapturePreviewWithLiveWindowExt(mLiveWindow)) {
            LogUtil.showLog("Failed to connect capture preview with livewindow!");
            return;
        }
        mCurrentDeviceIndex = 0;
        //采集设备数量判定
        if (mStreamingContext.getCaptureDeviceCount() > 1) {
            //  mSwitchFacingLayout.setEnabled(true);
        }
        mPermissionGranted = true;
        startCapturePreview(false);
        mStreamingContext.removeAllCaptureVideoFx();
    }

    private boolean startCapturePreview(boolean deviceChanged) {
        // 判断当前引擎状态是否为采集预览状态
        int captureResolutionGrade = ParameterSettingValues.instance().getCaptureResolutionGrade();
        if (mPermissionGranted && (deviceChanged || getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW)) {
            if (!mStreamingContext.startCapturePreview(mCurrentDeviceIndex,
                    captureResolutionGrade,
                    NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER |
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_CAPTURE_BUDDY_HOST_VIDEO_FRAME
                            | NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_STRICT_PREVIEW_VIDEO_SIZE, null)) {
                LogUtil.showLog("Failed to start capture preview!");
                return false;
            }
        }
        return true;
    }

    // 获取当前引擎状态
    private int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }


    private void updateSettingsWithCapability(int deviceIndex) {
        //获取采集设备能力描述对象，设置自动聚焦，曝光补偿，缩放
        mCapability = mStreamingContext.getCaptureDeviceCapability(deviceIndex);
        if (null == mCapability) {
            return;
        }

        //是否支持闪光灯
        if (mCapability.supportFlash) {
            //   mFlashLayout.setEnabled(true);
        }

//        mImageAutoFocusRect.setX((mLiveWindow.getWidth() - mImageAutoFocusRect.getWidth()) / 2);
//        mImageAutoFocusRect.setY((mLiveWindow.getHeight() - mImageAutoFocusRect.getHeight()) / 2);
//        RectF rectFrame = new RectF();
//        rectFrame.set(mImageAutoFocusRect.getX(), mImageAutoFocusRect.getY(),
//                mImageAutoFocusRect.getX() + mImageAutoFocusRect.getWidth(),
//                mImageAutoFocusRect.getY() + mImageAutoFocusRect.getHeight());

        //是否支持自动聚焦
//        if (mCapability.supportAutoFocus) {
//            mStreamingContext.startAutoFocus(new RectF(rectFrame));
//        }

        // 是否支持缩放
//        if (mCapability.supportZoom) {
//            mZoomValue = mCapability.maxZoom;
//            mZoomSeekbar.setMax(mZoomValue);
//            mZoomSeekbar.setProgress(mStreamingContext.getZoom());
//            mZoomSeekbar.setEnabled(true);
//        } else {
//            Log.e(TAG, "该设备不支持缩放");
//        }

        // 是否支持曝光补偿
//        if (mCapability.supportExposureCompensation) {
//            mMinExpose = mCapability.minExposureCompensation;
//            mExposeSeekbar.setMax(mCapability.maxExposureCompensation - mMinExpose);
//            mExposeSeekbar.setProgress(mStreamingContext.getExposureCompensation() - mMinExpose);
//            mExposeSeekbar.setEnabled(true);
//        }
    }

    @Override
    public void onCaptureDeviceCapsReady(int captureDeviceIndex) {
        if (captureDeviceIndex != mCurrentDeviceIndex) {
            return;
        }
        updateSettingsWithCapability(captureDeviceIndex);
        LogUtil.showLog("聚焦");
    }

    @Override
    public void onCaptureDevicePreviewResolutionReady(int i) {

    }

    @Override
    public void onCaptureDevicePreviewStarted(int i) {
        mIsSwitchingCamera = false;
    }

    @Override
    public void onCaptureDeviceError(int i, int i1) {

    }

    @Override
    public void onCaptureDeviceStopped(int i) {

    }

    @Override
    public void onCaptureDeviceAutoFocusComplete(int i, boolean b) {

    }

    @Override
    public void onCaptureRecordingFinished(int i) {
        setRecordStateBackground(RECORD_STOP);
        if (shotType == 1) {
            tv_video_num.setVisibility(View.INVISIBLE);
        } else {
            tv_video_num.setVisibility(View.VISIBLE);
            String temp = getResources().getString(R.string.video_num);
            String timeTip = String.format(temp, blockTime * mRecordFileList.size() / (float) 1000 / 1000, mRecordFileList.size());
            tv_video_num.setText(timeTip);
        }
        Log.e("weiwei", "结束录制");
        puseMusic(); //录制结束，停止音乐
        // 保存到媒体库
        if (mRecordFileList != null && !mRecordFileList.isEmpty()) {
            for (String path : mRecordFileList) {
                if (path == null) {
                    continue;
                }
                if (path.endsWith(".mp4")) {
                    MediaScannerUtil.scanFile(path, "video/mp4");
                }
            }
            videoShowAdapter.setData(mRecordFileList);
        }

    }

    @Override
    public void onCaptureRecordingError(int i) {
        //tv_time.setText(mAllRecordingTime+"");
    }

    @Override
    public void onCaptureRecordingDuration(int i, long l) {
        // 拍视频or拍照片
        if (l >= MIN_RECORD_DURATION) {
            btn_shot.setEnabled(true);
        }
        mEachRecodingVideoTime = l;


        if (time - mEachRecodingVideoTime <= 0) {
            time = 0;
            tv_time.setText(formatTimeStrWithUs(time));
            stopRecording();
            //TODO:录制时间达到30s
            //分段模式录制完毕处理
            if (videoCount == videoShowAdapter.getItemCount()) {
                time = 0;
                toEditView();
            } else if (shotType == 1) {
                toEditView();
            }


        } else {
            tv_time.setText(formatTimeStrWithUs(time - mEachRecodingVideoTime));
        }
        //  mRecordTime.setVisibility(View.VISIBLE);
        // mRecordTime.setText(TimeFormatUtil.formatUsToString2(mAllRecordingTime + mEachRecodingVideoTime));
    }

    @Override
    public void onCaptureRecordingStarted(int i) {
        tv_time.setText(formatTimeStrWithUs(time));
        setRecordStateBackground(RECORD_PLATING);
        if (mRecordFileList != null && mRecordFileList.size() == 1) {
            initMusic(musicInfo.getFilePath());
            player.seekTo(leftTime);
        }
        playMusic();

    }


    private void viewHide() {
        ll_fliter.setVisibility(View.GONE);
        ll_beauty.setVisibility(View.GONE);
        closeMusicLayout(0);
        setRecordStateBackground(RECORD_STOP);
    }

    private void removeAllFilterFx() {
        List<Integer> remove_list = new ArrayList<>();
        for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
            NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
            if (fx == null)
                continue;

            String name = fx.getBuiltinCaptureVideoFxName();
            if (name != null && !name.equals("Beauty") && !name.equals("Face Effect")) {
                remove_list.add(i);
            }
        }
        if (!remove_list.isEmpty()) {
            for (int i = 0; i < remove_list.size(); i++) {
                mStreamingContext.removeCaptureVideoFx(remove_list.get(i));
            }
        }
        //设置曝光度
        mStreamingContext.setExposureCompensation(0);
        strength.setProgress(0);
        mWhitening.setProgress(0);
        mReddening.setProgress(0);

        tv_strength.setText(0 + "");
        tv_whitening.setText(0 + "");
        tv_red.setText(0 + "");

        intensitySeekBar.setProgress(12);
        tv_expot.setText("0");
    }

    private static final int RECORD_PLATING = 1;   //录制状态
    private static final int RECORD_STOP = 2;      //录制结束
    private static final int RECORD_FILTER = 3;    //点击滤镜

    private void setRecordStateBackground(int status) {
        switch (status) {
            case RECORD_PLATING:
                ll_beauty.setVisibility(View.GONE);
                ll_fliter.setVisibility(View.GONE);
                ll_sticker.setVisibility(View.GONE);
                ll_speed.setVisibility(View.GONE);
                closeMusicLayout(0);

                tv_time.setVisibility(View.VISIBLE);
                function_ll.setBackgroundColor(ContextCompat.getColor(this, R.color.white_0));
                btn_ratio.setVisibility(View.GONE);
                btn_filter.setVisibility(View.GONE);
                btn_sticker.setVisibility(View.GONE);
                btn_set.setVisibility(View.GONE);

                rl_top.setVisibility(View.GONE);
                ll_right.setVisibility(View.GONE);

                bottom_view.setVisibility(View.INVISIBLE);
                break;

            case RECORD_STOP:
                ll_beauty.setVisibility(View.GONE);
                ll_fliter.setVisibility(View.GONE);
                ll_sticker.setVisibility(View.GONE);
                ll_speed.setVisibility(View.GONE);
                closeMusicLayout(0);


                tv_time.setVisibility(View.GONE);
                function_ll.setBackgroundColor(ContextCompat.getColor(this, R.color.white_0));
                btn_ratio.setVisibility(View.VISIBLE);
                btn_filter.setVisibility(View.VISIBLE);
                btn_sticker.setVisibility(View.VISIBLE);
                btn_set.setVisibility(View.VISIBLE);

                rl_top.setVisibility(View.VISIBLE);
                ll_right.setVisibility(View.VISIBLE);

                bottom_view.setVisibility(View.VISIBLE);
                bottom_view.setBackgroundColor(ContextCompat.getColor(this, R.color.white_0));
                break;
            case RECORD_FILTER:
                tv_time.setVisibility(View.GONE);
                function_ll.setBackgroundColor(ContextCompat.getColor(this, R.color.c50000000));
                btn_ratio.setVisibility(View.VISIBLE);
                btn_filter.setVisibility(View.VISIBLE);
                btn_sticker.setVisibility(View.VISIBLE);
                btn_set.setVisibility(View.VISIBLE);

                rl_top.setVisibility(View.VISIBLE);
                ll_right.setVisibility(View.VISIBLE);

                bottom_view.setVisibility(View.VISIBLE);
                bottom_view.setBackgroundColor(ContextCompat.getColor(this, R.color.c50000000));
                break;

        }
    }

    CountDownTimer mCountDownTimer;

    private void initTimer() {
        mCountDownTimer = new CountDownTimer(4000 + 50, 1000) {
            @Override
            public void onTick(long l) {
                if ((l / 1000) - 1 == 0) {
                    isDown = false;
                    tv_count_down.setVisibility(View.GONE);
                    btn_shot.setVisibility(View.VISIBLE);
                    tv_time.setVisibility(View.VISIBLE);
                    startRecord();
                } else {
                    isDown = true;
                    tv_time.setVisibility(View.GONE);
                    tv_count_down.setText((int) (l / 1000 - 1) + "");
                    btn_shot.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFinish() {
                isDown = false;
                tv_count_down.setVisibility(View.GONE);
                btn_shot.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
            }
        };

    }


    int videoCount = 4;
    long blockTime = (10 * 1000 * 1000) / 4;

    private void setShotType() {
        if (shotType == 2) { //分段拍摄
            time = (10 * 1000 * 1000) / 4;
            shotBlockDialog.show();
            tv_shot_type.setText(getResources().getString(R.string.shot_dis4));
            tv_shot_dis.setText(getResources().getString(R.string.shot_block_dis));
            shotBlockDialog.setmBlockCountLisenter(new ShotBlockDialog.setBlockCountLisenter() {
                @Override
                public void setVideoCount(long time, int count) {
                    tv_shot_dis.setText(time + "S · " + count + "Divides");
                    long mTime = (time * 1000 * 1000) / count; //单段时长
                    ShotActivity.this.time = mTime;
                    ShotActivity.this.blockTime = mTime;
                    ShotActivity.this.videoCount = count;

                }
            });
            btn_music.setVisibility(View.GONE);
            btn_cut_music.setVisibility(View.GONE);
            btn_count_down.setVisibility(View.GONE);
        } else {
            //自由拍摄
            btn_music.setVisibility(View.VISIBLE);
            btn_count_down.setVisibility(View.VISIBLE);
            btn_cut_music.setVisibility(View.GONE);
            tv_shot_type.setText(getResources().getString(R.string.shot_dis2));
            tv_shot_dis.setText(getResources().getString(R.string.shot_free_dis));
        }
    }


    /**
     * 跳转编辑界面
     */
    private void toEditView() {
        /*将拍摄的视频传到下一个页面mRecordFileList*/
        ArrayList<ClipInfo> pathList = new ArrayList<>();
        for (int i = 0; i < mRecordFileList.size(); i++) {
            ClipInfo clipInfo = new ClipInfo();
            clipInfo.setFilePath(mRecordFileList.get(i));
            clipInfo.setSpeed(Tran_speed.get(i));
            pathList.add(clipInfo);
        }
        TimelineData.instance().clear();
        NvsVideoResolution videoEditRes = new NvsVideoResolution();
        videoEditRes.imageWidth = Constants.IMAGE_WIDTH;      /*视频分辨率的宽*/
        videoEditRes.imageHeight = Constants.IMAGE_HEIGHT;
        TimelineData.instance().setVideoResolution(videoEditRes);
        TimelineData.instance().setClipInfoData(pathList);
        TimelineData.instance().setMakeRatio(Constants.SCREEN_TYPE);  //设置实用默认比例

//            Bundle bundle = new Bundle();
//            bundle.putBoolean(Constants.START_ACTIVITY_FROM_CAPTURE, true);
        Intent intent = new Intent(mContext, VideoEditActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStreamingContext.removeCurrentCaptureScene();
        if (mStreamingContext != null) {
            mStreamingContext.stop();
        }
        stopMusic();
        clearData();
        EventBus.getDefault().unregister(this);
    }

    private void clearData() {
        TimelineManager.getInstance().clear();
        TimelineData.instance().clear();
        TimelineData.instance().setMasterMusic(null);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
            stopRecording();
        }
        puseMusic();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("ShotActivity");
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCapturePreview(false);
        tv_flters.startAnimation(mFitersAnimation);
        tv_flters_type.startAnimation(mFitersAnimation);

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("ShotActivity");
            MobclickAgent.onResume(this);
        }
    }


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

    @Override
    public void setSDKCategroy(List<CategoryResponse.DataBean> mDatas) {

    }

    @Override
    public void setSdkDown(List<DownResponse.DataBean> mDatas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }

        final String dir = PathUtils.getAssetDownloadPath(NvAsset.ASSET_CAPTURE_SCENE);
        //检查文件是否下载解压
        for (DownResponse.DataBean dataBean : mDatas) {

            if (TextUtils.isEmpty(dataBean.getF_id())) {
                dataBean.setDown(false);
                continue;
            }
            final String checkPath = dir + File.separator + dataBean.getF_id();
            dataBean.setDown(PathUtils.fileIsExists(checkPath));
        }

        DownResponse.DataBean dataBean = new DownResponse.DataBean();
        dataBean.setName("None");
        mDatas.add(0, dataBean);

        this.mDatas = mDatas;

        pb_sticker.setVisibility(View.GONE);
        tv_refresh_sticker.setVisibility(View.GONE);
        stickerAdapter.setDatas(mDatas);
    }

    @Override
    public DownRequest getDownRequest() {
        DownRequest downRequest = new DownRequest();
        downRequest.setType(Constants.TYPE_CAPTURESCENE);
//        downRequest.setPage(0);
//        downRequest.setPage_size(0);
        return downRequest;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            stickerAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 下载解压缩完毕了
     *
     * @param dataBean
     */
    @Override
    public void onDownLoadFinish(DownResponse.DataBean dataBean) {

        DownResponse.DataBean selectData = mDatas.get(select_cappos);
        selectData.setDown(dataBean.isDown());
        if (selectData.isInstall()) {
            mStreamingContext.applyCaptureScene(selectData.getThemeId());

            stickerAdapter.notifyItemChanged(select_cappos);
        } else {
            //安装素材包
            if (installCaption(selectData)) {
                selectData.setInstall(true);
                mStreamingContext.applyCaptureScene(selectData.getThemeId());
            } else {
                ToastUtils.showShort(mContext, getString(R.string.install_fail));
            }
        }


        selectData.setOnLoad(false);
        handler.sendEmptyMessage(1);
    }

    public boolean installCaption(DownResponse.DataBean dataBean) {
        String Extension = "capturescene";
        String licSub = "lic";
        final String dir = PathUtils.getAssetDownloadPath(NvAsset.ASSET_CAPTURE_SCENE);
        final String fileDir = dir + File.separator + dataBean.getF_id();

        String capPath = null;
        String licPath = null;

        File file = new File(fileDir);
        if (file.exists()) {
            File[] files = new File(fileDir).listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    if (f.isFile()) {
                        if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension))  //判断扩展名
                        {
                            capPath = f.getPath();
                        }

                        if (f.getPath().substring(f.getPath().length() - licSub.length()).equals(licSub))  //判断扩展名
                        {
                            licPath = f.getPath();
                        }
                    }
                }

                if (!TextUtils.isEmpty(capPath) && !TextUtils.isEmpty(licPath)) {
                    String themeId = installSingleCaptureScene(capPath, licPath);
                    if (themeId != null) {
                        dataBean.setThemeId(themeId);
                        return true;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(ShotActivity.this, getString(R.string.down_fail));
            }
        });

        mDatas.get(select_cappos).setOnLoad(false);
        handler.sendEmptyMessage(1);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        pb_sticker.setVisibility(View.GONE);
        tv_refresh_sticker.setVisibility(View.VISIBLE);
        ToastUtils.showShort(mContext,msg);
    }
}
