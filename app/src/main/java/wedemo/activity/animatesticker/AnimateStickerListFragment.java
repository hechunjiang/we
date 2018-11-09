package wedemo.activity.animatesticker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.List;

import wedemo.activity.down.DownResponse;
import wedemo.activity.interfaces.OnItemClickListener;

public class AnimateStickerListFragment extends Fragment {
    private RecyclerView mAssetRecycleView;
    private StickerRecycleViewAdaper mStickerRecycleAdapter;
    private ImageView mCustomStickerAddButton;
    private List<DownResponse.DataBean> mAssetInfolist = new ArrayList<>();
    private AnimateStickerClickerListener mAnimateStickerClickerListener;

    public interface AnimateStickerClickerListener {
        void onFragmentLoadFinish();

        void onItemClick(View view, int pos);

        void onAddCustomSticker();
    }

    public void setAnimateStickerClickerListener(AnimateStickerClickerListener animateStickerClickerListener) {
        this.mAnimateStickerClickerListener = animateStickerClickerListener;
    }

    public void setAssetInfolist(List<DownResponse.DataBean> assetInfolist) {
        mAssetInfolist = assetInfolist;
        if (mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.setAssetList(assetInfolist);
    }

    public void setSelectedPos(int selectedPos) {
        if (mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.setSelectedPos(selectedPos);
    }

    public void notifyDataSetChanged() {
        if (mStickerRecycleAdapter != null)
            mStickerRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootParent = inflater.inflate(R.layout.animatesticker_asset_list_fragment, container, false);
        mCustomStickerAddButton = (ImageView) rootParent.findViewById(R.id.customStickerAddButton);
        mAssetRecycleView = (RecyclerView) rootParent.findViewById(R.id.assetRecycleView);
        return rootParent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAssetRecycleAdapter();
        mCustomStickerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimateStickerClickerListener != null) {
                    mAnimateStickerClickerListener.onAddCustomSticker();
                }
            }
        });

        if (mAnimateStickerClickerListener != null) {
            mAnimateStickerClickerListener.onFragmentLoadFinish();
        }
    }

    public void setCustomStickerButtonVisible(int visible) {
        if (mCustomStickerAddButton != null)
            mCustomStickerAddButton.setVisibility(visible);
    }

    private void initAssetRecycleAdapter() {
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false);
        mAssetRecycleView.setLayoutManager(layoutManager);
        mStickerRecycleAdapter = new StickerRecycleViewAdaper(getActivity());
        mStickerRecycleAdapter.setAssetList(mAssetInfolist);
        mAssetRecycleView.setAdapter(mStickerRecycleAdapter);
        //mAssetRecycleView.addItemDecoration(new SpaceItemDecoration(26, 14));
        mStickerRecycleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (mAnimateStickerClickerListener != null) {
                    mAnimateStickerClickerListener.onItemClick(view, pos);
                }
            }
        });
    }

}
