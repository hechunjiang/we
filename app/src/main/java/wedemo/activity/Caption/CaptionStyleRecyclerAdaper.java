package wedemo.activity.Caption;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sven.huinews.international.R;

import java.util.ArrayList;

import wedemo.activity.interfaces.OnItemClickListener;
import wedemo.utils.asset.NvAsset;

/**
 * Created by admin on 2018/5/25.
 */

public class CaptionStyleRecyclerAdaper extends RecyclerView.Adapter<CaptionStyleRecyclerAdaper.ViewHolder> {
    private ArrayList<NvAsset> mAssetList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;

    public CaptionStyleRecyclerAdaper(Context context) {
        mContext = context;
    }

    private int mSelectedPos = 0;

    public void setAssetList(ArrayList<NvAsset> assetArrayList) {
        this.mAssetList = assetArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_asset_caption_style, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public void setSelectedPos(int selectedPos) {
        this.mSelectedPos = selectedPos;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final NvAsset asset = mAssetList.get(position);
        //加载图片
        if (position == 0) {
            holder.mStickerAssetCover.setImageResource(R.mipmap.captionstyle_no);
        } else {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.mipmap.package1);  //占位图
            Glide.with(mContext)
                    .asBitmap()
                    .load(asset.coverUrl)
                    .apply(options)
                    .into(holder.mStickerAssetCover);
        }

        holder.mCaptionStyleName.setText(asset.name);
        holder.mSelecteItem.setVisibility(mSelectedPos == position ? View.VISIBLE : View.GONE);
        holder.mCaptionStyleName.setTextColor(mSelectedPos == position ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ffffffff"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
                if (mSelectedPos == position)
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
        ImageView mStickerAssetCover;
        View mSelecteItem;
        TextView mCaptionStyleName;

        public ViewHolder(View itemView) {
            super(itemView);
            mStickerAssetCover = (ImageView) itemView.findViewById(R.id.captionStyleAssetCover);
            mSelecteItem = itemView.findViewById(R.id.selectedItem);
            mCaptionStyleName = (TextView) itemView.findViewById(R.id.captionStyleName);
        }
    }
}
