package wedemo.activity.animatesticker;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import wedemo.activity.data.BackupData;
import wedemo.activity.down.CategoryResponse;
import wedemo.activity.down.DownContract;
import wedemo.activity.down.DownModel;
import wedemo.activity.down.DownPresenter;
import wedemo.activity.down.DownRequest;
import wedemo.activity.down.DownResponse;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.activity.view.CustomViewPager;
import wedemo.base.BaseActivity;
import wedemo.fragment.VideoFragment;
import wedemo.utils.AppManager;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.PathUtils;
import wedemo.utils.TimelineManager;
import wedemo.utils.asset.NvAsset;
import wedemo.utils.asset.NvAssetManager;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.StickerInfo;

public class AnimateStickerAssetActivity extends BaseActivity<DownPresenter, DownModel> implements DownContract.View {
    private static final String TAG = "ASAssetActivity";
    private static final int ANIMATESTICKERREQUESTLIST = 104;
    private static final int VIDEOPLAYTOEOF = 105;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private ImageView mMoreDownload;
    private TabLayout mAnimateStickerTypeTab;
    private CustomViewPager mViewPager;
    private ImageView mStickerAssetFinish;
    private VideoFragment mVideoFragment;

    private ArrayList<String> mStickerAssetTypeList;
    private ArrayList<AnimateStickerListFragment> mAssetFragmentsArray = new ArrayList<>();
    private AnimateStickerListFragment mStickerListFragment;
    private AnimateStickerListFragment mCustomStickerListFragment;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private NvAssetManager mAssetManager;
    private int mAssetType = NvAsset.ASSET_ANIMATED_STICKER;
    private int mCustomAssetType = NvAsset.ASSET_CUSTOM_ANIMATED_STICKER;
    private long mInPoint = 0;
    private long mStickerDuration = 0;
    private int mCurTabPage = 0;// 记录当前tab页
    private int mPrevTabPage = 0;//记录上次操作的Tab
    ArrayList<StickerInfo> mStickerDataListClone;
    NvsTimelineAnimatedSticker mCurAddAnimateSticker = null;
    private int mCurSelectedPos = -1;
    private boolean isAnimateStickerUuidItemClick = false;

    private AnimateStickerAssetActivity.AnimateStickerAssetHandler m_handler = new AnimateStickerAssetActivity.AnimateStickerAssetHandler(this);
    private CustomTimeLine customTimeLine;

    private List<DownResponse.DataBean> mDatas = new ArrayList<>();

    private int selectPos = 0;
    private ProgressBar pb_anim;
    private TextView tv_refresh_anim;

    @Override
    public void setSDKCategroy(List<CategoryResponse.DataBean> mDatas) {

    }

    @Override
    public void setSdkDown(List<DownResponse.DataBean> mDatas) {
        pb_anim.setVisibility(View.GONE);
        tv_refresh_anim.setVisibility(View.GONE);

        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }

        final String dir = PathUtils.getAssetDownloadPath(NvAsset.ASSET_ANIMATED_STICKER);
        //检查文件是否下载解压
        for (DownResponse.DataBean dataBean : mDatas) {

            if (TextUtils.isEmpty(dataBean.getF_id())) {
                dataBean.setDown(false);
                continue;
            }
            final String checkPath = dir + File.separator + dataBean.getF_id();
            dataBean.setDown(PathUtils.fileIsExists(checkPath));
        }

        this.mDatas = mDatas;
        mStickerListFragment.setAssetInfolist(mDatas);
    }

    @Override
    public DownRequest getDownRequest() {
        DownRequest downRequest = new DownRequest();
        downRequest.setType(Constants.TYPE_ANIMATEDSTICKER);
        return downRequest;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mStickerListFragment.setAssetInfolist(mDatas);
        }
    };

    @Override
    public void onDownLoadFinish(DownResponse.DataBean dataBean) {
        if (dataBean.isInstall()) {

            ///stickerAdapter.notifyItemChanged(select_cappos);
            mStickerListFragment.setAssetInfolist(mDatas);
            applyAnimateSticker(selectPos);
        } else {
            //安装素材包
            if (installCaption(dataBean)) {
                dataBean.setInstall(true);
                //mStreamingContext.applyCaptureScene(dataBean.getThemeId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        applyAnimateSticker(selectPos);
                    }
                });

                Log.e("weiwei", "引用素材");
            } else {
                ToastUtils.showShort(mContext, getString(R.string.install_fail));
            }
        }

        dataBean.setOnLoad(false);
        handler.sendEmptyMessage(1);
    }

    public boolean installCaption(DownResponse.DataBean dataBean) {
        String Extension = "animatedsticker";
        String licSub = "lic";
        final String dir = PathUtils.getAssetDownloadPath(NvAsset.ASSET_ANIMATED_STICKER);
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

    private String installSingleCaptureScene(String path, String licpath) {
        StringBuilder themeId = new StringBuilder();
        int error = mStreamingContext.getAssetPackageManager().installAssetPackage(path, licpath,
                NvsAssetPackageManager.ASSET_PACKAGE_TYPE_ANIMATEDSTICKER, true, themeId);
        if (error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR &&
                error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            Log.e("weiwei", "Failed to install package!" + path);
            return null;
        }

        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            error = mStreamingContext.getAssetPackageManager().upgradeAssetPackage(path, licpath,
                    NvsAssetPackageManager.ASSET_PACKAGE_TYPE_ANIMATEDSTICKER, true, themeId);
            if (error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR) {
                Log.e("weiwei", "Failed to upgrade package!" + path);
            }
        }

        return themeId.toString();
    }

    @Override
    public void showLoading() {
        // ToastUtils.showShort(this,getString(R.string.down_fail));
        mDatas.get(selectPos).setOnLoad(false);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStickerListFragment.setAssetInfolist(mDatas);
            }
        });

        // handler.sendEmptyMessage(1);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        pb_anim.setVisibility(View.GONE);
        tv_refresh_anim.setVisibility(View.VISIBLE);

        ToastUtils.showShort(mContext, msg);
    }

    static class AnimateStickerAssetHandler extends Handler {
        WeakReference<AnimateStickerAssetActivity> mWeakReference;

        public AnimateStickerAssetHandler(AnimateStickerAssetActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final AnimateStickerAssetActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case VIDEOPLAYTOEOF:
                        activity.seekTimeline();
                        activity.updateStickerBoundingRect();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_animate_sticker_asset;
    }

    @Override
    public void initView() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mMoreDownload = (ImageView) findViewById(R.id.moreDownload);
        mAnimateStickerTypeTab = (TabLayout) findViewById(R.id.animateStickerTypeTab);
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        mViewPager.setPagingEnabled(false);
        mStickerAssetFinish = (ImageView) findViewById(R.id.stickerAssetFinish);
        pb_anim = findViewById(R.id.pb_anim);
        tv_refresh_anim = findViewById(R.id.tv_refresh_anim);

        mTitleBar.setRightText(getString(R.string.save));
    }


    @Override
    public void initObject() {
        setMVP();
        initAssetData();
        initVideoFragment();
        initTabLayout();

        initAnimateStickerDataList();
    }


    @Override
    public void initEvents() {
        mMoreDownload.setOnClickListener(this);
        mStickerAssetFinish.setOnClickListener(this);
        tv_refresh_anim.setOnClickListener(this);

        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                finish();
            }

            @Override
            public void OnNextTextClick() {
                mStreamingContext.stop();
                BackupData.instance().setStickerData(mStickerDataListClone);
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
            }
        });

        mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
            @Override
            public void onAssetDelete() {
                int zVal = (int) mCurAddAnimateSticker.getZValue();
                int stickerIndex = getAnimateStickerIndex(zVal);
                if (stickerIndex >= 0)
                    mStickerDataListClone.remove(stickerIndex);
                mTimeline.removeAnimatedSticker(mCurAddAnimateSticker);
                mCurAddAnimateSticker = null;
                mVideoFragment.setCurAnimateSticker(mCurAddAnimateSticker);
                mVideoFragment.changeStickerRectVisible();
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);

                //取消每个Tab页贴纸选中的状态
                for (int index = 0; index < mAssetFragmentsArray.size(); ++index) {
                    mAssetFragmentsArray.get(index).setSelectedPos(-1);
                    mAssetFragmentsArray.get(index).notifyDataSetChanged();
                }
            }

            @Override
            public void onAssetSelected(PointF curPoint) {
            }

            @Override
            public void onAssetTranstion() {
                if (mCurAddAnimateSticker == null)
                    return;
                int zVal = (int) mCurAddAnimateSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0)
                    mStickerDataListClone.get(index).setTranslation(mCurAddAnimateSticker.getTranslation());
            }

            @Override
            public void onAssetScale() {
                if (mCurAddAnimateSticker == null)
                    return;
                int zVal = (int) mCurAddAnimateSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setTranslation(mCurAddAnimateSticker.getTranslation());
                    mStickerDataListClone.get(index).setScaleFactor(mCurAddAnimateSticker.getScale());
                    mStickerDataListClone.get(index).setRotation(mCurAddAnimateSticker.getRotationZ());
                }
            }

            @Override
            public void onAssetAlign(int alignVal) {

            }

            @Override
            public void onAssetHorizFlip(boolean isHorizFlip) {
                if (mCurAddAnimateSticker == null)
                    return;
                int zVal = (int) mCurAddAnimateSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setHorizFlip(mCurAddAnimateSticker.getHorizontalFlip());
                }
            }
        });

        mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                m_handler.sendEmptyMessage(VIDEOPLAYTOEOF);
            }

            @Override
            public void playStopped(NvsTimeline timeline) {
                if (isAnimateStickerUuidItemClick)
                    return;
                updateStickerBoundingRect();
            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                mVideoFragment.setDrawRectVisible(View.GONE);
            }

            @Override
            public void streamingEngineStateChanged(int state) {

            }
        });
        mVideoFragment.setLiveWindowClickListener(new VideoFragment.OnLiveWindowClickListener() {
            @Override
            public void onLiveWindowClick() {
                isAnimateStickerUuidItemClick = false;
            }
        });
        mVideoFragment.setStickerMuteListener(new VideoFragment.OnStickerMuteListener() {
            @Override
            public void onStickerMute() {
                if (mCurAddAnimateSticker == null)
                    return;
                float volumeGain = mCurAddAnimateSticker.getVolumeGain().leftVolume;
                int zVal = (int) mCurAddAnimateSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setVolumeGain(volumeGain);
                }
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        if (id == R.id.moreDownload) {
            mStreamingContext.stop();
            mMoreDownload.setClickable(false);
            AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AnimateStickerDowloadActivity.class, null, ANIMATESTICKERREQUESTLIST);

        } else if (id == R.id.stickerAssetFinish) {
            mStreamingContext.stop();
            BackupData.instance().setStickerData(mStickerDataListClone);
            //TimelineManager.getInstance().getCurrenTimeline().getTimeData().setStickerData(mStickerDataListClone);
            removeTimeline();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            AppManager.getInstance().finishActivity();

        } else if (id == R.id.tv_refresh_anim) {
            initAnimateStickerDataList();
        }
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case ANIMATESTICKERREQUESTLIST:
                initAnimateStickerDataList();
                mAssetFragmentsArray.get(0).setAssetInfolist(mDatas);
                int selectPos = getSelectedPos();
                mAssetFragmentsArray.get(0).setSelectedPos(selectPos);
                mAssetFragmentsArray.get(0).notifyDataSetChanged();
                updateStickerBoundingRect();
                break;
            default:
                break;
        }
    }

    private void removeTimeline() {
        CustomTimelineUtil.removeTimeline(customTimeLine);
        mTimeline = null;
        m_handler.removeCallbacksAndMessages(null);
    }

    private int getSelectedPos() {
        int selectPos = -1;
        if (mCurAddAnimateSticker != null) {
            String uuid = mCurAddAnimateSticker.getAnimatedStickerPackageId();
//            for (int index = 0;index < mTotalStickerAssetList.size();++index){
//                if (mTotalStickerAssetList.get(index).uuid.compareTo(uuid) == 0){
//                    selectPos = index;
//                    break;
//                }
//            }
        }

        return selectPos;
    }

    private void initAssetData() {
        customTimeLine = CustomTimelineUtil.createcCopyTimeline(TimelineManager.getInstance().getCurrenTimeline());
        mTimeline = customTimeLine.getTimeline();
        if (mTimeline == null)
            return;
        mStickerDataListClone = BackupData.instance().cloneStickerData();
        CustomTimelineUtil.setAllSticker(customTimeLine);
        mStickerAssetTypeList = new ArrayList<>();
        mAssetManager = NvAssetManager.sharedInstance(this);
        mInPoint = BackupData.instance().getCurSeekTimelinePos();
        mStickerDuration = BackupData.instance().getStickerDuration();

//        mAssetManager.searchLocalAssets(mAssetType);
//        String bundlePath = "sticker";
//        mAssetManager.searchReservedAssets(mAssetType,bundlePath);
//
//        //自定义贴纸
//        mAssetManager.searchLocalAssets(mCustomAssetType);

    }

    private void initAnimateStickerDataList() {
        pb_anim.setVisibility(View.VISIBLE);
        tv_refresh_anim.setVisibility(View.GONE);
        mPresenter.onDownload(this);
    }

    //获取下载到手机缓存路径下的素材，包括assets路径下自带的素材
    private ArrayList<NvAsset> getAssetsDataList() {
        return mAssetManager.getUsableAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    private ArrayList<NvAsset> getCustomAssetsDataList() {
        return mAssetManager.getUsableAssets(mCustomAssetType, NvAsset.AspectRatio_All, 0);
    }

    private void initTabLayout() {
        mStickerAssetTypeList.add("全部");
        //mStickerAssetTypeList.add("自定义");
        for (int index = 0; index < mStickerAssetTypeList.size(); index++) {
            mAnimateStickerTypeTab.addTab(mAnimateStickerTypeTab.newTab().setText(mStickerAssetTypeList.get(index)));
        }
        initAnimateStickerFragment();
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mAssetFragmentsArray.get(position);
            }

            @Override
            public int getCount() {
                return mAssetFragmentsArray.size();
            }
        });

        //mAnimateStickerTypeTab 添加tab切换的监听事件
        mAnimateStickerTypeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //当前选中的tab的位置，切换到相应的fragment
                mCurTabPage = tab.getPosition();
                mViewPager.setCurrentItem(mCurTabPage);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initAnimateStickerFragment() {
        mStickerListFragment = new AnimateStickerListFragment();
        mStickerListFragment.setAnimateStickerClickerListener(new AnimateStickerListFragment.AnimateStickerClickerListener() {
            @Override
            public void onFragmentLoadFinish() {
                mStickerListFragment.setCustomStickerButtonVisible(View.GONE);
                mStickerListFragment.setAssetInfolist(mDatas);
            }

            @Override
            public void onItemClick(View view, int pos) {
                selectPos = pos;
                DownResponse.DataBean dataBean = mDatas.get(pos);
                if (dataBean.isInstall()) {
                    applyAnimateSticker(selectPos);
                } else {
                    dataBean.setOnLoad(true);
                    //stickerAdapter.notifyDataSetChanged();
                    mStickerListFragment.setAssetInfolist(mDatas);
                    mPresenter.downSdk(dataBean, NvAsset.ASSET_ANIMATED_STICKER);
                }

            }

            @Override
            public void onAddCustomSticker() {

            }
        });

        mAssetFragmentsArray.add(mStickerListFragment);
        mCustomStickerListFragment = new AnimateStickerListFragment();
        mCustomStickerListFragment.setAnimateStickerClickerListener(new AnimateStickerListFragment.AnimateStickerClickerListener() {
            @Override
            public void onFragmentLoadFinish() {
                mCustomStickerListFragment.setCustomStickerButtonVisible(View.VISIBLE);
                mCustomStickerListFragment.setAssetInfolist(mDatas);
            }

            @Override
            public void onItemClick(View view, int pos) {
                applyAnimateSticker(pos);
            }

            @Override
            public void onAddCustomSticker() {

            }
        });

        mAssetFragmentsArray.add(mCustomStickerListFragment);
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mStickerAssetFinish.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoFragment.seekTimeline(mInPoint, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
                    }
                }, 100);
            }
        });

        mVideoFragment.setTimeline(mTimeline);
        //设置贴纸模式
        mVideoFragment.setEditMode(Constants.EDIT_MODE_STICKER);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineManager.getInstance().getCurrenTimeline().getTimeData().getMakeRatio());
        bundle.putBoolean("playBarVisible", false);
        mVideoFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.spaceLayout, mVideoFragment)
                .commit();
    }

    private void applyAnimateSticker(int pos) {
        //取消其他页贴纸选中
        if (mPrevTabPage != mCurTabPage) {
            mAssetFragmentsArray.get(mPrevTabPage).setSelectedPos(-1);
            mAssetFragmentsArray.get(mPrevTabPage).notifyDataSetChanged();
        }
        isAnimateStickerUuidItemClick = true;
        mVideoFragment.setDrawRectVisible(View.GONE);
        long startTime = mInPoint;
        long endTime = mInPoint + mStickerDuration;
        if (mCurAddAnimateSticker != null
                && mPrevTabPage == mCurTabPage
                && mCurSelectedPos == pos) {
            mVideoFragment.playVideo(startTime, endTime);
            return;
        }
        mPrevTabPage = mCurTabPage;
        mCurSelectedPos = pos;
        if (mCurAddAnimateSticker != null) {
            int zVal = (int) mCurAddAnimateSticker.getZValue();
            int index = getAnimateStickerIndex(zVal);
            if (index >= 0)
                mStickerDataListClone.remove(index);
            mTimeline.removeAnimatedSticker(mCurAddAnimateSticker);
            mCurAddAnimateSticker = null;
            mVideoFragment.setCurAnimateSticker(mCurAddAnimateSticker);
            mVideoFragment.changeStickerRectVisible();
        }

        //添加贴纸
        float zStickerVal = getCurAnimateStickerZVal();
        mCurAddAnimateSticker = mTimeline.addAnimatedSticker(mInPoint, mStickerDuration, mDatas.get(pos).getThemeId());
        if (mCurAddAnimateSticker != null)
            mCurAddAnimateSticker.setZValue(zStickerVal);
        mVideoFragment.playVideo(startTime, endTime);
        //保存数据
        StickerInfo stickerInfo = saveStickerInfo(mCurAddAnimateSticker);
        if (stickerInfo != null)
            mStickerDataListClone.add(stickerInfo);
    }

    private StickerInfo saveStickerInfo(NvsTimelineAnimatedSticker animatedSticker) {
        if (animatedSticker == null)
            return null;
        StickerInfo stickerInfo = new StickerInfo();
        stickerInfo.setIndex(TimelineManager.getInstance().getBranch());
        stickerInfo.setInPoint(mCurAddAnimateSticker.getInPoint());
        stickerInfo.setMasterInPoint(mCurAddAnimateSticker.getInPoint() + TimelineManager.getInstance().getCurrentPreviousAllTime());
        stickerInfo.setDuration(mStickerDuration);

        stickerInfo.setChangeTrimIn(mCurAddAnimateSticker.getInPoint());
        stickerInfo.setChangeTrimOut(mCurAddAnimateSticker.getInPoint() + mStickerDuration);
        stickerInfo.setHorizFlip(mCurAddAnimateSticker.getHorizontalFlip());
        stickerInfo.setTranslation(mCurAddAnimateSticker.getTranslation());
        stickerInfo.setId(mCurAddAnimateSticker.getAnimatedStickerPackageId());
        int zVal = (int) animatedSticker.getZValue();
        stickerInfo.setAnimateStickerZVal(zVal);
        return stickerInfo;
    }

    private float getCurAnimateStickerZVal() {
        float zVal = 0.0f;
        NvsTimelineAnimatedSticker animatedSticker = mTimeline.getFirstAnimatedSticker();
        while (animatedSticker != null) {
            float tmpZVal = animatedSticker.getZValue();
            if (tmpZVal > zVal)
                zVal = tmpZVal;
            animatedSticker = mTimeline.getNextAnimatedSticker(animatedSticker);
        }
        zVal += 1.0;
        return zVal;
    }

    private int getAnimateStickerIndex(int curZValue) {
        int index = -1;
        int count = mStickerDataListClone.size();
        for (int i = 0; i < count; ++i) {
            int zVal = mStickerDataListClone.get(i).getAnimateStickerZVal();
            if (curZValue == zVal) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void updateStickerBoundingRect() {
        mVideoFragment.setCurAnimateSticker(mCurAddAnimateSticker);
        mVideoFragment.updateAnimateStickerCoordinate(mCurAddAnimateSticker);
        updateStickerMuteVisible();
        mVideoFragment.changeStickerRectVisible();
    }

    private void seekTimeline() {
        mVideoFragment.seekTimeline(mInPoint, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
    }

    private void updateStickerMuteVisible() {
        if (mCurAddAnimateSticker != null) {
            boolean hasAudio = mCurAddAnimateSticker.hasAudio();
            mVideoFragment.setMuteVisible(hasAudio);
            if (hasAudio) {
                float leftVolume = (int) mCurAddAnimateSticker.getVolumeGain().leftVolume;
                mVideoFragment.setStickerMuteIndex(leftVolume > 0 ? 0 : 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoreDownload.setClickable(true);

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("AnimateStickerAssetActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("AnimateStickerAssetActivity");
            MobclickAgent.onPause(this);
        }
    }
}