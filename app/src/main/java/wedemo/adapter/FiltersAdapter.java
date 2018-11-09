package wedemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sven.huinews.international.R;

import java.io.InputStream;
import java.util.ArrayList;

import wedemo.shot.bean.FilterItem;

/**
 * Created by xyp on 2017/11/12.
 */

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {
    private Context mContext;
    private Boolean mIsArface = false;
    private OnItemClickListener mClickListener;
    private ArrayList<FilterItem> mFilterDataList = new ArrayList<>();

    private int mSelectPos = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onSameItemClick();
    }

    public FiltersAdapter(Context context) {
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setFilterDataList(ArrayList<FilterItem> filterDataList) {
        this.mFilterDataList = filterDataList;
    }

    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_assetLayout;
        private ImageView item_assetImage;
        private TextView item_assetName;

        public ViewHolder(View view) {
            super(view);
            item_assetLayout = view.findViewById(R.id.layoutAsset);
            item_assetName = view.findViewById(R.id.nameAsset);
            item_assetImage = view.findViewById(R.id.imageAsset);
        }
    }

    public void isArface(Boolean isArface) {
        mIsArface = isArface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fx, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FilterItem itemData = mFilterDataList.get(position);
        if (itemData == null)
            return;

        String name = itemData.getFilterName();
        if (name != null && !mIsArface) {
            holder.item_assetName.setText(itemData.getEnglishName());
        }
        if (mIsArface) {
            holder.item_assetName.setText("");
        }

        int filterMode = itemData.getFilterMode();
        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
            int imageId = itemData.getImageId();
            if (imageId != 0)
                holder.item_assetImage.setImageResource(imageId);
        } else {
            String imageUrl = itemData.getImageUrl();
            if (imageUrl != null) {
                if (filterMode == FilterItem.FILTERMODE_BUNDLE) {
                    try {
                        InputStream inStream = mContext.getAssets().open(imageUrl);
                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        holder.item_assetImage.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //加载图片
                    RequestOptions options = new RequestOptions();
                    options.centerCrop();
                    options.placeholder(R.drawable.def_image);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(imageUrl)
                            .apply(options)
                            .into(holder.item_assetImage);
                }
            }
        }
        if (mSelectPos == position) {
            holder.item_assetLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_shape_select));
            holder.item_assetName.setTextColor(Color.parseColor("#EBBA5C"));
        } else {
            holder.item_assetLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_shape_unselect));
            holder.item_assetName.setTextColor(Color.parseColor("#ffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectPos == position) {
                    if (mClickListener != null) {
                        mClickListener.onSameItemClick();
                    }
                    return;
                }

                mSelectPos = position;
                notifyDataSetChanged();
                if (mClickListener != null) {
                    mClickListener.onItemClick(view, position);
                }
            }
        });
    }

    public void callItemClick(int position){
//        if (mSelectPos == position) {
//            if (mClickListener != null) {
//                mClickListener.onSameItemClick();
//            }
//            return;
//        }

        mSelectPos = position;
        notifyDataSetChanged();
        if (mClickListener != null) {
            mClickListener.onItemClick(null, position);
        }
    }

    @Override
    public int getItemCount() {
        return mFilterDataList.size();
    }
}
