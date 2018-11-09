package wedemo.activity.filter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.sven.huinews.international.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import wedemo.MessageEvent;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.adapter.FiltersAdapter;
import wedemo.base.BaseActivity;
import wedemo.fragment.VideoFragment;
import wedemo.shot.bean.FilterItem;
import wedemo.utils.AppManager;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.Util;
import wedemo.utils.asset.NvAsset;
import wedemo.utils.asset.NvAssetManager;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.TimelineData;
import wedemo.utils.dataInfo.VideoClipFxInfo;

public class FilterActivity extends BaseActivity {
    private CustomTitleBar title_bar;
    private RelativeLayout video_layout;
    private RelativeLayout bottom_layout;
    private RecyclerView fliter_rv;
    private TextView tv_scenery;
    private TextView tv_beauty;

    private VideoFragment mVideoFragment;
    private NvsStreamingContext mStreamingContext;
    private CustomTimeLine mCustomTimeline;
    private NvsTimeline mTimeline;
    private NvAssetManager mAssetManager;

    private ArrayList<FilterItem> mFilterItemArrayList = new ArrayList<>();
    private ArrayList<FilterItem> mFilterItemBeautyArrayList = new ArrayList<>();
    private FiltersAdapter mFiltersAdapter;

    private int filterSelect = 0; //当前选中滤镜分类
    private VideoClipFxInfo videoClipFx;

    @Override
    public int getLayoutId() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_filter;
    }

    @Override
    public void initView() {
        title_bar = findViewById(R.id.title_bar);
        video_layout = findViewById(R.id.video_layout);
        bottom_layout = findViewById(R.id.bottom_layout);

        tv_scenery = findViewById(R.id.tv_scenery);
        tv_beauty = findViewById(R.id.tv_beauty);

        fliter_rv = findViewById(R.id.fliter_rv);

        title_bar.setRightText(getString(R.string.save));
    }

    @Override
    public void initEvents() {
        title_bar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
                AppManager.getInstance().finishActivity();
                finish();
            }

            @Override
            public void OnNextTextClick() {
                mStreamingContext.stop();
                removeTimeline();
                TimelineManager.getInstance().getCurrenTimeline().getTimeData().setVideoClipFxData(videoClipFx);
                EventBus.getDefault().post(new MessageEvent("filter"));
                AppManager.getInstance().finishActivity();
            }
        });

        tv_scenery.setOnClickListener(this);
        tv_beauty.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == tv_scenery) {
            filterSelect = 0;
            mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
            mFiltersAdapter.setSelectPos(-1);

            mFiltersAdapter.notifyDataSetChanged();
            tv_beauty.setTextColor(ContextCompat.getColor(FilterActivity.this, R.color.a4a4));
            tv_scenery.setTextColor(ContextCompat.getColor(FilterActivity.this, R.color.bg_white));
        } else if (v == tv_beauty) {
            filterSelect = 1;
            mFiltersAdapter.setFilterDataList(mFilterItemBeautyArrayList);
            mFiltersAdapter.setSelectPos(-1);

            mFiltersAdapter.notifyDataSetChanged();
            tv_scenery.setTextColor(ContextCompat.getColor(FilterActivity.this, R.color.a4a4));
            tv_beauty.setTextColor(ContextCompat.getColor(FilterActivity.this, R.color.bg_white));
        }
    }

    @Override
    public void initObject() {
        mAssetManager = NvAssetManager.sharedInstance(this);
        mAssetManager.searchLocalAssets(NvAsset.ASSET_FILTER);

        String bundlePath = "filter";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_FILTER, bundlePath);

        bundlePath = "filterbeauty";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_FILTER_BEAUTY, bundlePath);

        initTimeline();
        initVideoFragment();

        initBeautyFilterList();
        initFilterList();
        initFilterView();

        //初始化时，移除所有添加的滤镜
        videoClipFx.setFxId(null);
        videoClipFx.setFxMode(FilterItem.FILTERMODE_PACKAGE);
        CustomTimelineUtil.buildTimelineSingleFilter(mTimeline, videoClipFx);
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void initTimeline() {
        mCustomTimeline = CustomTimelineUtil.createcCopyTimeline(TimelineManager.getInstance().getCurrenTimeline());
        if (mCustomTimeline == null)
            return;

        mTimeline = mCustomTimeline.getTimeline();
        videoClipFx = mCustomTimeline.getTimeData().getVideoClipFxData();
        if (videoClipFx == null) {
            videoClipFx = new VideoClipFxInfo();
        }
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), 0);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", title_bar.getLayoutParams().height);
        bundle.putInt("bottomHeight", bottom_layout.getLayoutParams().height);
        bundle.putBoolean("playBarVisible", true);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        mVideoFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.video_layout, mVideoFragment)
                .commit();
    }

    private void removeTimeline() {
        CustomTimelineUtil.removeTimeline(mCustomTimeline);
        mTimeline = null;
    }


    /**
     * 初始化美颜
     */
    private void initBeautyFilterList() {
        mFilterItemBeautyArrayList.clear();
        FilterItem filterItem = new FilterItem();
        filterItem.setFilterName(getString(R.string.no_info));
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
            mFilterItemBeautyArrayList.add(newFilterItem);
        }
    }

    /**
     * 初始化风景滤镜
     */
    private void initFilterList() {
        FilterItem filterItem = new FilterItem();
        filterItem.setFilterName(getString(R.string.no_info));
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
        mFiltersAdapter.setFilterDataList(mFilterItemArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        fliter_rv.setLayoutManager(linearLayoutManager);
        fliter_rv.setAdapter(mFiltersAdapter);
        mFiltersAdapter.setOnItemClickListener(new FiltersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FilterItem filterItem = null;
                if (filterSelect == 0) {
                    if (position < mFilterItemArrayList.size()) {
                        if (position == 0) {
                            videoClipFx.setFxId(null);
                            videoClipFx.setFxMode(FilterItem.FILTERMODE_PACKAGE);
                            CustomTimelineUtil.buildTimelineSingleFilter(mTimeline, videoClipFx);
                            mVideoFragment.playVideoButtonCilck();
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
                        if (position == 0) {
                            videoClipFx.setFxId(null);
                            videoClipFx.setFxMode(FilterItem.FILTERMODE_PACKAGE);
                            CustomTimelineUtil.buildTimelineSingleFilter(mTimeline, videoClipFx);
                            mVideoFragment.playVideoButtonCilck();
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

                CustomTimelineUtil.buildTimelineSingleFilter(mTimeline, videoClipFx);
                mVideoFragment.playVideoButtonCilck();
            }

            @Override
            public void onSameItemClick() {

            }
        });
    }

}
