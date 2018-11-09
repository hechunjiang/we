package wedemo.fragment;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.meicam.sdk.NvsVideoClip;
import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wedemo.TransitionActivity;
import wedemo.adapter.TransitionFileAdapter;
import wedemo.adapter.TransitionThemeAdapter;
import wedemo.base.BaseFragment;
import wedemo.shot.bean.FilterItem;
import wedemo.utils.Util;
import wedemo.utils.asset.NvAsset;
import wedemo.utils.asset.NvAssetManager;
import wedemo.utils.dataInfo.TimelineData;

public class TransitionFragment extends BaseFragment {
    protected String TAG = getClass().getSimpleName();
    protected View rootView;

    private NvAssetManager mAssetManager;
    private int mAssetType = NvAsset.ASSET_VIDEO_TRANSITION;

    private RecyclerView rv_transition_file, rv_transition_theme;
    private RadioGroup rg_transition_apply;

    private PreviewFragment.OnPlayListener listener;
    private TransitionFileAdapter fileAdapter;
    private List<String> pathList = new ArrayList<>();
    private TransitionThemeAdapter themeAdapter;
    private List<FilterItem> themeModels = new ArrayList<>();
    private HashMap<String, FilterItem> itemMap = new HashMap<>();
    private int applyType;
    private int linkPosition;
    private int videoPosition;
    private int themePosition;
    public static long TIME_BASE = 1000000;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_transition;
    }

    @Override
    public void initObject() {
        mAssetManager = NvAssetManager.sharedInstance(getActivity());
        mAssetManager.searchLocalAssets(mAssetType);
        String bundlePath = "transition";
        mAssetManager.searchReservedAssets(mAssetType, bundlePath);

        initTransitionDataList();
        initThumbRecyclerView();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(View v) {
        rootView = v;
        rv_transition_file = findViewById(R.id.rv_transition_file);
        rv_transition_theme = findViewById(R.id.rv_transition_theme);
        rg_transition_apply = findViewById(R.id.rg_transition_apply);


        fileAdapter = new TransitionFileAdapter(pathList, itemMap);
        rv_transition_file.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_transition_file.setAdapter(fileAdapter);

        themeAdapter = new TransitionThemeAdapter(themeModels);
        rv_transition_theme.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_transition_theme.setAdapter(themeAdapter);

    }

    @Override
    public void initEvents() {

        rg_transition_apply.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_transition_apply_current) {
                    applyType = 0;

                } else if (checkedId == R.id.rb_transition_apply_all) {
                    applyType = 1;

                }
            }
        });

        fileAdapter.setOnItemClickListener(new TransitionFileAdapter.OnItemClickListener() {
            @Override
            public void onClickThumbnail(int position) {
                videoPosition = position;
                if (TransitionActivity.mVideoTrack == null || TransitionActivity.mTimeline == null)
                    return;
                if (TransitionActivity.mVideoTrack.getClipCount() < position) return;
                NvsVideoClip clip = TransitionActivity.mVideoTrack.getClipByIndex(position);
                if (clip == null) return;
                if (listener != null)
                    listener.onPlayVideo(clip.getInPoint(), TransitionActivity.mTimeline.getDuration());
            }

            @Override
            public void onClickLink(int position) {
                linkPosition = position;
                if (TransitionActivity.mVideoTrack == null) return;
                playTransition(position);

            }
        });


        themeAdapter.setOnItemClickListener(new TransitionThemeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                themePosition = itemPosition;
                if (TransitionActivity.mVideoTrack == null) return;
                if (applyType == 0) {
                    itemMap.put(pathList.get(linkPosition), themeModels.get(itemPosition));
                    fileAdapter.notifyItemChanged(linkPosition);
                    setBuiltinTransition(linkPosition);
                    playTransition(linkPosition);
                } else {
                    for (int i = 0; i < pathList.size(); i++) {
                        setBuiltinTransition(i);
                        itemMap.put(pathList.get(i), themeModels.get(itemPosition));
                    }
                    fileAdapter.notifyDataSetChanged();
                    if (listener != null)
                        listener.onPlayVideo(0, TransitionActivity.mTimeline.getDuration());
                }
            }
        });
    }

    /**
     * 向时间线中插入转场
     *
     * @param position
     */
    private void setBuiltinTransition(int position) {
        if (themeModels == null || themeModels.size() < themePosition || TransitionActivity.mVideoTrack == null || TransitionActivity.mVideoTrack.getClipCount() < position + 1)
            return;
        FilterItem item = themeModels.get(themePosition);
        if (item == null) return;
        if (item.getFilterMode() == FilterItem.FILTERMODE_BUILTIN) {
            TransitionActivity.mVideoTrack.setBuiltinTransition(position, item.getFilterName());
        } else {
            TransitionActivity.mVideoTrack.setPackagedTransition(position, item.getPackageId());
        }
    }

    @Override
    public void OnClickEvents(View v) {
    }


    /**
     * 设置播放监听
     *
     * @param listener
     */
    public void setPlayListener(PreviewFragment.OnPlayListener listener) {
        this.listener = listener;
    }


    /**
     * 播放转场
     *
     * @param index
     */
    private void playTransition(int index) {
        int videoCount = TransitionActivity.mVideoTrack.getClipCount();
        if (videoCount < index + 1) return;

        NvsVideoClip clip = TransitionActivity.mVideoTrack.getClipByIndex(index);
        if (clip == null) return;

        long inPointBefore = clip.getInPoint();
        long outPointBefore = clip.getOutPoint();

        NvsVideoClip nextClip = TransitionActivity.mVideoTrack.getClipByIndex(index + 1);
        if (nextClip == null) return;

        long inPointNext = nextClip.getInPoint();
        long outPointNext = nextClip.getOutPoint();
        outPointBefore -= TIME_BASE;
        inPointNext += TIME_BASE;

        if (outPointBefore < inPointBefore) {
            outPointBefore = inPointBefore;
        }

        if (inPointNext > outPointNext) {
            inPointNext = outPointNext;
        }
        if (listener != null) listener.onPlayVideo(outPointBefore, inPointNext);
    }


    /**
     * 初始化转场数据
     */
    private void initTransitionDataList() {
        int[] resImages = {
                R.mipmap.icon_transition_fade, R.mipmap.icon_transition_turning, R.mipmap.icon_transition_swap, R.mipmap.icon_transition_stretch_in,
                R.mipmap.icon_transition_page_curl, R.mipmap.icon_transition_lens_flare, R.mipmap.icon_transition_star, R.mipmap.icon_transition_dip_to_black,
                R.mipmap.icon_transition_dip_to_white, R.mipmap.icon_transition_push_to_right, R.mipmap.icon_transition_push_to_left, R.mipmap.icon_transition_upper_left_into
        };

        themeModels.clear();
        FilterItem filterItem = new FilterItem();
        filterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
        filterItem.setFilterName(getString(R.string.no_info));
        filterItem.setImageId(R.mipmap.icon_transition_no);
        themeModels.add(filterItem);

        List<String> builtinTransitionList = TransitionActivity.mStreamingContext.getAllBuiltinVideoTransitionNames();
        for (int i = 0; i < builtinTransitionList.size(); i++) {
            String transitionName = builtinTransitionList.get(i);
            FilterItem newFilterItem = new FilterItem();
            newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
            newFilterItem.setFilterName(transitionName);
            if (i < resImages.length) {
                int imageId = resImages[i];
                newFilterItem.setImageId(imageId);
            }

            themeModels.add(newFilterItem);
        }

        ArrayList<NvAsset> transitionList = getLocalData();
        String bundlePath = "transition/info.txt";
        Util.getBundleFilterInfo(getActivity(), transitionList, bundlePath);

        int ratio = TimelineData.instance().getMakeRatio();
        for (NvAsset asset : transitionList) {
            if ((ratio & asset.aspectRatio) == 0) continue;

            FilterItem newFilterItem = new FilterItem();
            if (asset.isReserved) {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
            } else {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
            }
            newFilterItem.setFilterName(asset.name);
            newFilterItem.setPackageId(asset.uuid);
            newFilterItem.setImageUrl(asset.coverUrl);
            themeModels.add(newFilterItem);
        }
    }

    private ArrayList<NvAsset> getLocalData() {
        return mAssetManager.getUsableAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    private void initThumbRecyclerView() {
        if (TransitionActivity.mVideoTrack == null) return;
        for (int i = 0; i < TransitionActivity.mVideoTrack.getClipCount(); i++) {
            pathList.add(TransitionActivity.mVideoTrack.getClipByIndex(i).getFilePath());
        }
        fileAdapter.notifyDataSetChanged();
    }


    protected <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    public void t(@NonNull String msg) {
        Toast.makeText(getContext(), "" + msg, Toast.LENGTH_LONG).show();
    }

    public void i(@NonNull String msg) {
        Log.i(TAG, ">>>>>>>>" + msg);
    }

    public void e(@NonNull String msg) {
        Log.e(TAG, ">>>>>>>>" + msg);
    }


}
