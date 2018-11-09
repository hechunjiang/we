package wedemo.activity.animatesticker;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.List;

import wedemo.activity.down.DownResponse;
import wedemo.activity.interfaces.OnItemClickListener;

/**
 * Created by admin on 2018/5/25.
 */

public class StickerRecycleViewAdaper extends RecyclerView.Adapter<StickerRecycleViewAdaper.ViewHolder> {
    private List<DownResponse.DataBean> mAssetList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;

    private int mSelectedPos = -1;

    public StickerRecycleViewAdaper(Context context) {
        mContext = context;
    }

    public void setAssetList(List<DownResponse.DataBean> assetList) {
        this.mAssetList = assetList;
        notifyDataSetChanged();
    }

    public void setSelectedPos(int selectedPos) {
        this.mSelectedPos = selectedPos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_asset_animatesticker, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DownResponse.DataBean asset = mAssetList.get(position);

        if (asset != null && !TextUtils.isEmpty(asset.getCover())) {
            Uri uri = Uri.parse(asset.getCover());
            holder.mStickerAssetCover.setImageURI(uri);
        } else {
            holder.mStickerAssetCover.setImageResource(R.mipmap.no);
            holder.pb_load.setVisibility(View.GONE);
        }

        if (asset.isDown()) {
            holder.iv_down.setVisibility(View.GONE);
        } else {
            holder.iv_down.setVisibility(View.VISIBLE);
        }

        if (asset.isOnLoad()) {
            holder.pb_load.setVisibility(View.VISIBLE);
            holder.mStickerAssetCover.setVisibility(View.GONE);
        } else {
            holder.pb_load.setVisibility(View.GONE);
            holder.mStickerAssetCover.setVisibility(View.VISIBLE);
        }

        holder.mSelecteItem.setVisibility(mSelectedPos == position ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
                if (mSelectedPos >= 0 && mSelectedPos == position)
                    return;
                mSelectedPos = position;
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAssetList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mStickerAssetCover;
        View mSelecteItem;
        ProgressBar pb_load;
        ImageView iv_down;

        public ViewHolder(View itemView) {
            super(itemView);
            mStickerAssetCover = (SimpleDraweeView) itemView.findViewById(R.id.stickerAssetCover);
            mSelecteItem = itemView.findViewById(R.id.selectedItem);
            pb_load = itemView.findViewById(R.id.pb_load);
            iv_down = itemView.findViewById(R.id.iv_down);
        }
    }
}
