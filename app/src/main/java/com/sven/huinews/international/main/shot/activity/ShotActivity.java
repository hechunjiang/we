//package com.sven.huinews.international.main.shot.activity;
//
//
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.graphics.RectF;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.animation.AlphaAnimation;
//import android.widget.CompoundButton;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.ToggleButton;
//
//import com.meicam.sdk.NvsCaptureVideoFx;
//import com.meicam.sdk.NvsFxDescription;
//import com.meicam.sdk.NvsLiveWindowExt;
//import com.meicam.sdk.NvsStreamingContext;
//import com.meicam.sdk.NvsVideoResolution;
//import com.sven.huinews.international.R;
//import com.sven.huinews.international.base.BaseActivity;
//import com.sven.huinews.international.entity.requst.NvsResCategroyRequest;
//import com.sven.huinews.international.entity.requst.NvsResListRequest;
//import com.sven.huinews.international.main.shot.adapter.FiltersAdapter;
//import com.sven.huinews.international.main.shot.adapter.VideoShowAdapter;
//import com.sven.huinews.international.main.shot.bean.FilterItem;
//import com.sven.huinews.international.main.shot.contract.ShotContract;
//import com.sven.huinews.international.main.shot.model.ShotModel;
//import com.sven.huinews.international.main.shot.presenter.ShotPresenter;
//import com.sven.huinews.international.utils.ActivityManager;
//import com.sven.huinews.international.utils.LogUtil;
//import com.sven.huinews.international.utils.ToastUtils;
//import com.sven.huinews.international.utils.shot.ParameterSettingValues;
//import com.sven.huinews.international.utils.shot.utils.Constants;
//import com.sven.huinews.international.utils.shot.utils.MediaScannerUtil;
//import com.sven.huinews.international.utils.shot.utils.PathUtils;
//import com.sven.huinews.international.utils.shot.utils.Util;
//import com.sven.huinews.international.utils.shot.utils.asset.NvAsset;
//import com.sven.huinews.international.utils.shot.utils.asset.NvAssetManager;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.ClipInfo;
//import com.sven.huinews.international.utils.shot.utils.dataInfo.TimelineData;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
///**
// * 拍摄
// */
//public class ShotActivity extends BaseActivity<ShotPresenter, ShotModel> implements NvsStreamingContext.CaptureDeviceCallback, NvsStreamingContext.CaptureRecordingDurationCallback, NvsStreamingContext.CaptureRecordingStartedCallback, ShotContract.View {
//    private NvsLiveWindowExt mLiveWindow;
//
//    private NvsStreamingContext mStreamingContext;
//    NvsStreamingContext.CaptureDeviceCapability mCapability = null;
//    private boolean mPermissionGranted;
//    private int mCurrentDeviceIndex;
//    /*已拍摄的列表*/
//    private RecyclerView rv_videos;
//    /*滤镜*/
//    private LinearLayout ll_fliter;
//    private RecyclerView fliters_rv;
//    private ImageView btn_fliters;
//    private ImageView btn_ratios;
//    private ImageView btn_stickers;
//    private ImageView btn_sets;
//    private ImageView btn_music;
//
//    private FiltersAdapter mFiltersAdapter;
//    private ArrayList<FilterItem> mFilterItemArrayList = new ArrayList<>();
//    private NvsCaptureVideoFx mCurCaptureVideoFx;
//
//
//    //对焦
//    private ImageView mImageAutoFocusRect;
//    private AlphaAnimation mFocusAnimation;
//    //拍摄速率
//    private RadioGroup rg_video;
//    private float speed = 1;  //默认速率
//    private int mSpeedMode = 3;
//    private int moreSlowSpeedMode = 1;
//    private int slowSpeedMode = 2;
//    private int normalSpeedMode = 3;
//    private int fastSpeedMode = 4;
//    private int moreFastSpeedMode = 5;
//    //美颜
//    private SeekBar strength; //磨皮
//    private SeekBar mWhitening; //美白
//    private SeekBar mReddening; //红润
//    private double mStrengthValue;
//    private double mWhiteningValue;
//    private double mReddeningValue;
//
//    //切换摄像头
//    private boolean mIsSwitchingCamera = false;
//    private ToggleButton btn_swich_camera;
//    private NvAssetManager mAssetManager;
//    //闪光灯
//    private ToggleButton btn_flash;
//
//    //滤镜布局
//    private LinearLayout ll_beauty;
//
//    //拍摄
//    private TextView btn_shot;
//    //拍摄布局
//    private LinearLayout function_ll;
//    private LinearLayout btn_ratio;
//    private LinearLayout btn_filter;
//    private LinearLayout btn_sticker;
//    private LinearLayout btn_set;
//    //当前拍摄视频路径
//    private String mCurRecordVideoPath;
//    private long mEachRecodingVideoTime = 0, mEachRecodingImageTime = 4000000;
//    private long mAllRecordingTime = 0;
//
//    //删除视频
//    private TextView btn_del_video;
//
//    private final int MIN_RECORD_DURATION = 1000000;
//
//    //拍摄视频路径集合
//    private List<String> mRecordFileList = new ArrayList<String>();
//    //录制的时间集合
//    private ArrayList<Long> mRecordTimeList = new ArrayList<Long>();
//
//    //录制视频列表
//    private VideoShowAdapter videoShowAdapter;
//
//    //底部
//    private LinearLayout bottom_view;
//    //顶部
//    private RelativeLayout rl_top;
//    //右边
//    private LinearLayout ll_right;
//    //下一步
//    private TextView btn_next;
//
//    //拍摄最大时长
//    private long time = 30 * 1000 * 1000;
//    private long copy_time = 30 * 1000 * 1000;
//    private TextView tv_time;
//
//
//    @Override
//    public int getLayoutId() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        mStreamingContext = NvsStreamingContext.getInstance();
//        ActivityManager.getInstance().pushOneActivity(this);
//        return R.layout.activity_ms_shot;
//    }
//
//    @Override
//    public void initView() {
//        mLiveWindow = findViewById(R.id.liveWindow);
//        /*滤镜dialog*/
//        btn_fliters = findViewById(R.id.btn_fliters);
//        ll_fliter = findViewById(R.id.ll_fliter);
//        fliters_rv = findViewById(R.id.fliters_rv);
//        mImageAutoFocusRect = findViewById(R.id.imageAutoFocusRect);
//        btn_swich_camera = findViewById(R.id.btn_swich_camera);
//        rg_video = findViewById(R.id.rg_video);
//        strength = findViewById(R.id.strength);
//        mWhitening = findViewById(R.id.mWhitening);
//        mReddening = findViewById(R.id.mReddening);
//        btn_flash = findViewById(R.id.btn_flash);
//        btn_shot = findViewById(R.id.btn_shot);
//        rv_videos = findViewById(R.id.rv_videos);
//        btn_del_video = findViewById(R.id.btn_del_video);
//        tv_time = findViewById(R.id.tv_time);
//        ll_beauty = findViewById(R.id.ll_beauty);
//        function_ll = findViewById(R.id.function_ll);
//
//        btn_ratio = findViewById(R.id.btn_ratio);
//        btn_filter = findViewById(R.id.btn_filter);
//        btn_sticker = findViewById(R.id.btn_sticker);
//        btn_set = findViewById(R.id.btn_set);
//
//        btn_ratios = findViewById(R.id.btn_ratios);
//        btn_stickers = findViewById(R.id.btn_stickers);
//        btn_sets = findViewById(R.id.btn_sets);
//
//        bottom_view = findViewById(R.id.bottom_view);
//
//        rl_top = findViewById(R.id.rl_top);
//
//        ll_right = findViewById(R.id.ll_right);
//        btn_music = findViewById(R.id.btn_music);
//        btn_next = findViewById(R.id.btn_next);
//
//        //timer.schedule(task,time,1000);
//    }
//
//    @Override
//    public void initEvents() {
//        btn_fliters.setOnClickListener(this);
//        btn_ratios.setOnClickListener(this);
//        btn_stickers.setOnClickListener(this);
//        btn_sets.setOnClickListener(this);
//
//        btn_shot.setOnClickListener(this);
//        btn_del_video.setOnClickListener(this);
//        btn_music.setOnClickListener(this);
//        btn_next.setOnClickListener(this);
//        btn_swich_camera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (mIsSwitchingCamera) {
//                    return;
//                }
//                if (isChecked) {
//                    mCurrentDeviceIndex = 1;
//                    btn_flash.setEnabled(false);
//                    btn_flash.setVisibility(View.INVISIBLE);
//                } else {
//                    mCurrentDeviceIndex = 0;
//                    btn_flash.setEnabled(true);
//                    btn_flash.setVisibility(View.VISIBLE);
//                }
//                mIsSwitchingCamera = true;
//                startCapturePreview(true);
//            }
//        });
//
//        btn_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                mStreamingContext.toggleFlash(b);
//            }
//        });
//
//        rg_video.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                switch (i) {
//                    case R.id.rb_video_1:
//                        if (mSpeedMode != moreSlowSpeedMode) {
//                            mSpeedMode = moreSlowSpeedMode;
//                            speed = 0.5f;
//                        }
//                        break;
//                    case R.id.rb_video_2:
//                        if (mSpeedMode != slowSpeedMode) {
//                            mSpeedMode = slowSpeedMode;
//                            speed = 0.75f;
//                        }
//                        break;
//                    case R.id.rb_video_3:
//                        if (mSpeedMode != normalSpeedMode) {
//                            mSpeedMode = normalSpeedMode;
//                            speed = 1f;
//                        }
//                        break;
//                    case R.id.rb_video_4:
//                        if (mSpeedMode != fastSpeedMode) {
//                            mSpeedMode = fastSpeedMode;
//                            speed = 1.5f;
//                        }
//                        break;
//                    case R.id.rb_video_5:
//                        if (mSpeedMode != moreFastSpeedMode) {
//                            mSpeedMode = moreFastSpeedMode;
//                            speed = 2.0f;
//                        }
//                        break;
//
//                }
//            }
//        });
//
//        mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                viewHide();
//
//                float rectHalfWidth = mImageAutoFocusRect.getWidth() / 2;
//                if (event.getX() - rectHalfWidth >= 0 && event.getX() + rectHalfWidth <= mLiveWindow.getWidth()
//                        && event.getY() - rectHalfWidth >= 0 && event.getY() + rectHalfWidth <= mLiveWindow.getHeight()) {
//                    mImageAutoFocusRect.setX(event.getX() - rectHalfWidth);
//                    mImageAutoFocusRect.setY(event.getY() - rectHalfWidth);
//                    RectF rectFrame = new RectF();
//                    rectFrame.set(mImageAutoFocusRect.getX(), mImageAutoFocusRect.getY(),
//                            mImageAutoFocusRect.getX() + mImageAutoFocusRect.getWidth(),
//                            mImageAutoFocusRect.getY() + mImageAutoFocusRect.getHeight());
//                    //启动自动聚焦
//                    mImageAutoFocusRect.startAnimation(mFocusAnimation);
//                    mStreamingContext.startAutoFocus(new RectF(rectFrame));
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public void onClickEvent(View v) {
//        if (v == btn_fliters) { //滤镜
//            setRecordStateBackground(RECORD_FILTER);
//            ll_fliter.setVisibility(View.VISIBLE);
//            ll_beauty.setVisibility(View.VISIBLE);
//        } else if (v == btn_ratios) { //缩放
//            setRecordStateBackground(RECORD_FILTER);
//            ll_fliter.setVisibility(View.GONE);
//            ll_beauty.setVisibility(View.VISIBLE);
//        } else if (v == btn_stickers) { //贴图
//            setRecordStateBackground(RECORD_FILTER);
//            ll_fliter.setVisibility(View.GONE);
//            ll_beauty.setVisibility(View.VISIBLE);
//        } else if (v == btn_sets) {  //设置
//            setRecordStateBackground(RECORD_FILTER);
//            ll_fliter.setVisibility(View.GONE);
//            ll_beauty.setVisibility(View.VISIBLE);
//        } else if (v == btn_shot) {
//            startRecord();
//        } else if (v == btn_del_video) {
//
//            if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
//                ToastUtils.showShort(mContext, getString(R.string.video_delet_tip));
//                return;
//            }
//
//            if (mRecordTimeList.size() != 0 && mRecordFileList.size() != 0) {
//                mAllRecordingTime -= mRecordTimeList.get(mRecordTimeList.size() - 1);
//                mRecordTimeList.remove(mRecordTimeList.size() - 1);
//                PathUtils.deleteFile(mRecordFileList.get(mRecordFileList.size() - 1));
//                mRecordFileList.remove(mRecordFileList.size() - 1);
//                videoShowAdapter.setData(mRecordFileList);
//
//                time = copy_time - mAllRecordingTime;
//                tv_time.setText(time + "");
//            }
//
//        } else if (v == btn_next) {
//            next();
//        } else if (v == btn_music) {
//            startActivity(new Intent(mContext, MusicActivity.class));
//        }
//    }
//
//    @Override
//    public void initObject() {
//        setMVP();
//        setNvsResCategroy();
//        initCapture();
//
//        mAssetManager = NvAssetManager.sharedInstance(this);
//        mAssetManager.searchLocalAssets(NvAsset.ASSET_FILTER);
//        String bundlePath = "filter";
//        mAssetManager.searchReservedAssets(NvAsset.ASSET_FILTER, bundlePath);
//
//        bundlePath = "arface";
//        mAssetManager.searchReservedAssets(NvAsset.ASSET_FACE_BUNDLE_STICKER, bundlePath);
//
//        mAssetManager.searchLocalAssets(NvAsset.ASSET_FACE1_STICKER);
//
//        mFocusAnimation = new AlphaAnimation(1.0f, 0.0f);
//        mFocusAnimation.setDuration(1000);
//        mFocusAnimation.setFillAfter(true);
//
//        initVideosView();
//        initFilterList();
//        initFilterView();
//
//        initBeautyData();
//        beautyListener();
//
//    }
//
//    private void setNvsResCategroy() {
//       // mPresenter.onNvsResCategroy();
//        mPresenter.onNvsResCategroy();
//    }
//
//    private void initVideosView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        rv_videos.setLayoutManager(linearLayoutManager);
//        videoShowAdapter = new VideoShowAdapter(mRecordFileList);
//        videoShowAdapter.setHasStableIds(true);
//        rv_videos.setAdapter(videoShowAdapter);
//
//        videoShowAdapter.setOnItemClickListener(new VideoShowAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
//                    ToastUtils.showShort(ShotActivity.this, getString(R.string.video_delet_tip));
//                    return;
//                }
//
//                if (mRecordTimeList.size() != 0 && mRecordFileList.size() != 0) {
//                    mAllRecordingTime -= mRecordTimeList.get(position);
//                    mRecordTimeList.remove(position);
//
//                    PathUtils.deleteFile(mRecordFileList.get(mRecordFileList.size() - 1));
//                    mRecordFileList.remove(position);
//                    videoShowAdapter.setData(mRecordFileList);
//
//                    time = copy_time - mAllRecordingTime;
//                    tv_time.setText(time + "");
//
//                }
//            }
//        });
//    }
//
//    /**
//     * 开始录制
//     */
//    private void startRecord() {
//        // 当前在录制状态，可停止视频录制
//        if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
//            stopRecording();
//        } else {
//            //TODO:录制时间达到30s
//            if (time <= 0) {
//                LogUtil.showLog("录制时间达到30s");
//                return;
//            }
//
//            mCurRecordVideoPath = PathUtils.getRecordVideoPath();
//            LogUtil.showLog("msg----mCurRecordVideoPath:" + mCurRecordVideoPath);
//            if (mCapability == null) {
//                return;
//            }
//            btn_shot.setEnabled(false);
//            btn_shot.setBackgroundResource(R.mipmap.icon_shot_stop);
//
//            mEachRecodingVideoTime = 0;
//            //当前未在视频录制状态，则启动视频录制。此处使用带特效的录制方式
//            if (!mStreamingContext.startRecording(mCurRecordVideoPath))
//                return;
//            // isInRecording(false);
//            mRecordFileList.add(mCurRecordVideoPath);
//        }
//    }
//
//
//    /**
//     * 结束录制
//     */
//    private void stopRecording() {
//        mStreamingContext.stopRecording();
//        btn_shot.setBackgroundResource(R.mipmap.icon_shot);
//
//        // 拍视频
//
//        mAllRecordingTime += mEachRecodingVideoTime;
//        mRecordTimeList.add(mEachRecodingVideoTime);
//
//        //TODO:处理录制时间刚好达到30s的情况
//        if (mAllRecordingTime >= copy_time) {
//            Log.e("weiwei", "结束");
//            time = 0;
//        } else {
//            time = copy_time - mAllRecordingTime;
//        }
//        //  mStartText.setText(mRecordTimeList.size() + "");
//        // isInRecording(true);
//    }
//
//    /**
//     * 初始化滤镜数据
//     */
//    private void initFilterList() {
//        int[] resImags = {
//                R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo,
//                R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo,
//                R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo, R.mipmap.lvjing_photo
//        };
//
//        mFilterItemArrayList.clear();
//        FilterItem filterItem = new FilterItem();
//        filterItem.setFilterName("无");
//        filterItem.setImageId(R.mipmap.lvjing_photo);
//        mFilterItemArrayList.add(filterItem);
//
//        ArrayList<NvAsset> filterList = getLocalData(NvAsset.ASSET_FILTER);
//        String bundlePath = "filter/info.txt";
//        Util.getBundleFilterInfo(this, filterList, bundlePath);
//        for (NvAsset asset : filterList) {
//            FilterItem newFilterItem = new FilterItem();
//            if (asset.isReserved()) {
//                newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUNDLE);
//            } else {
//                newFilterItem.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
//            }
//            newFilterItem.setFilterName(asset.name);
//            newFilterItem.setPackageId(asset.uuid);
//            newFilterItem.setImageUrl(asset.coverUrl);
//            mFilterItemArrayList.add(newFilterItem);
//        }
//
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
//    }
//
//    /**
//     * 获取滤镜数据
//     *
//     * @param assetType
//     * @return
//     */
//    private ArrayList<NvAsset> getLocalData(int assetType) {
//        return mAssetManager.getUsableAssets(assetType, NvAsset.AspectRatio_All, 0);
//    }
//
//
//    /**
//     * 初始化滤镜
//     */
//    private void initFilterView() {
//        mFiltersAdapter = new FiltersAdapter(mContext);
//        mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        fliters_rv.setLayoutManager(linearLayoutManager);
//        fliters_rv.setAdapter(mFiltersAdapter);
//        mFiltersAdapter.setOnItemClickListener(new FiltersAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (position < mFilterItemArrayList.size()) {
//                    removeAllFilterFx();
//                    if (position == 0) {
//                        return;
//                    }
//
//                    FilterItem filterItem = mFilterItemArrayList.get(position);
//                    int filterMode = filterItem.getFilterMode();
//                    if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
//                        String filterName = filterItem.getFilterName();
//                        if (!TextUtils.isEmpty(filterName)) {
//                            mCurCaptureVideoFx = mStreamingContext.appendBuiltinCaptureVideoFx(filterName);
//                        }
//                    } else {
//                        String filterPackageId = filterItem.getPackageId();
//                        if (!TextUtils.isEmpty(filterPackageId)) {
//                            mCurCaptureVideoFx = mStreamingContext.appendPackagedCaptureVideoFx(filterPackageId);
//                        }
//                    }
//                    mCurCaptureVideoFx.setFilterIntensity(1.0f);
//                }
//            }
//
//            @Override
//            public void onSameItemClick() {
//
//            }
//        });
//    }
//
//    private void initBeautyData() {
//        mStrengthValue = 50;
//        mWhiteningValue = 50;
//        mReddeningValue = 50;
//
//        //默认开启美颜
//        NvsCaptureVideoFx fx = mStreamingContext.appendBeautyCaptureVideoFx();   //添加美颜采集特效
//        fx.setFloatVal("Strength", mStrengthValue);//设置美颜强度值
//        fx.setFloatVal("Whitening", mWhiteningValue);
//        fx.setFloatVal("Reddening", mReddeningValue);
//
//        //设置美颜数据
//        NvsFxDescription fxDescription = mStreamingContext.getVideoFxDescription("Beauty");
//        List<NvsFxDescription.ParamInfoObject> paramInfo = fxDescription.getAllParamsInfo();
//        for (NvsFxDescription.ParamInfoObject param : paramInfo) {
//            String paramName = param.getString("paramName");
//            if (paramName.equals("Strength")) {
//                double maxValue = param.getFloat("floatMaxVal");
//                mStrengthValue = param.getFloat("floatDefVal");
//                Log.e("mStrengthValue=", mStrengthValue + "");
//                //  mStrength.setMax((int) (maxValue * 100));
//                strength.setProgress((int) (mStrengthValue * 100));
//
//                //  mStrength_text.setText(String.format(Locale.getDefault(), "%.2f", mStrengthValue));
//            } else if (paramName.equals("Whitening")) {
//                double maxValue = param.getFloat("floatMaxVal");
//                mWhiteningValue = param.getFloat("floatDefVal");
//                Log.e("mWhiteningValue=", mWhiteningValue + "");
//              /*  mWhitening.setMax((int) (maxValue * 100));
//                mWhitening.setProgress((int) (mWhiteningValue * 100));
//                mWhitening_text.setText(String.format(Locale.getDefault(), "%.2f", mWhiteningValue));*/
//            } else if (paramName.equals("Reddening")) {
//                double maxValue = param.getFloat("floatMaxVal");
//                mReddeningValue = param.getFloat("floatDefVal");
//                Log.e("mReddeningValue=", mReddeningValue + "");
//              /*  mReddening.setMax((int) (maxValue * 100));
//                mReddening.setProgress((int) (mReddeningValue * 100));
//                mReddening_text.setText(String.format(Locale.getDefault(), "%.2f", mReddeningValue));*/
//
//            }
//        }
//    }
//
//    /**
//     * 美颜动作监听
//     */
//    private void beautyListener() {
//        //磨皮
//        strength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mStrengthValue = progress * 0.01;
//                for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
//                    NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
//                    String name = fx.getBuiltinCaptureVideoFxName();
//                    if (name.equals("Beauty")) {
//                        //设置美颜强度值
//                        fx.setFloatVal("Strength", mStrengthValue);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        //美白
//        mWhitening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
//                mWhiteningValue = progress * 0.01;
//
//                for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
//                    NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
//                    String name = fx.getBuiltinCaptureVideoFxName();
//                    if (name.equals("Beauty")) {
//                        //设置美颜强度值
//                        fx.setFloatVal("Whitening", mWhiteningValue);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//        /*红润*/
//        mReddening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mReddeningValue = progress * 0.01;
//                for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
//                    NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
//                    String name = fx.getBuiltinCaptureVideoFxName();
//                    if (name.equals("Beauty")) {
//                        //设置美颜强度值
//                        fx.setFloatVal("Reddening", mReddeningValue);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }
//
//    /**
//     * 初始化预览
//     */
//    private void initCapture() {
//        if (null == mStreamingContext) {
//            return;
//        }
//        //给Streaming Context设置采集设备回调接口
//        mStreamingContext.setCaptureDeviceCallback(this);
//        mStreamingContext.setCaptureRecordingDurationCallback(this);
//        mStreamingContext.setCaptureRecordingStartedCallback(this);
//        if (mStreamingContext.getCaptureDeviceCount() == 0) {
//            return;
//        }
//
//        // 将采集预览输出连接到LiveWindow控件
//        if (!mStreamingContext.connectCapturePreviewWithLiveWindowExt(mLiveWindow)) {
//            LogUtil.showLog("Failed to connect capture preview with livewindow!");
//            return;
//        }
//        mCurrentDeviceIndex = 0;
//        //采集设备数量判定
//        if (mStreamingContext.getCaptureDeviceCount() > 1) {
//            //  mSwitchFacingLayout.setEnabled(true);
//        }
//        mPermissionGranted = true;
//        startCapturePreview(false);
//        mStreamingContext.removeAllCaptureVideoFx();
//    }
//
//    private boolean startCapturePreview(boolean deviceChanged) {
//        // 判断当前引擎状态是否为采集预览状态
//        int captureResolutionGrade = ParameterSettingValues.instance().getCaptureResolutionGrade();
//        if (mPermissionGranted && (deviceChanged || getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW)) {
//            if (!mStreamingContext.startCapturePreview(mCurrentDeviceIndex,
//                    captureResolutionGrade,
//                    NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER |
//                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_CAPTURE_BUDDY_HOST_VIDEO_FRAME
//                            | NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_STRICT_PREVIEW_VIDEO_SIZE, null)) {
//                LogUtil.showLog("Failed to start capture preview!");
//                return false;
//            }
//        }
//        return true;
//    }
//
//    // 获取当前引擎状态
//    private int getCurrentEngineState() {
//        return mStreamingContext.getStreamingEngineState();
//    }
//
//
//    private void updateSettingsWithCapability(int deviceIndex) {
//        //获取采集设备能力描述对象，设置自动聚焦，曝光补偿，缩放
//        mCapability = mStreamingContext.getCaptureDeviceCapability(deviceIndex);
//        if (null == mCapability) {
//            return;
//        }
//
//        //是否支持闪光灯
//        if (mCapability.supportFlash) {
//            //   mFlashLayout.setEnabled(true);
//        }
//
//        mImageAutoFocusRect.setX((mLiveWindow.getWidth() - mImageAutoFocusRect.getWidth()) / 2);
//        mImageAutoFocusRect.setY((mLiveWindow.getHeight() - mImageAutoFocusRect.getHeight()) / 2);
//        RectF rectFrame = new RectF();
//        rectFrame.set(mImageAutoFocusRect.getX(), mImageAutoFocusRect.getY(),
//                mImageAutoFocusRect.getX() + mImageAutoFocusRect.getWidth(),
//                mImageAutoFocusRect.getY() + mImageAutoFocusRect.getHeight());
//
//        //是否支持自动聚焦
//        /*if (mCapability.supportAutoFocus) {
//            mStreamingContext.startAutoFocus(new RectF(rectFrame));
//        }*/
//
//        // 是否支持缩放
////        if (mCapability.supportZoom) {
////            mZoomValue = mCapability.maxZoom;
////            mZoomSeekbar.setMax(mZoomValue);
////            mZoomSeekbar.setProgress(mStreamingContext.getZoom());
////            mZoomSeekbar.setEnabled(true);
////        } else {
////            Log.e(TAG, "该设备不支持缩放");
////        }
//
//        // 是否支持曝光补偿
////        if (mCapability.supportExposureCompensation) {
////            mMinExpose = mCapability.minExposureCompensation;
////            mExposeSeekbar.setMax(mCapability.maxExposureCompensation - mMinExpose);
////            mExposeSeekbar.setProgress(mStreamingContext.getExposureCompensation() - mMinExpose);
////            mExposeSeekbar.setEnabled(true);
////        }
//    }
//
//    @Override
//    public void onCaptureDeviceCapsReady(int captureDeviceIndex) {
//        if (captureDeviceIndex != mCurrentDeviceIndex) {
//            return;
//        }
//        updateSettingsWithCapability(captureDeviceIndex);
//    }
//
//    @Override
//    public void onCaptureDevicePreviewResolutionReady(int i) {
//
//    }
//
//    @Override
//    public void onCaptureDevicePreviewStarted(int i) {
//        mIsSwitchingCamera = false;
//    }
//
//    @Override
//    public void onCaptureDeviceError(int i, int i1) {
//
//    }
//
//    @Override
//    public void onCaptureDeviceStopped(int i) {
//
//    }
//
//    @Override
//    public void onCaptureDeviceAutoFocusComplete(int i, boolean b) {
//
//    }
//
//    @Override
//    public void onCaptureRecordingFinished(int i) {
//        setRecordStateBackground(RECORD_STOP);
//        // 保存到媒体库
//        if (mRecordFileList != null && !mRecordFileList.isEmpty()) {
//            for (String path : mRecordFileList) {
//                if (path == null) {
//                    continue;
//                }
//                if (path.endsWith(".mp4")) {
//                    MediaScannerUtil.scanFile(path, "video/mp4");
//                }
//            }
//            videoShowAdapter.setData(mRecordFileList);
//        }
//
//    }
//
//    @Override
//    public void onCaptureRecordingError(int i) {
//        //tv_time.setText(mAllRecordingTime+"");
//    }
//
//    @Override
//    public void onCaptureRecordingDuration(int i, long l) {
//        // 拍视频or拍照片
//        if (l >= MIN_RECORD_DURATION) {
//            btn_shot.setEnabled(true);
//        }
//        mEachRecodingVideoTime = l;
//
//
//        if (time - mEachRecodingVideoTime <= 0) {
//            //TODO:录制时间达到30s
//            LogUtil.showLog("录制不能超过30s");
//            time = 0;
//            tv_time.setText(time + "");
//            stopRecording();
//        } else {
//            tv_time.setText("" + (time - mEachRecodingVideoTime));
//        }
//        //  mRecordTime.setVisibility(View.VISIBLE);
//        // mRecordTime.setText(TimeFormatUtil.formatUsToString2(mAllRecordingTime + mEachRecodingVideoTime));
//    }
//
//    @Override
//    public void onCaptureRecordingStarted(int i) {
//        tv_time.setText("" + time);
//        setRecordStateBackground(RECORD_PLATING);
//    }
//
//
//    private void viewHide() {
//        ll_fliter.setVisibility(View.GONE);
//        ll_beauty.setVisibility(View.GONE);
//        setRecordStateBackground(RECORD_STOP);
//    }
//
//    private void removeAllFilterFx() {
//        List<Integer> remove_list = new ArrayList<>();
//        for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
//            NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
//            if (fx == null)
//                continue;
//
//            String name = fx.getBuiltinCaptureVideoFxName();
//            if (name != null && !name.equals("Beauty") && !name.equals("Face Effect")) {
//                remove_list.add(i);
//                Log.e("===>", "fx name: " + name);
//            }
//        }
//        if (!remove_list.isEmpty()) {
//            for (int i = 0; i < remove_list.size(); i++) {
//                mStreamingContext.removeCaptureVideoFx(remove_list.get(i));
//            }
//        }
//    }
//
//    /**
//     * 跳转编辑界面
//     */
//    private void next() {
//        /*将拍摄的视频传到下一个页面mRecordFileList*/
//        ArrayList<ClipInfo> pathList = new ArrayList<>();
//        for (int i = 0; i < mRecordFileList.size(); i++) {
//            ClipInfo clipInfo = new ClipInfo();
//            clipInfo.setFilePath(mRecordFileList.get(i));
//            pathList.add(clipInfo);
//        }
//        TimelineData.instance().clear();
//        NvsVideoResolution videoEditRes = new NvsVideoResolution();
//        videoEditRes.imageWidth = 720;
//        videoEditRes.imageHeight = 1280;
//        TimelineData.instance().setVideoResolution(videoEditRes);
//        TimelineData.instance().setClipInfoData(pathList);
//        TimelineData.instance().setMakeRatio(4);
//        btn_next.setClickable(false);
//
//        //  Bundle bundle = new Bundle();
//        // bundle.putBoolean(Constants.START_ACTIVITY_FROM_CAPTURE, true);
//        // AppManager.getInstance().jumpActivity(CaptureActivity.this, VideoEditActivity.class, bundle);
//
//        startActivity(new Intent(mContext, PublishActivity.class));
//    }
//
//    private static final int RECORD_PLATING = 1;   //录制状态
//    private static final int RECORD_STOP = 2;      //录制结束
//    private static final int RECORD_FILTER = 3;    //点击滤镜
//
//    private void setRecordStateBackground(int status) {
//        switch (status) {
//            case RECORD_PLATING:
//                Log.e("weiwei", "RECORD_PLATING");
//                ll_beauty.setVisibility(View.GONE);
//                ll_fliter.setVisibility(View.GONE);
//
//                tv_time.setVisibility(View.VISIBLE);
//                function_ll.setBackgroundColor(ContextCompat.getColor(this, R.color.white_0));
//                btn_ratio.setVisibility(View.GONE);
//                btn_filter.setVisibility(View.GONE);
//                btn_sticker.setVisibility(View.GONE);
//                btn_set.setVisibility(View.GONE);
//
//                rl_top.setVisibility(View.GONE);
//                ll_right.setVisibility(View.GONE);
//
//                bottom_view.setVisibility(View.INVISIBLE);
//                break;
//
//            case RECORD_STOP:
//                Log.e("weiwei", "RECORD_STOP");
//                ll_beauty.setVisibility(View.GONE);
//                ll_fliter.setVisibility(View.GONE);
//
//
//                tv_time.setVisibility(View.GONE);
//                function_ll.setBackgroundColor(ContextCompat.getColor(this, R.color.white_0));
//                btn_ratio.setVisibility(View.VISIBLE);
//                btn_filter.setVisibility(View.VISIBLE);
//                btn_sticker.setVisibility(View.VISIBLE);
//                btn_set.setVisibility(View.VISIBLE);
//
//                rl_top.setVisibility(View.VISIBLE);
//                ll_right.setVisibility(View.VISIBLE);
//
//                bottom_view.setVisibility(View.VISIBLE);
//                bottom_view.setBackgroundColor(ContextCompat.getColor(this, R.color.white_0));
//                break;
//            case RECORD_FILTER:
//                Log.e("weiwei", "RECORD_FILTER");
//
//
//                tv_time.setVisibility(View.GONE);
//                function_ll.setBackgroundColor(ContextCompat.getColor(this, R.color.c50000000));
//                btn_ratio.setVisibility(View.VISIBLE);
//                btn_filter.setVisibility(View.VISIBLE);
//                btn_sticker.setVisibility(View.VISIBLE);
//                btn_set.setVisibility(View.VISIBLE);
//
//                rl_top.setVisibility(View.VISIBLE);
//                ll_right.setVisibility(View.VISIBLE);
//
//                bottom_view.setVisibility(View.VISIBLE);
//                bottom_view.setBackgroundColor(ContextCompat.getColor(this, R.color.c50000000));
//                break;
//
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mStreamingContext != null) {
//            mStreamingContext.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (getCurrentEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
//            stopRecording();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        startCapturePreview(false);
//    }
//
//    @Override
//    public NvsResCategroyRequest getNvsResCategroyRequest() {
//        NvsResCategroyRequest request = new NvsResCategroyRequest();
//        request.setType(3);
//        return request;
//    }
//
//    @Override
//    public NvsResListRequest getResListRequest() {
//        return null;
//    }
//
//    @Override
//    public void showLoading() {
//
//    }
//
//    @Override
//    public void hideLoading() {
//
//    }
//
//    @Override
//    public void showErrorTip(int code, String msg) {
//
//    }
//}
