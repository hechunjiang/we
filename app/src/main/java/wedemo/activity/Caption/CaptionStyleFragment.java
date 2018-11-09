package wedemo.activity.Caption;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sven.huinews.international.R;

import java.util.ArrayList;

import wedemo.activity.adapter.SpaceItemDecoration;
import wedemo.activity.interfaces.OnItemClickListener;
import wedemo.utils.asset.NvAsset;

public class CaptionStyleFragment extends Fragment {
    private RecyclerView mCaptionStyleRecycleView;
    private CaptionStyleRecyclerAdaper mCaptionStyleRecycleAdapter;
    private ArrayList<NvAsset> mAssetInfolist = new ArrayList<>();
    private RelativeLayout mDownloadMoreCapionSytle;
    private LinearLayout mApplyToAll;
    private ImageView mApplyToAllImage;
    private TextView mApplyToAllText;
    private boolean mIsApplyToAll = false;
    private OnCaptionStyleListener mCaptionStyleListener;

    public interface OnCaptionStyleListener {
        void onFragmentLoadFinished();

        void OnDownloadCaptionStyle();

        void onItemClick(int pos);

        void onIsApplyToAll(boolean isApplyToAll);
    }

    public void setCaptionStyleListener(OnCaptionStyleListener captionStyleListener) {
        this.mCaptionStyleListener = captionStyleListener;
    }

    public void setSelectedPos(int selectedPos) {
        if (mCaptionStyleRecycleAdapter != null)
            mCaptionStyleRecycleAdapter.setSelectedPos(selectedPos);
    }

    public void setAssetInfolist(ArrayList<NvAsset> assetInfolist) {
        mAssetInfolist = assetInfolist;
        if (mCaptionStyleRecycleAdapter != null)
            mCaptionStyleRecycleAdapter.setAssetList(assetInfolist);
    }

    public void applyToAllCaption(boolean isApplyToAll) {
        mApplyToAllImage.setImageResource(isApplyToAll ? R.mipmap.applytoall : R.mipmap.unapplytoall);
        mApplyToAllText.setTextColor(isApplyToAll ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ff909293"));
        mIsApplyToAll = isApplyToAll;
    }

    public void notifyDataSetChanged() {
        if (mCaptionStyleRecycleAdapter != null)
            mCaptionStyleRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootParent = inflater.inflate(R.layout.caption_style_list_fragment, container, false);
        mDownloadMoreCapionSytle = (RelativeLayout) rootParent.findViewById(R.id.download_more);
        mCaptionStyleRecycleView = (RecyclerView) rootParent.findViewById(R.id.captionStyleRecycleView);
        mApplyToAll = (LinearLayout) rootParent.findViewById(R.id.applyToAll);
        mApplyToAllImage = (ImageView) rootParent.findViewById(R.id.applyToAllImage);
        mApplyToAllText = (TextView) rootParent.findViewById(R.id.applyToAllText);
        return rootParent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAssetRecycleAdapter();
        if (mCaptionStyleListener != null) {
            mCaptionStyleListener.onFragmentLoadFinished();
        }
    }

    private void initAssetRecycleAdapter() {
        mDownloadMoreCapionSytle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCaptionStyleListener != null) {
                    mCaptionStyleListener.OnDownloadCaptionStyle();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mCaptionStyleRecycleView.setLayoutManager(layoutManager);
        mCaptionStyleRecycleAdapter = new CaptionStyleRecyclerAdaper(getActivity());
        mCaptionStyleRecycleAdapter.setAssetList(mAssetInfolist);
        mCaptionStyleRecycleView.setAdapter(mCaptionStyleRecycleAdapter);
        mCaptionStyleRecycleView.addItemDecoration(new SpaceItemDecoration(0, 14));
        mCaptionStyleRecycleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (mCaptionStyleListener != null) {
                    mCaptionStyleListener.onItemClick(pos);
                }
            }
        });
        mApplyToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsApplyToAll = !mIsApplyToAll;
                applyToAllCaption(mIsApplyToAll);
                if (mCaptionStyleListener != null) {
                    mCaptionStyleListener.onIsApplyToAll(mIsApplyToAll);
                }
            }
        });
    }
}
