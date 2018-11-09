package wedemo.activity.Caption;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineCaption;
import com.sven.huinews.international.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wedemo.activity.data.BackupData;
import wedemo.activity.data.CaptionColorInfo;
import wedemo.activity.data.CaptionFontInfo;
import wedemo.activity.view.CustomTitleBar;
import wedemo.activity.view.CustomViewPager;
import wedemo.activity.view.InputDialog;
import wedemo.base.BaseActivity;
import wedemo.fragment.VideoFragment;
import wedemo.utils.AppManager;
import wedemo.utils.ColorUtil;
import wedemo.utils.Constants;
import wedemo.utils.TimelineManager;
import wedemo.utils.TimelineUtil;
import wedemo.utils.Util;
import wedemo.utils.asset.NvAsset;
import wedemo.utils.asset.NvAssetManager;
import wedemo.utils.dataInfo.CaptionInfo;
import wedemo.utils.dataInfo.TimelineData;


public class CaptionStyleActivity extends BaseActivity {
    private static final String TAG = "CaptionStyleActivity";
    private static final int CAPTIONSTYLEREQUESTLIST = 103;
    private static final int VIDEOPLAYTOEOF = 105;
    private static final int CAPTION_ALIGNLEFT = 0;
    private static final int CAPTION_ALIGNHORIZCENTER = 1;
    private static final int CAPTION_ALIGNRIGHT = 2;
    private static final int CAPTION_ALIGNTOP = 3;
    private static final int CAPTION_ALIGNVERTCENTER = 4;
    private static final int CAPTION_ALIGNBOTTOM = 5;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;

    private TabLayout mCaptionStyleTab;
    private CustomViewPager mViewPager;
    private ImageView mCaptionAssetFinish;
    private VideoFragment mVideoFragment;
    private ArrayList<NvAsset> mTotalCaptionStyleList;//总的字幕样式列表
    private ArrayList<Fragment> mAssetFragmentsArray;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private NvAssetManager mAssetManager;
    private int mAssetType = NvAsset.ASSET_CAPTION_STYLE;
    private int mSelectedStylePos = 0;
    private int mSelectedColorPos = -1;
    private int mSelectedOutlinePos = 0;
    private int mSelectedFontPos = 0;
    NvsTimelineCaption mCurAddCaption = null;
    private int mAlignType = -1;
    private CaptionStyleFragment mCaptionStyleFragment;
    private CaptionColorFragment mCaptionColorFragment;
    private CaptionOutlineFragment mCaptionOutlineFragment;
    private CaptionFontFragment mCaptionFontFragment;
    private CaptionSizeFragment mCaptionSizeFragment;
    private CaptionPositionFragment mCaptionPositionFragment;
    private ArrayList<CaptionColorInfo> mCaptionColorList;
    private ArrayList<CaptionColorInfo> mCaptionOutlineColorList;
    private ArrayList<CaptionFontInfo> mCaptionFontList;
    private int mCaptionColorOpacityValue = 100;
    private int mCaptionOutlineWidthValue = 8;
    private int mCaptionOutlineOpacityValue = 100;
    private int mCaptionSizeValue = 72;
    ArrayList<CaptionInfo> mCaptionDataListClone;
    private int mCurCaptionZVal = 0;

    private boolean bIsStyleUuidApplyToAll = false;
    private boolean bIsCaptionColorApplyToAll = false;
    private boolean bIsOutlineApplyToAll = false;
    private boolean bIsFontApplyToAll = false;
    private boolean bIsSizeApplyToAll = false;
    private boolean bIsPositionApplyToAll = false;

    private boolean isCaptionStyleItemClick = false;
    private String[] mCaptionColors = {"#ffffffff", "#ff000000",
            "#ffd0021b", "#fff8e71c",
            "#ff05d109", "#ff02c9ff", "#ff9013fe"};
    private String[] mCaptionOutlineColors = {"#ffffffff", "#ff000000",
            "#ffd0021b", "#fff8e71c",
            "#ff05d109", "#ff02c9ff", "#ff9013fe"};

    boolean m_waitFlag = false;

    @Override
    public int getLayoutId() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_caption_style;
    }

    private CaptionStyleActivity.CaptionStyleHandler m_handler = new CaptionStyleActivity.CaptionStyleHandler(this);

    static class CaptionStyleHandler extends Handler {
        WeakReference<CaptionStyleActivity> mWeakReference;

        public CaptionStyleHandler(CaptionStyleActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CaptionStyleActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case VIDEOPLAYTOEOF:
                        activity.updateCaption();
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @Override
    public void initView() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mCaptionStyleTab = (TabLayout) findViewById(R.id.captionStyleTab);
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        mViewPager.setPagingEnabled(false);
        mCaptionAssetFinish = (ImageView) findViewById(R.id.captionAssetFinish);
    }


    @Override
    public void initEvents() {
        mCaptionAssetFinish.setOnClickListener(this);
        mVideoFragment.setCaptionTextEditListener(new VideoFragment.VideoCaptionTextEditListener() {
            @Override
            public void onCaptionTextEdit() {
                //字幕编辑
                new InputDialog(CaptionStyleActivity.this, R.style.dialog, new InputDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean ok) {
                        if (ok) {
                            InputDialog d = (InputDialog) dialog;
                            String userInputText = d.getUserInputText();
                            mCurAddCaption.setText(userInputText);
                            updateCaption();
                            int index = getCaptionIndex(mCurCaptionZVal);
                            if (index >= 0) {
                                mCaptionDataListClone.get(index).setText(userInputText);
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).show();
            }
        });
        mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
            @Override
            public void onAssetDelete() {
                mTimeline.removeCaption(mCurAddCaption);
                mCurAddCaption = null;
                mVideoFragment.setCurCaption(mCurAddCaption);
                mVideoFragment.changeCaptionRectVisible();
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0) {
                    mCaptionDataListClone.remove(index);
                    BackupData.instance().setCaptionData(mCaptionDataListClone);
                    removeTimeline();
                    Intent intent = new Intent();
                    intent.putExtra("isSelectCurCaption", false);
                    setResult(RESULT_OK, intent);
                    AppManager.getInstance().finishActivity();
                }
            }

            @Override
            public void onAssetSelected(PointF curPoint) {
            }

            @Override
            public void onAssetTranstion() {
                if (mCurAddCaption == null)
                    return;
                PointF captionTranslation = mCurAddCaption.getCaptionTranslation();
                //Log.e(TAG,"captionTranslation.x = " + captionTranslation.x + "pointF.y =" + captionTranslation.y);
                int zVal = (int) mCurAddCaption.getZValue();
                int index = getCaptionIndex(zVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(captionTranslation);
            }

            @Override
            public void onAssetScale() {
                int zVal = (int) mCurAddCaption.getZValue();
                int index = getCaptionIndex(zVal);
                if (index >= 0) {
                    mCaptionDataListClone.get(index).setScaleFactorX(mCurAddCaption.getScaleX());
                    mCaptionDataListClone.get(index).setScaleFactorY(mCurAddCaption.getScaleY());
                    mCaptionDataListClone.get(index).setAnchor(mCurAddCaption.getAnchorPoint());
                    mCaptionDataListClone.get(index).setRotation(mCurAddCaption.getRotationZ());
                    mCaptionDataListClone.get(index).setCaptionSize(mCurAddCaption.getFontSize());
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
                }
            }

            @Override
            public void onAssetAlign(int alignVal) {
                int zVal = (int) mCurAddCaption.getZValue();
                int index = getCaptionIndex(zVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setAlignVal(alignVal);
            }

            @Override
            public void onAssetHorizFlip(boolean isHorizFlip) {

            }
        });
        mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                m_handler.sendEmptyMessage(VIDEOPLAYTOEOF);
            }

            @Override
            public void playStopped(NvsTimeline timeline) {
                if (isCaptionStyleItemClick)
                    return;
                updateDrawRect();
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
                isCaptionStyleItemClick = false;
            }
        });
    }


    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        if (id == R.id.captionAssetFinish) {
            applyToAllCaption();
            BackupData.instance().setCaptionData(mCaptionDataListClone);
            removeTimeline();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            intent.putExtra("isSelectCurCaption", true);
            AppManager.getInstance().finishActivity();

        } else {
        }
    }

    @Override
    public void initObject() {
        initAssetData();
        initVideoFragment();
        initTabLayout();
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
            case CAPTIONSTYLEREQUESTLIST:
                initCaptionStyleList();
                mCaptionStyleFragment.setAssetInfolist(mTotalCaptionStyleList);
                mSelectedStylePos = getCaptionStyleSelectedIndex();
                mCaptionStyleFragment.setSelectedPos(mSelectedStylePos);
                mCaptionStyleFragment.notifyDataSetChanged();
                updateCaption();
                break;
            default:
                break;
        }
    }

    private void applyToAllCaption() {
        int index = getCaptionIndex(mCurCaptionZVal);
        if (index < 0)
            return;
        int count = mCaptionDataListClone.size();
        CaptionInfo curCaptionInfo = mCaptionDataListClone.get(index);
        for (int i = 0; i < count; ++i) {
            if (i == index)
                continue;
            if (bIsStyleUuidApplyToAll) {
                mCaptionDataListClone.get(i).setCaptionStyleUuid(curCaptionInfo.getCaptionStyleUuid());
            }
            if (bIsCaptionColorApplyToAll) {
                mCaptionDataListClone.get(i).setCaptionColor(curCaptionInfo.getCaptionColor());
                mCaptionDataListClone.get(i).setCaptionColorAlpha(curCaptionInfo.getCaptionColorAlpha());
            }
            if (bIsOutlineApplyToAll) {
                mCaptionDataListClone.get(i).setHasOutline(curCaptionInfo.isHasOutline());
                mCaptionDataListClone.get(i).setOutlineColor(curCaptionInfo.getOutlineColor());
                mCaptionDataListClone.get(i).setOutlineColorAlpha(curCaptionInfo.getOutlineColorAlpha());
                mCaptionDataListClone.get(i).setOutlineWidth(curCaptionInfo.getOutlineWidth());
            }
            if (bIsFontApplyToAll) {
                mCaptionDataListClone.get(i).setCaptionFont(curCaptionInfo.getCaptionFont());
                mCaptionDataListClone.get(i).setBold(curCaptionInfo.isBold());
                mCaptionDataListClone.get(i).setItalic(curCaptionInfo.isItalic());
                mCaptionDataListClone.get(i).setShadow(curCaptionInfo.isShadow());
            }
            if (bIsSizeApplyToAll) {
                mCaptionDataListClone.get(i).setCaptionSize(curCaptionInfo.getCaptionSize());
            }
        }
        if (bIsPositionApplyToAll)
            updateCaptionPosition();
    }

    private void updateCaptionPosition() {
        NvsTimelineCaption caption = mTimeline.getFirstCaption();
        while (caption != null) {
            int zVal = (int) caption.getZValue();
            if (mCurCaptionZVal == zVal) {
                caption = mTimeline.getNextCaption(caption);
                continue;
            }
            List<PointF> list = caption.getBoundingRectangleVertices();
            if (list == null || list.size() < 4) {
                caption = mTimeline.getNextCaption(caption);
                continue;
            }

            int index = getCaptionIndex(zVal);
            switch (mAlignType) {
                case CAPTION_ALIGNLEFT://左对齐
                    Collections.sort(list, new Util.PointXComparator());
                    float xLeftOffset = -(mTimeline.getVideoRes().imageWidth / 2 + list.get(0).x);
                    caption.translateCaption(new PointF(xLeftOffset, 0));
                    break;
                case CAPTION_ALIGNHORIZCENTER://水平居中
                    Collections.sort(list, new Util.PointXComparator());
                    float xHorizCenterOffset = -((list.get(3).x - list.get(0).x) / 2 + list.get(0).x);
                    caption.translateCaption(new PointF(xHorizCenterOffset, 0));
                    break;
                case CAPTION_ALIGNRIGHT://右对齐
                    Collections.sort(list, new Util.PointXComparator());
                    float xRightOffset = mTimeline.getVideoRes().imageWidth / 2 - list.get(3).x;
                    caption.translateCaption(new PointF(xRightOffset, 0));
                    break;
                case CAPTION_ALIGNTOP://上对齐
                    Collections.sort(list, new Util.PointYComparator());
                    float yTopdis = list.get(3).y - list.get(0).y;
                    float yTopOffset = mTimeline.getVideoRes().imageHeight / 2 - list.get(0).y - yTopdis;
                    caption.translateCaption(new PointF(0, yTopOffset));
                    break;
                case CAPTION_ALIGNVERTCENTER://垂直居中
                    Collections.sort(list, new Util.PointYComparator());
                    float yVertCenterOffset = -((list.get(3).y - list.get(0).y) / 2 + list.get(0).y);
                    caption.translateCaption(new PointF(0, yVertCenterOffset));
                    break;
                case CAPTION_ALIGNBOTTOM://底部对齐
                    Collections.sort(list, new Util.PointYComparator());
                    float yBottomdis = list.get(3).y - list.get(0).y;
                    float yBottomOffset = -(mTimeline.getVideoRes().imageHeight / 2 + list.get(3).y - yBottomdis);
                    caption.translateCaption(new PointF(0, yBottomOffset));
                    break;
                default:
                    break;
            }
            if (index >= 0)
                mCaptionDataListClone.get(index).setTranslation(caption.getCaptionTranslation());
            caption = mTimeline.getNextCaption(caption);
        }
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
        m_handler.removeCallbacksAndMessages(null);
    }

    private void initTabLayout() {
        String[] assetName = getResources().getStringArray(R.array.captionEdit);
        for (int i = 0; i < assetName.length; i++) {
            mCaptionStyleTab.addTab(mCaptionStyleTab.newTab().setText(assetName[i]));
        }
        initCaptionTabFragment();
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
        mCaptionStyleTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //当前选中的tab的位置，切换到相应的fragment
                int nowPosition = tab.getPosition();
                mViewPager.setCurrentItem(nowPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initCaptionTabFragment() {
        mCaptionStyleFragment = initCaptionStyleFragment();
        mAssetFragmentsArray.add(mCaptionStyleFragment);
        mCaptionColorFragment = initCaptionColorFragment();
        mAssetFragmentsArray.add(mCaptionColorFragment);
        mCaptionOutlineFragment = initCaptionOutlineFragment();
        mAssetFragmentsArray.add(mCaptionOutlineFragment);
        mCaptionFontFragment = initCaptionFontFragment();
        mAssetFragmentsArray.add(mCaptionFontFragment);
        mCaptionSizeFragment = initCaptionSizeFragment();
        mAssetFragmentsArray.add(mCaptionSizeFragment);
        mCaptionPositionFragment = initCaptionPositionFragment();
        mAssetFragmentsArray.add(mCaptionPositionFragment);
    }

    private void initAssetData() {
        mTimeline = TimelineUtil.createTimeline(TimelineManager.getInstance().getBranch());
        if (mTimeline == null)
            return;
        mCurCaptionZVal = BackupData.instance().getCaptionZVal();
        mCaptionDataListClone = BackupData.instance().cloneCaptionData();

        // TimelineUtil.setAllCaption(mTimeline,TimelineManager.getInstance().getMasterTimeline(),mCaptionDataListClone,TimelineManager.getInstance().getBranch());

        mTotalCaptionStyleList = new ArrayList<>();
        mAssetFragmentsArray = new ArrayList<>();
        mCaptionColorList = new ArrayList<>();
        mCaptionOutlineColorList = new ArrayList<>();
        mCaptionFontList = new ArrayList<>();
        //
        mAssetManager = NvAssetManager.sharedInstance(this);

        mAssetManager.searchLocalAssets(mAssetType);
        String bundlePath = "captionstyle";
        mAssetManager.searchReservedAssets(mAssetType, bundlePath);
        initCaptionStyleList();
        initCaptionColorList();
        initCaptionOutlineColorList();
        initCaptionFontList();
    }

    private void initCaptionStyleList() {
        mTotalCaptionStyleList.clear();
        ArrayList<NvAsset> usableAsset = getAssetsDataList();
        if (usableAsset != null && usableAsset.size() > 0) {
            String bundlePath = "captionstyle/info.txt";
            Util.getBundleFilterInfo(this, usableAsset, bundlePath);
            for (NvAsset asset : usableAsset) {
                if (asset.isReserved()) {
                    String coverPath = "file:///android_asset/captionstyle/";
                    coverPath += asset.uuid;
                    coverPath += ".png";
                    asset.coverUrl = coverPath;
                }
            }
        }
        mTotalCaptionStyleList = usableAsset;
        NvAsset asset = new NvAsset();
        asset.name = "无";
        mTotalCaptionStyleList.add(0, asset);
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                long cutPos = BackupData.instance().getCurSeekTimelinePos();
                mVideoFragment.seekTimeline(cutPos, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                selectCaption();
                mCaptionAssetFinish.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoFragment.setCurCaption(mCurAddCaption);
                        mVideoFragment.updateCaptionCoordinate(mCurAddCaption);
                        mVideoFragment.changeCaptionRectVisible();
                        if (mCurAddCaption != null) {
                            int alignVal = mCurAddCaption.getTextAlignment();
                            mVideoFragment.setAlignIndex(alignVal);
                        }
                    }
                }, 100);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        //设置贴纸模式
        mVideoFragment.setEditMode(Constants.EDIT_MODE_CAPTION);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("playBarVisible", false);
        mVideoFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.video_layout, mVideoFragment)
                .commit();
    }

    //获取下载到手机缓存路径下的素材，包括assets路径下自带的素材
    private ArrayList<NvAsset> getAssetsDataList() {
        return mAssetManager.getUsableAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    private void initCaptionColorList() {
        for (int index = 0; index < mCaptionColors.length; ++index) {
            mCaptionColorList.add(new CaptionColorInfo(mCaptionColors[index], false));
        }
    }

    private void initCaptionOutlineColorList() {
        for (int index = 0; index < mCaptionOutlineColors.length; ++index) {
            mCaptionOutlineColorList.add(new CaptionColorInfo(mCaptionOutlineColors[index], false));
        }
    }

    private void initCaptionFontList() {
        mCaptionFontList.add(new CaptionFontInfo("", R.mipmap.no, false));
        mCaptionFontList.add(new CaptionFontInfo("assets:/font/font.ttf", R.mipmap.font, false));
    }

    private CaptionStyleFragment initCaptionStyleFragment() {
        CaptionStyleFragment captionStyleFragment = new CaptionStyleFragment();
        captionStyleFragment.setAssetInfolist(mTotalCaptionStyleList);
        captionStyleFragment.setCaptionStyleListener(new CaptionStyleFragment.OnCaptionStyleListener() {
            @Override
            public void onFragmentLoadFinished() {
                mCaptionStyleFragment.applyToAllCaption(false);
                mSelectedStylePos = getCaptionStyleSelectedIndex();
                mCaptionStyleFragment.setSelectedPos(mSelectedStylePos);
                mCaptionStyleFragment.notifyDataSetChanged();
            }

            @Override
            public void OnDownloadCaptionStyle() {
                if (m_waitFlag)
                    return;
                mVideoFragment.stopEngine();
                // AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), CaptionStyleDownloadActivity.class, null,CAPTIONSTYLEREQUESTLIST);
                m_waitFlag = true;
            }

            @Override
            public void onItemClick(int pos) {
                if (mCurAddCaption == null)
                    return;
                isCaptionStyleItemClick = true;
                long startTime = mCurAddCaption.getInPoint();
                long endTime = mCurAddCaption.getOutPoint();
                mVideoFragment.setDrawRectVisible(View.GONE);
                if (mSelectedStylePos == pos) {
                    mVideoFragment.playVideo(startTime, endTime);
                    return;
                }

                mSelectedStylePos = pos;
                //设置字幕样式
                mCurAddCaption.applyCaptionStyle(mTotalCaptionStyleList.get(pos).uuid);
                mVideoFragment.playVideo(startTime, endTime);
                float captionSize = mCurAddCaption.getFontSize();
                float scaleX = mCurAddCaption.getScaleX();
                float scaleY = mCurAddCaption.getScaleY();
                PointF pointF = mCurAddCaption.getCaptionTranslation();
                int category = mCurAddCaption.getCategory();
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0) {
                    mCaptionDataListClone.get(index).setCaptionStyleUuid(mTotalCaptionStyleList.get(pos).uuid);
                    mCaptionDataListClone.get(index).setTranslation(pointF);
                    mCaptionDataListClone.get(index).setCaptionSize(captionSize);
                    mCaptionDataListClone.get(index).setScaleFactorX(scaleX);
                    mCaptionDataListClone.get(index).setScaleFactorY(scaleY);
                    int curCategory = mCaptionDataListClone.get(index).getCaptionCategory();
                    if (curCategory != 2)
                        mCaptionDataListClone.get(index).setCaptionCategory(category);
                }
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsStyleUuidApplyToAll = isApplyToAll;
            }
        });
        return captionStyleFragment;
    }

    private CaptionColorFragment initCaptionColorFragment() {
        CaptionColorFragment captionColorFragment = new CaptionColorFragment();
        captionColorFragment.setCaptionColorInfolist(mCaptionColorList);
        captionColorFragment.setCaptionColorListener(new CaptionColorFragment.OnCaptionColorListener() {
            @Override
            public void onFragmentLoadFinished() {
                mCaptionColorFragment.applyToAllCaption(false);
                mSelectedColorPos = getCaptionColorSelectedIndex();
                if (mSelectedColorPos >= 0) {
                    mCaptionColorList.get(mSelectedColorPos).mSelected = true;
                    mCaptionColorFragment.setCaptionColorInfolist(mCaptionColorList);
                    mCaptionColorFragment.notifyDataSetChanged();
                }
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0) {
                    mCaptionColorOpacityValue = mCaptionDataListClone.get(index).getCaptionColorAlpha();
                    mCaptionColorFragment.updateCaptionOpacityValue(mCaptionColorOpacityValue);
                }
            }

            @Override
            public void onCaptionColor(int pos) {
                if (mCurAddCaption == null)
                    return;
                if (mSelectedColorPos == pos)
                    return;
                if (mSelectedColorPos >= 0)
                    mCaptionColorList.get(mSelectedColorPos).mSelected = false;
                mCaptionColorList.get(pos).mSelected = true;
                mCaptionColorFragment.notifyDataSetChanged();
                mSelectedColorPos = pos;
                mCaptionColorOpacityValue = 100;
                mCaptionColorFragment.updateCaptionOpacityValue(mCaptionColorOpacityValue);
                // 设置字体颜色
                NvsColor color = ColorUtil.colorStringtoNvsColor(mCaptionColorList.get(pos).mColorValue);
                mCurAddCaption.setTextColor(color);
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setCaptionColor(mCaptionColorList.get(pos).mColorValue);
                updateCaption();
            }

            @Override
            public void onCaptionOpacity(int progress) {
                if (mCurAddCaption == null)
                    return;

                // 设置字体的不透明度
                NvsColor curColor = mCurAddCaption.getTextColor();
                curColor.a = progress / 100.0f;
                String strColor = ColorUtil.nvsColorToHexString(curColor);
                mCurAddCaption.setTextColor(curColor);
                mCaptionColorOpacityValue = progress;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0) {
                    mCaptionDataListClone.get(index).setCaptionColor(strColor);
                    mCaptionDataListClone.get(index).setCaptionColorAlpha(progress);
                }

                updateCaption();
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsCaptionColorApplyToAll = isApplyToAll;
            }
        });
        return captionColorFragment;
    }

    private CaptionOutlineFragment initCaptionOutlineFragment() {
        CaptionOutlineFragment captionOutlineFragment = new CaptionOutlineFragment();
        captionOutlineFragment.setCaptionOutlineInfolist(mCaptionOutlineColorList);
        captionOutlineFragment.setCaptionOutlineListener(new CaptionOutlineFragment.OnCaptionOutlineListener() {
            @Override
            public void onFragmentLoadFinished() {
                mCaptionOutlineFragment.applyToAllCaption(false);
                mSelectedOutlinePos = getOutlineColorSelectedIndex();
                mCaptionOutlineColorList.get(mSelectedOutlinePos).mSelected = true;
                mCaptionOutlineFragment.setCaptionOutlineInfolist(mCaptionOutlineColorList);
                mCaptionOutlineFragment.notifyDataSetChanged();
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0) {
                    boolean isDrawOutline = mCaptionDataListClone.get(index).isHasOutline();
                    if (isDrawOutline) {
                        mCaptionOutlineWidthValue = (int) mCaptionDataListClone.get(index).getOutlineWidth();
                        mCaptionOutlineOpacityValue = mCaptionDataListClone.get(index).getOutlineColorAlpha();
                    }
                    mCaptionOutlineFragment.updateCaptionOutlineWidthValue(mCaptionOutlineWidthValue);
                    mCaptionOutlineFragment.updateCaptionOutlineOpacityValue(mCaptionOutlineOpacityValue);
                }
            }

            @Override
            public void onCaptionOutlineColor(int pos) {
                if (mCurAddCaption == null)
                    return;
                if (mSelectedOutlinePos == pos)
                    return;
                mCaptionOutlineColorList.get(mSelectedOutlinePos).mSelected = false;
                mCaptionOutlineColorList.get(pos).mSelected = true;
                mCaptionOutlineFragment.notifyDataSetChanged();
                mSelectedOutlinePos = pos;

                mCaptionOutlineOpacityValue = 100;
                int index = getCaptionIndex(mCurCaptionZVal);
                // 设置字体颜色
                if (pos == 0) {
                    mCurAddCaption.setDrawOutline(false);
                    mCaptionOutlineWidthValue = 0;
                    if (index >= 0)
                        mCaptionDataListClone.get(index).setHasOutline(false);

                } else {
                    mCaptionOutlineWidthValue = 8;
                    mCurAddCaption.setDrawOutline(true);//字幕描边
                    // 设置描边颜色
                    NvsColor color = ColorUtil.colorStringtoNvsColor(mCaptionOutlineColorList.get(pos).mColorValue);
                    mCurAddCaption.setOutlineColor(color);
                    mCurAddCaption.setOutlineWidth(mCaptionOutlineWidthValue); //字幕描边宽度
                    if (index >= 0) {
                        mCaptionDataListClone.get(index).setHasOutline(true);
                        mCaptionDataListClone.get(index).setOutlineColor(mCaptionOutlineColorList.get(pos).mColorValue);
                        mCaptionDataListClone.get(index).setOutlineWidth(mCaptionOutlineWidthValue);
                        mCaptionDataListClone.get(index).setOutlineColorAlpha(mCaptionOutlineOpacityValue);
                    }
                }
                mCaptionOutlineFragment.updateCaptionOutlineWidthValue(mCaptionOutlineWidthValue);
                mCaptionOutlineFragment.updateCaptionOutlineOpacityValue(mCaptionOutlineOpacityValue);
                updateCaption();
            }

            @Override
            public void onCaptionOutlineWidth(int width) {
                if (mCurAddCaption == null)
                    return;
                if (mSelectedOutlinePos == 0)
                    return;
                mCurAddCaption.setOutlineWidth(width); //字幕描边宽度
                mCaptionOutlineWidthValue = width;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setOutlineWidth(mCaptionOutlineWidthValue);
                updateCaption();
            }

            @Override
            public void onCaptionOutlineOpacity(int opacity) {
                if (mCurAddCaption == null)
                    return;
                if (mSelectedOutlinePos == 0)
                    return;
                // 设置描边的不透明度
                NvsColor curColor = mCurAddCaption.getOutlineColor();
                curColor.a = opacity / 100.0f;
                mCurAddCaption.setOutlineColor(curColor);
                mCaptionOutlineOpacityValue = opacity;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setOutlineColorAlpha(opacity);
                updateCaption();
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsOutlineApplyToAll = isApplyToAll;
            }
        });
        return captionOutlineFragment;
    }

    private CaptionFontFragment initCaptionFontFragment() {
        CaptionFontFragment captionFontFragment = new CaptionFontFragment();
        captionFontFragment.setFontInfolist(mCaptionFontList);
        captionFontFragment.setCaptionFontListener(new CaptionFontFragment.OnCaptionFontListener() {
            @Override
            public void onFragmentLoadFinished() {
                mCaptionFontFragment.applyToAllCaption(false);
                mSelectedFontPos = getCaptionFontSelectedIndex();
                mCaptionFontList.get(mSelectedFontPos).mSelected = true;
                mCaptionFontFragment.setFontInfolist(mCaptionFontList);
                mCaptionFontFragment.notifyDataSetChanged();
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0) {
                    mCaptionFontFragment.updateBoldButton(mCaptionDataListClone.get(index).isBold());
                    mCaptionFontFragment.updateItalicButton(mCaptionDataListClone.get(index).isItalic());
                    mCaptionFontFragment.updateShadowButton(mCaptionDataListClone.get(index).isShadow());
                }
            }

            @Override
            public void onItemClick(int pos) {
                if (mCurAddCaption == null)
                    return;
                if (mSelectedFontPos == pos)
                    return;
                mCaptionFontList.get(mSelectedFontPos).mSelected = false;
                mCaptionFontList.get(pos).mSelected = true;
                mCaptionFontFragment.notifyDataSetChanged();
                mSelectedFontPos = pos;
                mCurAddCaption.setFontByFilePath(mCaptionFontList.get(pos).mFontPath);

                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setCaptionFont(mCaptionFontList.get(pos).mFontPath);
                updateCaption();
            }

            @Override
            public void onBold() {
                if (mCurAddCaption == null)
                    return;
                boolean isBold = mCurAddCaption.getBold();
                isBold = !isBold;
                mCurAddCaption.setBold(isBold); //加粗
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setBold(isBold);
                updateCaption();
            }

            @Override
            public void onItalic() {
                if (mCurAddCaption == null)
                    return;
                boolean isItalic = mCurAddCaption.getItalic();
                isItalic = !isItalic;
                mCurAddCaption.setItalic(isItalic); // 斜体
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setItalic(isItalic);
                updateCaption();
            }

            @Override
            public void onShadow() {
                if (mCurAddCaption == null)
                    return;
                boolean isShadow = mCurAddCaption.getDrawShadow();
                isShadow = !isShadow;
                if (isShadow) {
                    PointF point = new PointF(7, -7);
                    NvsColor shadowColor = new NvsColor(0, 0, 0, 0.5f);
                    mCurAddCaption.setShadowOffset(point);  //字幕阴影偏移量
                    mCurAddCaption.setShadowColor(shadowColor); // 字幕阴影颜色
                }
                mCurAddCaption.setDrawShadow(isShadow);

                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setShadow(isShadow);
                updateCaption();
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsFontApplyToAll = isApplyToAll;
            }
        });
        return captionFontFragment;
    }

    private CaptionSizeFragment initCaptionSizeFragment() {
        CaptionSizeFragment captionSizeFragment = new CaptionSizeFragment();
        captionSizeFragment.setCaptionSizeListener(new CaptionSizeFragment.OnCaptionSizeListener() {
            @Override
            public void onFragmentLoadFinished() {
                mCaptionSizeFragment.applyToAllCaption(false);
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0) {
                    int captionSizeVal = (int) mCaptionDataListClone.get(index).getCaptionSize();
                    if (captionSizeVal >= 0)
                        mCaptionSizeValue = captionSizeVal;
                    mCaptionSizeFragment.updateCaptionSizeValue(mCaptionSizeValue);
                }
            }

            @Override
            public void OnCaptionSize(int size) {
                if (mCurAddCaption == null)
                    return;
                mCurAddCaption.setFontSize(size);
                mCaptionSizeValue = size;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setCaptionSize(size);
                updateCaption();
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsSizeApplyToAll = isApplyToAll;
            }
        });
        return captionSizeFragment;
    }

    private CaptionPositionFragment initCaptionPositionFragment() {
        CaptionPositionFragment captionPositionFragment = new CaptionPositionFragment();
        captionPositionFragment.setCaptionPostionListener(new CaptionPositionFragment.OnCaptionPositionListener() {
            @Override
            public void onFragmentLoadFinished() {
                mCaptionPositionFragment.applyToAllCaption(false);
            }

            @Override
            public void OnAlignLeft() {
                if (mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if (list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointXComparator());

                float xOffset = -(mTimeline.getVideoRes().imageWidth / 2 + list.get(0).x);
                mCurAddCaption.translateCaption(new PointF(xOffset, 0));
                mAlignType = CAPTION_ALIGNLEFT;
                updateCaption();
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignCenterHorizontal() {
                if (mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if (list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointXComparator());

                float xOffset = -((list.get(3).x - list.get(0).x) / 2 + list.get(0).x);
                mCurAddCaption.translateCaption(new PointF(xOffset, 0));
                updateCaption();
                mAlignType = CAPTION_ALIGNHORIZCENTER;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignRight() {
                if (mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if (list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointXComparator());

                float xOffset = mTimeline.getVideoRes().imageWidth / 2 - list.get(3).x;
                mCurAddCaption.translateCaption(new PointF(xOffset, 0));
                updateCaption();
                mAlignType = CAPTION_ALIGNRIGHT;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignTop() {
                if (mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if (list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointYComparator());
                float y_dis = list.get(3).y - list.get(0).y;

                float yOffset = mTimeline.getVideoRes().imageHeight / 2 - list.get(0).y - y_dis;
                mCurAddCaption.translateCaption(new PointF(0, yOffset));
                updateCaption();
                mAlignType = CAPTION_ALIGNTOP;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignCenterVertical() {
                if (mCurAddCaption == null)
                    return;
                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if (list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointYComparator());

                float yOffset = -((list.get(3).y - list.get(0).y) / 2 + list.get(0).y);
                mCurAddCaption.translateCaption(new PointF(0, yOffset));
                updateCaption();
                mAlignType = CAPTION_ALIGNVERTCENTER;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignBottom() {
                if (mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if (list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointYComparator());
                float y_dis = list.get(3).y - list.get(0).y;

                float yOffset = -(mTimeline.getVideoRes().imageHeight / 2 + list.get(3).y - y_dis);
                mCurAddCaption.translateCaption(new PointF(0, yOffset));
                updateCaption();
                mAlignType = CAPTION_ALIGNBOTTOM;
                int index = getCaptionIndex(mCurCaptionZVal);
                if (index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsPositionApplyToAll = isApplyToAll;
            }
        });
        return captionPositionFragment;
    }

    private int getCaptionStyleSelectedIndex() {
        int selectIndex = 0;
        if (mCurAddCaption != null) {
            String uuid = mCurAddCaption.getCaptionStylePackageId();
            for (int index = 0; index < mTotalCaptionStyleList.size(); ++index) {
                if (mTotalCaptionStyleList.get(index).uuid.compareTo(uuid) == 0) {
                    selectIndex = index;
                    break;
                }
            }
        }

        return selectIndex;
    }

    private int getCaptionColorSelectedIndex() {
        int selectedPos = -1;
        int captionIndex = getCaptionIndex(mCurCaptionZVal);
        if (captionIndex >= 0) {
            String captionColor = mCaptionDataListClone.get(captionIndex).getCaptionColor();
            for (int i = 0; i < mCaptionColorList.size(); ++i) {
                if (mCaptionColorList.get(i).mColorValue.compareTo(captionColor) == 0) {
                    selectedPos = i;
                    break;
                }
            }
        }
        return selectedPos;
    }

    private int getOutlineColorSelectedIndex() {
        int selectedPos = 0;
        int captionIndex = getCaptionIndex(mCurCaptionZVal);
        if (captionIndex >= 0) {
            String outlineColor = mCaptionDataListClone.get(captionIndex).getOutlineColor();
            for (int i = 0; i < mCaptionOutlineColorList.size(); ++i) {
                if (mCaptionOutlineColorList.get(i).mColorValue.compareTo(outlineColor) == 0) {
                    selectedPos = i;
                    break;
                }
            }
        }
        return selectedPos;
    }

    private int getCaptionFontSelectedIndex() {
        int selectedPos = 0;
        int captionIndex = getCaptionIndex(mCurCaptionZVal);
        if (captionIndex >= 0) {
            String captionFont = mCaptionDataListClone.get(captionIndex).getCaptionFont();
            for (int i = 0; i < mCaptionFontList.size(); ++i) {
                if (mCaptionFontList.get(i).mFontPath.compareTo(captionFont) == 0) {
                    selectedPos = i;
                    break;
                }
            }
        }
        return selectedPos;
    }

    private void updateCaption() {
        mVideoFragment.seekTimeline(mCurAddCaption.getInPoint(), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
        updateDrawRect();
    }

    private void updateDrawRect() {
        if (mCurAddCaption != null) {
            int alignVal = mCurAddCaption.getTextAlignment();
            mVideoFragment.setAlignIndex(alignVal);
        }
        mVideoFragment.updateCaptionCoordinate(mCurAddCaption);
        mVideoFragment.changeCaptionRectVisible();
    }

    private int getCaptionIndex(int curZValue) {
        int index = -1;
        int count = mCaptionDataListClone.size();
        for (int i = 0; i < count; ++i) {
            int zVal = mCaptionDataListClone.get(i).getCaptionZVal();
            if (curZValue == zVal) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void selectCaption() {
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineCaption> captionList = mTimeline.getCaptionsByTimelinePosition(curPos);
        int captionCount = captionList.size();
        for (int index = 0; index < captionCount; index++) {
            int tmpZVal = (int) captionList.get(index).getZValue();
            if (mCurCaptionZVal == tmpZVal) {
                mCurAddCaption = captionList.get(index);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_waitFlag = false;
    }

}
