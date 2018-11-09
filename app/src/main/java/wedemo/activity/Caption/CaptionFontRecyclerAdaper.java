package wedemo.activity.Caption;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sven.huinews.international.R;

import java.util.ArrayList;

import wedemo.activity.data.CaptionFontInfo;
import wedemo.activity.interfaces.OnItemClickListener;
import wedemo.utils.ScreenUtils;

/**
 * Created by admin on 2018/5/25.
 */

public class CaptionFontRecyclerAdaper extends RecyclerView.Adapter<CaptionFontRecyclerAdaper.ViewHolder> {
    private ArrayList<CaptionFontInfo> mFontInfoList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;

    public CaptionFontRecyclerAdaper(Context context) {
        mContext = context;
    }

    public void setFontInfoList(ArrayList<CaptionFontInfo> fontInfoList) {
        this.mFontInfoList = fontInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_asset_caption_font, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CaptionFontInfo fontInfo = mFontInfoList.get(position);
        if (0 == position) {
            ViewGroup.LayoutParams layoutParams = holder.mCaptinFontCover.getLayoutParams();
            layoutParams.width = ScreenUtils.dip2px(mContext, 25);
            layoutParams.height = layoutParams.width;
            holder.mCaptinFontCover.setLayoutParams(layoutParams);
        }
        holder.mCaptinFontCover.setImageResource(fontInfo.mImageRes);
        if (fontInfo.mSelected) {
            holder.mSelecteItem.setVisibility(View.VISIBLE);
        } else {
            holder.mSelecteItem.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFontInfoList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mCaptinFontCover;
        View mSelecteItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mCaptinFontCover = (ImageView) itemView.findViewById(R.id.captionFontAssetCover);
            mSelecteItem = itemView.findViewById(R.id.selectedItem);
        }
    }
}
