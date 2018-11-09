package wedemo.activity.download;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sven.huinews.international.R;

import java.io.File;
import java.util.ArrayList;

import wedemo.utils.RatioUtil;
import wedemo.utils.asset.NvAsset;

public class AssetDownloadListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NvAsset> mAssetDataList = new ArrayList<>();
    private Context mContext;
    //view type
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;

    //加载状态：LOADING--正在加载；LOADING_COMPLETE--加载完成;LOADING_END -- 加载到底
    public static final int LOADING = 1;
    public static final int LOADING_COMPLETE = 2;
    public static final int LOADING_FAILED = 3;
    public static final int LOADING_END = 4;
    //当前状态,默认加载完成
    private int currentLoadState = LOADING_COMPLETE;
    private OnDownloadClickListener mDownloadClickerListener = null;
    private int curTimelineRatio = 0;
    private int mAssetType = 0;

    public class DownloadButtonInfo {
        int buttonBackgroud;
        String buttonText;
        String buttonTextColor;

        public DownloadButtonInfo() {

        }
    }

    public AssetDownloadListAdapter(Context context) {
        mContext = context;
    }

    public void setAssetType(int assetType) {
        this.mAssetType = assetType;
    }

    public void setCurTimelineRatio(int curTimelineRatio) {
        this.curTimelineRatio = curTimelineRatio;
    }

    public void setAssetDatalist(ArrayList<NvAsset> assetDataList) {
        this.mAssetDataList = assetDataList;
        Log.e("Datalist", "DataCount = " + mAssetDataList.size());
    }

    public void setDownloadClickerListener(OnDownloadClickListener downloadClickerListener) {
        this.mDownloadClickerListener = downloadClickerListener;
    }

    public interface OnDownloadClickListener {
        void onItemDownloadClick(RecyclerViewHolder holder, int pos);
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_asset_download, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_asset_download_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            final NvAsset asset = mAssetDataList.get(position);
            //加载图片

            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.mipmap.bank_thumbnail_local);
            Glide.with(mContext)
                    .asBitmap()
                    .load(asset.coverUrl)
                    .apply(options)
                    .into(recyclerViewHolder.mAssetCover);

            recyclerViewHolder.mAssetName.setText(asset.name);
            //mAssetType = 4是贴纸，贴纸无场景区分
            recyclerViewHolder.mAssetRatio.setText(mAssetType == 4 ? "通用" : getAssetRatio(asset.aspectRatio));
            recyclerViewHolder.mAssetSize.setText(getAssetSize(asset.remotePackageSize));
            DownloadButtonInfo buttonInfo = getDownloadButtonInfo(asset);
            recyclerViewHolder.mDownloadButton.setBackgroundResource(buttonInfo.buttonBackgroud);
            recyclerViewHolder.mDownloadButton.setText(buttonInfo.buttonText);
            recyclerViewHolder.mDownloadButton.setTextColor(Color.parseColor(buttonInfo.buttonTextColor));
            recyclerViewHolder.mDownloadProgressBar.setVisibility(View.GONE);
            recyclerViewHolder.mDownloadButton.setVisibility(View.VISIBLE);
            if (asset.downloadStatus == NvAsset.DownloadStatusFailed) {
                recyclerViewHolder.mDownloadButton.setText("重试");
                recyclerViewHolder.mDownloadButton.setTextColor(Color.parseColor("#ffffffff"));
                recyclerViewHolder.mDownloadButton.setBackgroundResource(R.drawable.download_button_shape_corner_retry);
                recyclerViewHolder.mDownloadProgressBar.setVisibility(View.GONE);
                recyclerViewHolder.mDownloadButton.setVisibility(View.VISIBLE);
            } else if (asset.downloadStatus == NvAsset.DownloadStatusFinished) {
                recyclerViewHolder.mDownloadProgressBar.setVisibility(View.GONE);
                recyclerViewHolder.mDownloadButton.setVisibility(View.VISIBLE);
            } else if (asset.downloadStatus == NvAsset.DownloadStatusInProgress) {
                recyclerViewHolder.mDownloadProgressBar.setVisibility(View.VISIBLE);
                recyclerViewHolder.mDownloadProgressBar.setProgress(asset.downloadProgress);
                recyclerViewHolder.mDownloadButton.setVisibility(View.GONE);
            }
            recyclerViewHolder.mDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (curTimelineRatio >= 1 && mAssetType != 4) {
                        if (RatioUtil.ratioIsUnsuitable(curTimelineRatio, asset.aspectRatio))
                            return;
                    }

                    if (asset.isUsable() && !asset.hasUpdate())
                        return;
                    if (asset.isUsable() && asset.hasRemoteAsset() && asset.hasUpdate()) {
                        File file = new File(asset.localDirPath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    if (mDownloadClickerListener != null) {
                        mDownloadClickerListener.onItemDownloadClick(recyclerViewHolder, position);
                    }
                }
            });
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (currentLoadState) {
                case LOADING: // 正在加载
                    footViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footViewHolder.mLoadFailTips.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.mLoadLayout.setVisibility(View.INVISIBLE);
                    footViewHolder.mLoadFailTips.setVisibility(View.GONE);
                    break;
                case LOADING_FAILED:
                    footViewHolder.mLoadLayout.setVisibility(View.GONE);
                    footViewHolder.mLoadFailTips.setVisibility(View.VISIBLE);
                    break;
                case LOADING_END:
                    footViewHolder.mLoadLayout.setVisibility(View.GONE);
                    footViewHolder.mLoadFailTips.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAssetDataList.size() + 1;
    }

    private String getAssetRatio(int aspectRatio) {
        String assetRatio = null;
        switch (aspectRatio) {
            case 1:
                assetRatio = "16:9";
                break;
            case 2:
                assetRatio = "1:1";
                break;
            case 3:
                assetRatio = "16:9 1:1";
                break;
            case 4:
                assetRatio = "9:16";
                break;
            case 5:
                assetRatio = "16:9 9:16";
                break;
            case 6:
                assetRatio = "1:1 9:16";
                break;
            case 7:
                assetRatio = "16:9 1:1 9:16";
                break;
            case 8:
                assetRatio = "3:4";
                break;
            case 9:
                assetRatio = "16:9 3:4";
                break;
            case 10:
                assetRatio = "1:1 3:4";
                break;
            case 11:
                assetRatio = "16:9 1:1 3:4";
                break;
            case 12:
                assetRatio = "9:16 3:4";
                break;
            case 13:
                assetRatio = "16:9 9:16 3:4";
                break;
            case 14:
                assetRatio = "1:1 9:16 3:4";
                break;
            case 15:
                assetRatio = "通用";
                break;
            default:
                break;
        }
        return assetRatio;
    }

    private String getAssetSize(int assetSize) {
        int totalKbSize = assetSize / 1024;
        int mbSize = totalKbSize / 1024;
        int kbSize = totalKbSize % 1024;
        float tempSize = (float) (kbSize / 1024.0);
        String packageAssetSize;
        if (mbSize > 0) {
            tempSize = (mbSize + tempSize);
            packageAssetSize = String.format("%.1f", tempSize);
            packageAssetSize = packageAssetSize + "M";
        } else {
            packageAssetSize = String.format("%d", kbSize);
            packageAssetSize = packageAssetSize + "K";
        }
        return packageAssetSize;
    }

    private DownloadButtonInfo getDownloadButtonInfo(NvAsset asset) {
        DownloadButtonInfo buttonInfo = new DownloadButtonInfo();
        if (curTimelineRatio >= 1
                && mAssetType != 4
                && RatioUtil.ratioIsUnsuitable(curTimelineRatio, asset.aspectRatio)) {
            buttonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_finished;
            buttonInfo.buttonText = "不适配";
            buttonInfo.buttonTextColor = "#ff928c8c";
        } else if (!asset.isUsable() && asset.hasRemoteAsset()) {
            buttonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_download;
            buttonInfo.buttonText = "下载";
            buttonInfo.buttonTextColor = "#ffffffff";
        } else if (asset.isUsable() && !asset.hasUpdate()) {
            buttonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_finished;
            buttonInfo.buttonText = "已完成";
            buttonInfo.buttonTextColor = "#ff909293";
        } else if (asset.isUsable() && asset.hasRemoteAsset() && asset.hasUpdate()) {
            buttonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_update;
            buttonInfo.buttonText = "更新";
            buttonInfo.buttonTextColor = "#ffffffff";
        }
        return buttonInfo;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView mAssetCover;
        TextView mAssetName;
        TextView mAssetRatio;
        TextView mAssetSize;
        Button mDownloadButton;
        DownloadProgressBar mDownloadProgressBar;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mAssetCover = (ImageView) itemView.findViewById(R.id.assetCover);
            mAssetName = (TextView) itemView.findViewById(R.id.assetName);
            mAssetRatio = (TextView) itemView.findViewById(R.id.assetRatio);
            mAssetSize = (TextView) itemView.findViewById(R.id.assetSize);
            mDownloadButton = (Button) itemView.findViewById(R.id.download_button);
            mDownloadProgressBar = (DownloadProgressBar) itemView.findViewById(R.id.downloadProgressBar);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLoadLayout;
        FrameLayout mLoadFailTips;

        FootViewHolder(View itemView) {
            super(itemView);
            mLoadLayout = (LinearLayout) itemView.findViewById(R.id.loadLayout);
            mLoadFailTips = (FrameLayout) itemView.findViewById(R.id.loadFailTips);
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3,加载结束
     */
    public void setLoadState(int loadState) {
        this.currentLoadState = loadState;
        notifyDataSetChanged();
    }

    public void updateDownloadItems() {
        notifyDataSetChanged();
    }
}
