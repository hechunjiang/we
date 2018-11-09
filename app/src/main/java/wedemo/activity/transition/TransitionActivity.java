package wedemo.activity.transition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.sven.huinews.international.R;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wedemo.MessageEvent;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.base.BaseActivity;
import wedemo.fragment.VideoFragment;
import wedemo.shot.bean.FilterItem;
import wedemo.utils.AppManager;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.TimelineUtil;
import wedemo.utils.Util;
import wedemo.utils.asset.NvAsset;
import wedemo.utils.asset.NvAssetManager;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.TimelineData;
import wedemo.utils.dataInfo.TransitionInfo;


public class TransitionActivity extends BaseActivity {
    private static final String TAG = "TransitionActivity";
    private static final int TRANSITIONREQUESTLIST = 105;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private NvsVideoTrack mVideoTrack;
    private VideoFragment mVideoFragment;
    private CustomTitleBar mTitleBar;
    private LinearLayout mBottomLayout;

    private RecyclerView mThumbRecyclerView;
    private RecyclerView mTransitionRecyclerView;
    private RelativeLayout mFinishLayout;
    private RelativeLayout mMoreDownload;

    private TransitionAdapter mTransitionAdapter;
    private ThumbAdapter mThumbAdapter;

    private List<String> mFileData = new ArrayList<>();
    private ArrayList<FilterItem> mFilterList = new ArrayList<>();
    private TransitionInfo mTransitionInfo;

    private int mAssetType = NvAsset.ASSET_VIDEO_TRANSITION;
    private NvAssetManager mAssetManager;
    private CustomTimeLine customTimeLine;

    private CheckBox rb_transition_apply_current;
    private int applyType = 0;

    @Override
    public int getLayoutId() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_transition_new;
    }

    @Override
    public void initView() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mThumbRecyclerView = (RecyclerView) findViewById(R.id.thumbRecyclerView);
        mTransitionRecyclerView = (RecyclerView) findViewById(R.id.transitionRecyclerView);
        mFinishLayout = (RelativeLayout) findViewById(R.id.finishLayout);
        mMoreDownload = (RelativeLayout) findViewById(R.id.download_more_btn);
        rb_transition_apply_current = findViewById(R.id.rb_transition_apply_current);

        mTitleBar.setRightText(getString(R.string.save));
    }


    @Override
    public void initObject() {
        customTimeLine = CustomTimelineUtil.createcCopyMasterTimeline();
        mTimeline = customTimeLine.getTimeline();

        mTransitionInfo = customTimeLine.getTimeData().cloneTransitionData();
        if (mTransitionInfo == null)
            mTransitionInfo = new TransitionInfo();


        if (mTimeline != null) {
            mVideoTrack = mTimeline.getVideoTrackByIndex(0);
        }
        mAssetManager = NvAssetManager.sharedInstance(this);
        mAssetManager.searchLocalAssets(mAssetType);
        String bundlePath = "transition";
        mAssetManager.searchReservedAssets(mAssetType, bundlePath);

        initVideoFragment();
        initTransitionDataList();
        initThumbRecyclerView();
        initTransitionRecyclerView();
    }

    @Override
    public void initEvents() {

        rb_transition_apply_current.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    applyType = 1;
                } else {
                    applyType = 0;
                }
            }
        });


        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
                finish();
            }

            @Override
            public void OnNextTextClick() {
                TimelineData.instance().setTransitionData(mTransitionInfo);
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
                EventBus.getDefault().post(new MessageEvent("transition"));
                finish();
            }

        });

//        mFinishLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TimelineData.instance().setTransitionData(mTransitionInfo);
//                removeTimeline();
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                AppManager.getInstance().finishActivity();
//            }
//        });
        mMoreDownload.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        if (id == R.id.download_more_btn) {
            mMoreDownload.setClickable(false);
            // AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), TransitionDownloadActivity.class, null,TRANSITIONREQUESTLIST);

        } else {
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case TRANSITIONREQUESTLIST:
                initTransitionDataList();
                mTransitionAdapter.setFilterList(mFilterList);
                mTransitionAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putBoolean("playBarVisible", true);
        bundle.putInt("ratio", customTimeLine.getTimeData().getMakeRatio());
        mVideoFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.video_layout, mVideoFragment)
                .commit();
    }

    private void initThumbRecyclerView() {
        if (mVideoTrack == null) {
            return;
        }

        for (int i = 0; i < mVideoTrack.getClipCount(); i++) {
            mFileData.add(mVideoTrack.getClipByIndex(i).getFilePath());
        }

        int selectedFilterPos = getSelectedFilterPos();
        FilterItem filterItem = null;
        if (selectedFilterPos >= 0) {
            filterItem = mFilterList.get(selectedFilterPos);
        }

        mThumbAdapter = new ThumbAdapter(this, mFileData, mTransitionInfo.getItemMap());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mThumbRecyclerView.setLayoutManager(linearLayoutManager);
        mThumbRecyclerView.setAdapter(mThumbAdapter);
        mThumbAdapter.setOnItemClickListener(new ThumbAdapter.OnItemClickListener() {
            @Override
            public void onThumbItemClick(View view, int position) {
                if (mVideoTrack == null)
                    return;

                int videoCount = mVideoTrack.getClipCount();
                if (videoCount < position)
                    return;

                NvsVideoClip clip = mVideoTrack.getClipByIndex(position);
                if (clip == null)
                    return;

                long inPoint = clip.getInPoint();
                mVideoFragment.playVideo(inPoint, -1);
            }

            @Override
            public void onTransitionItemClick(View view, int position) {
                if (mVideoTrack == null)
                    return;

                playTransition(position);
            }
        });
    }

    private void initTransitionDataList() {
//        int[] resImages = {
//                R.mipmap.icon_transition_fade,R.mipmap.icon_transition_turning,R.mipmap.icon_transition_swap,R.mipmap.icon_transition_stretch_in,
//                R.mipmap.icon_transition_page_curl,R.mipmap.icon_transition_lens_flare,R.mipmap.icon_transition_star,R.mipmap.icon_transition_dip_to_black,
//                R.mipmap.icon_transition_dip_to_white,R.mipmap.icon_transition_push_to_right,R.mipmap.icon_transition_push_to_left,R.mipmap.icon_transition_upper_left_into
//        };

        mFilterList.clear();
        FilterItem filterItem = new FilterItem();
        filterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
        filterItem.setFilterName(getString(R.string.no_info));
        filterItem.setEnglishName(getString(R.string.no_info_eng));
        filterItem.setImageId(R.mipmap.no_data);
        mFilterList.add(filterItem);

//        List<String> builtinTransitionList = mStreamingContext.getAllBuiltinVideoTransitionNames();
//        for(int i = 0;i < builtinTransitionList.size();i++) {
//            String transitionName = builtinTransitionList.get(i);
//            FilterItem newFilterItem = new FilterItem();
//            newFilterItem.setFilterName(transitionName);
//            if(i < resImages.length) {
//                int imageId = resImages[i];
//                newFilterItem.setImageId(imageId);
//            }
//            newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
//            mFilterList.add(newFilterItem);
//        }

        ArrayList<NvAsset> transitionList = getLocalData();
        String bundlePath = "transition/info.txt";
        Util.getBundleFilterInfo(this, transitionList, bundlePath);

        int ratio = TimelineData.instance().getMakeRatio();
        for (NvAsset asset : transitionList) {
            if ((ratio & asset.aspectRatio) == 0)
                continue;

            FilterItem newFilterItem = new FilterItem();
            if (asset.isReserved) {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUNDLE);
            } else {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
            }
            newFilterItem.setFilterName(asset.name);
            newFilterItem.setPackageId(asset.uuid);
            newFilterItem.setImageUrl(asset.coverUrl);
            newFilterItem.setEnglishName(asset.englishName);
            mFilterList.add(newFilterItem);
        }

        int position = getSelectedFilterPos();
        if (mTransitionAdapter != null) {
            mTransitionAdapter.setSelectPos(position);
        }
    }

    private int getSelectedFilterPos() {
        if (mFilterList == null || mFilterList.size() == 0)
            return -1;

        if (mTransitionInfo != null) {
            HashMap<String, FilterItem> itemMap = mTransitionInfo.getItemMap();
            FilterItem filterItem = itemMap.get("0");//默认那第一个
            if (filterItem == null) {
                return 0;
            }
            String transitionName = filterItem.getFilterName();
            if (TextUtils.isEmpty(transitionName))
                return 0;

            for (int i = 0; i < mFilterList.size(); i++) {
                FilterItem newFilterItem = mFilterList.get(i);
                if (newFilterItem == null)
                    continue;

                int filterMode = newFilterItem.getFilterMode();
                String filterName;
                if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                    filterName = newFilterItem.getFilterName();
                } else {
                    filterName = newFilterItem.getFilterId();
                }

                if (transitionName.equals(filterName)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void initTransitionRecyclerView() {
        mTransitionAdapter = new TransitionAdapter(this);
        mTransitionAdapter.setFilterList(mFilterList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTransitionRecyclerView.setLayoutManager(linearLayoutManager);
        mTransitionRecyclerView.setAdapter(mTransitionAdapter);

        int selectedFilterPos = getSelectedFilterPos();
        mTransitionAdapter.setSelectPos(selectedFilterPos);

        mTransitionAdapter.setOnItemClickListener(new TransitionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (applyType == 0) {
                    if (position == 0) {
                        int selectPos = mThumbAdapter.getSelectPos();
                        FilterItem filterItem = new FilterItem();

                        filterItem.setFilterId(null);
                        filterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
                        filterItem.setFilterName(getString(R.string.no_info));
                        filterItem.setImageId(R.mipmap.no_data);
                        //filterItem.setImageId();
                        mTransitionInfo.setFilterItem(selectPos + "", filterItem);
                    } else {
                        if (mFilterList.size() < position) {
                            return;
                        }
                        FilterItem filterItem = mFilterList.get(position);
                        int filterMode = filterItem.getFilterMode();
                        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {

                            FilterItem f = new FilterItem();
                            f.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
                            f.setFilterId(filterItem.getFilterName());
                            f.setImageId(filterItem.getImageId());
                            f.setImageUrl(filterItem.getImageUrl());

                            mTransitionInfo.setFilterItem(mThumbAdapter.getSelectPos() + "", f);

                        } else if (filterMode == FilterItem.FILTERMODE_BUNDLE) {

                            FilterItem f = new FilterItem();
                            f.setFilterMode(FilterItem.FILTERMODE_BUNDLE);
                            f.setPackageId(filterItem.getPackageId());
                            f.setImageId(filterItem.getImageId());
                            f.setImageUrl(filterItem.getImageUrl());

                            mTransitionInfo.setFilterItem(mThumbAdapter.getSelectPos() + "", f);

                        } else {
                            FilterItem f = new FilterItem();
                            f.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
                            f.setPackageId(filterItem.getPackageId());
                            f.setImageId(filterItem.getImageId());
                            f.setImageUrl(filterItem.getImageUrl());

                            mTransitionInfo.setFilterItem(mThumbAdapter.getSelectPos() + "", f);
                        }
                    }
                } else if (applyType == 1) {
                    for (int i = 0; i < mFileData.size() - 1; i++) {
                        if (position == 0) {
                            FilterItem filterItem = new FilterItem();
                            filterItem.setFilterId(null);
                            filterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
                            filterItem.setFilterName(getString(R.string.no_info));
                            filterItem.setImageId(R.mipmap.no_data);
                            mTransitionInfo.setFilterItem(i + "", filterItem);
                        } else {
                            if (mFilterList.size() < position) {
                                return;
                            }
                            FilterItem filterItem = mFilterList.get(position);
                            int filterMode = filterItem.getFilterMode();
                            if (filterMode == FilterItem.FILTERMODE_BUILTIN) {

                                FilterItem f = new FilterItem();
                                f.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
                                f.setFilterId(filterItem.getFilterName());
                                f.setImageId(filterItem.getImageId());
                                f.setImageUrl(filterItem.getImageUrl());

                                mTransitionInfo.setFilterItem(i + "", f);

                            } else if (filterMode == FilterItem.FILTERMODE_BUNDLE) {

                                FilterItem f = new FilterItem();
                                f.setFilterMode(FilterItem.FILTERMODE_BUNDLE);
                                f.setPackageId(filterItem.getPackageId());
                                f.setImageId(filterItem.getImageId());
                                f.setImageUrl(filterItem.getImageUrl());

                                mTransitionInfo.setFilterItem(i + "", f);

                            } else {
                                FilterItem f = new FilterItem();
                                f.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
                                f.setPackageId(filterItem.getPackageId());
                                f.setImageId(filterItem.getImageId());
                                f.setImageUrl(filterItem.getImageUrl());

                                mTransitionInfo.setFilterItem(i + "", f);
                            }
                        }
                    }
                }

                CustomTimelineUtil.setTransition(mTimeline, mTransitionInfo);
                if (mThumbAdapter != null) {
                    playTransition(mThumbAdapter.getSelectPos());
                }

                mThumbAdapter.setTransitionFilterItem(mTransitionInfo.getItemMap());
                mThumbAdapter.notifyDataSetChanged();
            }

            @Override
            public void onResetTransition(FilterItem filterItem) {

            }

            @Override
            public void onSameItemClick() {
                if (mThumbAdapter != null) {
                    playTransition(mThumbAdapter.getSelectPos());
                }
            }
        });
    }

    private ArrayList<NvAsset> getBundleData() {
        return mAssetManager.getReservedAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    private ArrayList<NvAsset> getLocalData() {
        return mAssetManager.getUsableAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        super.onBackPressed();
        AppManager.getInstance().finishActivity();
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
    }

    private void playSingleTransition(int index) {
        int videoCount = mVideoTrack.getClipCount();
        if (videoCount < index + 1)
            return;

        NvsVideoClip clip = mVideoTrack.getClipByIndex(index);
        if (clip == null)
            return;

        long inPointBefore = clip.getInPoint();
        long outPointBefore = clip.getOutPoint();

        NvsVideoClip nextClip = mVideoTrack.getClipByIndex(index + 1);
        if (nextClip == null)
            return;

        long inPointNext = nextClip.getInPoint();
        long outPointNext = nextClip.getOutPoint();
        outPointBefore -= TimelineUtil.TIME_BASE;
        inPointNext += TimelineUtil.TIME_BASE;
        if (outPointBefore < inPointBefore) {
            outPointBefore = inPointBefore;
        }
        if (inPointNext > outPointNext) {
            inPointNext = outPointNext;
        }

        mVideoFragment.playVideo(outPointBefore, inPointNext);
    }

    private void playTransition(int index) {
        int videoCount = mVideoTrack.getClipCount();
        if (videoCount < index + 1)
            return;

        NvsVideoClip clip = mVideoTrack.getClipByIndex(index);
        if (clip == null)
            return;

        long inPointBefore = clip.getInPoint();
        long outPointBefore = clip.getOutPoint();

        NvsVideoClip nextClip = mVideoTrack.getClipByIndex(index + 1);
        if (nextClip == null)
            return;

        long inPointNext = nextClip.getInPoint();
        long outPointNext = nextClip.getOutPoint();
        outPointBefore -= TimelineUtil.TIME_BASE;
        inPointNext += TimelineUtil.TIME_BASE;
        if (outPointBefore < inPointBefore) {
            outPointBefore = inPointBefore;
        }
        if (inPointNext > outPointNext) {
            inPointNext = outPointNext;
        }

        mVideoFragment.playVideo(outPointBefore, inPointNext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoreDownload.setClickable(true);

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("TransitionActivity");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("TransitionActivity");
            MobclickAgent.onPause(this);
        }
    }
}
